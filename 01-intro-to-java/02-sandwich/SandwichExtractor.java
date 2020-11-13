import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

public class SandwichExtractor {
    public static String[] extractIngredients(String sandwich)
    {
        String[] help;
        String[] ans;
        int indexOne = sandwich.indexOf("bread");
        int indexTwo = sandwich.lastIndexOf("bread");
        if (indexOne == indexTwo)
        {
            return new String[0];
        }
        else
        {
            String sub = sandwich.substring(indexOne+5, indexTwo);
            if (sub.equals("") || sub.equals("-"))
            {
                return new String[0];
            }

            help = sub.split("-");
            int cnt = 0;
            for (int i = 0; i < help.length; i++)
            {
                if (help[i].equals("olives"))
                {
                    cnt++;
                }
            }

            ans = new String[help.length - cnt];

            int pos = 0;
            for (int i = 0; i < help.length; i++) {
                if (!help[i].equals("olives")) {
                    ans[pos] = help[i];
                    pos++;
                }
            }
            Arrays.sort(ans);
        }
        return ans;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String sandwich = s.nextLine();
        String[] ans = extractIngredients(sandwich);
        for (int i = 0; i < ans.length; i++)
        {
            System.out.println(ans[i]);
        }
    }
}
