/********************************************************************************
**
**  CONFIDENTIAL - GETRAG Getriebe- und Zahnradfabrik
**                 Hermann Hagenmeyer GmbH & Cie KG
** 
**  This is an unpublished work, which is a trade secret, created in 2013.      
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
#include "getrag_context.h"
#include "l2s_cop.h"
#include "l2s_cop_out_spd.h"
#include "l2s_cop.h"
#include "l2s_cop_drv_intrf.h"
#include "l2s_trp.h"
#include "l2s_trp_lib.h"
#include "l2s_vep.h"
#include "l2s_vep_inp.h"
#include "l2s_cop_cal.h"
#include "l2s_cop_gear.h"
#include "l2s_cop_gbx_tq.h"
#include "l2s_trp_gbx_tq.h"
#include "l2s_trp_cal.h"
#include "l2s_sz002_lnc_wd.h"


/***************************************************************************//**
*
*     \file            l2s_sz001_inc_tq.c
*
*     \author          Frank Messicci
*
*     \date            21.06.2013
*
*     \b Compiler:     Renesas V9.04
*
*     \b Description:  Level 2 software core platform component
*                      global safety function 001
*                      Prevent unwanted engine torque increase
*
*******************************************************************************/
/******************************************************************************
*
* $ProjectName: d:/MKS/Repository/GCG_PS_VP_150_DF/L2S_CORE.pj $
*
* $ProjectRevision: 1.33 $
*
* --------------------------------------------------------------------------
* Modification History:
* ---------------+--------------------+-----------------------------------------
*  21.06.2013    | Frank Messicci     | creation
* ---------------+--------------------+-----------------------------------------
*******************************************************************************/

/*******************************************************************************
*
*     Include Files
*
*******************************************************************************/

/*******************************************************************************
*
*     Global Constants (const)
*
*******************************************************************************/


/*******************************************************************************
*
*     Global Variables
*
*******************************************************************************/
/**flags indicating violation of SZ002*/
GT_BOOL l2s_violation_sz002[L2S_NUM_TRNS_PATH];

/*******************************************************************************
*
*     Export Functions
*
*******************************************************************************/


/*******************************************************************************
*
*     Internal Defines and Macros (#define)
*
*******************************************************************************/
#define VEHICLE_VEL_LNC l2s_ptr_cop_cal->shared.l2s_c_veh_vel_lnc
#define ENG_SPD_OFF l2s_ptr_cop_cal->sz002.l2s_c_eng_spd_off

/*******************************************************************************
*
*     Internal Constants (static const)
*
*******************************************************************************/

/*******************************************************************************
*
*     Internal Variables (static)
*
*******************************************************************************/
/******************************************************************************
*
*     Internal Functions (static)
*
*******************************************************************************/
/*******************************************************************************
*
*     Internal Variables
*
*******************************************************************************/

