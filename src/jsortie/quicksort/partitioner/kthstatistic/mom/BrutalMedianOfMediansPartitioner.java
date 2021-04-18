package jsortie.quicksort.partitioner.kthstatistic.mom;

import jsortie.RangeSorter;
import jsortie.helper.RangeSortHelper;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.dirty.DirtySelectorHelper;

public class BrutalMedianOfMediansPartitioner 
  implements KthStatisticPartitioner, SinglePivotSelector {
  protected PartitionExpander   expander;
  protected RangeSorter         janitor = new InsertionSort();
  protected DirtySelectorHelper helper  = new DirtySelectorHelper();
  @Override public String toString() {
    return this.getClass().getSimpleName() 
            + "(" + expander.toString() + ")";
  }
  public BrutalMedianOfMediansPartitioner 
    ( PartitionExpander expanderToUse ) {
    this.expander    = expanderToUse;
  }
  @Override
  public int selectPivotIndex
    ( int[] vArray, int start, int stop ) {
    int count      = stop - start;
    int step       = count/5;
    step          -= ((step&1)==0) ? 1 : 0;
    int innerStart = start+step*2+(count%step)/2;
    int innerStop  = stop-step*2-(count%step)/2;
    sortGroupsOfFive(vArray, innerStart, innerStop, step);
    int candidate  = innerStart+step/2;
    partitionRangeExactly
      ( vArray, innerStart, innerStop, candidate);
    return candidate;
  }
  public void sortGroupsOfFive
    ( int[] vArray, int innerStart, int innerStop, int step) {
    int b = innerStart-step;
    int a = b-step;
    int d = innerStart+step;
    int e = d+step;
    for (int c=innerStart; c<innerStop; ++a, ++b, ++c, ++d, ++e) {
      compareAndSwapIntoOrder(vArray, a, b);
      compareAndSwapIntoOrder(vArray, d, e);
      int vA = vArray[a];
      int vB = vArray[b];
      int vD = vArray[d];
      int vE = vArray[e];
      boolean swap = ( vArray[d] < vArray[a] );
      vArray[a] = swap ? vD : vA;
      vArray[b] = swap ? vE : vB;
      vArray[d] = swap ? vA : vD;
      vArray[e] = swap ? vB : vE;
      if ( vArray[b] <= vArray[c] ) {
        compareAndSwapIntoOrder(vArray, c, d);
      } else if ( vArray[c] <= vArray[d]) {
        compareAndSwapIntoOrder(vArray, b, c);
      } else {
        RangeSortHelper.compareAndSwapIntoOrder(vArray, c, e);
      }
    }
  }
  public static void compareAndSwapIntoOrder
    ( int[] vArray, int x, int y ) {
    int vX = vArray[x];
    int vY = vArray[y];
    boolean swap = vY < vX;
    vArray[x] = swap ? vY : vX;
    vArray[y] = swap ? vX : vY;
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start
    , int stop, int target) {
    while ( 10 < stop-start ) {
      int split;
      int count      = stop - start;
      int step       = count/5;
      step          -= ((step&1)==0) ? 1 : 0;
      int innerStart = start+step*2+(count%step)/2;
      int innerStop  = stop -step*2-(count%step)/2;
      sortGroupsOfFive(vArray, innerStart, innerStop, step);
      int candidate  = innerStart+step/2;
      partitionRangeExactly 
        ( vArray, innerStart, innerStop, candidate);
      split = expander.expandPartition 
              ( vArray, start, innerStart
              , candidate, innerStop, stop);
      if (split < target) {
        start = split + 1 ;
        int vPivot = vArray[split]; //== 
        while (vArray[start]==vPivot && start<target) {
          ++start; 
        } //==
        if (target<start) {
          return;  //==
        }
      } else if ( target < split) {
        stop = split;
        int vPivot = vArray[split]; //==
        while (target<stop && vPivot==vArray[stop-1]) {
          --stop; 
        } //==
        if (stop<=target) { 
          return; //==
        }
      } else {
        return;
      }
    }
    janitor.sortRange( vArray, start, stop);
  }
}
