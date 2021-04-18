package jsortie.object.quicksort.selector;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.collector.ObjectPositionalCollector;
import jsortie.object.collector.ObjectSampleCollector;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class DirtyObjectTheoreticalSelector<T> 
  implements SinglePivotObjectSelector<T> {
  double    x=0.5;  //Desired fractional rank: a 
                    //number between >=0 and <1 (.5 for median)
  int       samplingThreshold=64;
  protected KthStatisticObjectPartitioner<T> ksp;
  protected SampleSizer               sizer;
  protected ObjectSampleCollector<T>         collector;
  protected ObjectRangeSorter<T>             janitor;
  public DirtyObjectTheoreticalSelector
    ( double fractionalRank) {
    if (fractionalRank<=0 || 1<=fractionalRank) {
      throw new IllegalArgumentException
                ("fractional rank " + x + " outside legal range");
    }
    x          = fractionalRank;
    sizer      = new SquareRootSampleSizer(); 
    collector  = new ObjectPositionalCollector<T>(0);
    janitor   =  new ObjectBinaryInsertionSort<T>();
    ksp        = new ObjectQuickSelector<T> ();
  }
  public DirtyObjectTheoreticalSelector 
    ( double fractionalRank
    , SinglePivotObjectPartitioner<T> basePartitioner) {
    if (fractionalRank<=0 || 1<=fractionalRank) {
      throw new IllegalArgumentException
                ("fractional rank " + x + " outside legal range");
    }
    x         = fractionalRank;
    sizer     = new SquareRootSampleSizer();
    collector = new ObjectPositionalCollector<T>(0);
    janitor   =  new ObjectBinaryInsertionSort<T>();
    ksp       = new ObjectQuickSelector<T> ();
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + collector.toString() 
      + "," + ksp.toString() + ")";
  }
  @Override
  public int selectPivotIndex
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int count = stop-start;
    if (count<samplingThreshold) {
      return start + count/2;
    }
    int sampleSize  = sizer.getSampleSize(stop-start, 2);
    int k           = (int) Math.floor( (double)sampleSize * x );
    int sampleStart = (int) Math.floor( (double)count      * x ) - k;
    if (sampleStart<start) {
      sampleStart = start;
    }
    if (stop<=sampleStart+sampleSize) {
      sampleStart = stop-sampleSize;
    }
    int sampleStop = sampleStart + sampleSize;
    k += sampleStart;
    collector.moveSampleToPosition
      ( comparator, vArray, start, stop, sampleStart, sampleStop );
    ksp.partitionRangeExactly
      ( comparator, vArray, sampleStart, sampleStop, k );
    return k;
  }
}
