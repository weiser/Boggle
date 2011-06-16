package weiser.david;

/**
 * this class represents an (x,y,string) value which tells us that, at position (x,y) the current word is string
 * @author david
 *
 */
public class BoggleState {

  private int posx;
  private int posy;
  private String word;
  
  public BoggleState(int x,int y, String word){
    this.posx = x;
    this.posy = y;
    this.word = word;
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
