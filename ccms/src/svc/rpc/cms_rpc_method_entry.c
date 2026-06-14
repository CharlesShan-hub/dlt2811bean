#include "svc/rpc/cms_rpc_method_entry.h"
#include "data/choice/cms_data_definition.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"

int cms_rpc_method_entry_encode_stream(per_stream_t*s,const cms_rpc_method_entry_t*v){
    if(!v||!v->name||!v->version||!v->timeout||!v->request||!v->response)return CMS_ERR;int err;
    err=cms_visible_string_encode_stream(s,v->name,UINT32_MAX);if(err)return err;
    err=cms_int32u_encode_stream(s,v->version);if(err)return err;
    err=cms_int32u_encode_stream(s,v->timeout);if(err)return err;
    err=cms_data_definition_encode_stream(s,v->request);if(err)return err;
    err=cms_data_definition_encode_stream(s,v->response);if(err)return err;
    return CMS_OK;
}
int cms_rpc_method_entry_decode_stream(per_stream_t*s,cms_rpc_method_entry_t*v){
    if(!v||!v->name||!v->version||!v->timeout||!v->request||!v->response)return CMS_ERR;int err;
    err=cms_visible_string_decode_stream(s,v->name,UINT32_MAX);if(err)return err;
    err=cms_int32u_decode_stream(s,v->version);if(err)return err;
    err=cms_int32u_decode_stream(s,v->timeout);if(err)return err;
    err=cms_data_definition_decode_stream(s,v->request);if(err)return err;
    err=cms_data_definition_decode_stream(s,v->response);if(err)return err;
    return CMS_OK;
}
