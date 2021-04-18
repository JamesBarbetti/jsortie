package jsortie.quicksort.selector.clean;

public class CleanVanEmdenSelector 
  implements CleanSinglePivotSelector {
  public int getSampleSize(int count) {
    return (int) Math.sqrt(count);
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int selectPivotIndex(int[] vArray
       , int start, int stop) {    
    int rangeCount  = stop-start;
    if (rangeCount< 3) {
      return start + rangeCount/2 ;
    }
    int sampleCount = getSampleSize(rangeCount);
    int p = start + (rangeCount-sampleCount)/2;
    int q = start + stop - 1 - p;
    int vX = vArray[p];
    int vZ = vArray[q];
    int vTemp;;
    if (vZ < vX) {
      vTemp = vX;
      vX = vZ;
      vZ = vTemp;
    }
    if (p+1==q) {
      return start;
    }
    
    int vXX = vX;
    int  iX = p;
    int vZZ = vZ;
    int  iZ = q;
    for (;;) {
      for (++p;p<q;++p) {
         vX = vArray[p];
         if ( vXX < vX ) {
           break; 
         }
      }
      if (q<=p) {
        p = q - 1;
        break;
      }
      for (--q;p<q;--q) {
        vZ = vArray[q];
        if ( vZ < vZZ) {
          break;
        }
      }
      if (q<=p) {
        q = p;
        --p;
        vZ = vX;
        vX = vArray[p];
      }
      if (vZ < vX) {
        vTemp = vX;
        vX = vZ;
        vZ = vTemp;
      }
      if (vXX < vX) {
        vXX = vX;
        iX  = p;
      }
      if (vZ < vZZ) {
        vZZ = vZ;
        iZ  = q;
      }
    }
    return (stop-1-q > p-start) ? iZ : iX;
  }
}
