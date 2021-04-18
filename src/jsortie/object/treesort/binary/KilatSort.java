package jsortie.object.treesort.binary;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class KilatSort<T>
  implements ObjectRangeSorter<T> {
  static    double phi = (1.0 + Math.sqrt(5.0)) / 2.0;
  static    double reciprocalOfPhi = 1.0/phi;
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) 
  {
    int count = stop-start;
    int step  = (int) Math.floor(count * reciprocalOfPhi);
    while (1<gcd(step, count)) { 
      --step;
    }
    int j=start+step;
    int i=start;
    for (i=start;i<stop;++i) {
      T vTemp = vArray[j];
      vArray[j]=vArray[i];
      vArray[i]=vTemp;
      j += step;
  	  j -= (j<stop) ? 0 : count;
    }
    j = 0;
    RandomTreeNode<T> root 
      = new RandomTreeNode<T>(vArray[start], j);
    for (i=start+1;i<stop;++i) {
      if (((i-start) & (i-start-1)) == 0) {
        root.emit(vArray, start);
        root = rebuild ( vArray, start, i
                       , new Integer(step), step, count);
      }
      root.accept 
        ( comparator
        , new RandomTreeNode<T>(vArray[j], j));
      j += step;
      j -= (j<count) ? 0 : count;
    }
  }
  private RandomTreeNode<T> rebuild
    ( T[] vArray, int start, int stop
    , Integer fudge, int step, int count) {
    RandomTreeNode<T> node=null;
    if (start<stop) {
      int i = start + (stop-start)/2;
      node = new RandomTreeNode<T>
                 ( vArray[i], fudge.intValue() );
      fudge += step;
      fudge -= (fudge<count) ? 0 : count;
      node.less 
        = rebuild(vArray, start, i,  fudge, step, count);
      node.more 
        = rebuild(vArray, i+1, stop, fudge, step, count);
    }
    return node;
  }
  protected int gcd(int a, int b) {
    return (b==0) ? a : (b<a ? gcd(b,a%b) : gcd(a,b%a));
  }
}
