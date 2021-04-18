package jsortie.testing;

import java.util.ArrayList;
import java.util.List;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.NullEarlyExitDetector;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive2;
import jsortie.quicksort.governor.adaptive.QuicksortTwoPartitioner;
import jsortie.quicksort.multiway.governor.AdaptiveMultiPivotQuicksort;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.adapter.SingleToMultiSelector;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class SortList     extends ArrayList<RangeSorter> {
  private static final long serialVersionUID = 1L;
  SinglePivotSelector singlePivotSelector;
  MultiPivotSelector  multiPivotSelector;
  RangeSorter         janitor;
  int                 janitorThreshold;
  RangeSorter         lastResortSort;
  RangeSortEarlyExitDetector eed = new NullEarlyExitDetector();
  List<String>        names = new ArrayList<String>();
  public SortList() {
    singlePivotSelector = new MiddleElementSelector();
    janitor             = new InsertionSort2Way();
    janitorThreshold    = 64;
    lastResortSort      = new TwoAtATimeHeapsort();
  }
  public SortList( RangeSorter janitorToUse, int threshold) {
    singlePivotSelector = new MiddleElementSelector();
    janitor             = janitorToUse;
    janitorThreshold    = threshold;
    lastResortSort      = new TwoAtATimeHeapsort();
  }
  public SortList
    ( SinglePivotSelector selector, RangeSorter janitorToUse
    , int threshold) {
    singlePivotSelector = selector;
    janitor             = janitorToUse;
    janitorThreshold    = threshold;
    lastResortSort      = new TwoAtATimeHeapsort();
  }
  public SortList 
    ( MultiPivotSelector mpSelector, RangeSorter janitorToUse
    , int threshold) {
    singlePivotSelector = new MiddleElementSelector();
    multiPivotSelector  = mpSelector;
    janitor             = janitorToUse;
    janitorThreshold    = threshold;
    lastResortSort      = new TwoAtATimeHeapsort();
  }
  public void setSelector( MultiPivotSelector mps) {
    multiPivotSelector = mps;
  }
  public void setJanitorThreshold(int threshold) {
    janitorThreshold = threshold;
  }
  public void setEarlyExitDetector(RangeSortEarlyExitDetector earlyOut) {
    eed = earlyOut;
  }
  public RangeSorter addMPQ(MultiPivotPartitioner party) {
    RangeSorter sorter = new MultiPivotQuicksort
    ( getMultiPivotSelector(), party
    , janitor, janitorThreshold);
    super.add(sorter);
    names.add(party.toString());
    return sorter;
  }
  public RangeSorter addAdaptiveMPQ(MultiPivotPartitioner party) {
    RangeSorter sorter = new AdaptiveMultiPivotQuicksort
        ( getMultiPivotSelector(), party
        , janitor, janitorThreshold
        , lastResortSort, eed);
    super.add(sorter);
    names.add(party.toString() + "-Adaptive");
    return sorter;
  }
  public RangeSorter addAdaptiveMPQ
    ( MultiPivotSelector selector
    , MultiPivotPartitioner party
    , int threshold
    , RangeSorter janitor) {
    RangeSorter sorter = new AdaptiveMultiPivotQuicksort
        ( selector, party
        , janitor, threshold
        , lastResortSort, eed);
    super.add(sorter);
    names.add(party.toString() + "-Adaptive");
    return sorter;
  }
  public RangeSorter addAdaptiveMPQ(SinglePivotPartitioner spp) {
    return addAdaptiveMPQ(new SingleToMultiPartitioner(spp));
  }
  public RangeSorter addSPQ(SinglePivotPartitioner party) {
    RangeSorter sorter = new QuicksortAdaptive 
        ( singlePivotSelector, party, janitor
        , janitorThreshold, lastResortSort, eed ) ;
    super.add( sorter );
    names.add(party.toString());
    return sorter;
  }
  public RangeSorter addAdaptiveSPQ(SinglePivotPartitioner party) {
    if (singlePivotSelector instanceof CleanSinglePivotSelector) {
      CleanSinglePivotSelector cleanSel 
        = (CleanSinglePivotSelector)singlePivotSelector;
      RangeSorter sorter = new QuicksortAdaptive2 
          ( cleanSel, party, janitor
          , janitorThreshold, lastResortSort ) ;
      super.add( sorter );
      names.add(party.toString());
      return sorter;
    } else {
      return addSPQ(party);
    }
  }
  public RangeSorter add2PQ ( SinglePivotPartitioner party1
                            , SinglePivotPartitioner party2) {
    SinglePivotPartitioner[] parties 
      = new SinglePivotPartitioner[] { party1, party2 };
    RangeSorter sorter = new QuicksortTwoPartitioner
    ( singlePivotSelector, parties, janitor
    , janitorThreshold, lastResortSort, eed );
    super.add( sorter );
    names.add(party1.toString() + " + "  + party2.toString());
    return sorter;
  }
  @Override
  public boolean add( RangeSorter s) {
	boolean rc = super.add(s);
	if (rc) {
      names.add(s.toString());
	}
	return rc;
  }
  public boolean add( RangeSorter s, String name) {
    boolean rc = super.add(s);
    if (rc) {
      names.add(name);
    }
    return rc;
  }
  public void clear() {
    super.clear();
    names.clear();
  }
  protected MultiPivotSelector getMultiPivotSelector() {
    return ( multiPivotSelector != null )
      ? multiPivotSelector
      : new SingleToMultiSelector(singlePivotSelector);	
  }
  public void writeSortHeader(String nameOfFirstColumn) {
    System.out.println(getSortHeader(nameOfFirstColumn));	  
  }
  public String getSortHeader(String nameOfFirstColumn) {
    String header = nameOfFirstColumn;
    for ( String s : names) {
      header += "\t" + s.toString();
    }
    return header;
  }
  public String getSortName(int index) {
    return names.get(index);
  }  
  public void warmUp() {
    (new SortTest()).warmUpSorts(this);
  }
  public void writeSortList() {
    int n = 1;
    for ( String s : names) {
      System.out.println("Sort " + n + " is " + s.toString());
      ++n;
    }
    
  }
}  

