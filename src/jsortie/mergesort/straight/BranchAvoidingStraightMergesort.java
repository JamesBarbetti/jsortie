package jsortie.mergesort.straight;

import jsortie.StableRangeSorter;
import jsortie.janitors.insertion.BranchAvoidingBinaryInsertionSort;

public class BranchAvoidingStraightMergesort extends StraightMergesort {
  protected BranchAvoidingBinaryInsertionSort bin;
  public BranchAvoidingStraightMergesort(StableRangeSorter janitor, int threshold) {
    super(janitor, threshold);
	 bin = new BranchAvoidingBinaryInsertionSort();  
  }
  @Override
  public void mergeExternal 
    ( int[] vLeftArray,  int leftScan,  int leftStop
    , int[] vRightArray, int rightScan, int rightStop
    , int[] vDestArray,  int destWrite) {
	int destStart = destWrite;
    if (leftScan<leftStop && rightScan<rightStop) {
      int destStop;
      int vLeft  = vLeftArray  [ leftStop  - 1];
      int vRight = vRightArray [ rightStop - 1];
      if ( vLeft <= vRight ) {
    	destStop = destStart + (leftStop - leftScan)
    	  + (bin.findPreInsertionPoint
            (vRightArray, rightScan, rightStop, vLeft) - rightScan);
      } else {
    	destStop = destStart + (rightStop - rightScan)
          + (bin.findPostInsertionPoint
            (vLeftArray, leftScan, leftStop, vRight) - leftScan);
      }
      do {
	    vLeft                 = vLeftArray  [ leftScan  ];
	    vRight                = vRightArray [ rightScan ];
	    boolean takeLeft      = ( vLeft <= vRight );
	    int leftDelta         = takeLeft ? 1 : 0;
	    int rightDelta        = 1 - leftDelta;
	    vDestArray[destWrite] = takeLeft ? vLeft : vRight;
	    destWrite            += 1;
	    leftScan             += leftDelta;
	    rightScan            += rightDelta;
	  } while (destWrite < destStop);
    }
    for (; leftScan < leftStop; ++leftScan, ++destWrite) {
      vDestArray [ destWrite ] = vLeftArray [ leftScan ];
    }
    if (rightScan < rightStop 
        && ( rightScan != destWrite || vDestArray != vRightArray ) ) {
      do {    	
        vDestArray [ destWrite ] = vRightArray [ rightScan ];
        ++rightScan;
        ++destWrite;    	
      } while ( rightScan < rightStop );
    }
  }

}
