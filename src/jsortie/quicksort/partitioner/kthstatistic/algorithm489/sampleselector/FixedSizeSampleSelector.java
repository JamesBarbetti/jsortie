package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class FixedSizeSampleSelector 
  implements SampleSelector {
  int sampleSize;
  int sampleRank;
  public FixedSizeSampleSelector(int c) {
    if (c<1) {
      throw new 
        IllegalArgumentException
        ( "Cannot use sample size " 
        + c + " (as it is less than 1)");
    }
    sampleSize = c;
    sampleRank = 0;
  }
  public FixedSizeSampleSelector(int a, int c) {
    if (c<1) {
      throw new 
        IllegalArgumentException
        ( "Cannot use sample size " 
        + c + " (as it is less than 1)");
    }
    sampleSize = c;
    if (a<1 || c<a) {
      throw new
        IllegalArgumentException
        ( "Cannot use sample rank "
        + a + " (outside the legal range"
        + " of 1.." + c);
    }
    sampleRank = a;
  }
  
  
  @Override
  public void chooseSample
    ( KthStatisticSubproblem p ) {
    int    m = p.stop - p.start;
    int    c = sampleSize;
    if ( m <= c ) {
      p.innerStart = p.pivotIndex;
      p.innerStop  = p.pivotIndex+1;
      return;
    } else {
      int    k  = p.pivotIndex - p.start + 1;
      int    t  
        = (sampleRank==0) 
          ? (int)Math.floor((double)(c+1)*k/(double)(m+1)+.5)
          : sampleRank;
      p.innerStart = p.pivotIndex + 1 - t; 
      p.innerStop  = p.innerStart + c;
      if (p.innerStart < p.start) {
        p.innerStart = p.start;
      }
      if (p.stop < p.innerStop) {
        p.innerStop = p.stop;
      }
    }
  }
}
