#ifndef DATA_CONTROL_CMS_ORIGINATOR_H
#define DATA_CONTROL_CMS_ORIGINATOR_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Originator
 * ============================================================
 */
typedef enum {
    CMS_ORCAT_NOT_SUPPORTED     = 0,
    CMS_ORCAT_BAY_CONTROL       = 1,
    CMS_ORCAT_STATION_CONTROL   = 2,
    CMS_ORCAT_REMOTE_CONTROL    = 3,
    CMS_ORCAT_AUTOMATIC_BAY     = 4,
    CMS_ORCAT_AUTOMATIC_STATION = 5,
    CMS_ORCAT_AUTOMATIC_REMOTE  = 6,
    CMS_ORCAT_MAINTENANCE       = 7,
    CMS_ORCAT_PROCESS           = 8
} cms_orcat_t;

CMS_EXPORT int cms_originator_encode(cms_orcat_t or_cat, const uint8_t *or_ident, int or_ident_len, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_originator_decode(const uint8_t *in_buf, int in_len, cms_orcat_t *or_cat, uint8_t *or_ident, int *or_ident_cap);
int cms_originator_encode_stream(per_stream_t *s, cms_orcat_t or_cat, const uint8_t *or_ident, int or_ident_len);
int cms_originator_decode_stream(per_stream_t *s, cms_orcat_t *or_cat, uint8_t *or_ident, int *or_ident_cap);

#ifdef __cplusplus
}
#endif

#endif
