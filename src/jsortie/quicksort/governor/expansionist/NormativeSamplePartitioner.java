package jsortie.quicksort.governor.expansionist;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class NormativeSamplePartitioner 
  implements SamplePartitioner {
  static protected SampleSizer defaultSampleSizer
    = new SquareRootSampleSizer();
  static protected PartitionExpander defaultLeftExpander
    = new LeftSkippyExpander();
  static protected PartitionExpander defaultRightExpander 
    = new RightSkippyExpander();
  SampleSizer sizer;
  protected PartitionExpander leftExpander;
  protected PartitionExpander rightExpander;
  public NormativeSamplePartitioner() {
    sizer         = defaultSampleSizer;
    leftExpander  = defaultLeftExpander;
    rightExpander = defaultRightExpander;
  }
  @Override
  public int partitionSampleRange
    ( int[] vArray
    , int start, int stop, int targetRank
    , int sampleStart, int sampleStop) {
    int m = stop - start;
    int k = (targetRank - start + 1);
    int c = sampleStop - sampleStart;
    double kCost  = costAfterPartition( m, k);
    double tt = (double) k 
              * (double) (c + 1 )
              / (double) (m + 1 );
    int sampleTarget 
      = (int) Math.floor(sampleStart + tt - .5);
    if (sampleTarget < sampleStart) {
      sampleTarget = sampleStart;
    }
    if (sampleStop <= sampleTarget) {
      sampleTarget = sampleStop -1;
    }
    int aStart = sampleStart;
    int aStop  = sampleStop;
    PartitionExpander expander = leftExpander;
    for ( int aCount=aStop-aStart; 1<aCount
        ; aCount=aStop-aStart ) {
      int b = sizer.getSampleSize(aCount, 2);
      int bStart;
      int bStop;
      int bPivotIndex;
      if (b<2 || aCount<=b) {
        bPivotIndex = sampleTarget;
        bStart      = sampleTarget;
        bStop       = sampleTarget + 1;
      } else {
        double tHat = (double)(sampleTarget + 1 - aStart) 
                    / (double)(aCount + 1 );
        bStart  = (int) Math.floor ( sampleTarget - tHat * b +.5 ); 
        bStop   = (int) Math.floor ( sampleTarget + (1.0-tHat) * b + 1.5 );
        if (bStart < aStart) {
          bStart = aStart;
        }
        if (aStop < bStop) {
          bStop = aStop;
        }
        if (bStart==aStart && bStop==aStop) {
          bPivotIndex = sampleTarget;
          bStart      = sampleTarget;
          bStop       = sampleTarget + 1;
        } else {
          bPivotIndex
          = partitionSampleRange
            ( vArray, aStart, aStop, sampleTarget
            , bStart, bStop);
        }
      }
      int aSplit = expander.expandPartition
          ( vArray, aStart, bStart
          , bPivotIndex, bStop, aStop);
      if ( aSplit < sampleTarget ) {
        if ( aStop < sampleStop ) {
          if ( vArray[aSplit] == vArray[aStop] ) {
            return sampleTarget;
          }
        }
        aStart = aSplit + 1;
        expander = rightExpander;
      } else if ( sampleTarget < aSplit ) {
        if ( sampleStart < aStart ) {
          if ( vArray[aStart-1] == vArray[aSplit]) {
            return sampleTarget;
          }
        }
        aStop = aSplit;
        expander = leftExpander;
      } else {
        return aSplit;
      }
      //Estimate the cost of trying harder
      boolean onLeft 
        = ( sampleTarget - aStart 
            < aStop - sampleTarget );
      double costOfTryinHarder
        = ( aStop - aStart ) 
        + ( onLeft 
            ? (sampleTarget-aStart) 
            : (aStop-1-sampleTarget) );
      //And estimate the cost of... giving up
      double mu = (double)(aSplit + 1 - sampleStart) 
                * (double)(m + 1 ) 
                / (double)(c+1); //1-based
      double muCost = costAfterPartition(m, mu);
      double costOfGivinUp;
      if (kCost<muCost) {
        costOfGivinUp = muCost - kCost;
      } else {
        if (k<mu) {
          costOfGivinUp = searchCost(mu-1, k);
        } else {
          costOfGivinUp = searchCost(m-mu, k-mu);
        }
      }
      if ( costOfGivinUp < costOfTryinHarder ) {
        return aSplit; //Screw it, then! a will do.
      }
    }
    return aStart;
  }
  private double costAfterPartition
    ( double m, double k ) {
    double leftCost  = (k<3)   ? 0 : (k-1)*Math.log(k-1); 
    double rightCost = (m-k<2) ? 0 : (m-k)*Math.log(m-k);
    return (leftCost + rightCost) / Math.log(2);
  }
  private double searchCost (double m, double k) {
    if (m<2) {
      return 0;
    } else if (k<m-k) {
      return m + k;
    } else {
      return m + m - k;
    }
  }
}
