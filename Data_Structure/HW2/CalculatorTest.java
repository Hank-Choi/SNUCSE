import java.io.*;
import java.util.Stack;
import java.util.StringTokenizer;

public class CalculatorTest {
    private Stack<Character> operatorStack;
    protected StringBuilder postFixExpression;
    private final char[] operators = {'(', ')', '^', '~', '*', '/', '%', '+', '-'};
    private final int[] operatorPriority = {-1, -1, 4, 3, 2, 2, 2, 1, 1};

    public CalculatorTest() {
        this.operatorStack = new Stack<>();
        this.postFixExpression = new StringBuilder();
    }


    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("q") == 0)
                    break;

                command(input);

            } catch (Exception e) {
                System.out.println("ERROR");
            }
        }
    }

    private static void command(String input) throws Exception {
        CalculatorTest postfix = infixToPostFix(input); //infix에서 postfix로 바꾸기
        String precal = postfix.postFixExpression.toString(); // StringBuilder class를 String으로 변환
        long result = calculateProcess(precal);
        System.out.print(precal);//postfix 수식 출력.
        System.out.print("\n");// println으로 출력시 오류 발생하여 수정
        System.out.println(result);
    }

    private static long calculateProcess(String precal) { // 계산을 위해 postfix를 스택으로 옮기며, 연산 수행
        Stack<Long> operandStack = new Stack<>();
        String[] postfix = precal.split(" ");
        for (String a : postfix) { // string 배열로 변환 후 for loop
            if (a.matches("^[0-9]*$")) { // 숫자일 경우 피연산자 스택에 push
                operandStack.push(Long.parseLong(a));
            } else if (a.charAt(0) == '~') { // unary -의 경우 스택 탑의 피연산자 부호 바꿔줌
                long tmp = operandStack.pop();
                operandStack.push(-tmp);
            } else { // 나머지 연산자의 경우 피연산자 2개 꺼낸후 연산 수행.
                long operand2 = operandStack.pop();
                long operand1 = operandStack.pop();
                long result = operation(operand1, operand2, a.charAt(0));
                operandStack.push(result);
            }
        }
        return operandStack.pop();
    }

    private static long operation(long num1, long num2, char op) { // 각각의 연산자에 따른 정수연산.
        long result = 0;
        switch (op) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = num1 / num2;
                break;
            case '%':
                result = num1 % num2;
                break;
            case '^':
                result = (long) Math.pow(num1, num2);
                break;
        }
        return result;
    }


    private static String unaryConversion(String input) throws Exception { // unary - 를 ~로 바꿔주는 메소드

        String mExpression = input.replaceAll(" ", "").replaceAll("\\t", ""); // 공백 제거
        if (mExpression.matches(".+[\\^*/%+]{2,}.+")) { // 연산자 2번 이상 반복되는 오류 throw
            throw new Exception();
        }
        String tmp = mExpression.replaceAll("-", "~"); // - 기호를 일단 ~로 변환
        char[] chs = tmp.toCharArray();
        int parenthesisCount = 0;
        int lengthTmp = tmp.length();
        for (int i = 0; i < lengthTmp; i++) { // 숫자 바로 뒤에 ~가 오는 경우 binary - 이므로 -로 바꿔주는 작업, 그리고 괄호 사이의 ~ 경우도 고려..
            if (chs[i] == 40) { //여는 괄호 카운트
                parenthesisCount++;
            } else if (chs[i] == 41) { //닫는 괄호 카운트
                parenthesisCount--;
            }

            if ((chs[i] >= 48 & chs[i] < 58) | chs[i] == 41) { // i번째가 숫자 또는 닫는 괄호인 경우
                if (i == lengthTmp - 1) { //마지막 인덱스의 경우 아래의 else if문이 오류나므로 그 전에 루프 탈출
                    break;
                } else if (chs[i + 1] == '~') { // 숫자 or 여는 괄호 다음에 ~가 오는 경우 binary 연산자 -로 변경.
                    chs[i + 1] = '-';
                }
            }
        }
        if (parenthesisCount != 0) { //괄호가 짝이 안맞으면 오류 발생
            throw new Exception();
        }
        return new String(chs);
    }

    private static CalculatorTest infixToPostFix(String input) throws Exception { // infix 수식을 postfix로 바꿔주는 과정
        if (input.matches(".+[0-9]\\s+[0-9].+")) {
            throw new Exception();
        }
        StringTokenizer mExpressionToken = new StringTokenizer(unaryConversion(input), "()^*/%+-~", true);
        CalculatorTest a = new CalculatorTest();
        while (mExpressionToken.hasMoreTokens()) {
            String tmp = mExpressionToken.nextToken();
            if (tmp.matches("^[0-9]*$")) { // 문자열을 잘랐는데 숫자인 경우
                a.postFixExpression.append(tmp);
                a.postFixExpression.append(" ");

            } else if (tmp.matches("[\\(\\)\\^*/%+-~]")) { // 토큰이 연산자인 경우 스택에 임시로 쌓아두고 ,
                a.opStackControl(tmp.charAt(0)); // 이 메소드는 연산자를 스택에 쌓으며 비교하여 postfix 수식에 담기

            } else {
                throw new Exception();
                //처리 불가능한 문자
                //오류 처리 연산자 및 피연산자(숫자) 이외의 문자가 들어온 경우
            }
        }
        if (!mExpressionToken.hasMoreTokens()) {// 최종 토큰을 옮긴 후 op스택에 남은 연산자 포스트 픽스로 이동(opStackControl에서 처리가 힘든 경우)
            while (!a.operatorStack.empty()) {
                a.postFixExpression.append(a.operatorStack.pop());
                a.postFixExpression.append(" ");
            }//남아있는 연산자를 모두 꺼내서 postfix수식으로 이동
        }
        // post fix로 변환 완료
        if (a.postFixExpression.charAt(a.postFixExpression.length() - 1) == ' ') { // postfix 수식 맨 마지막 공백 제거
            a.postFixExpression.deleteCharAt(a.postFixExpression.length() - 1);
        }
        return a;
    }

    private int priorityOfOp(char op) {// operator의 우선순위를 리턴하는 메소드

        int a = 0;
        for (int i = 0; i < operators.length; i++) {
            if (operators[i] == op) {
                a += operatorPriority[i];
            }
        }
        return a;
    }

    private void opStackControl(char op) throws Exception { // 연산자를 우선순위에 따라 옮기는 메소드

        if (this.operatorStack.empty() | op == '(') { // 스택이 비거나 여는 괄호의 경우 바로 스택에 push
            this.operatorStack.push(op);
        } else {
            char topOfStack = this.operatorStack.peek();
            if (priorityOfOp(topOfStack) < priorityOfOp(op)) { //스택 탑에 존재하는 연산자보다 op의 우선순위가 더 높다 그냥 push
                this.operatorStack.push(op);
            } else {// 스택 탑 보다 우선순위가 같거나 더 낮은 연산자가 들어온 경우. 안에 있는 우선순위 높은 연산자들 꺼내줘야한다.
                while (!this.operatorStack.empty() & priorityOfOp(op) <= priorityOfOp(topOfStack)) {
                    if (topOfStack == '(') { // 만약 top이 여는 괄호인데 여기 조건문까지 들어온 경우 op가 ')'인 경우이다.
                        this.operatorStack.pop(); //여는 괄호 없애주고
                        break; // 루프 빠져나가기
                    }
                    if (op == '^' | op == '~') { // right-associative 의 경우에는 우선 스택에 쌓기 위해 여기서 while루프 탈출.
                        break;
                    }
                    this.postFixExpression.append(this.operatorStack.pop()); //  스택 안에 있던 op보다 우선순위가 높거나 같은 연산자를 postfix 수식에 저장
                    this.postFixExpression.append(" ");

                    if (!this.operatorStack.empty()) { //하나 옮긴 뒤 스택 탑에 있는 연산자 peek하기
                        topOfStack = this.operatorStack.peek();
                    }
                }
                if (op != ')') { // operator 스택에서 우선순위 높거나 같은 것을 모두 옮긴 뒤에 이제서야 op를 스택에 push 해주는데 이때 )는 넣어주지 않음.
                    this.operatorStack.push(op);
                }
            }
        }
    }
}