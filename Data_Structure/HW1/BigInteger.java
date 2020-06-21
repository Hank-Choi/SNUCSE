import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");
    
    private char[] bigIntegerNum;
    private boolean bigIntegerSign;
    //sign이 T이면 양수 F이면 음수
    
    public BigInteger(char[] num,boolean sign)
    {
    	bigIntegerNum =num;
    	bigIntegerSign =sign;
    }
  
    public static boolean signOf(String sign) {
    	if(sign.matches("-"))
    		return false;
    	else
    		return true;
    }
    //숫자 부호가 공백일 때도 +를 반환해준다.
    
    public static char[] str2char(String number) {
    	int k=0;
    	char[] charResult= new char[102];
       for(int i=number.length()-1;i>=0;i--) {
        	char ithChar =number.charAt(i);
        	charResult[k]= ithChar;
        	k++;
        }
       return charResult;
    }
    //스트링을 역순으로 배열에 저장한다.
    
    public char[] getNum() {
    	return bigIntegerNum;
    }
    
    public boolean getSign() {
    	return bigIntegerSign;
    }
  
    public BigInteger add(BigInteger big)
    {
    	char addCarry='0';
    	char[] addArray=new char[201];
    	char value;
    	for(int i = 0; getNum()[i]!=0 || big.getNum()[i]!=0 || addCarry!='0'; i++) {
    		char a= (getNum()[i]==0) ? 0 : (char)(getNum()[i]-'0');
    		char b= (big.getNum()[i]==0) ? 0 : (char)(big.getNum()[i]-'0');
    		//빈 공간일 때는 0을 넣어서 더할 때 영향이 없게 해준다.
    		value=(char) (addCarry+a+b);
    		if(value>57) {
    			value-=10;
    			addCarry='1';
    		}
    		else
    			addCarry='0';
    		addArray[i]=value;
    	}
    	BigInteger addValue=new BigInteger(addArray, getSign());
    	return addValue;
    }
  
    public BigInteger subtract(BigInteger big)
    {
    	BigInteger A=this;
    	BigInteger B=big;
    	//A,B라는 매개 BigInteger를 통해 계산
    	boolean resultSign=true;
    	for(int i=100;i>=0;i--) {
    		if(getNum()[i]>big.getNum()[i]) {
    			resultSign=(getSign()==true) ? true : false;
    			break;
    		}
    		else if(getNum()[i]<big.getNum()[i]) {
    			resultSign=(getSign()==true) ? false : true;
    			A=big;
    			B=this;
    			break;
    		}
    		else if(i==0) {
    			return new BigInteger(new char[] {'0'},true);
    		}
        	//num1의 절대값이 num2보다 클 때는 num1의 부호와 결과값의 부호가 같다.
        	//받은 두 수중 절대값이 큰 수가 A 절대값 작은 수가 B 같으면 0출력
    	}
    	char borrow='0';
    	char[] subtractArray=new char[100];
    	char value;
    	for(int i = 0; A.getNum()[i]!=0 || B.getNum()[i]!=0 || borrow!='0'; i++) {
    		char a= (A.getNum()[i]==0) ? 0 : (char)(A.getNum()[i]-'0');
    		char b= (B.getNum()[i]==0) ? 0 : (char)(B.getNum()[i]-'0');
    		//빈 공간일 때는 0을 넣어서 뺄 때 영향이 없게 해준다.
    		value=(char) (a-b+borrow);
    		if(value<48) {
    			value+=10;
    			borrow=(char)('0'-1);
    		}
    		else
    			borrow='0';
    		subtractArray[i]=value;
    	}
		return new BigInteger(subtractArray,resultSign);
    }
  
    public BigInteger multiply(BigInteger big)
    {
    	byte carry=0;
    	char units=0;
    	char[] multiplyChar=new char[201];
    	BigInteger totalValue=(getSign()==big.getSign() ? new BigInteger(new char[200],true) : new BigInteger(new char[200],false));
    	BigInteger eachValue;
    	//eachValue는 자릿수 하나에 대해 곱한 값이고 totalValue는 그것들을 모두 더한 값이다
    	for(int i = 0; getNum()[i]!=0; i++) {
    		for(int j = 0; big.getNum()[j]!=0 || carry!=0; j++) {
        		char b=(big.getNum()[j]==0) ? 0 : (char)(big.getNum()[j]-'0');
        		//빈 공간일 때는 0을 넣어서 곱할 때 영향이 없게 해준다.
    			units=(char)( ( (getNum()[i]-'0') * b +carry)%10 + '0');
    			carry=(byte)( ( (getNum()[i]-'0') * b +carry) /10);
    			multiplyChar[i+j]=units;
    		}
    		eachValue=new BigInteger(multiplyChar,true);
    		multiplyChar=new char[201];
    		totalValue=totalValue.add(eachValue);
    	}
    	return totalValue;
    }
  
    @Override
    public String toString()
    {
    	String resultString="";
    	if(!getSign()) {
    		resultString="-";
    	}
    	for (int i = bigIntegerNum.length-1; i>=0; i--) {
    		if(bigIntegerNum[i]!=0) {
    			if((resultString.isEmpty()||resultString.equals("-"))&& bigIntegerNum[i]=='0') {	}
    			else {
    				resultString += Character.toString(bigIntegerNum[i]);
    			}
    		}
    	}
    	//비었거나 "-" 일때 0이 들어오게 되면 안 됨 (empty||equal("-"))&&BigIntegerNum[i]==0
    	if(resultString.equals("-0")||resultString.equals("-")||resultString.isEmpty()) {
    		resultString="0";
    	}
    	//-0인 출력 예외처리
    	return resultString;
    }
  
    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
    	 boolean num1sign=true;
    	 boolean num2sign=true;
    	 char[] operator=new char[5];
    	 char[] arg1=new char[100];
    	 char[] arg2=new char[100];
    	 String patternStr = "([ ]*)(?<num1sign>[[+][-]]?)([ ]*)(?<num1>[0-9]+)([ ]*)(?<operator>[[+][-][*]])([ ]*)(?<num2sign>[[+][-]]?)([ ]*)(?<num2>[0-9]+)([ ]*)";
         //(0개 이상의 공백) (부호 0~1개) (0개 이상의 공백) (숫자) (0개 이상의 공백) (+/-/*) (0개 이상의 공백) (부호 0~1개) (0개 이상의 공백) (숫자) (0개 이상의 공백)
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()) {
        	num1sign= signOf(matcher.group("num1sign"));
        	num2sign= signOf(matcher.group("num2sign"));
        	operator=str2char(matcher.group("operator"));
        	arg1=str2char(matcher.group("num1"));
        	arg2=str2char(matcher.group("num2"));
        }
        BigInteger num1 = new BigInteger(arg1,num1sign);
        BigInteger num2 = new BigInteger(arg2,num2sign);
        //여기까지는 regex를 통해 두 입력의 부호와 숫자를 BigInteger로 저장
        //이 아래는 연산자에 따라 num1,num2계산
        BigInteger result;
        if(operator[0]=='*') {
        	result=num1.multiply(num2);
    		return result;
        }
        else if(operator[0]=='-') {
        	result=(num1sign==num2sign) ? num1.subtract(num2) : num1.add(num2);
        	return result;
        }
        else {
        	result=(num1sign==num2sign) ? num1.add(num2) : num1.subtract(num2);
    		return result;
        }
        //operator가 +나 -일 때 operator의 부호를 num2sign과 곱한 부호가 num1sign과 같으면 add 다르면 subtract
    }
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}