#ifndef DATA_BLOCK_CMS_SMP_MOD_H
#define DATA_BLOCK_CMS_SMP_MOD_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SmpMod ::= INTEGER {
 *     samples-per-nominal-period (0),
 *     samples-per-second         (1),
 *     seconds-per-sample         (2)
 * } (0..2)
 * ============================================================
 */
#define CMS_SMP_MOD_SAMPLES_PER_NOMINAL_PERIOD 0
#define CMS_SMP_MOD_SAMPLES_PER_SECOND         1
#define CMS_SMP_MOD_SECONDS_PER_SAMPLE         2

typedef struct { cms_int32_t value; } cms_smp_mod_t;

CMS_EXPORT int cms_smp_mod_encode(const cms_smp_mod_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_smp_mod_decode(cms_smp_mod_t *v, const uint8_t *in_buf, int in_len);
int cms_smp_mod_encode_stream(per_stream_t *s, const cms_smp_mod_t *v);
int cms_smp_mod_decode_stream(per_stream_t *s, cms_smp_mod_t *v);

#ifdef __cplusplus
}
#endif

#endif
