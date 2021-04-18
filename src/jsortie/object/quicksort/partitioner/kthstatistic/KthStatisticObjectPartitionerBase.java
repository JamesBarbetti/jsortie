package jsortie.object.quicksort.partitioner.kthstatistic;

import java.util.Comparator;

import jsortie.object.collector.ObjectNullCollector;
import jsortie.object.collector.ObjectSampleCollector;
import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.quicksort.expander.CentripetalObjectExpander;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.selector.KthStatisticObjectPartitioner;
import jsortie.object.quicksort.selector.reselector.DefaultPivotObjectReselector;
import jsortie.object.quicksort.selector.reselector.SinglePivotObjectReselector;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public abstract class KthStatisticObjectPartitionerBase<T> 
  implements KthStatisticObjectPartitioner<T> {
  protected  SampleSizer                      sizer;
  protected  ObjectSampleCollector<T>         collector;
  protected  SinglePivotObjectReselector<T>   reselector;
  protected  PartitionObjectExpander<T>       leftExpander;
  protected  PartitionObjectExpander<T>       rightExpander;
  protected  int                              janitorThreshold = 10;
  protected  KthStatisticObjectPartitioner<T> janitor;
  protected  boolean                          foldingWanted;
  @Override
  public String toString() {
    String name 
      = this.getClass().getSimpleName() + "("
      + getParameterList() + ")";
    return name;  
  }
  public String getParameterList() {
    String name = sizer.toString() + ",";
    if (!(collector instanceof
          ObjectNullCollector)) {
      name += collector.toString() + ",";
    }
    if (!(reselector instanceof
          DefaultPivotObjectReselector)) {
      name += reselector.toString() + ",";
    }
    name += leftExpander.toString() + ",";
    if ( leftExpander.toString() 
         != rightExpander.toString()) {
      name += rightExpander.toString() + ",";
    }
    name += "" + janitorThreshold;
    if (1<janitorThreshold) {
      name += "," + janitor.toString();
    }
    return name;
  }
  protected KthStatisticObjectPartitionerBase
  ( PartitionObjectExpander<T> expanderToUseOnLeft
  , PartitionObjectExpander<T> expanderToUseOnRight ) {
    this.sizer             = new SquareRootSampleSizer();
    this.collector         = new ObjectNullCollector<T>();
    this.reselector        = new DefaultPivotObjectReselector<T>();
    this.leftExpander      = expanderToUseOnLeft;
    this.rightExpander     = expanderToUseOnRight;
    this.janitorThreshold  = 5;
    this.janitor 
      = new ObjectRangeSorterToKthStatisticPartitioner<T>
            ( new ObjectInsertionSort<T>());
  }
 
  public KthStatisticObjectPartitionerBase() {
    this.sizer             = new SquareRootSampleSizer();
    this.collector         = new ObjectNullCollector<T>();
    this.reselector        = new DefaultPivotObjectReselector<T>();
    this.leftExpander      = new CentripetalObjectExpander<T>();
    this.rightExpander     = leftExpander;
    this.janitorThreshold  = 5;
    this.janitor 
      = new ObjectRangeSorterToKthStatisticPartitioner<T>
            ( new ObjectInsertionSort<T>() );
  }
  public void fold
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int i = start;
    int j = stop-1;
    for (; i<j; ++i, --j) {
      T vLeft  = vArray[i];
      T vRight = vArray[j];
      vArray[i] = comparator.compare
                  ( vLeft,vRight ) <=0 
                  ? vLeft  : vRight;
      vArray[j] = comparator.compare
                  ( vLeft, vRight ) <=0 
                  ? vRight : vLeft;
    }
  }    
  public int foldIfAsked
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop ) {
    if (foldingWanted) {
      fold(comparator, vArray, start, stop);
      return (stop-start)/2;
    }
    return 0;
  }
  public void setFolding(boolean fold) {
    foldingWanted = fold;
  }  
}
