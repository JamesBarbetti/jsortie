package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.collector.ObjectNullCollector;
import jsortie.object.collector.ObjectSampleCollector;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.quicksort.helper.ObjectPartitionHelper;
import jsortie.object.quicksort.selector.KthStatisticObjectPartitioner;
import jsortie.quicksort.samplesizer.OneItemSampleSizer;
import jsortie.quicksort.samplesizer.SampleSizer;

public class VanEmdenObjectPartitionerRevised<T> 
  implements SinglePivotObjectPartitioner<T> 
           , KthStatisticObjectPartitioner<T> {
  protected SampleSizer                     sizer;
  protected ObjectSampleCollector<T>        collector;
  protected ObjectRangeSorter<T>            sampleSorter;
  protected SinglePivotObjectPartitioner<T> finisher;
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public VanEmdenObjectPartitionerRevised() {
    this.sizer        = new OneItemSampleSizer();
    this.collector    = new ObjectNullCollector<T>();
    this.sampleSorter = new ObjectHeapSort<T>(); 
    this.finisher     = new SingletonObjectPartitioner<T>(); 
  }
  public VanEmdenObjectPartitionerRevised
    ( SampleSizer sampleSizer
    , ObjectSampleCollector<T> collectorToUse) {
    this.sizer        = sampleSizer;
    this.collector    = collectorToUse;
    this.sampleSorter = new ObjectHeapSort<T>();
    this.finisher     = new SingletonObjectPartitioner<T>(); 
  }
  @Override
  public int partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex ) {
    int count = stop - start;
    if (count<2) {
      return start;
    }
    int middle = pivotIndex;
    if (middle<start) {
      middle = start;
    }
    if (stop<middle+2) {
      middle = stop-2;
    }
    int sampleSize = sizer.getSampleSize(count, 2);
    if (2<=sampleSize&&sampleSize<count) {
      int sampleStart 
        = getSampleStart
          ( start, stop, middle, sampleSize );
      int sampleStop  
        = getSampleStop 
          ( start, stop, middle, sampleSize );
      collector.moveSampleToPosition
        ( comparator, vArray, start, stop
        , sampleStart, sampleStop );
      sampleSorter.sortRange
        ( comparator, vArray, sampleStart, sampleStop );
    }
    T   vLow;
    T   vHigh;
    int p = start;
    int q = stop-1;
    //The elements at [middle] and [middle+1] are the initial pivots
    //We will rig things so that they're out of order, i.e. ensure that
    //vArray[middle+1]<vArray[middle], so we can reduce the
    //number of boundary checks we need to do in inner loops, later.
    if (comparator.compare(vArray[middle] , vArray[middle+1])<0) {
      vLow             = vArray[middle];
      vHigh            = vArray[middle+1];
      vArray[middle]   = vHigh;
      vArray[middle+1] = vLow;
    } else {
      vLow  = vArray[middle+1];
      vHigh = vArray[middle];
    }
    T vLeft = null;
    T vRight = null;
    for (;;) {
      while ( p<middle 
              && comparator.compare
                 ( vLeft=vArray[p], vLow ) < 0 ) {
        ++p;
      }
      if (p==middle) { //we've reached the middle
        if (middle+6<q) {
          middle = moveToMiddle ( vArray, middle, start
                                , p, q, stop);
        } else {
          return finishFromLeft ( comparator, vArray
                                , start, p, q, stop);
        }
      }
      while ( middle+1<q 
              && comparator.compare
                 ( vHigh, vRight=vArray[q] ) < 0 ) {
        --q;
      }
      if (q==middle+1) { //we've reached the middle
        if (p<middle-5) {
          middle = moveToMiddle ( vArray, middle
                                , start, p, q+1, stop);
        } else {
          return finishFromRight ( comparator, vArray
                                 , start, p, q, stop);
        }
      }
      if (comparator.compare(vHigh,vLeft)<0) { 
        if (comparator.compare(vRight,vLow)<0) {
          //vLeft goes right, vRight goes left, so swap them
          vArray[p] = vRight;
          vArray[q] = vLeft;
          ++p;
          --q;
        } else if (p-start<stop-q) { 
          //vLeft goes high, vRight is a better lower bound
          vArray[q]        = vLeft;
          vArray[p]        = vLow;
          vArray[middle+1] = vRight;
          vLow             = vRight;
          ++p;
          --q;
        } else { 
          //vLeft goes high, (but) vRight is a better *upper* bound
          vArray[q]        = vLeft;
          --q;
          vArray[p]        = vRight;
        }
      } else if (comparator.compare(vRight,vLow)<0) { 
        //vLeft is a better bound, vRight goes left
        if (p-start<stop-q) {  
          //vRight goes low, (but) vLeft is a better lower bound
          vArray[p]        = vRight;
          ++p;
          vArray[q]        = vLeft;
        } else { 
          //vRight goes low, and vLeft is a better upper bound
          vArray[q]        = vHigh;
          vArray[middle]   = vLeft;
          vArray[p]        = vRight;
          vHigh            = vLeft;
          ++p;
          --q;
        }
      } else if (p-start<stop-q){
        //vLeft and vRight are both... better lower bounds
        if (comparator.compare(vRight,vLeft)<0)
        { 
          vRight=vArray[q]; 
          vLeft=vArray[p]; 
        }
        vArray[middle+1] = vLeft;
        vArray[p]        = vLow;
        vLow             = vLeft;
        vArray[q]        = vRight;
        ++p;
      } else { 
        //vLeft and vRight are both... better upper bounds
        if (comparator.compare(vRight,vLeft)<0)
        { 
          vRight=vArray[q]; 
          vLeft=vArray[p]; 
        }
        vArray[middle] = vRight;
        vArray[q]      = vHigh;
        vHigh          = vRight;
        vArray[p]      = vLeft;
        --q;
      }
    } //end of for-loop
  } //end of partitionRange method
  protected int getSampleStart
    ( int start, int stop, int middle, int sampleSize ) {
    int t = middle-sampleSize/2;
    return (start<t) ? t : start;
  }
  protected int getSampleStop
    ( int start, int stop, int middle, int sampleSize ) {
    int t = middle+1+sampleSize/2;
    return (t<stop) ? t : stop;
  }
  private int finishFromLeft
    ( Comparator<? super T> comparator
    , T[] vArray, int start
    , int p, int q, int stop) {  
    //vArray[p] and vArray[p+1] are our bounds; 
    //partition vArray[p+2:q] with them
    int leftStop   = finisher.partitionRange
                     ( comparator, vArray, p
                     , q+1, (p-start <= stop-q) ? p : p+1);
    int rightStart = leftStop+1;
    ObjectPartitionHelper<T> helper 
      = new ObjectPartitionHelper<T>();
    if ( start<leftStop
         && comparator.compare
            ( vArray[leftStop-1]
            , vArray[leftStop] ) == 0 ) {
      leftStop 
        = helper.swapEqualToRight
          ( comparator, vArray, start, leftStop
          , vArray[leftStop] );
    }
    if ( rightStart<stop
         && comparator.compare
            ( vArray[rightStart-1]
            , vArray[rightStart] ) == 0 ) {
      rightStart = helper.swapEqualToLeft
                   ( comparator, vArray, rightStart
                   , stop, vArray[rightStart]);
    }
    return rightStart;
  }
  private int finishFromRight
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int p, int q, int stop) {
    //vArray[q-1] and vArray[q] are our 
    //partition bounds (in reverse order); 
    //partition vArray[p:q-2] with them
    int leftStop = finisher.partitionRange
                   ( comparator, vArray, p
                   , q+1, (p-start <= stop-q) ? (q-1): q);
    int rightStart = leftStop+1;
    ObjectPartitionHelper<T> helper 
      = new ObjectPartitionHelper<T>();
    if ( start<leftStop 
         && comparator.compare
            ( vArray[leftStop-1]
            , vArray[leftStop])==0) {
      leftStop 
        = helper.swapEqualToRight
          ( comparator, vArray, start, leftStop
          , vArray[leftStop] );
    }
    if ( rightStart<stop 
         && comparator.compare
            ( vArray[rightStart-1]
            ,vArray[rightStart] ) == 0 ) {
      rightStart 
        = helper.swapEqualToLeft
          ( comparator, vArray, rightStart, stop
          , vArray[rightStart] );
    }
    return rightStart;
  }
  private int moveToMiddle 
    ( T[] vArray, int pairStart
    , int start, int p, int q, int stop) {
    int middle          = p + (q-p-1) / 2;
    T   vHigh           = vArray[pairStart];
    T   vLow            = vArray[pairStart+1];
    vArray[pairStart]   = vArray[middle];
    vArray[pairStart+1] = vArray[middle+1];
    vArray[middle]      = vHigh;
    vArray[middle+1]    = vLow;
    return middle;
  }
  //KthStatisticObjectPartitioner<T>
  @Override
  public void partitionRangeExactly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int targetIndex) {
    int middle;
    do
    {
      middle = partitionRange
               ( comparator
               , vArray, start, stop
               , targetIndex);
      if (middle<targetIndex) {
        start=middle+1;
      }
      if (targetIndex<targetIndex) {
        stop=middle;
      }
    } while (middle!=targetIndex);
  }
}
