package jsortie.quicksort.multiway.partitioner.morepivots;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class SevenPivotPartitioner 
  implements MultiPivotPartitioner {
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices )  {
    MultiPivotUtils.movePivotsAside
      ( vArray, pivotIndices
      , new int[] { start, start+1, start+2, start+3
                  , start+4, start+5, start+6} );
    int v1 = vArray[start]; 
    int v2 = vArray[start+1];
    int v3 = vArray[start+2];
    int v4 = vArray[start+3];
    int v5 = vArray[start+4];
    int v6 = vArray[start+5];
    int v7 = vArray[start+6];
    
    //Partition Boundaries
    int b[] 
      = new int[] 
          { start+1, start+2, start+3, start+4
          , start+5, start+6, start+7 };
    for (int scan=start+7; scan<stop; ++scan)
    {
      int v = vArray[scan];

      int p;
      if ( v < v4 )
        if ( v < v2 )
          if ( v < v1 ) p = 0;
          else p = 1;
        else if ( v < v3 ) p = 2;
        else p = 3;
      else if ( v < v6 )
        if ( v< v5) p = 4;
        else p = 5;
      else if ( v < v7 ) p = 6;
      else p = 7;
      
      if ( p<7)
      {
        vArray[scan]=vArray[b[6]];
        for (int x=5; p<=x; --x)
          vArray[b[x+1]++]=vArray[b[x]];
        vArray[b[p]++]=v;
      }	
    }
    return new int[] 
      { start, b[0], b[0], b[1], b[1], b[2]
      , b[2], b[3], b[3], b[4], b[4], b[5]
      , b[5], b[6], b[6], stop };
  }
}
