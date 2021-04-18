package jsortie.quicksort.governor.introsort;

import jsortie.RangeSorter;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class MindYourHeadSort 
  extends EnteroSort {
    public MindYourHeadSort 
    ( SinglePivotSelector selector
    , SinglePivotPartitioner partitioner
    , RangeSorter janitor, int janitorThreshold
    , RangeSorter sortOfLastResort) {
    super ( selector , partitioner, janitor
          , (janitorThreshold<1) ? 1 : janitorThreshold
          , sortOfLastResort);
  }
  public double getComparisonLimit(int m) {
    return ( Math.log(m) - Math.log(janitorThreshold))
           * m / ln2;
  }
  public double getComparisonSpend
    ( int start, int pivot, int stop ) {
    double m      = stop - start;
    double mLeft  = pivot - start;
    double mRight = stop - pivot - 1;
    return m - 1  
         + logFactorial(mLeft)
         + logFactorial(mRight) - logFactorial(m)
         + Math.log(m) / Math.log(2.0);
  }
  public double logFactorial(double m) {
    if (m < 2 ) {
      return 0.0;
    }
    return m * ( Math.log(m) - 1 ) / ln2;
  }
}
