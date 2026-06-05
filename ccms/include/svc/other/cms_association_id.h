#ifndef CMS_ASSOCIATION_ID_H
#define CMS_ASSOCIATION_ID_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

#define CMS_ASSOCIATION_ID_MAX 64

typedef cms_uint8_array_t cms_association_id_t;

int cms_association_id_encode_stream(per_stream_t *s, const cms_association_id_t *id);
int cms_association_id_decode_stream(per_stream_t *s, cms_association_id_t *id);
CMS_EXPORT int cms_association_id_encode(const cms_association_id_t *id, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_association_id_decode(cms_association_id_t *id, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
