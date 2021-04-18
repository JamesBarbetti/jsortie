package jsortie.quicksort.partitioner.unidirectional;

import jsortie.helper.ShiftHelper;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;

public class Defenestrator 
  extends CentrePivotPartitioner {
  static final int blockSize = 256;	
    //block size, in items (should be~ 8K).
  protected ShiftHelper shifter = new ShiftHelper();
  @Override
  public int partitionRange
    ( int[] vArray, int start
    , int stop, int pivotIndex) {
    if (stop-start<blockSize*4) {
      return super.partitionRange(vArray, start, stop, pivotIndex);
    }
    int v = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int lhs = start;
    int middle = start + blockSize;
    int rhs = middle + blockSize ;
    int rightBlock = stop; //start of last block used from right
    int leftBlock  = rhs;  //end of last block used from left
    for (;;) {
      do { 
        --rhs; 
      } while (middle<=rhs && v < vArray[rhs] );
      vArray[lhs] = vArray[rhs];
      if (rhs<middle) {
        messWith( vArray, middle, middle+blockSize);
        shifter.moveFrontElementsToBack
          ( vArray, middle, middle+blockSize, rightBlock );
        rightBlock -= blockSize;
        vArray[rhs] = vArray[rhs+blockSize];
        rhs += blockSize;
        if ( rightBlock < leftBlock + blockSize ) {
          break;
        }
      }
      do { 
        ++lhs; 
      } while (lhs<middle && vArray[lhs] < v );
      vArray[rhs] = vArray[lhs];
      if (middle<=lhs) {
        messWith( vArray, start, middle);
        shifter.moveFrontElementsToBack
          ( vArray, start, start+blockSize, leftBlock+blockSize );
        leftBlock += blockSize;
        vArray[lhs] = vArray[lhs-blockSize];
        lhs -= blockSize;
        if ( rightBlock < leftBlock + blockSize  ) {
          break;
        }
      }
    }
    shifter.moveFrontElementsToBack(vArray, start, start+2*blockSize, leftBlock);
    int jump = leftBlock-start-2*blockSize;
    int rightCount = start+2*blockSize-rhs;
    lhs += jump;
    rhs += jump;
    shifter.moveFrontElementsToBack(vArray, rhs, rhs+rightCount, rightBlock);
    rhs = rightBlock;
    return super.partitionRange(vArray, lhs, rhs, lhs);
  }
  public void messWith(int [] vArray, int start, int stop)
  {
    //for (--stop;start<stop;++start,--stop)
    //	RangeSortHelper.compareAndSwapIntoOrder(array, start, stop);
  }	
}
