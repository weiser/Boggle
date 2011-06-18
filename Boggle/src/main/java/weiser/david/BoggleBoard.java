package weiser.david;

public class BoggleBoard {

  private String[][] board;
  private int width;
  private int length;

  /**
   * creates a width x length board filled with null values
   * 
   * @param width
   * @param length
   */
  public BoggleBoard(int width, int length) {
    this.board = new String[width][length];
    this.width = width;
    this.length = length;
    emptyBoard();
  }

  private void emptyBoard() {
    for (int ii = 0; ii < this.width; ii++) {
      for (int jj = 0; jj < this.length; jj++) {
        this.board[ii][jj] = null;
      }
    }

  }

  public int getWidth() {
    // TODO Auto-generated method stub
    return this.width;
  }

  public int getLength() {

    return this.length;
  }

  public void setPosition(int ii, int jj, String letter) {
    if (ii >= 0 && ii < this.width) {
      if (jj >= 0 && jj < this.length) {
        this.board[ii][jj] = letter;
      }
    }

  }

  public String getLetterAt(int nextX, int nextY) {
    // TODO Auto-generated method stub
    return this.board[nextX][nextY];
  }

  public String toString() {
    String ret = "";
    for (int ii = this.board.length - 1; ii >= 0; ii--) {
      ret+="\n";
      for (int jj = 0; jj < this.board[ii].length; jj++) {
        ret += this.board[ii][jj]+" ";
      }
    }
    return ret;
  }

  public boolean isFull() {
    // TODO Auto-generated method stub
    for (int ii = 0; ii < this.width; ii++) {
      for (int jj = 0; jj < this.length; jj++) {
        if (this.board[ii][jj] == null)
          return false;
      }
    }
    return true;
  }

}
