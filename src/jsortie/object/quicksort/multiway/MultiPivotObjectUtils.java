package jsortie.object.quicksort.multiway;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class MultiPivotObjectUtils<T> {
  protected ObjectRangeSorter<T> sorter;
  public MultiPivotObjectUtils() {
    sorter = new ObjectHeapSort<T>(); 
  }
  public void movePivotsAside
    ( T vArray[], int pivotIndices[]
    , int destinations[] ) {
    if ( pivotIndices.length < destinations.length) {
      throw new IllegalArgumentException
        ( "Cannot move " + destinations.length 
        + " pivots when only " + pivotIndices.length 
        + " were selected");
    }
    else if ( pivotIndices.length > destinations.length) {
      //Discard the excess indices; choose the indices 
      //to be kept by selecting indexes evenly spaced 
      //through the sample we already have
      int oldIndices[] = pivotIndices;
      pivotIndices = new int [ destinations.length ];
      double step = (double)(oldIndices.length + 1)
                  / (double)(pivotIndices.length + 1); 
      //step will always be at least 1.
      double read = step;
      for (int i=0; i<pivotIndices.length; ++i) {
        int r = (int)Math.floor(read - .5);
        pivotIndices[i] = oldIndices[ r ];
        read += step;
      }
    }
    for (int i=0; i<destinations.length; ++i) {
      int d = destinations[i];
      int s = pivotIndices[i];
      if (s!=d) {
        T v = vArray[d];
        vArray[d] = vArray[s];
        vArray[s] = v;
        for (int j=i+1; j<pivotIndices.length; ++j) {
          if (pivotIndices[j]==d) {
        	pivotIndices[j]=s;
          }
        }
      }
    }
  }
  public boolean isRangeSorted
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    for (++start; start<stop; ++start) {
      if ( comparator.compare
           ( vArray[start], vArray[start-1] ) < 0 ) {
        return false;
      }
    }
    return true;
  }
  public int[] sortEvenlySpacedIndices
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int count
    , int pivotCount) {
    @SuppressWarnings("unchecked")
    T[]    vLocal  = (T[]) new Object[pivotCount];
    int[]  indices = new int[pivotCount];
    double step    = (double)count / (double)(pivotCount+1);
    double r       = start + 0.5 * step;
    for (int w=0; w<pivotCount; ++w) {
      indices[w] = (int)Math.floor( r + .5);
      vLocal[w]  = vArray[indices[w]];
      r         += step;
    }
    sorter.sortRange(comparator, vLocal, 0, vLocal.length);
    r = start + 0.5 * step;
    for (int w=0; w<pivotCount; ++w) {
      vArray[ (int)Math.floor( r + .5) ] = vLocal[w];
    }
    return indices;
  }
  public void heapSortSlice
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop, int step) {
    if (stop<=start+step) {
      return;
    }
    int fudge= start - step; //different
    int i;
    int j;
    T   v;
    for ( int h=start+((stop-start+1)/2/step)*step
        ; h>=start; h-=step) {
      i = h;
      v = vArray[i];
      j = i + i - fudge;
      while (j<stop) {
        if (j<stop-step) { 
          j+= ( comparator.compare
                ( vArray[j], vArray[j+step] ) < 0 ) 
              ? step : 0;
        }
        if ( comparator.compare
             ( vArray[j], v ) <=0 ) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i + i - fudge;
      }
      vArray[i]=v;
    }
    for ( stop-=step; stop>=start; stop-=step ) {
      v=vArray[stop];
      vArray[stop]=vArray[start]; 
      i = start;
      j = start + step;
      while (j<stop) {
        if (j<stop-step) {
          j+= ( comparator.compare
                ( vArray[j], vArray[j+step]) < 0 ) 
              ? step : 0;
        }
        if ( comparator.compare
             ( vArray[j], v ) <= 0 ) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i + i - fudge;
      }
      vArray[i] = v;
    }
  }
  public void insertionSortSlice
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int step) {
    for ( int input=start+step
        ; input<stop; input+=step) {
      T v = vArray[input];
      int scan;
      for ( scan=input-step
          ; start<=scan; scan-=step) {
        if ( comparator.compare
             ( vArray[scan],v ) <= 0 ) {
          break;
        } else {
          vArray[scan+step] = vArray[scan];
        }
      }
      vArray[scan+step] = v;
    }
  }
  public void reverseRange
    ( T[] vArray, int start, int stop ) {
    T v;
    for (--stop;start<stop;++start,--stop) {
      v            = vArray[start];
      vArray[start] = vArray[stop];
      vArray[stop]  = v;
    }
  }
  public boolean isRangeReverseSorted
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    for (++start; start<stop; ++start) {
      if ( comparator.compare
           ( vArray[start-1],vArray[start]) < 0 ) {
        return false;
      }
    }
    return true;
  }
  public void collectPositionalSample
    ( T[] vArray, int start, int stop
    , int sampleStart, int sampleStop ) {
    int    count       = stop - start;
    int    sampleCount = sampleStop - sampleStart;
    double step        = (double)count
                       / (double)(sampleCount+1);
    double r           = start + 0.5 * step;
    for (int i=sampleStart; i<sampleStop; ++i, r+=step) {
      int j     = (int) Math.floor ( r + .5 );
      T   v     = vArray[i];
      vArray[i] = vArray[j];
      vArray[j] = v;
    }
  }
  public void collectRandomSample
    ( T[] vArray, int start, int stop
    , int sampleStart, int sampleStop) {
    int count       = stop - start;
    T   v;
    for (int i=sampleStart;i<sampleStop;++i) {
      double r  = Math.random()*count;
      int j     = start +  (int)Math.floor(r);
      v         = vArray[i];
      vArray[i] = vArray[j];
      vArray[j] = v;
    }
  }
  public boolean tryToMovePivotsAside
    ( T[] vArray, int[] pivotIndices
    , int[] whereTo ) {
    if (pivotIndices.length < whereTo.length) {
      return false;
    } else {
      movePivotsAside(vArray, pivotIndices, whereTo);
      return true;
    }
  }
  public int[] dummyPartitions
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotCount ) {
    ObjectInsertionSort.insertionSortRange
      ( comparator, vArray, start, stop); 
    int partyCount =pivotCount + pivotCount + 2;
    int[] partitions = new int [  partyCount ];
    int r = start;
    int w = 0;
    while (r<stop-1 && w<partyCount) {
      partitions[w] = r;
      ++w;
      ++r;
      partitions[w] = r;
      ++w;
    }
    while (w<partyCount-2) {
      partitions[w] = r;
      ++w;
      partitions[w] = r;
      ++w;
    }
    if (w==partyCount) w-=2;
    partitions[w] = r;
    ++w;
    partitions[w] = stop;
    return partitions;
  }
  public int[] fakePartitions
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int[] pivotIndices, int pivotCount) {
    SingletonObjectPartitioner<T> singleton
      = new SingletonObjectPartitioner<T>();
    int middlePivotIndex 
      = pivotIndices[pivotIndices.length / 2];
    int split 
      = singleton.partitionRange
        ( comparator, vArray, start, stop, middlePivotIndex);
    int[] partitions 
      = new int[] { start, split, split+1, stop };
    return MultiPivotUtils.ensurePartitionCount
           ( partitions, pivotCount * 2 + 2 );
  }
  public boolean areAllPivotsTheSame 
    ( Comparator<? super T> comparator
    , T[] vArray, int[] pivotIndices) {
    T vFirstPivot = vArray[pivotIndices[0]];
    for (int i=1; i<pivotIndices.length; ++i) {
      if ( comparator.compare 
           ( vArray[pivotIndices[i]] , vFirstPivot) != 0 ) {
        return false;
      }
    }
    return true;
  }
  public int[] partitionRangeWithOnePivot
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex, PartitionObjectExpander<T> pox) {
    //1. swap pivot, vPivot to start of range;
    //2. extend partition to right, 
    //   so that [start..pivotIndex2] <= vPivot
    //3. extend partition to left,  
    //   so that [start..pivotIndex-1] < vPivot,
    //   [pivotIndexIndex..pivotIndex2] ==, and 
    //   vPivot < [pivotIndex2+1..stop-1].
    T vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start]      = vPivot;
    int pivotIndex2    
      = pox.expandPartition
        ( comparator, vArray, start, start, start
        , start+1, stop);
    pivotIndex 
      = pox.expandPartition
        ( comparator, vArray, start, pivotIndex2
        , pivotIndex2, pivotIndex+1, pivotIndex+1 );
    return new int[] 
      { start, pivotIndex, pivotIndex2+1, stop };  
  }
}
