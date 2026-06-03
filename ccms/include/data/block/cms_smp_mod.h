#ifndef DATA_BLOCK_CMS_SMP_MOD_H
#define DATA_BLOCK_CMS_SMP_MOD_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * ============================================================
 * SMPMod
 * ============================================================
 */
typedef enum {
    CMS_SMP_MOD_SAMPLES_PER_NOMINAL_PERIOD = 0,
    CMS_SMP_MOD_SAMPLES_PER_SECOND         = 1,
    CMS_SMP_MOD_SECONDS_PER_SAMPLE         = 2
} cms_smp_mod_t;

CMS_EXPORT int cms_smp_mod_encode(cms_smp_mod_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_smp_mod_decode(const uint8_t *in_buf, int in_len, cms_smp_mod_t *value);
int cms_smp_mod_encode_stream(per_stream_t *s, cms_smp_mod_t value);
int cms_smp_mod_decode_stream(per_stream_t *s, cms_smp_mod_t *value);

#ifdef __cplusplus
}
#endif

#endif
