package codegen;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class Bleh<T> extends TreeSet<T> {
  @Override
  public boolean removeAll(Collection<?> c) {
    boolean modified = false;
    if (size() > c.size()) {
      for (Iterator<?> i = c.iterator(); i.hasNext(); ) {
        modified |= remove(i.next());
      }
    } else {
      for (Iterator<?> i = iterator(); i.hasNext(); ) {
        if (c.contains(i.next())) {
          i.remove();
          modified = true;
        }
      }
    }
    return modified;
  }
}
