package weiser.david;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TrieTest extends TestCase {
  
  public void testInsert(){
    List<String> dict = new ArrayList<String>();
    
    dict.add("a");
    dict.add("aaa");
    dict.add("aa");
    
    
    Trie trie = new Trie(dict);
    
    Assert.assertEquals(true, trie.containsWord("aaa"));
    Assert.assertEquals(true,trie.containsWord("aa"));
    Assert.assertEquals(true,trie.containsWord("a"));
    
    Assert.assertEquals(false, trie.containsWord("b"));
    
    
  }

}
