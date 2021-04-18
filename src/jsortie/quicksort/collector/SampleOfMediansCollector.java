package jsortie.quicksort.collector;

import jsortie.helper.RotationHelper;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;

public class SampleOfMediansCollector 
  implements ComparisonCountingSampleCollector {
  private BranchAvoidingAlternatingCombsort comb
    = new BranchAvoidingAlternatingCombsort();
  private RotationHelper rot = new RotationHelper();
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  @Override
  public void moveSampleToPosition
    ( int[] vArray, int start, int stop
    , int sampleStart, int sampleStop) {
    moveSampleToPositionCountComparisons
      ( vArray, start, stop, sampleStart, sampleStop );
  }
  @Override
  public int moveSampleToPositionCountComparisons
    ( int[] vArray, int start, int stop
    , int sampleStart, int sampleStop) {
    int m = stop - start;
    int c = sampleStop - sampleStart;
    int c3 = c * 3;
    if ( m < c3 ) {
      return 0;
    }
    int a = sampleStart - c;
    if (a < start) {
      a = start;
    }
    int b = a + c3;
    if (stop <= b) {
      b = stop;
      a = b - c3;
    }
    //c-sort the range vArray[a..(a+c3-1)].
    //(doing it this way requires exactly c3 comparisons every time)
    comb.leftToRightCombsortPass(vArray, a, b, 2*c);
    comb.rightToLeftCombsortPass(vArray, a, b, c);
    if ( sampleStart - c < start ) {
      //We want the middle c of the c3 items to 
      //go to [sampleStart..sampleStop] 
      //but they're in the range [a+c..a+2*c-1]
      rot.rotateRangeLeft(vArray, a, a+c+c, a+c-sampleStart);
    } else if ( stop < sampleStop + c) {
      rot.rotateRangeRight(vArray, stop-c-c, stop, sampleStop+c-stop);
    }
    return c3;
  }
}
