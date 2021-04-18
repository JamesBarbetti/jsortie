package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Afterthought489Partitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;

public class DirtyTheoreticalSelector 
  implements SinglePivotSelector {
  double    aHat=0.5; //Desired fractional rank: 
                      //a number between >=0 and <1 
                      //(.5 for median)
  int       samplingThreshold=64;
  protected KthStatisticPartitioner ksp;
  protected SampleSizer             sizer;
  protected SampleCollector         collector;
  public DirtyTheoreticalSelector
    ( double fractionalRank ) {
    if (fractionalRank<=0 || 1<=fractionalRank) {
      throw new IllegalArgumentException
                ( "fractional rank " + fractionalRank 
                + " outside legal range");
    }
    aHat      = fractionalRank;    
    sizer     = new SquareRootSampleSizer(0);
    collector = new NullSampleCollector();
    ksp       = new Afterthought489Partitioner
                    ( collector
                    , new LeftSkippyExpander()
                    , new RightSkippyExpander() );
  }
  public DirtyTheoreticalSelector 
    ( double fractionalRank
    , SampleSizer     sampleSizer
    , SampleCollector collectorToUse
    , KthStatisticPartitioner kthStatisticPartitioner) {
    if (fractionalRank<=0 || 1<=fractionalRank) {
      throw new IllegalArgumentException
                ( "fractional rank " + fractionalRank 
                + " outside legal range");
    }
    sizer     = sampleSizer;
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
    int m = stop-start;
    if (m<samplingThreshold) {
      return start + m/2;
    }
    int c = sizer.getSampleSize
            ( stop-start, 2 );
    int a = (int) Math.floor
            ( (double)(c+1) * aHat +.5);
    if (c<=a) a=c-1;
    int sampleStart 
      = (int) Math.floor
        ( (double)m * aHat ) - a;
    if (sampleStart<start) {
      sampleStart = start;
    }
    if (stop<=sampleStart+c) {
      sampleStart = stop-c;
    }
    int sampleStop = sampleStart + c;
    a += sampleStart;
    collector.moveSampleToPosition
      ( vArray, start, stop
      , sampleStart, sampleStop );
    ksp.partitionRangeExactly
      ( vArray, sampleStart, sampleStop, a );
    return a;
  }
}
