package jsortie.quicksort.external;

public class BranchAvoidingExternalPartitioner 
  implements ExternalSinglePivotPartitioner {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int partitionMainRange 
    ( int[] vMainArray, int start, int stop, int pivotIndex
    , int[] vAuxArray,  int auxStart) {
    int vPivot     = vMainArray[pivotIndex];
    int scan       = start; //index in main array
    if (scan<pivotIndex && vMainArray[scan] <= vPivot) {
      do {
        ++scan; 
      } while ( scan<pivotIndex
                && vMainArray[scan] <= vPivot);
    }
    int writeLeft  = scan;     //index into main array
    int writeRight = auxStart; //index into aux array
    int v;                     //item at vMainArray[scan].
    for (scan=writeLeft; scan < pivotIndex; ++scan) {
      v                         = vMainArray[scan];
      int leftDelta             = ( v <= vPivot ) ? 1 : 0;
      vMainArray [ writeLeft  ] = v;
      vAuxArray  [ writeRight ] = v;
      writeLeft                += leftDelta;
      writeRight               += (1-leftDelta);
    }
    for (++scan;scan<stop;++scan) {
      v                         = vMainArray[scan];
      int leftDelta             = ( v < vPivot ) ? 1 : 0;
      vMainArray [ writeLeft  ] = v;
      vAuxArray  [ writeRight ] = v;
      writeLeft                += leftDelta;
      writeRight               += (1-leftDelta);
    }
    vMainArray[writeLeft] = vPivot;    
    return writeLeft - start;
  }	
  public int partitionAuxiliaryRange
    ( int[] vMainArray, int start
    , int[] vAuxArray,  int auxStart, int auxStop
    , int auxPivotIndex) {
    int vPivot     = vAuxArray [ auxPivotIndex ];
    int writeLeft  = start;    //index into main array
    int writeRight = auxStart; //index into aux array
    int scan;                  //index into aux array
    for ( scan=writeRight ; scan < auxPivotIndex ; ++scan ) {
      int v                      = vAuxArray[scan];
      int leftDelta              = ( v <= vPivot ) ? 1 : 0;
      vMainArray [ writeLeft  ]  = v;
      vAuxArray  [ writeRight ]  = v;
      writeLeft                 += leftDelta;
      writeRight                += (1-leftDelta); 
    }
    for ( ++scan ; scan < auxStop ; ++scan ) {
      int v                      = vAuxArray[scan];
      int leftDelta              = ( v < vPivot) ? 1 : 0;
      vMainArray [ writeLeft ]   = v;
      vAuxArray [ writeRight ]   = v;
      writeLeft                 += leftDelta;
      writeRight                += (1-leftDelta);
    }
    vMainArray [ writeLeft ] = vPivot;
    return writeLeft-start;
  }
}
