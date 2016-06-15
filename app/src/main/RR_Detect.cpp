/*
 * RR_Detect.c
 *
 *  Created on: Mar 3, 2016
 *      Author: admin
 */

#include "RR_Detect.h"
#include <memory.h>

typedef enum
{
	SEG_TYPE_CENTER 		= 0,
	SEG_TYPE_NO_EFFECT 		= 1,
	SEG_TYPE_SUBTRACT 		= 2,
	SEG_TYPE_ADD 			= 3,
	SEG_TYPE_OUT_OF_BOUND	= 4
}SEG_TYPE;

#pragma DATA_SECTION(BeatCntTbl,".fbss")
unsigned char BeatCntTbl[BEAT_CNT_TABLE_SIZE];		// 2 days of beat every minute
unsigned BeatCntTblPtr;

#pragma DATA_SECTION(dRRTbl,".fbss")
signed char dRRTbl[RR_TABLE_SIZE][2];
unsigned dRRTblPtr;

#pragma DATA_SECTION(dRRHist,".fbss")
unsigned char dRRHist[31][32];		// Use 32 for easy access

unsigned char PC[4];
unsigned char BC[4];

unsigned ShortestRR=BIT_RATE*1/8;
unsigned SenseDecayDelay=BIT_RATE*1/8;
unsigned RMinimalSense=mV2BITS*1/64;
DetectFlags detectFlags;
DetectFlags episodeDetectedFlags;
unsigned TachyInt=BIT_RATE/4;
unsigned TachyNumBeats=4;
unsigned TachyCnt;
unsigned TachyInWindow;
unsigned long TachyWindow;
unsigned BardyInt=BIT_RATE*2;
unsigned BardyNumBeats=4;
unsigned BardyCnt;

unsigned PauseLen=BIT_RATE*5;
unsigned InputOffset=(1<<(SAMPL_BITS));
int AfibThreshold=50;


int thresh;
static int threshOrig;
unsigned RRint;		// RR interval
unsigned RRintPrev;	// Prev RR interval
static unsigned RRcnt;		// count from prev RR
static unsigned decay;
static unsigned beatCnt;
static unsigned minCnt;

static void HandleNewRR();
static void DetectAfib();
static void DetectTachy();
static void DetectBardy();
static void DetectPause();


void __cdecl DetectRRInit()
{
	thresh=mV2BITS*5/8;
	threshOrig=thresh;

	BeatCntTblPtr=0;

	RRint=0;
	RRcnt=0;
	decay=0;
	beatCnt=0;
	minCnt=0;

	TachyCnt=0;
	TachyInWindow=0;
	TachyWindow=0;

	detectFlags.flagArr=0;
	episodeDetectedFlags.flagArr=0;
}

void __cdecl DetectAfibInit()
{
	dRRTblPtr=RR_TABLE_SIZE-1;
	memset(dRRTbl,-1,sizeof(dRRTbl));
	memset(dRRHist,0,sizeof(dRRHist));
	memset(PC,0,sizeof(PC));
	memset(BC,0,sizeof(BC));
}

void __cdecl DetectRR(unsigned samp)
{
	int sampSign=((int)samp)-InputOffset;
	if(sampSign>thresh)
	{
		thresh=sampSign;
		RRint+=RRcnt;
		threshOrig=thresh;
		if(threshOrig>mV2BITS)
		{
			threshOrig=mV2BITS;
		}
		decay=0;
		RRcnt=0;
	}
	else
	{
		if(RRcnt==ShortestRR)		// New RR detected !!
		{
			HandleNewRR();
			RRintPrev=RRint;
			RRint=0;
			decay=threshOrig*((BIT_RATE*3)/8);
			beatCnt++;
		}
		else
		{
			unsigned cmp_val=ShortestRR+SenseDecayDelay;
			if(RRcnt>cmp_val)
			{
				cmp_val+=BIT_RATE;
				if(RRcnt<cmp_val)
				{
					decay+=(threshOrig*5)>>4;
				}
				else
				{
					cmp_val+=BIT_RATE/2;
					if(RRcnt==cmp_val)
					{
						decay=threshOrig*((BIT_RATE*13)/16);
					}
					else if(RRcnt>cmp_val)
					{
						decay+=(threshOrig*3)>>4;
					}
				}
			}
			thresh=threshOrig-(decay>>BIT_RATE_SHIFT);
			if(thresh<RMinimalSense)
			{
				thresh=RMinimalSense;
			}
		}
	}
	RRcnt++;
	if(detectFlags.flags.DetectPause)
	{
		DetectPause();
	}
	if(++minCnt==BIT_RATE*60)
	{
		minCnt=0;
		BeatCntTbl[BeatCntTblPtr++]=beatCnt;
		beatCnt=0;
	}
}


void HandleNewRR()
{
	if(detectFlags.flags.DetectAfib)
	{
		DetectAfib();
	}
	if(detectFlags.flags.DetectTachy)
	{
		DetectTachy();
	}
	if(detectFlags.flags.DetectBardy)
	{
		DetectBardy();
	}
}

void DetectPause()
{
	episodeDetectedFlags.flags.DetectPause=RRcnt>PauseLen;
}

