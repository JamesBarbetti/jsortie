package jsortie.quicksort.expander.branchavoiding;

public class SkippyCentripetalExpander
  extends SkippyExpander {	
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }	
  @Override
  public int expandPartition 
    ( int[] vArray,int start, int stopLeft, int hole
    , int startRight, int stop) {
    int onLeft   = stopLeft-start;
    int onRight  = stop-startRight;
    int uniform;
    if (onLeft<=0 || onRight<=0) {
      uniform=0;
    } else {
      uniform  = (onLeft < onRight) ? onLeft : onRight;
      hole     = expandUniformly 
                 ( vArray, stopLeft - uniform, stopLeft
                 , hole, startRight, startRight+uniform );
      onLeft  -= uniform;
      onRight -= uniform;
    }
    if ( 0 < onLeft ) {
      hole = expandPartitionToLeft
             ( vArray, start, stopLeft-uniform, hole );
    } else if ( 0 < onRight ) {
      hole = expandPartitionToRight
             ( vArray, hole, startRight+uniform, stop );
    } 
    return hole;
  }
  private int expandUniformly
    ( int[] vArray, int start, int stopLeft, int hole
    , int startRight, int stop ) {
  int vPivot     = vArray[hole];
  int scanLeft   = stopLeft-1;
  int scanRight  = startRight;
    for ( ; scanRight<stop; --scanLeft, ++scanRight) {
      int vLeft          = vArray[scanLeft];
      vArray[hole]       = vLeft;
      hole              -= (vPivot <= vLeft ) ? 1 : 0;
      vArray[scanLeft]   = vArray[hole];      
      int vRight         = vArray[scanRight];
      vArray[hole]       = vRight;
      hole              += (vRight <= vPivot) ? 1 : 0;
      vArray[scanRight]  = vArray[hole];
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
