package jsortie.quicksort.multiway.partitioner.singlepivot;

import jsortie.helper.EgalitarianPartitionerHelper;

public class InertialPartitioner
  implements TernarySinglePivotPartitioner {	
  //This is a one-pivot, three-way partitioner, that returns two
  //partitions, one containing elements <= the pivot, one containing
  //elements >= the pivot.  Elements adjacent to the pivot with
  //the same value as the pivot are *not* included in either partition;
  //This partitioner is *based* on the Hoyos (two-way) partitioner.
  //Cases (in order of appearance):
  //  1. something in the left half of the range is > the pivot, and
  //     nothing right of it is < the pivot;
  //  2. something in the right half of the range is < the pivot, and
  //     nothing left of it is > the pivot;
  //  3. everything in the left half of the range is <= the pivot, and everything
  //     in the right half of the range is >= the pivot;
  //     
  //1-3 are the corner cases.  But usually we expect we'll find stuff
  //    that needs to be exchanged (>pivot on left, <pivot on right),
  //    so ... we do "Hoare-style" exchanging of pairs of such, moving
  //    the pointers inwards, until...the pointers meet or cross.
  //
  //  4.either there were equal elements in the middle, so the 
  //    pointers crossed over and differ by more than 1; or
  //  5.there weren't, so the pointers met and stopped, or "just"
  //    crossed over and differ by only 1.
  //
  //Depending on handleDuplicates, items equal to the pivot are "moved to
  //the centre in the following cases:
  //
  // handleDuplicates cases where we group duplicates
  // ================ ===============================
  // 0                cases 1 and 2
  // 1                cases 1,2 and 3
  // 2                cases 1,2,3, and 4
  // 3                cases 1,2,3,4, and 5
  //
  int handleDuplicates = 0;
  static EgalitarianPartitionerHelper helper 
    = new EgalitarianPartitionerHelper();
  public InertialPartitioner
    ( int levelOfDuplicateHandling ) {
    handleDuplicates = levelOfDuplicateHandling;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + handleDuplicates + ")";
  }  
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int lhs=start;
    int rhs=stop-1;
    int middlePivot=pivotIndices.length/2;
    int pivotIndex=pivotIndices[middlePivot];
    int vPivot=vArray[pivotIndex];
    boolean centering = false;
    for (; lhs<=rhs; ++lhs, --rhs) {
      if (vPivot<vArray[lhs]) {
        while ( vPivot <= vArray[rhs] && lhs<rhs ) {
          --rhs;
        }
        centering |= (lhs==rhs);
        break;
      } else if (vArray[rhs]<vPivot) {
        while ( vArray[lhs] <= vPivot && lhs<rhs ) {
          ++lhs;
        }
        centering |= (lhs==rhs);
        break;
      }
    }
    if (rhs<=lhs) {
      //Found nothing high that needed to go low, OR nothing low
      //that needed to go high. That's pretty suspicious!
      //Either (a) the input is mostly sorted, (b) most of the 
      //input consists mostly of items that compare equal to the pivot, or
      //(c) the pivot is the minimum or maximum of the range.
      //centering will already be true if:
      //  If we found one thing high that goes low, but nothing low; or
      //  If we found one thing low that goes high, but nothing high
      //But... If we found nothing left that goes right, AND
      //nothing that goes left, centering will still be false.
      if (0<handleDuplicates) {
        centering = true;
      }
    } else {
      do {
        int vTemp  = vArray[lhs];
        vArray[lhs] = vArray[rhs];
        vArray[rhs] = vTemp;
        do { 
          ++lhs; 
        } while ( vArray[lhs] <= vPivot);
        do { 
          --rhs; 
        } while ( vPivot <= vArray[rhs]);			
      } while (lhs<rhs);
    }     
    if (rhs+1<lhs || 2<handleDuplicates) {
      centering = true;
    }
    int scanLeft  = rhs; //first item that mightn't belong on left
    int scanRight = lhs; //last item that mightn't belong on right
    if (vArray[scanLeft]<vPivot)  ++scanLeft;  //in case they "just" crossed
    if (vPivot<vArray[scanRight]) --scanRight; //in case they "just" crossed
    if ( pivotIndex < scanLeft ) {
      --scanLeft;
      if (vArray[scanLeft] != vPivot) {
        vArray[pivotIndex] = vArray[scanLeft];
        vArray[scanLeft]   = vPivot;
      }
    } else if ( scanRight < pivotIndex ) {
      ++scanRight;
      if (vArray[scanRight] != vPivot) {
        vArray[pivotIndex]  = vArray[scanRight];
        vArray[scanRight]   = vPivot;
      }
    }
    if (centering) {
      scanLeft  
        = helper.swapEqualToRight
          ( vArray, start, scanLeft, vPivot);
      scanRight 
        = helper.swapEqualToLeft 
          ( vArray, scanRight+1, stop, vPivot )-1;
    }
    return new int[] 
      { start, scanLeft, scanRight+1, stop };
  }
  @Override
  public int getPivotCount() {
    return 1;
  }	
}