void DetectTachy()
{
	if(TachyWindow & 0x80000000L)
	{
		TachyInWindow--;
	}
	TachyWindow<<=1;
	if(RRint<=TachyInt)
	{
		TachyInWindow++;
		TachyWindow|=1;
		if(episodeDetectedFlags.flags.DetectTachy==0)
		{
			TachyCnt++;
			if(TachyCnt==TachyNumBeats)
			{
				TachyCnt=0;
				episodeDetectedFlags.flags.DetectTachy=1;
			}
		}
	}
	else if(episodeDetectedFlags.flags.DetectTachy==1)
	{
		TachyCnt++;
		if(TachyCnt==TachyNumBeats*2)
		{
			TachyCnt=0;
			episodeDetectedFlags.flags.DetectTachy=0;
		}
	}
	else
	{
		TachyCnt=0;
	}
	if(TachyInWindow>24)
	{
		episodeDetectedFlags.flags.DetectTachy=1;
	}
}

void DetectBardy()
{
	if(RRint>=BardyInt)
	{
		if(episodeDetectedFlags.flags.DetectBardy==0)
		{
			BardyCnt++;
			if(BardyCnt==BardyNumBeats)
			{
				BardyCnt=0;
				episodeDetectedFlags.flags.DetectBardy=1;
			}
		}
		else
		{
			BardyCnt=0;
		}
	}
	else if(episodeDetectedFlags.flags.DetectBardy==1)
	{
		BardyCnt++;
		if(BardyCnt==BardyNumBeats*2)
		{
			BardyCnt=0;
			episodeDetectedFlags.flags.DetectBardy=0;
		}
	}
	else
	{
		BardyCnt=0;
	}
}

void DetectAfib()
{
	signed char dRRPrev;
	signed char dRR;
	char seg;
	dRRPrev=dRRTbl[dRRTblPtr][0];
	if((RRint>RRintPrev+BIT_RATE*3/2)||(RRintPrev>RRint+BIT_RATE*3/2))
	{
		dRR=-1;
		seg=SEG_TYPE_OUT_OF_BOUND;
	}
	else
	{
		int dRRval=RRint-RRintPrev;
		if(RRint<BIT_RATE/2 || RRintPrev<BIT_RATE/2)
		{
			dRRval<<=1;
		}
		if(RRint>BIT_RATE || RRintPrev>BIT_RATE)
		{
			dRRval>>=1;
		}
		dRR=(((dRRval*1000)>>BIT_RATE_SHIFT)+620)/40;
		if(dRR<0)
		{
			dRR=0;
		}
		else if(dRR>30)
		{
			dRR=30;
		}
		if(dRRPrev>=0)
		{
			seg=SEG_TYPE_ADD;
			if(dRR+dRRPrev>27)
			{
				int onDiag=(dRR<dRRPrev+3 && dRR+3>dRRPrev);
				if(dRR+dRRPrev<33)
				{
					if(onDiag)
					{
						seg=SEG_TYPE_CENTER;
					}
					else
					{
						seg=SEG_TYPE_NO_EFFECT;
					}
				}
				else if(dRR>12 && dRRPrev>12 && (dRR<18 || dRRPrev<18 || onDiag))
				{
					seg=SEG_TYPE_SUBTRACT;
				}
			}
			if(dRRHist[dRR][dRRPrev]++==0)
			{
				BC[seg]++;
			}
			PC[seg]++;
			{
//				int P=PC[3]-BC[3]-PC[2]+BC[2];
//				int Irr=BC[1]+BC[2]+BC[3];
//				int AF=(Irr-PC[0]-(P<<1));
				int AF=((PC[2]-PC[3]+BC[3])<<1)+BC[3]-BC[2]+BC[1]-PC[0];
				episodeDetectedFlags.flags.DetectAfib=AF>AfibThreshold;
			}
		}
		{
			char segPrev;
			unsigned dRRTblPtrNext;
			dRRTblPtr=(dRRTblPtr+1)&(RR_TABLE_SIZE-1);
			dRRTblPtrNext=(dRRTblPtr+1)&(RR_TABLE_SIZE-1);
			dRRPrev=dRRTbl[dRRTblPtr][0];

			dRRTbl[dRRTblPtr][0]=dRR;
			dRRTbl[dRRTblPtr][1]=seg;

			segPrev=dRRTbl[dRRTblPtrNext][1];
			dRR=dRRTbl[dRRTblPtrNext][0];

			if(dRR>=0 && dRRPrev>=0)
			{
				if(--dRRHist[dRR][dRRPrev]==0)
				{
					BC[segPrev]--;
				}
				PC[segPrev]--;
			}
		}
	}

	return;
}

int DebugCheckAfibConsistency()
{
	int i,j;
	int BCcnt,PCcnt,HistSum,HistCell;
	BCcnt=0;
	PCcnt=0;
	HistSum=0;
	HistCell=0;
	for(i=0;i<4;i++)
	{
		BCcnt+=BC[i];
		PCcnt+=PC[i];
	}
	for(i=0;i<31;i++)
	{
		for(j=0;j<31;j++)
		{
			HistSum+=dRRHist[i][j];
			if(dRRHist[i][j]!=0)
			{
				HistCell++;
			}
		}
	}
	if(BCcnt==HistCell && PCcnt==HistSum)
	{
		return 1;
	}
	return 0;
}
