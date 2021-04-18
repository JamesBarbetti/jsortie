package jsortie.object.librarysort;

import java.util.Comparator;

public class ObjectMiddenSort<T> 
  extends ObjectLibrarySort<T> {
  public ObjectMiddenSort () {
    super();
  }
  public ObjectMiddenSort
    ( double ratio ) {
    super(ratio);
  }
  @Override
  public ObjectBookshelf<T> newBookshelf
    ( Comparator<? super T> comparator
    , int count ) {
    return 
      new ObjectMidden<T>
          ( comparator, count, capacityRatio );
  }
}
