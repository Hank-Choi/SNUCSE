import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution1 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution1.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output1.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution1

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution1
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution1   // 0.5초 수행
       timeout 1 java Solution1     // 1초 수행
 */

class Solution1 {
    static final int MAX_N = 1000000;
    static int[] a = new int[MAX_N];
    static int[] copied = new int[MAX_N];
    static int n;
    static int answer1, answer2, answer3;
    static long start;
    static double time1, time2, time3;


    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input1.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output1.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input1.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output1.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int testCase = 1; testCase <= 10; testCase++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 배열의 원소의 개수를 n에 읽어들입니다.
			   그리고 각 원소를 A[0], A[1], ... , A[n-1]에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(stk.nextToken());
            }


            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
               원본으로 주어진 입력 배열이 변경되는 것을 방지하기 위해,
               여러분이 구현한 각각의 함수에 복사한 배열을 입력으로 넣도록 코드가 작성되었습니다.

			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
               함수를 구현하면 Answer1, Answer2, Answer3에 해당하는 주석을 제거하고 제출하세요.

               문제 1은 java 프로그래밍 연습을 위한 과제입니다.
			 */

            /* Problem 1-1 */
            System.arraycopy(a, 0, copied, 0, n);
            start = System.currentTimeMillis();
            answer1 = merge1(copied);
            time1 = (System.currentTimeMillis() - start) / 1000.;

            /* Problem 1-2 */
            System.arraycopy(a, 0, copied, 0, n);
            start = System.currentTimeMillis();
            answer2 = merge2(copied);
            time2 = (System.currentTimeMillis() - start) / 1000.;

            /* Problem 1-3 */
            System.arraycopy(a, 0, copied, 0, n);
            start = System.currentTimeMillis();
            answer3 = merge3(copied);
            time3 = (System.currentTimeMillis() - start) / 1000.;


            /*
            	여러분의 답안 Answer1, Answer2, Answer3을 비교하는 코드를 아래에 작성.
             	세 개 답안이 동일하다면 System.out.println("YES");
             	만일 어느하나라도 답안이 다르다면 System.out.println("NO");
            */
            if (answer1 == answer2 && answer2 == answer3)
                System.out.println("YES");
            else
                System.out.println("NO");
            /////////////////////////////////////////////////////////////////////////////////////////////


            // output1.txt로 답안을 출력합니다. Answer1, Answer2, Answer3 중 구현된 함수의 답안 출력
            pw.println("#" + testCase + " " + answer1 + " " + time1 + " " + time2 + " " + time3);
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

