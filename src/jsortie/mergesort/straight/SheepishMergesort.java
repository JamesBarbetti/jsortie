package jsortie.mergesort.straight;

import jsortie.StableRangeSorter;
import jsortie.helper.RangeSortHelper;

public class SheepishMergesort
  extends BranchAvoidingStraightMergesort {
  public SheepishMergesort(StableRangeSorter janitor, int threshold) {
    super(janitor, threshold);
  }
  @Override
  public void mergeExternal2 ( int left[],  int leftStart,  int leftStop
          , int right[], int rightStart, int rightStop
          , int dest[],  int destStart) {
    mergeExternalRightToLeft ( left, leftStart, leftStop
                  , right, rightStart, rightStop
                  , dest, destStart);    
  }  
  @Override
  public void copyRange2(int[] src, int start, int stop, int[] dest, int w) {
	RangeSortHelper.copyRangeRightToLeft(src, start, stop, dest, w);
  }	
  //A Branch-Avoiding Alternating straight mergesort
  //  B      A        A !
  public void mergeExternalRightToLeft 
    ( int vLeftArray[],  int leftStart,  int leftStop
    , int vRightArray[], int rightStart, int rightStop
    , int vDestArray[],  int destStart) {
    int leftScan  = leftStop - 1;
    int rightScan = rightStop - 1;
    int destWrite = destStart + (leftStop-leftStart) + (rightStop-rightStart) - 1;
   
    //DumpRangeHelper.dumpRange("L", vLeftArray,  leftStart,  leftStop);
    //DumpRangeHelper.dumpRange("R", vRightArray, rightStart, rightStop);
    if (leftStart<=leftScan && rightStart<=rightScan) {
      int vLeft  = vLeftArray  [ leftStart  ];
      int vRight = vRightArray [ rightStart ];
      int destFirst;
      //System.out.println("vLeft=" + vLeft + ", vRight=" + vRight);
      if ( vLeft <= vRight ) {
        destFirst= destStart 
          + ( bin.findPostInsertionPoint
              ( vLeftArray, leftStart, leftStop, vRight ) - leftStart );
      } else {
        destFirst = destStart
          + ( bin.findPreInsertionPoint
              ( vRightArray, rightStart, rightStop, vLeft) - rightStart);
      }
      do {
	    vLeft                 = vLeftArray  [ leftScan  ];
	    vRight                = vRightArray [ rightScan ];
	    int rightDelta        = ( vLeft <= vRight ) ? 1 : 0;
	    int leftDelta         = 1 - rightDelta;
	    vDestArray[destWrite] = ( vLeft <= vRight ) ? vRight : vLeft;
	    destWrite            -= 1;
	    leftScan             -= leftDelta;
	    rightScan            -= rightDelta;
	  } while (destFirst <= destWrite);
      
    }
    for (; leftStart <= leftScan; --leftScan,--destWrite) {
      vDestArray [ destWrite ] = vLeftArray [ leftScan ];
    }
    if ( rightStart <= rightScan
        && ( rightScan != destWrite || vDestArray != vRightArray ) ) {
      do {    	
        vDestArray [ destWrite ] = vRightArray [ rightScan ];
        --rightScan;
        --destWrite;    	
      } while ( rightStart <= rightScan );
    }    
  }
}
