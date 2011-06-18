package weiser.david;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Driver {
  
  public static void main(String[] args){
    List<String> dictionary = new ArrayList<String>();

    if(args.length < 2)
    Driver.initDictionary(dictionary, "src/main/resources/dict.txt");
    else
      Driver.initDictionary(dictionary, args[1]);
   
    
    BoggleBoard maxBoard = null;
    int maxScore = -1;
    List<String> winningVocab = null;
    Set<String> scorerFoundWords = null;
    //run 100 simulations.  See if we can get a winner!
    for(int i = 0; i < 100; i++){
      BoggleBoardMaker plannedBoardMaker = new BoggleBoardMaker(new BoggleBoard(
          5, 5), dictionary);
      plannedBoardMaker.plan();

      BoggleScorer scorer = new BoggleScorer(dictionary, plannedBoardMaker
          .getBoard());
      
      scorer.score();
      if(maxScore < scorer.getScore()){
        maxBoard = plannedBoardMaker.getBoard();
        maxScore = scorer.getScore();
        scorerFoundWords = scorer.getFoundWords().keySet();
        winningVocab = plannedBoardMaker.getWordsPlaced();
      }
    }
    
    
    
    System.out.println("After 100 simulations, using a hill climbing approach, the max score found was: "+maxScore+" for the given board:");
    System.out.println(maxBoard.toString());
    System.out.print("The planner used the following,"+winningVocab.size()+", words to create its configuration: ");
    for(String word: winningVocab){
      System.out.print(word+", ");
    }
    
    System.out.println("\nThe scorer found the following,"+scorerFoundWords.size()+", words: ");
    for(String word: scorerFoundWords){
      System.out.print(word+", ");
    }
    
    
  }

  private static void initDictionary(List<String> dictionary, String filename) {
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
