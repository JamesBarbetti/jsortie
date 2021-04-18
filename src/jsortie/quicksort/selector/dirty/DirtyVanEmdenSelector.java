package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.partitioner.standalone.VanEmdenPartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;

public class DirtyVanEmdenSelector 
  implements SinglePivotSelector {
  public VanEmdenPartitioner vep 
    = new VanEmdenPartitioner();
  public SampleSizer sizer
    = new SquareRootSampleSizer();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    int c = sizer.getSampleSize(stop-start, 2);
    int sampleStart = start + (stop-start)/2 - c/2;
    int sampleStop  = sampleStart + c;
    int[] s = vep.multiPartitionRange(vArray, sampleStart, sampleStop);
    return s[0];
  }
}
