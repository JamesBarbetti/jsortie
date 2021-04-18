package jsortie.testing;

import org.junit.Test;

import jsortie.ClaytonsSort;
import jsortie.RangeSorter;
import jsortie.earlyexitdetector.NullEarlyExitDetector;
import jsortie.earlyexitdetector.OrsonPetersEarlyExitDetector;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightEarlyExitDetector;
import jsortie.heapsort.topdown.HeapsortStandard;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.janitors.insertion.PairInsertionSort;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyPartitioner2;
import jsortie.quicksort.multiway.protector.CheckedMultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner3;
import jsortie.quicksort.multiway.partitioner.singlepivot.BentleyMcIlroyPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.OptimisticBentleyMcIlroyPartitioner;
import jsortie.quicksort.multiway.partitioner.threepivot.KLQMPartitioner3;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.adapter.SingleToMultiSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;
import jsortie.quicksort.multiway.tracer.TracedMultiPivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoarePartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.PDQPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.TunedPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.protector.CheckedRangeSorter;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanLeftHandedSelector;
import jsortie.quicksort.selector.clean.CleanTheoreticalSelector;
import jsortie.quicksort.selector.dirty.DirtySingletonSelector;
import jsortie.quicksort.selector.dirty.DirtyTukeySelector;
import jsortie.quicksort.selector.simple.FirstElementSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.selector.simple.RandomElementSelector;

