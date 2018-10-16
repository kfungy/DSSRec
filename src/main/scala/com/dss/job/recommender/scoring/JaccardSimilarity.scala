package com.dss.job.recommender.scoring

import scala.collection.mutable

object JaccardSimilarity {

  /**
    * Computes the jaccard similarity given two Sets
    * @param a Set 1
    * @param b Set 2
    * @return jaccard similarity score
    */
  def score(a: mutable.Set[Int], b: mutable.Set[Int]): Double = {
    if(a == null || b == null){
      throw new Exception("Set(s) cannot be null")
    }

    var sizeSetA = a.size
    var sizeSetB = b.size

    if (sizeSetA == 0 || sizeSetB == 0) {
      0.0
    } else {
      val intersection = a.intersect(b).size.toDouble
      intersection / ((sizeSetA + sizeSetB) - intersection)
    }
  }
}
