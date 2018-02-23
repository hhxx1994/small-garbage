package com.hhx.house.service.recommend

import javax.annotation.PostConstruct

import com.hhx.house.mapping.UserTagMapper
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

  private var sparkContext: SparkContext = _

  private var matrixFactorizationModel: MatrixFactorizationModel = _

  def recommend(userId: Int, num: Int): Array[Rating] = {
    matrixFactorizationModel.recommendProducts(userId, num)
  }


  @PostConstruct
  private def init = {
    val sparkConf = new SparkConf().setAppName("HouseALS").setMaster("local[6]").set("spark.executor.memory", "2g")
    sparkContext = new SparkContext(sparkConf)
    trainData
  }

  @Scheduled(cron="0 0 3 1/1 * ?")
  private def trainData = {
    val ratings = userTagMapper.selectList(null).asScala.map(userTag => Rating(userTag.getUserId, userTag.getId, userTag.getScore.doubleValue()))
    val rdd = sparkContext.makeRDD(ratings)
    matrixFactorizationModel = ALS.train(rdd, 100, 10, 0.1)
  }


}
