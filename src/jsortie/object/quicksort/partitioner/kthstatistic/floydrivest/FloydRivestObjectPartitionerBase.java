package jsortie.object.quicksort.partitioner.kthstatistic.floydrivest;

import java.util.Comparator;

import jsortie.helper.DumpRangeHelper;
import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.object.quicksort.expander.LeftLomutoObjectExpander;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.expander.RightLomutoObjectExpander;
import jsortie.object.quicksort.expander.SingletonObjectExpander;
import jsortie.object.quicksort.helper.ObjectShiftHelper;
import jsortie.object.quicksort.partitioner.kthstatistic.KthStatisticObjectPartitionerBase;
import jsortie.object.quicksort.partitioner.kthstatistic.remedian.RemedianObjectPartitioner;
import jsortie.object.quicksort.selector.KthStatisticObjectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector.FloydRivestSamplePartitionSelector;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector.ThreeWaySamplePartitionSelector;
import jsortie.quicksort.samplesizer.FloydRivestSampleSizer;

public abstract class FloydRivestObjectPartitionerBase<T> 
  extends KthStatisticObjectPartitionerBase<T> {
  //
  //Notes: 
  // 1. Originally, this was FloydRivestObjectPartitioner
  //    (but I made it a base class, and renamed it, 
  //    so that FloydRivestObjectPartitioner could focus
  //    on the "doublePartition" method.
  //    (Note to self: this comment dated, 20-Dec-2019) 
  //
  protected ObjectShiftHelper<T> shifty 
    = new ObjectShiftHelper<T>();
  protected KthStatisticObjectPartitioner<T>
    lastResort;
  protected double  comparisonCount = 0;
  protected boolean beLazy          = false;
  protected boolean isFolding       = false;
  protected boolean quintary        = false;
  protected boolean useSafetyNet    = false;
  protected double  alpha           = 18.6624;
  protected boolean traced          = false;
  FancierEgalitarianPartitionerHelper quinny
    = new FancierEgalitarianPartitionerHelper();
    //Only used when quintary == true
  protected ThreeWaySamplePartitionSelector 
   ps = new FloydRivestSamplePartitionSelector();
  
  public FloydRivestObjectPartitionerBase() {
    super ();
    sizer = new FloydRivestSampleSizer(alpha);
    lastResort 
      = new RemedianObjectPartitioner<T>
        ( new LeftLomutoObjectExpander<T>()
        , new RightLomutoObjectExpander<T>()
        , 2, true);
  }
  public FloydRivestObjectPartitionerBase(double a) {
    super ();
    if (a<=1.0) { 
      throw new IllegalArgumentException
        ( "alpha (" + a + ") too low;" 
        + " must be greater than 1");
    }
    alpha = a;
    sizer = new FloydRivestSampleSizer(alpha);
    lastResort 
      = new RemedianObjectPartitioner<T>
        ( new SingletonObjectExpander<T>()
        , new SingletonObjectExpander<T>(), 2, true);
  }
  public FloydRivestObjectPartitionerBase
    ( PartitionObjectExpander<T> expanderOnLeft
    , PartitionObjectExpander<T> expanderOnRight) {
    super(expanderOnLeft, expanderOnRight);
    sizer = new FloydRivestSampleSizer(alpha);
    lastResort 
      = new RemedianObjectPartitioner<T>
            ( expanderOnLeft
            , expanderOnRight, 2, true);
  }
  public void setTraced
    ( boolean b ) {
    traced = b;
  }
  public void setUseSafetyNet
    ( boolean b ) {
    useSafetyNet = b;
  }
  public void setLazyPartitioning
    ( boolean b ) {
    beLazy = b;
  }    
  protected int expandWithOnePivot
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int sampleStart
    , int hole, int sampleStop, int stop
    , PartitionObjectExpander<T> lx
    , PartitionObjectExpander<T> rx) {
    hole = lx.expandPartition
                ( comparator, vArray
                , start, sampleStart, hole
                , sampleStop, sampleStop);
    if (traced) {
      DumpRangeHelper.dumpRange("dp: lx", vArray, start, sampleStop);
      System.out.println("hole at " + hole);
    }
    hole = rx.expandPartition
           ( comparator, vArray
           , sampleStart, sampleStart, hole
           , sampleStop, stop); 
    if (traced) {
      DumpRangeHelper.dumpRange("dp: lx", vArray, start, sampleStop);
      System.out.println("hole at " + hole);
    }
    return hole;
  }
  public int getTimeToLive(int count) {
    return count*8;
  }
  public int collectSample
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int sampleStart, int sampleStop ) {
    collector.moveSampleToPosition
    ( comparator, vArray, start, stop
    , sampleStart, sampleStop );
    return 0;
  }
  public void setAlpha(double a) {
    if (a<=1) {
      throw new IllegalArgumentException
                ( "Alpha " + a + " too low."
                +" Must be more than 1");
    }
    alpha = a;
    sizer = new FloydRivestSampleSizer(alpha);
  }
}