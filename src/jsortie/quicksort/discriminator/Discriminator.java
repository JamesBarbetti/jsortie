package jsortie.quicksort.discriminator;

public interface Discriminator {
  void pushState();
  void popState();
  boolean ofInterest
    ( int start, int stop );
  int getTargetIndex
    ( int indexStart, int indexStop );
}
