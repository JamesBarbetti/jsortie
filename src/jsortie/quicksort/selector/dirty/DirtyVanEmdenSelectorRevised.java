package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.partitioner.standalone.VanEmdenPartitionerRevised;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;

public class DirtyVanEmdenSelectorRevised 
  implements SinglePivotSelector {
  public VanEmdenPartitionerRevised rvep 
    = new VanEmdenPartitionerRevised();
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
    int sampleStop  = start + c;
    int[] s = rvep.multiPartitionRange(vArray, sampleStart, sampleStop);
    return s[0];
  }
}
