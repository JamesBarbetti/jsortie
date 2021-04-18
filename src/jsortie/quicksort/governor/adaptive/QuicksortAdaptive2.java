package jsortie.quicksort.governor.adaptive;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.quicksort.governor.QuicksortBase;
import jsortie.quicksort.partitioner.PartiallySortedPartition;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;

public class QuicksortAdaptive2 extends QuicksortBase {
  protected RangeSorter lastResort;
  @Override
  public String toString() {
    return "QuicksortAdaptive2(" + selector.toString()
    + ", " + partitioner.toString() 
    + ", " + janitorThreshold 
    + ", " + janitor.toString() 
    + ", " + lastResort.toString() + ")";
  }
  public QuicksortAdaptive2(CleanSinglePivotSelector selector
    , SinglePivotPartitioner partitioner, RangeSorter janitor
    , int janitorThreshold, RangeSorter sortOfLastResort) {
    super(selector, partitioner, janitor, janitorThreshold);
    lastResort  = sortOfLastResort;
  }
  public void sortRange(int[] vArray, int start, int stop) {
    PartiallySortedPartition p = new PartiallySortedPartition();
    p.set( -1, start, start, stop, stop, -1 );
    if (p.start+1<p.stop) {
      double maxDepth = Math.log(stop-start)*3/Math.log(2)+1.0;
      sortRangeAdaptive(vArray, p , (int)Math.floor(maxDepth));
    }
  }
  private void sortRangeAdaptive ( int[] vArray
    , PartiallySortedPartition p, int maxDepth) {
    PartiallySortedPartition pLeft
      = new PartiallySortedPartition();
    PartiallySortedPartition pRight
     = new PartiallySortedPartition();
    while  ( 0<maxDepth 
             && janitorThreshold < p.stop-p.start) {
      zoomIn(vArray, p);
      if (p.isEmpty()) { 
        return; //early exit for entirely sorted subrange
      }
      int pivotIndex = selector.selectPivotIndex
                       (vArray, p.start, p.stop);
      int v          = vArray[pivotIndex];
      int left       = p.start;
      int right      = p.stop;
      
      if ( left<pivotIndex 
           && pivotIndex <= p.lastSortedOnLeft)  {
        left = pivotIndex;
        right = BinaryInsertionSort.findPreInsertionPoint
                (vArray, p.firstSortedOnRight, right, v);
      } else if ( pivotIndex<right-1 
                  && p.firstSortedOnRight <= pivotIndex) {
        left  = BinaryInsertionSort.findPostInsertionPoint
                (vArray, left, p.lastSortedOnLeft+1, v);
        right = pivotIndex + 1;
      } else  {
        left  = BinaryInsertionSort.findPostInsertionPoint
                (vArray, left, p.lastSortedOnLeft+1, v);
        right = BinaryInsertionSort.findPreInsertionPoint
                (vArray, p.firstSortedOnRight, right, v);
      }
      
      pivotIndex = partitioner.partitionRange
                   (vArray, left, right, pivotIndex); 
                   //where pivot landed
      
      left  = (left <= p.lastSortedOnLeft ) 
              ? (left-1)  : p.lastSortedOnLeft;
      if (left<p.start) left=p.start;
      if  ( p.pivotOnLeft!=-1 
              && v==vArray[p.pivotOnLeft]) {
        //if all items in left child partition equal
        pLeft.done(); 
      } else {
        int leftStop = pivotIndex;
        while ( left < leftStop && vArray[leftStop-1] == v) {
          --leftStop; 
        }
        pLeft.set ( p.pivotOnLeft, p.start, left 
                  , leftStop, leftStop, pivotIndex);    	  
      }
      
      right = (p.firstSortedOnRight<=right)
              ? (right+1) : p.firstSortedOnRight;
      if (p.stop<=right) right=p.stop-1;
      if ( p.pivotOnRight!=-1
           && v==vArray[p.pivotOnRight]) {
        //if all items in right child partition equal
        pRight.done(); 
      } else {
        int rightStart = pivotIndex+1;
        while ( rightStart < right 
                && vArray[rightStart]==v) { 
          ++ rightStart;
        }
        pRight.set ( pivotIndex, rightStart, rightStart
                   , right, p.stop, p.pivotOnRight);
      }
      
      if ( pLeft.stop - pLeft.start 
         < pRight.stop - pRight.start ) {
        sortRangeAdaptive(vArray, pLeft, maxDepth-1);
        p.copy(pRight);
      }	else {
        sortRangeAdaptive(vArray, pRight, maxDepth-1);
        p.copy(pLeft);
      }
      --maxDepth; //avoiding quadratic worst case
    }
		
    if (p.start+1<p.stop)  {
      if (0==maxDepth) { //avoiding quadratic worst case
        lastResort.sortRange(vArray, p.start, p.stop);
      } else {
        janitor.sortRange(vArray, p.start, p.stop);
      }
    }
  }
	
  public static void zoomIn
    ( int [] vArray, PartiallySortedPartition par) {			  
    while (par.lastSortedOnLeft < par.firstSortedOnRight
           && vArray[par.lastSortedOnLeft] 
              < vArray[par.lastSortedOnLeft + 1]) {
      ++par.lastSortedOnLeft;
    }
    if (par.lastSortedOnLeft == par.firstSortedOnRight) {
      par.start = par.stop = par.lastSortedOnLeft; 
      return;
    }
    while (par.lastSortedOnLeft + 1 < par.firstSortedOnRight
           && vArray[par.firstSortedOnRight - 1] 
              < vArray[par.firstSortedOnRight]) {
           --par.firstSortedOnRight;
    }
  }	
}
