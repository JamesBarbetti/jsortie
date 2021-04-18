package jsortie.object.treesort.binary;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class RandomTreeSort<T>
  implements ObjectRangeSorter<T> {
  static double phi = (1.0 + Math.sqrt(5.0)) / 2.0;
  static double reciprocalOfPhi = 1.0/phi;
  public String toString() {
    return this.getClass().getSimpleName().toString();
  }	
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    RandomTreeNode<T> root 
      = arrayToTree(comparator, vArray, start, stop);
    if (root!=null) {
      root.emit(vArray, start);
    }
  }
  public RandomTreeNode<T> arrayToTree
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    RandomTreeNode<T> root = null;
    if (start<stop) {
      int count = stop-start;
      int step 
        = (int) Math.floor
                (count * reciprocalOfPhi);
      if (step==0) {
        return new RandomTreeNode<T>(vArray[start], 0);
      }
      while (1<gcd(step, count)) { 
        --step;
      }
      int i = step;
      root = new RandomTreeNode<T>
                 (vArray[start+i], i);
      do {
        i += step;
        i -= (count<=i) ? count : 0;
        root.accept ( comparator
                    , new RandomTreeNode<T>
                          (vArray[start+i], i));
      }
      while (i!=0);
    } 
    return root;
  }
  protected int gcd(int a, int b) {
    return (b==0) 
      ? a 
      : (b<a 
           ? gcd(b,a%b) 
            : gcd(a,b%a));
  }
}
