package com.ysh.jcms.core;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * jcms FFI — ccms dll bridge.
 *
 * Uses NativeLibrary + enum to eliminate repetitive Lib interface boilerplate.
 * Each {@link Codec} entry maps to a pair of C functions:
 * <pre>
 *   int cms_{name}_encode(Pointer v, byte[] outBuf, IntByReference outLen);
 *   int cms_{name}_decode(Pointer v, byte[] inBuf, int inLen);
 * </pre>
 *
 * Usage:
 * <pre>
 *   byte[] out = NativeBridge.Codec.ABORT.encode(structPtr);
 *   NativeBridge.Codec.ABORT.decode(structPtr, inData);
 * </pre>
 */
public class NativeBridge {

    private static final NativeLibrary LIB = NativeLibrary.getInstance("ccms");

    public enum Codec {
        ABORT("cms_abort"),
        ASSOCIATE_ERROR("cms_associate_error"),
        ASSOCIATE_REQUEST("cms_associate_request"),
        ASSOCIATE_RESPONSE("cms_associate_response"),
        ASSOCIATION_ID("cms_association_id"),
        BINARY_TIME("cms_binary_time"),
        BOOLEAN("cms_boolean"),
        BRCB("cms_brcb"),
        CANCEL_ERROR("cms_cancel_error"),
        CANCEL_REQUEST("cms_cancel_request"),
        CANCEL_RESPONSE("cms_cancel_response"),
        CHECK("cms_check"),
        COMMAND_TERMINATION("cms_command_termination"),
        CONFIRM_EDIT_SG_VALUES_ERROR("cms_confirm_edit_sg_values_error"),
        CONFIRM_EDIT_SG_VALUES_REQUEST("cms_confirm_edit_sg_values_request"),
        CONFIRM_EDIT_SG_VALUES_RESPONSE("cms_confirm_edit_sg_values_response"),
        CREATE_DATA_SET_ERROR("cms_create_data_set_error"),
        CREATE_DATA_SET_REQUEST("cms_create_data_set_request"),
        CREATE_DATA_SET_RESPONSE("cms_create_data_set_response"),
        DATA("cms_data"),
        DATA_DEFINITION("cms_data_definition"),
        DELETE_DATA_SET_ERROR("cms_delete_data_set_error"),
        DELETE_DATA_SET_REQUEST("cms_delete_data_set_request"),
        DELETE_DATA_SET_RESPONSE("cms_delete_data_set_response"),
        DELETE_FILE_ERROR("cms_delete_file_error"),
        DELETE_FILE_REQUEST("cms_delete_file_request"),
        DELETE_FILE_RESPONSE("cms_delete_file_response"),
        ENTRY_ID("cms_entry_id"),
        ENUMERATED("cms_enumerated"),
        FILE_ENTRY("cms_file_entry"),
        FLOAT32("cms_float32"),
        FLOAT64("cms_float64"),
        FUNCTIONAL_CONSTRAINT("cms_functional_constraint"),
        GET_ALL_CB_VALUES_ERROR("cms_get_all_cb_values_error"),
        GET_ALL_CB_VALUES_REQUEST("cms_get_all_cb_values_request"),
        GET_ALL_CB_VALUES_RESPONSE("cms_get_all_cb_values_response"),
        GET_ALL_DATA_DEFINITION_ERROR("cms_get_all_data_definition_error"),
        GET_ALL_DATA_DEFINITION_REQUEST("cms_get_all_data_definition_request"),
        GET_ALL_DATA_DEFINITION_RESPONSE("cms_get_all_data_definition_response"),
        GET_ALL_DATA_VALUES_ERROR("cms_get_all_data_values_error"),
        GET_ALL_DATA_VALUES_REQUEST("cms_get_all_data_values_request"),
        GET_ALL_DATA_VALUES_RESPONSE("cms_get_all_data_values_response"),
        GET_BRCB_VALUES_ERROR("cms_get_brcb_values_error"),
        GET_BRCB_VALUES_REQUEST("cms_get_brcb_values_request"),
        GET_BRCB_VALUES_RESPONSE("cms_get_brcb_values_response"),
        GET_DATA_DEFINITION_ERROR("cms_get_data_definition_error"),
        GET_DATA_DEFINITION_REQUEST("cms_get_data_definition_request"),
        GET_DATA_DEFINITION_RESPONSE("cms_get_data_definition_response"),
        GET_DATA_DIRECTORY_ERROR("cms_get_data_directory_error"),
        GET_DATA_DIRECTORY_REQUEST("cms_get_data_directory_request"),
        GET_DATA_DIRECTORY_RESPONSE("cms_get_data_directory_response"),
        GET_DATA_SET_DIRECTORY_ERROR("cms_get_data_set_directory_error"),
        GET_DATA_SET_DIRECTORY_REQUEST("cms_get_data_set_directory_request"),
        GET_DATA_SET_DIRECTORY_RESPONSE("cms_get_data_set_directory_response"),
        GET_DATA_SET_VALUES_ERROR("cms_get_data_set_values_error"),
        GET_DATA_SET_VALUES_REQUEST("cms_get_data_set_values_request"),
        GET_DATA_SET_VALUES_RESPONSE("cms_get_data_set_values_response"),
        GET_DATA_VALUES_ERROR("cms_get_data_values_error"),
        GET_DATA_VALUES_REQUEST("cms_get_data_values_request"),
        GET_DATA_VALUES_RESPONSE("cms_get_data_values_response"),
        GET_EDIT_SG_VALUE_ERROR("cms_get_edit_sg_value_error"),
        GET_EDIT_SG_VALUE_REQUEST("cms_get_edit_sg_value_request"),
        GET_EDIT_SG_VALUE_RESPONSE("cms_get_edit_sg_value_response"),
        GET_FILE_ATTRIBUTE_VALUES_ERROR("cms_get_file_attribute_values_error"),
        GET_FILE_ATTRIBUTE_VALUES_REQUEST("cms_get_file_attribute_values_request"),
        GET_FILE_ATTRIBUTE_VALUES_RESPONSE("cms_get_file_attribute_values_response"),
        GET_FILE_DIRECTORY_ERROR("cms_get_file_directory_error"),
        GET_FILE_DIRECTORY_REQUEST("cms_get_file_directory_request"),
        GET_FILE_DIRECTORY_RESPONSE("cms_get_file_directory_response"),
        GET_FILE_ERROR("cms_get_file_error"),
        GET_FILE_REQUEST("cms_get_file_request"),
        GET_FILE_RESPONSE("cms_get_file_response"),
        GET_GO_CB_VALUES_ERROR("cms_get_go_cb_values_error"),
        GET_GO_CB_VALUES_REQUEST("cms_get_go_cb_values_request"),
        GET_GO_CB_VALUES_RESPONSE("cms_get_go_cb_values_response"),
        GET_GO_REFERENCE_ERROR("cms_get_go_reference_error"),
        GET_GO_REFERENCE_REQUEST("cms_get_go_reference_request"),
        GET_GO_REFERENCE_RESPONSE("cms_get_go_reference_response"),
        GET_GOOSE_ELEMENT_NUMBER_ERROR("cms_get_goose_element_number_error"),
        GET_GOOSE_ELEMENT_NUMBER_REQUEST("cms_get_goose_element_number_request"),
        GET_GOOSE_ELEMENT_NUMBER_RESPONSE("cms_get_goose_element_number_response"),
        GET_LCB_VALUES_ERROR("cms_get_lcb_values_error"),
        GET_LCB_VALUES_REQUEST("cms_get_lcb_values_request"),
        GET_LCB_VALUES_RESPONSE("cms_get_lcb_values_response"),
        GET_LOG_STATUS_VALUES_ERROR("cms_get_log_status_values_error"),
        GET_LOG_STATUS_VALUES_REQUEST("cms_get_log_status_values_request"),
        GET_LOG_STATUS_VALUES_RESPONSE("cms_get_log_status_values_response"),
        GET_LOGICAL_DEVICE_DIRECTORY_ERROR("cms_get_logical_device_directory_error"),
        GET_LOGICAL_DEVICE_DIRECTORY_REQUEST("cms_get_logical_device_directory_request"),
        GET_LOGICAL_DEVICE_DIRECTORY_RESPONSE("cms_get_logical_device_directory_response"),
        GET_LOGICAL_NODE_DIRECTORY_ERROR("cms_get_logical_node_directory_error"),
        GET_LOGICAL_NODE_DIRECTORY_REQUEST("cms_get_logical_node_directory_request"),
        GET_LOGICAL_NODE_DIRECTORY_RESPONSE("cms_get_logical_node_directory_response"),
        GET_MSVCB_VALUES_ERROR("cms_get_msvcb_values_error"),
        GET_MSVCB_VALUES_REQUEST("cms_get_msvcb_values_request"),
        GET_MSVCB_VALUES_RESPONSE("cms_get_msvcb_values_response"),
        GET_RPC_INTERFACE_DEFINITION_ERROR("cms_get_rpc_interface_definition_error"),
        GET_RPC_INTERFACE_DEFINITION_REQUEST("cms_get_rpc_interface_definition_request"),
        GET_RPC_INTERFACE_DEFINITION_RESPONSE("cms_get_rpc_interface_definition_response"),
        GET_RPC_INTERFACE_DIRECTORY_ERROR("cms_get_rpc_interface_directory_error"),
        GET_RPC_INTERFACE_DIRECTORY_REQUEST("cms_get_rpc_interface_directory_request"),
        GET_RPC_INTERFACE_DIRECTORY_RESPONSE("cms_get_rpc_interface_directory_response"),
        GET_RPC_METHOD_DEFINITION_ERROR("cms_get_rpc_method_definition_error"),
        GET_RPC_METHOD_DEFINITION_REQUEST("cms_get_rpc_method_definition_request"),
        GET_RPC_METHOD_DEFINITION_RESPONSE("cms_get_rpc_method_definition_response"),
        GET_RPC_METHOD_DIRECTORY_ERROR("cms_get_rpc_method_directory_error"),
        GET_RPC_METHOD_DIRECTORY_REQUEST("cms_get_rpc_method_directory_request"),
        GET_RPC_METHOD_DIRECTORY_RESPONSE("cms_get_rpc_method_directory_response"),
        GET_SERVER_DIRECTORY_ERROR("cms_get_server_directory_error"),
        GET_SERVER_DIRECTORY_REQUEST("cms_get_server_directory_request"),
        GET_SERVER_DIRECTORY_RESPONSE("cms_get_server_directory_response"),
        GET_SGCB_VALUES_ERROR("cms_get_sgcb_values_error"),
        GET_SGCB_VALUES_REQUEST("cms_get_sgcb_values_request"),
        GET_SGCB_VALUES_RESPONSE("cms_get_sgcb_values_response"),
        GET_URCB_VALUES_ERROR("cms_get_urcb_values_error"),
        GET_URCB_VALUES_REQUEST("cms_get_urcb_values_request"),
        GET_URCB_VALUES_RESPONSE("cms_get_urcb_values_response"),
        GO_CB("cms_go_cb"),
        INT16("cms_int16"),
        INT16U("cms_int16u"),
        INT24U("cms_int24u"),
        INT32("cms_int32"),
        INT32U("cms_int32u"),
        INT64("cms_int64"),
        INT64U("cms_int64u"),
        INT8("cms_int8"),
        INT8U("cms_int8u"),
        LCB("cms_lcb"),
        LCB_OPT_FLDS("cms_lcb_opt_flds"),
        MSVCB("cms_msvcb"),
        MSVCB_OPT_FLDS("cms_msvcb_opt_flds"),
        NEGOTIATE_ERROR("cms_negotiate_error"),
        NEGOTIATE_REQUEST("cms_negotiate_request"),
        NEGOTIATE_RESPONSE("cms_negotiate_response"),
        OBJECT_NAME("cms_object_name"),
        OBJECT_REFERENCE("cms_object_reference"),
        OPERATE_ERROR("cms_operate_error"),
        OPERATE_REQUEST("cms_operate_request"),
        OPERATE_RESPONSE("cms_operate_response"),
        ORIGINATOR("cms_originator"),
        PHY_COM_ADDR("cms_phy_com_addr"),
        QUALITY("cms_quality"),
        QUERY_LOG_AFTER_ERROR("cms_query_log_after_error"),
        QUERY_LOG_AFTER_REQUEST("cms_query_log_after_request"),
        QUERY_LOG_AFTER_RESPONSE("cms_query_log_after_response"),
        QUERY_LOG_BY_TIME_ERROR("cms_query_log_by_time_error"),
        QUERY_LOG_BY_TIME_REQUEST("cms_query_log_by_time_request"),
        QUERY_LOG_BY_TIME_RESPONSE("cms_query_log_by_time_response"),
        RCB_OPT_FLDS("cms_rcb_opt_flds"),
        REASON_CODE("cms_reason_code"),
        RELEASE_ERROR("cms_release_error"),
        RELEASE_REQUEST("cms_release_request"),
        RELEASE_RESPONSE("cms_release_response"),
        REPORT("cms_report"),
        RPC_CALL_ERROR("cms_rpc_call_error"),
        RPC_CALL_REQUEST("cms_rpc_call_request"),
        RPC_CALL_RESPONSE("cms_rpc_call_response"),
        SELECT_ACTIVE_SG_ERROR("cms_select_active_sg_error"),
        SELECT_ACTIVE_SG_REQUEST("cms_select_active_sg_request"),
        SELECT_ACTIVE_SG_RESPONSE("cms_select_active_sg_response"),
        SELECT_EDIT_SG_ERROR("cms_select_edit_sg_error"),
        SELECT_EDIT_SG_REQUEST("cms_select_edit_sg_request"),
        SELECT_EDIT_SG_RESPONSE("cms_select_edit_sg_response"),
        SELECT_ERROR("cms_select_error"),
        SELECT_REQUEST("cms_select_request"),
        SELECT_RESPONSE("cms_select_response"),
        SELECT_WITH_VALUE_ERROR("cms_select_with_value_error"),
        SELECT_WITH_VALUE_REQUEST("cms_select_with_value_request"),
        SELECT_WITH_VALUE_RESPONSE("cms_select_with_value_response"),
        SEND_GOOSE_MESSAGE("cms_send_goose_message"),
        SEND_MSV_MESSAGE("cms_send_msv_message"),
        SET_BRCB_VALUES_ERROR("cms_set_brcb_values_error"),
        SET_BRCB_VALUES_REQUEST("cms_set_brcb_values_request"),
        SET_BRCB_VALUES_RESPONSE("cms_set_brcb_values_response"),
        SET_DATA_SET_VALUES_ERROR("cms_set_data_set_values_error"),
        SET_DATA_SET_VALUES_REQUEST("cms_set_data_set_values_request"),
        SET_DATA_SET_VALUES_RESPONSE("cms_set_data_set_values_response"),
        SET_DATA_VALUES_ERROR("cms_set_data_values_error"),
        SET_DATA_VALUES_REQUEST("cms_set_data_values_request"),
        SET_DATA_VALUES_RESPONSE("cms_set_data_values_response"),
        SET_EDIT_SG_VALUE_ERROR("cms_set_edit_sg_value_error"),
        SET_EDIT_SG_VALUE_REQUEST("cms_set_edit_sg_value_request"),
        SET_EDIT_SG_VALUE_RESPONSE("cms_set_edit_sg_value_response"),
        SET_FILE_ERROR("cms_set_file_error"),
        SET_FILE_REQUEST("cms_set_file_request"),
        SET_FILE_RESPONSE("cms_set_file_response"),
        SET_GO_CB_VALUES_ERROR("cms_set_go_cb_values_error"),
        SET_GO_CB_VALUES_REQUEST("cms_set_go_cb_values_request"),
        SET_GO_CB_VALUES_RESPONSE("cms_set_go_cb_values_response"),
        SET_LCB_VALUES_ERROR("cms_set_lcb_values_error"),
        SET_LCB_VALUES_REQUEST("cms_set_lcb_values_request"),
        SET_LCB_VALUES_RESPONSE("cms_set_lcb_values_response"),
        SET_MSVCB_VALUES_ERROR("cms_set_msvcb_values_error"),
        SET_MSVCB_VALUES_REQUEST("cms_set_msvcb_values_request"),
        SET_MSVCB_VALUES_RESPONSE("cms_set_msvcb_values_response"),
        SET_URCB_VALUES_ERROR("cms_set_urcb_values_error"),
        SET_URCB_VALUES_REQUEST("cms_set_urcb_values_request"),
        SET_URCB_VALUES_RESPONSE("cms_set_urcb_values_response"),
        SGCB("cms_sgcb"),
        SUB_REFERENCE("cms_sub_reference"),
        TEST_ERROR("cms_test_error"),
        TEST_REQUEST("cms_test_request"),
        TEST_RESPONSE("cms_test_response"),
        TIME_ACTIVATED_OPERATE_ERROR("cms_time_activated_operate_error"),
        TIME_ACTIVATED_OPERATE_REQUEST("cms_time_activated_operate_request"),
        TIME_ACTIVATED_OPERATE_RESPONSE("cms_time_activated_operate_response"),
        TIME_ACTIVATED_OPERATE_TERMINATION("cms_time_activated_operate_termination"),
        TIME_QUALITY("cms_time_quality"),
        TRIGGER_CONDITIONS("cms_trigger_conditions"),
        URCB("cms_urcb"),
        UTC_TIME("cms_utc_time"),
        ;

        private final String prefix;

        Codec(String prefix) { this.prefix = prefix; }

        public byte[] encode(Pointer ptr) {
            Function fn = LIB.getFunction(prefix + "_encode");
            PointerByReference outBuf = new PointerByReference();
            LongByReference outLen = new LongByReference();
            int rc = fn.invokeInt(new Object[]{ptr, outBuf, outLen});
            if (rc != 0) throw new RuntimeException(
                "encode failed: rc=" + rc + " for " + prefix);
            Pointer buf = outBuf.getValue();
            long len = outLen.getValue();
            byte[] result = buf.getByteArray(0, (int) len);
            Native.free(Pointer.nativeValue(buf));
            return result;
        }

        public void decode(Pointer ptr, byte[] data) {
            Function fn = LIB.getFunction(prefix + "_decode");
            int rc = fn.invokeInt(new Object[]{ptr, data, data.length});
            if (rc != 0) throw new RuntimeException(
                "decode failed: rc=" + rc + " for " + prefix);
        }
    }
}
