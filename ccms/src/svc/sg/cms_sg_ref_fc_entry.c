#include "svc/sg/cms_sg_ref_fc_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"

int cms_sg_ref_fc_entry_encode_stream(per_stream_t *s, const cms_sg_ref_fc_entry_t *v) {
    if (!v || !v->reference || !v->fc) return CMS_ERR;
    int err; err = cms_object_reference_encode_stream(s, v->reference); if (err) return err;
    err = cms_functional_constraint_encode_stream(s, v->fc); if (err) return err;
    return CMS_OK;
}
int cms_sg_ref_fc_entry_decode_stream(per_stream_t *s, cms_sg_ref_fc_entry_t *v) {
    if (!v || !v->reference || !v->fc) return CMS_ERR;
    int err; err = cms_object_reference_decode_stream(s, v->reference); if (err) return err;
    err = cms_functional_constraint_decode_stream(s, v->fc); if (err) return err;
    return CMS_OK;
}
