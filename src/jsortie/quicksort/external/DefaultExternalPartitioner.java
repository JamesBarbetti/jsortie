package jsortie.quicksort.external;

public class DefaultExternalPartitioner 
  implements ExternalSinglePivotPartitioner {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int partitionMainRange
  ( int[] source, int start, int stop
  , int pivotIndex
  , int[] rightDest, int destStart) {
    //returns the number of items 
    //written to the *left* partition
    int v         = source[pivotIndex];
    int writeLeft = start;
    if ( writeLeft<pivotIndex
         &&source[writeLeft]<=v) {
      do {
        ++writeLeft;
      } while ( writeLeft<pivotIndex
          &&source[writeLeft]<=v);
    }
    int scan;
    int writeRight = destStart;
    for ( scan=writeLeft; 
          scan<pivotIndex; ++scan) {
      if (source[scan] <= v) {
        source[writeLeft]=source[scan];
        ++writeLeft;
      } else {
        rightDest[writeRight]=source[scan];
        ++writeRight;
      }
    }
    for (++scan;scan<stop;++scan) {
      if (source[scan] < v) {
        source[writeLeft]=source[scan];
        ++writeLeft;
      } else {
        rightDest[writeRight]=source[scan];
        ++writeRight;
      }
    }
    source[writeLeft]=v;    
    return writeLeft-start;
  }	
  public int partitionAuxiliaryRange
  ( int[] vArray, int start
  , int[] aux, int auxStart, int auxStop
  , int pivotIndexInAuxArray) {
    //returns the number of items 
    //written to the *left* partition
    int v          = aux[pivotIndexInAuxArray];
    int writeRight = auxStart;
    int scan;
    int writeLeft = start;
    
    for (scan=writeRight; 
         scan<pivotIndexInAuxArray;
         ++scan) {
      if (aux[scan]<=v) {
        vArray[writeLeft]=aux[scan];
        ++writeLeft;
      } else {
        aux[writeRight]=aux[scan];
        ++writeRight;
      }
    }
    for (++scan;scan<auxStop;++scan) {
      if (aux[scan]<v) {
        vArray[writeLeft]=aux[scan];
        ++writeLeft;
      } else {
        aux[writeRight]=aux[scan];
        ++writeRight;
      }
    }
    vArray[writeLeft]=v;
    return writeLeft-start;
  }
}
