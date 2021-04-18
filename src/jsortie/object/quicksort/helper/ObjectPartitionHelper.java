package jsortie.object.quicksort.helper;

import java.util.Arrays;
import java.util.Comparator;

import jsortie.exception.SortingFailureException;

public class ObjectPartitionHelper<T> {
  public int swapEqualToLeft
    ( Comparator<? super T> comp
    , T[] vArray, int start, int stop
    , T vPivot) {
    if (stop<=start) return start;
    int lhs = start;
    int rhs = stop-1;
    while ( lhs < stop && 
            comp.compare(vArray[lhs],vPivot )<= 0) {
      ++lhs;
    }
    while ( lhs < rhs  && 
            comp.compare(vPivot, vArray[rhs]  ) < 0 ) {
      --rhs;
    }
    while ( lhs < rhs) {
      T vTemp = vArray[lhs];
      vArray[lhs] = vArray[rhs];
      vArray[rhs] = vTemp;
      do {
        ++lhs;
      } while (comp.compare(vArray[lhs], vPivot)<= 0);
      do {
        --rhs;
      } while (comp.compare(vPivot, vArray[rhs]) < 0);
    }
    return lhs;
  }	
  public int swapEqualToRight
    ( Comparator<? super T> comp
    , T[] vArray, int start, int stop, T vPivot) {
    if (stop<=start) return stop;
    int lhs = start;
    int rhs = stop-1;
    while ( lhs < stop && 
            comp.compare(vArray[lhs] , vPivot  ) < 0) { 
      ++lhs;
    }
    while ( lhs < rhs  && 
            comp.compare(vPivot , vArray[rhs] )<= 0) {
      --rhs;
    }
    while ( lhs < rhs) {
      T vTemp = vArray[lhs];
      vArray[lhs] = vArray[rhs];
      vArray[rhs] = vTemp;
      do {
        ++lhs;
      } while ( comp.compare(vArray[lhs] , vPivot ) < 0);
      do {
        --rhs;
      } while ( comp.compare(vPivot, vArray[rhs] ) <= 0);
    }
    return rhs+1;		
  }
  
  public int[] fudgeBoundaries
    ( Comparator<? super T> comp
    , T[] vArray, int[] partitions ) {
    int partitionCount = partitions.length;
    int start = partitions[0];
    int stop  = partitions[partitions.length-1];
    for (int i=0; i<partitionCount; i+=2) {
      int a = partitions[i];
      int b = partitions[i+1];
      if ( start<a && a<b && b<stop && 
           comp.compare(vArray[a-1] , vArray[b] )==0 ) {
        b = a;
      } else {
        if ( 0<i && a<stop && partitions[i-1]<a && 
             comp.compare(vArray[a-1], vArray[a] )==0) {
          T vMin = vArray[a];
          a = swapEqualToLeft
              ( comp, vArray, a, b, vMin );
        }
        if ( a<b && b<stop && 
             comp.compare(vArray[b-1] , vArray[b])==0) {
          T vMax = vArray[b];
          b = swapEqualToRight
             ( comp, vArray, a, b, vMax );
        }
      }
      partitions[i]   = a;
      partitions[i+1] = b;
    }
    return partitions;
  }
  public T[] copyOfRange
    ( T[] vArray, int start, int stop ) {
    @SuppressWarnings("unchecked")
    T[] vCopy = (T[]) new Object[stop-start];
    for (int i=start; i<stop; ++i) {
      vCopy[i-start] = vArray[i];
    }
    return vCopy;
  }
  public void checkPartition
    ( String innerName, Comparator<? super T> comparator
    , T[] vArray, int start, int pivotIndex, int stop) 
    throws SortingFailureException {
    T vPivot = vArray[pivotIndex];
    //DumpRangeHelper.dumpRange
    //  ("z2 ", vArray, start, stop);
    //System.out.println("pivot at [" 
    //  + pivotIndex + "]=" + vPivot);
    for (int check=start; check<pivotIndex; ++check)
      if (comparator.compare(vPivot, vArray[check])<0) {
      String gripe = innerName + ": Element on left" 
          + " [" + check + "]=" + vArray[check]
          + " after partitioning the array range" 
          + " [" + start + ".." + (stop-1) + "], with "
          + " pivot " + vPivot + ", into the subranges" 
          + " [" + start + ".." + (pivotIndex-1) + "] and"
          + " [" + (pivotIndex+1) + ".." + (stop-1) + "]";
        throw new SortingFailureException(gripe);
      }
    for (int check=pivotIndex+1; check<stop; ++check)
      if (comparator.compare(vArray[check] , vPivot)<0) {
        String gripe = innerName + ": Element on right" 
          + " [" + check + "]=" + vArray[check]
          + " after partitioning the array range" 
          + " [" + start + ".." + (stop-1) + "], with "
          + " pivot " + vPivot + ", into the subranges" 
          + " [" + start + ".." + (pivotIndex-1) + "] and"
          + " [" + (pivotIndex+1) + ".." + (stop-1) + "]";
        throw new SortingFailureException ( gripe );
      }
  }
  public void sortArrayTrusted
    ( Comparator<? super T> comparator
    , T[] vSortMe) {
    Arrays.sort(vSortMe, comparator);
  }
  public void checkRangeIsPermutationOf
    ( String innerName
    , Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , T[] vTrashableCopyOfOriginal) 
    throws SortingFailureException {
    int count = stop-start;
    if (0<count) {
      sortArrayTrusted ( comparator
                       , vTrashableCopyOfOriginal);
      T[] vTrashableCopyOfPermutation
        = copyOfRange(vArray, start, stop);
      sortArrayTrusted ( comparator
                       , vTrashableCopyOfPermutation);
      for (int i=0; i<count; ++i) {
        if ( vTrashableCopyOfPermutation[i] != 
             vTrashableCopyOfOriginal[i] ) {
          String gripe = innerName + ": Array corrupted:"
            + " Element [" + (start+i) + "]" 
            + " in (sorted) output"
            + " (" + vTrashableCopyOfPermutation[i] + ")" 
            + " differs from the corresponding element"
            + " (" + vTrashableCopyOfOriginal[i] + ")"
            + " in (sorted) input";
          throw new SortingFailureException ( gripe );
        }
      }
    }
  }

}
