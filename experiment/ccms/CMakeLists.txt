cmake_minimum_required(VERSION 3.14)
project(ccms VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

# --- PER encoding/decoding primitives library ---
file(GLOB LIB_SRC src/per/cms_*.c)
add_library(ccms_core STATIC ${LIB_SRC})

target_include_directories(ccms_core
    PUBLIC  $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
)

# --- FFI library (hand-written PER codec for DL/T 2811 data types and APDU) ---
add_library(ccms SHARED
    src/svc/connect/cms_associate.c
    src/svc/connect/cms_release.c
    src/svc/connect/cms_abort.c
    src/svc/other/cms_apdu.c
    src/data/basic/cms_boolean.c
    src/data/basic/cms_integer.c
    src/data/basic/cms_float.c
    src/data/basic/cms_string.c
    src/data/basic/cms_packed_list.c
    src/data/extended/cms_time.c
    src/data/common/cms_object_name.c
    src/data/common/cms_time_stamp.c
    src/data/common/cms_phy_com_addr.c
    src/data/common/cms_quality.c
    src/data/common/cms_file_entry.c
    src/data/fc/cms_fc.c
    src/data/control/cms_originator.c
    src/data/control/cms_check.c
    src/data/control/cms_add_cause.c
    src/data/block/cms_opt_flds.c
    src/data/block/cms_reason_code.c
    src/data/block/cms_trigger_conditions.c
    src/data/block/cms_smp_mod.c
    src/data/choice/cms_data.c
    src/data/choice/cms_data_definition.c
)
target_include_directories(ccms
    PUBLIC  $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
)
target_link_libraries(ccms
    PUBLIC  ccms_core
)
if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_C_COMPILER_ID MATCHES "Clang")
    target_compile_options(ccms PRIVATE -fvisibility=hidden)
endif()
set_target_properties(ccms PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
)

option(BUILD_TESTS "Build tests" ON)
if(BUILD_TESTS)
    enable_testing()
    add_subdirectory(tests)
endif()
