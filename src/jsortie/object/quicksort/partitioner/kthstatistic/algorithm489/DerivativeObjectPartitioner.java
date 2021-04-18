package jsortie.object.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.Derivative489SampleSelector;

public class DerivativeObjectPartitioner<T> 
  extends Algorithm489ObjectPartitioner<T> { 
  public DerivativeObjectPartitioner() {
    super();
    sampleSelector = new Derivative489SampleSelector();
  }
  public DerivativeObjectPartitioner
    ( PartitionObjectExpander<T> leftExpander
    , PartitionObjectExpander<T> rightExpander) {
    super(leftExpander, rightExpander);
    sampleSelector = new Derivative489SampleSelector();
  }
}  