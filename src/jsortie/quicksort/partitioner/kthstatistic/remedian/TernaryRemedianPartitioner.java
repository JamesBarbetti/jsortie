package jsortie.quicksort.partitioner.kthstatistic.remedian;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.expander.PartitionExpander;

public class TernaryRemedianPartitioner
  extends RemedianPartitioner {
  FancierEgalitarianPartitionerHelper feph
    = new FancierEgalitarianPartitionerHelper();
  public TernaryRemedianPartitioner
    ( PartitionExpander leftExpander
    , PartitionExpander rightExpander
    , int powerToRaiseTo, boolean isBiased) {
    super ( leftExpander, rightExpander
           , powerToRaiseTo, isBiased);
  }
  public TernaryRemedianPartitioner() {
    super();
  }
  @Override
  public int partitionInnerRangerExactly
    ( int[] vArray, int originalStart
    , int start, int stop
    , int originalStop, int targetIndex
    , int comparisonsRemaining
    , PartitionExpander expander) {
    //== indicates code for dealing 
    //with items that compare equal
    while (start+janitorThreshold<stop) {
      int sampleStart = start;
      int sampleStop  = stop;
      int sampleSize  = sampleStop - sampleStart;
      int localPower  = (0<=comparisonsRemaining) 
                      ? power : 2;
      for ( int p=localPower
          ; p>0 && janitorThreshold<sampleSize; --p) {
        int t = (sampleStop-sampleStart)/3;
        comb.leftToRightCombsortPass
          ( vArray, sampleStart, sampleStop,    t );
        comb.rightToLeftCombsortPass
          ( vArray, sampleStart, sampleStop- t, t );
        comparisonsRemaining 
          -= (sampleStop-sampleStart)*2 - t*3;
        sampleStart += t;
        sampleStop  -= t;
        sampleSize   = sampleStop - sampleStart;
      }
      int sampleTarget = sampleStart + (sampleSize+1)/2;
      if (biased && 0<comparisonsRemaining) {
        double k = targetIndex + 1 - start;
        double a  
          = k * (sampleStop + 1 - sampleStart) 
              / (stop + 1 - start);
        sampleTarget 
          = sampleStart + (int)Math.floor(a - .5);
        if (sampleTarget<sampleStart) {
          sampleTarget = sampleStart;
        } else if (sampleStop<=sampleTarget) {
          sampleTarget = sampleStop-1;
        }
      }
      if (sampleSize <= janitorThreshold) {
        janitor.partitionRangeExactly
          ( vArray, sampleStart
          , sampleStop, sampleTarget);
      } else {
        comparisonsRemaining 
          = partitionInnerRangerExactly 
            ( vArray, sampleStart, sampleStart 
            , sampleStop, sampleStop, sampleTarget
            , comparisonsRemaining, expander );
      }
      int split 
        = expander.expandPartition 
          ( vArray, start, sampleStart
          , sampleTarget, sampleStop, stop );
      comparisonsRemaining 
        -= (sampleStart-start) 
         + (stop-sampleStop);
      int vPivot = vArray[split];
      if (split < targetIndex) {
        start  = feph.moveEqualOrLessToLeft
                 ( vArray, split+1, stop, vPivot );
        comparisonsRemaining -= (stop-split-1);
        if (targetIndex < start) {
          return comparisonsRemaining;
        }
        expander = rightExpander;
      } else if (targetIndex < split) {
        stop = feph.moveEqualOrGreaterToRight
               ( vArray, start, split, vPivot );
        comparisonsRemaining -= (split-start);
        expander = leftExpander;
        if (stop <= targetIndex) {
          return comparisonsRemaining;
        }
      } else {
        return comparisonsRemaining;
      }
      if (stop<start+2) {
        return comparisonsRemaining;
      }
    }
    janitor.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
    return comparisonsRemaining;
  }
}
