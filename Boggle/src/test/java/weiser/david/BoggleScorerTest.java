package weiser.david;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BoggleScorerTest extends TestCase {

  /**
   * The maximum score possible for any given Boggle game is one in which all
   * dice show the same letter (e.g. "a") and the dictionary consists of 25
   * entries ("aaa", "aaaa", ..., 25 consecutive "a"'s). This gives us a max
   * score of 210.
   */
  public void testMaxScore() {
    List<String> dictionary = new ArrayList<String>();

    String word = "a";
    for (int ii = 0; ii < 25; ii++) {
      dictionary.add(word);
      word += "a";
    }
    Assert.assertEquals(25, dictionary.size());

    BoggleBoard boggleBoard = boardFilledWithAs(5,5);

    BoggleScorer scorer = new BoggleScorer(dictionary, boggleBoard);
    scorer.score();
    Assert.assertEquals(210, scorer.getScore());

  }

  private BoggleBoard boardFilledWithAs(int width, int height) {
    BoggleBoard boggleBoard = new BoggleBoard(width, height);
    for (int ii = 0; ii < boggleBoard.getWidth(); ii++) {
      for (int jj = 0; jj < boggleBoard.getLength(); jj++) {
        boggleBoard.setPosition(ii, jj, "a");
      }
    }
    return boggleBoard;
  }

  public void testScoreWord() {
    BoggleScorer scorer = new BoggleScorer(new ArrayList<String>(), new BoggleBoard(5,5));
    
    Assert.assertEquals(0,scorer.scoreWord(""));
    Assert.assertEquals(0,scorer.scoreWord("1"));
    Assert.assertEquals(0,scorer.scoreWord("12"));
    Assert.assertEquals(1,scorer.scoreWord("123"));
    Assert.assertEquals(1,scorer.scoreWord("1234"));
    Assert.assertEquals(2,scorer.scoreWord("12345"));
    Assert.assertEquals(3,scorer.scoreWord("123456"));
    Assert.assertEquals(5,scorer.scoreWord("1234567"));
    Assert.assertEquals(11,scorer.scoreWord("12345678"));
    Assert.assertEquals(11,scorer.scoreWord("123456789"));
    Assert.assertEquals(11,scorer.scoreWord("1234567890"));
  }
  
  /**
   * ensure that when we move, that the coordinates change accordingly.
   * (0,0) is the bottom left.  (5,5) is the top right.
   */
  public void testDirections(){
    
    boolean[][] visitedSquares = new boolean[5][5]; 
    
    clearVisited(visitedSquares);
    
    BoggleState state = new BoggleState("a",0,0, "a");
    
    BoggleScorer scorer  = new BoggleScorer(new ArrayList<String>(), boardFilledWithAs(5,5));
    
    Assert.assertEquals(1, scorer.goRight(state).getPosx());
    Assert.assertEquals(0, scorer.goRight(state).getPosy());
    
    Assert.assertEquals(null, scorer.goLeft(state));
    Assert.assertEquals(null, scorer.goLeft(state));
    
    Assert.assertEquals(0, scorer.goUp(state).getPosx());
    Assert.assertEquals(1, scorer.goUp(state).getPosy());
    
    Assert.assertEquals(null, scorer.goDown(state));
    Assert.assertEquals(null, scorer.goDown(state));
    
    Assert.assertEquals(1, scorer.goRightUp(state).getPosx());
    Assert.assertEquals(1, scorer.goRightUp(state).getPosy());
    
    Assert.assertEquals(null, scorer.goLeftDown(state));
    Assert.assertEquals(null, scorer.goLeftDown(state));
    
    Assert.assertEquals(null, scorer.goLeftUp(state));
    Assert.assertEquals(null, scorer.goLeftUp(state));
    
    Assert.assertEquals(null, scorer.goRightDown(state));
    Assert.assertEquals(null, scorer.goRightDown(state));
    
    
  }

  private void clearVisited(boolean[][] visitedSquares) {
    for(int ii = 0; ii < visitedSquares.length; ii++){
      for(int jj = 0; jj < visitedSquares[ii].length; jj++){
        visitedSquares[ii][jj] = false;
      }
    }
    
  }

}
