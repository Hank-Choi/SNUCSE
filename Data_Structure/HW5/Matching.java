package src;

import java.io.*;

public class Matching {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input);
            } catch (IOException e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void command(String input) throws IOException {
        Database db = Database.getInstance();
        String operator = input.substring(0,2);
        String operand = input.substring(2);
        switch (operator) {
            case ("< "):
                db.readFile(operand);
                break;
            case ("@ "):
                try {
                    int index = Integer.parseInt(operand);
                    System.out.println(db.searchSlot(index));
                } catch (NumberFormatException e){
                    throw new IOException(input);
                }
                break;
            case ("? "):
                System.out.println(db.searchPattern(operand));
                break;
            default:
                throw new IOException(input);
        }
    }
}