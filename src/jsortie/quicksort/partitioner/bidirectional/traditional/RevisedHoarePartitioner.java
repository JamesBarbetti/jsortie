package jsortie.quicksort.partitioner.bidirectional.traditional;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class RevisedHoarePartitioner 
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  @Override
  public int partitionRange 
    ( int[] vArray, int start
    , int stop, int pivotIndex) { 
    int vPivot = vArray[pivotIndex];
    
    int lhs = start-1;
    do {
      ++lhs;
    } while ( lhs<stop 
              && vArray[lhs]<=vPivot );
    int rhs = stop;
    do {
      --rhs;
    } while ( start<=rhs 
              && vPivot<=vArray[rhs] );
    if ( lhs==stop || rhs<start )
    {
      int rc;
      if (lhs==stop) {
        if (rhs<start) {
          //all must be equal move pivot to middle
          rc = choosePreferredPivot(start, stop);
          vArray[pivotIndex] = vArray[rc];
          vArray[rc]         = vPivot;
          return rc;
          
        } else {
          //all must be <= vPivot;
          //swap pivot with last value < vPivot
          rc = ( pivotIndex < rhs ) ? rhs : ( stop - 1 );
        }
      } else {
        //all must be >= vPivot; 
        //swap pivot with first value > vPivot 
        rc = (lhs<pivotIndex) ? lhs : start;
      }
      vArray[pivotIndex] = vArray[rc];
      vArray[rc]         = vPivot;
      return rc;
    }
    
    if (lhs<pivotIndex) {
      vArray[pivotIndex] = vArray[lhs];
      vArray[lhs]        = vPivot;
      pivotIndex         = lhs;
    } else {
      --lhs;
    }
    
    do {
      vArray[pivotIndex] = vArray[rhs];
      vArray[rhs]        = vPivot;
      pivotIndex         = rhs;   
      
      do {
        ++lhs;
      } while ( vArray[lhs] < vPivot );
      if (rhs<=lhs) {
        break;
      }
      vArray[pivotIndex] = vArray[lhs];
      vArray[lhs]        = vPivot;
      pivotIndex         = lhs;
      
      do {
        --rhs;	
      } while ( vPivot < vArray[rhs] );
    } while (lhs<rhs);
    return pivotIndex;
  }
  public int choosePreferredPivot
    ( int start, int stop ) {
    return start + (stop-start) / 2;
  }
}
