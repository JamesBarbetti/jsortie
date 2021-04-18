package jsortie.quicksort.tracer;

import javax.management.RuntimeErrorException;

public class Traced<T> {	
  protected T inner;
  public Traced(T innerObject) {
	inner = innerObject;
  }
  public String toString() {
    return this.getClass().getSimpleName() + "(" + inner.toString() + ")";  
  }
  
  protected String prefix(String methodName) {
	return inner.toString() + "." + methodName + ": "; 
  }
  
  protected void traceEntry(String s) {
	System.out.println( s);
  }
  
  protected void traceExit(String s) {
    System.out.println(s);
  }

  protected <V> void traceExit(String s, boolean threw, V v) {
    if (threw) {
      s+= " failed";
    } else {
      s+= " returned " + v;
    }
    traceExit(s);
  }
  
  protected <V> void traceExit(String s, boolean threw, V[] partitions) {
    if (threw) {
      s+= " failed";
    } else if (partitions == null) {
      s+=" returned null";
    } else {
      s+= " returned " + arrayToString(partitions);
    }
    traceExit(s);
  }
  
  protected void traceException(String string, RuntimeErrorException e) {
    System.out.println(string + ": threw exception " + e.getMessage());
    throw e;
  }
  
  protected <V> String arrayToString(V[] traceMe) {	
    String s = "[";
    for (int i=0; i<traceMe.length; ++i) {
      s += (0<i) ? "," : "";
      s += traceMe[i];
    }
    s+=" ]";
    return s;
  }
}
