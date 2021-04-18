package jsortie.earlyexitdetector;

public class OrsonPetersEarlyExitDetector 
  implements RangeSortEarlyExitDetector {
  int moveLimit = 8;
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  @Override
  public boolean exitEarlyIfSorted
    ( int[] vArray, int start, int stop ) {
    int movesRemaining = moveLimit;
    int lhs = start;
    for (int rhs=start+1; rhs<stop; ++rhs) {
      int v = vArray[rhs];
      if ( v < vArray[lhs] ) {
        if ( movesRemaining <= 0) {
          return false;
        }
        //
        //note: You *could* limit the movement 
        //      of the current item, v, being sifted 
        //      here, using 
        // 
        //      max ( rhs - movesRemaining - 1, start ) 
        //
        //      in place of start, but 
        //      I haven't bench-marked that.
        //
        do {
          vArray[lhs+1] = vArray[lhs];
          --lhs;
        } while (start<=lhs && v < vArray[lhs]);
        vArray[lhs+1]   = v;
        movesRemaining -= (rhs - lhs - 1);
      } 
      lhs = rhs;
    }
    return true;
  }
}
