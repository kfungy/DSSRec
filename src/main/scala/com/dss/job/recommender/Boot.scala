package com.dss.job.recommender

import com.dss.job.recommender.model.VideoRecommender
import com.dss.job.recommender.scoring.JaccardSimilarity

object Boot {

  def main(args: Array[String]) = {

    if (args.length < 2) {
      println("Must supply args: \narg0 = input csv file location \narg1 = location to save results")
      System.exit(0)
    }

    val t0 = System.nanoTime()

    val recommender = new VideoRecommender()
    recommender.fit(args(0))
    recommender.predict(JaccardSimilarity.score)
    recommender.writeResults(args(1))

    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) / 1000000000.0 + "s")
  }
}
