#ifndef DATA_BASIC_CMS_CODED_ENUM_H
#define DATA_BASIC_CMS_CODED_ENUM_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

int cms_coded_enum_encode_stream(per_stream_t *s, const cms_bit_string_fixed_t *v);
int cms_coded_enum_decode_stream(per_stream_t *s, cms_bit_string_fixed_t *v);

#ifdef __cplusplus
}
#endif

#endif
