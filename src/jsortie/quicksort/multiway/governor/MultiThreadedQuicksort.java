package jsortie.quicksort.multiway.governor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import jsortie.RangeSorter;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;

public class MultiThreadedQuicksort 
  extends AdaptiveMultiPivotQuicksort {
  public MultiThreadedQuicksort
    ( MultiPivotSelector selector
    , MultiPivotPartitioner partitioner
    , RangeSorter janitor
    , int janitorThreshold
    , int mttThreshold) {
    super ( selector, partitioner
          , janitor, janitorThreshold);
    multiThreadingThreshold = mttThreshold;
  }
  public MultiThreadedQuicksort
    ( StandAlonePartitioner party
    , RangeSorter janitor
    , int janitorThreshold
    , int multiThreadingThreshold) {
    super ( party, janitor, janitorThreshold );
    this.multiThreadingThreshold 
      = multiThreadingThreshold;
  }
  AtomicInteger subTaskCount;
  AtomicInteger failureCount;
  int multiThreadingThreshold ;
  Object target = null;
  static ExecutorService executorService 
    = Executors.newFixedThreadPool(4);
  protected class SubTask implements Runnable {
    int[] vArray;
    int   lastBoundary, nextBoundary;
    int   start, stop, maxDepth;
    public SubTask 
      ( int[] vArray, int lastBoundary
      , int start, int stop
      , int nextBoundary, int maxDepth) {
      this.vArray = vArray;
      this.lastBoundary = lastBoundary;
      this.start = start;
      this.stop = stop;
      this.nextBoundary = nextBoundary;
      this.maxDepth = maxDepth;
      subTaskCount.incrementAndGet();
      executorService.execute(this);
    }
    @Override
    public void run() {
      boolean done = false;
      try {
        adaptiveSortRange
          ( vArray, lastBoundary, start, stop
          , nextBoundary, maxDepth);
        done = true;
      }
      catch (Throwable t) {
        failureCount.incrementAndGet();
        done = true;
      }
      finally {
        if (!done) {
          failureCount.incrementAndGet();
        }
        if (subTaskCount.decrementAndGet()==0) {
          synchronized (target) {
            target.notify();
          }
        }
      }
    }
  }
  @Override
  public String toString() {
    return getClass().getSimpleName() 
      + "( " + party.toString() 
      + ", " + janitor.toString() 
      + ", " + janitorThreshold 
      + ", " + lastResort.toString()+ " )";
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    if (start+1<stop) {
      int maxDepth 
        = (int) Math.floor
                ( Math.log(stop-start) 
                 /Math.log(2)*3);
      if (stop-start<multiThreadingThreshold) {
        super.adaptiveSortRange
          ( vArray, -1, start, stop, -1, maxDepth );
      } else {
        MultiThreadedQuicksort task 
          = new MultiThreadedQuicksort
                ( party, janitor, janitorThreshold
                , multiThreadingThreshold);
        task.subTaskCount = new AtomicInteger();
        task.failureCount = new AtomicInteger();
        task.target = new Object();
        task.adaptiveSortChildRange
          ( vArray, -1, start, stop, -1, maxDepth );
        try {
          if (task.subTaskCount.get()>0) {
            synchronized (task.target) {
              task.target.wait();
            }
          }
          if (task.failureCount.get() != 0 ) {
            super.sortRange(vArray, start, stop); 
            //do it again on current thread.
          }
        } 
        catch (InterruptedException e) {
          super.sortRange(vArray, start, stop); 
          //do it again on current thread
        }
      }
    }
  }
  @Override
  public void adaptiveSortChildRange 
    ( int[] vArray, int lastBoundary
    , int start, int stop
    , int nextBoundary, int maxDepth ) {
    if ( multiThreadingThreshold < stop-start 
         && target != null) {
      new SubTask ( vArray, lastBoundary
                  , start, stop
                  , nextBoundary, maxDepth-1 );
    } else {
      super.adaptiveSortRange
        ( vArray, lastBoundary
        , start, stop
        , nextBoundary, maxDepth-1 );
    }
  }
}
