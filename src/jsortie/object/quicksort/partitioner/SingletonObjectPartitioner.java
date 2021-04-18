package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.quicksort.multiway.partitioner.FixedCountPivotObjectPartitioner;

public class SingletonObjectPartitioner<T>
  implements SinglePivotObjectPartitioner<T> 
           , FixedCountPivotObjectPartitioner<T> {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex ) {
    T v               = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start]      = v;
    int lhs           = start;
    int rhs           = stop;
    do { 
      --rhs; 
    } while (comparator.compare
             ( v, vArray[rhs] ) < 0 );
    do { 
      ++lhs; 
    } while (lhs<rhs && comparator.compare
                        ( vArray[lhs], v ) < 0 );
    while (lhs<rhs) {
      ObjectRangeSortHelper.swapElements
      ( vArray, lhs, rhs );
      do { 
        --rhs; 
      } while ( comparator.compare
                ( v,vArray[rhs] ) < 0 );
      if (lhs<rhs) {
        do {
          ++lhs;
        } while ( comparator.compare 
                  ( vArray[lhs], v ) < 0);
      }
    }	
    if (start<rhs) {
      vArray[start] = vArray[rhs]; 
      vArray[rhs] = v; 
    }
    return rhs;	
  }
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int[] pivotIndices) {
    int m = pivotIndices.length / 2;
    int p = partitionRange
            ( comparator, vArray, start, stop
            , pivotIndices[m]);
    return new int[] { start, p, p+1, stop };
  }
  public int getPivotCount() {
    return 1;
  }
}
