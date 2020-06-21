import java.io.*;
import java.util.Stack;

public class CalculatorTest {
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

	public static long String2long(String stringnum) throws Exception {
		if (stringnum.isEmpty())
			throw new Exception();
		long result = 0;
		for (int i = 0; i < stringnum.length(); i++) {
			result *= 10;
			result += (long) (stringnum.charAt(i) - 48);
		}
		return result;
	}

	public static int priority(char operator) {
		if (operator == '^')
			return 4;
		else if (operator == '~')
			return 3;
		else if (operator == '*' || operator == '/' || operator == '%')
			return 2;
		else if (operator == '+' || operator == '-')
			return 1;
		else if (operator == '(')
			return 0;
		else
			return -1;
	}

	public static Stack<Object> postfix(String input) throws Exception {
		String numstr = "";
		Stack<Character> oper = new Stack<Character>();
		Stack<Object> result = new Stack<Object>();
		char lastoper = 1; //마지막으로 읽은 operator를 저장한다.
		boolean operatorExist = true; // 숫자 뒤에 공백이 오면 false, 그 후 숫자가 또 들어오면 error 호출
		for (int i = 0; i < input.length(); i++) {
			char ithChar = input.charAt(i);
			if (ithChar > 32) { // 공백 무시
				if (47 < ithChar && ithChar < 58) { // operand
					if (operatorExist == false) {
						throw new Exception();
					}
					numstr += ithChar;
				}
				else { // operator
					operatorExist = true;
					if (lastoper == ')' || !numstr.isEmpty()) { //연산자가 연속되지 않거나 이전 operator가 ')'일 때
						if (lastoper == ')') {
							if (ithChar == '(')
								throw new Exception();
						}
						else {	//operator가 나올 때 숫자 삽입
							long num = String2long(numstr);
							result.push(num);
							numstr = "";
						}
						
						if (ithChar == ')') {
							while (!oper.empty()) {
								if (priority(oper.peek()) != 0)
									result.push(oper.pop());
								else
									break;
							}
							if (oper.empty()) // '('가 존재하지 않으면 error 호출
								throw new Exception();
							else // '(' pop
								oper.pop();
						}
						else if (priority(ithChar) == 4)
							oper.push(ithChar);
						else if (priority(ithChar) == 2) {
							while (!oper.empty()) {
								if (priority(oper.peek()) >= 2)
									result.push(oper.pop());
								else 
									break;
							}
							oper.push(ithChar);
						}
						else if (priority(ithChar) == 1) {
							while (!oper.empty()) {
								if (priority(oper.peek()) >= 1)
									result.push(oper.pop());
								else
									break;
							}
							oper.push(ithChar);
						}
					}
					// unary '-' 와 '(' 는 연산자가 연속돼도 상관없다.
					else if (ithChar == '(')
						oper.push(ithChar);
					else if (ithChar == '-') {// unary '-'
						while (!oper.empty()) {
							if (priority(oper.peek()) >= 4)
								result.push(oper.pop());
							else 
								break;
						}
						oper.push('~');
					}
					else	// '('와 unary'-' 외에 operator가 연속되면 error call
						throw new Exception();
					lastoper = ithChar;
				}
			}
			else if (!numstr.isEmpty())
				operatorExist = false;
		}
		//input을 변환하는 for문 종료
		if (lastoper != ')') {
			long num = String2long(numstr);
			result.push(num);
		}
		while (!oper.empty()) {
			result.push(oper.pop());
		} //numstr과 oper에 남아 있는 숫자와 operator를 result에 삽입
		return result;
	}

	public static String stack2string(Stack<Object> stack) {
		String result = "";
		for (Object j : stack) {
			result += j.toString() + " ";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}

	public static long calculate(Stack<Object> postfixStack) throws Exception {
		Object stackTop = postfixStack.pop();
		if (stackTop.getClass() == Long.class) {
			return (long) stackTop;
		}
		else {
			long operand1 = calculate(postfixStack);
			if ((char) stackTop == '~')
				return operand1 * (-1);
			long operand2 = calculate(postfixStack);
			
			if ((char) stackTop == '/')
				return operand2 / operand1;
			else if ((char) stackTop == '^') {
				if (operand1 < 0 && operand2 == 0)
					throw new Exception();
				return (long) Math.pow(operand2, operand1);
			}
			else if ((char) stackTop == '-')
				return operand2 - operand1;
			else if ((char) stackTop == '*')
				return operand2 * operand1;
			else if ((char) stackTop == '+')
				return operand2 + operand1;
			else if ((char) stackTop == '%')
				return operand2 % operand1;
		}
		return 0;
	}

	private static void command(String input) throws Exception {
		Stack<Object> Postfix = postfix(input);
		String postfixStr = stack2string(Postfix);
		long result = calculate(Postfix);
		System.out.println(postfixStr);
		System.out.println(result);
	}
}