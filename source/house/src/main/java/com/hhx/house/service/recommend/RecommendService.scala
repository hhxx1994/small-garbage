package com.hhx.house.service.recommend

import javax.annotation.PostConstruct

import com.hhx.house.mapping.{UserMapper, UserTagMapper}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._


/**
  * @author hhx 
  * @since 2018/2/22 17:02 
  *
  **/
@Service
class RecommendService {

  @Autowired
  private var userTagMapper: UserTagMapper = _

  @Autowired
  private var userMapper: UserMapper = _

  private var sparkContext: SparkContext = _

  private var matrixFactorizationModel: MatrixFactorizationModel = _

  def recommend(userId: Int, num: Int): Array[Rating] = {
    matrixFactorizationModel.recommendProducts(userId, num)
  }

  def recommendProductsForUsers(num: Int): Array[Rating] = {
    matrixFactorizationModel.recommendProductsForUsers(num).flatMap(_._2).top(num)(Ordering.by(_.rating))
  }


  @PostConstruct
  private def init = {
    val sparkConf = new SparkConf().setAppName("HouseALS").setMaster("local[4]").set("spark.executor.memory", "2g")
    sparkContext = new SparkContext(sparkConf)
    trainData
  }

  /**
    * 5分钟训练一次
    */
  @Scheduled(cron = "0 0/3 * * * ?")                               
  private def trainData = {
    val tags = userTagMapper.getTrainData
    val ratings = tags.asScala.map(tag => Rating(tag.getUserId, tag.getId, tag.getCount + tag.getScore))
    val rdd = sparkContext.makeRDD(ratings)
    matrixFactorizationModel = ALS.train(rdd, 100, 10, 0.1)
    userMapper.updateBatch(tags.asScala.groupBy(_.getUserId).keys.toList.asJavaCollection)
  }
}
