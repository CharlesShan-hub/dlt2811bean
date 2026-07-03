#ifndef CMS_GET_ALL_DATA_DEFINITION_H
#define CMS_GET_ALL_DATA_DEFINITION_H

#include "svc/cms_svc.h"
#include "svc/other/cms_reference_choice.h"
#include "svc/directory/cms_data_definition_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetAllDataDefinition-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ReferenceChoice,
 *     fc              [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     referenceAfter  [2] IMPLICIT ObjectReference OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_reference_choice_t *reference;
    cms_boolean_t *fc_present;
    cms_functional_constraint_t *fc;
    cms_boolean_t *ref_after_present;
    cms_object_reference_t *ref_after;
} cms_get_all_data_definition_request_t;

/*
 * ============================================================
 * GetAllDataDefinition-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF DataDefinitionEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *data;           /* SEQUENCE OF DataDefinitionEntry */
    cms_boolean_t *more_follows; /* DEFAULT TRUE */
} cms_get_all_data_definition_response_t;

/*
 * ============================================================
 * GetAllDataDefinition-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_get_all_data_definition_error_t;

CMS_EXPORT int cms_get_all_data_definition_request_encode(const cms_get_all_data_definition_request_t *pdu,
                                                          uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_get_all_data_definition_request_decode(cms_get_all_data_definition_request_t *pdu,
                                                          const uint8_t *in_buf, int in_len);

CMS_EXPORT int cms_get_all_data_definition_response_encode(const cms_get_all_data_definition_response_t *pdu,
                                                           uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_get_all_data_definition_response_decode(cms_get_all_data_definition_response_t *pdu,
                                                           const uint8_t *in_buf, int in_len);

CMS_EXPORT int cms_get_all_data_definition_error_encode(const cms_get_all_data_definition_error_t *pdu,
                                                        uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_get_all_data_definition_error_decode(cms_get_all_data_definition_error_t *pdu, const uint8_t *in_buf,
                                                        int in_len);

#ifdef __cplusplus
}
#endif

#endif
