package com.dss.job.recommender.model

import java.io._

import com.dss.job.recommender.collections.MinHeap

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.{BufferedSource, Source}

case class RecommendedVideos(watchedBy: mutable.Set[Int] = mutable.Set[Int](), scores: MinHeap = new MinHeap())
case class VideoWatchedCsvInput(userId: Int, watchedVideoId: String)

/**
  * Collaborative Filtering using Jaccard's Similarity
  *
  * Reads a csv file with format [userId],[watched video id]
  * Fits to a Map with key=VideoId and Value = RecommendedVideos
  *   RecommendedVideos == tuple with _1 = Set of userIds that watched the video
  *                                   _2 = min heap of Jaccard scores and the associated recommended video
  * Compute jaccard score on the fitted Map using Futures for parallel execution
  */
class VideoRecommender {

  private var videosHm = mutable.Map[String, RecommendedVideos]()
  /**
    * Sets up a Map with key = video id and value=VideoRecommendations which will contain the recommendations and scores
    * @param src Input csv file. First col should be user Id. Second col is video Id
    * @param skipHeader If set to true the header line gets skipped
    * @return returns the map to be computed by predict
    */
  def fit (src: String, skipHeader: Boolean = true) = {
    var buffSrc: BufferedSource = null
    try {
      buffSrc = Source.fromFile(src)
      val buffItr = skipHeader match {
        case true => buffSrc.getLines().drop(1)
        case false => buffSrc.getLines()
      }
      for (line <- buffItr) {
        processLine(line) match {
          case Some(row) => {
            var videoInfo = videosHm.get(row.watchedVideoId) match {
              case Some(video) => video.watchedBy += row.userId
              case None => { var tmp = new RecommendedVideos()
                tmp.watchedBy += row.userId
                videosHm.put(row.watchedVideoId, tmp)
              }
            }
          }
          case None => println("Skipping invalid row: \"" + line + "\"")
        }
      }
    } catch {
      case e: Exception => throw new Exception(e)
    } finally {
      if (buffSrc != null) {
        buffSrc.close()
      }
    }
  }

  /**
    * After fitting the dataset, compute jaccard similarity for recommendations
    * @param scoreAlgo Function to compute scoring
    * @return Map with scoring completed by scoreAlgo
    */
  def predict (scoreAlgo: (mutable.Set[Int], mutable.Set[Int]) => Double) = {
    var futureCalcs = videosHm.map(x => Future {
      var curVideo = x._1
      var curVideoScore = x._2.scores
      var curVideoWatchedBy = x._2.watchedBy

      for (compareVideo <- videosHm) {
        var tmpVideo = compareVideo._1
        var tmpVideoWatchedBy = compareVideo._2.watchedBy
        if (curVideo != tmpVideo) {
          var rating = scoreAlgo(curVideoWatchedBy, tmpVideoWatchedBy)
          curVideoScore.enqueue((rating, tmpVideo))
        }
      }
    }).toSeq
    Await.result(Future.sequence(futureCalcs), Duration.Inf)
  }

  /**
    * Writes the three recommendations to a csv in format [video id], [recommended video], [score]
    * @param dst destination to write the file to
    */
  def writeResults (dst: String, writeHeader: Boolean = true)= {
    var bw:BufferedWriter = null

    try {
      bw = new BufferedWriter(new FileWriter(new File(dst)))
      if (writeHeader) {
        bw.write("video_id,recommended_video_id,score")
        bw.newLine()
      }

      for (row <- videosHm) {
        var videoId = row._1
        while (!row._2.scores.isEmpty()) {
          val recommendation = row._2.scores.dequeue()
          val toWrite = Array(videoId, recommendation._2, recommendation._1).mkString(",")
          bw.write(toWrite)
          bw.newLine()
        }
      }
    } catch {
      case e:Exception => throw new Exception(e)
    } finally {
      if (bw!=null) {
        bw.close()
      }
    }
  }

  /**
    * Takes a string representation of a line in a csv file and parse it into a VideoWatchedCsvInput object
    * @param line String representing the row of line from a csv string
    * @return VideoWatchedCsvInput parsed from csv string line
    */
  def processLine(line: String): Option[VideoWatchedCsvInput] = {
    val cols = line.split(",").map(_.trim)
    if (cols.size == 2) {
      val user = cols(0).toInt
      val video = cols(1)
      Some(new VideoWatchedCsvInput(user, video))
    }else{
      None
    }
  }

}
