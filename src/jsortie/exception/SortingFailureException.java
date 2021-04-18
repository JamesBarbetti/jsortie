package jsortie.exception;

public class SortingFailureException
  extends RuntimeException { 
  private static final long serialVersionUID = 1L;
  //thrown by "Checked..." protector classes
  public SortingFailureException(String text) {
    super(text);
    System.out.println(text);
  }
  public void appendMessage(String s) {
    System.out.println(s);
  }
}
