#ifndef DATA_BASIC_CMS_ENUMERATED_H
#define DATA_BASIC_CMS_ENUMERATED_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT per_error_t per_encode_enumerated(per_stream_t *s, uint32_t value, uint32_t value_count);
CMS_EXPORT per_error_t per_decode_enumerated(per_stream_t *s, uint32_t *out, uint32_t value_count);

#ifdef __cplusplus
}
#endif

#endif
