#ifndef CMS_FFI_H
#define CMS_FFI_H

#include <stdint.h>
#include <stddef.h>

#ifdef _MSC_VER
  #define CMS_EXPORT __declspec(dllexport)
#else
  #define CMS_EXPORT __attribute__((visibility("default")))
#endif

#ifdef __cplusplus
extern "C" {
#endif

/* ==================== 通用返回值 ==================== */
#define CMS_OK      0
#define CMS_ERR    -1
#define CMS_ERR_BUF_TOO_SMALL  -2

/* ==================== Associate-Request ==================== */
CMS_EXPORT int cms_ffi_encode_associate_request(
    int64_t req_id,
    const char *sap_ref,
    int has_auth,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_ffi_decode_associate_request(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id,
    char *sap_ref, int *sap_ref_cap,
    int *has_auth
);

/* ==================== Release-Request ==================== */
CMS_EXPORT int cms_ffi_encode_release_request(
    int64_t req_id,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_ffi_decode_release_request(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id
);

/* ==================== Abort ==================== */
CMS_EXPORT int cms_ffi_encode_abort(
    int64_t req_id,
    int64_t abort_reason,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_ffi_decode_abort(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id,
    int64_t *abort_reason
);

#ifdef __cplusplus
}
#endif

#endif /* CMS_FFI_H */
