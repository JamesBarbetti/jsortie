package jsortie.object.treesort.higherdegree;

import java.util.Comparator;

public class FancyQuacksort<T> 
  extends QuackSort<T>
{
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    FancierSortingATree<T> tree 
      = new FancierSortingATree<T>(comparator, quicksort);
    tree.sort(vArray, start, stop);
  }
}
