package jsortie.quicksort.selector.clean;

import jsortie.quicksort.indexselector.PositionalIndexSelector;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;

public class CleanTenPercentSelector 
  extends CleanExternalSelector {	
  public CleanTenPercentSelector () {
    super ( new PositionalIndexSelector()
          , new QuickSelectPartitioner());
  }
  public int getIndexIntoSample(int sampleCount) {
    return sampleCount/10;	
  }
}
