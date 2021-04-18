package jsortie.object.indexed;

public class IndexRemedianSelector 
  implements IndexPivotSelector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] selectPivotsForIndexRange
    ( IndexComparator comparator, int start, int stop ) {
    int   count  = stop-start;
    int   depth  = (int) Math.floor(Math.log(count) 
                   / Math.log(9.0));
    int   split  = selectPivotFromRange
                   ( comparator, start, stop, depth );
    return new int[] { split };
  }
  private int selectPivotFromRange
    ( IndexComparator comparator
    , int start, int stop, int depth ) {
    int a, b, c;
    if (0==depth) {
      int step = (stop-start)/4;
      if (step<1) {
        return start + (stop-start)/2;
      }
      a = start+step;
      b = start+(stop-start)/2;
      c = stop-step;
    } else {
      int third = (stop-start)/3;
      --depth;
      a = selectPivotFromRange ( comparator,  start
                               , start+third, depth);
      b = selectPivotFromRange ( comparator,  start+third
                               , stop-third,  depth);
      c = selectPivotFromRange ( comparator,  stop-third
                               ,  stop,       depth);
    }
    if (comparator.compareIndices(a,b) <= 0) {
      if (comparator.compareIndices(b, c) <= 0) { 
        return b /*v1<=v2<=v3*/ ;
      } else if (comparator.compareIndices(a, c) <=0) {
        return c /*v1<v3<v2*/ ;
      } else 
        return a /*v3<v1<v2*/;
    } else /*v2<v1*/ if (comparator.compareIndices(b, c) < 0) {
      return b /* v3<v2<v1 */; 
    } else /*v2<v3, v2<v1*/ if (comparator.compareIndices(a,c) <= 0 ) {
      return a /* v2<v1<v3 */; 
    } else {
      return c /* v2<v3<v1 */;
    }
  }
}
