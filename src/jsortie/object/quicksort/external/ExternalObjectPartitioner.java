package jsortie.object.quicksort.external;

import java.util.Comparator;

public class ExternalObjectPartitioner<T> {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int partitionMainRange
    ( Comparator<? super T> comparator
    , T[] source, int start, int stop, int pivotIndex
    , T[] rightDest, int destStart) {
    //returns the number of items 
    //written to the *left* partition
	T   v         = source[pivotIndex];
    int writeLeft = start;
    while ( writeLeft<pivotIndex
            && comparator.compare
               ( source[writeLeft],v)<=0 ) {
      ++writeLeft;
    }
    int scan;
    int writeRight = destStart;
    for ( scan=writeLeft
        ; scan<pivotIndex; ++scan) {
      if ( comparator.compare 
           ( source[scan],v ) <= 0 ) {
        source[writeLeft]=source[scan];
        ++writeLeft;
      } else {
        rightDest[writeRight]=source[scan];
        ++writeRight;
      }
    }
    for (++scan;scan<stop;++scan) {
      if ( comparator.compare
           ( source[scan], v ) < 0 ) {
        source[writeLeft] 
          = source[scan];
        ++writeLeft;
      } else {
        rightDest[writeRight]
          = source[scan];
        ++writeRight;
      }
    }
    source[writeLeft]=v;
    return writeLeft-start;
  }
  public int partitionAuxiliaryRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start
    , T[] vAuxArray, int auxStart, int auxStop
    , int pivotIndexInAuxArray) {
    //returns the number of items 
    //written to the *left* partition
    T v = vAuxArray[pivotIndexInAuxArray];
    int writeRight = auxStart;
    int scan;
    int writeLeft = start;
	    
    for ( scan=writeRight
        ; scan<pivotIndexInAuxArray;++scan) {
      if ( comparator.compare 
           ( vAuxArray[scan],v ) <= 0 ) {
        vArray[writeLeft]
          = vAuxArray[scan];
        ++writeLeft;
      } else {
        vAuxArray[writeRight]
          = vAuxArray[scan];
        ++writeRight;
      }
    }
    for (++scan;scan<auxStop;++scan) {
      if ( comparator.compare
           ( vAuxArray[scan],v ) < 0 ) {
        vArray[writeLeft]=vAuxArray[scan];
        ++writeLeft;
      } else {
        vAuxArray[writeRight]=vAuxArray[scan];
        ++writeRight;
      }
    }
    vArray[writeLeft]=v;
    return writeLeft-start;
  }
}
