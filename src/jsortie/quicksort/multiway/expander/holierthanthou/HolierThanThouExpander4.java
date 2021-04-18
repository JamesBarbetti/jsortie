package jsortie.quicksort.multiway.expander.holierthanthou;

public class HolierThanThouExpander4 
  extends HolierThanThouExpanderBase {
  public HolierThanThouExpander4() {
    super(4);
  }
  //note: the superclass (HolierThanThouPartitioner2) 
  //      has an expandPartitions method that
  //      works regardless of pivotIndices.length. 
  @Override
  public int[] expandPartitionsToRight
    ( int[] vArray, int start, int[] pivotIndices
    , int startRight, int stop ) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int hole3 = pivotIndices[2];  
    int hole4 = pivotIndices[3]; //fourth hole

    int vP    = vArray[hole1];
    int vQ    = vArray[hole2];
    int vR    = vArray[hole3];  
    int vS    = vArray[hole4]; //fourth pivot
    
    for (int scan=startRight; scan<stop; ++scan) {
      int v = vArray[scan];
      if ( v <= vQ ) { //less than or equal
        vArray[scan]   = vArray[hole4+1]; //one extra move
        vArray[hole4]  = vArray[hole3+1]; //on these two lines
        vArray[hole3]  = vArray[hole2+1];
        if ( v < vP ) {
          vArray[hole2] = vArray[hole1+1];
          vArray[hole1]   = v;
          ++hole1;
        } else {
          vArray[hole2] = v;
        }
        ++hole2;
        ++hole3;
        ++hole4; //adjust location of fourth hole
      }
      else if ( v < vR ) {	    	
        vArray[scan]  = vArray[hole4+1]; //one extra move
        vArray[hole4] = vArray[hole3+1]; //on these two lines
        vArray[hole3] = v;
        ++hole3;
        ++hole4; //adjust location of fourth hole
      }
      else if (v <= vS)	{ //this entire block is new
        vArray[scan]  = vArray[hole4+1]; //add to end of...
        vArray[hole4] = v;              //fourth partition
        ++hole4;
      }
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    vArray[hole3] = vR;
    vArray[hole4] = vS;
    return new int[]
      { start, hole1, hole1+1, hole2, hole2+1
      , hole3, hole3+1, hole4, hole4+1, stop };
  }
  
  @Override
  public int[] expandPartitionsToLeft
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int hole3 = pivotIndices[2];
    int hole4 = pivotIndices[3];  //third hole
    int vO = vArray[hole1];
    int vP = vArray[hole2];
    int vQ = vArray[hole3];
    int vR = vArray[hole4];  //third pivot
    for (int scan=stopLeft-1; start<=scan; --scan) {
      int v = vArray[scan];
      if ( vP < v ) {
        vArray[scan]  = vArray[hole1-1];
        vArray[hole1] = vArray[hole2-1];
        if ( vQ <= v) {
          vArray[hole2] = vArray[hole3-1];
          if ( vR < v) {
            vArray[hole3] = vArray[hole4-1];
            vArray[hole4] = v;
            --hole4;
          } else {
            vArray[hole3] = v;
          }
          --hole3;
        } else {
          vArray[hole2] = v;
        }
        --hole2;
        --hole1;
      } else if ( vO <= v ) {
        vArray[scan]  = vArray[hole1-1];
        vArray[hole1] = v;
        --hole1;
      } //else v < vO, and v can stay just where it's at
    }
    vArray[hole1] = vO;
    vArray[hole2] = vP;
    vArray[hole3] = vQ;
    vArray[hole4] = vR;
    return new int[] 
      { start, hole2, hole2+1, hole3, hole3+1
      , hole4, hole4+1, stop };
  }
}