/***************************************************************************//** 
  \brief  <b>SZ_002</b>
  
  <b>Component Functionality</b>\n
    This function prevents unwanted launch in wrong direction. Each gearbox is
    monitored against this violation

  \req SC_93
  \n DOORS Baseline 4.10

  <b>Control Flow:</b> 

  <b>Calibration Parameters:</b>\n

  <b>Inputs:</b>\n

  <b>Outputs:</b>\n

  \param  none
  \return none

*******************************************************************************/
void l2s_sz002_call(void)
{
    GT_S16 tmp_ratio;
    
    /** -A- */
    if(( l2s_cop_out_spd.max_axle_speed.val < VEHICLE_VEL_LNC
      || l2s_cop_out_spd.max_axle_speed.err)
     &&
       ( l2s_vep_inp.eng_spd.err
      || l2s_vep_inp.eng_spd.val > ENG_SPD_OFF) 
      )
    {
        /** -B- */
        if(( l2s_cop_drv_intrf.lever_pos.err)
        && (!l2s_cop_drv_intrf.lever_limp_home_flg)
          )
        {
            l2s_violation_sz002[L2S_WOR_TRNS_IDX] = GT_TRUE;
            l2s_violation_sz002[L2S_WR_TRNS_IDX] = GT_TRUE;        
        }
        else
        {
            /** -C- */
            if(!l2s_cop_drv_intrf.lever_pos.err)
            {
                switch(l2s_cop_drv_intrf.lever_pos.val)
                {
                    case L2S_SHIFTER_POS_R:
                    {
                        /** violations for gearbox without R gear */
                        if( (l2s_cop_gear.gear_tq_able[L2S_WOR_TRNS_IDX] != L2S_GEAR_2ND_R
                          ||(l2s_cop_gear.gear_act[L2S_WOR_TRNS_IDX].err) )
                         &&
                            (l2s_cop_gbx_tq.clu[L2S_WOR_TRNS_IDX].tq.val > l2s_trp_gbx_tq.prot.clu[L2S_WOR_TRNS_IDX].tq_thres_sz002
                          || l2s_cop_gbx_tq.clu[L2S_WOR_TRNS_IDX].tq.err)
                          )
                        {
                            l2s_violation_sz002[L2S_WOR_TRNS_IDX] = GT_TRUE;
                        }
                        else
                        {
                            l2s_violation_sz002[L2S_WOR_TRNS_IDX] = GT_FALSE;
                        }

                        /** violations for gearbox with R gear */
                        if( (l2s_cop_gear.gear_tq_able[L2S_WR_TRNS_IDX] != L2S_GEAR_R
                          ||(l2s_cop_gear.gear_act[L2S_WR_TRNS_IDX].err) )
                         &&
                            (l2s_cop_gbx_tq.clu[L2S_WR_TRNS_IDX].tq.val > l2s_trp_gbx_tq.prot.clu[L2S_WR_TRNS_IDX].tq_thres_sz002
                          || l2s_cop_gbx_tq.clu[L2S_WR_TRNS_IDX].tq.err)
                          )
                        {
                            l2s_violation_sz002[L2S_WR_TRNS_IDX] = GT_TRUE;
                        }
                        else
                        {
                            l2s_violation_sz002[L2S_WR_TRNS_IDX] = GT_FALSE;
                        }
                    }
                    break;
                    
                    case L2S_SHIFTER_POS_D:
                    case L2S_SHIFTER_POS_M:
                    case L2S_SHIFTER_POS_M_UP:
                    case L2S_SHIFTER_POS_M_DOWN:
                    {
                        /** violations for gearbox without R gear */
                        tmp_ratio = l2s_lib_get_gear_ratio(L2S_GEAR_R, L2S_WOR_TRNS_IDX);
                        if(  tmp_ratio > 0
                         &&
                            (l2s_cop_gear.gear_tq_able[L2S_WOR_TRNS_IDX] == L2S_GEAR_2ND_R
                          || l2s_cop_gear.gear_act[L2S_WOR_TRNS_IDX].err)
                         &&
                            (l2s_cop_gbx_tq.clu[L2S_WOR_TRNS_IDX].tq.val > l2s_trp_gbx_tq.prot.clu[L2S_WOR_TRNS_IDX].tq_thres_sz002
                          || l2s_cop_gbx_tq.clu[L2S_WOR_TRNS_IDX].tq.err)
                          )
                        {
                            l2s_violation_sz002[L2S_WOR_TRNS_IDX] = GT_TRUE;
                        }
                        else
                        {
                            l2s_violation_sz002[L2S_WOR_TRNS_IDX] = GT_FALSE;
                        }
                        
                        /** violations for gearbox with R gear */
                        /* _OSCA: Lib function will translate R define to partial transmission */
                        tmp_ratio = l2s_lib_get_gear_ratio(L2S_GEAR_R, L2S_WR_TRNS_IDX);                     
                        if( tmp_ratio > 0
                         &&
                            (l2s_cop_gear.gear_tq_able[L2S_WR_TRNS_IDX] == L2S_GEAR_R
                          || l2s_cop_gear.gear_act[L2S_WR_TRNS_IDX].err)
                         &&
                            (l2s_cop_gbx_tq.clu[L2S_WR_TRNS_IDX].tq.val > l2s_trp_gbx_tq.prot.clu[L2S_WR_TRNS_IDX].tq_thres_sz002
                          || l2s_cop_gbx_tq.clu[L2S_WR_TRNS_IDX].tq.err)
                          )
                        {
                            l2s_violation_sz002[L2S_WR_TRNS_IDX] = GT_TRUE;
                        }
                        else
                        {
                            l2s_violation_sz002[L2S_WR_TRNS_IDX] = GT_FALSE;
                        }
                                                      
                    }
                    break;
                    
                    default:
                    {
                        l2s_violation_sz002[L2S_WOR_TRNS_IDX] = GT_FALSE;
                        l2s_violation_sz002[L2S_WR_TRNS_IDX] = GT_FALSE;
                    }   
                    break;
                        
                        
                }
            }
            else
            {
                l2s_violation_sz002[L2S_WOR_TRNS_IDX] = GT_FALSE;
                l2s_violation_sz002[L2S_WR_TRNS_IDX] = GT_FALSE;
            }
            
        }
            
    }
    else
    {
        l2s_violation_sz002[L2S_WOR_TRNS_IDX] = GT_FALSE;
        l2s_violation_sz002[L2S_WR_TRNS_IDX] = GT_FALSE;
    }
    
}




