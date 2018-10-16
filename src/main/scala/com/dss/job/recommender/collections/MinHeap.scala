package com.dss.job.recommender.collections

/**
  * Min Heap. Heap will not expand more than three elements. We are keeping track of recommended videos and its score
  */
class MinHeap {
  private val h = scala.collection.mutable.PriorityQueue.empty(MinHeapOrdering)
  val maxSize = 3

  def enqueue(elems: (Double, String)): Unit = {
    if (h.size == maxSize) {
      if (h.head._1 < elems._1) {
        h.dequeue()
        h.enqueue(elems)
      }
    } else {
      h.enqueue(elems)
    }
  }

  def dequeue() = {
    h.dequeue()
  }

  def isEmpty() = {
    h.isEmpty
  }
}

object MinHeapOrdering extends Ordering[(Double, String)] {
  /**
    * Customize ordering so that the the lowest score will get dequeued first
    * if y > x return 1
    * if x > y return -1
    * if x == y return 0
    */
  def compare(x: (Double,String), y: (Double, String)) = {
    y._1 compare x._1
  }
}
