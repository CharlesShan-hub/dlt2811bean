#ifndef CMSPER_ENUMERATED_H
#define CMSPER_ENUMERATED_H

#include "per/cms_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Per X.691: Enumerated is encoded as a constrained int with range = valueCount. */
per_error_t per_encode_enumerated(per_stream_t *s, uint32_t value, uint32_t value_count);
per_error_t per_decode_enumerated(per_stream_t *s, uint32_t *out, uint32_t value_count);

#ifdef __cplusplus
}
#endif

#endif
