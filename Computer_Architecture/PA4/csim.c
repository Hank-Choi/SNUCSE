#include "cachelab.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

typedef struct
{
    block *blocks;
} set;

typedef struct
{
    int valid;
    unsigned long long tag;
    int recent_count;
} block;
void update_count(block *blocks, int index, int E)
{
    blocks[index].recent_count = 0;
    for (int i = 0; i < E; i++)
    {
        blocks[i].recent_count++;
    }
}
void access(int *hit_count, int *miss_count, int *eviction_count, unsigned long long set_index, unsigned long long tag, int E, set *cache_sets)
{
    for (int i = 0; i < E; i++)
    {
        if (cache_sets[set_index].blocks[i].valid == 0) //miss
        {
            // printf("miss ");
            (*miss_count)++;
            cache_sets[set_index].blocks[i].valid = 1;
            cache_sets[set_index].blocks[i].tag = tag;
            update_count(cache_sets[set_index].blocks, i, E);
            break;
        }
        else
        {
            if (cache_sets[set_index].blocks[i].tag == tag) //hit
            {
                // printf("hit ");
                (*hit_count)++;
                update_count(cache_sets[set_index].blocks, i, E);
                break;
            }
            else if (E == i + 1) //evict
            {
                // printf("miss evict ");
                (*miss_count)++;
                (*eviction_count)++;
                int oldest_index = 0;
                int oldest_count = 0;
                for (int j = 0; j < E; j++)
                {
                    int j_count = cache_sets[set_index].blocks[j].recent_count;
                    if (j_count > oldest_count)
                    {
                        oldest_index = j;
                        oldest_count = j_count;
                    }
                }
                cache_sets[set_index].blocks[oldest_index].tag = tag;
                update_count(cache_sets[set_index].blocks, oldest_index, E);
                break;
            }
        }
    }
}

void execute(FILE *trace, set *cache_sets, int *hit_count, int *miss_count, int *eviction_count, int s, int E, int b)
{
    char buffer[100];
    while (fgets(buffer, sizeof(buffer), trace))
    {
        if (buffer[0] == ' ')
        {
            // printf("%s", buffer);
            unsigned long long address = 0;
            for (int i = 3; buffer[i] != ','; i++)
            {
                int digit;
                address = address << 4;
                if (buffer[i] >= 'a')
                    digit = buffer[i] - 'a' + 10;
                else
                    digit = buffer[i] - '0';
                address += digit;
            }
            unsigned long long set_index = ((address >> b) << (64 - s)) >> (64 - s);
            unsigned long long tag = address >> (b + s);
            if (buffer[1] == 'M')
                access(hit_count, miss_count, eviction_count, set_index, tag, E, cache_sets);
            access(hit_count, miss_count, eviction_count, set_index, tag, E, cache_sets);
        }
    }
}
int main(int argc, char *argv[])
{
    FILE *trace = fopen(argv[argc - 1], "r");
    int hit_count = 0, miss_count = 0, eviction_count = 0;
    int i = 0;
    if (strcmp(argv[1], "-v") == 0 || strcmp(argv[1], "-h") == 0)
    {
        i = 1;
    }
    int s = *argv[2 + i] - '0', E = *argv[4 + i] - '0', b = *argv[6 + i] - '0';
    unsigned long long S = 1 << s;
    set cache_sets[S];
    for (int i = 0; i < S; i++)
    {
        cache_sets[i].blocks = (block *)malloc(sizeof(block) * E);
        // printf("cache_sets: %p   cache_sets %d . block :%p\n", cache_sets, i, cache_sets[i].blocks);
        for (int j = 0; j < E; j++)
        {
            cache_sets[i].blocks[j].valid = 0;
            cache_sets[i].blocks[j].tag = 0;
            cache_sets[i].blocks[j].recent_count = 0;
        }
    }
    execute(trace, cache_sets, &hit_count, &miss_count, &eviction_count, s, E, b);
    fclose(trace);
    printSummary(hit_count, miss_count, eviction_count);
    return 0;
}