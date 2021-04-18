package jsortie.object.quicksort.partitioner.kthstatistic.remedian;

import java.util.Comparator;

import jsortie.object.janitor.ObjectAlternatingCombsort;
import jsortie.object.quicksort.expander.LeftLomutoObjectExpander;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.expander.RightLomutoObjectExpander;
import jsortie.object.quicksort.partitioner.kthstatistic.KthStatisticObjectPartitionerBase;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class RemedianObjectPartitioner<T> 
  extends KthStatisticObjectPartitionerBase<T>
  implements SinglePivotObjectSelector<T> { 
  protected int     power;
  protected boolean biased;
  protected ObjectAlternatingCombsort<T> comb
    = new ObjectAlternatingCombsort<T>();
  double    comparisons;
  public RemedianObjectPartitioner
    ( PartitionObjectExpander<T> expanderToUseOnLeft
    , PartitionObjectExpander<T> expanderToUseOnRight
    , int powerToRaiseTo, boolean isBiased) {
    super ( expanderToUseOnLeft
          , expanderToUseOnRight);
    //Note: janitor threshold must be at least 18.
    if (powerToRaiseTo<1) {
      throw new IllegalArgumentException
                ( "powerToRaiseTo " + powerToRaiseTo 
                + " was less than 1");
    }
    power            = powerToRaiseTo;
    biased           = isBiased;
    comb             = new ObjectAlternatingCombsort<T>();
    comparisons      = 0;
  }
  public RemedianObjectPartitioner() {
    super ( new LeftLomutoObjectExpander<T>()
          , new RightLomutoObjectExpander<T>());
    power = 2;
    biased = true;
  }
  @Override public String toString() {
    String leftBit = leftExpander.toString();
    String rightBit = rightExpander.toString();
    if (rightBit==leftBit) {
      rightBit="";
    } else {
      rightBit=", " + rightBit;
    }
    return this.getClass().getSimpleName() 
      + "(" + leftBit + rightBit 
      + "," + power + "," 
      + (biased?"biased":"unbiased") + ")";
  }
  @Override
  public int selectPivotIndex
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop)  {
    int targetIndex = start + (stop-start)/2;
    partitionRangeExactly
      ( comparator, vArray, start, stop, targetIndex );
    return targetIndex;
  }
  @Override
  public void partitionRangeExactly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int targetIndex ) {
    //Despair is delayed for power==1, because otherwise 
    //we'd just about always switch to power==2 (because 
    //average # of comparisons for power==1 is about ~ 9m ) 
    //(the real reason that using power==1 sucks!)
    //(never mind the theoretically nasty worst case,
    //the actually nasty average case is reason enough)
    int comparisonsBeforeDespair 
      = (stop-start) * ((power==1) ? 30 : 6);
    int comparisonsLeft 
      = partitionInnerRangerExactly
        ( comparator, vArray, start, start, stop, stop
        , targetIndex, comparisonsBeforeDespair); //May go -ve
    comparisons += comparisonsBeforeDespair - comparisonsLeft;
  }
  public int partitionInnerRangerExactly
    ( Comparator<? super T> comparator, T[] vArray
    , int originalStart, int start, int stop
    , int originalStop, int targetIndex
    , int comparisonsLeft) {
    //== indicates additional code,
    //for dealing with items that compare equal
    PartitionObjectExpander<T> expander = leftExpander;
    while (start+janitorThreshold<stop) {
      //The following is effectively hard-coding g=3, 
      //and the sample depends on the power:
      //if power == 1 the middle 3rd, medians of 3
      //if power == 2 the middle 9th, 
      //   medians of medians of 3 (best)
      //if power == 3 the middle 27th, 
      //   remedians (base=3, power=3) (nearly as good)
      //if power == 4 the middle 81th, 
      //   remedians (base=3, power=4) (not so good...)
      //if power > 4, the middle (3^power)th, 
      //   remedians (worse...)
      //Because a linear # of comparisons 
      //is not guaranteed for power==1,
      //and because power==2 runs best, 
      //switches to 2 if the sort takes too long.
      int sampleStart  = start;
      int sampleStop   = stop;
      boolean sorted   = false;
      for ( int p=(0<=comparisonsLeft) ? power : 2
          ; p>0 && !sorted; --p) {
        int t = (sampleStop-sampleStart)/3;
        comb.leftToRightCombsortPass
             ( comparator, vArray
             , sampleStart, sampleStop,    t );
        comb.rightToLeftCombsortPass
             ( comparator, vArray
             , sampleStart, sampleStop- t, t );
        comparisonsLeft -= (sampleStop-sampleStart)*2 - t*3;
        sampleStart += t;
        sampleStop  -= t;
        if (t <= janitorThreshold) {
          janitor.partitionRangeExactly
                  ( comparator, vArray, sampleStart
                  , sampleStop, targetIndex);
          sorted = true;
        }
      }
      //Because biased versions *don't* have 
      //a guaranteed linear run-time,
      //biasing is turned off if we "run out" 
      //of allowed comparisons.
      int sampleSize   = sampleStop-sampleStart;
      int sampleTarget = sampleStart + 
        ((biased && 0<comparisonsLeft) 
          ? (int) Math.floor 
                  ( (double)( targetIndex - start )
                    /(double)(stop-start) * sampleSize )
          : (sampleSize/2));
      if (!sorted) {
        comparisonsLeft 
          = partitionInnerRangerExactly 
            ( comparator, vArray, sampleStart, sampleStart 
            , sampleStop, sampleStop, sampleTarget
            , comparisonsLeft );
      }
      int split 
        = expander.expandPartition 
          ( comparator, vArray, start, sampleStart
          , sampleTarget, sampleStop, stop );
      comparisonsLeft 
        -= (sampleStart-start) + (stop-sampleStop);
      T vPivot = vArray[split];
      if (split<targetIndex) {
        if ( stop<originalStop 
             && comparator.compare
                ( vArray[stop], vPivot ) == 0) { //==
          return comparisonsLeft; //==
        } //==
        start = split+1;
        expander = rightExpander;
      } else if (targetIndex<split) {
        if ( originalStart<start 
              && comparator.compare
                 ( vArray[start-1], vPivot ) == 0) { //==
          return comparisonsLeft; //==
        } //==
        stop = split;
        expander = leftExpander;
      } else {
        return comparisonsLeft;
      }
    }
    janitor.partitionRangeExactly
    ( comparator, vArray
    , start, stop, targetIndex);
    return comparisonsLeft;
  }
  public double getComparisonCount() {
    return comparisons;
  }
  public void setComparisonCount(double c) {
    comparisons = c;
  }
}