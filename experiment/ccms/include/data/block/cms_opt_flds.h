#ifndef DATA_BLOCK_CMS_OPT_FLDS_H
#define DATA_BLOCK_CMS_OPT_FLDS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_lcb_opt_flds_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_lcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_lcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[1]);

CMS_EXPORT int cms_msvcb_opt_flds_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_msvcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[1]);

CMS_EXPORT int cms_rcb_opt_flds_encode(const uint8_t value[2], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_rcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[2]);
int cms_rcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[2]);
int cms_rcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[2]);

#ifdef __cplusplus
}
#endif

#endif
