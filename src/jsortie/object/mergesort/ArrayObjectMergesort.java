package jsortie.object.mergesort;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.ObjectRangeSortHelper;

public class ArrayObjectMergesort<T>
  implements ObjectRangeSorter<T>
           , IObjectMergesorter<T> {
  //
  //A standard top-down binary mergesort 
  //(and supporting merge functions)
  //
  ObjectRangeSorter<T>    janitor;
  int                     threshold;

  public ArrayObjectMergesort 
    ( ObjectRangeSorter<T> janitor
    , int threshold) {
    this.janitor    = janitor;
    this.threshold  = threshold;
  }
  public ArrayObjectMergesort() {
    this.janitor
      = new ObjectBinaryInsertionSort<T>();
    this.threshold  = 32;
  }
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + janitor.toString()
      + "," + threshold + ")";
  }
  public void copyAndSortRange 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , T dest[], int destStart) {
    // Sorts the values in a range 
    // (in array[start..start+count-1]) 
    // into a destination range 
    // (in dest[destStart..destStart+count-1]).
    // the values in the input range will 
    // be shuffled during the process.
    int count = stop-start;
    switch (count) {
      case 0: 
        break;
      case 1: 
        dest[destStart]=vArray[start];
        break;
      case 2:
        if ( comparator.compare
             ( (T)vArray[start+1]
             , (T)vArray[start])<0) {
          dest[destStart] = vArray[start+1];
          dest[destStart+1] = vArray[start];
        } else {
          dest[destStart] = vArray[start];
          dest[destStart+1] = vArray[start+1];
        }
        break;
      default:
        if (count<threshold) {
          janitor.sortRange 
            ( comparator, vArray
            , start, stop);
          ObjectRangeSortHelper.copyRange
            ( vArray, start, stop, dest, destStart );
        } else {
          int leftCount  = count / 2;
          int destStop   = destStart + count;
          sortRangeUsing   ( comparator, vArray
                           , start, start+leftCount
                           ,  dest, destStart, destStop);
          copyAndSortRange ( comparator, vArray
                           , start+leftCount, stop
                           , dest, destStart+leftCount);
          mergeToLeft      ( comparator, vArray, start
                           , start+leftCount, dest
                           , destStart+leftCount, destStop
                           , dest, destStart);
        }
        break;
    }
  }
  public void sortRangeUsing 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , T workarea[], int workStart, int workStop) {
  int count = stop - start;
  switch (count) {			
    case 0: 
      break;
    case 1: 
      break;
    case 2:
      if ( comparator.compare
           ( (T)vArray[start+1]
           , (T)vArray[start])<0) {
        T tmp = vArray[start];
        vArray[start] = vArray[start+1];
        vArray[start+1] = tmp;
      }
      break; 				
    default:
      if (count<threshold) {
        janitor.sortRange ( comparator, vArray
                          , start, start+count );
      } else {
        int leftCount = count/2;
        sortRangeUsing   ( comparator, vArray
                         , start+leftCount, stop
                         ,  workarea, workStart
                         , workStart + leftCount);
        copyAndSortRange ( comparator, vArray
                         , start, start+leftCount
                         , workarea, workStart );
        mergeToLeft ( comparator, workarea
                    , workStart, workStart+leftCount
                    , vArray, start+leftCount
                    , start+count, vArray, start );
      }
      break;
    }
  }
  public void copyRangeFromLeft
    ( T source[]
    , int sourceStart, int sourceStop
    , T dest[], int destStart) {
    int offset = destStart - sourceStart;
    if ( offset==0 && source==dest) return;
    for (int i=sourceStart; i<sourceStop; ++i) {
      dest[ i + offset ] = source [ i ] ;
    }
  }
  public int mergeToLeft 
    ( Comparator<? super T> comparator
    , T[] vWorkArea,  int workStart, int workStop
    , T[] vArray,     int arrayStart, int arrayStop
    , T[] vDestArray, int destStart) {
    //Assumed: workStart < workStop, 
    //         arrayStart < arrayStop
    if (workStart==workStop) {
      copyRangeFromLeft 
        ( vArray, arrayStart, arrayStop
        , vDestArray, destStart);
      destStart += (arrayStop-arrayStart);
    } else if (arrayStart==arrayStop) {
      copyRangeFromLeft 
        ( vWorkArea, workStart, workStop
        , vDestArray, destStart);
      destStart += (workStop-workStart);
    } else {		
      for (;;) {
        if ( comparator.compare
             ( vWorkArea[workStart]
             , vArray[arrayStart])<=0) {
          vDestArray[destStart] 
            = vWorkArea[workStart];
          ++destStart;
          ++workStart;
          if (workStart==workStop) {
            copyRangeFromLeft 
              ( vArray, arrayStart, arrayStop
              , vDestArray, destStart);
            destStart += (arrayStop-arrayStart);
            break;
          }
        } else {
          vDestArray[destStart] 
            = vArray[arrayStart];
          ++destStart;
          ++arrayStart;
          if (arrayStart == arrayStop) {
            copyRangeFromLeft 
              ( vWorkArea, workStart, workStop
              , vDestArray, destStart);
            destStart += (workStop-workStart);
            break;
          }
        }
      }
    }
    return destStart;
  }
  public void copyRangeFromRight 
    ( Comparator<? super T> comparator
    , T source[], int sourceStart, int sourceStop
    , T dest[],   int destStart) {
    int offset = destStart - sourceStart;
    if (offset==0 && source == dest) return;
    for ( int i=sourceStop-1
        ; i>=sourceStart; --i) {
      dest[i + offset] = source [ i ];
    }
  }
  @Override
  public void mergeToRight 
    ( Comparator<? super T> comparator
    , T[] vWorkArea,  int workStart,  int workStop
    , T[] vArray,     int arrayStart, int arrayStop
    , T[] vDestArray, int destStart) {
    int w = workStop - 1;
    int a = arrayStop - 1;
    int d = destStart + ( workStop-workStart) 
                      + ( arrayStop-arrayStart) - 1;
    for (;;) {
      if ( comparator.compare
           ( vArray[a]
           , vWorkArea[w])<=0) {
        vArray[d] = vWorkArea[w];
        --w;
        --d;
        if (w<workStart) {
          copyRangeFromRight
            ( comparator, vArray
            , arrayStart, a+1
            , vDestArray, destStart);
          break;
        }
      } else {
        vArray[d] = vArray[a];
        --a;
        --d;
        if (a<arrayStart) {
          copyRangeFromRight
            ( comparator, vWorkArea
            , workStart, w+1
            , vDestArray, destStart);
          break;
        }
      }
    }
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop) {
    if (threshold<stop-start) {
      int count = stop-start;
      @SuppressWarnings("unchecked")
      T[] vAuxiliaryArray = (T[]) new Object[count];
      sortRangeUsing
        ( comparator, vArray, start, stop
        , vAuxiliaryArray, 0, count );		
    } else {
      this.janitor.sortRange
        ( comparator, vArray, start, stop );
    }
  }
}
