import copy

import community as lvcm
import networkx as nx
import matplotlib.pyplot as plt


def CAL_dQ(communities, edges, node_s, community_s):
    sigma_in = sum(
        [edge[2] for edge in edges if (edge[0] in communities[community_s]) & (edge[1] in communities[community_s])])
    sigma_total = sum(
        [edge[2] for edge in edges if (edge[0] in communities[community_s]) | (edge[1] in communities[community_s])])
    k_i = sum([edge[2] for edge in edges if (edge[0] == node_s) | (edge[1] == node_s)])
    k_i_in = sum([edge[2] for edge in edges
                  if ((edge[0] == node_s) & (edge[1] in communities[community_s])) |
                  ((edge[1] == node_s) & (edge[0] in communities[community_s]))])
    m = sum([edge[2] for edge in edges])
    dQ = ((sigma_in + k_i_in) / 2 / m - ((sigma_total + k_i) / 2 / m) ** 2) - (
                (sigma_in / 2 / m) - ((sigma_total / 2 / m) ** 2) - ((k_i / 2 / m) ** 2))
    return dQ


def Phase_First(communities_p1, nodes_p1, edges_p1):
    """ Phase 1 """
    flag_changed = 1
    cnt = 0
    log_community = [copy.deepcopy(communities_p1)]
    while flag_changed == 1:
        cnt += 1
        flag_changed = 0
        for node_s in nodes_p1:
            """ node_s를 community로부터 분리 """
            communities_ = copy.deepcopy(communities_p1)
            tmp_x = [comm for comm in communities_ if node_s not in comm]
            tmp_o = [comm for comm in communities_ if node_s in comm]
            if len(tmp_o[0]) != 1:
                tmp_o[0].remove(node_s)
                communities_cal = tmp_x + tmp_o + [[node_s]]
            else:
                communities_cal = communities_

            """ dQ 계산 """
            max_dQ = 0
            community_d = 0
            for community_s in range(len(communities_cal)):
                if communities_cal[community_s] != [node_s]:
                    dQ = CAL_dQ(communities_cal, edges_p1, node_s, community_s)
                    if dQ > max_dQ:
                        community_d = community_s
                        max_dQ = dQ

            """ max_dQ에 node_s를 편입 """
            if max_dQ > 0:
                communities_cal[community_d] += [node_s]
                communities_cal[community_d].sort()
                communities_cal.remove([node_s])
                communities_cal.sort()
                if communities_p1 != communities_cal:
                    communities_p1 = copy.deepcopy(communities_cal)
                    community_2 = {i + 1: 0 for i in range(len(G.nodes))}
                    for j in range(len(communities_p1)):
                        com_temp = communities_p1[j]
                        for k in range(len(com_temp)):
                            community_2[com_temp[k]] = j
                    modularity_all.append(lvcm.modularity(community_2,G))
                    flag_changed = 1
                    log_community += [communities_p1]

    return communities_p1, log_community, cnt


def Convert_p2_to_p1(communities_p1, communities_p2_result):
    communities_resultb = []
    for community_p2 in communities_p2_result:
        tmp = []
        for comm in community_p2:
            tmp += communities_p1[comm]
        communities_resultb += [tmp]
    return communities_resultb


if __name__ == '__main__':
    """ initial """
    data_path: str = "../data/karate.gml"
    G: nx.Graph = nx.read_gml(data_path, label="id")
    communities_init = [[i] for i in list(G.nodes())]
    nodes_init = list(G.nodes())
    edges_init = list(G.edges.data('weight', default=True))
    # Q_init = lvcm.modularity(graph=G, partition=communities_init, weight='weight')

    communities_p1 = copy.deepcopy(communities_init)
    nodes_p1 = copy.deepcopy(nodes_init)
    edges_p1 = copy.deepcopy(edges_init)

    abc = {i + 1: i for i in range(len(G.nodes))}
    mod_init = lvcm.modularity(abc, G)
    modularity_all = [mod_init]

    """ Louvain algorithm """
    end_cnt = 0
    log_community_all = []
    communities_result = []
    while end_cnt != 1:
        communities_p2 = [[i] for i in range(len(communities_p1))]
        nodes_p2 = list(range(len(communities_p2)))
        edges_p2 = []
        for c_1 in range(len(communities_p1)):
            for c_2 in range(len(communities_p1)):
                tmp = sum([edge[2] for edge in edges_p1
                           if ((edge[0] in communities_p1[c_1]) & (edge[1] in communities_p1[c_2])) |
                           ((edge[0] in communities_p1[c_1]) & (edge[1] in communities_p1[c_2]))])
                if tmp != 0:
                    edges_p2 += [(c_1, c_2, tmp)]
        communities_p2_result, log_community, end_cnt = Phase_First(communities_p2, nodes_p2, edges_p2)

        for comm in log_community:
            log_community_all += [Convert_p2_to_p1(communities_p1, comm)]

        communities_result = Convert_p2_to_p1(communities_p1, communities_p2_result)
        communities_p1 = copy.deepcopy(communities_result)

    community_num_group = len(communities_result)
    color_list_community = [0 for i in range(len(G.nodes()))]
    for i in range(len(G.nodes())):
        for j in range(community_num_group):
            if i + 1 in communities_result[j]:
                color_list_community[i] = j

    fig2 = plt.figure()
    plt.subplots_adjust(hspace=0.5, wspace=0.3)
    plt.plot(range(len(modularity_all)), modularity_all)
    plt.xlabel('step')
    plt.ylabel('modularity')
    plt.title("unweighted")
    plt.show(block=False)

    """ Plot Community """
    fig = plt.figure()
    Gedges = G.edges()
    pos = nx.spring_layout(G)
    # weights = [G[u][v]['weight'] for u, v in Gedges]
    Feature_color_sub = color_list_community
    node_size = 50
    im = nx.draw_networkx_nodes(G, pos, node_color=Feature_color_sub, node_size=node_size)
    nx.draw_networkx_edges(G, pos)
    nx.draw_networkx_labels(G, pos, font_size=5, font_color="black")
    plt.xticks([])
    plt.yticks([])
    plt.show(block=False)
