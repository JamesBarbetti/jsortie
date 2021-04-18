package jsortie.quicksort.partitioner.standalone;

import jsortie.RangeSorter;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.samplesizer.OneItemSampleSizer;
import jsortie.quicksort.samplesizer.SampleSizer;

public class VanEmdenPartitionerRevised
  extends VanEmdenPartitioner {
  protected SampleSizer            sizer;
  protected SampleCollector        collector;
  protected RangeSorter            sampleSorter;
  protected SinglePivotPartitioner finisher;
  protected FancierEgalitarianPartitionerHelper helper;

  public VanEmdenPartitionerRevised() {
    this.sizer        = new OneItemSampleSizer();
    this.collector    = new NullSampleCollector();
    this.sampleSorter = new TwoAtATimeHeapsort(); 
    this.finisher     = new HoyosPartitioner(); 
    this.helper       = new FancierEgalitarianPartitionerHelper();
  }
  public VanEmdenPartitionerRevised
    ( SampleSizer sampleSizer
    , SampleCollector collectorToUse) {
    this.sizer        = sampleSizer;
    this.collector    = collectorToUse;
    this.sampleSorter = new TwoAtATimeHeapsort();
    this.finisher     = new HoyosPartitioner(); 
    this.helper       = new FancierEgalitarianPartitionerHelper();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop ) {
    int count = stop - start;
    if (count<2) {
      return new int[] { start, start };
    }
    
    int middle = start + (stop-start-1)/2;
    int sampleSize = sizer.getSampleSize(count, 2);
    
    if (2<=sampleSize&&sampleSize<count) {
      int sampleStart = middle-sampleSize/2;
      int sampleStop  = middle+1+sampleSize/2;
      collector.moveSampleToPosition
        ( vArray, start, stop
        , sampleStart, sampleStop );
      sampleSorter.sortRange
       ( vArray, sampleStart, sampleStop );
    }
      
    int vLow;
    int vHigh;
    int p = start;
    int q = stop-1;
    
    //The middle two elements are the initial pivots
    if (vArray[middle] < vArray[middle+1]) {
      vLow             = vArray[middle];
      vHigh            = vArray[middle+1];
      vArray[middle]   = vHigh;
      vArray[middle+1] = vLow;
    } else {
      vLow  = vArray[middle+1];
      vHigh = vArray[middle];
    }
    
    int vLeft  = 0;
    int vRight = 0;
    for (;;) {
      //todo: shouldn't need second p<middle check; must be a problem
      if (p<middle) while (p<middle && (vLeft=vArray[p]) < vLow) ++p;    	
      if (p==middle) { //we've reached the middle
        if (middle+6<q) {
          middle = moveToMiddle(vArray, middle, start, p, q, stop);
        } else {
          return finishFromLeft(vArray, start, p, q, stop);
        }
      }
      //todo: shouldn't need second middle+1<q check; must be a problem
      if (middle+1<q) while (middle+1<q && vHigh < (vRight=vArray[q])) --q;
      if (q==middle+1) { //we've reached the middle
        if (p<middle-5) {
          middle = moveToMiddle(vArray, middle, start, p, q+1, stop);
        } else {
          return finishFromRight(vArray, start, p, q, stop);
        }
      }
      if (vHigh<vLeft) { 
        if (vRight<vLow) {
          //vLeft goes right, vRight goes left, so swap them
          vArray[p] = vRight;
          vArray[q] = vLeft;
          ++p;
          --q;
        } else if (p-start<stop-q) { //vLeft goes high, vRight is a better lower bound
          vArray[q]        = vLeft;
          vArray[p]        = vLow;
          vArray[middle+1] = vRight;
          vLow             = vRight;
          ++p;
          --q;
        } else { //vLeft goes high, (but) vRight is a better *upper* bound
          vArray[q]        = vLeft;
          --q;
          vArray[p]        = vRight;
        }
      } else if (vRight<vLow) { //vLeft is a better bound, vRight goes left
        if (p-start<stop-q) {  //vRight goes low, (but) vLeft is a better lower bound
          vArray[p]        = vRight;
          ++p;
          vArray[q]        = vLeft;
        } else { //vRight goes low, and vLeft is a better upper bound
          vArray[q]        = vHigh;
          vArray[middle]   = vLeft;
          vArray[p]        = vRight;
          vHigh            = vLeft;
          ++p;
          --q;
        }
      } else if (p-start<stop-q){ //vLeft and vRight are both... better lower bounds
        if (vRight<vLeft) { vRight=vArray[q]; vLeft=vArray[p]; }
        vArray[middle+1] = vLeft;
        vArray[p]        = vLow;
        vLow             = vLeft;
        vArray[q]        = vRight;
        ++p;
      } else { //vLeft and vRight are both... better upper bounds
        if (vRight<vLeft) { vRight=vArray[q]; vLeft=vArray[p]; }
        vArray[middle] = vRight;
        vArray[q]      = vHigh;
        vHigh          = vRight;
        vArray[p]      = vLeft;
        --q;
      }
    } //end of for-loop
  } //end of partitionRange method
  private int[] finishFromLeft(int[] vArray, int start, int p, int q, int stop) {  
    //vArray[p] and vArray[p+1] are our bounds; 
    //partition vArray[p+2:q] with them
    int leftStop   = finisher.partitionRange(vArray, p, q+1, (p-start <= stop-q) ? p : p+1);
    int rightStart = leftStop+1;
    if (start<leftStop && vArray[leftStop-1]==vArray[leftStop]) {
      leftStop = helper.moveUnequalToLeft(vArray, start, leftStop, vArray[leftStop]);
    }
    if (rightStart<stop && vArray[rightStart-1]==vArray[rightStart]) {
      rightStart = helper.moveUnequalToRight(vArray, rightStart, stop, vArray[rightStart]);
    }
    return new int[] { start, leftStop, rightStart, stop };
  }
  private int[] finishFromRight(int[] vArray, int start,  int p, int q, int stop) {
    //vArray[q-1] and vArray[q] are our bounds (in reverse order); 
    //partition vArray[p:q-2] with them
    int leftStop = finisher.partitionRange(vArray, p, q+1, (p-start <= stop-q) ? (q-1): q);
    int rightStart = leftStop+1;
    if (start<leftStop && vArray[leftStop-1]==vArray[leftStop]) {
      leftStop = helper.moveUnequalToLeft(vArray, start, leftStop, vArray[leftStop]);
    }
    if (rightStart<stop && vArray[rightStart-1]==vArray[rightStart]) {
      rightStart = helper.moveUnequalToRight(vArray, rightStart, stop, vArray[rightStart]);
    }
    return new int[] { start, leftStop, rightStart, stop };
  }
  private int moveToMiddle(int[] vArray, int pairStart, int start, int p, int q, int stop) {
    int middle          = p + (q-p-1) / 2;
    int vHigh           = vArray[pairStart];
    int vLow            = vArray[pairStart+1];
    vArray[pairStart]   = vArray[middle];
    vArray[pairStart+1] = vArray[middle+1];
    vArray[middle]      = vHigh;
    vArray[middle+1]    = vLow;
    return middle;
  }
}
