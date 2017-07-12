import java.util.LinkedList;
import java.util.List;

/**
 * Created by jz on 2017/7/10.
 */
public class MyTest {

    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<String>();
        System.out.println(list.peek().length());
    }
}

class LetterCombinations {
    private List<String> phoneNumberLetter;

    public List<String> result(String digits) {
        LinkedList<String> ans = new LinkedList<String>();
        String[] mapping = new String[] {"0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        ans.add("");
        for(int i =0; i<digits.length();i++){
            int x = Character.getNumericValue(digits.charAt(i));
            while(ans.peek().length()==i){
                String t = ans.remove();
                for(char s : mapping[x].toCharArray())
                    ans.add(t+s);
            }
        }
        return ans;
    }
}