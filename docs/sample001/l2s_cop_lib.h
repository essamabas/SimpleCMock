/********************************************************************************
**
**  CONFIDENTIAL - GETRAG Getriebe- und Zahnradfabrik
**                 Hermann Hagenmeyer GmbH & Cie KG
** 
**  this is an unpublished work, which is a trade secret, created in 2013.      
**  GETRAG Getriebe- und Zahnradfabrik Hermann Hagenmeyer GmbH & Cie KG
**  owns all rights to this work and intends to maintain it in confidence
**  to preserve its trade secret status. GETRAG Getriebe- und Zahnradfabrik
**  Hermann Hagenmeyer GmbH & Cie KG reserves the right to protect this work       
**  as an unpublished copyrighted work in the event of an inadvertent or      
**  deliberate unauthorized publication. GETRAG Getriebe- und Zahnradfabrik
**  Hermann Hagenmeyer GmbH & Cie KG also reserves its rights under the copyright
**  laws to protect this work as a published work. Those having access to this
**  work may not copy it, use it or disclose the information contained in it
**  without the written authorization of GETRAG Getriebe- und Zahnradfabrik
**  Hermann Hagenmeyer GmbH & Cie KG.
**
*******************************************************************************/

/***************************************************************************//**
*
*     \file            l2s_cop_lib.h
*
*     \author          Tim Weyres
*
*     \date            03.06.2013
*
*     \b Compiler:     Renesas V9.04
*
*     \b Description:  Level 2 software library functions for Core Platform
*
*     <b> Modification History:   </b>
*
*      Date        |    Name        |                                         \n
*    --------------+----------------+-----------------------------------------\n
*    11. May 2015  | H.Schwindt     | -merge branch version; TASK-915676      \n
*    --------------+----------------+-----------------------------------------\n
*     8. Apr 2015  | Guido Dickers  | -eleminate MISRA warnings; TASK-915656  \n
*    --------------+----------------+-----------------------------------------\n
*    10. Mar 2015  | Tim Weyres     | -add function l2s_lib_s16_sat           \n
*                  |                | -remove functions l2s_add_circular()    \n
*                  |                |  and l2s_init_circular()                \n
*    --------------+----------------+-----------------------------------------\n
*     7 Nov 2013   | Guido Dickers  | -introduce first order low pass filter  \n
*    --------------+----------------+-----------------------------------------\n
*     9 Jul 2013   | Guido Dickers  | -introduce signed subtraction with      \n
*                  |                |  saturation of S16                      \n
*                  |                | -introduce abs with saturation of S16   \n
*    --------------+----------------+-----------------------------------------\n
*     8 Jul 2013   | Guido Dickers  | -introduce signed addition with         \n
*                  |                |  saturation of S16                      \n
*                  |                | -introduce unsigned addition with       \n
*                  |                |  saturation of U16                      \n
*                  |                | -introduce unsigned subtraction         \n
*                  |                |  with saturation of U16                 \n
*    --------------+----------------+-----------------------------------------\n
*     5 Jul 2013   | Guido Dickers  | -modify breakpoint index search         \n
*                  |                | -extend naming of interpolation         \n
*                  |                |  functions                              \n
*    --------------+----------------+-----------------------------------------\n
*     4. Jul 2012  | Guido Dickers  | -remove prototype for geting gear       \n
*                  |                |  ratio function                         \n 
*    --------------+----------------+-----------------------------------------\n
*     3. Jul 2013  | Guido Dickers  | -introduce prototype for geting gear    \n
*                  |                |  ratio function                         \n
*    --------------+----------------+-----------------------------------------\n
*    21. Jun 2013  | Guido Dickers  | -introduce two dimensional interpolation\n
*                  |                |  function                               \n
*                  |                | -introduce pure breakpoint search       \n
*    --------------+----------------+-----------------------------------------\n
*    03.06.2013    | Tim Weyres     | initial revision                        \n
*    --------------+----------------+-----------------------------------------\n
*
*
*******************************************************************************/

/*******************************************************************************
*
*     Header Prologue
*
*******************************************************************************/
#ifndef L2S_COP_LIB_H
#define L2S_COP_LIB_H

/*******************************************************************************
*
*     Global Type Declarations (typedef)
*
*******************************************************************************/
/**Define the breakpoint search routine return structure*/
typedef struct{
               /**index of breakpoint below input [S8 | - | - ]*/
               GT_S8  idx;
               
               /*intervall ratio in input direction [ S16 | En14 | - ]*/       
               GT_S16 ival_ratio; 
               } l2s_lib_ipol_brkpt_type;



/**Define a basic 2 dimensional table look up structure with a real x input
  and a calculated y output                                                 \n
  y = function(x) by linear interpolation*/
typedef struct 
      {
       /**address of function output list[] with Y values */
       const volatile GT_S16  *y_list;
       
       /**address of X breakpoint list[]*/    
       const volatile GT_S16  *x_list;
       
       /**number of breakpoints [S8 | - | - ]*/    
       GT_S8   no_brk_pts; 
      } l2s_lib_ipol_dim2_type;
      
/*******************************************************************************
*
*     Global Defines and Macros (#define)
*
*******************************************************************************/


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
extern GT_S16 l2s_lib_abs_s16_sat(GT_S16 in_value_abs_sat);
extern GT_S16 l2s_lib_s16_sat(GT_S32 in_value);
extern GT_S16 l2s_lib_s16_add_s16_s16_sat(GT_S16 in_value1, GT_S16 in_value2);
extern GT_S32 l2s_lib_s32_add_s32_s32_sat(GT_S32 in_value1, GT_S32 in_value2);
extern GT_S16 l2s_lib_s16_sub_s16_s16_sat(GT_S16 in_value1, GT_S16 in_value2);
extern GT_U16 l2s_lib_u16_add_u16_u16_sat(GT_U16 in_value1, GT_U16 in_value2);
extern GT_U16 l2s_lib_u16_sub_u16_u16_sat(GT_U16 in_value1, GT_U16 in_value2);
extern GT_U32 l2s_lib_div_u32u32(GT_U32 z, GT_U32 n);
extern GT_S32 l2s_lib_div_s32s32(GT_S32 z, GT_S32 n);
extern GT_S32 l2s_abs_s32(GT_S32 i);
extern GT_U8 l2s_lib_idx_brkpt_srch(const volatile GT_S16 *x_list, GT_S8 nr, GT_S16 x_in);
extern l2s_lib_ipol_brkpt_type l2s_lib_ipol_brkpt_srch(const volatile GT_S16 *x_list, GT_S8 nr, GT_S16 x_in);
extern GT_S16 l2s_lib_ipol_dim2_S16_S16(l2s_lib_ipol_dim2_type *ptr_dim2, GT_S16 x_in);
extern GT_S16 l2s_lib_rolav_S16_S16(GT_S16 input, GT_S16 old_result, GT_S16 filt_coef);
extern GT_S16 l2s_lib_mul_s16_s16_sr13_sat(GT_S16 in_value1, GT_S16 in_value2);
extern GT_U8 l2s_calc_crc8(GT_U8* start, GT_U32 size);
extern GT_U16 l2s_calc_crc16(GT_U8* start, GT_U32 size);
extern GT_S16 l2s_lib_maxS16S16S16(GT_S16 a, GT_S16 b);


/*******************************************************************************
*
*     Header Epilogue
*
*******************************************************************************/
#endif /* L2S_COP_LIB_H */
