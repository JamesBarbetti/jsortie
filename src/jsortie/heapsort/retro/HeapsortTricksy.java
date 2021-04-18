package jsortie.heapsort.retro;

public class HeapsortTricksy 
  extends HeapsortOriginal {
  @Override
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    for (int j=start+1;j<stop;++j) {
      int i = j;
      do {
        int h = (i-start+1)/2 + start - 1;
        int vBelow = vArray[h];
        int vAbove = vArray[i];
        boolean swapped = vAbove < vBelow;
        vArray[h] = swapped ? vBelow : vAbove;
        vArray[i] = swapped ? vAbove : vBelow;
        i = h;
      } while (start<i);
    }
  }
}
