package jsortie.quicksort.partitioner.bidirectional.traditional;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class SingletonPartitioner 
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  @Override
  public int partitionRange ( int vArray[], int start
                            , int stop, int pivotIndex) { 
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int lhs            = start;
    int rhs            = stop;
    //Find last value, v, from right, such that v <= vPivot
    do { 
      --rhs; 
    } while ( lhs < rhs && vPivot < vArray[rhs] ); 
    //Find first value, v, from left, such that vPivot <= v
    do {
      ++lhs; 
    } while ( lhs < rhs && vArray[lhs] < vPivot );
    if ( lhs < rhs ) {    	
      //Swap.  And find the next value (searching from the 
      //right), and the next value (searching from the left), 
      //that might need to be swapped.  Boundary checks 
      //*aren't* needed in the inner loop after the first 
      //such exchange (the two items involved in the first
      //exchange act as sentinels).
      do {
        int vTemp   = vArray[lhs];
        vArray[lhs] = vArray[rhs];
        vArray[rhs] = vTemp;
        do { 
          --rhs;
        } while ( vPivot < vArray[rhs] );
        if (rhs <= lhs) {
          break;
        }
        do {
          ++lhs;
        } while ( vArray[lhs] < vPivot );
      } while ( lhs < rhs );
    }
    vArray[start] = vArray[rhs]; 
    vArray[rhs]   = vPivot; 
    return rhs;
  }
}
