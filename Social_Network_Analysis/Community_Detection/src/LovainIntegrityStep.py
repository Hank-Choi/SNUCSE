import community as lvcm
import networkx as nx
import matplotlib.pyplot as plt
import time
from networkx.algorithms import community

if __name__ == '__main__':
    start = time.time()
    data_path: str = "../data/4.txt"
    # data_path: str = "../data/karate.gml"
    # G: nx.Graph = nx.read_gml(data_path, label="id")
    G: nx.Graph = nx.read_edgelist(data_path)

    """ Louvain method """
    # dendo = lvcm.generate_dendrogram(G)
    best:dict = lvcm.best_partition(G)
    a=set(best.values())
    # k = sorted(nx.connected_components(G), key=len, reverse=True)  # k 는 모두 연결되어있는 Community를 노드로 나타낸 값
    # m = community.modularity(G, communities=k)  # 추출된 커뮤니티의 modularity 계산
    # modularity_all=[m]
    # for level in range(len(dendo)):
    #     step_part = lvcm.partition_at_level(dendo, level)
    #     step_mod = lvcm.modularity(step_part, G)
    #     modularity_all.append(step_mod)
    #     print(level)

    """ Make Community Color list """

    print("time :", time.time() - start)
    fig2 = plt.figure(figsize=(6, 6))

    plt.subplots_adjust(hspace=0.5, wspace=0.3)
    plt.xlabel('step')
    plt.ylabel('modularity')
    plt.show(block=False)


    """ Plot Community """
    # fig = plt.figure()
    # pos = nx.spring_layout(G)
    # edges = G.edges()
    # Feature_color_sub = [0 for i in range(1008)]
    # node_size = 10
    # im = nx.draw_networkx_nodes(G, pos, node_color=Feature_color_sub, node_size=node_size)
    # # nx.draw_networkx_edges(G, pos)
    # # nx.draw_networkx_labels(G, pos, font_size=1, font_color="black")
    # plt.xticks([])
    # plt.yticks([])
    # plt.show(block=False)
