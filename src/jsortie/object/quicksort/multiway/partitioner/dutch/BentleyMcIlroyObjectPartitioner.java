package jsortie.object.quicksort.multiway.partitioner.dutch;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;

public class BentleyMcIlroyObjectPartitioner<T> 
  implements MultiPivotObjectPartitioner<T> {
    @Override public String toString() {
      return this.getClass().getSimpleName();
    }
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int[] pivotIndices) {
    int pivotIndex
      = pivotIndices[pivotIndices.length/2];
    T  vPivot
      = (T) vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start] = vPivot; //needed as a sentinel for the first scan from the right
    int low  = start+1; //where the next value == vPivot will go on left
    int lhs  = start+1; //the left scanning pointer
    int rhs  = stop-1;  //the right scanning pointer
    int high = stop-1;  //where the next value == vPivot will go on right
    int leftComp;
    int rightComp;
    while ( ( rightComp 
              = comparator.compare
                ( vPivot , (T) vArray[rhs] ) ) < 0 ) {
      --rhs;
    }
    if (rhs==start) {
      //early bail out: pivot is the minimum, no duplicates 
      return new int[] { start+1, stop };
    }
    while ( ( leftComp
              = comparator.compare
                ( (T) vArray[lhs], vPivot) )<0 
             && lhs<rhs ) {
      ++lhs;
    }
    if (lhs==rhs) {
      //early bail out: array[start+1..lhs-1] < pivot, 
      //array[lhs] <= pivot, array[lhs+1..stop-1] > pivot
      vArray[start] = vArray[lhs];
      vArray[lhs] = vPivot;
      return new int[] { start, lhs, lhs+1, stop };
    }
    do {
      ObjectRangeSortHelper.swapElements(vArray, lhs, rhs); 
      //swap the (possibly equal) elements at lhs and rhs
      if ( rightComp==0) { 
        //element that was on the right (but is now on the right) 
        //compared equal to pivot
        ObjectRangeSortHelper.swapElements(vArray, low, lhs); 
        //send left equal to low
        ++low;
      }
      if ( leftComp==0) { 
        //element that was on the left compared equal to the pivot
        ObjectRangeSortHelper.swapElements(vArray, rhs, high); 
        //send right equal to high
        --high;
      }
      do { 
        ++lhs; 
      } while ( ( leftComp
                  = comparator.compare
                    ( (T) vArray[lhs],vPivot ) ) < 0 );
      //find next value >= vPivot from left
      do {
        --rhs; 
      } while ( ( rightComp 
                  = comparator.compare
                    ( vPivot,(T) vArray[rhs] ) ) < 0 ); 
      //find next value <= vPivot from right
    } while (lhs<rhs);
    //RangeSortHelper.dumpRange("BMI v=" + vPivot 
    //+ ", low=" + low + ", rhs=" + rhs 
    //+ ", lhs=" + lhs + ", high=" + high 
    //+ " ", vArray, start, stop );
    int dupesOnLeft  = low-start;
    int dupesOnRight = stop-high-1;		
    moveDupesToBack(vArray, start, lhs-start, dupesOnLeft);
    moveDupesToFront(vArray, lhs,   stop-lhs,  dupesOnRight);
    return new int[] { 
        start, lhs-dupesOnLeft, lhs+dupesOnRight, stop };
  }
  private void moveDupesToBack
    ( Object[] vArray, int lhs, int count
    , int dupesOnLeft) {
    //assumes: dupesOnRight <= count
    if (count/2 < dupesOnLeft ) {
      dupesOnLeft = count - dupesOnLeft;
    }
    if (count<1) {
      return;
    }
    
    int rhs = lhs+count-1;
    Object vLifted = vArray[rhs];
    int stopLeft = lhs+dupesOnLeft;
    while (lhs<stopLeft) {
      vArray[rhs] = vArray[lhs];
      --rhs;
      vArray[lhs]  = vArray[rhs];
      ++lhs; 
    }
    vArray[rhs] = vLifted;
  }
  private void moveDupesToFront
    ( Object[] vArray, int lhs, int count
    , int dupesOnRight) {
    //assumes: dupesOnRight <= count
    if (count/2 < dupesOnRight ) {
      dupesOnRight = count - dupesOnRight;
    }
    if (count<1) {
      return;
    }
    int rhs = lhs+count-1;
    Object vLifted = vArray[lhs];
    int stopRight = rhs-dupesOnRight;
    while (stopRight<rhs) {
      vArray[lhs] = vArray[rhs];
      ++lhs;
      vArray[rhs] = vArray[lhs];
      --rhs;
    }
    vArray[lhs] = vLifted;
  }
}
