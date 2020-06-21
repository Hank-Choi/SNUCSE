import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution3.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output3.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution3

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution3
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution3   // 0.5초 수행
       timeout 1 java Solution3     // 1초 수행
 */

class Solution3 {

    static int n;
    static String s;
    static int Answer;

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input3.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output3.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input3.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output3.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
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
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////
            Answer = palindrome();


            // output3.txt로 답안을 출력합니다.
            pw.println("#" + test_case + " " + Answer);
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


    private static int palindrome() {
        int[][] dArray = new int[5000][5000];
        for (int k = 0; k < n; k++)
            dArray[k][k] = 1;
        return palindromeSubstring(dArray, 0, n - 1);
    }

    private static int palindromeSubstring(int[][] dArray, int i1, int i2) {
        if (i1 <= i2) {
            if (dArray[i1][i2] == 0) {
                if (s.charAt(i1) == s.charAt(i2)) {
                    dArray[i1][i2] = palindromeSubstring(dArray, i1 + 1, i2 - 1) + 2;
                } else {
                    int temp1 = palindromeSubstring(dArray, i1 + 1, i2);
                    int temp2 = palindromeSubstring(dArray, i1, i2 - 1);
                    dArray[i1][i2] = (temp1 > temp2) ? temp1 : temp2;
                }
            }
            return dArray[i1][i2];
        } else
            return 0;
    }
    /*
    구현을 재귀적인 형태와 dynamic programming을 결합해 만든 방식으로 중복되는 호출이 있는 경우에는 이미 저장된 dArray에서 값을
    꺼내씀으로서 중복되는 연산을 최소화시켰다. 따라서 일반적인 DP방식과 palindromeSubstring이 호출되는 횟수가 같다고 볼 수 있다.
    크기가 n인 문제에서 palindromeSubstring이 호출되는 횟수는 최악의 경우에 크기가 k(1<=k<n)인 부분문제의 개수와 같으므로 n^2에 비례한다.
    각 palindromeSubstring함수는 상수시간만큼의 시간이 걸리기 때문에 크기가 n인 회문탐색문제의 시간복잡도는 O(n^2)이다.
    */
}
