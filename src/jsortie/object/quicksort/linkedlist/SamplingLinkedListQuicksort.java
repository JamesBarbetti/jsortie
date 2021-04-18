package jsortie.object.quicksort.linkedlist;

import java.util.Comparator;

public class SamplingLinkedListQuicksort<T>
  extends StableLinkedListQuicksort<T> {
  protected int threshold = 200;
  protected int maxSample 
    = Integer.MAX_VALUE;
  public SamplingLinkedListQuicksort() {
  }
  public SamplingLinkedListQuicksort 
    ( int maxSampleCount ) {
    maxSample = maxSampleCount;
  }
  public String toString() {
    return this.getClass().getSimpleName()
      + "(" + maxSample + ")";
  }
  protected class SortingFrame {
    Object[] heads;
    Object[] tails;
    int[]        counts;
    SortingFrame subframe;    
    public Object[] getHeads(int headCount) {
      if (heads==null || heads.length < headCount) {
        heads = new Object[headCount];
      }
      return heads;
    }
    public SortingFrame getSubFrame() {
      if (subframe==null) {
        subframe = new SortingFrame();
      }
      return subframe;
    }
    public int[] getCounts(int partitionCount) {
      if (counts==null) {
        counts = new int[partitionCount];
      } else if (counts.length<partitionCount) {
        counts = new int[partitionCount];
      } else {
        for (int i=0; i<partitionCount; ++i) {
          counts[i]=0;
        }
      }
      return counts;
    }
    public Object[] getTails(int partitionCount) {
      if ( tails==null || 
           tails.length<partitionCount) {
        tails = new Object[partitionCount];
      }
      for (int i=0; i<partitionCount; ++i) {
        tails[i] = heads[i];
      }
      return tails;
    }
  }
  public SortingFrame newFrame()
  {
    return new SortingFrame();
  }
  @SuppressWarnings("unchecked")
  protected void sortSubList 
    ( Comparator<? super T> comparator
    , SortingFrame frame
    , StableNode before, StableNode after
    , int count) {
    while (threshold<count) {
      int sampleCount = 1;
      while ( sampleCount < maxSample 
              && sampleCount * sampleCount < count) {
        sampleCount += sampleCount + 1; 
        //1,3,7,15, ...
      }
      Object[]   heads 
        = frame.getHeads(sampleCount+1);
      StableNode scan  
        = before.getNext();
      for (int i=sampleCount; 0<i; --i) {
        scan=scan.getNext();
      }
      sortSubList
        ( comparator, frame.getSubFrame()
        , before, scan, sampleCount);
      constructTree 
        ( before, scan, frame.heads
        , sampleCount+1);
      distribute 
        ( comparator, scan, after
        , sampleCount+1, frame);
      int b = sortSubFrames
              ( comparator, frame, sampleCount);
      before = (StableNode)heads[b];
      after  
        = ((StableNode)frame.tails[b]).getNext();
      count  = frame.counts[b];
    }
    super.sortSubList 
    ( comparator, before, after, count );
  }
  @SuppressWarnings("unchecked")
  protected int sortSubFrames
    ( Comparator<? super T> comparator
    , SortingFrame frame, int sampleCount) {
    int   biggestCount = 0;
    int   b            = -1; 
    //biggest partition's number
    //(this will be returned)
    Object[] heads    = frame.heads;
    Object[] tails    = frame.tails;
    int[]    counts   = frame.counts;
    SortingFrame subframe = frame.getSubFrame();
    //recurse for all bar the largest partition
    for (int i=0; i<=sampleCount; ++i) {
      if ( counts[i] <= biggestCount ) {
        if ( 1 < counts[i] ) {
          sortSubList 
            ( comparator, subframe
            , (StableNode)heads[i]
            , ((StableNode)tails[i]).getNext()
            , counts[i]);
        }           
      } else {
        if ( 1 < biggestCount ) {
          sortSubList 
            ( comparator, subframe
            , (StableNode)heads[b]
            , ((StableNode)tails[b]).getNext()
            , counts[b]);
        }
        b            = i;
        biggestCount = counts[i];
      }
    }
    return b;
  }
  protected int constructTree
    ( StableNode before, StableNode after
    , Object[] sample, int sampleCount) {
    int i=0;
    for ( StableNode scan=before
        ; scan!=after
        ; scan=scan.getNext(), ++i) {
      sample[i] = scan;
    }
    return i;
  }  
  @SuppressWarnings("unchecked")
  protected void distribute
    ( Comparator<? super T> comparator
    , StableNode scan, StableNode after
    , int partitionCount
    , SortingFrame frame) {
    Object[] heads  = frame.heads;
    int[]    counts 
      = frame.getCounts(partitionCount); 
    Object[] tails  
      = frame.getTails(partitionCount);
    do {
      int lo = 0;
      int hi = partitionCount-1;
      while (lo<hi) {
        int m    = hi - (hi-lo) / 2;
        StableNode head = (StableNode)heads[m];
        int diff = 
          comparator.compare 
          ( scan.value, head.value );
        if (diff==0) {
          diff = ( scan.rank < head.rank ) 
                 ? -1 
                 : 1;
        }
        if (diff<0) {
          hi = m-1;
        } else {
          lo = m;
        }
      }
      ++counts[lo];
      ((StableNode)tails[lo]).next = scan;
      tails[lo]      = scan;
      scan = scan.getNext();
    } while (scan!=after);
    //now, link together all those sublists
    for (int i=0; i+1<partitionCount; ++i) {
      ((StableNode)tails[i]).next 
        = (StableNode)(heads[i+1]);
    }
    ((StableNode)tails[partitionCount-1]).next 
      = after;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start+1<stop) {
      @SuppressWarnings("unchecked")
      StableNode head 
        = (StableNode)buildInputList
          ( vArray, start, stop );
      SortingFrame frame = newFrame();
      sortSubList 
        ( comparator, frame, head
        , null, stop-start);
      emitList ( head, vArray
               , start, stop);
    }
  }  
}
