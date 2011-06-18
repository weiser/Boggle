package weiser.david;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Hello world!
 * 
 */
public class BoggleScorer {

  private List<String> dictionary;
  private BoggleBoard board;
  private HashMap<String, String> foundWords;
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
    this.dictionary = new ArrayList<String>();
    for (String entry : dictionary) {
      this.dictionary.add(entry);
    }
    this.board = boggleBoard;
    this.foundWords = new HashMap<String, String>();
    this.score = 0;

  }

  public int getScore() {
    return this.score;
  }

  /**
   * This function is the workhorse of the BoggleScorer. It computes the score
   * of the configuration of the board. It goes through each square, attempting
   * to find each word in the dictionary which starts at the square.
   */
  public void score() {

    for (String entry : this.dictionary) {
      for (int ii = 0; ii < 5; ii++) {
        for (int jj = 0; jj < 5; jj++) {
          if (!foundWords.containsKey(entry) && findWordAt(entry, ii, jj)) {
            foundWords.put(entry, "");
          }
        }
      }
    }

  }

  /**
   * This method finds a word, entry, starting at (x, y). It returns true if
   * entry was found starting at (x,y) and false otherwise.
   * 
   * @param entry
   * @param x
   * @param y
   * @return boolean - was entry found, starting at (x,y)
   */
  private boolean findWordAt(String entry, int x, int y) {
    // Since Java isn't a huge fan of recursion, we're going to simulate
    // recursion with a queue.

    // let's use a priority queue which is based on how close a the prefix
    // represented by a BoggleState is to the entry we're looking for.
    //Queue<BoggleState> queue = new PriorityQueue<BoggleState>(10000,
    //    new BoggleEntryComparator<BoggleState>());
    
    Stack<BoggleState> stack = new Stack<BoggleState>();

    if (this.board.getLetterAt(x, y) == null) {
      throw new NullPointerException("(" + x + "," + y + ") is null");
    }
    BoggleState initState = new BoggleState(entry, x, y, this.board
        .getLetterAt(x, y));

    //queue.add(initState);

    stack.push(initState);
    while (!stack.isEmpty()){//!queue.isEmpty()) {
      BoggleState curState = stack.pop();//queue.poll();

      if (curState == null)
        continue;

      //System.out.println("curState word: " + curState.getWord());
      // if the word represented in curState is in the dictionary and it
      // hasn't been found yet, update the score

      if (entry.equals(curState.getWord()) && entry.length() >= 3) {
        int wordScore = scoreWord(curState.getWord());
        this.score += wordScore;
        return true;
      } else {

        if (entry.startsWith(curState.getWord())) {
          // generate the next states to visit. If a certain position has
          // already
          // been visited, the go*(curState) method will return null
          try {
            
            stack.push(goUp(curState));
            stack.push(goDown(curState));
            stack.push(goLeft(curState));
            stack.push(goRight(curState));
            stack.push(goLeftUp(curState));
            stack.push(goLeftDown(curState));
            stack.push(goRightUp(curState));
            stack.push(goRightDown(curState));
          } catch (NullPointerException npe) {
            // if we caught this, the next state was null. we can ignore this.
          }
        } 
      }
    }
    return false;

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

     BoggleState bs =new BoggleState(curState.getSoughtWord(), nextX, nextY, curState
         .getWord()
         + this.board.getLetterAt(nextX, nextY));
     //System.out.println("new state: "+bs.toString());
    return bs;

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

  public HashMap<String, String> getFoundWords() {
    return foundWords;
  }

  public void setFoundWords(HashMap<String, String> foundWords) {
    this.foundWords = foundWords;
  }
}
