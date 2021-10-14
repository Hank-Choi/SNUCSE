import matplotlib.pyplot as plt
import networkx as nx
from networkx.algorithms import community

# modularity, conductance, NMI

def girvan_newman_algorithm(G, weight):
    """ G는 원래 네트워크 g는 Edge를 한개씩 끊어나갈 네트워크 """
    g = G.copy()


    """ initial """
    step = 0  # step
    log_step = []  # step 기록
    log_modularity = []  # modularity 기록
    old_max_m = 0  # 이전 최대 modularity 기억
    k = sorted(nx.connected_components(G), key=len, reverse=True)  # k 는 모두 연결되어있는 Community를 노드로 나타낸 값
    m = community.modularity(G, communities=k, weight=weight)  # modularity
    max_step = 0  # max_step은 modularity가 최대일 때 step값 기록용

    """ Girvan-Newman algorithm """
    while len(g.edges()) > 0:
        k = sorted(nx.connected_components(g), key=len, reverse=True)  # 커뮤니티 추출
        m = community.modularity(G, communities=k, weight=weight)  # 추출된 커뮤니티의 modularity 계산
        if m > old_max_m:  # 이전 최대 modularity보다 현재 modularity가 높을 경우 기록
            max_step = step
            old_max_m = m
        log_step = log_step + [step]  # 로깅용
        log_modularity = log_modularity + [m]  # 로깅용
        print("step: ", step, "  modularity: ", m)

        """ remove edge """
        step = step + 1
        betweenness = nx.edge_betweenness_centrality(g, weight=weight)  # betweennes centrality 계산
        max_edge = max(betweenness, key=betweenness.get)  # betweeness centrality가 가장 큰 Edge 선택
        g.remove_edge(max_edge[0], max_edge[1])  # Edge 추출

    return log_step, log_modularity, max_step


if __name__ == '__main__':
    """ 본문 """

    """ Network 생성 """
    G: nx.Graph = nx.read_edgelist("../data/4.txt")

    """ Girvan Newman algorithm"""
    log_step, log_modularity, max_step = girvan_newman_algorithm(G,
                                                                                      weight=None)  # weight='weight' : weighted network
    # Algorithm 실행결과 플롯
    fig = plt.figure()
    plt.subplots_adjust(hspace=0.5, wspace=0.3)
    plt.plot(log_step, log_modularity)
    plt.xlabel('step')
    plt.ylabel('modularity')
    plt.title("unweighted")
    plt.show(block=False)

