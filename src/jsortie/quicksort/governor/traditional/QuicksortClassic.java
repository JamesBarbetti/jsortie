package jsortie.quicksort.governor.traditional;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.selector.simple.FirstElementSelector;

public class QuicksortClassic extends QuicksortGovernor
{
  public QuicksortClassic ()
  {
    super(new FirstElementSelector(), new SingletonPartitioner(), new InsertionSort(), 9);	
  }
}
