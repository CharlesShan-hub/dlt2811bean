#ifndef DATA_BLOCK_CMS_OPT_FLDS_H
#define DATA_BLOCK_CMS_OPT_FLDS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LCBOptFLDS
 * ============================================================
 */
CMS_EXPORT int cms_lcb_opt_flds_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_lcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_lcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[1]);

/*
 * ============================================================
 * MSVBOptFLDS
 * ============================================================
 */
typedef enum {
    CMS_MSVC_OPT_REFRESH_TIME = 0, /* bit 0 */
    CMS_MSVC_OPT_RESERVED     = 1, /* bit 1 */
    CMS_MSVC_OPT_SAMPLE_RATE  = 2, /* bit 2 */
    CMS_MSVC_OPT_DATA_SET_NAME= 3, /* bit 3 */
    CMS_MSVC_OPT_SECURITY     = 4  /* bit 4 */
} cms_msvcb_opt_flds_t;

CMS_EXPORT int cms_msvcb_opt_flds_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_msvcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[1]);

/*
 * ============================================================
 * RCBOptFLDS
 * ============================================================
 */
typedef enum {
    CMS_RCB_OPT_RESERVED         = 0, /* bit 0 */
    CMS_RCB_OPT_SEQUENCE_NUMBER  = 1, /* bit 1 */
    CMS_RCB_OPT_REPORT_TIME_STAMP= 2, /* bit 2 */
    CMS_RCB_OPT_REASON_FOR_INCLUSION = 3, /* bit 3 */
    CMS_RCB_OPT_DATA_SET_NAME    = 4, /* bit 4 */
    CMS_RCB_OPT_DATA_REFERENCE    = 5, /* bit 5 */
    CMS_RCB_OPT_BUFFER_OVERFLOW   = 6, /* bit 6 */
    CMS_RCB_OPT_ENTRY_ID          = 7, /* bit 7 */
    CMS_RCB_OPT_CONF_REVISION     = 8, /* bit 8 */
    CMS_RCB_OPT_SEGMENTATION      = 9  /* bit 9 */
} cms_rcb_opt_flds_t;

CMS_EXPORT int cms_rcb_opt_flds_encode(const uint8_t value[2], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_rcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[2]);
int cms_rcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[2]);
int cms_rcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[2]);

#ifdef __cplusplus
}
#endif

#endif
