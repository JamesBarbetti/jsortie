package jsortie.quicksort.expander.external;

import jsortie.quicksort.expander.PartitionExpander;

public class ExternalExpander 
  implements PartitionExpander {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    int [] vTemp  = new int[stop-start+1];
    int vPivot    = vArray[hole];
    int wLocal    = start;
    int wOther    = 0;
    //move aside high values in left expansion area
    //shuffle low values to left, in left expansion area
    for (int i=start; i<stopLeft; ++i) {
      int v = vArray[i];
      vArray[wLocal] = v;
      vTemp [wOther] = v;
      int less =( v <= vPivot ) ? 1 : 0; 
      wLocal += less;
      wOther += 1 - less;
    }
    //copy low values in partitioned area to left
    for (int i=stopLeft;i<hole; ++i, ++wLocal) {
      vArray[wLocal] = vArray[i];
    }
    //copy high values in partitioned area to vTemp
    for (int i=hole+1; i<startRight; ++i, ++wOther) {
      vTemp[wOther] = vArray[i];
    }
    //move aside high values in right expansion area,
    //shuffle low values to left, from right expansion area
    for (int i=startRight; i<stop; ++i) {
      int v = vArray[i];
      vArray[wLocal] = v;
      vTemp [wOther] = v;
      int less =( v <= vPivot ) ? 1 : 0; 
      wLocal += less;
      wOther += 1 - less;
    }
    vArray[wLocal] = vPivot;
    for (int i=0;i<wOther;++i) {
      vArray[wLocal+1+i] = vTemp[i];
    }
    return wLocal;
  }
}
