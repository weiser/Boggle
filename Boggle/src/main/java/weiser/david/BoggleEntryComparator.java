package weiser.david;

import java.util.Comparator;

/**
 * this class compares two words represented in seperate BoggleStates, and
 * compares each word against the BoggleState.soughtWord. The word which is most
 * similar to the BoggleState.soughtWord will have the minimum key.
 * 
 * @author david
 * 
 * @param <BoggleState>
 */
public class BoggleEntryComparator<T> implements Comparator<T> {

  public int compare(T arg0, T arg1) {
    BoggleState bs1 = (BoggleState) arg0;
    BoggleState bs2 = (BoggleState) arg1;
    return bs1.getWord().compareTo(bs1.getSoughtWord()) - bs2.getWord().compareTo(bs2.getSoughtWord());
  }

}
