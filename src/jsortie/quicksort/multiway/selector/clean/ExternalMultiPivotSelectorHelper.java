package jsortie.quicksort.multiway.selector.clean;

public class ExternalMultiPivotSelectorHelper 
  extends CleanMultiPivotSelectorHelper {
  @Override
  public void sortSample 
    ( int[] vArray, int sampleIndices[]
    , int start, int stop) {
    int[] vSample
      = new int[ sampleIndices.length ];
    for ( int r=0; r<sampleIndices.length
        ; ++r ) {
      vSample[r] = vArray[sampleIndices[r]];
    }
    constructProxyHeap
      ( vSample, 0, vSample.length
      , sampleIndices );
    extractFromProxyHeap
      ( vSample, 0, vSample.length
      , sampleIndices );
  }
  public void constructProxyHeap
    ( int[] vProxy, int start, int stop
    , int[] indexArray ) {
    int fudge= start - 2;
    for ( int h = start+(stop-start-3)/2
        ; h>=start ; --h ) {
      int i = h;
      int v = vProxy[i];
      int ix = indexArray[i];
      int j = i - fudge + i;  
      do {
        if (j+1<stop) {
          j += (vProxy[j] < vProxy[j+1]) 
            ? 1 : 0;
        }
        if ( vProxy[j] <= v ) {
          break;
        }
        vProxy[i] = vProxy[j];
        indexArray[i] = indexArray[j];
        i = j;
        j = i - fudge + i;
      } while (j<stop);
      vProxy[i]=v;
      indexArray[i] = ix;
    }
  }
  public void extractFromProxyHeap
    ( int[] vProxy, int start, int stop 
    , int[] indexArray ) {
    int fudge= start - 2;
    //heap extraction phase
    int firstChild=start+2;
    for ( --stop; stop>=firstChild; stop--) {
      int v  = vProxy[stop];
      int ix = indexArray[stop];
      int i  = stop;
      int j  = start;
      //extract, assuming v will go 
      //into a bottom-level node
      do {
        if (j+1<stop) {
          j += ( vProxy[j] < vProxy[j+1] ) 
            ? 1 : 0;
        }
        vProxy[i]     = vProxy[j];
        indexArray[i] = indexArray[j];
        i = j;
        j = i - fudge + i;  
      } while (j<stop);
      //search back up the path, 
      //toward the top of the heap, 
      //to place v
      int h = (i-fudge)/2 + fudge;
      while (start<=h && vProxy[h]<v) {
        vProxy[i]     = vProxy[h];
        indexArray[i] = indexArray[h];
        i = h;
        h = (i-fudge)/2+fudge;
      } 
      vProxy[i]     = v;
      indexArray[i] = ix;
    }    
    sortSmallProxyRange
      ( vProxy, start
      , firstChild, indexArray );
  }
  public void sortSmallProxyRange
    ( int[] vProxy, int start, int stop 
    , int[] indexArray ) {
    for ( int place=start+1
        ; place<stop; ++place) {
      int v = vProxy[place];
      int ix = indexArray[place];
      int scanPlus1 = place;
      int scan = place-1;
      for ( ; start<=scan && v<vProxy[scan]
            ; --scan, --scanPlus1) {
        vProxy[scanPlus1]     = vProxy[scan];
        indexArray[scanPlus1] = indexArray[scan];
      }
      vProxy[scanPlus1]     = v;
      indexArray[scanPlus1] = ix;
    }
  }
}
