package jsortie.testing;
import java.util.Arrays;

import org.junit.Test;

import jsortie.helper.ShiftHelper;
import junit.framework.TestCase;

public class RangeSortHelperTest extends TestCase {
  protected ShiftHelper shifter = new ShiftHelper();
  public boolean arrayIsIdentityPermutation(int [] vArray) {
    for (int i=0; i<vArray.length; ++i)
      if (vArray[i]!=i) return false;
    return true;
  }	
  @Test
  public void testRotateLeft() {
  }
  @Test
  public void testRotateRight() {		
  }
  @Test
  public void testMoveBackToFront() {
    for (int back=0; back<=10; ++back) {
      int [] vArray = new int[10];
      for (int i=0; i<vArray.length; ++i) { 
        vArray[i] = i; }
      shifter.moveBackElementsToFront(vArray, 0, vArray.length-back, vArray.length);
      for (int j=0; j<back; ++j) {
        assertSame(vArray[j], j + vArray.length - back);
      }
      Arrays.sort(vArray);
      assertTrue( arrayIsIdentityPermutation(vArray));
    }
  }
  @Test
  public void testMoveFrontToBack() {
    for (int front=0; front<=10; ++front) {
      int [] vArray = new int[10];
      for (int i=0; i<vArray.length; ++i) { 
        vArray[i]=i; 
      }
      shifter.moveFrontElementsToBack(vArray, 0, front, vArray.length);
      for (int j=vArray.length - front; j<vArray.length; ++j) {
	    assertSame(vArray[j], j - vArray.length + front);
      }
      Arrays.sort(vArray);
      assertTrue( arrayIsIdentityPermutation(vArray));
    }
  }
}
