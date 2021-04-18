package jsortie.quicksort.selector.clean;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.external.ExternalPositionalCollector;
import jsortie.quicksort.collector.external.ExternalSampleCollector;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Afterthought489Partitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class CleanTheoreticalSelector
  implements CleanSinglePivotSelector {
  protected SampleSizer sizer;
  protected ExternalSampleCollector collector;
  double    aHat=0.5; //Desired fractional rank: 
                      //a number between >=0 and <1 
                      //(.5 for median)
  protected KthStatisticPartitioner ksp;
  public CleanTheoreticalSelector
    ( double fractionalRank ) {
    if (fractionalRank<=0 || 1<=fractionalRank) {
      throw new IllegalArgumentException
                ( "fractional rank " + fractionalRank
                + " outside legal range");
    }
    sizer     = new SquareRootSampleSizer();
    collector = new ExternalPositionalCollector();
    aHat      = fractionalRank;
    ksp       = new Afterthought489Partitioner
                    ( new NullSampleCollector()
                    , new LeftSkippyExpander() 
                    , new RightSkippyExpander());
  }
  public CleanTheoreticalSelector 
    ( double fractionalRank
    , ExternalSampleCollector collectorToUse
    , KthStatisticPartitioner kthStatisticPartitioner) {
    if (fractionalRank<=0 || 1<=fractionalRank) {
      throw new IllegalArgumentException
                ( "fractional rank " + fractionalRank 
                + " outside legal range");
    }
    sizer     = new SquareRootSampleSizer();
    collector = collectorToUse;
    aHat      = fractionalRank;
    ksp       = kthStatisticPartitioner;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + collector.toString() 
           + "," + ksp.toString() + ")";
  }
  @Override
  public int selectPivotIndex
    ( int[] vArray, int start, int stop ) {
    if (stop-start<20) {
      return start + (stop-start)/2;
    }
    int c = sizer.getSampleSize(stop-start, 2);
    int[] vSample  = new int[c];
    Object state 
      = collector.collectSampleInExternalArray
        ( vArray, start, stop, vSample, 0, c );
    int a 
      = (int) Math.floor 
              ( (double)(c+1) * aHat + .5);
    if (c<=a) a=c-1;
    ksp.partitionRangeExactly
      ( vSample, 0, c, a );
    return collector.indexOfSampleItemInSourceArray
      ( state, vSample[a], 0, c
      , vArray, start, stop );	
  }
}
