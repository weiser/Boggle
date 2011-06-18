package weiser.david;

import java.util.Comparator;

public class BoggleWordComparator implements Comparator<String> {

  public int compare(String arg0, String arg1) {
    // TODO Auto-generated method stub
    int diff0 = 8-arg0.length();
    int diff1 = 8-arg1.length();
    //so that super long words and super short words get the same treatment
    diff1 = Math.abs(diff1);
    diff0 = Math.abs(diff0);
    
    //prefer shorter words
    return diff0 - diff1;
  }
  

}
