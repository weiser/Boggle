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

    List<Integer> visitedSpaces = new ArrayList<Integer>(25);

    for (int x = 0; x < this.board.getWidth() && posx < 0 && posy < 0; x++) {
      for (int y = 0; y < this.board.getLength() && posx < 0 && posy < 0; y++) {
        if (this.board.getLetterAt(x, y) != null
            && this.board.getLetterAt(x, y).equals(word.charAt(0))) {
          posx = x;
          posy = y;
        }
      }
    }

    if (posx < 0 && posy < 0) {
      Random rand = new Random();
      posx = rand.nextInt(this.board.getWidth());
      posy = rand.nextInt(this.board.getLength());

    }

    visitedSpaces.add(calcIntegerAddress(posx, posy));

    this.board
        .setPosition(posx, posy, String.valueOf(word.charAt(letterIndex)));
    letterIndex++;
    int nextLetterAddress;

    // if the next letter of the word is next to where we're starting at, follow
    // the next letter (as long as we haven't gone past the end of the word)
    while (letterIndex < word.length()
        && (nextLetterAddress = addressOfLetterInWord(String.valueOf(word
            .charAt(letterIndex)), posx, posy)) >= 0) {
      visitedSpaces.add(nextLetterAddress);
      posx = calcXCoord(nextLetterAddress);
      posy = calcYCoord(nextLetterAddress);
      letterIndex++;
    }

    // need to put the word in empty spaces
    while (letterIndex < word.length()
        && (nextLetterAddress = addressOfNeighborEmptySpace(posx, posy)) >= 0) {
      visitedSpaces.add(nextLetterAddress);
      posx = calcXCoord(nextLetterAddress);
      posy = calcYCoord(nextLetterAddress);
      this.board.setPosition(posx, posy, String.valueOf(word
          .charAt(letterIndex)));
      letterIndex++;
    }

    // at this point, we failed to place the word on the board. remove the
    // partial word from the board and return failure
    if (letterIndex < word.length()) {
      for (int integerAddress : visitedSpaces) {
        posx = calcXCoord(integerAddress);
        posy = calcYCoord(integerAddress);
        this.board.setPosition(posx, posy, null);
      }
      return false;
    }
    return true;
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
      return allEmptyNeighborSpaces.get(rand.nextInt(allEmptyNeighborSpaces
          .size()));
    }

  }

  /**
   * given (posx,posy) coordinates, return the calcIntegerAddress of the
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
    for (int x : neighborOffsets) {
      for (int y : neighborOffsets) {
        // ensure we're in bounds
        if (x + posx < this.board.getWidth() && x + posx >= 0
            && y + posy < this.board.getLength() && y + posy >= 0) {
          if (this.board.getLetterAt(posx + x, posy + y) != null
              && this.board.getLetterAt(x + posx, y + posy).equals(letter))
            return calcIntegerAddress(posx + x, posy + y);
        }
      }
    }
    return -1;
  }

  public int calcIntegerAddress(int posx, int posy) {
    return posx * this.board.getWidth() + posy;
  }

  public int calcYCoord(int nextLetterAddress) {
    return nextLetterAddress % this.board.getLength();
  }

  public int calcXCoord(int nextLetterAddress) {
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
