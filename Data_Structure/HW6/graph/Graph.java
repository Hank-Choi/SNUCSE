package graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Graph {
    private HashMap<String, GraphNode> List; //key 값으로 역을 찾는 HashMap
    private HashMap<String, GraphNode> NameList; //역 이름으로 해당 node 를 찾는 HashMap

    public Graph() {
        List = new HashMap<>();
        NameList = new HashMap<>();
    }

    public void put(String key, String name, String line) {
        GraphNode NewNode = new GraphNode(key, name, line);
        if (this.NameList.containsKey(name)) { //이름이 같은 역이 있을 경우 nameList 에 있는 대표 노드에서 환승역 처리
            this.NameList.get(name).transferSet(NewNode);
        } else {
            NewNode.transferList = new ArrayList<>();
            this.NameList.put(name, NewNode);
        }
        this.List.put(key, NewNode);
    }

    public void setDistance(String key1, String key2, int distance) {
        GraphNode Station1 = this.List.get(key1);
        GraphNode Station2 = this.List.get(key2);
        Station1.setAdjacentNode(distance, Station2);
    }

    public void searchPath(String startingStation, String destination) {
        GraphNode StartNode = this.NameList.get(startingStation);
        GraphNode EndNode = this.NameList.get(destination);

        StartNode.doNotTransfer(0);
        EndNode.doNotTransfer(0);
        StartNode.shortestDistance = 0;
        //출발지와 목적지에서 환승시간 5분을 없애고 출발지 거리 0으로 초기화
        ArrayList<GraphNode> S = new ArrayList<>();
        S.add(StartNode);
        StartNode.shortestPath = S;
        PriorityQueue<GraphNode> candidate = new PriorityQueue<>();
        StartNode.relaxation(candidate);
        //최단 경로가 확정된 nodes(S)와 S에 연결된 nodes(candidate) 초기화
        while (!candidate.isEmpty())
            dijkstra(S, candidate);
        this.NameList.get(destination).printPath();

        StartNode.doNotTransfer(5);
        EndNode.doNotTransfer(5);
        this.resetDistance();
        //알고리즘이 실행되기 전 상태로 초기화
    }

    public void dijkstra(ArrayList<GraphNode> S, PriorityQueue<GraphNode> candidate) {
        GraphNode shortestNode = candidate.poll();
        shortestNode.relaxation(candidate);
        S.add(shortestNode);
    }

    public void resetDistance() {
        for (GraphNode i : List.values()) {
            i.shortestDistance = Integer.MAX_VALUE;
        }
    }
}