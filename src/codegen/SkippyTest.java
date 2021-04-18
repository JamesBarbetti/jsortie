package codegen;

public class SkippyTest  {
  public void testSkippy6() {
    generateSkippyCode(6);
  }
  private void generateSkippyCode(int p) {
    StringBuilder code = new StringBuilder();
    code.append ("public class SkippyExpander" + p + " extends HolierThanThouBase {\n");
    code.append ("  public SkippyExpander" + p + "() {\n");
    code.append ("    super(" + p + ")\n");
    code.append ("  }");
    code.append ("  @Override\n");
    code.append ("  public int[] expandPartitionsToRight\n");
    code.append ("    ( int[] vArray, int start, int[] pivotIndices, int startRight, int stop) {\n");
    for (int i=0;i<p;++i) {
      code.append ("    int hole" + (i+1) + " = pivotIndices[" + i + "];\n");
      code.append ("    int v" +  Character.toString((char)('P' + i)) + "    = vArray[hole" + (i+1) + "];\n");	
    }
    code.append ("\n");
    code.append ("    for (int scan=startRight; scan<stop; ++scan) {\n");
    code.append ("      int vScan      = vArray[scan];\n");
    for (int i=1;i<=p;++i) {
      code.append ("      vArray[hole" + i + "]  = " + ((i==1) ? "vScan" : ("vArray[hole" + (i-1) + "]")) + ";\n");
      code.append ("      hole" + i + "         += ( vScan <= v" + Character.toString((char)('O' + i)) + " ) ? 1 : 0;\n");
    }
    code.append( "      vArray[scan]   = vArray[hole" + p +"];\n");
    code.append("    }\n");    
    for (int i=0;i<p;++i) {
      code.append("    vArray[hole" + (i+1) + "] = v" + Character.toString((char)('P' + i)) + ";\n");
    }
    code.append("    return new int[] { start");
    for (int i=1;i<=p;++i) {
      code.append(", hole" + i + ", hole" + i + "+1"); 
    }
    code.append(", stop };\n");
    code.append("  }\n");
    code.append("}\n\n");
    
    //Todo: There ought to be an expandPartitionsToLeft as well!
    
    code.append ("public class SkippyPartitioner" + p + " extends SkippyPartitioner2 {\n");
    code.append ("  public SkippyPartitioner" + p + "() {\n");
    code.append ("    mpx = new SkippyExpander" + p + "();\n");
    code.append ("  }\n");
    code.append ("  @Override\n");
    code.append ("  public int getPivotCount() {\n");
    code.append ("    return " + p + ";\n");
    code.append ("  }\n");
    code.append ("}\n");
    System.out.println( code.toString());
  }
}
