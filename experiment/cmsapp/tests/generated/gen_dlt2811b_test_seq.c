#include "gen_dlt2811b_test_seq.h"
#include <stdlib.h>
#include <string.h>

int encode_Item(per_stream_t *s, const Item *v) {
    per_encode_constrained_int(s, v->id, 0, 255);
    per_encode_constrained_int(s, v->value, 0, 4294967295);
    return 0;
}

int decode_Item(per_stream_t *s, Item *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->id = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->value = (int)_tmp;
    }
    return 0;
}

int encode_Container(per_stream_t *s, const Container *v) {
    per_encode_visible_string(s, v->name, 255);
    per_encode_length(s, v->items_count);
    for (int _i = 0; _i < v->items_count; _i++) {
        encode_Item(s, &v->items[_i]);
    }
    return 0;
}

int decode_Container(per_stream_t *s, Container *v) {
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->name = strdup(_buf);
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->items_count = (int)_count;
        v->items = calloc(_count, sizeof(*v->items));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_Item(s, &v->items[_i]);
        }
    }
    return 0;
}

