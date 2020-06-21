import matplotlib.pyplot as plt
import networkx as nx
from networkx.algorithms import community
import time

# modularity, conductance, NMI

def girvan_newman_algorithm(G, weight):
    """ G는 원래 네트워크 g는 Edge를 한개씩 끊어나갈 네트워크 """
    g = G.copy()

    """ initial """
    step = 0  # step
    log_step = []  # step 기록
    log_modularity = []  # modularity 기록
    old_max_m = 0  # 이전 최대 modularity 기억
    # max_g = g.copy()  # modularity가 최대일 때의 네트워크 여기서는 초기화 작업
    # k = sorted(nx.connected_components(G), key=len, reverse=True)  # k 는 모두 연결되어있는 Community를 노드로 나타낸 값
    # k_list = []
    # for j in range(len(k)):
    #     k_list = k_list + [list(k[j])]
    # max_k = k_list  # max_k 는 modularity가 최대일 때의 k 값 저장용
    # m = community.modularity(G, communities=k, weight=weight)  # modularity




    # max_m = m  # max_m은 modularity가 최대일 때 값 기록용
    # max_step = 0  # max_step은 modularity가 최대일 때 step값 기록용

    """ Girvan-Newman algorithm """
    while len(g.edges()) > 0:
        k = sorted(nx.connected_components(g), key=len, reverse=True)  # 커뮤니티 추출
        # m = community.modularity(G, communities=k, weight=weight)  # 추출된 커뮤니티의 modularity 계산

        # if m > old_max_m:  # 이전 최대 modularity보다 현재 modularity가 높을 경우 기록
        #     max_g = g.copy()
        #     max_m = m
        #     k_list = []
        #     for j in range(len(k)):
        #         k_list = k_list + [list(k[j])]
        #     max_k = k_list
        #     max_step = step
        #     old_max_m = m
        # log_step = log_step + [step]  # 로깅용
        # log_modularity = log_modularity + [m]  # 로깅용
        max_g=0
        max_m=0
        max_k=0
        max_step=0
        print("step: ", step, "  modularity: " )

        """ remove edge """
        step = step + 1
        betweenness = nx.edge_betweenness_centrality(g, weight=weight)  # betweennes centrality 계산
        max_edge = max(betweenness, key=betweenness.get)  # betweeness centrality가 가장 큰 Edge 선택
        g.remove_edge(max_edge[0], max_edge[1])  # Edge 추출

    return log_step, log_modularity, max_g, max_m, max_k, max_step


if __name__ == '__main__':
    """ 본문 """

    start = time.time()
    """ Network 생성 """
    # data_path: str = "../data/email-Eu-core.txt"
    data_path: str = "../data/karate.gml"
    G: nx.Graph = nx.read_gml(data_path, label="id")
    # G: nx.Graph = nx.read_edgelist(data_path)

    """ Girvan Newman algorithm"""
    log_step, log_modularity, max_g, max_m, max_k, max_step = girvan_newman_algorithm(G,
                                                                                      weight=None)  # weight='weight' : weighted network
    print("time :", time.time() - start)
    # Algorithm 실행결과 플롯
    fig = plt.figure()
    plt.subplots_adjust(hspace=0.5, wspace=0.3)
    plt.plot(log_step, log_modularity)
    plt.xlabel('step')
    plt.ylabel('modularity')
    plt.show(block=False)


    plt.figure()
    # """ Graph G Plot """
    # pos = nx.spring_layout(G)
    # fig2 = plt.figure(figsize=(6, 6))
    # node_size = 100
    # node_color_list = []
    # """max_k, 즉 Modularity 가 가장 높은 지점의 Community k 별로 색깔 설정"""
    # for i in range(len(G.nodes)+1):
    #     if i in max_k[0]:
    #         node_color_list = node_color_list + ['red']
    #     elif i in max_k[1]:
    #         node_color_list = node_color_list + ['yellow']
    #     elif i in max_k[2]:
    #         node_color_list = node_color_list + ['blue']
    #     elif i in max_k[3]:
    #         node_color_list = node_color_list + ['orange']
    #     elif i in max_k[4]:
    #         node_color_list = node_color_list + ['green']
    # im = nx.draw_networkx_nodes(G, pos, node_color=node_color_list, node_size=node_size)
    # nx.draw_networkx_edges(G, pos)
    # nx.draw_networkx_labels(G, pos, font_size=10, font_color="black")
    # plt.xticks([])
    # plt.yticks([])
    # plt.xlim(-1.1, 1.1)
    # plt.ylim(-1.1, 1.1)
    # plt.show(block=False)
    # plt.savefig("karate.png",dpi=300)
