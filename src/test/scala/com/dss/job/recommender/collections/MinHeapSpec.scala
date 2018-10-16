package com.dss.job.recommender.collections

import org.scalatest.{FlatSpec, Matchers}

class MinHeapSpec extends FlatSpec with Matchers {

  val testData = Array( (0.12,"The Notebook"), (0.81, "Batman"), (0.9, "SuperMan"), (0.8, "Pokemon"))

  "MinHeap" should "contain only up to three elements" in {
    val mh = new MinHeap()
    testData.map(x => mh.enqueue(x))
    var counter = 0
    while (!mh.isEmpty()) {
      mh.dequeue()
      counter+=1
    }
    counter should be (3)
  }

  "MinHeap" should "dq elements based on the lowest score" in {
    val mh = new MinHeap()
    testData.map(x => mh.enqueue(x))
    mh.dequeue()._2 should be ("Pokemon")
    mh.dequeue()._2 should be ("Batman")
    mh.dequeue()._2 should be ("SuperMan")
  }

  "MinHeap" should "be empty if nothing is added to it" in {
    val mh = new MinHeap()
    mh.isEmpty() should be (true)
  }

  "MinHeap" should "not be empty if something is added to it" in {
    val mh = new MinHeap()
    testData.map(x => mh.enqueue(x))
    mh.isEmpty() should be (false)
  }

  "MinHeapOrdering" should "not be empty if something is added to it" in {
    MinHeapOrdering.compare(testData(0), testData(1)) should be (1)
    MinHeapOrdering.compare(testData(1), testData(0)) should be (-1)
    MinHeapOrdering.compare(testData(0), testData(0)) should be (0)
  }

  "MinHeapOrdering" should "Exception if we try to dequeue with an empty queue" in {
    val mh = new MinHeap()
    a [Exception] should be thrownBy {
      mh.dequeue()
    }
  }

}
