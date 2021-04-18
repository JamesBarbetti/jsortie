package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.selector.dirty.DirtyCompoundSelector;
import jsortie.quicksort.selector.reselector.SelectorToReselector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class Algorithm489PartitionerWeird 
  extends QuickSelectPartitioner {
  @Override
  public String toString() {
    //We need this! Else we'd cop a stack 
    //overflow when toString() is called!
    //Because the DirtyCompoundSelector has 
    //been passed *this* as a parameter!
    return this.getClass().getSimpleName() 
      + "(" + leftPartitioner.toString() + ")";
  }
  public Algorithm489PartitionerWeird
    ( SinglePivotPartitioner leftPartitioner
    , SinglePivotPartitioner rightPartitioner) {
    super ( new MiddleElementSelector()
          , leftPartitioner, rightPartitioner
          , 5, new KislitsynPartitioner());
    SampleCollector collector 
      = new NullSampleCollector();
    SampleSizer sizer = new SampleSizer() {
      @Override
      public int getSampleSize(int count, int radix) {
        double z  = Math.log(count);
        int size = (int) Math.floor(Math.exp( 2 * z/3 )); // n ^ (2/3)
        return size + (((size&1)==0) ? 1 : 0); //round to an odd number
      }
    };
    reselector 
      = new SelectorToReselector
            ( new DirtyCompoundSelector
                  ( sizer, collector, this ) );
  }
}
