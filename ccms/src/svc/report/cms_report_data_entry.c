#include "svc/report/cms_report_data_entry.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/block/cms_reason_code.h"

int cms_report_data_entry_encode_stream(per_stream_t *s, const cms_report_data_entry_t *v) {
    if (!v || !v->id || !v->value) return CMS_ERR; int err;

    /* reference — ObjectReference OPTIONAL */
    { int p = (v->ref_present && v->ref_present->value) && v->reference; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_object_reference_encode_stream(s,v->reference);if(err)return err;} }

    /* fc — FunctionalConstraint OPTIONAL */
    { int p = (v->fc_present && v->fc_present->value) && v->fc; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_functional_constraint_encode_stream(s,v->fc);if(err)return err;} }

    /* id — INT16U */
    err = cms_int16u_encode_stream(s, v->id); if (err) return err;

    /* value — Data */
    err = cms_data_encode_stream(s, v->value); if (err) return err;

    /* reason — ReasonCode OPTIONAL */
    { int p = (v->reason_present && v->reason_present->value) && v->reason; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_reason_code_encode_stream(s,v->reason);if(err)return err;} }

    return CMS_OK;
}

int cms_report_data_entry_decode_stream(per_stream_t *s, cms_report_data_entry_t *v) {
    if (!v || !v->id || !v->value) return CMS_ERR; int err;

    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->ref_present)v->ref_present->value=b.value; if(b.value&&v->reference){err=cms_object_reference_decode_stream(s,v->reference);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->fc_present)v->fc_present->value=b.value; if(b.value&&v->fc){err=cms_functional_constraint_decode_stream(s,v->fc);if(err)return err;} }

    err = cms_int16u_decode_stream(s, v->id); if (err) return err;
    err = cms_data_decode_stream(s, v->value); if (err) return err;

    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->reason_present)v->reason_present->value=b.value; if(b.value&&v->reason){err=cms_reason_code_decode_stream(s,v->reason);if(err)return err;} }

    return CMS_OK;
}
