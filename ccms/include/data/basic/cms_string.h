#ifndef DATA_BASIC_CMS_STRING_H
#define DATA_BASIC_CMS_STRING_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * VisibleString
 * ============================================================
 * size_len > 0  -> fixed mode (no length prefix)
 * size_len == 0 -> variable mode (constrained int length prefix, max_len = ub)
 */
/* ---------- VisibleStringFixed ---------- */
typedef struct {
    char   *value;
    int     fixed_len;
} cms_visible_string_fixed_t;

CMS_EXPORT int cms_visible_string_fixed_encode(const cms_visible_string_fixed_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_visible_string_fixed_decode(cms_visible_string_fixed_t *v, const uint8_t *in_buf, int in_len);
int cms_visible_string_fixed_encode_stream(per_stream_t *s, const cms_visible_string_fixed_t *v);
int cms_visible_string_fixed_decode_stream(per_stream_t *s, cms_visible_string_fixed_t *v);

/* ---------- VisibleStringVar ---------- */
typedef struct {
    char   *value;
    int     max_len;
} cms_visible_string_var_t;

CMS_EXPORT int cms_visible_string_var_encode(const cms_visible_string_var_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_visible_string_var_decode(cms_visible_string_var_t *v, const uint8_t *in_buf, int in_len);
int cms_visible_string_var_encode_stream(per_stream_t *s, const cms_visible_string_var_t *value);
int cms_visible_string_var_decode_stream(per_stream_t *s, cms_visible_string_var_t *value);

/*
 * ============================================================
 * UTF8String
 * ============================================================
 * size_len > 0  -> fixed mode (no length prefix)
 * size_len == 0 -> variable mode (constrained int length prefix, max_len = ub)
 */
/* ---------- UTF8StringFixed ---------- */
typedef struct {
    char   *value;
    int     fixed_len;
} cms_utf8_string_fixed_t;

CMS_EXPORT int cms_utf8_string_fixed_encode(const cms_utf8_string_fixed_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utf8_string_fixed_decode(cms_utf8_string_fixed_t *v, const uint8_t *in_buf, int in_len);
int cms_utf8_string_fixed_encode_stream(per_stream_t *s, const cms_utf8_string_fixed_t *v);
int cms_utf8_string_fixed_decode_stream(per_stream_t *s, cms_utf8_string_fixed_t *v);

/* ---------- UTF8StringVar ---------- */
typedef struct {
    char   *value;
    int     max_len;
} cms_utf8_string_var_t;

CMS_EXPORT int cms_utf8_string_var_encode(const cms_utf8_string_var_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utf8_string_var_decode(cms_utf8_string_var_t *v, const uint8_t *in_buf, int in_len);
int cms_utf8_string_var_encode_stream(per_stream_t *s, const cms_utf8_string_var_t *value);
int cms_utf8_string_var_decode_stream(per_stream_t *s, cms_utf8_string_var_t *value);

/*
 * ============================================================
 * OctetString
 * ============================================================
 * size_len > 0  -> fixed mode, encode exactly size_len bytes (no length prefix)
 * size_len == 0 -> variable mode, constrained int length prefix, max_len = ub
 */
/* ---------- OctetStringFixed ---------- */
typedef struct {
    uint8_t *value;
    int      fixed_len;
} cms_octet_string_fixed_t;

CMS_EXPORT int cms_octet_string_fixed_encode(const cms_octet_string_fixed_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_octet_string_fixed_decode(cms_octet_string_fixed_t *v, const uint8_t *in_buf, int in_len);
int cms_octet_string_fixed_encode_stream(per_stream_t *s, const cms_octet_string_fixed_t *v);
int cms_octet_string_fixed_decode_stream(per_stream_t *s, cms_octet_string_fixed_t *v);

/* ---------- OctetStringVar ---------- */
typedef struct {
    uint8_t *value;
    int      len;
    int      max_len;
} cms_octet_string_var_t;

CMS_EXPORT int cms_octet_string_var_encode(const cms_octet_string_var_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_octet_string_var_decode(cms_octet_string_var_t *v, const uint8_t *in_buf, int in_len);
int cms_octet_string_var_encode_stream(per_stream_t *s, const cms_octet_string_var_t *v);
int cms_octet_string_var_decode_stream(per_stream_t *s, cms_octet_string_var_t *v);

/*
 * ============================================================
 * BitString
 * ============================================================
 * nbits > 0 && max_nbits == 0 -> fixed mode, encode exactly nbits bits
 * nbits > 0 && max_nbits > 0  -> variable mode, encode nbits of max_nbits
 */
/* ---------- BitStringFixed ---------- */
typedef struct {
    uint8_t *value;
    int      nbits;
} cms_bit_string_fixed_t;

CMS_EXPORT int cms_bit_string_fixed_encode(const cms_bit_string_fixed_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_bit_string_fixed_decode(cms_bit_string_fixed_t *v, const uint8_t *in_buf, int in_len);
int cms_bit_string_fixed_encode_stream(per_stream_t *s, const cms_bit_string_fixed_t *v);
int cms_bit_string_fixed_decode_stream(per_stream_t *s, cms_bit_string_fixed_t *v);

/* ---------- BitStringVar ---------- */
typedef struct {
    uint8_t *value;
    int      nbits;
    int      max_len;
} cms_bit_string_var_t;

CMS_EXPORT int cms_bit_string_var_encode(const cms_bit_string_var_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_bit_string_var_decode(cms_bit_string_var_t *v, const uint8_t *in_buf, int in_len);
int cms_bit_string_var_encode_stream(per_stream_t *s, const cms_bit_string_var_t *value);
int cms_bit_string_var_decode_stream(per_stream_t *s, cms_bit_string_var_t *value);

#ifdef __cplusplus
}
#endif

#endif
