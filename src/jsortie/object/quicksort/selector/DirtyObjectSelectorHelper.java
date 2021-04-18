package jsortie.object.quicksort.selector;

import java.util.Comparator;

import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;

public class DirtyObjectSelectorHelper<T> {
  protected SinglePivotObjectPartitioner<T> leftPartitioner
    = new SingletonObjectPartitioner<T>();
  protected SinglePivotObjectPartitioner<T> rightPartitioner
    = new SingletonObjectPartitioner<T>();
  
  
  @SuppressWarnings("unchecked")
  public int partiallySortSlices
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop, int step
    , int innerStart, int innerStop) {
    int rowCountFloor = (stop-start-1) / step;
    T[] vSlice = (T[]) new Object[rowCountFloor+1];
    int comparisonCount = 0;
    for (int column=0;column<step;++column) {
      //Copy items from slice "column" into vSlice
      int w = 0;
      int r=start+column; 
      for (; r<stop; r+=step) {
        vSlice[w] = vArray[r];
        ++w;
      }
      int a = ( innerStart + step - 1 - start - column ) / step;
      int b = ( innerStop  + step - 1 - start - column ) / step;
      if (0<=a && a<w) {
        comparisonCount += 
          partitionRangeExactlyCountComparisons
          ( comparator, vSlice, 0, w, a );
        if (a+1<b && b-1<w) {
          partitionRangeExactlyCountComparisons
          ( comparator, vSlice, a+1, w, b-1 );
        }
      }
      //Copy partially sorted items from vSlice back 
      for (r-=step; start<=r; r-=step) {
        --w;
        vArray[r] = (T)vSlice[w];
      }
    }
    return comparisonCount;
  }
  public int partitionRangeExactlyCountComparisons
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop, int target) {
    int comparisonCount = 0;
    int originalStart   = start;
    int originalStop    = stop;
    int pivotIndex      = target;
    SinglePivotObjectPartitioner<T> partitioner 
      = leftPartitioner;
    while (1<stop-start) {
      int split = partitioner.partitionRange
                  ( comparator, vArray
                  , start, stop, pivotIndex );
      comparisonCount += (stop-start-1);
      T vPivot = vArray[split];
      if ( split < target) {
        if ( stop < originalStop ) {
          ++comparisonCount;
          if ( comparator.compare( vPivot, vArray[stop] ) == 0 ) {
            return comparisonCount;
          }
        }
        start        = split + 1;
        partitioner  = rightPartitioner;
      } else if (split==target) {
        return comparisonCount;
      } else {
        if ( originalStart < start ) {
          ++comparisonCount;
          if ( vArray[start-1] == vPivot ) {
            return comparisonCount;
          }
        }
        stop        = split;
        partitioner = leftPartitioner;
      }
      if (stop<=start) {
        return comparisonCount;
      }
    }
    return comparisonCount;
  }
   
}
