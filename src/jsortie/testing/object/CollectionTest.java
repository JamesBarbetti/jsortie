package jsortie.testing.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import jsortie.exception.SortingFailureException;
import jsortie.object.collection.BFlatList;
import jsortie.object.collection.BufferRing;
import jsortie.object.treesort.binary.balanced.MonkeyPuzzleContainer;
import jsortie.testing.RandomInput;

public class CollectionTest {
  @Test
  public void testMonkeyPuzzleContainer() {
    Collection<MyInteger> c 
      = new MonkeyPuzzleContainer<MyInteger>
            ( MyInteger.countingComparator );
    testCollection(c);
  }
  @Test
  public void testBufferRing() {
    Collection<MyInteger> c 
      = new BufferRing<MyInteger>( );
    testCollection(c);
  }
  @Test
  public void testBFlatList() {
    Collection<MyInteger> c
      = new BFlatList<MyInteger>(6,6);
    testCollection(c);
  }
  
  private void testCollection
    (Collection<MyInteger> c) {
    checkCollection(c, 0, "");
    c.add( new MyInteger(1));
    checkCollection(c, 1, "1");
    c.add( new MyInteger(2));
    checkCollection(c, 2, "12");
    c.add( new MyInteger(3));
    checkCollection(c, 3, "123");
    c.add( new MyInteger(4));
    checkCollection(c, 4, "1234");
    boolean removed 
      = c.remove( new MyInteger(3));
    if (!removed) {
      throw new SortingFailureException
        ( "remove() of in-collection item 3" 
        + " returned false");
    }
    checkCollection(c, 3, "124");
    removed = c.remove( new MyInteger(6));
    if (removed) {
      throw new SortingFailureException
        ( "remove() of out-of-collection " 
        + " item 6 returned true");      
    }
    checkCollection(c, 3, "124");
    c.add( new MyInteger(5));
    Collection<MyInteger> removals
      = new ArrayList<MyInteger>();
    removals.add(new MyInteger(1));
    removals.add(new MyInteger(5));
    c.removeAll(removals);
    checkCollection(c, 2, "24");
    c.remove(new MyInteger(2));
    checkCollection(c, 1, "4");
    c.remove(new MyInteger(4));
    checkCollection(c, 0, "");
    removed =  c.remove(new MyInteger(5));
    if (removed) {
      throw new SortingFailureException
        ( "remove() of out-of-collection " 
        + " item 5 (for empty collection) returned true");
    }
    c.clear();
    for (int i=0; i<100; ++i) {
      c.add( new MyInteger(i));
      //System.out.println("Added " + i);
    }
    RandomInput random = new RandomInput();
    int[] deletes = random.randomPermutation(100);
    for (int d : deletes) {
      if (!c.remove(new MyInteger(d))) {
        throw new SortingFailureException
          ( "Delete of " + d + " return false." );
      }
    }
  }
  private void checkCollection
    ( Collection<MyInteger> c, int n, String content) {
    if ( c.size() != n ) {
      throw new SortingFailureException
        ( "Size of " + c.toString() 
        + " was supposed to be " + n
        + " but was " + c.size() );
    }
    StringBuffer s = new StringBuffer();
    for ( Iterator<MyInteger> i = c.iterator();
          i.hasNext(); ) {
      /*System.out.println(".");*/
      s.append("" + i.next().intValue());
    }
    String actual = s.toString();
    if (!actual.equals(content)) {
      throw new SortingFailureException
        ( "Content of " + c.toString() 
        + " was supposed to be " + content
        + " but was " + actual );
    }
    System.out.println("Collection check passed: " + content);
  }
}
