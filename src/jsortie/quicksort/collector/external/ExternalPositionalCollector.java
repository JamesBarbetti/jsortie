package jsortie.quicksort.collector.external;

import jsortie.exception.SortingFailureException;

public class ExternalPositionalCollector 
  implements ExternalSampleCollector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public Object collectSampleInExternalArray 
    ( int[] vSourceArray, int start, int stop
    , int[] vSampleArray
    , int sampleStart, int sampleStop) {
    int sampleCount = sampleStop - sampleStart;
    double step = (double)(stop-start) 
                / (double)(sampleCount+1);
    double r    = start + step + .5;
    for ( int w=sampleStart
        ; w<sampleStop; ++w, r+=step) {
      vSampleArray[w] 
        = vSourceArray[ (int) Math.floor ( r  ) ];
    }
    return null;
  }  
  @Override
  public int indexOfSampleItemInSourceArray 
    ( Object state, int vItem
    , int sampleStart, int sampleStop
    , int[] vArray, int start, int stop) {
    int  sampleCount = sampleStop - sampleStart;
    double      step = (double)(stop-start) 
                     / (double)(sampleCount+1);
    int    bestIndex = start - 1;
    int    bestScore = 0; //distance from edge
    for ( double r = start + step + .5; r<stop; r+=step) {
      int i = (int) Math.floor(r);
      if ( vArray[i] == vItem) {
        int score 
          = (i-start) < (stop-1-i) 
            ? (i-start) 
            : (stop-1-i);
        if (bestScore<score) {
          bestIndex = i;
          stop -= (score-bestScore);
          bestScore = score;
        }
      }
    }
    if (bestIndex<start)
      throw new SortingFailureException
        ( "Did not find " + vItem 
        + " in one of the source elements");
    return bestIndex;
  }
}
