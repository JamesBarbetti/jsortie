package jsortie.quicksort.collector.external;

import java.util.Random;

import jsortie.exception.SortingFailureException;

public class ExternalRandomCollector 
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
    int count       = stop - start;
    Random generator = new java.util.Random(start);
    int[] state = new int[sampleStop-sampleStart];
    for (int w=sampleStart; w<sampleStop; ++w) {
      double rDouble 
        = start + generator.nextDouble() * count;
      int r = (int) Math.floor(rDouble);
      vSampleArray[w] = vSourceArray[ r ];
      state[w-sampleStart] = r;
    }
    return state;
  }
  @Override
  public int indexOfSampleItemInSourceArray 
    ( Object state, int vItem
    , int sampleStart, int sampleStop
    , int[] vArray, int start, int stop) {
    int    bestIndex   = start - 1;
    int    bestScore   = 0; //distance from edge
    int[]  indices     = (int[]) state;
    int    last        = stop - 1;
    for ( int w = sampleStart; w<sampleStop; ++w) {
      int r = indices[w-sampleStart];
      if ( vArray[r] == vItem) {
        int score = (r-start) < (last-r) 
          ? (r-start) : (last-r);
        if (bestScore<score) {
          bestIndex = r;
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
