import matplotlib.pyplot as plt
import networkx as nx
from networkx.algorithms import community
import time


# modularity, conductance, NMI

def girvan_newman_algorithm(G, weight):
    """ G는 원래 네트워크 g는 Edge를 한개씩 끊어나갈 네트워크 """
    a = nx.nodes(G)
    g = nx.Graph()
    g.add_nodes_from(a)

    """ initial """
    step = 0  # step
    log_step = []  # step 기록
    log_modularity = []  # modularity 기록
    max_g = g.copy()  # modularity가 최대일 때의 네트워크 여기서는 초기화 작업
    k = sorted(nx.connected_components(g), key=len, reverse=True)  # k 는 모두 연결되어있는 Community를 노드로 나타낸 값
    k_list = []
    for j in range(len(k)):
        k_list = k_list + [list(k[j])]
    max_k = k_list  # max_k 는 modularity가 최대일 때의 k 값 저장용
    m = community.modularity(G, communities=k, weight=weight)  # modularity
    max_m = m  # max_m은 modularity가 최대일 때 값 기록용
    max_step = 0  # max_step은 modularity가 최대일 때 step값 기록용

    new_g = G.copy()

    """ Girvan-Newman algorithm """

    while len(new_g.edges()) > 0:
        k = sorted(nx.connected_components(g), key=len, reverse=True)  # 커뮤니티 추출
        m = community.modularity(G, communities=k, weight=weight)  # modularity
        modularities = []
        for i in new_g.edges():
            temp_g = g.copy()
            temp_g.add_edge(i[0], i[1])
            k2 = sorted(nx.connected_components(temp_g), key=len, reverse=True)  # 커뮤니티 추출
            modularities.append(community.modularity(temp_g, communities=k2, weight=weight))
        mam = modularities[0]
        m_index = 0
        for index in range(len(modularities)):
            if modularities[index] > mam:
                mam = modularities[index]
                m_index = index
        edges = list(new_g.edges())
        edge_should_added = edges[m_index]
        new_g.remove_edge(edge_should_added[0], edge_should_added[1])
        g.add_edge(edge_should_added[0], edge_should_added[1])
        step += 1

        log_step = log_step + [step]  # 로깅용
        log_modularity = log_modularity + [m]  # 로깅용
        print("step: ", step, "  modularity: ", m)

    return log_step, log_modularity, max_g, max_m, max_k, max_step


if __name__ == '__main__':
    start = time.time()
    data_path: str = "../data/email-Eu-core.txt"
    # G: nx.Graph = nx.read_gml(data_path, label="id")
    G: nx.Graph = nx.read_edgelist(data_path)

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

    """ Graph G Plot """
    # pos = nx.spring_layout(G)
    # fig2 = plt.figure(figsize=(6, 6))
    # node_size = 100
    # node_color_list = []
    # """max_k, 즉 Modularity 가 가장 높은 지점의 Community k 별로 색깔 설정"""
    # for i in range(len(G.nodes) + 1):
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
    #     elif i in max_k[5]:
    #         node_color_list = node_color_list + ['purple']
    # im = nx.draw_networkx_nodes(G, pos, node_color=node_color_list, node_size=node_size)
    # nx.draw_networkx_edges(G, pos)
    # nx.draw_networkx_labels(G, pos, font_size=14, font_color="black")
    # plt.xticks([])
    # plt.yticks([])
    # plt.xlim(-1.1, 1.1)
    # plt.ylim(-1.1, 1.1)
    # plt.show(block=False)
    # plt.savefig("karate.png", dpi=300)
