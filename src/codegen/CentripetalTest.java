package codegen;

public class CentripetalTest {
  public void testCentripetalCases(int p) {
	  dumpCentripetalCode(p, 0, p+1, 0, p+1, "");
  }
  private void dumpCentripetalCode(int p, int startLeft, int stopLeft, int startRight, int stopRight, String indent) {
    if (startLeft+1<stopLeft) {
      int x = startLeft + (stopLeft-startLeft) / 2;
      System.out.println(indent + "if ( vLeft<=vPivot" + x + " ) {");
      dumpCentripetalCode(p, startLeft, x, startRight, stopRight, indent+ "  ");
      System.out.println(indent + "} else {");
      dumpCentripetalCode(p, x, stopLeft, startRight, stopRight, indent+ "  ");
      System.out.println(indent + "}");
    } else {
      if (startRight+1<stopRight) {
        int x = startRight + (stopRight-startRight+1) / 2;
        System.out.println(indent + "if ( vRight<=vPivot" + x + " ) {");
        dumpCentripetalCode(p, startLeft, stopLeft, startRight, x, indent+ "  ");
        System.out.println(indent + "} else {");
        dumpCentripetalCode(p, startLeft, stopLeft, x, stopRight, indent+ "  ");
        System.out.println(indent + "}");
      } else {
        System.out.println(indent + "//vLeft in " + partyName(stopLeft) + ", vRight in " + partyName(stopRight));
        if (stopLeft <= stopRight) {
          fiddleMiddle(p, stopLeft, "vLeft", stopRight, "vRight", indent);
        } else {
          fiddleMiddle(p, stopRight, "vRight", stopLeft, "vLeft", indent);
        }
      }
    }
  }
  
  private String hole(int p, int i) { return (i==0) ? "scanLeft" : ((i<=p) ? ("hole" + i ) : "scanRight"); }
  
  private void fiddleMiddle(int p, int i, String iVar, int j, String jVar, String indent) {
    for (int x=0; x+1<i; ++x) {
      String rhs = (x+1 < i) ? (hole(p,x+1) + "-1") : hole(p,x+1);
      System.out.println(indent + "vArray[" + hole(p,x) + "] = vArray[" + rhs + "];");
    }
    System.out.println(indent + "vArray[" + hole(p,i-1) + "] = " + iVar + ";");
    for (int y=p+1; j<y; --y) {
      String rhs = (j < y+1) ? (hole(p,y-1) + "+1") : hole(p,y-1);
      System.out.println(indent + "vArray[" + hole(p,y) + "] = vArray[" + rhs + "];");
    }
    System.out.println(indent + "vArray[" + hole(p,j) + "] = " + jVar + ";");    
    
    for (int x=1; x<i; ++x) {
      System.out.println(indent + hole(p,x) + "--;");
    }
    for (int y=p; j<=y; --y) {
      System.out.println(indent + hole(p,y) + "++;");
    }
    
  }
  
  private String partyName(int cardinal) {
    switch (cardinal) {
      case 1: return "1st partition";
      case 2: return "2nd partition";
      case 3: return "3rd partition";
      default: return "" + cardinal + "th partition";
    }
  }
  
  public void testCentripetal3() {
	testCentripetalCases(4);
  }
}
