package weiser.david;

import java.util.Arrays;

/**
 * this class represents an (x,y,string) value which tells us that, at position (x,y) the current word is string
 * @author david
 *
 */
public class BoggleState {

  private int posx;
  private int posy;
  private String word;
  private String soughtWord;
  
  
  public BoggleState(String entry, int x,int y, String word){
    this.soughtWord = entry;
    this.posx = x;
    this.posy = y;
    this.word = word;
    
  }
  
  public String getSoughtWord() {
    return soughtWord;
  }

  public void setSoughtWord(String soughtWord) {
    this.soughtWord = soughtWord;
  }

  public BoggleState(String entry, int x, int y, String letterAt,
      boolean[][] visited2) {
    this.posx = x;
    this.posy = y;
    this.word = letterAt;
    this.soughtWord = entry;
   
  }

  
  private boolean[][] deepCopyVisited(boolean[][] visited2) {
    boolean[][] copy = new boolean[visited2.length][];
    for(int ii = 0; ii < visited2.length; ii++){
      copy[ii] = Arrays.copyOf(visited2[ii], visited2[ii].length);
    }
    return copy;
  }
  
  public String toString(){
    return "("+this.posx+","+this.posy+") = '"+this.word+"'";
  }
  
  
  public int getPosx() {
    return posx;
  }
  public void setPosx(int posx) {
    this.posx = posx;
  }
  public int getPosy() {
    return posy;
  }
  public void setPosy(int posy) {
    this.posy = posy;
  }
  public String getWord() {
    return word;
  }
  public void setWord(String curWord) {
    this.word = curWord;
  }
  
}
