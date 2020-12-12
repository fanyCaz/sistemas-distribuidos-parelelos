"""
Produces load on all available CPU cores
"""

from multiprocessing import Pool
from multiprocessing import cpu_count
import time
import sys

def f(x):
    start = time.time()
    a = []
    while(time.time() - start < int(sys.argv[1])):
        x*x
        if len(a) < 6000000:
            a.append(' ' * 5**2)
        

if __name__ == '__main__':
    processes = cpu_count()
    print('utilizing %d cores\n' % processes)
    pool = Pool(processes)
    pool.map(f, range(processes))

