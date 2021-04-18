package jsortie.object.quicksort.linkedlist;

import java.util.Comparator;

public class FancierLinkedListQuicksort<T>
  extends SamplingLinkedListQuicksort<T> {
  public FancierLinkedListQuicksort(int i) {
    super(i);
  }
  public class FancierFrame 
    extends SortingFrame {
    Object[] searchTree;
    public Object[] getTree(int pivotCount) {
      if ( searchTree == null || 
           searchTree.length<pivotCount) {
        searchTree = new Object[pivotCount];
      }
      //construct an implicit tree, 
      //in searchTree[0..partitionCount].
      //System.out.println(heads.length);
      populateTree(pivotCount, 1, 0);
      return searchTree;
    }
    protected int populateTree
      ( int pivotCount, int r, int d) {
      if (pivotCount<=d) {
        return r;
      }
      int s = populateTree(pivotCount, r, d+d+1);
      searchTree[d] = heads[s];
      return populateTree(pivotCount, s+1, d+d+2);
    }
    public SortingFrame getSubFrame() {
      if (subframe==null) {
        subframe = new FancierFrame();
      }
      return subframe;
    }    
  }
  public SortingFrame newFrame()
  {
    return new FancierFrame();
  }
  @SuppressWarnings("unchecked")
  protected void distribute
    ( Comparator<? super T> comparator
    , StableNode scan, StableNode afterLast
    , int partitionCount, SortingFrame frame) {
    Object[] heads  = frame.heads;
    int[]    counts 
      = frame.getCounts(partitionCount); 
    Object[] tails  
      = frame.getTails(partitionCount);
    int      pivotCount = partitionCount-1;
    Object[] searchTree 
      = ((FancierFrame)frame).getTree(pivotCount);
    do {
      int search = 0;
      do  {
        StableNode node 
          = (StableNode) searchTree[search];
        int diff = comparator.compare
                   ( scan.value, node.value );
        if (diff==0) {
          diff = scan.rank - node.rank;
        }
        search += search + ((diff<0) ? 1 : 2);
      }
      while (search<pivotCount);
      search -= pivotCount;
      ++counts[search];
      ((StableNode)tails[search]).next = scan;
      tails[search]      = scan;
      scan = scan.getNext();
    } while (scan!=afterLast);
    //now, link together all those sublists
    for (int i=0; i+1<partitionCount; ++i) {
      ((StableNode)tails[i]).next 
      = (StableNode)(heads[i+1]);
    }
    ((StableNode)tails[partitionCount-1]).next 
      = afterLast;
  }
}
