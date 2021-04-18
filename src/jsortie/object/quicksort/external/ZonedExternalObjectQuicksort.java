package jsortie.object.quicksort.external;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.selector.MiddleElementObjectSelector;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;
import jsortie.object.zoned.ObjectArrayZone;
import jsortie.object.zoned.ZonedObjectArray;

public class ZonedExternalObjectQuicksort<T> 
  extends ZonedObjectArray<T>
  implements ObjectRangeSorter<T> {

  //This is a version of ExternalQuicksort with 
  //a (much) lower space overhead. The central idea 
  //is the same as ZonedMergesort; the array is 
  //divided into zones, and the sort rearranges 
  //zones as it proceeds (and never needs more 
  //than two additional zones).
  protected ExternalObjectQuicksort<T>   simplerSort;
  protected SinglePivotObjectSelector<T> simpleSortSelector;
  protected ObjectRangeSorter<T>         janitor;
  protected Comparator<? super T>        comparator;
	
  public ZonedExternalObjectQuicksort 
    ( Comparator<? super T> comparatorToUse
    , ObjectRangeSorter<T> sorterToUse) {		
    super();
    this.comparator = comparatorToUse;
    this.simpleSortSelector 
      = new MiddleElementObjectSelector<T>();
    this.simplerSort 
      = new ExternalObjectQuicksort<T>
            ( simpleSortSelector, janitor, 32 );
    this.janitor = sorterToUse;
  }
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ZonedExternalObjectQuicksort 
    ( Comparator comparatorToUse
    , ObjectRangeSorter sorterToUse
    , T [] vArray
    , int start, int stop) {
    super(vArray, start, stop);
    this.comparator = comparatorToUse;
    this.janitor    = sorterToUse;
  }	
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    ZonedExternalObjectQuicksort<T> z 
      = new ZonedExternalObjectQuicksort<T>
            ( comparator, janitor, vArray, start, stop );
    z.sort(stop-start);
  }
  protected void linkArrayZones() {
    int firstZone = getFirstArrayZoneNumber();
    int lastZone  = getLastArrayZoneNumber();
    for (int i=firstZone; i<lastZone; ++i) {
      setNextZone(i, i+1);
    }
    setNextZone(lastZone, 0);
  }
  protected void sort(int count) {
    linkArrayZones();
    ObjectArrayZone<T> left  
      = newZone(this.getFirstArrayZoneNumber());
    ObjectArrayZone<T> right 
      = newZone(this.getLastArrayZoneNumber());
    sort(left, right, count, false);
  }
  protected void sort ( ObjectArrayZone<T> outerStart
                      , ObjectArrayZone<T> outerStop
                      , int count, boolean flipped) {
    ObjectArrayZone<T> start 
      = new ObjectArrayZone<T>(outerStart);
    ObjectArrayZone<T> stop  
      = new ObjectArrayZone<T>(outerStop);
    while ( start.getZoneNumber() 
            != stop.getZoneNumber() ) {
      ObjectArrayZone<T> scan 
        = new ObjectArrayZone<T>(start);
      if (!flipped) {
        //zones linked from left to right
        ObjectArrayZone<T> pivot 
          = new ObjectArrayZone<T>();
        choosePivotFromLeft(scan, count, pivot);	  
        T vPivot = pivot.getCurrent();
        int leftCount            = 0;
        ObjectArrayZone<T> left
          = new ObjectArrayZone<T>(scan);
        ObjectArrayZone<T> right = newZone();
        int firstBackwardZone = right.getZoneNumber();
        int lastRealZone = stop.getZoneNumber();
        int stopOffset = stop.getPosition()-stop.getStart();
        right.setPosition(right.getStart() + (stopOffset));
        while ( !scan.equals(vPivot) ) {
          T v = scan.getCurrent();
          if ( comparator.compare(v,vPivot) < 0 ) {
            left.setCurrent(v);
            left.incrementPosition();
            if (left.getPosition() == left.getStop()) {
              left.moveToNextZone();
            }
            ++leftCount;
          } else {
            if (right.getStart() == right.getPosition()) {
              int oldZone = right.getZoneNumber();
              right.setZoneNumber
                ( this.allocateZone(), true /*go stop*/ );
              right.setPreviousZone(oldZone);
            } 
            right.decrementPosition();
            right.setCurrent(v);
          }
          moveOnFromLeft(left, scan);		  
        }
        moveOnFromLeft(left, scan);
        while ( !scan.equals(stop) ) {
          moveOnFromLeft(left, scan);
        }
        left.setCurrent(vPivot);
        pivot.copy(left);
        //
        //Overwrite the part of the last input block,
        //immediately before stop, with what was written
        //(in reverse order) to the first backward zone
        //(it'll still be in reverse order).
        //
        int skip = (left.getZoneNumber()==lastRealZone) 
          ? (left.getPosition() - left.getStart() + 1) : 0;
        int nextRealZoneOnRight 
          = this.getNextZone(lastRealZone);
        this.setNextZone
          ( lastRealZone
          , this.getNextZone(firstBackwardZone));
        ObjectRangeSortHelper.copyRange 
          ( this.getZoneArray(firstBackwardZone) 
          , this.getZoneStart(firstBackwardZone)+skip
          , stop.getPosition()
          , this.getZoneArray(lastRealZone)
          , this.getZoneStart(lastRealZone)+skip );
        if ( 0<skip ) {
          //
          //Overwrite the far left of the last input block, with what
          //was written to the last forward zone (pointed to by left)
          //
          if ( left.getZoneNumber() != lastRealZone ) {
            ObjectRangeSortHelper.copyRange( left.getArray()
              , left.getStart(), left.getPosition()
              , this.getZoneArray(lastRealZone)
              , this.getZoneStart(lastRealZone));
            this.setNextZone
              ( left.getPreviousZoneNumber(), lastRealZone );
            pivot = this.newZone( lastRealZone );
            pivot.setPosition( pivot.getStart() + skip );
            left.copy(pivot);
          }
        }
        this.setNextZone ( right.getZoneNumber()
                         , nextRealZoneOnRight);
        //Recursively sort the smaller partition, 
        //and continue with the larger
        int rightCount = count - leftCount - 1;
        if (leftCount < rightCount) {
          sort(start, pivot, leftCount, false);
          //skip over pivot
          start.copy(pivot);
          start.incrementPosition();
          if (start.getPosition() == left.getStop()) {
            start.moveToNextZone();
          }
          flipped = true;
        } else {
          scan.copy(pivot);
          scan.incrementPosition();
          if (scan.getPosition() == scan.getStop()) {
            scan.moveToNextZone();
          }
          sort(scan, stop, rightCount, true);
          stop.copy(pivot);
	      flipped = false; //redundant
	    }
	  } else {
		//Todo: mirror image of all that  
	  }
	}
    ObjectArrayZone<T> scratch = newZone();
    simplerSort.sortRangeUsing 
    ( comparator, start.getArray(), start.getPosition(),   stop.getPosition()
    , scratch.getArray(), scratch.getPosition(), true);	
  }
  protected void sortWithinZone ( ObjectArrayZone<T> start
                                , ObjectArrayZone<T> stop) {
    //assumes: start and stop are in the same zone
  }
  protected void choosePivotFromLeft ( ObjectArrayZone<T> scan
                                   , int count
                                   , ObjectArrayZone<T> pivot) {
    pivot.copy(scan); //for now
  }
  //todo: mirror it.
  private void moveOnFromLeft( ObjectArrayZone<T> left, ObjectArrayZone<T> scan) {
    if (scan.getPosition() == scan.getStop()) {
      int prevZone = scan.getPreviousZoneNumber();
      int deadZone = scan.getZoneNumber();
      int nextZone = this.getNextZone(deadZone);
      int linkZone = (deadZone != left.getZoneNumber()) ? nextZone : deadZone;
      scan.moveToNextZone();
      setNextZone(prevZone, linkZone);
      if (deadZone!=left.getZoneNumber()) {
        releaseZone(deadZone);
      }	      
    } else {
      scan.incrementPosition();
    }	  
  }
}
