package jsortie.object;

import java.util.Comparator;

public class NullComparator<T> 
  implements Comparator<T> {
  @Override
  public int compare(T arg0, T arg1) {
    return 0;
  }
}
