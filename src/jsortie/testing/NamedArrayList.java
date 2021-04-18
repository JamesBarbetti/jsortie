package jsortie.testing;

import java.util.ArrayList;
import java.util.Iterator;

public class NamedArrayList<T> 
  implements Iterable<T>{
  ArrayList<T> inner = new ArrayList<T>();
  ArrayList<String> names = new ArrayList<String>();

  public void add(String name, T t) {
    names.add(name);
    inner.add(t);    
  }
  public String[] getNames() {
    String[] a = new String[names.size()];
    names.toArray(a);
    return a;
  }
  public T get(int index) {
    return inner.get(index);
  }
  public int size() {
    return inner.size();
  }
  public ArrayList<T> getArrayList() {
    return inner;
  }
  @Override
  public Iterator<T> iterator() {
    return inner.iterator();
  }
  public String getName(int index) {
    return names.get(index);
  }
  public void appendEveryName(String suffix) {
    @SuppressWarnings("unchecked")
    ArrayList<String> oldNames = (ArrayList<String>) names.clone();
    names.clear();
    for (String s: oldNames) {
      names.add(s + suffix);
    }
  }
  public void clear() {
   inner.clear();
   names.clear();
  }
  public ArrayList<String> getNameList() {
    ArrayList<String> copy = new ArrayList<String>();
    for ( String name: names ) {
      copy.add(name);
    }
    return copy;
  }
}
