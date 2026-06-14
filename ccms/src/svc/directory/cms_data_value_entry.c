#include "svc/directory/cms_data_value_entry.h"
#include "data/common/cms_sub_reference.h"
#include "data/choice/cms_data.h"

int cms_data_value_entry_encode_stream(per_stream_t *s, const cms_data_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR;
    int err;
    err = cms_sub_reference_encode_stream(s, v->reference);
    if (err) return err;
    err = cms_data_encode_stream(s, v->value);
    if (err) return err;
    return CMS_OK;
}

int cms_data_value_entry_decode_stream(per_stream_t *s, cms_data_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR;
    int err;
    err = cms_sub_reference_decode_stream(s, v->reference);
    if (err) return err;
    err = cms_data_decode_stream(s, v->value);
    if (err) return err;
    return CMS_OK;
}
