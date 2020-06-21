import graph.Graph;

import java.io.*;

public class Subway {
    public static void main(String args[]) {
        try {
            Graph SubwayGraph=ReadFile(args[0]);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input,SubwayGraph);
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    private static void command(String input,Graph SubwayGraph){
        String[] fromTo=input.split(" ");
        SubwayGraph.searchPath(fromTo[0],fromTo[1]);
    }

    private static Graph ReadFile(String FilePath) throws IOException{
        Graph newGraph=new Graph();
        FileReader fr=new FileReader(FilePath);
        BufferedReader br = new BufferedReader(fr);
        String lineString;
        while(!(lineString=br.readLine()).isEmpty()) {
            String[] splitLine=lineString.split(" ");
            newGraph.put(splitLine[0],splitLine[1],splitLine[2]);
        }
        while((lineString=br.readLine())!=null) {
            String[] splitLine=lineString.split(" ");
            newGraph.setDistance(splitLine[0],splitLine[1],Integer.parseInt(splitLine[2]));
        }
        return newGraph;
    }
}