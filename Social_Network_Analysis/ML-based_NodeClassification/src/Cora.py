import os.path as osp

import torch
import torch.nn.functional as F
from torch_geometric.datasets import Planetoid
import torch_geometric.transforms as T
from torch_geometric.nn import SplineConv

import torch
import os.path as osp
import torch.nn.functional as F
from torch.nn import ModuleList
from torch_geometric.datasets import KarateClub
from torch_geometric.datasets import Planetoid
import torch_geometric.transforms as T
from torch_geometric.nn import GCNConv, ChebConv  # noq
from torch_geometric.utils import convert
import matplotlib.pyplot as plt
from sklearn.manifold import TSNE
import networkx as nx

G= nx.karate_club_graph()
adj = nx.adjacency_data(G)

colors = [
    '#ffc0cb', '#bada55', '#008080', '#420420', '#7fe5f0', '#065535', '#ffd700'
]
real_label=[]
label =[0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]

for c in adj['nodes']:
    if c['club'] == 'Mr. Hi':
        real_label += [0]
    else:
        real_label += [1]

co1 = [colors[a] for a in label]
co2 = [colors[a] for a in real_label]
fig = plt.figure(figsize=(8, 8))
pos = nx.spring_layout(G)
im = nx.draw_networkx_nodes(G, pos, node_color=co1, node_size=100)
nx.draw_networkx_edges(G, pos)

plt.show(block=False)


plt.figure(figsize=(8, 8))
nx.draw_networkx_nodes(G, pos, node_color=co2, node_size=100)
nx.draw_networkx_edges(G, pos)

plt.show(block=False)


