package jsortie.object.zoned;

import jsortie.object.ObjectRangeSortHelper;

public class ObjectArrayZone<T> {
  protected T[]                 vArray;
  protected int                 zoneNumber;
  protected int                 start;
  protected int                 stop;
  protected int                 position;
  protected int                 previousZone;
  protected ZonedObjectArray<T> parent;
  public ObjectArrayZone(ZonedObjectArray<T> p, int z) {
    this.parent = p;
    this.zoneNumber = z;
    initialise();
  }
  public ObjectArrayZone(ZonedObjectArray<T> p) {
	this.parent     = p;
    this.zoneNumber = parent.allocateZone();    
  }
  public ObjectArrayZone(ObjectArrayZone<T> rhs) {
	copy(rhs);
  } 
  public ObjectArrayZone() {
  }
  public String toString() {
    String s = "Zone(" + zoneNumber + ")";
    if (zoneNumber!=0) {
      s+= (zoneNumber <= parent.getLastArrayZoneNumber()) 
          ? " Array " 
          : " Work ";
      s+= " at " + this.position + " in ";
      s+= "[" + this.start + ".." + this.stop + "]";
      s+= " = ";
      for (int i=start; i<stop; ++i) {
        T o = this.vArray[i];
        s += ((i==start)
             ? "[ " : ", ") + ((o==null) ? "null"
             : o.toString());
      }
      s+= " ]";
    }
    return s;
  }
  public String toChain() {
    String s = "ZoneChain( ";
    int zone = zoneNumber;
    for ( int halt = parent.getZoneCount()
        ; zone!=0 && halt!=0; --halt) {
      s+= ((halt==parent.getZoneCount())
          ? "" : ", " ) + zone;
      zone = parent.getNextZone(zone);
    }
    return s + ")";
  }
  protected void initialise() {
    this.vArray      = parent.getZoneArray(zoneNumber);
    this.start      = parent.getZoneStart(zoneNumber);
    this.stop       = parent.getZoneStop(zoneNumber);
    this.position   = this.start;
  }
  public boolean atStartOfZone() {
    return position == start;
  }
  public boolean atEndOfZone() {
    return position == stop;
  }
  public boolean readNext() {
    previousZone = zoneNumber;
    zoneNumber   = parent.getNextZone(previousZone);
    if (previousZone != parent.getLastArrayZoneNumber()) {
      parent.releaseZone(previousZone);
    }
    if (zoneNumber==0) {
      return false;
    } else {
      initialise();
      return true;
    }
  }
  public void writeNext() {
    previousZone = zoneNumber;
    zoneNumber   = parent.allocateZone();
    parent.setNextZone(zoneNumber,   0);
    parent.setNextZone(previousZone, zoneNumber);
    initialise();
  }
  public int copyFromMatchingZoneInList(int first) {
    int rank = 1;
    int prev = 0;
    int scan = first;
    while (scan!=0 && rank != zoneNumber) {
      ++rank;
      prev = scan;
      scan = parent.getNextZone(scan);
    }
    copyContentFromZone(scan);
    if (prev!=0) {
      parent.setNextZone(prev, zoneNumber);
    }
    parent.setNextZone(zoneNumber, parent.getNextZone(scan));
    zoneNumber = scan;
    initialise();
    return (prev==0) ? parent.getNextZone(first) : first;
  }
  public void copyContentFromZone(int first) {
    //System.out.println("Copying from zone " + first + " to " + zoneNumber);
    if (first!=zoneNumber) {
      ObjectArrayZone<T> source = parent.newZone(first);
      int copyStop = source.stop;
      if ( stop - start < source.stop - source.start) {
        copyStop = source.start + (stop-start);
      }
      ObjectRangeSortHelper.copyRange
        ( source.vArray, source.start, copyStop
        , vArray, start);
    }
  }
  public void copyRemainderOfZone(ObjectArrayZone<T> source) {
    ObjectRangeSortHelper.copyRange 
      ( source.vArray, source.position, source.stop
      , vArray, position);
  }
  public T[] getArray() {
    return vArray;
  }
  public int getPosition() {
    return position;
  }
  public void setPosition(int pos) {
    position = pos;
  }
  public int getStop() {
    return stop;
  }
  public int getZoneNumber() {
    return zoneNumber;
  }
  public int getPreviousZoneNumber() {
    return previousZone;
  }
  public int getStart() {
    return start;
  }
  public T getCurrent() {
    return vArray[position];
  }
  public void setCurrent(T v) {
    vArray[position] = v;
  }
  public void setPreviousZone(int oldZone) {
    this.previousZone = oldZone;
  }
  public boolean moveToNextZone() {
    int oldZone  = this.zoneNumber;
    int nextZone = parent.getNextZone(oldZone);
    if (nextZone==0) {
      return false;
    }
    this.setZoneNumber(nextZone, false);
    this.setPreviousZone(oldZone);
    return true;
  }
  public void setZoneNumber(int nextZone, boolean goToStop) {
    vArray = parent.getZoneArray(nextZone);
    zoneNumber = nextZone;
    start = parent.getZoneStart(nextZone);
    stop  = parent.getZoneStop(nextZone);
    position = goToStop ? stop : start;	
  }
  public void incrementPosition() {
    ++position;
  }
  public void decrementPosition() {
    --position;
  }
  public void copy(ObjectArrayZone<T> rhs) {
    this.vArray        = rhs.vArray;
    this.zoneNumber   = rhs.zoneNumber;
    this.start        = rhs.start;
    this.stop         = rhs.stop;
    this.position     = rhs.position;
    this.previousZone = rhs.previousZone;
    this.parent       = rhs.parent;
  }
  public boolean inLastZoneOfList() {
    return parent.isNullZoneNumber
           ( parent.getNextZone(zoneNumber) ); 
  }
}
