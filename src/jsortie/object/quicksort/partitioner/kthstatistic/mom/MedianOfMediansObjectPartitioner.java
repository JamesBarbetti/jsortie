package jsortie.object.quicksort.partitioner.kthstatistic.mom;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.partitioner.kthstatistic.KthStatisticObjectPartitionerBase;
import jsortie.object.quicksort.selector.DirtyObjectSelectorHelper;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class MedianOfMediansObjectPartitioner<T> 
  extends KthStatisticObjectPartitionerBase<T>
  implements SinglePivotObjectSelector<T> {
  protected int g = 5;  //must be odd. Must be at least 5 
  protected DirtyObjectSelectorHelper<T> 
    helper = new DirtyObjectSelectorHelper<T>();
  double comparisonCount = 0;
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + g 
      + "," + leftExpander.toString() 
      + "," + rightExpander.toString() + ")";
  }
  public MedianOfMediansObjectPartitioner() {
    super();
    this.g = 5;
    this.janitorThreshold 
      = ( g < 10 ) ? 10 : g;
  }
  public MedianOfMediansObjectPartitioner(int gToUse) {
    if (gToUse<5) {
      gToUse=5; 
    } else if ((gToUse&1)==0) { 
      ++gToUse;
    }
    this.g = gToUse; 
    if ( janitorThreshold < g ) { 
      janitorThreshold = g; 
    }
  }
  public MedianOfMediansObjectPartitioner 
    ( int gToUse
    , PartitionObjectExpander<T> expanderToUseOnLeft
    , PartitionObjectExpander<T> expanderToUseOnRight) {
    if (gToUse<5) {
      gToUse=5; 
    } else if ((gToUse&1)==0) { 
      ++gToUse;
    }
    this.g = gToUse; 
    this.leftExpander  = expanderToUseOnLeft;
    this.rightExpander = expanderToUseOnRight;
    if ( janitorThreshold < g ) { 
      janitorThreshold = g; 
    }
  }  
  public void findMedianOf5
    ( Comparator<? super T> comparator
    , T[] vArray, int a, int b
    , int c, int d, int e) {
    //
    //At most 6 comparisons (on average: 5.6).
    //As per... https://www.ocf.berkeley.edu/~wwu/cgi-bin
    //          /yabb/YaBB.cgi?board=riddles_cs;
    //          action=display;num=1061827085
    //
    comparisonCount += 3; //next three lines
    ObjectRangeSortHelper.compareAndSwapIntoOrder(comparator, vArray, a, b);
    ObjectRangeSortHelper.compareAndSwapIntoOrder(comparator, vArray, d, e);
    if ( comparator.compare(vArray[d] , vArray[a] ) < 0 ) {
      ObjectRangeSortHelper.swapElements(vArray, a, d);
      ObjectRangeSortHelper.swapElements(vArray, b, e);
    }
    if ( comparator.compare(vArray[b] , vArray[c] ) <= 0 ) {
      ObjectRangeSortHelper.compareAndSwapIntoOrder
        ( comparator, vArray, c, d );
      comparisonCount += 2;
    } else if ( comparator.compare(vArray[c], vArray[d]) <= 0 ) {
      ObjectRangeSortHelper.compareAndSwapIntoOrder
        ( comparator, vArray, b, c );
      comparisonCount += 3;
    } else {
      ObjectRangeSortHelper.compareAndSwapIntoOrder
        ( comparator, vArray, c, e );
      comparisonCount += 3;
    }
  }
  @Override
  public int selectPivotIndex
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    int count      = stop - start;
    int step       = count/g;
    step          += ((step&1)==0) ? 1 : 0;
    int twoStep    = step + step;
    int innerStart = start+step*(g/2)+(count%step)/2;
    int innerStop  = innerStart+step;
    if ( g==5 ) {
      //5.6m comparisons
      int iStop = innerStop;
      if ( stop-twoStep <= iStop ) {
        iStop = stop - twoStep;
      }
      for (int i=innerStart; i<iStop; ++i) {
        findMedianOf5 
          ( comparator, vArray
          , i - twoStep, i-step
          , i, i+step, i+twoStep );
      }
    } else {
      comparisonCount +=
        helper.partiallySortSlices
          ( comparator, vArray
          , start, stop, step
          , innerStart, innerStop );
    }
    int candidate  = innerStart+step/2;
    partitionRangeExactly 
      ( comparator, vArray
      , innerStart, innerStop, candidate);
    return candidate;
  }
  @Override
  public void partitionRangeExactly 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int target) {
    partitionRangeExactly
      ( comparator, vArray, start, start, target
      , stop, stop, leftExpander);
  }
  public void partitionRangeExactly 
    ( Comparator<? super T> comparator
    , T[] vArray, int outerStart, int start
    , int target, int stop, int outerStop
    , PartitionObjectExpander<T> expander) {
    while ( janitorThreshold < stop-start ) {
      //
      //if g=5, 
      //   average: 2.12m comparisons at each level; and
      //   problem size reduced by 0.3, so total
      //   expected comparison: bounded above by
      //   7.07m (2.12 / 0.3), using a single pivot
      //   partitioner (e.g. via PretendExpander)
      //
      //   or: 1.92m comparisons at each level (using
      //   an expander reduces it by 0.2m),
      //   bounded *above* by 6.4m (with a partition
      //   expander).
      //
      //   worst case (with expander):
      //   2m comparisons at each level each pass; and
      //   problem size reduced by 0.1, so total
      //   worst-case comparisons bounded above by 20m.
      //   worst case (with partitioner):
      //   bound 2.2m at each level, so total below 22m.
      //
      int pivotIndex 
        = selectPivotIndex
          ( comparator, vArray, start, stop );
      //This bit has to match the 
      //expander.corresponding bit at the start
      //of selectPivotIndex exactly (count, step, 
      //innerStart, and innerStop have to be the same).
      int count      = stop - start;
      int step       = count/g;
      step          += ((step&1)==0) ? 1 : 0;
      int innerStart = start+step*(g/2)+(count%step)/2;
      int innerStop  = innerStart+step;
      int split = expander.expandPartition 
              ( comparator, vArray, start, innerStart
              , pivotIndex, innerStop, stop);
      comparisonCount += 
          (innerStart-start) +
          (stop-innerStop);
      T vPivot = vArray[split]; 
      if (split < target) {
        if ( outerStart<start ) {
          ++comparisonCount;
          if ( comparator.compare
               ( vArray[start-1], vPivot ) == 0 ) {
            //every item in vArray[start-1..split]
            //must be ==vPivot.
            return;
          }
        }
        start = split + 1 ;
        expander = rightExpander;
      } else if ( target < split) {
        if ( stop<outerStop ) { 
          ++comparisonCount;
          if ( comparator.compare
               ( vArray[stop] , vPivot) == 0 ) {
            //every item in vArray[split..stop]
            //must be ==vPivot.
            return;
          }
        }
        stop = split;
        expander = leftExpander;
      } else {
        return;
      }
    }
    janitor.partitionRangeExactly
      ( comparator, vArray
      , start, stop, target);
    comparisonCount += 
      (stop-start) * (stop-start-1) / 2;
      //Upper bound! Might well be less.
  }
  public double getComparisonCount() {
    return comparisonCount;
  }
  public void setComparisonCount(double i) {
    i = comparisonCount;
  }
}
