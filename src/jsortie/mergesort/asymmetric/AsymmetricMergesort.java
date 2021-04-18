package jsortie.mergesort.asymmetric;

import jsortie.StableRangeSorter;
import jsortie.mergesort.vanilla.StaircaseMergesort;

public class AsymmetricMergesort
  extends StaircaseMergesort {
  public AsymmetricMergesort
    ( StableRangeSorter janitor, int threshold ) {
    super(janitor, threshold);
  }	
  protected static boolean preferPingPong() { 
    return false; }
  public int leftChildPartitionSize
    ( int parentPartitionSize ) {
    return parentPartitionSize / 10 ; 
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count = stop-start;
    if (count<janitorThreshold) {
      sortSmallRange(vArray, start, stop);
    } else {
      int workCount = leftChildPartitionSize(count);
      if (workCount<1) workCount=1;
      int workArea[] = new int[workCount];
      sortRangeUsing ( vArray, start, stop
                     , workArea, 0, workCount );
    }
  }
}