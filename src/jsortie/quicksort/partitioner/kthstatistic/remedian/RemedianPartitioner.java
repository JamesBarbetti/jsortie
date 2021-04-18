package jsortie.quicksort.partitioner.kthstatistic.remedian;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.janitors.exchanging.AlternatingCombsort;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.BalancedSkippyExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticExpanderBase;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.samplesizer.OneItemSampleSizer;
import jsortie.quicksort.selector.reselector.DefaultPivotReselector;

public class RemedianPartitioner 
  extends KthStatisticExpanderBase
  implements KthStatisticPartitioner {	
  protected int     power;
  protected boolean biased;
  protected AlternatingCombsort comb;
    //Although this will actually use a 
    //branch-avoiding version, you 
    //could use a conventional one, by
    //assigning comb ... to one.  But: why?
  protected FancierEgalitarianPartitionerHelper feph 
    = new FancierEgalitarianPartitionerHelper(); 
  public RemedianPartitioner() {
    super ( new OneItemSampleSizer() 
          , new NullSampleCollector()
          , new DefaultPivotReselector()
          , new BalancedSkippyExpander()
          , new BalancedSkippyExpander()
          , 5, new KislitsynPartitioner());
    power  = 2;
    biased = false;
    comb
      = new BranchAvoidingAlternatingCombsort();
  }
  public RemedianPartitioner
    ( PartitionExpander expanderToUseOnLeft
    , PartitionExpander expanderToUseOnRight
    , int powerToRaiseTo, boolean isBiased) {
    super ( new OneItemSampleSizer()
          , new NullSampleCollector()
          , new DefaultPivotReselector()
          , expanderToUseOnLeft
          , expanderToUseOnRight
          , 5,  new KislitsynPartitioner());
    if (powerToRaiseTo<1) {
      throw new IllegalArgumentException
                ( "powerToRaiseTo " + powerToRaiseTo 
                + " was less than 1");
    }
    power  = powerToRaiseTo;
    biased = isBiased;
    comb
      = new BranchAvoidingAlternatingCombsort();
  }
  @Override public String toString() { 
    return this.getClass().getSimpleName() 
      + "(" + leftExpander.toString()
      + "," + rightExpander.toString()
      + "," + power + "," 
      + (biased?"biased":"unbiased") + ")";
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
    //Despair is delayed for power==1, 
    //because otherwise we'd just about
    //always switch to power==2 (because 
    //average # of comparisons for power==1
    //is about ~ 9m ) 
    //(the real reason that using 
    //power==1 sucks!: It isn't that it
    //doesn't have a comparison count, Cs,
    //guaranteed linear in m. It's worse!
    //The coefficient of the average of Cs
    //is craptacular.
    //
    int m = stop - start;
    int comparisonsBeforeDespair 
      = m * ((power==1) ? 30 : 6);
    int middle = start + m/2;
    boolean onLeft = (targetIndex<=middle);
    PartitionExpander expander 
      = onLeft 
      ? leftExpander 
      : rightExpander;
    partitionInnerRangerExactly
      ( vArray, start, start, stop, stop
      , targetIndex, comparisonsBeforeDespair
      , expander); 
  }
  public int partitionInnerRangerExactly
    ( int[] vArray, int originalStart
    , int start, int stop
    , int originalStop, int targetIndex
    , int comparisonsRemaining
    , PartitionExpander expander) {
    while (start+janitorThreshold<stop) {
      //The following is effectively hard-coding g=3, 
      ///and the sample 
      //depends on the power:
      //if power == 1 the middle 3rd, medians of 3
      //if power == 2 the middle 9th, 
      //medians of medians of 3 (best)
      //if power == 3 the middle 27th, 
      //remedians (base=3, power=3) (nearly as good)
      //if power == 4 the middle 81th, 
      //remedians (base=3, power=4) (not so good...)
      //if power > 4, the middle (3^power)th, 
      //remedians (worse...)
      //Because a linear # of comparisons 
      //is not guaranteed for power==1,
      //and because power==2 has the best
      //worst case, switches to power=2,
      //if the sort takes too long.
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
      //Because biased versions *don't* have a 
      //guaranteed linear run-time, biasing is turned 
      //off if we "run out" of allowed comparisons.
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