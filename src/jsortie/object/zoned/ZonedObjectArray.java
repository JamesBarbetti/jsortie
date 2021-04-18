package jsortie.object.zoned;

import jsortie.exception.SortingFailureException;

public class ZonedObjectArray<T> {
  protected T          [] vArray;
  protected int        start;
  protected int        stop;
  protected int        zoneSize;
  protected int        zoneCount;
  protected static int firstArrayZoneNumber = 1;
  protected static int nullZoneNumber = 0;
  protected int        lastArrayZoneNumber;
  protected int        firstWorkZoneNumber;
  protected int        firstFreeZone;
  protected int        spareZoneCount = 3;	
  protected int        nextZone[];
  protected T          vWork[];
  
  public ZonedObjectArray () {
  }  
  @SuppressWarnings("unchecked")
  public ZonedObjectArray ( T [] vArrayToSort
                          , int start, int stop) {
    int count                 = stop-start;
    this.vArray               = vArrayToSort;
    this.start                = start;
    this.stop                 = stop;
    this.lastArrayZoneNumber  = (int) Math.floor(Math.sqrt(count));
    this.zoneSize             = (count+lastArrayZoneNumber-1)/lastArrayZoneNumber;
    this.zoneCount            = lastArrayZoneNumber + spareZoneCount;
    this.nextZone             = new int[zoneCount+spareZoneCount+1];
    this.lastArrayZoneNumber  = zoneCount-spareZoneCount;
    this.firstWorkZoneNumber  = lastArrayZoneNumber + 1;
    this.firstFreeZone        = lastArrayZoneNumber + 1;
    for (int z=0; z<spareZoneCount; ++z) {
      nextZone[firstFreeZone+z] = firstFreeZone+z+1;
    }
    this.vWork = (T[]) new Object[ spareZoneCount * zoneSize ];
  }
  public ObjectArrayZone<T> newZone() {
    return new ObjectArrayZone<T>(this, allocateZone());
  }
  public ObjectArrayZone<T> newZone(int zoneNumber) {
    return new ObjectArrayZone<T>(this, zoneNumber);
  }
  public int getZoneCount() {
    return zoneCount;
  }
  protected int getZoneStart(int z) { 
    if (z<=lastArrayZoneNumber) {
      return start + (z-1)*zoneSize; 
    } else {
      return (z-firstWorkZoneNumber)*zoneSize; 
    }
  }
  protected int getZoneStop (int z) {
    if (z<lastArrayZoneNumber) {
      return start + z*zoneSize; 
    } else if (z==lastArrayZoneNumber) {
      return stop;
    } else {
      return (z-lastArrayZoneNumber)*zoneSize;
    }
  }  
  protected T[] getZoneArray(int z) {
    return (z<=lastArrayZoneNumber) ? vArray : vWork; 
  }
  public int getNextZone(int zoneNumber) {
    return nextZone[zoneNumber];
  }
  public void setNextZone(int first, int second) {
    nextZone[first] = second;
  }
  public int allocateZone() {
	  int zoneNumber = firstFreeZone;
    firstFreeZone  = nextZone[firstFreeZone];
    return zoneNumber;
  }
  public void releaseZone(int zoneNumber) {
    nextZone[zoneNumber] = firstFreeZone;
    firstFreeZone = zoneNumber;
  }
  public boolean allocateSpecificZone
    ( int zoneNumber ) {
    if (zoneNumber==firstFreeZone)  {
      firstFreeZone = nextZone[firstFreeZone];
      return true;
    }
    int prev = firstFreeZone;
    for ( int scan=nextZone[prev]; scan!=nullZoneNumber
        ; prev = scan, scan = nextZone[scan]) {
      if ( scan==zoneNumber ) {
        nextZone[prev] = nextZone[scan];
        nextZone[scan] = nullZoneNumber;
        return true;
      }
    }
    return false;
  }
  public void copyZone(int fromZone, int toZone) {
    T[] sourceArray = getZoneArray(fromZone);
    T[] destArray   = getZoneArray(toZone);
    int r           = getZoneStart(fromZone);
    int sourceStop  = getZoneStop(fromZone);
    int w           = getZoneStart(toZone);
    for (; r < sourceStop; ++r, ++w) {
      destArray[w] = sourceArray[r];
    }
  }
  public void copyToPartialZone(int fromZone, int toZone) {
    T[] sourceArray = getZoneArray(fromZone);
    T[] destArray   = getZoneArray(toZone);
    int r           = getZoneStart(fromZone);
    int sourceStop  = getZoneStop(fromZone);
    int w           = getZoneStart(toZone);
    int destStop    = getZoneStop(toZone);
    if (destStop-w < sourceStop-r) {
      sourceStop = r + (destStop-w);
    }
    for (; r < sourceStop; ++r, ++w) {
      destArray[w] = sourceArray[r];
    }
  }
  public int getFirstArrayZoneNumber() {
    return 1;
  }
  public int getLastArrayZoneNumber() {
    return lastArrayZoneNumber;
  }
  public int getFirstWorkZoneNumber() {
	return firstWorkZoneNumber;
  }
  public void shuffleZones(int listHead) {
    for ( int w=firstArrayZoneNumber;
          w < lastArrayZoneNumber; 
          ++w ) {
      if (listHead == w) {
        listHead = nextZone[listHead]; //Easy!
      } else {
        if (!allocateSpecificZone(w)) {
          //w wasn't free, and is in the list.
          //find its predecessor, so we can 
          //replace it.
          int prev = listHead;
          for ( int scan = nextZone[prev]
              ; scan != w
              ; prev = scan, scan=nextZone[scan] ) {
            if ( scan == nullZoneNumber ) {
              throw new SortingFailureException
                ( "Logic error; could not find zone " + w
                + " in the sorted list of sorted zones");
            }
          }
          //replace it, with a zone that was free.
          int f = allocateZone();
          nextZone[prev] = f;
          nextZone[f]    = nextZone[w];
          copyZone(w, f);
          //So now we can write to it
        } 
        copyZone(listHead, w);
        int r = listHead;
        listHead = nextZone[listHead];
        releaseZone(r);
      }
    }
    if (listHead!=nullZoneNumber) {
      copyToPartialZone(listHead, lastArrayZoneNumber);
      releaseZone(listHead);
    }
  }
  public boolean isNullZoneNumber(int zoneNumber) {
    return zoneNumber == nullZoneNumber;
  }
}
