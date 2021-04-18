package jsortie.quicksort.protector;

import jsortie.exception.SortingFailureException;
import jsortie.quicksort.collector.SampleCollector;

public class CheckingSampleCollector 
  implements SampleCollector {
  protected SampleCollector inner;
  public CheckingSampleCollector
    (SampleCollector collectorToWrap) {
    if (collectorToWrap==null) {
      throw new NullPointerException
        ( this.getClass().getSimpleName()
        + ": collectorToWrap may not be null");
    }
    inner = collectorToWrap;
  }
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + inner.toString() + ")";
  }
  @Override
  public void moveSampleToPosition
    ( int[] vArray, int start, int stop
    , int sampleStart, int sampleStop) {
    if (vArray==null) {
      throw new NullPointerException
        ( toString() + ": moveSampleToPosition" 
        + " cannot be passed a null vArray.");
    } else if ( start < 0 ) {
      throw new SortingFailureException 
        ( toString() + ": moveSampleToPosition" 
        + " cannot be passed a start (" + start 
        + ") less than zero.");
    } else if ( stop < start ) {
      throw new SortingFailureException 
        ( toString() + ": moveSampleToPosition" 
        + " cannot be passed a stop (" + stop
        + ") less than or equal" 
        + " to start (" + start + ").");
    } else if ( vArray.length < stop ) {
      throw new SortingFailureException 
        ( toString() + ": moveSampleToPosition" 
        + " cannot be passed a stop (" + stop
        + ") that is more than"
        + " vArray.length (" + vArray.length + ").");
    } else if ( sampleStart < start) {
      throw new SortingFailureException 
        ( toString() + ": moveSampleToPosition" 
        + " must have start (" + start 
        + ") less than or equal to " 
        + " position (" + sampleStart + ")");
    } else if ( stop < sampleStop) {
      throw new SortingFailureException 
        ( toString() + ": moveSampleToPosition" 
        + " cannot write a sample to a subrange"
        + " [" + sampleStart + ".."  + (sampleStop-1) + "]"
        + " not contained within the range"
        + " [" + start + ".." + (stop-1) + "]");
    } else if ( sampleStop <= sampleStart ) {
      throw new SortingFailureException 
        ( toString() + ": moveSampleToPosition" 
        + " cannot be passed" 
        + " a sampleStop (" + sampleStop
        + ") less than or equal to" 
        + " sampleStart (" + sampleStart + ").");
    }
    inner.moveSampleToPosition
      ( vArray, start, stop
      , sampleStart, sampleStop);
  }
}