/////////////////////////////////////////////////////////////////

    private static int merge1(int[] array) {
        int[] arraySizeN = new int[n];
        System.arraycopy(array, 0, arraySizeN, 0, n);
        int[] mergedArray = mergeSort1(arraySizeN);
        int result = 0;
        for (int i = 0; i < mergedArray.length; i = i + 4) {
            result += mergedArray[i] % 7;
        }
        return result;
    }

    private static int[] mergeSort1(int[] value) {
        if (value.length == 1)
            return (value);
        int[] value1 = new int[value.length / 2];
        int[] value2 = new int[value.length - value.length / 2];
        System.arraycopy(value, 0, value1, 0, value.length / 2);
        System.arraycopy(value, value.length / 2, value2, 0, value.length - value.length / 2);
        value1 = mergeSort1(value1);
        value2 = mergeSort1(value2);
        value = merge1(value1, value2);
        return (value);
    }

    private static int[] merge1(int[] value1, int[] value2) {
        int[] mergedValue = new int[value1.length + value2.length];
        int i = 0, j = 0;
        while (i < value1.length && j < value2.length) {
            if (value1[i] < value2[j]) {
                mergedValue[i + j] = value1[i];
                ++i;
            } else {
                mergedValue[i + j] = value2[j];
                ++j;
            }
        }
        if (i < value1.length)
            System.arraycopy(value1, i, mergedValue, i + j, value1.length - i);
        if (j < value2.length)
            System.arraycopy(value2, j, mergedValue, i + j, value2.length - j);
        return mergedValue;
    }
    //수업시간에 배운 것처럼 일반적인 mergeSort는 T(n)=2T(n/2)+O(n) 을 만족시키므로 수행시간은 O(nlogn)이다.
    ///////////////////////////////////////////////////////////////////


    private static int merge2(int[] array) {
        int[] arraySizeN = new int[n];
        System.arraycopy(array, 0, arraySizeN, 0, n);
        int[] mergedArray = mergeSort2(arraySizeN);
        int result = 0;
        for (int i = 0; i < mergedArray.length; i = i + 4) {
            result += mergedArray[i] % 7;
        }
        return result;
    }

    private static int[] mergeSort2(int[] value) {
        if (value.length < 16)
            return mergeSort1(value);
        int lengthDevide = value.length / 16;
        int[][] values = new int[15][lengthDevide];
        int[] valueEnd = new int[lengthDevide + value.length % 16];
        for (int k = 0; k < 15; k++) {
            System.arraycopy(value, lengthDevide * k, values[k], 0, lengthDevide);
        }
        System.arraycopy(value, lengthDevide * 15, valueEnd, 0, lengthDevide + value.length % 16);
        for (int k = 0; k < 15; k++)
            values[k] = mergeSort2(values[k]);
        valueEnd = mergeSort2(valueEnd);
        value = merge2(values, valueEnd);
        return value;
    }

    private static int[] merge2(int[][] values, int[] valueEnd) {
        int length = values[0].length;
        int lengthEnd = valueEnd.length;
        int[] mergedValue = new int[length * 15 + lengthEnd];
        int[] pivots = new int[15];
        int pivotEnd = 0;
        int mergedPosition = 0;
        while (mergedPosition < mergedValue.length) {
            int smallest = Integer.MAX_VALUE;
            int smallestPosition = 0;
            for (int k = 0; k < 15; k++) {
                if (pivots[k] != length && values[k][pivots[k]] < smallest) {
                    smallest = values[k][pivots[k]];
                    smallestPosition = k;
                }
            }
            if (pivotEnd != lengthEnd && valueEnd[pivotEnd] < smallest) {
                mergedValue[mergedPosition] = valueEnd[pivotEnd];
                pivotEnd++;

            } else {
                mergedValue[mergedPosition] = smallest;
                pivots[smallestPosition]++;
            }
            mergedPosition++;
        }
        return mergedValue;
    }
    //16개로 나누는 mergeSort2의 수행시간은 T(n)=16T(n/16)+O(n)이므로 master 정리에 의해 mergeSort2의 수행시간은 O(nlogn)이다.
    //////////////////////////////////////////////////

    private static int merge3(int[] array) {
        int[] arraySizeN = new int[n];
        System.arraycopy(array, 0, arraySizeN, 0, n);
        int[] mergedArray = mergeSort3(arraySizeN);
        int result = 0;
        for (int i = 0; i < mergedArray.length; i = i + 4) {
            result += mergedArray[i] % 7;
        }
        return result;
    }

    private static int[] mergeSort3(int[] value) {
        if (value.length < 16)
            return mergeSort1(value);
        int lengthDevided = value.length / 16;
        int[][] values = new int[15][lengthDevided];
        int[] valueEnd = new int[lengthDevided + value.length % 16];
        for (int k = 0; k < 15; k++) {
            System.arraycopy(value, lengthDevided * k, values[k], 0, lengthDevided);
        }
        System.arraycopy(value, lengthDevided * 15, valueEnd, 0, lengthDevided + value.length % 16);
        for (int k = 0; k < 15; k++)
            values[k] = mergeSort3(values[k]);
        valueEnd = mergeSort3(valueEnd);
        value = merge3(values, valueEnd);
        return value;
    }

    private static int[] merge3(int[][] values, int[] valueEnd) {
        int length = values[0].length;
        int lengthEnd = valueEnd.length;
        int[] mergedValue = new int[length * 15 + lengthEnd];
        int[] pivots = new int[15];
        int[] minheap = new int[16];
        int[] position = new int[16];
        int pivotEnd = 1;
        int mergedPosition = 0;
        for (int k = 0; k < 15; k++) {
            minheap[k] = values[k][0];
            position[k] = k;
            pivots[k]++;
            percolateUp(minheap, position, k);
        }
        position[15] = 15;
        minheap[15] = valueEnd[0];
        percolateUp(minheap, position, 15);
        while (mergedPosition < mergedValue.length) {
            int smallest = minheap[0];
            int smallestPosition = position[0];
            if (smallestPosition < 15) {
                minheap[0] = (pivots[smallestPosition] < length) ? values[smallestPosition][pivots[smallestPosition]] : Integer.MAX_VALUE;
                pivots[smallestPosition]++;
            } else {
                minheap[0] = (pivotEnd != lengthEnd) ? valueEnd[pivotEnd] : Integer.MAX_VALUE;
                pivotEnd++;
            }
            percolateDown(minheap, position, 0, 16);
            mergedValue[mergedPosition] = smallest;
            mergedPosition++;
        }
        return mergedValue;
    }

    private static void percolateDown(int[] value, int[] position, int i, int length) {
        if (i <= length / 2 - 1) {
            int child1 = 2 * i + 1;
            int child2 = 2 * i + 2;
            int child = (child2 < length && value[child2] < value[child1]) ? child2 : child1;
            if (value[i] > value[child]) {
                int tempValue = value[child];
                int tempPosition = position[child];
                value[child] = value[i];
                position[child] = position[i];
                value[i] = tempValue;
                position[i] = tempPosition;
                percolateDown(value, position, child, length);
            }
        }
    }

    private static void percolateUp(int[] value, int[] position, int i) {
        if (i > 0) {
            int parent = (i - 1) / 2;
            if (value[i] < value[parent]) {
                int tempValue = value[parent];
                int tempPosition = position[parent];
                value[parent] = value[i];
                position[parent] = position[i];
                value[i] = tempValue;
                position[i] = tempPosition;
                percolateUp(value, position, parent);
            }
        }
    }
    //mergeSort3는 mergeSort2보다 merge시에 비교연산이 줄었지만 여전히 16T(n/16)이 수행시간을 결정하므로 수행시간이 O(nlogn)이다.
    ////////////////////////////////////////////////////
}
