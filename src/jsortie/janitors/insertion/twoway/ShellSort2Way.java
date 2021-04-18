package jsortie.janitors.insertion.twoway;

import jsortie.janitors.insertion.ShellSort;

public class ShellSort2Way extends ShellSort {
  public ShellSort2Way()                    { super(); }
  public ShellSort2Way(int[] gapsToUse)     { super(gapsToUse); }  
  public ShellSort2Way(double shrinkFactor) { super(shrinkFactor); }
  
  @Override
  public void sortSlicesOfRange ( int[] vArray
          , int start, int stop
          , int gap) {
	//Note: This is a two-way insertion sort; it is to 
	//ShellSort.sortSlicesOfRange what 
	//InsertionSort2Way.sortRange is to 
	//InsertionSort.sortRange.
	  
	int leftBlock = start + ((stop-start)/2/gap) *gap;
	int rightBlock = leftBlock + gap*2;
	for (; start<=leftBlock; leftBlock-=gap, rightBlock+=gap) {
	  int rightBlockStop = rightBlock + gap;
	  if (stop<rightBlockStop) {
        rightBlockStop = stop;
	  }
	  int lhs = leftBlock;
	  int rhs = rightBlock;
      for ( ; rhs < rightBlockStop; ++lhs, ++rhs ) {
	    int vLeft;
	    int vRight;
	    if ( vArray[lhs]<=vArray[rhs]) {
	      vLeft  = vArray[lhs];
	      vRight = vArray[rhs];
	    } else {
          vLeft  = vArray[rhs];
	      vRight = vArray[lhs];
	    }	  
	      
	    int shiftLeft    = lhs;
	    int scanFromLeft = lhs + gap;
	    //If item comparisons cost more, it would be worth
	    //checking that scanFromLeft<rhs before checking
	    //that array[scanFromLeft]<vLeft.
	    for (; vArray[scanFromLeft]<vLeft
             ; scanFromLeft += gap, shiftLeft += gap ) {
	      vArray[shiftLeft] = vArray[scanFromLeft];
	    }
	    vArray[shiftLeft] = vLeft;			
	      
	    int shiftRight    = rhs;
	    int scanFromRight = rhs-gap;
	    //If item comparisons cost more, it would be worth
	    //checking that lhs<scanFromRight before checking
	    //that vRight<array[scanFromRight].
   	    for (; vRight<vArray[scanFromRight]
             ; scanFromRight -= gap, shiftRight -= gap) {
          vArray[shiftRight] = vArray[scanFromRight];
	    }
	    vArray[shiftRight] = vRight;
      } //stuff in this block
      int leftBlockStop = leftBlock + gap;
      if (stop<leftBlockStop) leftBlockStop=stop;
      for (; lhs < leftBlockStop; ++lhs ) {
  	    int shiftLeft    = lhs;
  	    int scanFromLeft = lhs + gap;
  	    //If item comparisons cost more, it would be worth
  	    //checking that scanFromLeft<rhs before checking
  	    //that array[scanFromLeft]<vLeft.
	    int vLeft  = vArray[lhs];
  	    for (; scanFromLeft<stop 
               && vArray[scanFromLeft]<vLeft
             ; scanFromLeft += gap, shiftLeft += gap ) {
  	      vArray[shiftLeft] = vArray[scanFromLeft];
  	    }
  	    vArray[shiftLeft] = vLeft;			    	  
      } //end of: for left over bits in this block
	} //end of: for each block
  } //end of: method
}
