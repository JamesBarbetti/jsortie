package jsortie.testing;

import org.junit.Test;

import jsortie.quicksort.indexselector.indexset.HashIndexSet;

public class TheoryTest {
  public void testPivotHeight(int p) {
    int   n         = 1000000;
    int[] aHeights  = new int[n];
    int   maxHeight = getHeights(0, n, p, aHeights);
    for (int i=0; i<n; ++i) {
      if (maxHeight<aHeights[i]) {
        ++maxHeight;
      }
    }
    int[] aCount = new int[maxHeight+1];
    for (int i=0; i<n; ++i ) {
      ++aCount[aHeights[i]];
    }
    double th = 0;
    for (int j=0; j<maxHeight;++j) {
      double f  = (double)aCount[j]/(double)n;
      double fj = f * (double)j;
      System.out.println(""+j+"\t"+f+"\t"+fj+"\t"+(1.0/f));
      th+=fj;
    }
    System.out.println("Total pivot height " + th);
    System.out.println("");
  }
  private int getHeights(int start, int stop, int p, int[] aHeights) {
    if (stop-start<p+1) {
      return 0;
    }
    int[] pivots   = new int[p];
    HashIndexSet h = new HashIndexSet(start, stop, p);
    h.selectIndices(start, stop, p);
    h.emit(pivots, 0);
    int maxj = 0;
    for (int i=0; i<p; ++i) {
      int j = getHeights(start, pivots[i], p, aHeights);
      if (maxj<j) {
        maxj = j;
      }
      start = pivots[i] + 1;
    }
    int j = getHeights(start, stop, p, aHeights);
    if (maxj<j) {
      maxj = j;
    }
    for (int i=0; i<p; ++i) {
      aHeights[pivots[i]]=maxj+1;
    }
    return maxj+1;
  }
  @Test
  public void testPivotHeight() {
    for (int p=1; p<3; ++p) {
      testPivotHeight(p);
    }
  }
}
