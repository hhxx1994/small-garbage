package com.hhx.house.service.recommend

import com.hhx.house.mapping.UserTagMapper
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired
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
    getModel.recommendProducts(userId, num)
  }

  def getSparkContext: SparkContext = {
    if (sparkContext == null) {
      val sparkConf = new SparkConf().setAppName("MovieLensALS").setMaster("local[10]").set("spark.executor.memory", "2g")
      sparkContext = new SparkContext(sparkConf)
    }
    sparkContext
  }

  def getModel: MatrixFactorizationModel = {
    if (matrixFactorizationModel == null) {
      val ratings = userTagMapper.selectList(null).asScala.map(userTag => Rating(userTag.getUserId, userTag.getId, userTag.getScore.doubleValue()))
      val rdd = getSparkContext.makeRDD(ratings)
      matrixFactorizationModel = ALS.train(rdd, 100, 10, 0.1)
    }
    matrixFactorizationModel
  }

}
