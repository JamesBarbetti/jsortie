package jsortie.quicksort.samplesort;

import jsortie.RangeSorter;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.helper.ShiftHelper;
import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.MiddleIndexSelector;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class InternalSampleSort
  implements RangeSorter {
  protected OversamplingSampleSizer      sizer;
  protected SampleCollector              collector;
  protected SinglePivotPartitioner       partitioner;
  protected IndexSelector                selector;
  protected ShiftHelper                  shifter;
  protected EgalitarianPartitionerHelper partitionerHelper;
  protected int                          janitorThreshold;
  protected RangeSorter                  janitor;
  protected RangeSorter                  lastResort;

  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + collector.toString() + "," + partitioner.toString()
    + "," + janitor.toString()   + "," + janitorThreshold + ")";
  }
  public InternalSampleSort 
    ( OversamplingSampleSizer sampleSizer
    , SampleCollector         sampleCollector
    , SinglePivotPartitioner  partitionerToUse
    , RangeSorter             janitorToUse
    , int threshold) {
    sizer             = sampleSizer;
    collector         = sampleCollector;
    partitioner       = partitionerToUse;
    selector          = new MiddleIndexSelector();
    shifter           = new ShiftHelper();
    partitionerHelper = new EgalitarianPartitionerHelper();
    janitorThreshold  = threshold;
    janitor           = janitorToUse;
    lastResort        = new TwoAtATimeHeapsort();
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop) {
    int maxDepth = 0;
    if (start<stop) {
      maxDepth = (int)Math.floor
       ( 2.0 * Math.log(stop-start) / Math.log(2.0) );
    }
    sortRange(vArray, start, start+1, stop, stop, maxDepth);
  }  
  protected void sortRange
    ( int[] vArray, int a, int b, int c, int d
    , int   maxDepth) {
    while (janitorThreshold < d-a) {
      if ( maxDepth < 1 ) {
        //ensure n.log(n) running time; otherwise
        //there'd be a risk of n.log(n).log(n) running
        //time.
        lastResort.sortRange(vArray, a, d);
        return;
      }
      if ( c == d ) {
        //no sorted sample on right
    	int b2;
    	if (a==b) {
    	  //no sorted sample on left either. Go get one!
          int t = sizer.getSampleSize(d-a, 2);
          b2 = a + t;
          collector.moveSampleToPosition(vArray, a, d, a, b2);
          sortRange(vArray, a, b, b2, b2, maxDepth-1);
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
                       ( vArray, b, c, b );
      //handle situations where there are many items
      //equal to the pivot (go three-way on those).
      int vPivot = vArray[pivotIndex];
      int p1     = pivotIndex;
      if ( a < p1 && vArray[p1-1]==vPivot ) {
        while ( a<b && vArray[b-1]==vPivot) {
          --b;
        }
        p1 = partitionerHelper.swapEqualToRight
             ( vArray, b, p1, vPivot );
      }
      int p2     = pivotIndex;
      if ( p2+1 < d && vArray[p2+1]==vPivot ) {
    	while ( c<d && vArray[c]==vPivot) {
          ++c;
    	}
        p2 = partitionerHelper.swapEqualToLeft
             ( vArray, p2, c, vPivot ) - 1;
      }
      //recurse for the smaller partition, and
      //zoom in on the larger
      --maxDepth;
      int samplingCutoff
      = sizer.getOverSamplingFactor(d-a, 2);
      b = (b-a <= samplingCutoff ) ? a : b;
      c = (d-c <= samplingCutoff ) ? d : c;
      if ( p1 - a < d - p2) {
        sortRange(vArray, a, b, p1, p1, maxDepth);
        a = b = p2 + 1;
      } else {
        sortRange(vArray, p2+1, p2+1, c, d, maxDepth);
        c = d = p1;
      }     
    }
    janitor.sortRange(vArray, a, d);
  }  
}
