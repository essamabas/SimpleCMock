

/*******************************************************************************
*
*     Header Prologue
*
*******************************************************************************/
#ifndef GETRAG_CONTEXT_H
#define GETRAG_CONTEXT_H

/*******************************************************************************
*
*     Global Type Declarations (typedef)
*
*******************************************************************************/
/** Data types for Renesas */
typedef unsigned char   GT_U8;
typedef unsigned short  GT_U16;
typedef unsigned int    GT_U32;

typedef signed char     GT_S8;
typedef signed short    GT_S16;
typedef signed int      GT_S32;

typedef unsigned char   GT_BOOL;
typedef char            GT_CHAR;

/*******************************************************************************
*
*     Global Defines and Macros (#define)
*
*******************************************************************************/

/** min/max range of generic data types */
#define GT_U8_MAX       255U
#define GT_U8_MIN       0
#define GT_U16_MAX      65535U
#define GT_U16_MIN      0
#define GT_U32_MAX      4294967295U
#define GT_U32_MIN      0

#define GT_S8_MAX       127
#define GT_S8_MIN       (-127-1)
#define GT_S16_MAX      32767
#define GT_S16_MIN      (-32767-1)
#define GT_S32_MAX      2147483647
#define GT_S32_MIN      (-2147483647-1)


/* binary points */
#define GT_BIN_EN0              1
#define GT_BIN_EN1              2
#define GT_BIN_EN2              4
#define GT_BIN_EN3              8
#define GT_BIN_EN4             16
#define GT_BIN_EN5             32
#define GT_BIN_EN6             64
#define GT_BIN_EN7            128
#define GT_BIN_EN8            256
#define GT_BIN_EN9            512
#define GT_BIN_EN10          1024
#define GT_BIN_EN11          2048
#define GT_BIN_EN12          4096
#define GT_BIN_EN13          8192
#define GT_BIN_EN14         16384
#define GT_BIN_EN15         32768
#define GT_BIN_EN16         65536
#define GT_BIN_EN17        131072
#define GT_BIN_EN18        262144
#define GT_BIN_EN19        524288
#define GT_BIN_EN20       1048576
#define GT_BIN_EN21       2097152
#define GT_BIN_EN22       4194304
#define GT_BIN_EN23       8388608
#define GT_BIN_EN24      16777216
#define GT_BIN_EN25      33554432
#define GT_BIN_EN26      67108864
#define GT_BIN_EN27     134217728

#define GT_BIN_E1               0.5
#define GT_BIN_E2               0.25
#define GT_BIN_E3               0.125
#define GT_BIN_E4               0.0625
#define GT_BIN_E5               0.03125
#define GT_BIN_E6               0.015625
#define GT_BIN_E7               0.0078125
#define GT_BIN_E8               0.00390625


/** Define boolean value TRUE */
#define GT_TRUE   1U

/** Define boolean value FALSE */
#define GT_FALSE  0U

/** Null pointer */
#define GT_NULL     ((void *)0)

/*******************************************************************************
*
*     Global Constants Declarations (extern const)
*
*******************************************************************************/


/*******************************************************************************
*
*     Global Variables Declarations (extern)
*
*******************************************************************************/


/*******************************************************************************
*
*     Export Functions Declaration (extern)
*
*******************************************************************************/


/*******************************************************************************
*
*     Header Epilogue
*
*******************************************************************************/
#endif /* GETRAG_CONTEXT_H */
