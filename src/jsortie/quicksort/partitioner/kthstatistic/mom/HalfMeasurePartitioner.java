package jsortie.quicksort.partitioner.kthstatistic.mom;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;

public class HalfMeasurePartitioner 
  extends RemedianPartitioner {
  public HalfMeasurePartitioner
    ( PartitionExpander leftExpander
    , PartitionExpander rightExpander
    , int powerToRaiseTo
    , boolean isBiased) {
    super ( leftExpander, rightExpander
          , powerToRaiseTo, isBiased);
  }
  public int partitionInnerRangerExactly
    ( int[] vArray, int originalStart
    , int start, int stop, int originalStop
    , int targetIndex, int comparisonsRemaining
    , boolean onLeft) {
    while (start+janitorThreshold<stop) {
      int sampleStart  = start;
      int sampleStop   = stop;
      int sampleSize   = sampleStop  - sampleStart;
      int sampleTarget = sampleStart + sampleSize/2;
      if (0<comparisonsRemaining) {
        for ( int p = power 
            ; 0<p && janitorThreshold<sampleSize ; --p) {
          int h = (sampleStop-sampleStart)/2;//half (rounded down) 
          if ((h&1)==0) {
            ++h; //round up to even
          }
          int q = h/2; //quarter (rounded down)
          
          comb.rightToLeftCombsortPass
          ( vArray, sampleStart,  sampleStop,    h );
          comb.rightToLeftCombsortPass
          ( vArray, sampleStart,  sampleStart+h, q );
          comb.rightToLeftCombsortPass
          ( vArray, sampleStop-h, sampleStop,    q );
          comparisonsRemaining  
            -= ( sampleStop - sampleStart + h - q - q);
          sampleStart += q;
          sampleStop  -= q;
          sampleSize   = sampleStop - sampleStart;
          sampleTarget = sampleStart + sampleSize / 2; 
        }
      } else {
        //Resort to g=3, power=2, since that has a linear
        //worst case (this is adapted from RemedianPartitioner).
        for ( int p=2
            ; 0<p && janitorThreshold<sampleSize ; --p) {
          int t = (sampleStop-sampleStart)/3;
          comb.leftToRightCombsortPass
            ( vArray, sampleStart, sampleStop, t );
          comb.rightToLeftCombsortPass
            ( vArray, sampleStart, sampleStop - t, t );
          comparisonsRemaining 
            -= (sampleStop-sampleStart)*2 - t*3;
          sampleStart += t;
          sampleStop  -= t;
          sampleSize   = sampleStop - sampleStart;
          sampleTarget = sampleStart + sampleSize / 2; 
        }
      }
      if (biased && 0<comparisonsRemaining) {
        double m = stop-start;
        double k = targetIndex + 1- start;
        double a
          = Math.floor
            ( k * (sampleSize+1)
              / (m+1) + .5 );
        sampleTarget 
          = sampleStart + (int) Math.floor(a - .5);
      }
      comparisonsRemaining 
        = partitionInnerRangerExactly 
          ( vArray, sampleStart, sampleStart 
          , sampleStop, sampleStop
          , sampleTarget, comparisonsRemaining
          , onLeft );
      PartitionExpander expander
        = (onLeft ? leftExpander : rightExpander);
      int vPivot = vArray[sampleTarget];
      int split 
        = expander.expandPartition 
          ( vArray, start, sampleStart
          , sampleTarget, sampleStop, stop );
      comparisonsRemaining 
        -=  ( sampleStart-start 
              + stop - sampleStop);
      if ( split < targetIndex ) {
        if ( stop < originalStop 
             && vPivot == vArray[stop] ) {
          return comparisonsRemaining;
        }
        start  = split + 1;
        if (comparisonsRemaining<0) {
          stop = feph.moveEqualOrGreaterToRight
                 ( vArray, start, stop, vPivot );
          comparisonsRemaining 
            -= (stop-start);
          if ( stop <= targetIndex ) {
            return comparisonsRemaining;
          }
        }
        onLeft = false;
      } else if ( targetIndex < split ) {
        if ( originalStart<start 
             && vArray[start-1]==vPivot ) {
          return comparisonsRemaining;
        }
        stop   = split;
        if (comparisonsRemaining<0) {
          start = feph.moveEqualOrLessToLeft
                ( vArray, start, stop, vPivot );
          comparisonsRemaining 
            -= (stop-start);
          if ( targetIndex < start ) {
            return comparisonsRemaining;
          }
        }
        onLeft = true;
      } else {
        return comparisonsRemaining;
      }
    }
    janitor.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
    return comparisonsRemaining;
  }
}