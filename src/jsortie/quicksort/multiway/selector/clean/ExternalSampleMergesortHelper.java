package jsortie.quicksort.multiway.selector.clean;

import java.util.Arrays;

public class ExternalSampleMergesortHelper 
  implements ExternalSampleSelectorHelper {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void partiallySortSampleAndIndices
  ( int[] vSample, int[] indices, int start, int stop
  , int[] sampleRanks) {
    fullySortSampleAndIndices(vSample, indices, start, stop);
  }
  public void fullySortSampleAndIndices
    ( int[] vSample, int[] indices, int start, int stop ) {
    int[] vAuxSample = Arrays.copyOf(vSample, stop-start);
    int[] auxIndices = Arrays.copyOf(indices, stop-start);
    sortSampleAndIndicesUsing ( vSample, indices, start, stop
                              , vAuxSample, auxIndices, 0); 
  }
  protected void sortSampleAndIndicesUsing 
    ( int[] vSample, int[] indices, int start, int stop
    , int[] vAuxSample, int[] auxIndices, int auxStart) {
    if (stop-start<10) {
      insertionSortSampleAndIndices( vSample, indices, start, stop);
    } else {
    int middle    = start    + (stop-start)/2;
    int auxMiddle = auxStart + (stop-start)/2;
    int auxStop   = auxStart + (stop-start);
    sortSampleAndIndicesInto ( vSample, indices, start, middle
                             , vAuxSample, auxIndices, auxStart);
    sortSampleAndIndicesInto ( vSample, indices, middle, stop
                             , vAuxSample, auxIndices, auxMiddle);
    mergeSampleAndIndices    ( vAuxSample, auxIndices 
                             , auxStart, auxMiddle, auxStop
                             , vSample, indices, start);
    }
  }
  protected void sortSampleAndIndicesInto
    ( int[] vSample, int[] indices, int start, int stop
    , int[] vSampleDest, int[] indicesDest, int destStart) {
    int middle = start + (stop-start)/2;
    sortSampleAndIndicesUsing ( vSample, indices, start, middle
                              , vSampleDest, indicesDest, destStart );
    sortSampleAndIndicesUsing ( vSample, indices, middle, stop
                              , vSampleDest, indicesDest, destStart );
    mergeSampleAndIndices     ( vSample, indices, start, middle, stop
                              , vSampleDest, indicesDest, destStart );
  }
  protected void mergeSampleAndIndices 
    ( int[] vSampleSource, int[] sourceIndices
    , int sourceStart,     int   sourceMiddle, int sourceStop
    , int[] vSampleDest,   int[] destIndices,  int destStart) {
    //it is assumed that:
    //  1. sourceStart < sourceMiddle < sourceStop
    //  2. sourceStart, sourceMiddle, sourceStop-1 
    //     are valid indices into both vSampleSource 
    //     and sourceIndices;
    //  3. destStart, destStart + (sourceStop - sourceStart) - 1 
    //     are valid indices into vSampleDest and destIndices.
    //
    int leftStop = sourceMiddle;
    for (;;) {
      boolean leftLess 
        = ( sourceIndices[sourceStart] <= sourceIndices[sourceMiddle] )
        ? ( vSampleSource[sourceStart] <= vSampleSource[sourceMiddle] )
        : ( vSampleSource[sourceStart] <  vSampleSource[sourceMiddle] );
      if (leftLess) {
        vSampleDest[destStart] = vSampleSource[sourceStart];
        destIndices[destStart] = vSampleSource[sourceStart];
        ++sourceStart;
        ++destStart;
        if (sourceStart==leftStop) {
          do {
            vSampleDest[destStart] = vSampleSource[sourceMiddle];
            destIndices[destStart] = destIndices[sourceMiddle];
            ++sourceMiddle;
            ++destStart;
          } while (sourceMiddle<sourceStop);
          return;
        }
      } else {
        vSampleDest[destStart] = vSampleSource[sourceMiddle];
        destIndices[destStart] = destIndices[sourceMiddle];
        ++sourceMiddle;
        ++destStart;
        if (sourceMiddle==sourceStop) {
            do {
              vSampleDest[destStart] = vSampleSource[sourceStart];
              destIndices[destStart] = vSampleSource[sourceStart];
              ++sourceStart;
              ++destStart;
            } while (sourceStart<leftStop);
            return;
        }
      }
    }
  }
  private void insertionSortSampleAndIndices
    ( int[] vSample, int[] indices, int start, int stop ) {
    for (int place=start+1; place<stop; ++place) {
      int v = vSample[place];
      int i = indices[place];
      int scan = place-1;
      for (; start<=scan && v<vSample[scan]; --scan) {
        vSample[scan+1] = vSample[scan];
        indices[scan+1] = indices[scan];  
      }
      vSample[scan+1] = v;
      indices[scan+1] = i;
    }	  
  }
}
