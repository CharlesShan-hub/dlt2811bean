#include "svc/sg/cms_sg_ref_value_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/choice/cms_data.h"

int cms_sg_ref_value_entry_encode_stream(per_stream_t *s, const cms_sg_ref_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR;
    int err; err = cms_object_reference_encode_stream(s, v->reference); if (err) return err;
    err = cms_data_encode_stream(s, v->value); if (err) return err;
    return CMS_OK;
}
int cms_sg_ref_value_entry_decode_stream(per_stream_t *s, cms_sg_ref_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR;
    int err; err = cms_object_reference_decode_stream(s, v->reference); if (err) return err;
    err = cms_data_decode_stream(s, v->value); if (err) return err;
    return CMS_OK;
}
