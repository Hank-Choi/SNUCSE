import numpy as np
import torch

N, D_in, H, D_out = 64, 1000, 100, 10


x = torch.randn(N, D_in)
y = torch.randn(N, D_out)

