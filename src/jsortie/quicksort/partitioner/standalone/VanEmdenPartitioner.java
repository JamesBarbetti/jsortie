package jsortie.quicksort.partitioner.standalone;

import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;

public class VanEmdenPartitioner 
  implements StandAlonePartitioner {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int getSampleSize(int partitionSize) {
    int sample = (int) Math.floor( Math.sqrt( partitionSize ) );
    return sample + (((sample&1)==0) ? 1 : 0);
  }
  public int getSampleStart(int start, int stop) {
    int count = stop-start;
    return start + (count-getSampleSize(count))/2;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop ) {
    if (stop-start<2) {
      return new int[] { start, start };
    }
    int p = start;
    int q = stop-1;
    int vX = vArray[p];
    int vZ = vArray[q];
    int vTemp;;
    if (vZ < vX) {
      vTemp = vX;
      vArray[p] = vX = vZ;
      vArray[q] = vZ = vTemp;
    }
    if (p+1==q) {
      return new int[] { start, start };
    }
    int vXX = vX;
    int  iX = p;
    int vZZ = vZ;
    int  iZ = q;
    for (;;) {
      for (++p;p<q;++p) {
        vX = vArray[p];
        if ( vXX < vX ) break; 
      }
      if (q<=p) {
        p = q - 1;
        break;
      }
      for (--q;p<q;--q) {
        vZ = vArray[q];
        if ( vZ < vZZ) break;
      }
      if (q<=p) {
        q = p;
        --p;
        vZ = vX;
        vX = vArray[p];
      }
      if (vZ < vX) {
        vTemp = vX;
        vArray[p] = vX = vZ;
        vArray[q] = vZ = vTemp;
      }
      if (vXX < vX) {
        vXX = vX;
        iX  = p;
      }
      if (vZ < vZZ) {
        vZZ = vZ;
        iZ = q;
      }      
    }
    if ( p!=iX ) {
      vArray[p] = vXX;
      vArray[iX] = vX;
    }
    if ( q!=iZ ) {
      vArray[q]  = vZZ;
      vArray[iZ] = vZ;
    }
    return new int[] { start, p, q+1, stop };
  }
}
