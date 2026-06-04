#ifndef DATA_BLOCK_CMS_OPT_FLDS_H
#define DATA_BLOCK_CMS_OPT_FLDS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * RCBOptFlds (BIT STRING, 10 bits)
 * ============================================================
 */
typedef uint8_t cms_rcb_opt_flds_t[2];

CMS_EXPORT int cms_rcb_opt_flds_encode(const cms_rcb_opt_flds_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_rcb_opt_flds_decode(const uint8_t *in_buf, int in_len, cms_rcb_opt_flds_t value);
int cms_rcb_opt_flds_encode_stream(per_stream_t *s, const cms_rcb_opt_flds_t value);
int cms_rcb_opt_flds_decode_stream(per_stream_t *s, cms_rcb_opt_flds_t value);

/*
 * ============================================================
 * MSVCBOptFlds (BIT STRING, 5 bits)
 * ============================================================
 */
typedef uint8_t cms_msvcb_opt_flds_t[1];

CMS_EXPORT int cms_msvcb_opt_flds_encode(const cms_msvcb_opt_flds_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_msvcb_opt_flds_decode(const uint8_t *in_buf, int in_len, cms_msvcb_opt_flds_t value);
int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const cms_msvcb_opt_flds_t value);
int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, cms_msvcb_opt_flds_t value);

/*
 * ============================================================
 * LCBOptFlds (BIT STRING, 1 bit)
 * ============================================================
 */
typedef uint8_t cms_lcb_opt_flds_t[1];

CMS_EXPORT int cms_lcb_opt_flds_encode(const cms_lcb_opt_flds_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_lcb_opt_flds_decode(const uint8_t *in_buf, int in_len, cms_lcb_opt_flds_t value);
int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const cms_lcb_opt_flds_t value);
int cms_lcb_opt_flds_decode_stream(per_stream_t *s, cms_lcb_opt_flds_t value);

#ifdef __cplusplus
}
#endif

#endif
