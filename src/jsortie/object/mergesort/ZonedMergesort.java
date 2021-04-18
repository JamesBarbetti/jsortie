package jsortie.object.mergesort;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.zoned.ObjectArrayZone;
import jsortie.object.zoned.ZonedObjectArray;

public class ZonedMergesort<T>
  extends ZonedObjectArray<T> 
  implements ObjectRangeSorter<T> {
  protected ObjectRangeSorter<T>  janitor;
  protected Comparator<? super T> comparator;
  public ZonedMergesort 
    ( ObjectRangeSorter<T> janitor ) {
    this.janitor    = janitor;
  }
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + janitor.toString() + ")";
  }
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ZonedMergesort 
    ( Comparator<? super T> comparator
    , ObjectRangeSorter janitor 
    , T[] vArray
    , int start, int stop) {
    super(vArray, start, stop);
    this.comparator = comparator;
    this.janitor    = janitor;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    ZonedMergesort<T> z 
      = new ZonedMergesort<T>
            ( comparator, janitor, vArray, start, stop);
    z.sort();
  }
  private void sort() {
    int first = mergesortZones
                ( 1, getFirstWorkZoneNumber());
    shuffleZones(first);
  }
  private int mergesortZones
    ( int startZone, int stopZone) {
    int result = 0;
    if ( stopZone <= startZone + 1 ) {
      if (startZone<stopZone) {
        mergesortOneZone(startZone);
        result = startZone;
      }
    } else {
      int middle    = startZone + (stopZone-startZone)/2;
      int leftList  = mergesortZones(startZone, middle );
      int rightList = mergesortZones(middle, stopZone  );
      result = mergeZoneLists(leftList, rightList);
    }
    return result;
  }
  protected void mergesortOneZone
    ( int startZone) {
    janitor.sortRange 
      ( comparator,              getZoneArray(startZone)
     , getZoneStart(startZone), getZoneStop(startZone));
  }
  private int mergeZoneLists
    ( int leftList, int rightList ) {
    ObjectArrayZone<T> dest  = newZone();
    ObjectArrayZone<T> left  = newZone(leftList);
    ObjectArrayZone<T> right = newZone(rightList);
    int  firstWriteZone = dest.getZoneNumber();
    for (;;) {
      mergeUntilBoundary(left, right, dest);
      if (dest.atEndOfZone()) {
          dest.writeNext();
      }
      if (left.atEndOfZone()) {
        if (!left.readNext()) {
          break;
        }
      }
      if (right.atEndOfZone()) {
        if (!right.readNext()) {
          break;
        }
      }
    }
    if (left.getZoneNumber()==nullZoneNumber) {
      //left list of zones ran out first
      dest.copyRemainderOfZone(right);
      if ( right.getZoneNumber() 
           == lastArrayZoneNumber) {
        right.copyContentFromZone(dest.getZoneNumber());
        if (  dest.getPreviousZoneNumber() 
              != nullZoneNumber) {
          setNextZone 
            ( dest.getPreviousZoneNumber()
            , lastArrayZoneNumber );
        }
        else {
          firstWriteZone 
            = lastArrayZoneNumber;
        }
        releaseZone 
          ( dest.getZoneNumber() );
      } else {
        setNextZone 
          ( dest.getZoneNumber()
          ,  getNextZone(right.getZoneNumber()));
        releaseZone 
          ( right.getZoneNumber() );
      }
    } else { 
      //right list of zones ran out first.
      if ( right.getPreviousZoneNumber() == 
           lastArrayZoneNumber) {
        mergeToRightTail( left, dest );
      } else {
        dest.copyRemainderOfZone(left);
        setNextZone 
          ( dest.getZoneNumber()
          , getNextZone(left.getZoneNumber()));
        releaseZone ( left.getZoneNumber() );
      }
    }
    return firstWriteZone;
  }
  protected void mergeUntilBoundary
    ( ObjectArrayZone<T> left, ObjectArrayZone<T> right
    , ObjectArrayZone<T> dest) {
    T[] leftArray     = left.getArray();
    T[] rightArray    = right.getArray();
    T[] destArray     = dest.getArray();
    int leftPosition  = left.getPosition();
    int rightPosition = right.getPosition();
    int destPosition  = dest.getPosition();
    int leftStop      = left.getStop();
    int rightStop     = right.getStop();
    int destStop      = dest.getStop();
    for (;;) {
      if ( comparator.compare 
           ( leftArray[leftPosition]
           , rightArray[rightPosition])<=0) {
        destArray[destPosition] 
          = leftArray[leftPosition];
        ++leftPosition;
        ++destPosition;
        if ( leftPosition == leftStop 
             || destPosition == destStop ) {
          break;
        }
      } else {
        destArray[destPosition] 
          = rightArray[rightPosition];
        ++rightPosition;
        ++destPosition;
        if ( rightPosition == rightStop 
             || destPosition == destStop) {
          break;				
        }
      }
    }
    left.setPosition(leftPosition);
    right.setPosition(rightPosition);
    dest.setPosition(destPosition);
  }
  protected void mergeToRightTail
    ( ObjectArrayZone<T> left
    , ObjectArrayZone<T> dest) {
    T[] destArray     = dest.getArray();
    int destStart     = dest.getStart();
    int destPosition  = dest.getPosition();
    int destStop      = dest.getStop();
    do {
      T[] leftArray     = left.getArray();
      int leftPosition  = left.getPosition();
      int leftStop      = left.getStop();
      int leftCount     = leftStop - leftPosition;
      if ( left.inLastZoneOfList() && 
           destStop - destPosition < leftCount ) {
        releaseZone ( lastArrayZoneNumber );
      }
      for ( ; leftPosition<leftStop
          ; ++leftPosition) {
        destArray[destPosition] 
          = leftArray[leftPosition];
        ++destPosition;
        if (destPosition==destStop) {
          dest.writeNext();
          destArray     = dest.getArray();
          destStart     = dest.getStart();
          destPosition  = dest.getPosition();
          destStop      = dest.getStop();
        }
      }
    } while (left.readNext());
    int lastDestZone = dest.getZoneNumber(); 
    if ( lastDestZone  != lastArrayZoneNumber) {
      if ( destStart < destPosition ) {
        setNextZone 
          ( dest.getPreviousZoneNumber()
          , lastArrayZoneNumber );
        copyToPartialZone 
          ( lastDestZone
          , lastArrayZoneNumber );
      } else {
        setNextZone 
          ( dest.getPreviousZoneNumber()
          , nullZoneNumber );
      }
      releaseZone(lastDestZone);
    }
  }
}
