package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

public class VanEmdenObjectPartitioner<T>
  implements SinglePivotObjectPartitioner<T> {
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
  public int partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex /*ignored*/) {
    if (stop-start<2) {
      return start;
    }
    int p = start;
    int q = stop-1;
    T   vX = vArray[p];
    T   vZ = vArray[q];
    T   vTemp;
    if (comparator.compare(vZ,vX)<0) {
      vTemp = vX;
      vArray[p] = vX = vZ;
      vArray[q] = vZ = vTemp;
    }
    if (p+1==q) {
      return start;
    }
    T   vXX = vX;
    int  iX = p;
    T   vZZ = vZ;
    int  iZ = q;
    for (;;) {
      for (++p;p<q;++p) {
        vX = vArray[p];
        if ( comparator.compare(vXX,vX)<0) {
          break; 
        }
      }
      if (q<=p) {
        p = q - 1;
        break;
      }
      for (--q;p<q;--q) {
        vZ = vArray[q];
        if ( comparator.compare(vZ, vZZ)<0) {
          break;
        }
      }
      if (q<=p) {
        q = p;
        --p;
        vZ = vX;
        vX = vArray[p];
      }
      if ( comparator.compare(vZ, vX)<0) {
        vTemp = vX;
        vArray[p] = vX = vZ;
        vArray[q] = vZ = vTemp;
      }
      if ( comparator.compare(vXX, vX)<0) {
        vXX = vX;
        iX  = p;
      }
      if ( comparator.compare(vZ, vZZ)<0) {
        vZZ = vZ;
        iZ = q;
      }
    }
    if ( p!=iX ) {
      vArray[p]  = vXX;
      vArray[iX] = vX;
    }
    if ( q!=iZ ) {
      vArray[q]  = vZZ;
      vArray[iZ] = vZ;
    }
    return p;
  }
}
