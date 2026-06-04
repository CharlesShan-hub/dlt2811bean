#include "data/basic/cms_packed_list.h"

int cms_packed_list_encode_stream(per_stream_t *s, const cms_bit_string_var_t *v)
    { return cms_bit_string_var_encode_stream(s, v); }

int cms_packed_list_decode_stream(per_stream_t *s, cms_bit_string_var_t *v)
    { return cms_bit_string_var_decode_stream(s, v); }
