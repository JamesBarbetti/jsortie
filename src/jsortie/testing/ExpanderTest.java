package jsortie.testing;

import java.util.Arrays;

import org.junit.Test;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.bidirectional.HoareExpander;
import jsortie.quicksort.protector.CheckedPartitionExpander;

public class ExpanderTest 
  extends PartitionerTest {
  public void testExpander(PartitionExpander x) {
    for (int r=1; r<100; ++r) {
      int[] vData = random.randomPermutation(15);
      PartitionExpander x2 = new CheckedPartitionExpander(x);
      for (int i=0; i<15; ++i) {
        int[] vCopy = Arrays.copyOf(vData, vData.length);
        int stopLeft   = (i/5)*5;
        int hole       = (i%5) + stopLeft;
        int startRight = stopLeft+5;
        InsertionSort.sortSmallRange(vCopy, stopLeft, startRight);
        /*System.out.println
          ( "stopLeft=" + stopLeft 
          + ", hole=" + hole 
          + ", startRight=" + startRight);*/
        x2.expandPartition(vCopy, 0, stopLeft, hole, startRight, 15);
      }
    }
  }
  @Test
  public void testHoareExpander() {
    testExpander(new HoareExpander());
  }
}
