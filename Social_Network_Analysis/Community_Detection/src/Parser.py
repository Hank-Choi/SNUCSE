import networkx as nx

if __name__ == '__main__':
    G: nx.Graph = nx.read_weighted_edgelist("../data/4.txt")
    print(G.get_edge_data(1234,1234))
