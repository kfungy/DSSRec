package com.dss.job.recommender.scoring

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class JaccardSimilaritySpec extends FlatSpec with Matchers {

  val testSet1 = mutable.Set[Int](1,2,3,4)
  val testSet2 = mutable.Set[Int](3,4,5,6)
  val emptySet = mutable.Set[Int]()

  "JaccardSimilarity" should "return me a score given two Sets" in {
    val j = JaccardSimilarity.score(testSet1, testSet2)
    j should be (1.0/3)
  }

  "JaccardSimilarity" should "return me 1.0 if both sets are equal" in {
    val j = JaccardSimilarity.score(testSet1, testSet1)
    j should be (1.0)
  }

  "JaccardSimilarity" should "return me 0.0 if one set is empty" in {
    val j = JaccardSimilarity.score(emptySet, testSet1)
    j should be (0.0)
  }

  "JaccardSimilarity" should "return me 0.0 if both sets are empty" in {
    val j = JaccardSimilarity.score(emptySet, testSet1)
    j should be (0.0)
  }

  "JaccardSimilarity" should "Error if either input is null" in {
    a [Exception] should be thrownBy {
      JaccardSimilarity.score(null, testSet2)
    }
  }

  "JaccardSimilarity" should "Error if both inputs is null" in {
    a [Exception] should be thrownBy {
      JaccardSimilarity.score(null, null)
    }
  }

}
