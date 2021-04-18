package jsortie.object.quicksort.governor;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.collector.ObjectSampleCollector;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class ObjectSymmetryPartitionSort<T> 
  extends InternalObjectSampleSort<T>
	implements ObjectRangeSorter<T> {
  public ObjectSymmetryPartitionSort
    ( OversamplingSampleSizer sampleSizer
    , ObjectSampleCollector<T> sampleCollector
    , SinglePivotObjectPartitioner<T> partitionerToUse
    , ObjectRangeSorter<T> janitorToUse, int threshold
    , boolean isTunedForDuplicates) {
    super ( sampleSizer, sampleCollector, partitionerToUse
          , janitorToUse, threshold, isTunedForDuplicates);
  }
  public ObjectSymmetryPartitionSort
    ( OversamplingSampleSizer sampleSizer
    , ObjectSampleCollector<T> sampleCollector
    , IndexSelector indexSelector 
    , SinglePivotObjectPartitioner<T> partitionerToUse
    , ObjectRangeSorter<T> janitorToUse, int threshold
    , boolean isTunedForDuplicates) {
    super ( sampleSizer, sampleCollector, partitionerToUse
          , janitorToUse, threshold, isTunedForDuplicates);
    this.selector = indexSelector;
  }  
  @Override
  protected void sortRange
    ( Comparator<? super T> comp, T[] vArray
    , int a, int b, int c, int d, int   maxDepth) {
    while (janitorThreshold < d-a) {
      if ( maxDepth < 1 ) {
        //ensure n.log(n) running time; otherwise
        //there'd be a risk of n.log(n).log(n) running
        //time.
        lastResort.sortRange(comp, vArray, a, d);
        return;
      }
      if ( c == d ) {
        //sorted sample on the left. expand if need be
        int t = sizer.getSampleSize(d-a, 2);
        int b2 = a + t;        
        if (b+(b-a)<b2 && a+t<d-t) {
          if (b==a) ++b;
          collector.moveSampleToPosition
            ( comp, vArray, b, d, b, b2 );
          sortRange
            ( comp, vArray, a, b, b2, b2
            , maxDepth-1);
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
        //sorted sample on the right. expand if need be
        int t  = sizer.getSampleSize(d-a, 2);
        int c2 = d-t;
        if (a+t<=d-t && c2<c+(c-d)) {
          if (c==d) {
            --c;
          }
          collector.moveSampleToPosition
            ( comp, vArray, a, c, c2, c );
          sortRange
            ( comp, vArray, c2, c2, c, d
            , maxDepth-1);
        } else {
          c2 = c;
        }
        //split the sample, move the left half
        //of the sample (and the pivot) to the 
        //far left of the range and partition the 
        //unsorted items
        c = selector.selectIndices(c2, d, 1)[0] + 1;
        if (d<c) {
          c=d;
        }
        int leftSampleCount = c - c2; 
        //includes pivot, always at least 1
        b = a + leftSampleCount - 1;
        shifter.moveBackElementsToFront
                ( vArray, a, c2, c );
      }
      int pivotIndex = partitioner.partitionRange
                         ( comp, vArray, b, c, b );
      int p1   = pivotIndex;
      int p2     = pivotIndex;
      if (handleDuplicates) {
        T vPivot = vArray[pivotIndex];
        if ( a < p1 && comp.compare(vArray[p1-1], vPivot ) == 0 ) {
          while ( a<b && comp.compare(vArray[b-1], vPivot) == 0 ) {
            --b;
          }
          p1 = helper.swapEqualToRight
               ( comp, vArray, b, p1, vPivot );
        }
        if ( p2+1 < d && comp.compare(vArray[p2+1], vPivot ) == 0 ){
          while ( c<d && comp.compare(vArray[c], vPivot) == 0 ) {
            ++c;
          }
          p2 = helper.swapEqualToLeft
               ( comp, vArray, p2, c, vPivot ) - 1;
        }
      }
      //recurse for the smaller partition, and
      //zoom in on the larger
      --maxDepth;
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
