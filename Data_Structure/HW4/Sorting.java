import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/*
1.	입력은 숫자의 나열로 이루어지며, 첫 줄에 총 숫자의 갯수, 둘째줄부터 각각의 숫자가 입력됩니다.
    9              (총 숫자의 갯수)
    100
    7
    925
    -234
    10
    -9999
    12738
    239
    -2391          (여기까지 총 9개)

    입력이 끝나면 정렬된 배열이 출력됩니다.

2.	숫자의 나열 대신 난수를 입력할 수도 있습니다. 이 때는 첫 줄에 r (갯수) (최소값) (최대값) 이 오고 끝납니다.
    r 1000 -30000 30000        (-30000 에서 30000 까지(-30000과 30000도 포함) 1000개의 숫자를 랜덤하게 생성하고 그것을 입력으로 대신한다)

    입력이 끝나면 각 정렬의 수행시간이 출력됩니다.
 */

public class Sorting {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            boolean isRandom = false;
            int[] value;    // 입력 받을 숫자들의 배열
            String nums = br.readLine();    // 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r') {
                isRandom = true;    // 난수임을 표시

                String[] numsArg = nums.split(" ");

                int numSize = Integer.parseInt(numsArg[1]);    // 총 갯수
                int rMinimum = Integer.parseInt(numsArg[2]);    // 최소값
                int rMaximum = Integer.parseInt(numsArg[3]);    // 최대값

                Random rand = new Random();    // 난수 인스턴스를 생성한다.

                value = new int[numSize];    // 배열을 생성한다.
                for (int i = 0; i < value.length; i++)    // 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rMaximum - rMinimum + 1) + rMinimum;
            } else {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];    // 배열을 생성한다.
                for (int i = 0; i < value.length; i++)    // 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }

            while (true) {
                int[] newvalue = value.clone();    // 원래 값의 보호를 위해 복사본을 생성한다.

                String command = br.readLine();

                long t = System.currentTimeMillis();
                switch (command.charAt(0)) {
                    case 'M':    // Merge Sort
                        newvalue = doMergeSort(newvalue);
                        break;
                    case 'Q':    // Quick Sort
                        newvalue = doQuickSort(newvalue);
                        break;
                    case 'X':
                        return;
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                }
                if (isRandom) {
                    // 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                } else {
                    // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    for (int i = 0; i < newvalue.length; i++) {
                        System.out.println(newvalue[i]);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    private static int[] doMergeSort(int[] value) {
        return (value);
    }

    private static int[] doQuickSort(int[] value) {
        return (value);
    }

    private static void quickSort(int[] value, int i, int j) {
    }

}