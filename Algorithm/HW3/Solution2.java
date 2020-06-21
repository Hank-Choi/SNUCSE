import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution2 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
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
    static final int MAX_N = 20000;
    static final int MAX_E = 80000;

    static int N, E;
    static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
    static int Answer;

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
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다.
			   그리고 각 i번째 간선의 양 끝점의 번호를 U[i], V[i]에 읽어들이고, i번째 간선의 가중치를 W[i]에 읽어들입니다. (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤ N, 1 ≤ V[i] ≤ N)
			*/
            stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            E = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < E; i++) {
                U[i] = Integer.parseInt(stk.nextToken());
                V[i] = Integer.parseInt(stk.nextToken());
                W[i] = Integer.parseInt(stk.nextToken());
            }


            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////

            answer = kruskal();
            // output2.txt로 답안을 출력합니다.
            pw.println("#" + test_case + " " + answer);
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


    static int kruskal() {
        int[] parent = new int[N + 1];
        int[] edgeNode1 = new int[E + 1];
        int[] edgeNode2 = new int[E + 1];
        int[] edgeWeight = new int[E + 1];
        int edgeNum = E;
        int result = 0;
        for (int k = 0; k <= N; k++) {
            parent[k] = k;
        }
        for (int k = 0; k < E; k++) {
            edgeNode1[k] = U[k];
            edgeNode2[k] = V[k];
            edgeWeight[k] = W[k];
            percolateUp(edgeWeight, edgeNode1, edgeNode2, k);
        }
        while (edgeNum > 0) {
            int maxWeight = edgeWeight[0];
            int maxNode1 = edgeNode1[0];
            int maxNode2 = edgeNode2[0];
            edgeNum--;
            edgeWeight[0] = edgeWeight[edgeNum];
            edgeNode1[0] = edgeNode1[edgeNum];
            edgeNode2[0] = edgeNode2[edgeNum];
            percolateDown(edgeWeight, edgeNode1, edgeNode2, 0, edgeNum);
            int set1 = findSet(maxNode1, parent);
            int set2 = findSet(maxNode2, parent);
            if (set1 != set2) {
                parent[set2] = set1;
                result += maxWeight;
            }
        }
        return result;
    }
    //heap을 사용해 최장경로를 찾는 kruskal 알고리즘이다. heap을 만들 때 (2번째 for문) for문이 E번 돌고 힙을 만드는 percolateUp함수에
    //노드가 최대 E개 들어가므로 실행시간이 O(ElogE)이다.
    //최장경로를 더해나갈 때 edgeNum이 0이 될 때까지 while문이 E번 실행되고, 최장경로를 추출하고 재배열 할 때 percolateDown의 수행시간이
    //logE이므로 아래 while문의 실행시간도 마찬가지로 O(ElogE)이다. 결국 전체 알고리즘의 수행시간이 O(ElogE)이고
    //한 쌍에 두개 이상의 간선이 존재할 수 없으므로 E<N^2, 즉 전체 알고리즘의 수행시간은 O(ElogV)이다.

    private static int findSet(int i, int[] parent) {
        if (i == parent[i])
            return i;
        return parent[i] = findSet(parent[i], parent);
    }
    //path compression 방식을 사용해 findSet의 실행시간을 사실상 상수시간에 비례하도록 했다.


    private static void percolateDown(int[] value, int[] node1, int[] node2, int i, int length) {
        if (i <= length / 2) {
            int child1 = 2 * i;
            int child2 = 2 * i + 1;
            int child = (child2 < length && value[child2] > value[child1]) ? child2 : child1;
            if (value[i] < value[child]) {
                int tempValue = value[child];
                int tempNode1 = node1[child];
                int tempNode2 = node2[child];
                value[child] = value[i];
                node1[child] = node1[i];
                node2[child] = node2[i];
                value[i] = tempValue;
                node1[i] = tempNode1;
                node2[i] = tempNode2;
                percolateDown(value, node1, node2, child, length);
            }
        }
    }

    private static void percolateUp(int[] value, int[] node1, int[] node2, int i) {
        if (i > 0) {
            int parent = i / 2;
            if (value[i] > value[parent]) {
                int tempValue = value[parent];
                int tempNode1 = node1[parent];
                int tempNode2 = node2[parent];
                value[parent] = value[i];
                node1[parent] = node1[i];
                node2[parent] = node2[i];
                value[i] = tempValue;
                node1[i] = tempNode1;
                node2[i] = tempNode2;
                percolateUp(value, node1, node2, parent);
            }
        }
    }
}
