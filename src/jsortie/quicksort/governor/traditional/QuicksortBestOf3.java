package jsortie.quicksort.governor.traditional;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.selector.clean.CleanPositionalSampleSelector;

public class QuicksortBestOf3
  extends QuicksortGovernor {
  public QuicksortBestOf3() {
    super ( new CleanPositionalSampleSelector(3)
          , new SingletonPartitioner(), new InsertionSort(), 64);	
  }
}
