#include "svc/report/cms_report_entry.h"
#include "svc/report/cms_report_data_entry.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

int cms_report_entry_encode_stream(per_stream_t *s, const cms_report_entry_t *v) {
    if (!v || !v->entry_data)
        return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (2 fields: timeOfEntry, entryID) */
    bool opt_present[2] = {(v->time_of_entry_present && v->time_of_entry_present->value) && v->time_of_entry,
                           (v->entry_id_present && v->entry_id_present->value) && v->entry_id};
    err = (int) per_encode_optional_bitmap(s, opt_present, 2);
    if (err)
        return err;

    /* 1. timeOfEntry — EntryTime OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_entry_time_encode_stream(s, v->time_of_entry);
        if (err)
            return err;
    }

    /* 2. entryID — EntryID OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_entry_id_encode_stream(s, v->entry_id);
        if (err)
            return err;
    }

    /* 3. entryData — SEQUENCE OF ReportDataEntry */
    {
        uint32_t cnt = (uint32_t) v->entry_data->count;
        per_error_t perr = per_encode_length(s, cnt);
        if (perr)
            return CMS_ERR;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_report_data_entry_t *e = (cms_report_data_entry_t *) v->entry_data->elements[i];
            if (!e)
                return CMS_ERR;
            err = cms_report_data_entry_encode_stream(s, e);
            if (err)
                return err;
        }
    }

    return CMS_OK;
}

int cms_report_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_report_entry_t *v = (cms_report_entry_t *) ptr;
    int retry_needed = 0;
    int err;

    /* 0. OPTIONAL bitmap (2 fields: timeOfEntry, entryID) */
    bool opt_present[2];
    err = (int) per_decode_optional_bitmap(s, opt_present, 2);
    if (err)
        return err;
    if (v) {
        if (v->time_of_entry_present)
            v->time_of_entry_present->value = opt_present[0];
        if (v->entry_id_present)
            v->entry_id_present->value = opt_present[1];
    }

    /* 1. timeOfEntry OPTIONAL */
    if (opt_present[0]) {
        if (v && !v->time_of_entry)
            return CMS_ERR;
        err = cms_entry_time_decode_stream(s, v ? v->time_of_entry : NULL);
        if (err)
            return err;
    }

    /* 2. entryID OPTIONAL */
    if (opt_present[1]) {
        if (v && !v->entry_id)
            return CMS_ERR;
        err = cms_entry_id_decode_stream(s, v ? v->entry_id : NULL);
        if (err)
            return err;
    }

    /* 3. entryData */
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(s, &cnt);
        if (perr)
            return CMS_ERR;
        cms_array_t *arr = v ? v->entry_data : NULL;
        if (v && !arr)
            return CMS_ERR;
        if (arr) {
            if (arr->count < (int32_t) cnt)
                retry_needed = 1;
            arr->count = (int32_t) cnt;
        }
        int inner_retry_needed = 0;
        for (uint32_t i = 0; i < cnt; i++) {
            err = cms_report_data_entry_decode_stream(s, (!arr || retry_needed) ? NULL : arr->elements[i]);
            if (err == CMS_RETRY)
                inner_retry_needed = 1;
            else if (err)
                return err;
        }
        if (inner_retry_needed)
            retry_needed = 1;
    }

    return retry_needed ? CMS_RETRY : CMS_OK;
}
