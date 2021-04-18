package jsortie.quicksort.multiway.partitioner.holierthanthou;

import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class HolierThanThou2Metamorphic 
  extends HolierThanThouPartitioner2  {
  private class Implementation {
    int[] vArray;
    int   start; 
    int   stop;

    int vP, vQ;
    int leftHole, middleHole;
    public Implementation
      ( int[] vArray, int start, int stop
      , int[] pivotIndices ) {
      this.vArray = vArray;
      this.start = start;
      this.stop  = stop;
      MultiPivotUtils.movePivotsAside
        ( vArray,  pivotIndices
        , new int[] { start, start+1 } );
      vP = vArray[start];
      vQ = vArray[start+1];
      leftHole   = start;
      middleHole = start+1;
    }
    public int [] getPartitions() {
      vArray[leftHole]   = vP;
      vArray[middleHole] = vQ;
      if (vP<vQ) {    	  
        return new int[] 
          { start, leftHole, leftHole+1, middleHole
          , middleHole+1, stop };
      } else {
        return new int[] 
          { start, leftHole, middleHole+1, stop };
      }
	}
  }
  private static void partitionSubRangePFirst
    ( int localStart, int localStop
    , Implementation imp ) {
    int[] vArray = imp.vArray;
    int vP = imp.vP;
    int vQ = imp.vQ;
    int leftHole = imp.leftHole;
    int middleHole= imp.middleHole;
    
    for (int scan=localStart; scan<localStop; ++scan) {
      int v = vArray[scan];
      if ( v < vP ) {
        vArray[scan]       = vArray[middleHole+1];
        vArray[leftHole]   = v;
        ++leftHole;
        vArray[middleHole] = vArray[leftHole];
        ++middleHole;
      } else if ( v <= vQ ) {
        vArray[scan] = vArray[middleHole+1];
        vArray[middleHole] = v;
        ++middleHole;
      }
    }
    imp.leftHole = leftHole;
    imp.middleHole= middleHole;	    
  }
  private static void partitionSubRangeQFirst
    ( int localStart, int localStop, Implementation imp ) {
    int[] vArray = imp.vArray;
    int vP = imp.vP;
    int vQ = imp.vQ;
    int leftHole = imp.leftHole;
    int middleHole= imp.middleHole;
    for (int scan=localStart; scan<localStop; ++scan) {
      int v = vArray[scan];
      if ( v <= vQ ) {
        if ( v < vP ) {
          vArray[scan]       = vArray[middleHole+1];
          vArray[leftHole]   = v;
          ++leftHole;
          vArray[middleHole] = vArray[leftHole];
          ++middleHole;
        } else {
          vArray[scan] = vArray[middleHole+1];
          vArray[middleHole] = v;
          ++middleHole;
        }
      }	       
    }
    imp.leftHole = leftHole;
    imp.middleHole= middleHole;	    		
  }
  @Override
  public int[] multiPartitionRange 
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    if (stop-start<100) {
      if (stop-start<2) {
        return new int[] { start, stop };
      } else {
        return super.multiPartitionRange
               ( vArray, start, stop
               , pivotIndices);
      }
    }
    int sample 
      = (int)Math.floor(Math.sqrt(stop - start));
    int pause  = start + sample;
    Implementation imp 
      = new Implementation
            ( vArray, start, stop, pivotIndices );
    partitionSubRangePFirst( start+2, pause, imp);
    if ( imp.leftHole - imp.start 
         >= imp.middleHole - imp.leftHole ) {
      partitionSubRangePFirst( pause, stop, imp);
    } else {
      partitionSubRangeQFirst( pause, stop, imp);
    }
    return imp.getPartitions();
  }
}
