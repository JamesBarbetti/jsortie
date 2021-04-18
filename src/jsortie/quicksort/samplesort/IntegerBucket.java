package jsortie.quicksort.samplesort;

public class IntegerBucket {
  IntegerBucketNode first = null;
  IntegerBucketNode last  = null;
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public IntegerBucket(int size) {
    if (size<8) {
      size=8;
    }
    first = last = new IntegerBucketNode(size);
  }
  public int receive(int[] source, int scan, int stop) { 
    //returns scan position after receive
    if (scan<stop) {
      scan = last.receive(source, scan, stop);
      while (scan<stop) {
        last = last.link 
             = new IntegerBucketNode
                   ( last.vDataArray.length );
        scan = last.receive(source, scan, stop);
      }
    }
    return scan;
  }
  public int emit(int[] dest, int write) {
    //returns write position after transmit
    IntegerBucketNode node = first;
    while (node != last) {
      write = node.transmit(dest, write);
      node  = node.link;
    }
    return write = node.transmit(dest, write);
  }
  public void add(int v) {
    if (last.countHere == last.vDataArray.length) {
      last = last.link 
           = new IntegerBucketNode
             ( last.vDataArray.length );
    }
    last.vDataArray[last.countHere] = v;
    ++last.countHere;
  }
}
