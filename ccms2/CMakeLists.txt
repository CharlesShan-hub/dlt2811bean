cmake_minimum_required(VERSION 3.14)
project(ccms2 VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

set(CMS2_SRC
    src/per/cms_stream.c
    src/per/cms_integer.c
    src/per/cms_string.c
    src/per/cms_choice.c

    # scalar types (leaf, inline value)
    src/data/scalar/cms_boolean.c
    src/data/scalar/cms_int8.c
    src/data/scalar/cms_int8u.c
    src/data/scalar/cms_int16.c
    src/data/scalar/cms_int16u.c
    src/data/scalar/cms_int24u.c
    src/data/scalar/cms_int32.c
    src/data/scalar/cms_int32u.c
    src/data/scalar/cms_int64.c
    src/data/scalar/cms_int64u.c
    src/data/scalar/cms_float32.c
    src/data/scalar/cms_float64.c

    # time types
    src/data/time/cms_time_quality.c
    src/data/time/cms_utc_time.c
    src/data/time/cms_binary_time.c

    # string types
    src/data/string/cms_octet_string.c
    src/data/string/cms_visible_string.c
    src/data/string/cms_utf8_string.c
    src/data/string/cms_bit_string.c

    # common types
    src/data/common/cms_quality.c
    src/data/common/cms_object_name.c
    src/data/common/cms_object_reference.c
    src/data/common/cms_sub_reference.c
    src/data/common/cms_dbpos.c
    src/data/common/cms_tcmd.c
    src/data/common/cms_time_stamp.c
    src/data/common/cms_entry_id.c
    src/data/common/cms_entry_time.c
    src/data/common/cms_file_entry.c
    src/data/common/cms_service_error.c
    src/data/common/cms_phy_com_addr.c

    # enum types
    src/data/enum/cms_enumerated.c

    # fc types
    src/data/fc/cms_functional_constraint.c

    # control types
    src/data/control/cms_or_cat.c
    src/data/control/cms_originator.c
    src/data/control/cms_check.c
    src/data/control/cms_add_cause.c

    # block types
    src/data/block/cms_trigger_conditions.c
    src/data/block/cms_reason_code.c
    src/data/block/cms_rcb_opt_flds.c
    src/data/block/cms_lcb_opt_flds.c
    src/data/block/cms_msvcb_opt_flds.c
    src/data/block/cms_smp_mod.c
    src/data/block/cms_sgcb.c
    src/data/block/cms_brcb.c
    src/data/block/cms_urcb.c
    src/data/block/cms_lcb.c
    src/data/block/cms_go_cb.c
    src/data/block/cms_msvcb.c

    # choice types
    src/data/choice/cms_data.c
    src/data/choice/cms_data_definition.c
    src/data/choice/cms_data_definition_array.c
)

add_library(ccms2 STATIC ${CMS2_SRC})
target_include_directories(ccms2 PUBLIC include)

# Test
add_executable(test_scalar tests/test_scalar.c)
target_link_libraries(test_scalar ccms2)
target_include_directories(test_scalar PRIVATE include)
