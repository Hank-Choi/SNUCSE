import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution2.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output2.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution2

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution2
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution2   // 0.5초 수행
       timeout 1 java Solution2     // 1초 수행
 */

class Solution2 {

    /*
        ** 주의사항
        정답의 숫자가 매우 크기 때문에 답안은 반드시 int 대신 long 타입을 사용합니다.
        그렇지 않으면 overflow가 발생해서 0점 처리가 됩니다.
        Answer[0]을 a의 개수, Answer[1]을 b의 개수, Answer[2]를 c의 개수라고 가정했습니다.
    */
    static int n;                           // 문자열 길이
    static String s;                        // 문자열
    static long[] answer = new long[3];     // 정답

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input2.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output2.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input2.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output2.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int testCase = 1; testCase <= 10; testCase++) {
			/*
			   각 테스트 케이스를 파일에서 읽어옵니다.
               첫 번째 행에 쓰여진 문자열의 길이를 n에 읽어들입니다.
               그 다음 행에 쓰여진 문자열을 s에 한번에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken());
            s = br.readLine();

            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   정답을 구하기 위해 주어진 문자열 s를 여러분이 원하시는 대로 가공하셔도 좋습니다.
			   문제의 답을 계산하여 그 값을 Answer(long 타입!!)에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////
            answer = diversity();


            // output2.txt로 답안을 출력합니다.
            pw.println("#" + testCase + " " + answer[0] + " " + answer[1] + " " + answer[2]);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
            pw.flush();
        }

        br.close();
        pw.close();
    }

    private static long[] diversity() {
        long[][][] dArray = new long[3][30][30];
        long[] result = new long[3];
        for (long[][] row2 : dArray) {
            for (long[] row : row2) {
                Arrays.fill(row, -1);
            }
        }
        //상수시간
        for (int k = 0; k < n; k++) {
            if (s.charAt(k) == 'a')
                dArray[0][k][k] = 1;
            else if (s.charAt(k) == 'b')
                dArray[1][k][k] = 1;
            else
                dArray[2][k][k] = 1;
        }
        //O(n)
        result[0] = calculateNum(dArray, 0, 0, n - 1);
        result[1] = calculateNum(dArray, 1, 0, n - 1);
        result[2] = calculateNum(dArray, 2, 0, n - 1);
        return result;
    }

    private static long calculateNum(long[][][] dArray, int abc, int i, int j) {
        if (i <= j) {
            if (dArray[abc][i][j] == -1) {
                dArray[abc][i][j] = 0;
                if (abc == 0) {
                    for (int k = i; k < j; k++) {
                        dArray[0][i][j] += calculateNum(dArray, 0, i, k) * calculateNum(dArray, 2, k + 1, j)
                                + calculateNum(dArray, 1, i, k) * calculateNum(dArray, 2, k + 1, j)
                                + calculateNum(dArray, 2, i, k) * calculateNum(dArray, 0, k + 1, j);
                    }
                } else if (abc == 1) {
                    for (int k = i; k < j; k++) {
                        dArray[1][i][j] += calculateNum(dArray, 0, i, k) * calculateNum(dArray, 0, k + 1, j)
                                + calculateNum(dArray, 0, i, k) * calculateNum(dArray, 1, k + 1, j)
                                + calculateNum(dArray, 1, i, k) * calculateNum(dArray, 1, k + 1, j);
                    }
                } else {
                    for (int k = i; k < j; k++) {
                        dArray[2][i][j] += calculateNum(dArray, 1, i, k) * calculateNum(dArray, 0, k + 1, j)
                                + calculateNum(dArray, 2, i, k) * calculateNum(dArray, 1, k + 1, j)
                                + calculateNum(dArray, 2, i, k) * calculateNum(dArray, 2, k + 1, j);
                    }
                }
            }
            return dArray[abc][i][j];
        } else
            return 0;
    }
    /*구현을 재귀적인 형태와 dynamic programming을 결합해 만든 방식으로 중복되는 호출이 있는 경우에는 이미 저장된 dArray에서 값을
    꺼내씀으로서 중복되는 연산을 최소화시켰다. 따라서 일반적인 DP방식과 calculateNum이 호출되는 횟수가 같다고 볼 수 있다.
    문제의 크기가 n일 때 문제의 크기가 k(1<k<n)인 부분 문제의 개수는 n(n+1)/2 이고 각 문제에서 k가 i부터 j까지 호출된다. 각 부분 문제에서
    i~j까지의 길이가 평균적으로 n에 비례하므로 크기가 n인 문제를 풀 때의 수행시간은 big-Theta(n^3)이다.
    */
}

