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

/***************************************************************************//**
*
*     \file            l2s_sz002_lnc_wd.h
*
*     \author          Frank Messicci
*
*     \date            02.07.2013
*
*     \b Compiler:     Renesas V9.04
*
*     \b Description:  Core Platform part of Level 2 software component
*                      SZ_002
*
*     <b> Modification History:   </b>
*
*      Date        |    Name        |                                         \n
*    --------------+----------------+-----------------------------------------\n
*    02.07.2013    | Frank Messicci | initial revision                        \n
*    --------------+----------------+-----------------------------------------\n
*                  |                |                                         \n
*    --------------+----------------+-----------------------------------------
*
*******************************************************************************/

/*******************************************************************************
*
*     Header Prologue
*
*******************************************************************************/
#ifndef L2S_SZ002_LNC_WD_H
#define L2S_SZ002_LNC_WD_H

/*******************************************************************************
*
*     Global Type Declarations (typedef)
*
*******************************************************************************/

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
extern GT_BOOL l2s_violation_sz002[L2S_NUM_TRNS_PATH];

/*******************************************************************************
*
*     Export Functions Declaration (extern)
*
*******************************************************************************/
extern void l2s_sz002_call(void);

/*******************************************************************************
*
*     Header Epilogue
*
*******************************************************************************/
#endif /* L2S_SZ002_LNC_WD_H */
