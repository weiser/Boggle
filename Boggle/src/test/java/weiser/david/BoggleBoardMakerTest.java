package weiser.david;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BoggleBoardMakerTest extends TestCase {

  /**
   * A planned configuration of a board should win out over a board whose
   * configuration is random
   */
  public void testBoardMakerDoesBetterThanRandom() {

    List<String> dictionary = new ArrayList<String>();

    initDictionary(dictionary, "src/test/resources/dict.txt");

    BoggleBoard randomBoard = new BoggleBoard(5, 5);
    configureRandomBoard(randomBoard);
    BoggleBoardMaker plannedBoardMaker = new BoggleBoardMaker(new BoggleBoard(
        5, 5), dictionary);
    plannedBoardMaker.plan();

    BoggleScorer scorerRandom = new BoggleScorer(dictionary, randomBoard);
    BoggleScorer scorerPlanned = new BoggleScorer(dictionary, plannedBoardMaker
        .getBoard());
    try {
      String wordsPlaced = "";
      for(String placedWord : plannedBoardMaker.getWordsPlaced()){
        wordsPlaced += " "+placedWord;
      }
      System.out.println("Planned (words placed: {"+wordsPlaced+"}: \n"+plannedBoardMaker.getBoard().toString());
      System.out.println("\nRandom: \n"+randomBoard.toString());
      scorerPlanned.score();
      scorerRandom.score();
      Assert.assertEquals("scorerRandom.getScore() = "
          + scorerRandom.getScore() + " scorerPlanned.getScore() = "
          + scorerPlanned.getScore(), true,
          scorerRandom.getScore() < scorerPlanned.getScore());
    } catch (NullPointerException npe) {
      Assert.fail(npe.getMessage());
    }
  }
  
  public void testCoordinateConversion(){
    BoggleBoardMaker bmm = new BoggleBoardMaker(new BoggleBoard(5,5), new ArrayList<String>());
    
    int x = 4;
    int y = 4;
    
    Assert.assertEquals(24, bmm.calcIntegerAddress(x, y));
    
    int intAddress = 24;
    Assert.assertEquals(4, bmm.calcXCoord(intAddress));
    Assert.assertEquals(4, bmm.calcYCoord(intAddress));
    
    Assert.assertEquals(Integer.MAX_VALUE, bmm.calcIntegerAddress(0, 0));
    Assert.assertEquals(0, bmm.calcXCoord(0));
    Assert.assertEquals(0, bmm.calcYCoord(0));
    
  }

  /**
   * configures a random Boggle Board using only lower case letters.
   * 
   * @param randomBoard
   */
  private void configureRandomBoard(BoggleBoard randomBoard) {
    char[] letters = "abcedfghijklmnopqrstuvwxyz".toCharArray();
    Random rand = new Random();

    for (int ii = 0; ii < randomBoard.getWidth(); ii++) {
      for (int jj = 0; jj < randomBoard.getLength(); jj++) {
        randomBoard.setPosition(ii, jj, String.valueOf(letters[rand
            .nextInt(letters.length)]));
      }
    }

  }

  private void initDictionary(List<String> dictionary, String filename) {
    // open the file specified by file name and feed it, line by line, into the
    // dictionary.
    try {
      FileInputStream fis = new FileInputStream(filename);
      DataInputStream dis = new DataInputStream(fis);
      BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
      String line;
      while ((line = reader.readLine()) != null) {
        dictionary.add(line.toLowerCase());
      }
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
