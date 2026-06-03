#ifndef DATA_CHOICE_CMS_DATA_H
#define DATA_CHOICE_CMS_DATA_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "data/common/cms_quality.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * cms_data_t — tagged union for Data CHOICE (24 alternatives)
 *
 * For array (choice=1) and structure (choice=2), use:
 *   data.value.array.elements / data.value.structure.elements
 * ============================================================
 */
typedef struct cms_data {
    int      choice;       /* 0 .. 23 */
    union {
        cms_service_error_t error;                        /*  0 */
        struct {                                          /*  1 */
            struct cms_data *elements;
            int              count;
        } array;
        struct {                                          /*  2 */
            struct cms_data *elements;
            int              count;
        } structure;
        int              boolean_value;                   /*  3 */
        int8_t           int8;                            /*  4 */
        int16_t          int16;                           /*  5 */
        int32_t          int32;                           /*  6 */
        int64_t          int64;                           /*  7 */
        uint8_t          int8u;                           /*  8 */
        uint16_t         int16u;                          /*  9 */
        uint32_t         int32u;                          /* 10 */
        uint64_t         int64u;                          /* 11 */
        float            float32;                         /* 12 */
        double           float64;                         /* 13 */
        struct {
            uint8_t     *data;                            /* 14 */
            int          nbits;
        } bit_string;
        struct {
            uint8_t     *data;                            /* 15 */
            int          len;
        } octet_string;
        char            *visible_string;                  /* 16 */
        char            *utf8_string;                     /* 17 */
        int64_t          utc_time_ms;                     /* 18 */
        struct {
            uint32_t     msOfDay;                         /* 19 */
            uint16_t     daysSince1984;
        } binary_time;
        uint8_t          quality[2];                      /* 20 */
        cms_dbpos_t      dbpos;                           /* 21 */
        cms_tcmd_t       tcmd;                            /* 22 */
        uint8_t          check[2];                        /* 23 */
    } value;
} cms_data_t;

/*
 * ============================================================
 * Struct-based encode / decode
 * ============================================================
 */
CMS_EXPORT int cms_data_encode(const cms_data_t *data, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_data_decode(const uint8_t *in_buf, int in_len, cms_data_t *data);

int cms_data_encode_stream(per_stream_t *s, const cms_data_t *data);
int cms_data_decode_stream(per_stream_t *s, cms_data_t *data);

/*
 * ============================================================
 * Lightweight encode helpers — Java JNA uses these to encode
 * just the structural metadata (choice index, count) without
 * needing to marshal the full cms_data_t across FFI.
 * ============================================================
 */
CMS_EXPORT int cms_data_choice_encode(int choice, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_data_count_encode(int count, uint8_t *out_buf, int *out_len);

/*
 * ============================================================
 * Free all heap-allocated memory inside a cms_data_t
 * (frees child arrays, bit/octet strings, etc. but NOT the cms_data_t itself)
 * ============================================================
 */
CMS_EXPORT void cms_data_free(cms_data_t *data);

#ifdef __cplusplus
}
#endif

#endif
