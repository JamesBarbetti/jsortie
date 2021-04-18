package jsortie.object.mergesort;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.janitor.ObjectBinaryInsertionSort;

public class ObjectSpliceSort<T> 
  extends ArrayObjectMergesort<T> 
  implements IObjectMergesorter<T> {
  protected ObjectBinaryInsertionSort<T> 
    binaryInsertionSort;
  public ObjectSpliceSort
    ( ObjectRangeSorter<T> janitor, int threshold) {
    super(janitor, threshold);
    binaryInsertionSort 
      = new ObjectBinaryInsertionSort<T>();
  }
  @Override
  public int mergeToLeft
    ( Comparator<? super T> comparator
    , T[] vWorkArea, int workStart, int workStop
    , T[] vArray, int arrayStart, int arrayStop
    , T[] vDestArray, int destStart) {
    while ( workStart < workStop && arrayStart < arrayStop ) {
      if ( arrayStop - arrayStart < (workStop - workStart)/3 ) {
        int     m = workStart + (workStop-workStart)/2;
        T       v = vWorkArea[m];
        int split 
          = binaryInsertionSort.findPostInsertionPoint
            ( comparator, vArray, arrayStart, arrayStop, v );
        destStart 
          = mergeToLeft 
            ( comparator, vWorkArea, workStart, m
            , vArray, arrayStart, split, vDestArray, destStart);
        vDestArray [ destStart ] = v;
        ++destStart;
        workStart = m + 1;
        arrayStart = split;
      }
      else if ( workStop - workStart 
                < (arrayStop - arrayStart)/3 ) {
        int     m = arrayStart + (arrayStop-arrayStart)/2;
        T       v = vArray[m];
        int split 
          = binaryInsertionSort.findPreInsertionPoint
           ( comparator, vWorkArea, workStart, workStop, v );
        destStart 
          = mergeToLeft 
            ( comparator, vWorkArea, arrayStart, split
            , vArray, arrayStart, m, vDestArray, destStart );
        vDestArray [ destStart ] = v;
        ++destStart;
        arrayStart = m + 1;
        workStart = split;
      } else {
        return super.mergeToLeft
               ( comparator, vWorkArea, workStart, workStop
                , vArray, arrayStart, arrayStop
                , vDestArray, destStart);
      }
    }
    copyRangeFromLeft
      ( vWorkArea, workStart, workStop
      , vDestArray, destStart );
    destStart += (workStop-workStart);
    copyRangeFromLeft
      ( vArray, arrayStart, arrayStop
      , vDestArray, destStart);
    destStart += (arrayStop-arrayStart);
    return destStart;
  }
  @Override
  public void mergeToRight
    ( Comparator<? super T> comparator
    , T vWorkArea[], int workStart,  int workStop
    , T[] vArray,    int arrayStart, int arrayStop
    , T[] vDestArray, int destStart) {
    while ( workStart < workStop && arrayStart < arrayStop ) {
      if ( arrayStop - arrayStart < (workStop - workStart)/3 ) {
        int m = workStart + (workStop-workStart)/2;
        T   v = vWorkArea[m];
        int split 
          = binaryInsertionSort.findPostInsertionPoint
            ( comparator, vArray, arrayStart, arrayStop, v );
        int destSplit 
        = destStart + (m-workStart) + (split-arrayStart) ;
        mergeToRight 
          ( comparator, vWorkArea, m+1, workStop
          , vArray, split, arrayStop
          , vDestArray, destSplit+1);
        vDestArray [ destSplit ] = v;
        workStop = m;
        arrayStop = split;
      } else if ( workStop - workStart 
                  < (arrayStop - arrayStart)/3 ) {
        int m = arrayStart + (arrayStop-arrayStart)/2;
        T   v = vArray[m];
        int split 
          = binaryInsertionSort.findPreInsertionPoint
            ( comparator, vWorkArea
            , workStart, workStop, v );
        int destSplit 
          = destStart + (m-workStart) + (split-arrayStart) ;
        mergeToLeft
          ( comparator, vWorkArea, split, arrayStop
          , vArray, m+1, arrayStop, vDestArray, destStart );
        vDestArray [ destSplit ] = v;
        arrayStop = m;
        workStop = split;
      } else {
        super.mergeToRight 
          ( comparator, vWorkArea, workStart, workStop
          , vArray, arrayStart, arrayStop
          , vDestArray, destStart);
        return;
      }
    }
    copyRangeFromRight 
      ( comparator, vWorkArea, workStart, workStop
      , vDestArray, destStart + (arrayStop-arrayStart));
    copyRangeFromRight 
      ( comparator, vArray, arrayStart, arrayStop
      , vDestArray, destStart);
  }
}
