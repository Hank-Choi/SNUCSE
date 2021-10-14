package graph;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.PriorityQueue;


class GraphNode implements Comparable<GraphNode> {
    String key;
    String name;
    String line;
    ArrayList<GraphNode> transferList; //같은 이름의 역 중 가장 먼저 들어온 노드에만 리스트 생성
    ArrayList<GraphNode> shortestPath;
    ArrayList<GraphNode> adjacentNodes;
    ArrayList<Integer> adjacentNodesDistance;
    int shortestDistance;

    public GraphNode(String key, String name, String line) {
        this.key = key;
        this.name = name;
        this.line = line;
        this.shortestDistance = Integer.MAX_VALUE;
        this.adjacentNodes = new ArrayList<>();
        this.adjacentNodesDistance = new ArrayList<>();
    }

    public void setAdjacentNode(int distance, GraphNode Node) {
        this.adjacentNodes.add(Node);
        this.adjacentNodesDistance.add(distance);
    }

    public void relaxation(PriorityQueue<GraphNode> candidate) {
        ListIterator<Integer> distanceIterator = this.adjacentNodesDistance.listIterator();
        for (GraphNode nodes : adjacentNodes) {
            int distance = distanceIterator.next();
            if (nodes.shortestDistance > this.shortestDistance + distance) {
                if (nodes.shortestDistance != Integer.MAX_VALUE)
                    candidate.remove(nodes);
                nodes.shortestDistance = this.shortestDistance + distance;
                nodes.shortestPath = new ArrayList<>();
                nodes.shortestPath.addAll(this.shortestPath);
                nodes.shortestPath.add(nodes);
                candidate.add(nodes);
            }
        }
    }

    public void transferSet(GraphNode transferNode) {
        this.setAdjacentNode(5, transferNode);
        transferNode.setAdjacentNode(5, this);
        for (GraphNode originalNode : transferList) {
            originalNode.setAdjacentNode(5, transferNode);
            transferNode.setAdjacentNode(5, originalNode);
        }
        transferList.add(transferNode);
    } //새 노드와 이름이 같은 노드가 nameList 에 존재할 때 새 노드와 이름이 같은 노드들 간에 distance 를 5로 설정한다.

    public void doNotTransfer(int distance) { //경로를 찾을 때 출발지와 목적지의 환승시간을 0으로 설정, 경로를 찾으면 5로 복구시킨다.
        for (int i = 0; i < this.transferList.size(); ++i) {
            this.adjacentNodesDistance.set(i, distance);
            this.adjacentNodes.get(i).adjacentNodesDistance.set(0, distance);
        }
    }

    public void printPath() {
        String startingPoint = this.shortestPath.get(0).name;
        String destination = this.name;
        String buffer = startingPoint;
        String pathString = "";
        for (GraphNode wayPoint : shortestPath) {
            if (buffer.equals(wayPoint.name)) { //이름이 중복될 경우 환승역 처리 (출발역과 도착역 제외)
                if (!buffer.equals(startingPoint) && !buffer.equals(destination)) {
                    buffer = "[" + buffer + "]";
                }
            } else {
                pathString += buffer + " ";
                buffer = wayPoint.name;
            }
        }
        pathString += buffer;
        System.out.println(pathString);
        System.out.println(this.shortestDistance);
    }

    @Override
    public int compareTo(GraphNode graphNode) {
        return this.shortestDistance - graphNode.shortestDistance;
    }
}
