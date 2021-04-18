package jsortie.testing.object;

import java.util.ArrayList;
import java.util.List;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.quicksort.governor.ObjectQuicksortTwoPartitioner;
import jsortie.object.quicksort.multiway.governor.MultiPivotObjectQuicksort;
import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.SingleToMultiObjectPartitioner;
import jsortie.object.quicksort.multiway.selector.MultiPivotObjectSelector;
import jsortie.object.quicksort.multiway.selector.SingleToMultiObjectSelector;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.selector.MiddleElementObjectSelector;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class ObjectSortList<T> 
  extends ArrayList<ObjectRangeSorter<T>> {
  private static final long serialVersionUID = 1L;
  SinglePivotObjectSelector<T> singlePivotSelector;
  MultiPivotObjectSelector<T>  multiPivotSelector;
  ObjectRangeSorter<T>         janitor;
  int                          janitorThreshold;
  ObjectRangeSorter<T>         lastResortSort;
  List<String>                 names = new ArrayList<String>();
  public ObjectSortList() {
    singlePivotSelector = new MiddleElementObjectSelector<T>();
    janitor             = new ObjectBinaryInsertionSort<T>();
    janitorThreshold    = 64;
    lastResortSort      = new ObjectHeapSort<T>();
  }
  public ObjectSortList
    ( ObjectRangeSorter<T> janitorToUse, int threshold) {
    singlePivotSelector = new MiddleElementObjectSelector<T>();
    janitor             = janitorToUse;
    janitorThreshold    = threshold;
    lastResortSort      = new ObjectHeapSort<T>();
  }
  public ObjectSortList
    ( SinglePivotObjectSelector<T> selector
    , ObjectRangeSorter<T> janitorToUse, int threshold) {
    singlePivotSelector = selector;
    janitor             = janitorToUse;
    janitorThreshold    = threshold;
    lastResortSort      = new ObjectHeapSort<T>();
  }
  public ObjectSortList 
    ( MultiPivotObjectSelector<T> mpSelector
    , ObjectRangeSorter<T> janitorToUse,  int threshold) {
    singlePivotSelector = new MiddleElementObjectSelector<T>();
    multiPivotSelector  = mpSelector;
    janitor             = janitorToUse;
    janitorThreshold    = threshold;
    lastResortSort      = new ObjectHeapSort<T>();
  }
  public void setSelector( SinglePivotObjectSelector<T> sps) {
    singlePivotSelector = sps;
    multiPivotSelector  = new SingleToMultiObjectSelector<T>(sps);
  }
  public void setSelector( MultiPivotObjectSelector<T> mps) {
    multiPivotSelector = mps;
  }
  public void setJanitorThreshold(int threshold) {
    janitorThreshold = threshold;
  }
  public void addMPQ(MultiPivotObjectPartitioner<T> party) {
    super.add(new MultiPivotObjectQuicksort<T>
            ( getMultiPivotSelector(), party
            , janitor, janitorThreshold, lastResortSort));
    names.add(party.toString());
  }
  public void addAdaptiveMPQ(MultiPivotObjectPartitioner<T> party) {
    super.add(new MultiPivotObjectQuicksort<T>
            ( getMultiPivotSelector(), party
            , janitor, janitorThreshold, lastResortSort));
    names.add(party.toString() + "-Adaptive");	  
  }
  public void addAdaptiveMPQ(SinglePivotObjectPartitioner<T> spp) {
    addAdaptiveMPQ(new SingleToMultiObjectPartitioner<T>(spp));
  }
  public void addSPQ(SinglePivotObjectPartitioner<T> party) {
    super.add( new MultiPivotObjectQuicksort<T> 
    ( new SingleToMultiObjectSelector<T>( singlePivotSelector )
    , new SingleToMultiObjectPartitioner<T> (party)
    , janitor, janitorThreshold, lastResortSort ) );
    names.add(party.toString());
  }
  public void addSPQ(SinglePivotObjectPartitioner<T> party, String name) {
    super.add( new MultiPivotObjectQuicksort<T> 
    ( new SingleToMultiObjectSelector<T>( singlePivotSelector )
    , new SingleToMultiObjectPartitioner<T> (party)
    , janitor, janitorThreshold, lastResortSort ) );
    names.add(name);
  }
  public void add2PQ ( SinglePivotObjectPartitioner<T> party1
                     , SinglePivotObjectPartitioner<T> party2) {
    @SuppressWarnings("unchecked")
    SinglePivotObjectPartitioner<T>[] parties 
      = (SinglePivotObjectPartitioner<T>[]) new Object[] { party1, party2 };
    super.add( new ObjectQuicksortTwoPartitioner<T>
             ( singlePivotSelector, parties, janitor
             , janitorThreshold, lastResortSort ) );
    names.add(party1.toString() + " + "  + party2.toString());
  }
  @Override
  public boolean add( ObjectRangeSorter<T> s) {
    boolean rc = super.add(s);
    if (rc) {
      names.add(s.toString());
    }
    return rc;
  }
  public boolean add( ObjectRangeSorter<T> s, String name) {
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
  protected MultiPivotObjectSelector<T> getMultiPivotSelector() {
    return ( multiPivotSelector != null )
      ? multiPivotSelector
      : new SingleToMultiObjectSelector<T>(singlePivotSelector);	
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
  public String getSortName(int s) {
    return names.get(s);
  }
}  

