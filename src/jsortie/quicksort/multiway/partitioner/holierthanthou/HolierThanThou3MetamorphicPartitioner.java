package jsortie.quicksort.multiway.partitioner.holierthanthou;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class HolierThanThou3MetamorphicPartitioner 
  extends HolierThanThouPartitioner3 {
  EgalitarianPartitionerHelper helper 
    = new EgalitarianPartitionerHelper();
  PositionalSampleCollector collector 
    = new PositionalSampleCollector(); 
  private class Implementation {
    int[] vArray;
    int   start;
    int   stop;
    int   vP;
    int   vQ;
    int   vR;
    int   leftHole;
    int   middleHole;
    int   rightHole;
    public Implementation
      ( int[] vArray, int start, int stop
      , int[] pivotIndices) {
      this.vArray = vArray;
      this.start = start;
      this.stop  = stop;
      MultiPivotUtils.movePivotsAside
        ( vArray,  pivotIndices
        , new int[] { start, start+1, start+2 } );
      vP = vArray[start];
      vQ = vArray[start+1];
      vR = vArray[start+2];  //third pivot
		    
      leftHole   = start;
      middleHole = start+1;
      rightHole  = start+2;  //third hole
    }
    public int chooseScheme
      ( int localStart, int localStop ) {
      int best = 0;
      {
        int a = leftHole-localStart;
        int b = middleHole-leftHole-1;
        int c = rightHole-middleHole-1;
        int d = localStop-rightHole-1;
			    
        //There are five possible pivot-comparison ordering schemes 
        //# Scheme         expected comparisons per element       comment
        //0  vQ first:      1 + a/m   + b/m   + c/m   + d/m        usually best.
        //1 vP, then vQ:   1         + b/m   + 2*c/m + 2*d/m )
        //2 vP, then vR:   1         + 2*b/m + 2*c/m + d/m
        //3 vR, then vP:   1 + a/m   + 2*b/m + 2*c/m
        //4 vR, then vQ:   1 + 2*a/m + 2*b/m + c/m
			    
        int[] x = new int[] { a + b + c + d,             b + c + c + d + d
                            , b + b + c + c + d,         a + b + b + c + c
                            , a + a + b + b + c };
        for (int s=1; s<5; ++s) {
          if (x[s] < best ) best = s;
        }
      }
      return best;
    }
    public void partitionWithScheme
      ( int scheme, int localStart, int localStop ) {
      //System.out.println("scheme " + scheme);
      switch (scheme) {
        case 0: //vQ first
          partitionQPR(localStart, localStop, this);
          break;
        case 1: //vP, then vQ
          partitionPQR(localStart, localStop, this);
          break;
        case 2: //vP, then vR
          partitionPRQ(localStart, localStop, this);
          break;
        case 3: //vR, then vP
          partitionRPQ(localStart, localStop, this);
          break;
        case 4: //vR, then vQ
          partitionRQP(localStart, localStop, this);
          break;
      }
    }
      int[] getPartitions() {
      vArray[leftHole]   = vP;
      vArray[middleHole] = vQ;
      vArray[rightHole]  = vR;
      return new int[] 
        { start, leftHole, leftHole+1, middleHole
        , middleHole+1, rightHole, rightHole+1, stop };
    }
  }
  static protected void partitionQPR
    ( int localStart, int localStop, Implementation imp ) {
    //System.out.println("QPR " + localStart + " " + localStop );
    int [] vArray = imp.vArray;
    int vP = imp.vP;
    int vQ = imp.vQ;
    int vR = imp.vR;
    int leftHole   = imp.leftHole;
    int middleHole = imp.middleHole;
    int rightHole  = imp.rightHole;
				
    for (int scan=localStart; scan<localStop; ++scan) {
      int v = vArray[scan];
      if ( v < vQ ) {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole]  = vArray[middleHole+1];
        if ( v < vP ) {
          vArray[middleHole] = vArray[leftHole+1];
          vArray[leftHole]   = v;
          ++leftHole;
        } else {
          vArray[middleHole] = v;
        }
        ++middleHole;
        ++rightHole;
      } else if ( v < vR ) {
        vArray[scan]      = vArray[rightHole+1];
        vArray[rightHole] = v;
        ++rightHole;
      }
    }
    imp.leftHole   = leftHole;
    imp.middleHole = middleHole;
    imp.rightHole  = rightHole;
  }
  static protected void partitionPQR
    ( int localStart, int localStop, Implementation imp ) {
    //System.out.println("PQR");
	int [] vArray = imp.vArray;
    int vP = imp.vP;
    int vQ = imp.vQ;
    int vR = imp.vR;
    int leftHole = imp.leftHole;
    int middleHole = imp.middleHole;
    int rightHole = imp.rightHole;

    for (int scan=localStart; scan<localStop; ++scan) {
      int v = vArray[scan];
      if ( v < vP ) {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole]  = vArray[middleHole+1];
        vArray[middleHole] = vArray[leftHole+1];
        vArray[leftHole]   = v;
        ++leftHole;
        ++middleHole;
        ++rightHole;
      } else if ( v < vQ ) {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole]  = vArray[middleHole+1];
        vArray[middleHole] = v;
        ++middleHole;
        ++rightHole;
      } else if ( v < vR ) {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole] = v;
        ++rightHole;
      }
    }
    imp.leftHole   = leftHole;
    imp.middleHole = middleHole;
    imp.rightHole  = rightHole;
  }
  private static void partitionPRQ
    ( int localStart, int localStop, Implementation imp ) {
    //System.out.println("PRQ");
    int [] vArray = imp.vArray;
    int vP = imp.vP;
    int vQ = imp.vQ;
    int vR = imp.vR;
    int leftHole = imp.leftHole;
    int middleHole = imp.middleHole;
    int rightHole = imp.rightHole;

    for (int scan=localStart; scan<localStop; ++scan) {
      int v = vArray[scan];
      if ( v < vP ) {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole]  = vArray[middleHole+1];
        vArray[middleHole] = vArray[leftHole+1];
        vArray[leftHole]   = v;
        ++leftHole;
        ++middleHole;
        ++rightHole;
      } else if ( v < vR ) {
        if ( v < vQ ) {
          vArray[scan]       = vArray[rightHole+1];
          vArray[rightHole]  = vArray[middleHole+1];
          vArray[middleHole] = v;
          ++middleHole;
          ++rightHole;
        } else {
          vArray[scan]       = vArray[rightHole+1];
          vArray[rightHole] = v;
          ++rightHole;
        }
      }
    }
    imp.leftHole   = leftHole;
    imp.middleHole = middleHole;
    imp.rightHole  = rightHole;
  }
  private static void partitionRPQ
    ( int localStart, int localStop, Implementation imp ) {
    //System.out.println("RPQ");
    int [] vArray = imp.vArray;
    int vP = imp.vP;
    int vQ = imp.vQ;
    int vR = imp.vR;
    int leftHole = imp.leftHole;
    int middleHole = imp.middleHole;
    int rightHole = imp.rightHole;

    for (int scan=localStart; scan<localStop; ++scan) {
      int v = vArray[scan];
      if ( vR <= v ) {
        //All good
      } else if ( v < vP ) {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole]  = vArray[middleHole+1];
        vArray[middleHole] = vArray[leftHole+1];
        vArray[leftHole]   = v;
        ++leftHole;
        ++middleHole;
        ++rightHole;
      } else if ( v < vQ ) {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole]  = vArray[middleHole+1];
        vArray[middleHole] = v;
        ++middleHole;
        ++rightHole;
      } else  {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole] = v;
        ++rightHole;
      }
    }
    imp.leftHole   = leftHole;
    imp.middleHole = middleHole;
    imp.rightHole  = rightHole;
  }
  static private void partitionRQP
    ( int localStart, int localStop
    , Implementation imp ) {
    //System.out.println("RQP");
    int [] vArray = imp.vArray;
    int vP = imp.vP;
    int vQ = imp.vQ;
    int vR = imp.vR;
    int leftHole = imp.leftHole;
    int middleHole = imp.middleHole;
    int rightHole = imp.rightHole;

    for (int scan=localStart; scan<localStop; ++scan) {
      int v = vArray[scan];
      if ( vR <= v ) {
        //All good
      } else if ( v < vQ ) {
        if ( v < vP ) {
          vArray[scan]       = vArray[rightHole+1];
          vArray[rightHole]  = vArray[middleHole+1];
          vArray[middleHole] = vArray[leftHole+1];
          vArray[leftHole]   = v;
          ++leftHole;
          ++middleHole;
          ++rightHole;
        } else {
          vArray[scan]       = vArray[rightHole+1];
          vArray[rightHole]  = vArray[middleHole+1];
          vArray[middleHole] = v;
          ++middleHole;
          ++rightHole;
        }
      } else {
        vArray[scan]       = vArray[rightHole+1];
        vArray[rightHole] = v;
        ++rightHole;
      }
    }
    imp.leftHole   = leftHole;
    imp.middleHole = middleHole;
    imp.rightHole  = rightHole; 
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    if (stop-start<1000) {
      return super.multiPartitionRange
             ( vArray, start, stop, pivotIndices );
    }
    Implementation imp 
      = new Implementation
            ( vArray, start, stop, pivotIndices );
    int sample = 25;
    int pause = start + 3 + sample;
    collector.moveSampleToPosition
      ( vArray, start+3, stop, start+3, pause );
    partitionQPR(start + 3, pause, imp);
    int scheme = imp.chooseScheme(start+3, pause);
    imp.partitionWithScheme( scheme, pause, stop); 
    //imp.partitionWithScheme( 0, start+3, stop);
    return helper.fudgeBoundaries
           ( vArray, imp.getPartitions() );
  }
}
