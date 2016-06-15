/*
 * RR_Detect.h
 *
 *  Created on: Mar 3, 2016
 *      Author: admin
 */

#ifndef RR_DETECT_H_
#define RR_DETECT_H_

#define BIT_RATE_SHIFT 8
#define BIT_RATE (1<<BIT_RATE_SHIFT)
#define SAMPL_BITS	14
#define mV2BITS (((1<<(SAMPL_BITS+1))+3)/5)
#define RR_TABLE_SIZE	128
#define BEAT_CNT_TABLE_SIZE	(60*24*2)
//#define BEAT_CNT_TABLE_SIZE (60*24)
//#define BEAT_CNT_TABLE_SIZE (60*20)

typedef union
{
	struct
	{
		unsigned DetectTachy:1;
		unsigned DetectBardy:1;
		unsigned DetectPause:1;
		unsigned DetectAfib:1;
	} flags;
	unsigned flagArr;
}DetectFlags;

extern unsigned ShortestRR;
extern unsigned SenseDecayDelay;
extern unsigned RMinimalSense;

extern DetectFlags detectFlags;
extern DetectFlags episodeDetectedFlags;
extern unsigned TachyInt;
extern unsigned TachyNumBeats;
extern unsigned BardyInt;
extern unsigned BardyNumBeats;
extern unsigned PauseLen;
extern unsigned InputOffset;
extern int AfibThreshold;
extern int thresh;

extern unsigned char BeatCntTbl[BEAT_CNT_TABLE_SIZE];		// 2 days of beat every minute
extern unsigned BeatCntTblPtr;


void DetectRRInit();
void DetectAfibInit();

void DetectRR(unsigned samp);

int DebugCheckAfibConsistency();



#endif /* RR_DETECT_H_ */
