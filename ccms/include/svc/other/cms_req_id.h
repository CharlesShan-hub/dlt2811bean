#ifndef CMS_REQ_ID_H
#define CMS_REQ_ID_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_int16u.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ReqId ::= Int16U
 *
 * ============================================================
 */
typedef cms_int16u_t cms_req_id_t;

int cms_req_id_encode_stream(per_stream_t *s, const cms_req_id_t *v);
int cms_req_id_decode_stream(per_stream_t *s, void *ptr);
CMS_EXPORT int cms_req_id_encode(const cms_req_id_t *v, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_req_id_decode(cms_req_id_t *v, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
