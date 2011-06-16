package weiser.david;

import java.util.HashMap;
import java.util.List;

public class Trie {

  private HashMap<String, Trie> trie;

  /**
   * creates a prefix trie from a list of Strings
   * 
   * @param dict
   */
  public Trie(List<String> dict) {
    this.trie = new HashMap<String, Trie>();
    for (String entry : dict) {
      createEntry(entry);
    }
  }

  public void createEntry(String entry) {
    createEntryHelper(entry, 1);
  }

  private void createEntryHelper(String entry, int i) {
    if (i == entry.length()) {
      return;
    }
    if (!this.trie.containsKey(entry.substring(0, i))) {
      this.trie.put(entry.substring(0, i), createTrie(entry, i + 1));
    }

  }

  private Trie createTrie(String entry, int i) {
    // TODO Auto-generated method stub
    if (i == entry.length()) {
      return new Trie(entry, null);
    } else {
      Trie t = new Trie(entry.substring(0, i), createTrie(entry, i + 1));
      return t;
    }
  }

  public Trie() {
    this.trie = new HashMap<String, Trie>();
  }

  public Trie(String entry, Trie t) {
    this.trie = new HashMap<String, Trie>();
    this.trie.put(entry, t);
  }

  private void setWord(String entry) {
    // TODO Auto-generated method stub

  }

  public boolean containsWord(String word) {
    // if the word length is 1, we don't need to recurse.
    if (word.length() == 1 && this.trie.containsKey(word))
      return true;

    return containsWordHelper(this.trie.get(word.substring(0, 1)), word, 1 + 1);
  }

  private boolean containsWordHelper(Trie t, String word, int index) {
    if (t == null)
      return false;
    if (t.containsKey(word.substring(0, index)) && index == word.length()) {
      return true;
    }
    return t.containsWordHelper(t.get(word.substring(0, index)), word,
        index + 1);

  }

  private Trie get(String substring) {
    // TODO Auto-generated method stub
    return this.trie.get(substring);
  }

  private boolean containsKey(String substring) {
    // TODO Auto-generated method stub
    return this.trie.containsKey(substring);
  }

}
