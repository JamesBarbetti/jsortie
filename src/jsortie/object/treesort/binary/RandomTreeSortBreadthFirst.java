package jsortie.object.treesort.binary;

import java.util.Comparator;

public class RandomTreeSortBreadthFirst<T> 
  extends RandomTreeSortMiddling<T> {
  @Override
  public RandomTreeNode<T> arrayToTree
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    if (stop<=start) {
      return null;
    }
    int count   = stop-start;
    int middle = count/2;
    RandomTreeNode<T> root 
      = new RandomTreeNode<T>
            ( vArray[start+middle], start+middle );
    if (count==1) {
      return root;
    } else if (count==2) {
      root.accept( comparator
      , new RandomTreeNode<T>
            (vArray[start+1], start+1));
      return root;
    }
    double step = count/2.0;
    for (; 2.0 <= step; step *= 0.5) {
      for ( double sweep = start + step*.5
          ; sweep < stop; sweep += step) {
        int i = (int)Math.floor(sweep);
        root.accept 
          ( comparator
          , new RandomTreeNode<T>
                (vArray[i], i));
      }
    }
    if (1.0<step) {
      int i = start-1;
      for ( double sweep = start+step
          ; sweep <= stop; sweep += step) {
        int j = (int)Math.floor(sweep);
        for (++i; i<j; ++i) {
          root.accept 
          ( comparator
          , new RandomTreeNode<T>
                (vArray[i], i));
        }
      }
    } else if (2<count) {
      root.accept( comparator
      , new RandomTreeNode<T>
            (vArray[start], start));
    }
    return root;
  }
}
