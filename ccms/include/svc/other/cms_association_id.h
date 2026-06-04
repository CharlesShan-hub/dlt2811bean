#ifndef CMS_ASSOCIATION_ID_H
#define CMS_ASSOCIATION_ID_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

#define CMS_ASSOCIATION_ID_MAX 64

typedef struct {
    uint8_t data[CMS_ASSOCIATION_ID_MAX];
    int     len;
} cms_association_id_t;

/* Stream-based API — for SDU encode/decode reuse */
int cms_association_id_encode_stream(per_stream_t *s, const cms_association_id_t *id);
int cms_association_id_decode_stream(per_stream_t *s, cms_association_id_t *id);

/* Buffer-based API — for direct FFI calls */
CMS_EXPORT int cms_association_id_encode(const cms_association_id_t *id, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_association_id_decode(const uint8_t *in_buf, int in_len, cms_association_id_t *id);

#ifdef __cplusplus
}
#endif

#endif
