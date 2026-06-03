#ifndef CMS_SVC_UTIL_H
#define CMS_SVC_UTIL_H

#include "per/cms_stream.h"
#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Flush a dynamic stream into a fixed output buffer.
   Returns CMS_OK on success, CMS_ERR_BUF_TOO_SMALL if out_buf is too small. */
int cms_write_out(per_stream_t *w, uint8_t *out_buf, int *out_len);

#ifdef __cplusplus
}
#endif

#endif
