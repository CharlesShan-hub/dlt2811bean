#ifndef CMSPER_BOOLEAN_H
#define CMSPER_BOOLEAN_H

#include "per/cms_stream.h"
#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Per X.691: BOOLEAN is encoded as a single bit. */
per_error_t per_encode_boolean(per_stream_t *s, bool value);
per_error_t per_decode_boolean(per_stream_t *s, bool *out);

#ifdef __cplusplus
}
#endif

#endif
