package jsortie.testing;

import java.util.Random;

import jsortie.RangeSorter;
import jsortie.helper.DumpRangeHelper;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.mergesort.hybrid.WinnowingSort2;


public class WinnowingSortTest {
  protected DegenerateInput degen = new DegenerateInput();
  public void testWinnowingSort() {
    Random r = new Random();
    r.setSeed(0);
    int [] aa = degen.postRandomExchangePermutation(100, 9);
    DumpRangeHelper.dumpRange( "data  ", aa, 0, aa.length);
    class WSort extends WinnowingSort2
    {
      public WSort(RangeSorter innerSorter) {
        super(innerSorter);
      }
      public int winnow
       ( int[] vArray, int start, int stop ) {
       WinnowingResult w = new WinnowingResult();
       winnow(vArray, start, stop, w);
       System.out.println( "left sorted = " 
                        + w.isLeftSorted);
        return w.startOfChaff;
      }
    };    
    WSort s = new WSort(new InsertionSort2Way());        
    int rhs = s.winnow(aa, 0, aa.length);
    System.out.println("rhs was " + rhs);
    DumpRangeHelper.dumpRange( "grain ", aa, 0, rhs);
    int i;
    for (i=1;i<rhs;++i) {
      if (aa[i]<aa[i-1]) {
        break;
      }
      if (i==rhs) System.out.println("(grain is now in sort)");
      if (rhs<aa.length) DumpRangeHelper.dumpRange( "chaff ", aa, rhs, aa.length);		
	}
  }
}
