package jsortie.quicksort.partitioner.kthstatistic.remedian;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.selector.dirty.DirtySelectorHelper;

public class GeneralRemedianPartitioner 
  extends RemedianPartitioner {
  protected int     base;
  DirtySelectorHelper dsh 
    = new DirtySelectorHelper(); 
  @Override public String toString() { 
    return this.getClass().getSimpleName() 
      + "(" + leftExpander.toString()
      + "," + rightExpander.toString()
      + "," + base + "," + power + "," 
      + (biased?"biased":"unbiased") + ")";
  }
  public GeneralRemedianPartitioner
    ( PartitionExpander lx, PartitionExpander rx
    , int baseToUse, int powerToUse
    , boolean isBiased) {
    super(lx, rx, powerToUse, isBiased);
    base   = baseToUse;
  }
  public int partitionInnerRangerExactly
    ( int[] vArray, int originalStart
    , int start, int stop
    , int originalStop, int targetIndex
    , int comparisonsRemaining
    , PartitionExpander expander) {
    for ( int count=stop-start
        ; janitorThreshold<count 
        ; count=stop-start) {
      int middle      = start + ( count >> 1 );
      int sampleStart = start;
      int sampleStop  = stop;
      int sampleSize  = sampleStop - sampleStart;
      int localPower  = (0<=comparisonsRemaining) 
                      ? power : 2;
      int minSample
        = (janitorThreshold < base) 
        ? base : janitorThreshold;
      for ( int p=localPower
          ; p>0 && minSample<sampleSize; --p) {
        int t = (sampleStop-sampleStart)/base;
        int innerStart = middle     - (t >> 1);
        int innerStop  = innerStart + t;
        comparisonsRemaining -=
          dsh.partiallySortSlices
            ( vArray, sampleStart, sampleStop, t
            , innerStart, innerStop);
        sampleStart = innerStart;
        sampleStop  = innerStop;
        sampleSize  = sampleStop - sampleStart;
      }
      //Because biased versions *don't* have a 
      //guaranteed linear run-time, biasing is turned 
      //off if we "run out" of allowed comparisons.
      int sampleTarget 
        = sampleStart + sampleSize / 2;
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
      if (sampleSize <= janitorThreshold
          || sampleSize < base 
          || sampleStart == start ) {
        janitor.partitionRangeExactly
          ( vArray, sampleStart
          , sampleStop, sampleTarget);
        comparisonsRemaining -=
          (sampleStop - sampleStart)*3;
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
      if (split<targetIndex) {
        if ( stop<originalStop 
             && vArray[stop]==vPivot) {
          return comparisonsRemaining; 
        }
        start = split+1;
        if (comparisonsRemaining<0) {
          start
            = feph.moveEqualOrLessToLeft
              ( vArray, start, stop, vPivot );
          comparisonsRemaining 
            -= (stop-start);
          if ( targetIndex < start ) {
            return comparisonsRemaining;
          }
        }
        expander = rightExpander;
      } else if (targetIndex<split) {
        if ( originalStart<start 
             && vArray[start-1]==vPivot) {
          return comparisonsRemaining;
        }
        stop     = split;
        if (comparisonsRemaining<0) {
          stop 
            = feph.moveEqualOrGreaterToRight
              ( vArray, start, stop, vPivot );
          comparisonsRemaining 
            -= (stop-start);
          if ( stop <= targetIndex ) {
            return comparisonsRemaining;
          }
        }
        expander = leftExpander;
      } else {
        return comparisonsRemaining;
      }
    }
    janitor.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
    return comparisonsRemaining;
  }
}
