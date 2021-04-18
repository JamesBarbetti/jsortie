package jsortie.quicksort.partitioner.instrumented;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class InstrumentedSingletonPartitioner 
  implements SinglePivotPartitioner {
  protected double moveCount;
  protected double comparisonCount;
  protected double overheadComparisonCount;
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  public double getMoveCount() {
    return moveCount;
  }
  public double getComparisonCount() {
    return comparisonCount;
  }
  public double getOverheadComparisonCount() {
    return overheadComparisonCount;
  }
  public void zeroCounts() {
    moveCount = 0;
    comparisonCount = 0;
    overheadComparisonCount = 0;
  }
  @Override
  public int partitionRange 
    ( int vArray[], int start
    , int stop, int pivotIndex) { 
    int vPivot         = vArray[pivotIndex];
    int lhs            = start;
    int rhs            = stop;
    vArray[pivotIndex] = vArray[start];
    moveCount += 1;
    //Find last value, v, from right, such that v <= vPivot
    do { 
      --rhs;
    } while ( lhs < rhs && vPivot < vArray[rhs] ); 
    //Find first value, v, from left, such that vPivot <= v
    do {
      ++lhs; 
    } while ( lhs < rhs && vArray[lhs] < vPivot );
    if ( lhs < rhs ) {
      //Swap.  And find the next value (searching from the 
      //right), and the next value (searching from the left), 
      //that might need to be swapped.  Boundary checks 
      //*aren't* needed in the inner loop after the first 
      //such exchange (the two items involved in the first
      //exchange act as sentinels).
      do {
        int vTemp   = vArray[lhs];
        vArray[lhs] = vArray[rhs];
        vArray[rhs] = vTemp;
        moveCount  += 2;
        do { 
          --rhs;
        } while ( vPivot < vArray[rhs] );
        if (rhs <= lhs) {
          break;
        }
        do {
          ++lhs;
        } while ( vArray[lhs] < vPivot );
      } while ( lhs < rhs );
    }
    vArray[start]    = vArray[rhs]; 
    vArray[rhs]      = vPivot;
    moveCount       += 2;
    comparisonCount += stop-start;
    overheadComparisonCount += 
      getComparisonOverhead
      ( start, rhs, stop );
    return rhs;
  }
  public static double getComparisonOverhead
    ( int start, int pivot, int stop ) {
    double m      = stop - start;
    double mLeft  = pivot - start;
    double mRight = stop - pivot - 1;
    return m + 1 /* in truth, should be m, not m-1, as there's an additional item comparison */  
         + logFactorial(mLeft)
         + logFactorial(mRight)
         - logFactorial(m);
  }
  public static double logFactorial(double m) {
    if (m < 2 ) {
      return 0.0;
    }
    return (m+1) * ( Math.log(m) - 1.0 )
             / Math.log(2.0);
  }
}
