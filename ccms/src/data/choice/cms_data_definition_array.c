#include "data/choice/cms_data_definition_array.h"
#include "data/choice/cms_data_definition.h"

int cms_data_definition_array_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_data_definition_array_t *a = (const cms_data_definition_array_t*)ptr;
    if (!a) return CMS_ERR;
    int err;

    /* numberOfElement — Int32 */
    if (!a->numberOfElement) return CMS_ERR;
    err = cms_int32_encode_stream(s, a->numberOfElement);
    if (err) return err;

    /* elementType — DataDefinition */
    if (!a->elementType) return CMS_ERR;
    err = cms_data_definition_encode_stream(s, a->elementType);
    if (err) return err;

    return CMS_OK;
}

int cms_data_definition_array_decode_stream(per_stream_t *s, void *ptr) {
    cms_data_definition_array_t *a = (cms_data_definition_array_t*)ptr;
    int err;

    /* numberOfElement */
    if (a && !a->numberOfElement) return CMS_ERR;
    err = cms_int32_decode_stream(s, a ? a->numberOfElement : NULL);
    if (err) return err;

    /* elementType */
    if (a && !a->elementType) return CMS_ERR;
    err = cms_data_definition_decode_stream(s, a ? a->elementType : NULL);
    if (err) return err;

    return CMS_OK;
}
