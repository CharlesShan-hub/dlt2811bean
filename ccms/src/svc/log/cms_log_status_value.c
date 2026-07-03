#include "svc/log/cms_log_status_value.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"

int cms_log_status_value_encode_stream(per_stream_t *s, const cms_log_status_value_t *v) {
    if (!v || !v->old_entr_tm || !v->new_entr_tm || !v->old_entr || !v->new_entr)
        return CMS_ERR;
    int err;

    err = cms_entry_time_encode_stream(s, v->old_entr_tm);
    if (err)
        return err;

    err = cms_entry_time_encode_stream(s, v->new_entr_tm);
    if (err)
        return err;

    err = cms_entry_id_encode_stream(s, v->old_entr);
    if (err)
        return err;

    err = cms_entry_id_encode_stream(s, v->new_entr);
    if (err)
        return err;

    return CMS_OK;
}

int cms_log_status_value_decode_stream(per_stream_t *s, void *ptr) {
    cms_log_status_value_t *v = (cms_log_status_value_t *) ptr;
    int err;

    if (v && (!v->old_entr_tm || !v->new_entr_tm || !v->old_entr || !v->new_entr))
        return CMS_ERR;
    err = cms_entry_time_decode_stream(s, v ? v->old_entr_tm : NULL);
    if (err)
        return err;

    err = cms_entry_time_decode_stream(s, v ? v->new_entr_tm : NULL);
    if (err)
        return err;

    err = cms_entry_id_decode_stream(s, v ? v->old_entr : NULL);
    if (err)
        return err;

    err = cms_entry_id_decode_stream(s, v ? v->new_entr : NULL);
    if (err)
        return err;

    return CMS_OK;
}
