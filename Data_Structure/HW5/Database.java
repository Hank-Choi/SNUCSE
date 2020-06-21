package src;

import java.io.*;

public class Database {
    private static final Database db = new Database();
    private MyHashtable<Pattern> patternTable;

    private Database() {
    }

    public static Database getInstance(){
        return db;
    }

    public void readFile(String FilePath) throws IOException {
        patternTable = new MyHashtable<>();
        FileReader fr = new FileReader(FilePath);
        BufferedReader br = new BufferedReader(fr);
        String lineString;
        int lineIndex = 0;
        while ((lineString = br.readLine()) != null) {
            lineIndex++;
            readLine(lineString, lineIndex);
        }
    }

    public void readLine(String line, int lineIndex) {
        final int length = line.length();
        for (int start = 0; start < length - 5; ++start) {
            Pattern pattern = new Pattern(line.substring(start, start + 6), lineIndex, start + 1);
            insertPattern(pattern);
        }
    }

    public String searchSlot(int index) {
        AVL<Pattern> indexSlot = patternTable.treeAt(index);
        if (indexSlot==null){
            return "EMPTY";
        }
        else
        return indexSlot.getRoot().toString();
    }

    public String searchPattern(String operand) { //첫 패턴을 foundList 에 저장 후 다음 패턴들이 연속되면 유지, 연속되지 않으면 삭제. 남은 foundList 출력.
        MyLinkedList<Pattern> foundList = new MyLinkedList<>(); // 1 ~ n-1번째 패턴들이 연속되는 위치 list
        int n = 0; //입력한 String에 나오는 길이6 패턴의 개수
        for (int i = 0; i < operand.length() - 5; ++i) {
            if (i + 6 == operand.length() || i == 6 * n) {
                n++;
                Pattern pattern = new Pattern(operand.substring(i, i + 6));
                if (patternTable.search(pattern, hashcode(pattern)) != null) {
                    MyLinkedList<Pattern> newList = patternTable.search(pattern, hashcode(pattern)).getList(); //n번째 길이6 패턴의 위치
                    if (i == 0)
                        foundList = newList.copy();
                    else { // 1 ~ n-1번째까지의 패턴들의 위치와 n번째 패턴이 연속되는지 확인 (속도 향상을 위해 여러 경우로 나누어 구현)
                        MyLinkedList.MyLinkedListIterator<Pattern> foundListIterator = foundList.getIterator();
                        while (foundListIterator.getCurr() != null) {
                            int foundPatternLine = foundListIterator.getCurr().getItem().getLine();
                            int foundPatternRow = foundListIterator.getCurr().getItem().getRow();
                            MyLinkedList.MyLinkedListIterator<Pattern> newListIterator = newList.getIterator();
                            while (newListIterator.getCurr() != null) {
                                int newPatternLine = newListIterator.getCurr().getItem().getLine();
                                int newPatternRow = newListIterator.getCurr().getItem().getRow();
                                //n-1번째까지 연속된 패턴에 연속되는 n번째 패턴이 있는지 탐색, 없으면 삭제, 있으면 유지
                                if (newPatternLine < foundPatternLine)
                                    newListIterator.next();
                                else if (newPatternLine == foundPatternLine) {
                                    if (newPatternRow < foundPatternRow + i)
                                        newListIterator.next();
                                    else if (newPatternRow == foundPatternRow + i)
                                        break;
                                    else {
                                        foundListIterator.remove();
                                        break;
                                    }
                                } else {
                                    foundListIterator.remove();
                                    break;
                                }
                            }
                            if (newListIterator.getCurr() == null) {
                                foundListIterator.remove();
                            }
                            foundListIterator.next();
                        }
                    }
                } else {
                    foundList = new MyLinkedList<>();
                    break;
                }
            }
        }
        if (foundList.size() == 0)
            return "(0, 0)";
        else {
            MyLinkedList.LinkedListNode<Pattern> firstNode = foundList.first();
            String out = "";
            for (int j = 0; j < foundList.size() - 1; ++j) {
                out += (firstNode.getItem().getIndex() + " ");
                firstNode = firstNode.getNext();
            }
            out += firstNode.getItem().getIndex();
            return out;
        }
    }

    public void insertPattern(Pattern pattern) {
        patternTable.insert(pattern, hashcode(pattern));
    }

    public int hashcode(Pattern pattern) {
        String patternString = pattern.toString();
        int result = 0;
        for (int i = 0; i < patternString.length(); ++i)
            result += patternString.charAt(i);
        result = result % 100;
        return result;
    }
}
