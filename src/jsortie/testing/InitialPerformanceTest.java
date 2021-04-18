package jsortie.testing;

import jsortie.quicksort.governor.traditional.QuicksortClassic;
import jsortie.quicksort.sort.reference.QuicksortJava8;

public class InitialPerformanceTest {	
  protected SortTest sortTest = new SortTest();
  public void testClassicVersusJava8() {
    SortList sorts = new SortList();
    sorts.add ( new QuicksortClassic() );
    sortTest.testSpecificSorts( sorts, 100, 20000, 1000, 10000000, 25);
    sorts.add ( new QuicksortJava8() );
    sorts.warmUp();
    sortTest.testSpecificSorts( sorts, 100, 20000, 1000, 10000000, 25);
  }
}
