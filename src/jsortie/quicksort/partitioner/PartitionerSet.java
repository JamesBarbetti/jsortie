package jsortie.quicksort.partitioner;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.bidirectional.CentripetalExpander;
import jsortie.quicksort.expander.bidirectional.SingletonExpander;
import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.expander.branchavoiding.SkippyExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.LeftTunedExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightTunedExpander;
import jsortie.quicksort.expander.branchavoiding.TunedExpander;
import jsortie.quicksort.expander.unidirectional.LeftLomutoExpander;
import jsortie.quicksort.expander.unidirectional.LomutoExpander;
import jsortie.quicksort.expander.unidirectional.RightLomutoExpander;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.BalancedSkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.FlowerArrangementPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyCentripetalPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.TunedMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.TunedPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoMirrorPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;

public class PartitionerSet {
  String m_name;
  protected PartitionExpander m_lx, m_rx, m_cx;
  protected SinglePivotPartitioner m_lp, m_rp, m_cp;
  
  public String toString() { return m_name; }
  
  public PartitionExpander getLeftExpander()   { return m_lx; }
  public PartitionExpander getCentreExpander() { return m_cx; }
  public PartitionExpander getRightExpander()  { return m_rx; }
  
  public SinglePivotPartitioner getLeftPartitioner()   { return m_lp; }
  public SinglePivotPartitioner getCentrePartitioner() { return m_cp; } 
  public SinglePivotPartitioner getRightPartitioner()  { return m_rp; }
  
  public PartitionerSet
   ( String name, PartitionExpander lx, PartitionExpander cx, PartitionExpander rx
   , SinglePivotPartitioner lp, SinglePivotPartitioner cp, SinglePivotPartitioner rp) {
    m_name = name;
    m_lx = lx;
    m_cx = cx;
    m_rx = rx;
    m_lp = lp;
    m_cp = cp;
    m_rp = rp;
  }
  public static PartitionerSet CENTRIPETAL 
    = new PartitionerSet
      ( "Centripetal", new CentripetalExpander(), new CentripetalExpander()
      , new CentripetalExpander(), new CentripetalPartitioner()
      , new CentripetalPartitioner(), new CentripetalPartitioner());  
  public static PartitionerSet FLOWER 
    = new PartitionerSet
      ( "Flower"
          , new SkippyCentripetalExpander(), new SkippyCentripetalExpander()
      , new SkippyCentripetalExpander(), new FlowerArrangementPartitioner()
      , new FlowerArrangementPartitioner(), new FlowerArrangementPartitioner());
  public static PartitionerSet SKIPPY 
    = new PartitionerSet
      ( "Skippy", new LeftSkippyExpander(), new SkippyExpander()
      , new RightSkippyExpander(), new BalancedSkippyPartitioner()
      , new SkippyPartitioner(), new SkippyMirrorPartitioner());
  public static PartitionerSet SKIPPY_CENTRIPETAL 
    = new PartitionerSet
      ( "SkippyCentripetal"
          , new SkippyCentripetalExpander(), new SkippyCentripetalExpander()
      , new SkippyCentripetalExpander(), new SkippyCentripetalPartitioner()
      , new SkippyCentripetalPartitioner(), new SkippyCentripetalPartitioner());
  public static PartitionerSet LOMUTO 
    = new PartitionerSet
      ( "Lomuto", new LeftLomutoExpander(), new LomutoExpander()
      , new RightLomutoExpander(), new LomutoPartitioner()
      , new LomutoPartitioner(), new LomutoMirrorPartitioner());
  public static PartitionerSet SINGLETON
    = new PartitionerSet
      ( "Singleton", new SingletonExpander(), new SingletonExpander()
      , new SingletonExpander(), new SingletonPartitioner()
      , new SingletonPartitioner(), new SingletonPartitioner());
  public static PartitionerSet TUNED
    = new PartitionerSet
      ( "Tuned", new LeftTunedExpander(), new TunedExpander()
      , new RightTunedExpander(), new TunedPartitioner()
      , new TunedPartitioner(), new TunedMirrorPartitioner());  
}
