package jsortie.mergesort.asymmetric;

import jsortie.StableRangeSorter;
import jsortie.janitors.insertion.BranchAvoidingBinaryInsertionSort;

public class BranchAvoidingMergesort extends AsymmetricMergesort {
  public BranchAvoidingMergesort(StableRangeSorter janitor, int threshold) {
    super(janitor, threshold);
  }
  protected BranchAvoidingBinaryInsertionSort bin
    = new BranchAvoidingBinaryInsertionSort();
  @Override
  public int leftChildPartitionSize(int parentPartitionSize) {
    return parentPartitionSize / 2 ; 
  }
  @Override
  public void mergeToLeftForecasting 
    ( int[] vWorkArea, int leftStart,  int leftStop
    , int[] vArray,    int rightStart, int rightStop 
    , int dest) {
	  
    if (0<leftStop-leftStart && 0<rightStop-rightStart)
    {
      int vLeft  = vWorkArea [ leftStop  - 1];
      int vRight = vArray    [ rightStop - 1];
      int destStop;
      if ( vLeft <= vRight ) {
    	destStop = dest + (leftStop - leftStart)
    	  + (bin.findPreInsertionPoint
            (vArray, rightStart, rightStop, vLeft) - rightStart);
      } else {
    	destStop = dest + (rightStop - rightStart)
          + (bin.findPostInsertionPoint
            (vWorkArea, leftStart, leftStop, vRight) - leftStart);
      }
      do {
	    vLeft             = vWorkArea [ leftStart  ];
	    vRight            = vArray    [ rightStart ];
	    int leftDelta     = (vLeft <= vRight) ? 1 : 0;
	    int rightDelta    = 1 - leftDelta;
	    vArray[dest]       = (vLeft <= vRight) ? vLeft : vRight;
	    dest             += 1;
	    leftStart        += leftDelta;
	    rightStart       += rightDelta;
	  } while (dest < destStop);
    }
    for (; leftStart < leftStop; ++leftStart, ++dest) {
      vArray [ dest ] = vWorkArea [ leftStart ];
    }
    if (rightStart < rightStop && rightStart != dest ) {
      do {    	
        vArray [ dest ] = vArray [ rightStart ];
    	++rightStart;
    	++dest;    	
      } while (rightStart<rightStop);
    }
  }
}
