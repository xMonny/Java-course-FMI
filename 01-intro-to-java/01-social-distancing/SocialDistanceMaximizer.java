import java.util.Scanner;

public class SocialDistanceMaximizer {
    public static int maxDistance(int[] seats) {
        int distance = 0;
        int maximum = -1;
        int cntZeroes = 0;
        int startPoint = seats[0];

        for (int i = 0; i < seats.length; i++)
        {
            if (seats[i] == 1)
            {
                if (startPoint == 1)
                {
                    if (cntZeroes%2 != 0)
                    {
                        distance = distance/2 + 1;
                    }
                    else
                    {
                        distance = distance/2;
                    }
                }
                if (distance > maximum)
                {
                    maximum = distance;
                }
                distance = 0;
                startPoint = 1;
                cntZeroes = 0;
            }
            else
            {
                distance++;
                cntZeroes++;

                if (i == seats.length - 1)
                {
                    if (distance >= maximum)
                    {
                        maximum = distance;
                    }
                }
            }
        }
        return maximum;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int N = s.nextInt();
        int[] seats = new int[N];
        int number;
        for (int i = 0; i < N; i++) {
            number = s.nextInt();
            seats[i] = number;
        }
        int ans = maxDistance(seats);
        System.out.println(ans);
    }
}
