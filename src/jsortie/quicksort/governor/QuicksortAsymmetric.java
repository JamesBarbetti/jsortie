package jsortie.quicksort.governor;

import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.quicksort.governor.introsort.IntroSort;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.selector.clean.CleanRightHandedSelector;

public class QuicksortAsymmetric extends IntroSort {
  public QuicksortAsymmetric() {
    super ( new CleanRightHandedSelector(true), new HoyosPartitioner()
    	  , new TwoAtATimeHeapsort(), 1024, new TwoAtATimeHeapsort() );
  }
  public static void sort(int[] vArray) {
    (new QuicksortAsymmetric()).sortRange(vArray, 0, vArray.length);		
  }
};