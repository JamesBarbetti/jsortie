package jsortie.object.treesort.higherdegree;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.selector.KthStatisticObjectPartitioner;
import jsortie.object.quicksort.selector.ObjectQuickSelector;

public class OverlyLazyQuacksort<T> 
  extends FancyQuacksort<T> {
  KthStatisticObjectPartitioner<T> partitioner;
  ObjectRangeSorter<T> smallRangeSorter;
  public String toString() {
    return this.getClass().getSimpleName();
  }	
  public OverlyLazyQuacksort ( ObjectRangeSorter<T> sorter) {
    this.partitioner =  new ObjectQuickSelector<T>();
    this.smallRangeSorter = sorter;
  }
  public OverlyLazyQuacksort
    ( KthStatisticObjectPartitioner<T> kthStatisticPartitioner
    , ObjectRangeSorter<T> sorter) {
    this.partitioner =  kthStatisticPartitioner;
    this.smallRangeSorter = sorter;
  }  
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    OverlyLazySortingTree<T> tree 
      = new OverlyLazySortingTree<T>
            ( comparator, partitioner, smallRangeSorter );
    tree.sort(vArray, start, stop);
  }
}
