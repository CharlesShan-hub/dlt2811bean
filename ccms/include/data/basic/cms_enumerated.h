#ifndef DATA_BASIC_CMS_ENUMERATED_H
#define DATA_BASIC_CMS_ENUMERATED_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

int cms_enumerated_encode_stream(per_stream_t *s, const cms_int32_t *v);
int cms_enumerated_decode_stream(per_stream_t *s, cms_int32_t *v);

#ifdef __cplusplus
}
#endif

#endif
