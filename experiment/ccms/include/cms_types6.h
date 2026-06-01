#ifndef CMS_TYPES6_H
#define CMS_TYPES6_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_lcb_opt_flds_encode(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_lcb_opt_flds_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

CMS_EXPORT int cms_msvcb_opt_flds_encode(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_msvcb_opt_flds_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

CMS_EXPORT int cms_rcb_opt_flds_encode(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_rcb_opt_flds_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

CMS_EXPORT int cms_reason_code_encode(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_reason_code_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

CMS_EXPORT int cms_trigger_conditions_encode(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_trigger_conditions_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

CMS_EXPORT int cms_smp_mod_encode(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_smp_mod_decode(
    const uint8_t *in_buf, int in_len,
    int *value
);

#ifdef __cplusplus
}
#endif

#endif
