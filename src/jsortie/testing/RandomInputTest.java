package jsortie.testing;

import org.junit.Test;

import jsortie.quicksort.indexselector.indexset.HashIndexSet;
import jsortie.quicksort.indexselector.indexset.IndexSet;
import jsortie.quicksort.indexselector.indexset.TreeIndexSet;

public class RandomInputTest {
	RandomInput randy = new RandomInput();
  @Test
  public void testHashIndexSet() {
    int          n      = 1000;
    int          r      = 100;
    HashIndexSet s      = new HashIndexSet(0, n, r);
    TreeIndexSet t      = new TreeIndexSet();
    IndexSet[]   x      = new IndexSet[] { s, t };
    int[]        input  = new int[n];
    for (int i=0; i<n; ++i) {
      input[i] = (int) Math.floor(Math.random() * n);
    }
    for (IndexSet set : x) {
      int[]        output = new int[r]; 
      long start = System.nanoTime();
      int c=0;
      for (int i=0; i<n && c<r; ++i) {
        c += set.merge(i) ? 1 : 0;
      }
      int w = set.emit(output, 0);
      System.out.println( "" + w + "\t" + (System.nanoTime() - start) );
    }
  }
}
