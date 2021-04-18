package jsortie.quicksort.indexselector.indexset;

public class HashIndexSet
  implements IndexSet {
  int modulo;
  int[] heads;
  int[] next;
  int[] value;
  int count;
  public HashIndexSet
    ( int minimum, int maximumPlusOne, int probableCount) {
    if ( probableCount < 2 ) {
      probableCount = 2; // so modulo arithmetic will work
    }
    modulo = probableCount;
    heads  = new int[modulo];            // Assumes: all zeroed
    next   = new int[probableCount + 1]; // Assumes: all zeroed
    value  = new int[probableCount + 1]; // Assumes: all zeroed
    count  = 0;
  }
  @Override
  public boolean contains(int i) {
    int chain = i % modulo;
    for ( int scan = heads[chain]; scan != 0
        ; scan = next[scan]) {
      if (value[scan] == i) {
        return true;
      }
    }
    return false;
  }
  @Override
  public boolean merge(int i) {
    int chain = i % modulo;
    for ( int scan = heads[chain]; scan != 0
        ; scan = next[scan]) {
      if (value[scan] == i) {
        return false;
      }
    }
    if (value.length == count + 1) {
      grow(); // ...which changes the modulo, so...
      chain = i % modulo;
    }
    ++count;
    value[count] = i;
    next[count] = heads[chain];
    heads[chain] = count;
    return true;
  }
  public void grow() {
    int[] oldValue = value;
    modulo = modulo * 3 / 2; // Add 50% to modulo
    heads = new int[modulo]; // Assumes: all initialized to zero
    int capacity = count * 3 / 2; // Add 50% to capacity
    next  = new int[capacity + 1]; // Assumes: all zero
    value = new int[capacity + 1]; // Assumes: all zero
    for (int i = 1; i <= count; ++i) {
      value[i] = oldValue[i];
      int chain = oldValue[i] % modulo;
      next[i] = heads[chain];
      heads[chain] = i;
    }
    // Assumes: garbage collection cleans up oldValue array
  }
  @Override
  public int emit(int[] dest, int w) {
    for (int i = 1; i <= count; ++i, ++w) {
      dest[w] = value[i];
    }
    return w;
  }
  @Override
  public int emitInRangeAndNotInSet
    ( int start, int stop, int[] dest, int w ) {
    for (int i = start; i < stop; ++i) {
      if (!contains(i)) {
        dest[w] = i;
        ++w;
      }
    }
    return w;
  }
  @Override
  public boolean emitsInSortedOrder() {
    return false;
  }
  public void selectIndices
    ( int rangeStart, int rangeStop, int sampleSize ) {
    int rangeCount = rangeStop - rangeStart;
    while (0 < sampleSize) {
      double j = Math.random() * rangeCount;
      int k = rangeStart + (int) Math.floor(j);
      sampleSize -= merge(k) ? 1 : 0;
    }
  }
  public <T> int emitSample
    ( T[] vArray, T[] vSample, int w ) {
    for (int i = 1; i <= count; ++i, ++w) {
      vSample[w] = vArray[value[i]];
    }
    return w;
  }
}