public class HistoricalIntegerQuicksorts {
  protected SortTest sortTest = new SortTest();
  protected RandomInput random = new RandomInput();
  class SPQ extends QuicksortGovernor {
    protected String myName;
    public    String toString() { return myName; }
    public SPQ ( String name, SinglePivotSelector s, SinglePivotPartitioner p
               , RangeSorter j, int t) {
      super(s, p, j, t);
      myName = name;
    }			
  }
  class SPQA extends QuicksortAdaptive {
    protected String myName;
    public    String toString() { return myName; }
    public SPQA ( String name, SinglePivotSelector s, SinglePivotPartitioner p
                , RangeSorter j, int t, RangeSorter lastResort
                , RangeSortEarlyExitDetector eed) {
      super(s,p,j, t, lastResort, eed);
      myName = name;
    }			
  }
  class MPQ extends MultiPivotQuicksort {
    protected String myName;
    public    String toString() { return myName; }
    public MPQ ( String name, SinglePivotSelector s, SinglePivotPartitioner p
               , RangeSorter j, int t) {
	  super(s, p, j, t);
	  myName = name;
    }
    public MPQ ( String name, MultiPivotSelector s, MultiPivotPartitioner p
               , RangeSorter j, int t) {
      super(s, p, j, t);
      myName = name;
    } 
  }
  class SortTwice implements RangeSorter {
    RangeSorter firstSort;
    RangeSorter secondSort;
    public SortTwice( RangeSorter first, RangeSorter second) {
      firstSort  = first;
      secondSort = second;
    }
    public String toString() { 
      return this.getClass().getSimpleName() + "(" + firstSort.toString()
        + "," + secondSort.toString() + ")";
    }
    @Override
    public void sortRange(int[] vArray, int start, int stop) {
      firstSort.sortRange(vArray, start, stop);
      secondSort.sortRange(vArray, start, stop);
    }
  }
  @Test
  public void testCenty3() {
    MultiPivotPartitioner        centy3 = new CentrifugalPartitioner3();
    MultiPivotSelector           any3   = new SamplingMultiPivotSelector(3, false);
    CheckedMultiPivotPartitioner c      = new CheckedMultiPivotPartitioner(centy3);
    TracedMultiPivotPartitioner  t      = new TracedMultiPivotPartitioner(c);
    RangeSorter                  isort  = new InsertionSort();
    RangeSorter q = new MPQ ("Aumuller + Dietzfelbinge 2015",  any3,  t,  isort, 5);
    CheckedRangeSorter            q2      = new CheckedRangeSorter(q);    
    
    for (int i=0; i<100; ++i) {
      int[] dummy = random.randomPermutation(20);
      q2.sortRange( dummy, 0, dummy.length);
    }
  }
  @Test
  public void testHistoricalIntegerSorts() {

    SamplingMultiPivotSelector select2And4Of5 = new SamplingMultiPivotSelector(5, new int[]{1,3}, false);
    SinglePivotSelector     random        = new RandomElementSelector();
    SinglePivotSelector     middle        = new MiddleElementSelector();
    SinglePivotSelector     first         = new FirstElementSelector();
    SinglePivotSelector     bestOf3       = new DirtySingletonSelector();
    SinglePivotSelector     lefty         = new CleanLeftHandedSelector(false);
    SinglePivotSelector     tukey         = new DirtyTukeySelector();
    MultiPivotSelector      any2          = new SamplingMultiPivotSelector(2, false);
    MultiPivotSelector      any3          = new SamplingMultiPivotSelector(3, false);
    MultiPivotSelector      mBestOf3      = new SingleToMultiSelector(bestOf3);
    SinglePivotPartitioner  hoare         = new HoarePartitioner();
    SinglePivotPartitioner  singleton     = new SingletonPartitioner();
    SinglePivotPartitioner  lomuto        = new LomutoPartitioner();    
    SinglePivotPartitioner  hoyos         = new HoyosPartitioner();
    SinglePivotPartitioner  tuned         = new TunedPartitioner();
    SinglePivotPartitioner  pdq           = new PDQPartitioner();
    SinglePivotPartitioner  skippy        = new SkippyPartitioner();
    MultiPivotPartitioner   bentley       = new BentleyMcIlroyPartitioner();
    MultiPivotPartitioner   oBentley      = new OptimisticBentleyMcIlroyPartitioner();
    MultiPivotPartitioner   yaroslavskiy  = new YaroslavskiyPartitioner2();
    MultiPivotPartitioner   klqm          = new KLQMPartitioner3();
    MultiPivotPartitioner   cent3         = new CentrifugalPartitioner3();
    RangeSorter             insertion     = new InsertionSort();
    RangeSorter             pairInsertion = new PairInsertionSort();
    RangeSorter             heapsort      = new HeapsortStandard();
    RangeSorter             heapsort2     = new TwoAtATimeHeapsort();
    RangeSorter             nojanitor     = new ClaytonsSort();
    RangeSorter             comb          = new BranchAvoidingAlternatingCombsort(); //As sort
    RangeSorter             combover      = new BranchAvoidingAlternatingCombsort(1.4, 128); //As strategic janitor
    RangeSortEarlyExitDetector noDetector = new NullEarlyExitDetector();
    RangeSortEarlyExitDetector wainwright = new WainwrightEarlyExitDetector();
    RangeSortEarlyExitDetector peters     = new OrsonPetersEarlyExitDetector();
    RangeSortEarlyExitDetector jamesEED   = new TwoWayInsertionEarlyExitDetector();
    SinglePivotSelector    theory         = new CleanTheoreticalSelector(.5);
    
    SortList sorts = new SortList();
    sorts.add(new SPQ ("Hoare 1961",            random,   hoare,        insertion,     5));   
    sorts.add(new SPQ ("Scowen 1965",           middle ,  singleton,    insertion,     5));   
    sorts.add(new SPQ ("Singleton 1969",        bestOf3,  singleton,    insertion,     9)); 
    sorts.add(new SPQ ("Knuth 1973",            first,    singleton,    insertion,     9)); 
    sorts.add(new SPQ ("Lomuto 1980",           bestOf3,  lomuto,       insertion,     9));
    sorts.add(new SPQA("Wainwright 1985",       first,    singleton,    insertion,     9,  insertion, wainwright));
    sorts.add(new MPQ ("Bentley-McIlroy 1993",  mBestOf3,  bentley,      insertion,     9));
    sorts.add(new MPQ ("Bentley-McIlroy 1993 (O)", mBestOf3, oBentley,  insertion,     9));
    sorts.add(new SPQA("Musser 1997",           first,    singleton,    insertion,     9,  heapsort,  noDetector));
    sorts.add(new SPQ ("Hoyos 2005",            bestOf3,  hoyos,        insertion,     9));    
    sorts.add(new SPQ ("Kaligosi-Sanders 2006", lefty,    singleton,    insertion,     9));    
    sorts.add(new MPQ ("Yaroslavskiy 2009",     any2,     yaroslavskiy, pairInsertion, 27));
    sorts.add(new MPQ ("Java DPQ 2011",  select2And4Of5,  yaroslavskiy, pairInsertion, 27)); 
    sorts.add(new MPQ ("KLQM 2014",             any3,     klqm,         pairInsertion, 27)); 
    sorts.add(new MPQ ("Aumuller + Dietzfelbinge 2015",  any3,  cent3,  insertion,     27)); 
    sorts.add(new SPQ ("Jyrki Katajainen 2015", tukey,    tuned,        insertion,     32)); 
    sorts.add(new SPQA("Orson Peters 2016",     tukey,    pdq,          insertion,     32, insertion, peters));
    sorts.add(new SortTwice(new SPQA("x", theory,  skippy, nojanitor, 256, heapsort2, jamesEED), combover));
    sorts.add(comb);
    
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts( sorts, 10, 10000001, 25);		
  }
}
