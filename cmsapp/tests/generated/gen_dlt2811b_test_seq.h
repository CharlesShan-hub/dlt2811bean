#ifndef GEN_dlt2811b_test_seq_H
#define GEN_dlt2811b_test_seq_H

#include <stdint.h>
#include "cmsper.h"

typedef struct Item Item;
typedef struct Container Container;

typedef struct Item {
    int64_t id;
    int64_t value;
} Item;

typedef struct Container {
    char * name;
    Item *items;
    int items_count;
} Container;

typedef int64_t Int8U;

typedef int64_t Int32U;

int encode_Item(per_stream_t *s, const Item *v);
int decode_Item(per_stream_t *s, Item *v);
int encode_Container(per_stream_t *s, const Container *v);
int decode_Container(per_stream_t *s, Container *v);
#endif
