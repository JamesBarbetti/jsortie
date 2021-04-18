package jsortie.quicksort.multiway.partitioner.threepivot;

import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class OutsideInPartitioner3 
  implements FixedCountPivotPartitioner {
  //This is an alternative to KLMQPartitioner3 
  //that preferentially "works on the edges" 
  //(doing, we suppose, more comparisons, but 
  //hopefully they will be rather more predictable 
  //comparisons!)...
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    MultiPivotUtils.movePivotsAside
      ( vArray, pivotIndices
      , new int[] { start, start+1, stop-1 } );
    int vP = vArray[start];
    int vQ = vArray[start + 1];
    int vR = vArray[stop  - 1];				
    int a = start + 2; //where next item < vP is to go 
    int d = stop  - 2; //where next item > vR is to go
    while (vArray[a]<vP) {
      ++a; 
    }
    while (vR<vArray[d]) {
      --d;
    }
    int b = a-1; //the left-hand scanning index
    int c = d+1; //the right-hand scanning index
    int vAside;  //the "move-aside" item 
                 //used for swaps
    for (;;) {
      do {
        ++b;
        while (vArray[b] < vP) {
          vAside        = vArray[a];
          vArray[a] = vArray[b];
          vArray[b] = vAside;
          ++a;
          ++b;
        }
      } while (vArray[b] < vQ);
      do {
        --c;
        while (vR < vArray[c]) {
          vAside = vArray[c];
          vArray[c] = vArray[d];
          vArray[d] = vAside;
          --d;
          --c;
        }
      } while (vQ < vArray[c]);
      if (c<b) {
    	  break;
      }
      //Now, array[c] <= vQ <= array[b]
      vAside    = vArray[b];
      vArray[b] = vArray[c];
      vArray[c] = vAside;
      if (vArray[b] < vP) {
        vAside    = vArray[a];
        vArray[a] = vArray[b];
        vArray[b] = vAside;
        ++a;
      }
      if (vR < vArray[c]) {
        vAside    = vArray[c];
        vArray[c] = vArray[d];
        vArray[d] = vAside;
        --d;
      }
    }
    a-=2;
    vArray[start+1] = vArray[a];
    vArray[a]       = vArray[start];
    vArray[start]   = vArray[a+1];
    vArray[a+1]     = vArray[c];
    ++d;
    vArray[stop-1]  = vArray[d];
    vArray[a]       = vP;
    vArray[c]       = vQ;
    vArray[d]       = vR;
    return new int[] 
      { start, a, a+1, c
      , b, d, d+1, stop };
  }
  @Override
  public int getPivotCount() {
    return 3;
  }
}
