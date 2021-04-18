package jsortie.quicksort.multiway.selector.clean;

public class CleanMultiPivotSelectorHelper {
  public void sortSample 
    ( int[] vArray, int sampleIndices[]
    , int start, int stop) {
    //Heapsort, on indexes *into* an array
    int count = stop-start;
    if (count<2) {
      return;
    }
    int fudge = 2 - start;
    int i; //
    int j; //
    int x; //index being moved
    int v; //value, in the array (array[x])
    for ( int h=start+(count)/2 ; h>=0 ; --h ) {
      x = sampleIndices[h];
      v = vArray[x];
      i = h;
      j = i + i + fudge;
      while (j<stop-1) {
        if (j<stop-2) {
          j+= ( vArray[sampleIndices[j]]
                < vArray[sampleIndices[j+1]])
            ? 1 : 0;
        }
        if (vArray[sampleIndices[j]]<=v) {
          break;
        }
        sampleIndices[i]=sampleIndices[j];
        i = j;
        j = i + i + fudge;
      }
      sampleIndices[i]=x;			
    }		
    for (int k=stop-1;k>start;--k) {
      x = sampleIndices[k];
      v = vArray[x];
      i = k;
      j = 0;
      while (j<k) {
        if (j<k-1) { 
          j+= ( vArray[sampleIndices[j]]
                < vArray[sampleIndices[j+1]]) 
            ? 1 : 0;
        }
        if (vArray[sampleIndices[j]]<=v) {
          break;
        }
        sampleIndices[i]=sampleIndices[j];
        i = j;
        j = i + i + fudge;	
      }
      sampleIndices[i] = x;
    }					
  }
  public void sortSample
    (int[] vArray, int[] indices) {
    sortSample ( vArray, indices, 0
               , indices.length);
  }
}
