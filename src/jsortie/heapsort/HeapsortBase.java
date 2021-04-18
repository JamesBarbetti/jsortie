package jsortie.heapsort;

import jsortie.RangeSorter;

public abstract class HeapsortBase 
  implements RangeSorter {
  protected int radix = 2;
  public int getRadix() {
    return radix;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    if (start+1<stop) {
      constructHeap(vArray, start, stop);
      extractFromHeap(vArray, start, stop);
    }
  }
  public abstract void extractFromHeap(int[] vArray, int start, int stop);
  public abstract void constructHeap(int[] vArray, int start, int stop);  
}
