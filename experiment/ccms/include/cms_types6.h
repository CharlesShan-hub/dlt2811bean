#ifndef CMS_TYPES6_H
#define CMS_TYPES6_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_encode_LcbOptFlds(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_LcbOptFlds(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

CMS_EXPORT int cms_encode_MsvcbOptFlds(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_MsvcbOptFlds(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

CMS_EXPORT int cms_encode_RcbOptFlds(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_RcbOptFlds(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

CMS_EXPORT int cms_encode_ReasonCode(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_ReasonCode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

CMS_EXPORT int cms_encode_TriggerConditions(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_TriggerConditions(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

CMS_EXPORT int cms_encode_SmpMod(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_SmpMod(
    const uint8_t *in_buf, int in_len,
    int *value
);

#ifdef __cplusplus
}
#endif

#endif
