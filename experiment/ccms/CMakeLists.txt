cmake_minimum_required(VERSION 3.14)
project(ccms VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED ON)

# --- PER encoding/decoding primitives library ---
file(GLOB LIB_SRC src/per_*.c)
add_library(ccms_core STATIC ${LIB_SRC})

target_include_directories(ccms_core
    PUBLIC  $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
)

# --- FFI library (hand-written PER codec for DL/T 2811 data types and APDU) ---
add_library(ccms SHARED
    src/cms_ffi.c
    src/cms_ffi_datatypes.c
    src/cms_apdu.c
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
