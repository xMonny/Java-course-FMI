import java.util.Scanner;

public class Remembrall {
    public static boolean isPhoneNumberForgettable(String phoneNumber)
    {
        if (phoneNumber == null)
        {
            return false;
        }
        int[] arr = new int[10];
        int max = 0;
        for (int i = 0; i < phoneNumber.length(); i++)
        {
            if (phoneNumber.charAt(i) == '-' || phoneNumber.charAt(i) == ' ')
            {
                continue;
            }
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9')
            {
                return true;
            }
            int pos = phoneNumber.charAt(i) - '0';
            arr[pos]++;
            if (arr[pos] > max)
            {
                max = arr[pos];
            }
        }
        if (max == 1 && phoneNumber.length() != 1)
        {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String phoneNumber = s.nextLine();
        System.out.println(isPhoneNumberForgettable(phoneNumber));
    }
}
