#ifndef DATA_CONTROL_CMS_ADD_CAUSE_H
#define DATA_CONTROL_CMS_ADD_CAUSE_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * AddCause ::= INTEGER { ... (0..27) }
 * ============================================================
 */
#define CMS_ADD_CAUSE_UNKNOWN                       0
#define CMS_ADD_CAUSE_NOT_SUPPORTED                 1
#define CMS_ADD_CAUSE_BLOCKED_BY_SWITCHING_HIERARCHY 2
#define CMS_ADD_CAUSE_SELECT_FAILED                 3
#define CMS_ADD_CAUSE_INVALID_POSITION              4
#define CMS_ADD_CAUSE_POSITION_REACHED              5
#define CMS_ADD_CAUSE_PARAMETER_CHANGE_IN_EXECUTION 6
#define CMS_ADD_CAUSE_STEP_LIMIT                    7
#define CMS_ADD_CAUSE_BLOCKED_BY_MODE               8
#define CMS_ADD_CAUSE_BLOCKED_BY_PROCESS            9
#define CMS_ADD_CAUSE_BLOCKED_BY_INTERLOCKING       10
#define CMS_ADD_CAUSE_BLOCKED_BY_SYNCHECK           11
#define CMS_ADD_CAUSE_COMMAND_ALREADY_IN_EXECUTION  12
#define CMS_ADD_CAUSE_BLOCKED_BY_HEALTH             13
#define CMS_ADD_CAUSE_ONE_OF_A_CONTROL              14
#define CMS_ADD_CAUSE_ABORTION_BY_CANCEL            15
#define CMS_ADD_CAUSE_TIME_LIMIT_OVER               16
#define CMS_ADD_CAUSE_ABORTION_BY_TRIP              17
#define CMS_ADD_CAUSE_OBJECT_NOT_SELECTED           18
#define CMS_ADD_CAUSE_OBJECT_ALREADY_SELECTED       19
#define CMS_ADD_CAUSE_NO_ACCESS_AUTHORITY           20
#define CMS_ADD_CAUSE_ENDED_WITH_OVERSHOOT          21
#define CMS_ADD_CAUSE_ABORTION_DUE_TO_DEVIATION     22
#define CMS_ADD_CAUSE_ABORTION_BY_COMMUNICATION_LOSS 23
#define CMS_ADD_CAUSE_BLOCKED_BY_COMMAND            24
#define CMS_ADD_CAUSE_NONE                          25
#define CMS_ADD_CAUSE_LOCKED_BY_OTHER_CLIENT        26
#define CMS_ADD_CAUSE_INCONSISTENT_PARAMETERS       27

typedef struct { cms_int32_t value; } cms_add_cause_t;

CMS_EXPORT int cms_add_cause_encode(const cms_add_cause_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_add_cause_decode(cms_add_cause_t *v, const uint8_t *in_buf, int in_len);
int cms_add_cause_encode_stream(per_stream_t *s, const cms_add_cause_t *v);
int cms_add_cause_decode_stream(per_stream_t *s, cms_add_cause_t *v);

#ifdef __cplusplus
}
#endif

#endif
