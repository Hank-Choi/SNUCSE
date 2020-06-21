import community as lvcm
import networkx as nx
import matplotlib.pyplot as plt

if __name__ == '__main__':
    data_path: str = "../data/netscience.gml"
    G: nx.Graph = nx.read_gml(data_path, label="id")
    """ Louvain method """
    partition = lvcm.best_partition(graph=G, partition=None, resolution=1., randomize=True)
    dendo = lvcm.generate_dendrogram(G)
    for level in range(2):
        print("partition at level", level, "is", lvcm.partition_at_level(dendo, level))
    max_k_w = []
    for com in set(partition.values()):
        list_nodes = [nodes for nodes in partition.keys()
                      if partition[nodes] == com]
        max_k_w = max_k_w + [list_nodes]

    """ Make Community Color list """
    # community_num_group = len(max_k_w)
    # color_list_community = [0 for i in range(len(G.nodes()))]
    # for i in range(len(G.nodes())):
    #     for j in range(community_num_group):
    #         if i+1 in max_k_w[j]:
    #             color_list_community[i] = j

    pos = nx.spring_layout(G)
    fig2 = plt.figure(figsize=(6, 6))

    """ Plot Community """
    fig = plt.figure()
    edges = G.edges()
    Feature_color_sub = color_list_community
    node_size = 50
    im = nx.draw_networkx_nodes(G, pos, node_color=Feature_color_sub, node_size=node_size)
    nx.draw_networkx_edges(G, pos)
    nx.draw_networkx_labels(G, pos, font_size=10, font_color="black")
    plt.xticks([])
    plt.yticks([])
    plt.show(block=False)