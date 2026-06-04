#ifndef CMS_GET_SERVER_DIRECTORY_H
#define CMS_GET_SERVER_DIRECTORY_H

#include "svc/cms_svc.h"
#include "data/basic/cms_boolean.h"
#include "data/common/cms_object_name.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

#define CMS_MAX_REF_COUNT 32

typedef struct {
    int object_class;
    int has_ref_after;
    char ref_after[65];
} cms_get_server_directory_request_t;

typedef struct {
    int ref_count;
    char refs_flat[CMS_MAX_REF_COUNT * 65];
    int ref_lens[CMS_MAX_REF_COUNT];
    int more_follows;
} cms_get_server_directory_response_t;

typedef struct {
    cms_service_error_t service_error;
} cms_get_server_directory_error_t;

CMS_EXPORT int cms_get_server_directory_request_encode(
    const cms_get_server_directory_request_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_server_directory_request_decode(
    const uint8_t *in_buf, int in_len,
    cms_get_server_directory_request_t *sdu
);

CMS_EXPORT int cms_get_server_directory_response_encode(
    const cms_get_server_directory_response_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_server_directory_response_decode(
    const uint8_t *in_buf, int in_len,
    cms_get_server_directory_response_t *sdu
);

CMS_EXPORT int cms_get_server_directory_error_encode(
    const cms_get_server_directory_error_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_server_directory_error_decode(
    const uint8_t *in_buf, int in_len,
    cms_get_server_directory_error_t *sdu
);

#ifdef __cplusplus
}
#endif

#endif