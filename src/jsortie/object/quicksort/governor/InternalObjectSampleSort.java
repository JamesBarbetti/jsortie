package jsortie.object.quicksort.governor;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.collector.ObjectSampleCollector;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.quicksort.helper.ObjectPartitionHelper;
import jsortie.object.quicksort.helper.ObjectShiftHelper;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.MiddleIndexSelector;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class InternalObjectSampleSort<T> 
  implements ObjectRangeSorter<T> {
  protected OversamplingSampleSizer   sizer;
  protected ObjectSampleCollector<T>  collector;
  protected SinglePivotObjectPartitioner<T>  partitioner;
  protected IndexSelector             selector;
  protected ObjectShiftHelper<T>      shifter;
  protected boolean                   handleDuplicates;
  protected ObjectPartitionHelper<T>  helper;
  protected int                       janitorThreshold;
  protected ObjectRangeSorter<T>      janitor;
  protected ObjectRangeSorter<T>      lastResort;
  public String toString() {
    return this.getClass().getSimpleName()
      + "( " + collector.toString() 
      + ", " + partitioner.toString()
      + ", " + janitor.toString() 
      + ", " + janitorThreshold + ")";
  }	
  public InternalObjectSampleSort
    ( OversamplingSampleSizer  sampleSizer
    , ObjectSampleCollector<T> sampleCollector
    , SinglePivotObjectPartitioner<T> partitionerToUse
    , ObjectRangeSorter<T>     janitorToUse
    , int threshold
    , boolean isTunedForDuplicates) {
    sizer       = sampleSizer;
    collector   = sampleCollector;
    partitioner = partitionerToUse;
    selector    = new MiddleIndexSelector();
    shifter     = new ObjectShiftHelper<T>();
    helper      = new ObjectPartitionHelper<T>();
    janitorThreshold  = threshold;
    janitor     = janitorToUse;
    lastResort  = new ObjectHeapSort<T>();
    handleDuplicates = isTunedForDuplicates;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int maxDepth = 0;
    if (start<stop) {
      maxDepth = (int)Math.floor
       ( 2.0 * Math.log(stop-start) / Math.log(2.0) );
    }
    sortRange ( comparator, vArray, start, start+1
              , stop, stop, maxDepth);
  }  
  protected void sortRange
    ( Comparator<? super T> comp
    , T[] vArray, int a, int b, int c, int d
    , int   maxDepth) {
    while (janitorThreshold < d-a) {
      if ( maxDepth < 1 ) {
        //ensure n.log(n) running time; otherwise
        //there'd be a risk of n.log(n).log(n) running
        //time.
        lastResort.sortRange(comp, vArray, a, d);
        return;
      }
      if ( c == d ) {
        //no sorted sample on right
        int b2;
        if (a==b) {
          //no sorted sample on left either. Go get one!
          int t = sizer.getSampleSize(d-a, 2);
          b2 = a + t;
          collector.moveSampleToPosition
            ( comp, vArray, a, d, a, b2);
          sortRange(comp, vArray, a, b, b2, b2, maxDepth-1);
        } else {
          b2 = b;
        }
        //split the sorted sample, move the right half
        //of the sample to the far right of the range,
        //and partition the unsorted items
        b = selector.selectIndices(a, b2, 1)[0];
        int rightSampleCount = b2 - b - 1;
        if (0<rightSampleCount) {
          c = d - rightSampleCount;      
          shifter.moveFrontElementsToBack
                  ( vArray, b+1, b2, d );
        }
      } else {
        int c2 = c;
        //sorted sample on the right. 
        //split the sample, move the left half
        //of the sample (and the pivot) to the 
        //far left of the range and partition the 
        //unsorted items
        c = selector.selectIndices(c2, d, 1)[0] + 1;
        if (d<c) {
          c=d;
        }
        int leftSampleCount = c - c2; 
        //includes pivot, so always at least 1
        b = a + leftSampleCount - 1;
        shifter.moveBackElementsToFront
                ( vArray, a, c2, c );
      }
      int pivotIndex = partitioner.partitionRange
                       ( comp, vArray, b, c, b );
      //handle situations where there are many items
      //equal to the pivot (go three-way on those).
      T vPivot = vArray[pivotIndex];
      int p1   = pivotIndex;
      int p2   = pivotIndex;
      if (handleDuplicates) {
        if ( a < p1 && comp.compare(vArray[p1-1],vPivot ) == 0 )  {
          while ( a<b && comp.compare(vArray[b-1],vPivot) == 0 ) { 
           --b;
          }
          p1 = helper.swapEqualToRight
               ( comp, vArray, b, p1, vPivot );
        }
        if ( p2+1 < d && comp.compare(vArray[p2+1],vPivot )==0) {
          while ( c<d && comp.compare(vArray[c], vPivot)==0) {
            ++c;
          }
          p2 = helper.swapEqualToLeft
               ( comp, vArray, p2, c, vPivot ) - 1;
        }
      }
      //recurse for the smaller partition, and
      //zoom in on the larger
      --maxDepth;
      int samplingCutoff
        = sizer.getOverSamplingFactor(d-a, 2);
      b = (b-a <= samplingCutoff ) ? a : b;
      c = (d-c <= samplingCutoff ) ? d : c;
      if ( p1 - a < d - p2) {
        sortRange(comp, vArray, a, b, p1, p1, maxDepth);
        a = b = p2 + 1;
      } else {
        sortRange(comp, vArray, p2+1, p2+1, c, d, maxDepth);
        c = d = p1;
      }     
    }
    janitor.sortRange(comp, vArray, a, d);
  }  
}
