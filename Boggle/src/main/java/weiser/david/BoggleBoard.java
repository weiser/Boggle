package weiser.david;

public class BoggleBoard {

  private String[][] board;
  private int width;
  private int length;
  
  
  public BoggleBoard(int width, int length) {
    this.board = new String[width][length];
    this.width = width;
    this.length = length;
  }

  public int getWidth() {
    // TODO Auto-generated method stub
    return this.width;
  }

  public int getLength() {

    return this.length;
  }

  public void setPosition(int ii, int jj, String letter) {
    if(ii >=0 && ii < this.width){
      if(jj >=0 && jj < this.length){
        this.board[ii][jj] = letter;
      }
    }
    
  }

  public String getLetterAt(int nextX, int nextY) {
    // TODO Auto-generated method stub
    return this.board[nextX][nextY];
  }
  

}
