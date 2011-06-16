package weiser.david;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Hello world!
 * 
 */
public class BoggleScorer {

  private HashMap<String,String> dictionary;
  private BoggleBoard board;
  private HashMap<String, String> foundWords;
  private Boolean[][] visited;
  private int score;

  /**
   * Initalizes the Boggle Scorer
   * 
   * @param dictionary
   *          - a list of words
   * @param boggleBoard
   *          - a 5x5 array. Each entry is a single letter.
   */
  public BoggleScorer(List<String> dictionary, BoggleBoard boggleBoard) {
    this.dictionary = new HashMap<String,String>();
    for(String entry: dictionary){
      this.dictionary.put(entry, "");
    }
    this.board = boggleBoard;
    this.foundWords = new HashMap<String, String>();
    this.score = -1;
    this.visited = new Boolean[this.board.getWidth()][this.board.getLength()];
  }

  public int getScore() {
    return this.score;
  }

  /**
   * This function is the workhorse of the BoggleScorer. It computes the score
   * of the configuration of the board.
   */
  public void score() {

    for (int ii = 0; ii < 5; ii++) {
      for (int jj = 0; jj < 5; jj++) {
        clearVisits();
        System.out.print("finding at: ("+ii+","+jj+")");
        findWordsAt(ii, jj);
      }
    }

  }

  private void clearVisits() {
    for (int ii = 0; ii < 5; ii++) {
      for (int jj = 0; jj < 5; jj++) {
        visited[ii][jj] = false;
      }
    }
  }

  /**
   * this function finds all words which start at (posx,posy) on the board.
   * 
   * @param posx
   * @param posy
   */
  private void findWordsAt(int posx, int posy) {
    // Since Java isn't a huge fan of recursion, we're going to simulate
    // recursion with a stack.

    Stack<BoggleState> stack = new Stack<BoggleState>();

    BoggleState initState = new BoggleState(posx, posy, this.board.getLetterAt(
        posx, posy));

    stack.push(initState);
    while (!stack.isEmpty()) {
      BoggleState curState = stack.pop();
      if (curState != null) {

        // if the word represented in curState is in the dictionary and it
        // hasn't been found yet, update the score

        if (curState.getWord().length() >= 3)
          System.out.print("Have we seen '" + curState + "': "
              + foundWords.containsKey(curState.getWord()) + "\n");
        if (!foundWords.containsKey(curState.getWord())
            && this.dictionary.containsKey(curState.getWord())) {
          int wordScore = scoreWord(curState.getWord());
          this.score += wordScore;
          foundWords.put(curState.getWord(), "");
        }

        // generate the next states to visit. If a certain position has already
        // been visited, the go*(curState) method will return null
        stack.push(goUp(curState));
        stack.push(goDown(curState));
        stack.push(goLeft(curState));
        stack.push(goRight(curState));
        stack.push(goLeftUp(curState));
        stack.push(goLeftDown(curState));
        stack.push(goRightUp(curState));
        stack.push(goRightDown(curState));

      }
    }
    return;
  }

  public int scoreWord(String word) {
    // TODO Auto-generated method stub
    int scoreVal = 0;
    switch (word.length()) {
    case 1:
    case 2:
      scoreVal = 0;
      break;
    case 3:
    case 4:
      scoreVal = 1;
      break;
    case 5:
      scoreVal = 2;
      break;
    case 6:
      scoreVal = 3;
      break;
    case 7:
      scoreVal = 5;
      break;
    default:
      if (word.length() >= 8)
        scoreVal = 11;
      else
        scoreVal = 0;
      break;
    }
    return scoreVal;
  }

  private BoggleState makeNewBoggleState(BoggleState curState, int nextX,
      int nextY) {
    if (this.visited[curState.getPosx()][curState.getPosy()] == false) {
      this.visited[curState.getPosx()][curState.getPosy()] = true;

      return new BoggleState(nextX, nextY, curState.getWord()
          + this.board.getLetterAt(nextX, nextY));
    } else
      return null;
  }

  
  public BoggleState goRightDown(BoggleState curState) {

    int nextX = curState.getPosx() + 1;
    int nextY = curState.getPosy() - 1;
    if (nextX < 0 || nextY < 0 || nextX >= this.board.getWidth()
        || nextY >= this.board.getLength())
      return null;
    return makeNewBoggleState(curState, nextX, nextY);
  }

  
  public BoggleState goRightUp(BoggleState curState) {
    int nextX = curState.getPosx() + 1;
    int nextY = curState.getPosy() + 1;
    if (nextX < 0 || nextY < 0 || nextX >= this.board.getWidth()
        || nextY >= this.board.getLength())
      return null;
    return makeNewBoggleState(curState, nextX, nextY);
  }

  public BoggleState goLeftDown(BoggleState curState) {
    int nextX = curState.getPosx() - 1;
    int nextY = curState.getPosy() - 1;
    if (nextX < 0 || nextY < 0 || nextX >= this.board.getWidth()
        || nextY >= this.board.getLength())
      return null;
    return makeNewBoggleState(curState, nextX, nextY);
  }

  public BoggleState goLeftUp(BoggleState curState) {
    // TODO Auto-generated method stub
    int nextX = curState.getPosx() - 1;
    int nextY = curState.getPosy() + 1;
    if (nextX < 0 || nextY < 0 || nextX >= this.board.getWidth()
        || nextY >= this.board.getLength())
      return null;
    return makeNewBoggleState(curState, nextX, nextY);
  }

  public BoggleState goRight(BoggleState curState) {
    int nextX = curState.getPosx() + 1;
    int nextY = curState.getPosy() + 0;
    if (nextX < 0 || nextY < 0 || nextX >= this.board.getWidth()
        || nextY >= this.board.getLength())
      return null;
    return makeNewBoggleState(curState, nextX, nextY);
  }

  public BoggleState goLeft(BoggleState curState) {
    int nextX = curState.getPosx() - 1;
    int nextY = curState.getPosy() + 0;
    if (nextX < 0 || nextY < 0 || nextX >= this.board.getWidth()
        || nextY >= this.board.getLength())
      return null;
    return makeNewBoggleState(curState, nextX, nextY);
  }

  public BoggleState goDown(BoggleState curState) {
    int nextX = curState.getPosx() + 0;
    int nextY = curState.getPosy() - 1;
    if (nextX < 0 || nextY < 0 || nextX >= this.board.getWidth()
        || nextY >= this.board.getLength())
      return null;
    return makeNewBoggleState(curState, nextX, nextY);
  }

  public BoggleState goUp(BoggleState curState) {
    int nextX = curState.getPosx() + 0;
    int nextY = curState.getPosy() + 1;
    if (nextX < 0 || nextY < 0 || nextX >= this.board.getWidth()
        || nextY >= this.board.getLength())
      return null;
    return makeNewBoggleState(curState, nextX, nextY);
  }

}
