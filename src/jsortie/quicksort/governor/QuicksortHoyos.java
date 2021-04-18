package jsortie.quicksort.governor;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class QuicksortHoyos 
  extends QuicksortGovernor {
  public QuicksortHoyos() {
    super ( new MiddleElementSelector()
          , new HoyosPartitioner()
          , new InsertionSort(), 64);
  }
  public static void sort(int [] vArray) {
    (new QuicksortHoyos()).sortRange
    ( vArray,  0,  vArray.length );
  }
}