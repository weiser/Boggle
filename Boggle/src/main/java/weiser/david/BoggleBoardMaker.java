package weiser.david;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class BoggleBoardMaker {

  private List<String> dictionary;
  private BoggleBoard board;
  private List<String> wordsPlaced;

  public BoggleBoardMaker(BoggleBoard board, List<String> dict) {
    this.board = board;
    this.dictionary = dict;
    this.wordsPlaced = new ArrayList<String>();
  }

  public BoggleBoard getBoard() {
    // TODO Auto-generated method stub
    return this.board;
  }

  /**
   * Plan the board by using a greedy local search. The order in which words are
   * placed on the board are based on: 1. The length of the word (prefer words
   * which are 8 characters long, since 8 character words get the most points
   * per character) 2. If the word shares a prefix with another word.
   */
  public void plan() {

    // currently, this only prioritizes by word length
    Queue<String> priorityDict = prioritizeByLength(this.dictionary);

    while (!this.board.isFull() && !priorityDict.isEmpty()) {
      String word = priorityDict.poll();
      if (placeWordOnBoard(word)) {
        this.wordsPlaced.add(word);
      }
    }

  }

  /**
   * Place a word on the board in a greedy manner.
   * 
   * First, try to place the word such that it shares a prefix with another
   * word.
   * 
   * If the first approach fails, place the word randomly.
   * 
   * If the word fails to get placed on the board return false
   * 
   * @param word
   * @return boolean -- true if the word was placed on the board and false
   *         otherwise.
   */
  private boolean placeWordOnBoard(String word) {
    int posx = -1;
    int posy = -1;
    int letterIndex = 0;

    // a positive number indicates that a letter was placed on the space right
    // now. a negative number means that the space was already occupied when we
    // got here.
    List<Integer> visitedSpaces = new ArrayList<Integer>(50);

    for (int x = 0; x < this.board.getWidth() && posx < 0 && posy < 0; x++) {
      for (int y = 0; y < this.board.getLength() && posx < 0 && posy < 0; y++) {
        if (this.board.getLetterAt(x, y) != null
            && this.board.getLetterAt(x, y).equals(
                String.valueOf(word.charAt(0)))) {
          posx = x;
          posy = y;
          visitedSpaces.add(0 - calcIntegerAddress(posx, posy));
        }
      }
    }

    if (posx < 0 && posy < 0) {
      Random rand = new Random();
      do{
      posx = rand.nextInt(this.board.getWidth());
      posy = rand.nextInt(this.board.getLength());
      }while(this.board.getLetterAt(posx, posy) != null);
      
      visitedSpaces.add(calcIntegerAddress(posx, posy));
      this.board.setPosition(posx, posy, String.valueOf(word
          .charAt(letterIndex)));
      System.out.println("Inserted: "+word.charAt(letterIndex)+"\n"+this.board.toString());
    }

    letterIndex++;
    int nextLetterAddress;

    boolean moreMoves = true;
    // until we either finish placing the word, or fail to place the word

    while (moreMoves) {
      if (letterIndex >= word.length()) {
        System.out.println("placed: " + word +" board looks like: ");
        System.out.println(this.board.toString());
        return true;
      }
      // if the next letter of the word is next to where we're starting at, and
      // we
      // haven't visited the space before, follow the next letter
      if ((nextLetterAddress = addressOfLetterInWord(String.valueOf(word
          .charAt(letterIndex)), posx, posy)) >= 0
          && !visitedSpaces.contains(0 - nextLetterAddress)
          && !visitedSpaces.contains(nextLetterAddress)) {
        posx = calcXCoord(nextLetterAddress);
        posy = calcYCoord(nextLetterAddress);
        visitedSpaces.add(0 - calcIntegerAddress(posx, posy));
        letterIndex++;
        continue;
      }
      // if we got here, we failed to create the word using alerady placed
      // letters

      // need to put the word in empty spaces, since there was not an already
      // placed letter that worked for us
      if ((nextLetterAddress = addressOfNeighborEmptySpace(posx, posy)) >= 0 && !visitedSpaces.contains(nextLetterAddress)) {
        
        visitedSpaces.add(nextLetterAddress);
        posx = calcXCoord(nextLetterAddress);
        posy = calcYCoord(nextLetterAddress);

        this.board.setPosition(posx, posy, String.valueOf(word
            .charAt(letterIndex)));
        System.out.println("Inserted: "+word.charAt(letterIndex)+"\n"+this.board.toString());
        letterIndex++;
        continue;
      }

      // if we got here, we failed to find either an available letter to help
      // us, or an empty space. We did not place this word.
      moreMoves = false;

    }

    // at this point, we failed to place the word on the board. remove the
    // partial word from the board and return failure

    for (int integerAddress : visitedSpaces) {
      // integerAddress < 0 ==> the letter was already there, so we don't
      // remove it.
      if (integerAddress >= 0) {
        posx = calcXCoord(integerAddress);
        posy = calcYCoord(integerAddress);
        String removed = this.board.getLetterAt(posx, posy);
        System.out.println("Remove ("+word+") before: "+removed+"\n"+this.board.toString());
        this.board.setPosition(posx, posy, null);
        System.out.println("After remove: "+removed+"\n"+this.board.toString());
      }
    }

    return false;
  }

  /**
   * given (posx,posy) coordinates, return the calcIntegerAddress of a random
   * neighbor space which is null.
   * 
   * @param posx
   * @param posy
   * @return -1 if there are no empty neighbor spaces, >= 0 if a space has been
   *         found.
   */
  private int addressOfNeighborEmptySpace(int posx, int posy) {
    int[] neighborOffsets = { -1, 0, 1 };
    List<Integer> allEmptyNeighborSpaces = new ArrayList<Integer>(8);
    for (int x : neighborOffsets) {
      for (int y : neighborOffsets) {
        // ensure we're in bounds
        if (x + posx < this.board.getWidth() && x + posx >= 0
            && y + posy < this.board.getLength() && y + posy >= 0) {
          if (this.board.getLetterAt(x + posx, y + posy) == null)
            allEmptyNeighborSpaces.add(calcIntegerAddress(posx + x, posy + y));
        }
      }
    }
    if (allEmptyNeighborSpaces.isEmpty())
      return -1;
    else {
      Random rand = new Random();
      int integerAddress = allEmptyNeighborSpaces.get(rand.nextInt(allEmptyNeighborSpaces
          .size()));
      return integerAddress==Integer.MAX_VALUE?0:integerAddress;
    }

  }

  /**
   * given (posx,posy) coordinates, return the calcIntegerAddress of a random
   * neighbor space which contains letter.
   * 
   * @param letter
   * @param posx
   * @param posy
   * @return the calIntegerAddress of the neighbor space or -1 if it does not
   *         exist.
   */
  private int addressOfLetterInWord(String letter, int posx, int posy) {
    int[] neighborOffsets = { -1, 0, 1 };
    List<Integer> allLetterNeighborSpaces = new ArrayList<Integer>(8);
    for (int x : neighborOffsets) {
      for (int y : neighborOffsets) {
        // ensure we're in bounds
        if (x + posx < this.board.getWidth() && x + posx >= 0
            && y + posy < this.board.getLength() && y + posy >= 0) {
          if (this.board.getLetterAt(posx + x, posy + y) != null
              && this.board.getLetterAt(x + posx, y + posy).equals(letter))
            allLetterNeighborSpaces.add(calcIntegerAddress(posx + x, posy + y));
        }
      }
    }
    if (allLetterNeighborSpaces.isEmpty())
      return -1;
    else {
      Random rand = new Random();
      int integerAddress =  allLetterNeighborSpaces.get(rand.nextInt(allLetterNeighborSpaces
          .size()));
      return integerAddress==Integer.MAX_VALUE?0:integerAddress;
    }
  }

  /**
   * Also, if (0,0) is passed in, MAXINT is returned.
   * @param posx
   * @param posy
   * @return
   */
  public int calcIntegerAddress(int posx, int posy) {
    if(posx == 0 && posy == 0)
      return Integer.MAX_VALUE;
    return posx * this.board.getWidth() + posy;
  }

  /**
   * If MAXINT is passed in, this means that the coordinates are (0,0).  return 0 in that case.
   * @param nextLetterAddress
   * @return
   */
  public int calcYCoord(int nextLetterAddress) {
    if(nextLetterAddress == Integer.MAX_VALUE)
      return 0;
    return nextLetterAddress % this.board.getLength();
  }

  /**
   * If MAXINT is passed in, this means that the coordinates are (0,0).  return 0 in that case.
   * @param nextLetterAddress
   * @return
   */
  public int calcXCoord(int nextLetterAddress) {
    if(nextLetterAddress == Integer.MAX_VALUE)
      return 0;
    return nextLetterAddress / this.board.getWidth();
  }

  /**
   * This method prioritizes the dict by the word length. word length closest to
   * 8 wins.
   * 
   * @param dict
   *          - the dictionary to prioritize
   */
  private Queue<String> prioritizeByLength(List<String> dict) {
    Queue<String> priorityDict = new PriorityQueue<String>(dict.size(),
        new BoggleWordComparator());
    for (String entry : dict) {
      priorityDict.add(entry);
    }
    return priorityDict;

  }

  public List<String> getWordsPlaced() {
    return wordsPlaced;
  }

  public void setWordsPlaced(List<String> wordsPlaced) {
    this.wordsPlaced = wordsPlaced;
  }
}
