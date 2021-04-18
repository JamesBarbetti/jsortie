package jsortie.janitors.insertion;

public class QuadrupletInsertionSort
  extends PairInsertionSortRevised {
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    if (stop-start<2) {
      return;
    }
    bubbleMinimumToLeft(vArray, start, stop);
    sortSentinelledSubRange(vArray, start+1, stop);
  }
  public static void sortSentinelledSubRange
    (int[] vArray, int start, int stop) {
    while (((stop-start)&3) !=0 ) {
      SentinelInsertionSort.bubbleMinimumToLeft(vArray, start, stop);
      ++start;
    } 
    for (int x = start; x<stop; x+=4) {
      sort4(vArray, x);
      int v1=vArray[x];
      int v2=vArray[x+1];
      int v3=vArray[x+2];
      int v4=vArray[x+3];
      int w=x-1;
      for (; v4<vArray[w]; --w ) {
        vArray[w+4] = vArray[w];
      }
      vArray[w+4] = v4;
      for ( ; v3<vArray[w]; --w ) {
        vArray[w+3] = vArray[w];
      }
      vArray[w+3] = v3;
      for (; v2<vArray[w]; --w ) {
        vArray[w+2] = vArray[w];
      }
      vArray[w+2] = v2;
      for (; v1<vArray[w]; --w ) {
        vArray[w+1] = vArray[w];
      }
      vArray[w+1] = v1;
    }
  }
  public static void sort4(int[] vArray, int start) {
    sort2(vArray, start,   start+2);
    sort2(vArray, start+1, start+3);
    sort2(vArray, start,   start+1); //minimum at vArray[start]
    sort2(vArray, start+2, start+3); //maximum at vArray[start+3]
    sort2(vArray, start+1, start+2); //middle two items in order
  }
  public static void sort2(int[] vArray, int a, int b) {
    int x = vArray[a];
    int y = vArray[b];
    boolean swap = (y<x);
    vArray[a] = swap ? y : x;
    vArray[b] = swap ? x : y;
  }
}
