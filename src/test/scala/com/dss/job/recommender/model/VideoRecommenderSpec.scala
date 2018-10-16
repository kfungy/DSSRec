package com.dss.job.recommender.model

import com.dss.job.recommender.scoring.JaccardSimilarity
import org.scalatest.{FlatSpec, Matchers}

import scala.io.{BufferedSource, Source}

class VideoRecommenderSpec extends FlatSpec with Matchers {


  "fit" should "take in a csv file of two columns" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    v.fit("src/test/resources/user_video_watches_10k.csv")
    v.predict(JaccardSimilarity.score)
    v.writeResults("src/test/resources/" + outputFileName)

    val outputFile = Source.fromFile("src/test/resources/" + outputFileName)
    val outputFileSize = outputFile.getLines().size
    outputFile.close()
    outputFileSize should be (3715)

  }

  "fit 0" should "take in a csv file of zero rows of data" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    v.fit("src/test/resources/user_video_watches_0.csv")
    v.predict(JaccardSimilarity.score)
    v.writeResults("src/test/resources/" + outputFileName, writeHeader = false)

    val outputFile = Source.fromFile("src/test/resources/" + outputFileName)
    val outputFileSize = outputFile.getLines().size
    outputFile.close()
    outputFileSize should be (0)
  }

  "fit 1" should "take in a csv file of one row of data" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    v.fit("src/test/resources/user_video_watches_1.csv")
    v.predict(JaccardSimilarity.score)
    v.writeResults("src/test/resources/" + outputFileName, writeHeader = false)

    val outputFile = Source.fromFile("src/test/resources/" + outputFileName)
    val outputFileSize = outputFile.getLines().size
    outputFile.close()
    outputFileSize should be (0)
  }

  "fit 1 only header" should "take in a csv file of one row of data" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    v.fit("src/test/resources/user_video_watches_1.csv")
    v.predict(JaccardSimilarity.score)
    v.writeResults("src/test/resources/" + outputFileName, writeHeader = true)

    val outputFile = Source.fromFile("src/test/resources/" + outputFileName)
    val outputFileSize = outputFile.getLines().size
    outputFile.close()
    outputFileSize should be (1)
  }

  "fit 2" should "take in a csv file of two rows of data" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    v.fit("src/test/resources/user_video_watches_2.csv")
    v.predict(JaccardSimilarity.score)
    v.writeResults("src/test/resources/" + outputFileName , writeHeader = false)

    val outputFile = Source.fromFile("src/test/resources/" + outputFileName)
    val outputFileSize = outputFile.getLines().size
    outputFile.close()
    outputFileSize should be (2)
  }

  "bad fit 2" should "take a csv with a malformed row" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    v.fit("src/test/resources/user_video_watches_2_bad.csv")
    v.predict(JaccardSimilarity.score)
    v.writeResults("src/test/resources/" + outputFileName , writeHeader = false)

    val outputFile = Source.fromFile("src/test/resources/" + outputFileName)
    val outputFileSize = outputFile.getLines().size
    outputFile.close()
    outputFileSize should be (0)
  }

  "fit no header" should " take in a csv file of two rows of data" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    v.fit("src/test/resources/user_video_watches_2_no_header.csv",false)
    v.predict(JaccardSimilarity.score)
    v.writeResults("src/test/resources/" + outputFileName , writeHeader = false)

    val outputFile = Source.fromFile("src/test/resources/" + outputFileName)
    val outputFileSize = outputFile.getLines().size
    outputFile.close()
    outputFileSize should be (2)
  }

  "fit no file" should "exception if files does not exist" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    a [Exception] should be thrownBy {
      v.fit("does not eist")
    }
  }

  "cant write to destination" should "exception" in {
    lazy val outputFileName = System.currentTimeMillis() + ".log"

    val v = new VideoRecommender()
    v.fit("src/test/resources/user_video_watches_2_no_header.csv",false)
    v.predict(JaccardSimilarity.score)
    a [Exception] should be thrownBy {
      v.writeResults("Bad/test/Destination/" + outputFileName)
    }
  }

  "processLine" should "parse a csv of size 2" in {
    val v = new VideoRecommender()
    val res = v.processLine("1,17")
    res.get.userId should be (1)
    res.get.watchedVideoId should be ("17")
  }

  "processLine" should "not parse a csv of size != 2" in {
    val v = new VideoRecommender()
    val res = v.processLine("1,17,3")
    res should be (None)
  }

}
