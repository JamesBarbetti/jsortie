package jsortie.mergesort.splicing;

import jsortie.StableRangeSorter;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.mergesort.vanilla.StaircaseMergesort;

public class SpliceMergesort 
  extends StaircaseMergesort {
  public SpliceMergesort
    ( StableRangeSorter janitor, int threshold ) {
    super(janitor, threshold);
  }	
  protected static int splice 
    ( int[] vWorkArea, int leftStart, int leftStop
    , int[] vArray,    int rightStart, int rightStop
    , int dest) {
    while ( leftStart < leftStop && rightStart < rightStop) {
      if ( rightStop-rightStart < leftStop-leftStart ) {
        int m = ( leftStop-leftStart ) / 2;
        int v = vWorkArea[leftStart + m];
        int split = BinaryInsertionSort.findPreInsertionPoint
                    ( vArray, rightStart, rightStop, v );
        dest = splice ( vWorkArea, leftStart, leftStart+m
                      , vArray, rightStart, split, dest);
        vArray [ dest ] = v;
        ++dest;
        leftStart += m+1;
        rightStart = split;
      } else {
        int m = ( rightStop - rightStart ) / 2;
        int v = vArray[rightStart + m];
        int split = BinaryInsertionSort.findPostInsertionPoint
                    ( vWorkArea, leftStart, leftStop, v );
        dest = splice ( vWorkArea, leftStart, split
                      , vArray, rightStart, rightStart+m, dest );
        vArray [ dest ] = v;
         ++dest;
        rightStart += m + 1;
        leftStart   = split;
      }
    }
    for (; leftStart<leftStop; ++dest, ++leftStart) {
      vArray [ dest ] = vWorkArea [ leftStart ];
    }
    if ( dest != rightStart ) {
      for (; rightStart<rightStop; ++dest, ++rightStart) {
        vArray [ dest ] = vArray [ rightStart ];
      }
    }
    return dest;
  }
  @Override
  public void sortRange
    ( int [] vArray, int start, int stop ) {
    int count = stop-start;
    if (count<janitorThreshold) {
      sortSmallRange(vArray, start, stop);
    } else {
      int workCount = count/8;
      int workArea[] = new int[workCount];
      sortRangeUsing ( vArray, start, stop
                     , workArea, 0, workCount);
    }
  }
  public void sortRangeUsing
    ( int [] vArray, int start, int stop
    , int workarea[], int workStart
    , int workStop) {
    int count = stop-start;
    if (count<janitorThreshold) {
      sortSmallRange ( vArray, start, start+count );
    }	
    int workCount = workStop - workStart;
    if ( workCount < count ) {
      if (count/2 < workCount) {
        workCount = count/2;
        sortRangeUsing         ( vArray, start+workCount, stop
                               ,  workarea, workStart, workStop);
        copyAndSortRange       ( vArray, start, start+workCount
                               , workarea, workStart );
        mergeToLeftForecasting ( workarea, 0, workCount
                               , vArray, start+workCount
                               , stop, start );
      } else {
        sortRangeUsing   ( vArray, start+workCount, stop
                         , workarea, workStart, workCount);
        copyAndSortRange ( vArray, start, start+workCount
                         , workarea, workStart);
        splice ( workarea, 0, workCount
               , vArray, start+workCount, count, start );
      }
    } else  {
      super.sortRangeUsing ( vArray, start, stop
                           , workarea, workStart, workStop);
    }
  }
}