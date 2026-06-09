#ifndef DATA_BASIC_CMS_PACKED_LIST_H
#define DATA_BASIC_CMS_PACKED_LIST_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

int cms_packed_list_encode_stream(per_stream_t *s, const cms_bit_string_var_t *v);
int cms_packed_list_decode_stream(per_stream_t *s, cms_bit_string_var_t *v);

#ifdef __cplusplus
}
#endif

#endif
