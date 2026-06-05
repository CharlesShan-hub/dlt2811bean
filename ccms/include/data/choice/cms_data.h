#ifndef DATA_CHOICE_CMS_DATA_H
#define DATA_CHOICE_CMS_DATA_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_float.h"
#include "per/cms_string.h"
#include "data/common/cms_service_error.h"
#include "data/common/cms_dbpos.h"
#include "data/common/cms_tcmd.h"
#include "data/common/cms_quality.h"
#include "data/control/cms_check.h"
#include "data/extended/cms_time.h"


#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * cms_data_t — tagged union for Data CHOICE (24 alternatives)
 * ============================================================
 */
typedef struct cms_data cms_data_t;

typedef struct {
    cms_data_t *elements;
    cms_int32_t count;
} cms_data_array_t;

typedef struct {
    cms_data_t *elements;
    cms_int32_t count;
} cms_data_structure_t;

struct cms_data {
    int32_t  choice;       /* 0 .. 23 */
    union {
        cms_service_error_t   error;                        /*  0 */
        cms_data_array_t      array;                        /*  1 */
        cms_data_structure_t  structure;                    /*  2 */
        cms_boolean_t      boolean_value;                 /*  3 */
        cms_int8_t         int8;                          /*  4 */
        cms_int16_t        int16;                         /*  5 */
        cms_int32_t        int32;                         /*  6 */
        cms_int64_t        int64;                         /*  7 */
        cms_int8u_t        int8u;                         /*  8 */
        cms_int16u_t       int16u;                        /*  9 */
        cms_int32u_t       int32u;                        /* 10 */
        cms_int64u_t       int64u;                        /* 11 */
        cms_float32_t      float32;                       /* 12 */
        cms_float64_t      float64;                       /* 13 */
        cms_uint8_array_t  bit_string;                    /* 14 */
        cms_uint8_array_t  octet_string;                  /* 15 */
        cms_uint8_array_t  visible_string;                /* 16 */
        cms_uint8_array_t  utf8_string;                   /* 17 */
        cms_utc_time_t   utc_time;                          /* 18 */
        cms_binary_time_t  binary_time;                      /* 19 */
        cms_quality_t    quality;                         /* 20 */
        cms_dbpos_t      dbpos;                           /* 21 */
        cms_tcmd_t       tcmd;                            /* 22 */
        cms_check_t      check;                           /* 23 */
    } value;
};

CMS_EXPORT int cms_data_encode(const cms_data_t *data, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_data_decode(const uint8_t *in_buf, int in_len, cms_data_t *data);
int cms_data_encode_stream(per_stream_t *s, const cms_data_t *data);
int cms_data_decode_stream(per_stream_t *s, cms_data_t *data);

CMS_EXPORT int cms_data_choice_encode(int32_t choice, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_data_count_encode(int32_t count, uint8_t *out_buf, int *out_len);

CMS_EXPORT void cms_data_free(cms_data_t *data);

#ifdef __cplusplus
}
#endif

#endif
