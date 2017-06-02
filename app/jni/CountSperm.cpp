#include "CountSperm.h"

#include<opencv/cv.h>   //cv.h OpenCV����Ҫ����ͷ�ļ�
#include <opencv/highgui.h>//��ʾͼ���õģ���Ϊ�õ�����ʾͼƬ

using namespace std;
using namespace cv;

//ͼ���ROI�޶�������齹��������
int nWidth = 1000;
int nHeight = 1000;

//����������ʼ��
ParaRange AveSpermRangeIni = {13,35,24,5,10,7,3,6,4};

//����״̬��ʾ
/*˵��
* nAlgStatus = 1;//�����ɹ�
* nAlgStatus = 0;//�ڴ��쳣
* nAlgStatus = -1;//��������쳣
* nAlgStatus = -2;//����д���쳣
* nAlgStatus = -3;//δ�ҵ�ͼ���ͼ���ļ������淶��û�����ݶ�ȡȨ��
* nAlgStatus = -4;//����ͼ�������������޷���һ������
* nAlgStatus = -5;//�����쳣���������̫�٣�������ܲ�׼ȷ
* nAlgStatus = -6;//����ͼ�񱳾��쳣����������������ζ�
*/
int nAlgStatus = 1;

//����Ŀ�������޶�
double dMinArea = 8;//��С���,pixel
double dMaxArea = 400;//������,pixel

//��ȡ��ƵͼƬ����
int getVideoFrameNum(char *pcVideoPath);

//��ȡͼƬ��ַ������
int getImgFileSeq(const char *pcImgPath, char **ppcImageFile);

//����ͼ������
void genBackgroundImg(IplImage * pImgBackGround, const char **ppcFilePath, int const& nImgFileNum);

//��ȡ��������
void getSpermCount(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, SSettings const& SParaInput, double *pdTestResult);

//��ȡ����������ƽ��ÿ��ͼ��
int getSpermCountABC(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, IplImage * pImgBackGround, ParaRange *pSAveSpermRange);

//��ȡ�����������ƽ������ͼ��
int getActiveSpermCount(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, ParaRange *pSAveSpermRange);

//��ȡ��ͼ��
IplImage *clipCenterImage(IplImage *pImgScr);

//ͼ��Ҷ�����
void stretchGrayValue(IplImage *pImgScr);

//��ֵ�ָ�
IplImage *getThresholdImage(IplImage *pImgSubScr, int nFlag);

//������ƽ��ֵ�ͱ�׼��
double *calAveStd(const double *pdData, int nSpermNum);

//��ȡĿ���������伸����Ϣ
int getImgContourInfor(IplImage *pImgBinary, IplImage *pImgContourShow, SpermInfor *pSSpermInfor);

//��ֵ���ı���ͼƬ����������
int getBackImgContourInfor(IplImage *pImgBackGround, IplImage *pImgBinary, ParaRange *pSAveSpermRange);

//����ͼƬ֮�����������
int getSub2ImgContourInfor(IplImage *pImgBinary,  IplImage *pImgContourShow, ParaRange *pSAveSpermRange);

//ͳ��Ŀ�꼸��������Χ
ParaRange getSpermParaRange(SpermInfor *pSSpermInfor, int nSpermNum);

//ͳ��ƽ��ÿ��ͼ�ľ�������
int getAveSpermCount( ParaRange *pSAveSpermRange, ParaRange *pSSpermRange, int const& nImgFileNum, int *nSpermNum);

//��ȡ����ͼƬ�еľ���Ŀ�����
int getBackImgSpermCount(const char * pcResultPath, int const& nImgFileNum, IplImage *pImgBackGround, ParaRange *pSAveSpermRange);

//��ȡ����Ӹ���
int getTwoImgAliveSpermCount(IplImage *pImgSubScr1,IplImage *pImgSubScr2, IplImage *pImgContourShow, ParaRange *pSAveSpermRange);

int getSpermCountMain(const char *pcImgPath, const char *pcResultPath, const double *dVar, double *pdTestResult)
{
	//���������ȡ
	if ((dVar[0] < 1e-4) || (dVar[1] < 3) || (dVar[2] < 1) || (dVar[3] < 1e-4) )
	{
		nAlgStatus = -1;
		return nAlgStatus;//��������쳣
	}
	SSettings SParaInput = {
		dVar[0],//
		dVar[1],//
		dVar[2],//
		dVar[3]//
	};

	char *pcImgFileTemp[FILENUM] = {NULL};

	//��ȡͼƬ��ַ������
	for (int i = 0; i < FILENUM; i++)
	{
		pcImgFileTemp[i] = (char *)malloc(sizeof(char)*MAXPATHLENGTH);
		if (NULL == pcImgFileTemp[i])
		{
			for (int j = 0; j <= i; j++)
			{
				free(pcImgFileTemp[j]);
				pcImgFileTemp[j] = NULL;
			}

			nAlgStatus = 0;//�ڴ��쳣
			return nAlgStatus;
		}
	}

	int nImgNum = getImgFileSeq(pcImgPath,pcImgFileTemp);

	if (1 == nAlgStatus)
	{
		if (nImgNum == 0)
		{
			nAlgStatus = -3;//δ�ҵ�ͼ���ͼ���ļ������淶��û�����ݶ�ȡȨ��
		}
		else if (nImgNum >= 1 && nImgNum <= 9)
		{
			nAlgStatus = -4;//����ͼ�������������޷���һ������
		}
		else if (nImgNum >= 10)//����������
		{
			const char *ppcImageFile[FILENUM] = {NULL};
			for (int i = 0; i < nImgNum; i++)
			{
				ppcImageFile[i] = pcImgFileTemp[i];//����const char* ��const charת��
			}
			getSpermCount(ppcImageFile, pcResultPath, nImgNum, SParaInput, pdTestResult);
		}
	}	
	
	//��Դ�ͷ�
	for (int i = 0; i < FILENUM; i++)
	{
		free(pcImgFileTemp[i]);
		pcImgFileTemp[i] = NULL;
	}

	return nAlgStatus;
}

//��ȡͼƬ��ַ������
int getImgFileSeq(const char *pcImgPath, char **ppcImageFile)
{
	int i;
	int nImgFileNum = 0;	
	char *pcImgName = (char *)malloc(sizeof(char)*50);
	if ( NULL == pcImgName )
	{
		nAlgStatus = 0;
		return 0;//�ڴ��쳣
	}

	for(i = 0 ; i < FILENUM; i++)
	{
		char *pcFileNameTemp = NULL;
		FILE *fp = NULL;

		pcFileNameTemp = (char *)malloc(sizeof(char)*MAXPATHLENGTH);
		if ( NULL == pcFileNameTemp )
		{
			nAlgStatus = 0;
			break;//�ڴ��쳣
		}

		strcpy(pcFileNameTemp, pcImgPath);		
		sprintf(pcImgName, "%03d.jpg", i);
		strcat(pcFileNameTemp, pcImgName); 

		fp = fopen(pcFileNameTemp, "r");   //û������ļ������
		if ( fp )
		{
			strcpy(ppcImageFile[nImgFileNum],pcFileNameTemp);
			nImgFileNum++;
			fclose(fp);

			free(pcFileNameTemp);
			pcFileNameTemp = NULL;
		}
		else
		{
			free(pcFileNameTemp);
			pcFileNameTemp = NULL;
			break;
		}
	}

	free(pcImgName);
	pcImgName = NULL;

	return nImgFileNum;
}

//��ȡ��������
void getSpermCount(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, SSettings const& SParaInput, double *pdTestResult)
{
	int nTotalSpermNum = 0;//��������A+B+C+D
	int nAliveSpermNum = 0;//�������A+B+C
	int nActiveSpermNum = 0;//�˶���Ծ�ľ�����A+B
	int nDeadSpermNum = 0;//��ȫ�����ľ�����D
	double dSLNum = 0;//ֱ���˶�����������λ�ڸ�

	double dAliveSpermRatio = 0;//���ӻ��� = �������/��������
	double dActiveSpermRatio = 0;//���ӻ��� = A+B

	double dTotaSpermDensity;//�����ܶ�,��λ��10^6/mL
	double dAliveSpermDensity;//������ܶ�,��λ��10^6/mL

	double dRatio = 0;

	ParaRange *pSAveSpermRange = (ParaRange *)malloc(sizeof(ParaRange));
	pSAveSpermRange->dAreaAve = 0;
	pSAveSpermRange->dAreaMax = 0;
	pSAveSpermRange->dAreaMin = 0;
	pSAveSpermRange->dMajorLengthAve = 0;
	pSAveSpermRange->dMajorLengthMax = 0;
	pSAveSpermRange->dMajorLengthMin = 0;
	pSAveSpermRange->dMinorLengthAve = 0;
	pSAveSpermRange->dMinorLengthMax = 0;
	pSAveSpermRange->dMinorLengthMin = 0;

	IplImage * pImgScrTemp = NULL;
	IplImage * pImgBackGround = NULL;

	do 
	{
		//����ͼ������
		pImgScrTemp = cvLoadImage(ppcFilePath[0],0);//0��ʾǿ��ת����ȡͼ��Ϊ�Ҷ�ͼ
		pImgBackGround = clipCenterImage(pImgScrTemp);//�����ڴ�
		genBackgroundImg(pImgBackGround, ppcFilePath, nImgFileNum);

		dRatio = nWidth * nHeight * SParaInput.dRatioImg * SParaInput.dRatioImg * SParaInput.dSampleDepth;
		if ((dRatio >= -EPSINON) && (dRatio <= EPSINON))
		{
			nAlgStatus = -1;
			break;//��������쳣
		}else
		{
			//dRatio = dRatio * (1e-6);//��λ�������/����
			dRatio = dRatio * (1e-4);//��λ���ڸ�/����
		}

		//��ľ�������A+B+C
		nAliveSpermNum = getSpermCountABC(ppcFilePath, pcResultPath, nImgFileNum, pImgBackGround, pSAveSpermRange);
		if (nAlgStatus == 0 || nAlgStatus == -2)
		{
			break;
		}

		//��ȫ�����ľ�����D
		if (nAliveSpermNum <= 5)
		{
			nAlgStatus = -5;//�����쳣���������̫�٣�������ܲ�׼ȷ
			nDeadSpermNum = getBackImgSpermCount(pcResultPath, nImgFileNum, pImgBackGround,&AveSpermRangeIni);
		}else
		{
			nDeadSpermNum = getBackImgSpermCount(pcResultPath, nImgFileNum, pImgBackGround,pSAveSpermRange);
		}
		
		if (0 == nAlgStatus || -2 == nAlgStatus)
		{
			break;
		}

		//�˶���Ծ�ľ�����A+B
		if (nAliveSpermNum > 5)
		{
			nActiveSpermNum = getActiveSpermCount(ppcFilePath, pcResultPath, nImgFileNum, pSAveSpermRange);
			if (0 == nAlgStatus || -2 == nAlgStatus)
			{
				break;
			}
			if (nActiveSpermNum > nAliveSpermNum)
			{
				nAlgStatus = -6;//����ͼ�񱳾��쳣����������������ζ�
				break;
			}
		}
		
		nTotalSpermNum = nAliveSpermNum + nDeadSpermNum;

		dAliveSpermRatio = nAliveSpermNum/(nTotalSpermNum + 0.000001);
		dActiveSpermRatio = nActiveSpermNum/(nTotalSpermNum + 0.000001);
		dTotaSpermDensity = nTotalSpermNum/(dRatio);
		dAliveSpermDensity = nAliveSpermNum/(dRatio);
		dSLNum = dTotaSpermDensity * dActiveSpermRatio * SParaInput.dVolume;//���������㣬��λ�ڸ�

		pdTestResult[0] = nTotalSpermNum;
		pdTestResult[1] = dTotaSpermDensity;
		pdTestResult[2] = nAliveSpermNum;
		pdTestResult[3] = dAliveSpermDensity;
		pdTestResult[4] = dAliveSpermRatio;
		pdTestResult[5] = dActiveSpermRatio;

		pdTestResult[7] = dSLNum;

		pdTestResult[17] = dAliveSpermRatio - dActiveSpermRatio;//C��
		pdTestResult[18] = 1 - dAliveSpermRatio;//D��
		pdTestResult[19] = dActiveSpermRatio;//PR, A+B
		pdTestResult[20] = pdTestResult[17];//NP, C��
		pdTestResult[21] = pdTestResult[18];//IM, D��
	} while (0);

	cvReleaseImage(&pImgBackGround);
	pImgBackGround = NULL;
	cvReleaseImage(&pImgScrTemp);
	pImgScrTemp = NULL;
	free(pSAveSpermRange);
	pSAveSpermRange = NULL;
 }

void genBackgroundImg(IplImage * pImgBackGround, const char **ppcFilePath, int const& nImgFileNum)
{
	IplImage * pImgSubScr1 = NULL;
	IplImage * pImgSubScr2 = NULL;
	IplImage * pImgScrTemp = NULL;

	//����ͼ�����ɣ��˴�Ĭ��Ϊ��ɫ������
	pImgScrTemp = cvLoadImage(ppcFilePath[0],0);//0��ʾǿ��ת����ȡͼ��Ϊ�Ҷ�ͼ
	pImgSubScr1 = clipCenterImage(pImgScrTemp);//��ȡ��������
	cvReleaseImage(&pImgScrTemp);

	//������ͼƬ�нϴ��Ԫ��
	for (int i = 1; i < nImgFileNum; i++)
	{
		pImgScrTemp = cvLoadImage(ppcFilePath[i],0);
		pImgSubScr2 = clipCenterImage(pImgScrTemp);
		cvMax(pImgSubScr1,pImgSubScr2,pImgBackGround);
		cvCopy( pImgBackGround, pImgSubScr1,NULL );

		cvReleaseImage(&pImgScrTemp);
		cvReleaseImage(&pImgSubScr2);
	}
	cvReleaseImage(&pImgSubScr1);
}

int getSpermCountABC(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, IplImage * pImgBackGround, ParaRange *pSAveSpermRange)
{
	int i,j;
	int nThreshFlag;
	int nSingleImgSpermNum = 0;
	int nAveImgSpermNum = 0;//ƽ��ÿ��ͼ�ľ�����
	int nNormalSpermNum;
	int nLargeSpermNum;
	int nTotalSpermNum = 0;//��������
	double dLargeArea;

	int *nSpermNum = (int *)malloc(sizeof(int)*nImgFileNum);//ÿ��ͼ�ľ�����
	if (NULL == nSpermNum)
	{
		nAlgStatus = 0;
		return 0;//�ڴ��쳣
	}

	ParaRange *pSSpermRange = (ParaRange *)malloc(sizeof(ParaRange)*nImgFileNum);//ÿ��ͼ���ӵ�������Χ
	if (NULL == pSSpermRange)
	{
		free(nSpermNum);
		nSpermNum = NULL;
		nAlgStatus = 0;
		return 0;//�ڴ��쳣
	}

	//��ʼͼ����//
	for (i = 0; i < nImgFileNum; i++)
	{
		//1.��ȡͼ����ȡ��ͼ
		IplImage * pImgScrTemp = cvLoadImage(ppcFilePath[i],0);
		IplImage * pImgScr = clipCenterImage(pImgScrTemp);
		cvReleaseImage(&pImgScrTemp);
		pImgScrTemp = NULL;

		//2.������
		IplImage * pImgScrDiff = cvCreateImage(cvGetSize(pImgScr),pImgScr->depth,pImgScr->nChannels);
		cvSub(pImgBackGround, pImgScr, pImgScrDiff, NULL);
		cvReleaseImage(&pImgScr);
		pImgScr = NULL;

		//3.��˹ƽ��
		IplImage * pImgScrDiffSmth = cvCreateImage(cvGetSize(pImgScrDiff),pImgScrDiff->depth,pImgScrDiff->nChannels);
		cvSmooth( pImgScrDiff, pImgScrDiffSmth,CV_GAUSSIAN,5,0,1,0);
		cvReleaseImage(&pImgScrDiff);
		pImgScrDiff = NULL;

		//4.ͼ��Ҷ�����
		stretchGrayValue(pImgScrDiffSmth);

		//5.��ֵ�ָ����Ӧ��ֵ���̶���ֵ��Otu��ֵ��
		nThreshFlag = 1;
		IplImage * pImgBinary = getThresholdImage(pImgScrDiffSmth,nThreshFlag);
		cvReleaseImage(&pImgScrDiffSmth);
		pImgScrDiffSmth = NULL;

		//6.������
		IplConvKernel * pStructureEle = cvCreateStructuringElementEx(3, 3, 1, 1,
			CV_SHAPE_ELLIPSE, NULL);//�����ṹԪ��
		cvMorphologyEx( pImgBinary, pImgBinary,
			NULL, pStructureEle,
			CV_MOP_OPEN, 1 );	
		cvReleaseStructuringElement( &pStructureEle );//����ṹԪ��

		//7.����������ȡ�����ơ�����������ȡ
		SpermInfor *pSSpermInfor = (SpermInfor *)malloc(sizeof(SpermInfor)*MAXSPERMNUM);
		if (NULL == pSSpermInfor)
		{
			free(nSpermNum);	
			nSpermNum = NULL;
			free(pSSpermRange);
			pSSpermRange = NULL;
			cvReleaseImage(&pImgBinary);
			pImgBinary = NULL;

			nAlgStatus = 0;
			return 0;//�ڴ��쳣
		}
		
		pImgScrTemp = cvLoadImage(ppcFilePath[i],1);
		pImgScr = clipCenterImage(pImgScrTemp);
		IplImage * pImgContourShow = cvCreateImage(cvGetSize(pImgScr),pImgScr->depth,pImgScr->nChannels);
		//cvZero(pImgContourShow);
		cvCopy(pImgScr,pImgContourShow); //����ͼ��,λ��ߴ硢ͨ���豣��һ��
		nSingleImgSpermNum = getImgContourInfor(pImgBinary,pImgContourShow,pSSpermInfor);

		cvReleaseImage(&pImgScrTemp);
		pImgScrTemp = NULL;
		cvReleaseImage(&pImgScr);
		pImgScr = NULL;
		cvReleaseImage(&pImgBinary);
		pImgBinary = NULL;

		if (nSingleImgSpermNum <= 5 )
		{
			free(nSpermNum);
			nSpermNum = NULL;
			free(pSSpermRange);
			pSSpermRange = NULL;
			free(pSSpermInfor);
			pSSpermInfor = NULL;

			return 0;//�����쳣���������̫�٣�������ܲ�׼ȷ
		}

		//ͼ��洢
		char *pcFileNameTemp = (char *)malloc(sizeof(char)*MAXPATHLENGTH);
		if (NULL == pcFileNameTemp)
		{
			free(nSpermNum);	
			nSpermNum = NULL;
			free(pSSpermRange);
			pSSpermRange = NULL;
			free(pSSpermInfor);
			pSSpermInfor = NULL;

			nAlgStatus = 0;
			return 0;//�ڴ��쳣
		}
		char *pcImg = (char *)malloc(sizeof(char)*50);
		if (NULL == pcImg)
		{
			free(nSpermNum);	
			nSpermNum = NULL;
			free(pSSpermRange);
			pSSpermRange = NULL;
			free(pSSpermInfor);
			pSSpermInfor = NULL;
			free(pcFileNameTemp);
			pcFileNameTemp = NULL;

			nAlgStatus = 0;
			return 0;//�ڴ��쳣
		}

		strcpy(pcFileNameTemp, pcResultPath);
		sprintf(pcImg, "Result_%03d.jpg", i);
		strcat(pcFileNameTemp, pcImg); 
		int nSaveStatus = cvSaveImage(pcFileNameTemp,pImgContourShow);//ͼƬ�ļ��洢
		cvReleaseImage(&pImgContourShow);
		pImgContourShow = NULL;
		if (0 == nSaveStatus)
		{
			free(nSpermNum);	
			nSpermNum = NULL;
			free(pSSpermRange);
			pSSpermRange = NULL;
			free(pSSpermInfor);
			pSSpermInfor = NULL;
			free(pcFileNameTemp);
			pcFileNameTemp = NULL;
			free(pcImg);
			pcImg = NULL;

			nAlgStatus = -2;
			return 0;//����д���쳣
		}

		//8.����ɸѡ��������������ᡢ���ᣩ
		pSSpermRange[i] = getSpermParaRange(pSSpermInfor,nSingleImgSpermNum);
		if (0 == nAlgStatus)
		{
			free(nSpermNum);
			nSpermNum = NULL;
			free(pSSpermRange);
			pSSpermRange = NULL;
			free(pSSpermInfor);
			pSSpermInfor = NULL;
			free(pcFileNameTemp);
			pcFileNameTemp = NULL;
			free(pcImg);
			pcImg = NULL;

			return 0;//�ڴ��쳣
		}

		//9.����÷�ͼ�ľ��Ӹ���
		nNormalSpermNum = 0;
		nLargeSpermNum = 0;
		dLargeArea = 0;

		for (j = 0; j < nSingleImgSpermNum; j++)
		{
			if (pSSpermInfor[j].dArea > 1.5*pSSpermRange[i].dAreaMax)
			{
				dLargeArea = dLargeArea + pSSpermInfor[j].dArea;
			}else
			{
				nNormalSpermNum++;
			}
		}

		nLargeSpermNum = (int)(2*dLargeArea/(pSSpermRange[i].dAreaMax + pSSpermRange[i].dAreaMin));
		nSpermNum[i] = nLargeSpermNum + nNormalSpermNum;

		//��Դ�ͷ�
		free(pcFileNameTemp);
		pcFileNameTemp = NULL;
		free(pSSpermInfor);
		pSSpermInfor = NULL;
		free(pcImg);
		pcImg = NULL;
	}
	
	//10.ͳ��ƽ��ÿ��ͼ�ľ�������
	nAveImgSpermNum = getAveSpermCount( pSAveSpermRange, pSSpermRange, nImgFileNum, nSpermNum);

	//��Դ�ͷ�
	free(nSpermNum);	
	nSpermNum = NULL;
	free(pSSpermRange);
	pSSpermRange = NULL;

	return nAveImgSpermNum;
}

//��ȡ�����������ƽ������ͼ��
int getActiveSpermCount(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, ParaRange *pSAveSpermRange)
{
	int nAliveSpermNum = 0;
	int *nSingleImgAliveSpermNum = (int *)malloc(sizeof(int)*nImgFileNum);
	if (NULL == nSingleImgAliveSpermNum)
	{
		nAlgStatus = 0;
		return 0;//�ڴ��쳣
	}

	IplImage * pImgScrTemp = NULL;
	IplImage * pImgSubScr1 = NULL;
	IplImage * pImgSubScr2 = NULL;
	IplImage * pImgContourShow;

	char *pcFileNameTemp = (char *)malloc(sizeof(char)*MAXPATHLENGTH);
	if (NULL == pcFileNameTemp)
	{
		free(nSingleImgAliveSpermNum);
		nSingleImgAliveSpermNum = NULL;

		nAlgStatus = 0;
		return 0;//�ڴ��쳣
	}

	char *pcImg = (char *)malloc(sizeof(char)*50);
	if (NULL == pcImg)
	{
		free(nSingleImgAliveSpermNum);
		nSingleImgAliveSpermNum = NULL;
		free(pcFileNameTemp);
		pcFileNameTemp = NULL;

		nAlgStatus = 0;
		return 0;//�ڴ��쳣
	}

	for (int i = 0; i < nImgFileNum; i++)
	{
		if (i < nImgFileNum-1)
		{
			pImgScrTemp = cvLoadImage(ppcFilePath[i],0);
			pImgSubScr1 = clipCenterImage(pImgScrTemp);
			cvReleaseImage(&pImgScrTemp);
			pImgScrTemp = NULL;

			pImgScrTemp = cvLoadImage(ppcFilePath[i+1],0);
			pImgSubScr2 = clipCenterImage(pImgScrTemp);
			cvReleaseImage(&pImgScrTemp);
			pImgScrTemp = NULL;
		}
		else
		{
			pImgScrTemp = cvLoadImage(ppcFilePath[i],0);
			pImgSubScr1 = clipCenterImage(pImgScrTemp);
			cvReleaseImage(&pImgScrTemp);
			pImgScrTemp = NULL;

			pImgScrTemp = cvLoadImage(ppcFilePath[i-1],0);
			pImgSubScr2 = clipCenterImage(pImgScrTemp);
			cvReleaseImage(&pImgScrTemp);
			pImgScrTemp = NULL;
		}

		strcpy(pcFileNameTemp, pcResultPath);
		sprintf(pcImg, "Result_%03d.jpg", i);
		strcat(pcFileNameTemp, pcImg); 

		pImgContourShow = cvLoadImage(pcFileNameTemp,1);
		nSingleImgAliveSpermNum[i] = getTwoImgAliveSpermCount(pImgSubScr1,pImgSubScr2,pImgContourShow, pSAveSpermRange);//ע��ͼ2-ͼ1����Ϊ�ױ�����ͼ1������

		int nSaveStatus = cvSaveImage(pcFileNameTemp,pImgContourShow);//ͼƬ�ļ��洢
		cvReleaseImage(&pImgSubScr1);
		pImgSubScr1 = NULL;
		cvReleaseImage(&pImgSubScr2);
		pImgSubScr2 = NULL;
		cvReleaseImage(&pImgContourShow);
		pImgContourShow = NULL;

		if (0 == nSaveStatus)
		{
			free(nSingleImgAliveSpermNum);
			nSingleImgAliveSpermNum = NULL;
			free(pcFileNameTemp);
			pcFileNameTemp = NULL;
			free(pcImg);
			pcImg = NULL;

			nAlgStatus = -2;
			return 0;//����д���쳣
		}

		nAliveSpermNum = nAliveSpermNum + nSingleImgAliveSpermNum[i];
	}
	nAliveSpermNum = nAliveSpermNum/(nImgFileNum);

	//��Դ�ͷ�
	free(nSingleImgAliveSpermNum);
	nSingleImgAliveSpermNum = NULL;
	free(pcFileNameTemp);
	pcFileNameTemp = NULL;
	free(pcImg);
	pcImg = NULL;

	return nAliveSpermNum;
}

//��ȡ��ͼ��
IplImage *clipCenterImage(IplImage *pImgScr)
{
	IplImage *imgResult = NULL;
	CvRect SImROI;

	//ͼ��ROI�趨
	if (nWidth <= cvGetSize(pImgScr).width  && nWidth >= 50)
	{
		SImROI.x = cvGetSize(pImgScr).width/2 - nWidth/2;
		SImROI.width = nWidth;
	}
	else
	{
		SImROI.x = 0;
		SImROI.width = cvGetSize(pImgScr).width;
		nWidth =  cvGetSize(pImgScr).width;
	}
	if (nHeight <= cvGetSize(pImgScr).height && nHeight >= 50)
	{
		SImROI.y = cvGetSize(pImgScr).height/2 - nHeight/2;
		SImROI.height = nHeight;
	}
	else
	{
		SImROI.y = 0;
		SImROI.height = cvGetSize(pImgScr).height;
		nHeight = cvGetSize(pImgScr).height;
	}
	cvSetImageROI(pImgScr,SImROI);//����Դͼ��ROI
	imgResult = cvCreateImage(cvSize(nWidth,nHeight),pImgScr->depth,pImgScr->nChannels);//����Ŀ��ͼ��
	cvCopy(pImgScr,imgResult); //����ͼ��
	cvResetImageROI(pImgScr);//Դͼ����������ROI

	return imgResult;
}

//��ֵ�ָ�
IplImage *getThresholdImage(IplImage *pImgSubScr, int nFlag)
{
	IplImage * pImgBinary =cvCreateImage(cvGetSize(pImgSubScr),pImgSubScr->depth,pImgSubScr->nChannels);

	/*
	IplImage * pImgBinaryTest = cvCreateImage(cvGetSize(pImgScrDiffSmth),pImgScrDiffSmth->depth,pImgScrDiffSmth->nChannels);
	cvAdaptiveThreshold( pImgScrDiffSmth, pImgBinary, 255,CV_ADAPTIVE_THRESH_GAUSSIAN_C,
                                  CV_THRESH_BINARY,11,-5);//����Ӧ��ֵ���ָ�Ч����̫�ã����ײ�������
	*/

	//NOTE: Currently, the Otsu��s method is implemented only for 8-bit images.
	if (pImgSubScr->depth == 8)
	{
		/*do OTSU thresh with OpenCV2.4.10
		double dThreshValue = cvThreshold( pImgSubScr, pImgBinary,
			0, 255,
			CV_THRESH_BINARY|THRESH_OTSU);//Otsu��ֵ�ָ�
		*/

		//do OTSU thresh with OpenCV3.0
		double dThreshValue = cvThreshold( pImgSubScr, pImgBinary,
			0, 255,
			CV_THRESH_BINARY|CV_THRESH_OTSU);//Otsu��ֵ�ָ�

		switch (nFlag)
		{
			//��ʾ����ȥ������ĵ��ž���ͼƬ
		case 1 : 
			dThreshValue = 1.7*dThreshValue;
			break;

			//��ʾ������ͼƬ
		case 2 : 
			dThreshValue = 1.5*dThreshValue;
			break;

			//��ʾ��������֮���ͼƬ
		case 3 : 
			dThreshValue = 1.6*dThreshValue;
			break;
		}

		cvThreshold( pImgSubScr, pImgBinary,
			dThreshValue, 255,
			CV_THRESH_BINARY);
	}
	else
	{	
		cvThreshold( pImgSubScr, pImgBinary,
			20, 255,
			CV_THRESH_BINARY);//�̶���ֵ�ָ�
	}

	return pImgBinary;
}

//ƽ��ֵ�ͱ�׼�����
double *calAveStd(const double *pdData, int nSpermNum)
{
	int i;
	double *dAveStdSub = (double *)malloc(sizeof(double)*2);
	double dSum = 0;

	//ƽ��ֵ����	
	double dMean;
	
	for (i = 0; i < nSpermNum; i++)
	{
		dSum = dSum + pdData[i];
	}
	dMean = dSum/nSpermNum;

	//��׼�����
	double dStd;
	dSum = 0;
	for (int i = 0; i < nSpermNum; i++)
	{
		dSum = dSum + (pdData[i]-dMean)*(pdData[i]-dMean);
	}
	dSum = dSum/nSpermNum;
	dStd = sqrt(dSum);

	dAveStdSub[0] = dMean;
	dAveStdSub[1] = dStd;

	return dAveStdSub;
}

//��ȡĿ���������伸����Ϣ
int getImgContourInfor(IplImage *pImgBinary,IplImage *pImgContourShow, SpermInfor *pSSpermInfor)
{
	CvMemStorage *storage = cvCreateMemStorage();
	CvSeq* firstcontour = NULL;

	int nAllSpermNum = 0;
	double dConArea;
	CvBox2D GeoInfor;

	CvContourScanner scanner = cvStartFindContours(pImgBinary,
								storage,
								sizeof(CvContour),
								CV_RETR_LIST,
								CV_CHAIN_APPROX_SIMPLE,
								cvPoint(0,0));
	CvSeq * cTemp =NULL;
	while( (cTemp = cvFindNextContour(scanner) ) != NULL)//��ʼ����
	{ 

		//���
		dConArea = fabs(cvContourArea(cTemp,CV_WHOLE_SEQ,0));

		//�����������dMinArea������С��dMaxArea�������£���֮����ɾ���� 
		if(dConArea > dMinArea && dConArea < dMaxArea && nAllSpermNum < MAXSPERMNUM )
		{  
			//���Ʒ����������������
			cvDrawContours(pImgContourShow,cTemp,CV_RGB(0,0,255),CV_RGB(255,255,255),0);

			//���ģ������ᣬ�Ƕ�
			GeoInfor = cvMinAreaRect2(cTemp, NULL);//��С��Ӿ���

			/*
			if (cTemp->total > 6)
			{
				GeoInfor = cvFitEllipse2(cTemp);//��С�����Բ�����ȶ��������ڲ�����
			}else
			{
				GeoInfor = cvMinAreaRect2(cTemp, NULL);//��С��Ӿ���

			}
			*/

			//������Ϣ�洢
			pSSpermInfor[nAllSpermNum].dPosX = GeoInfor.center.x;
			pSSpermInfor[nAllSpermNum].dPosY = GeoInfor.center.y;

			if (GeoInfor.size.width >= GeoInfor.size.height)
			{
				pSSpermInfor[nAllSpermNum].dMajAxsLen = GeoInfor.size.width;
				pSSpermInfor[nAllSpermNum].dMinAxsLen = GeoInfor.size.height;
			}else
			{
				pSSpermInfor[nAllSpermNum].dMajAxsLen = GeoInfor.size.height;
				pSSpermInfor[nAllSpermNum].dMinAxsLen = GeoInfor.size.width;
			}
			
			pSSpermInfor[nAllSpermNum].dAngle = GeoInfor.angle;
			pSSpermInfor[nAllSpermNum].dArea = dConArea;
			nAllSpermNum = nAllSpermNum+1;
		}
		else  
		{  
			cvSubstituteContour(scanner,NULL);//ɾ����ǰ������
		}  
	}
	firstcontour = cvEndFindContours(&scanner);//���ҵ����������ص�firstContour�С�

	//��Դ�ͷ�
	cvReleaseMemStorage(&storage); 

	return nAllSpermNum;
}

//ͳ��Ŀ�꼸��������Χ
ParaRange getSpermParaRange(SpermInfor *pSSpermInfor, int nSpermNum)
{
	ParaRange pSSpermRange = {0,0,0,0,0,0,0,0,0};
	double *pdAveStd = NULL;
	double *pdData = (double *)malloc(sizeof(double)*nSpermNum);
	if (NULL == pdData)
	{
		nAlgStatus = 0;
		return pSSpermRange;//�ڴ��쳣
	}

	int i;

	//�����Χ
	double dAreaMin,dAreaMax,dAreaAve;
	for (i = 0; i < nSpermNum; i++)
	{
		pdData[i] = pSSpermInfor[i].dArea;
	}
	pdAveStd= calAveStd(pdData, nSpermNum);
	dAreaMin = pdAveStd[0] - pdAveStd[1];
	dAreaMax = pdAveStd[0] + pdAveStd[1];
	dAreaAve = pdAveStd[0];
	if (dAreaMin < 10)
	{
		dAreaMin = 10;
	}
	free(pdAveStd);
	pdAveStd = NULL;

	//���᷶Χ
	double dMajorLengthMin,dMajorLengthMax,dMajorLengthAve;
	for (i = 0; i < nSpermNum; i++)
	{
		pdData[i] = pSSpermInfor[i].dMajAxsLen;
	}
	pdAveStd= calAveStd(pdData, nSpermNum);
	dMajorLengthMin = pdAveStd[0] - 1.5 * pdAveStd[1];
	dMajorLengthMax = pdAveStd[0] + 1.5 * pdAveStd[1];
	dMajorLengthAve = pdAveStd[0];
	if (dMajorLengthMin < 5)
	{
		dMajorLengthMin = 5;
	}
	free(pdAveStd);
	pdAveStd = NULL;

	//���᷶Χ
	double dMinorLengthMin,dMinorLengthMax,dMinorLengthAve;
	for (i = 0; i < nSpermNum; i++)
	{
		pdData[i] = pSSpermInfor[i].dMinAxsLen;
	}
	pdAveStd= calAveStd(pdData, nSpermNum);
	dMinorLengthMin = pdAveStd[0] - 1.5 * pdAveStd[1];
	dMinorLengthMax = pdAveStd[0] + 1.5 * pdAveStd[1];
	dMinorLengthAve = pdAveStd[0];
	if (dMinorLengthMin < 3)
	{
		dMinorLengthMin = 3;
	}
	free(pdAveStd);
	pdAveStd = NULL;

	pSSpermRange.dAreaMin = dAreaMin;
	pSSpermRange.dAreaMax = dAreaMax;
	pSSpermRange.dAreaAve = dAreaAve;

	pSSpermRange.dMajorLengthMin = dMajorLengthMin;
	pSSpermRange.dMajorLengthMax = dMajorLengthMax;
	pSSpermRange.dMajorLengthAve = dMajorLengthAve;

	pSSpermRange.dMinorLengthMin = dMinorLengthMin;
	pSSpermRange.dMinorLengthMax = dMinorLengthMax;
	pSSpermRange.dMinorLengthAve = dMinorLengthAve;

	free(pdData);
	pdData = NULL;

	return pSSpermRange;
}

//ͳ��ƽ��ÿ��ͼ�ľ�������
int getAveSpermCount( ParaRange *pSAveSpermRange, ParaRange *pSSpermRange, int const& nImgFileNum, int *nSpermNum)
{
	int nAveImgSpermNum = 0;

	for (int i = 0; i < nImgFileNum; i++)
	{
		pSAveSpermRange->dAreaMax = pSAveSpermRange->dAreaMax + pSSpermRange[i].dAreaMax;
		pSAveSpermRange->dAreaMin = pSAveSpermRange->dAreaMin + pSSpermRange[i].dAreaMin;
		pSAveSpermRange->dAreaAve = pSAveSpermRange->dAreaAve + pSSpermRange[i].dAreaAve;

		pSAveSpermRange->dMajorLengthMax = pSAveSpermRange->dMajorLengthMax + pSSpermRange[i].dMajorLengthMax;
		pSAveSpermRange->dMajorLengthMin = pSAveSpermRange->dMajorLengthMin + pSSpermRange[i].dMajorLengthMin;
		pSAveSpermRange->dMajorLengthAve = pSAveSpermRange->dMajorLengthAve + pSSpermRange[i].dMajorLengthAve;

		pSAveSpermRange->dMinorLengthMax = pSAveSpermRange->dMinorLengthMax + pSSpermRange[i].dMinorLengthMax;
		pSAveSpermRange->dMinorLengthMin = pSAveSpermRange->dMinorLengthMin + pSSpermRange[i].dMinorLengthMin;
		pSAveSpermRange->dMinorLengthAve = pSAveSpermRange->dMinorLengthAve + pSSpermRange[i].dMinorLengthAve;

		nAveImgSpermNum = nAveImgSpermNum + nSpermNum[i];
	}

	pSAveSpermRange->dAreaMax =  pSAveSpermRange->dAreaMax/nImgFileNum;
	pSAveSpermRange->dAreaMin =  pSAveSpermRange->dAreaMin/nImgFileNum;
	pSAveSpermRange->dAreaAve =  pSAveSpermRange->dAreaAve/nImgFileNum;

	pSAveSpermRange->dMajorLengthMax =  pSAveSpermRange->dMajorLengthMax/nImgFileNum;
	pSAveSpermRange->dMajorLengthMin =  pSAveSpermRange->dMajorLengthMin/nImgFileNum;
	pSAveSpermRange->dMajorLengthAve =  pSAveSpermRange->dMajorLengthAve/nImgFileNum;

	pSAveSpermRange->dMinorLengthMax =  pSAveSpermRange->dMinorLengthMax/nImgFileNum;
	pSAveSpermRange->dMinorLengthMin =  pSAveSpermRange->dMinorLengthMin/nImgFileNum;
	pSAveSpermRange->dMinorLengthAve =  pSAveSpermRange->dMinorLengthAve/nImgFileNum;

	nAveImgSpermNum = nAveImgSpermNum/nImgFileNum;

	return nAveImgSpermNum;
}

//�񱳾�ͼƬ�еľ���Ŀ�����
int getBackImgSpermCount(const char * pcResultPath, int const& nImgFileNum, IplImage *pImgBackGround, ParaRange *pSAveSpermRange)
{
	int nBackgroundImgSpermNum = 0;

	IplConvKernel * pStructureEle = cvCreateStructuringElementEx(3, 3, 1, 1,
			CV_SHAPE_ELLIPSE, NULL);//�����ṹԪ��

	//1.����ͼƬ����
	IplImage * pImgBackImgInv = cvCreateImage(cvGetSize(pImgBackGround),pImgBackGround->depth,pImgBackGround->nChannels);
	cvNot(pImgBackGround, pImgBackImgInv);

	//2.��ֵ�˲�ȥ����
	IplImage * pImgMed = cvCreateImage(cvGetSize(pImgBackGround),pImgBackGround->depth,pImgBackGround->nChannels);
	IplImage * pImgDiff = cvCreateImage(cvGetSize(pImgBackGround),pImgBackGround->depth,pImgBackGround->nChannels);

	cvSmooth( pImgBackImgInv, pImgMed, CV_MEDIAN,9,0,0,0);

    //test
    //cvSaveImage("/storage/emulated/0/Pictures/TT-2/ResultImgs/MedianFilterImg.jpg",pImgMed);

	cvSub(pImgBackImgInv, pImgMed, pImgDiff, NULL);

	cvReleaseImage(&pImgBackImgInv);
	pImgBackImgInv = NULL;
	cvReleaseImage(&pImgMed);
	pImgMed = NULL;

	//3.��˹ƽ��
	IplImage * pImgBackSmth = cvCreateImage(cvGetSize(pImgBackGround),pImgBackGround->depth,pImgBackGround->nChannels);
	cvSmooth( pImgDiff, pImgBackSmth,CV_GAUSSIAN,5,0,1,0);
	cvReleaseImage(&pImgDiff);
	pImgDiff = NULL;

	//4.�Ҷ�����
	stretchGrayValue(pImgBackSmth);

	//5.��ֵ�ָ����Ӧ��ֵ���̶���ֵ��Otu��ֵ��
	IplImage * pImgBinary = getThresholdImage(pImgBackSmth,2);
	cvReleaseImage(&pImgBackSmth);
	pImgBackSmth = NULL;

	//6.������
	cvMorphologyEx( pImgBinary, pImgBinary,
		NULL, pStructureEle,
		CV_MOP_OPEN, 1 );

	//7.��ȡ����ͼƬ�е���������������Ŀ������������Ƶ�ԭͼ��
	char *pcFileNameTemp = (char *)malloc(sizeof(char)*MAXPATHLENGTH);
	if (NULL == pcFileNameTemp)
	{
		cvReleaseImage(&pImgBinary);
		cvReleaseStructuringElement( &pStructureEle );//����ṹԪ��

		nAlgStatus = 0;
		return 0;//�ڴ��쳣
	}
	char *pcImg = (char *)malloc(sizeof(char)*50);
	if (NULL == pcImg)
	{
		free(pcFileNameTemp);
		pcFileNameTemp = NULL;
		cvReleaseImage(&pImgBinary);
		cvReleaseStructuringElement( &pStructureEle );//����ṹԪ��

		nAlgStatus = 0;
		return 0;//�ڴ��쳣
	}

	//�˴���ͼ�޸ģ�Ӧ���Ż�������Ȼ������̫�󣡣���
	for (int i = 0; i < nImgFileNum; i++)
	{
        IplImage * pImgContourShow = NULL;

        strcpy(pcFileNameTemp, pcResultPath);
        sprintf(pcImg, "Result_%03d.jpg", i);
        strcat(pcFileNameTemp, pcImg);

        if (-5 == nAlgStatus)
        {
            pImgContourShow = cvCreateImage(cvGetSize(pImgBackGround),pImgBackGround->depth,pImgBackGround->nChannels);
            cvCopy(pImgBackGround,pImgContourShow);
        }
        else
        {
            pImgContourShow = cvLoadImage(pcFileNameTemp,1);
        }

		nBackgroundImgSpermNum = getBackImgContourInfor(pImgContourShow, pImgBinary, pSAveSpermRange);
		int nSaveStatus = cvSaveImage(pcFileNameTemp,pImgContourShow);//ͼƬ�ļ��洢
		if (0 == nSaveStatus)
		{
			free(pcFileNameTemp);
			pcFileNameTemp = NULL;
			free(pcImg);
			pcImg = NULL;
			cvReleaseImage(&pImgContourShow);
			cvReleaseImage(&pImgBinary);
			cvReleaseStructuringElement( &pStructureEle );//����ṹԪ��

			nAlgStatus = -2;
			return 0;//����д���쳣
		}
		cvReleaseImage(&pImgContourShow);
		pImgContourShow = NULL;
        if (-5 == nAlgStatus)
        {
            break;
        }
	}

	free(pcFileNameTemp);
	pcFileNameTemp = NULL;
	free(pcImg);
	pcImg = NULL;

	//8.�ͷ���Դ
	cvReleaseImage(&pImgBinary);
	cvReleaseStructuringElement( &pStructureEle );//����ṹԪ��

	return nBackgroundImgSpermNum;
}

//��ֵ���ı���ͼƬ����������
int getBackImgContourInfor(IplImage *pImgContourShow, IplImage *pImgBinary, ParaRange *pSAveSpermRange)
{
	//cvZero(pImgBackGround);

	CvMemStorage *storage = cvCreateMemStorage();
	CvSeq* firstcontour = NULL;

	int nAllSpermNum = 0;
	double dConArea;
	//double dConMajLength;
	//double dConMinLength;

	CvBox2D GeoInfor;
	CvContourScanner scanner = cvStartFindContours(pImgBinary,
								storage,
								sizeof(CvContour),
								CV_RETR_LIST,
								CV_CHAIN_APPROX_SIMPLE,
								cvPoint(0,0));
	CvSeq * cTemp =NULL;

	while( (cTemp = cvFindNextContour(scanner) ) != NULL)//��ʼ����  
	{ 
		//���
		dConArea = fabs(cvContourArea(cTemp,CV_WHOLE_SEQ,0));

		//�����������dMinArea������С��dMaxArea�������£���֮����ɾ����
		if(dConArea > 0.7*pSAveSpermRange->dAreaMin && dConArea < 1.5*pSAveSpermRange->dAreaMax && nAllSpermNum < MAXSPERMNUM )
		{  	
			/*/���ģ������ᣬ�Ƕȣ��������ж�Ч�����Ǻܺã��Ȳ���
			GeoInfor = cvMinAreaRect2(cTemp, NULL);
			if (GeoInfor.size.width >= GeoInfor.size.height)
			{
				dConMajLength = GeoInfor.size.width;
				dConMinLength = GeoInfor.size.height;
			}else
			{
				dConMajLength = GeoInfor.size.height;
				dConMinLength = GeoInfor.size.width;
			}

			if ((dConMajLength >= pSAveSpermRange->dMajorLengthMin && dConMajLength <= pSAveSpermRange->dMajorLengthMax)
				&& (dConMinLength >= pSAveSpermRange->dMinorLengthMin && dConMinLength <= pSAveSpermRange->dMinorLengthMax))
			{
				//���Ʒ����������������
				cvDrawContours(pImgContourShow,cTemp,CV_RGB(255,255,255),CV_RGB(255,255,255),0);
				nAllSpermNum = nAllSpermNum+1;
			}else
			{
				cvSubstituteContour(scanner,NULL);//ɾ����ǰ������
			}		
			*/

			//���Ʒ����������������
			cvDrawContours(pImgContourShow,cTemp,CV_RGB(255,0,0),CV_RGB(255,255,255),0);
			nAllSpermNum = nAllSpermNum+1;
		}
		else  
		{  
			cvSubstituteContour(scanner,NULL);//ɾ����ǰ������
		}  
	}
	firstcontour = cvEndFindContours(&scanner);//���ҵ����������ص�firstContour�С�

	cvReleaseMemStorage(&storage); 

	return nAllSpermNum;
}

//��ȡ����Ӹ���
int getTwoImgAliveSpermCount(IplImage *pImgSubScr1,IplImage *pImgSubScr2, IplImage *pImgContourShow, ParaRange *pSAveSpermRange)
{
	int nAliveSpermNum = 0;

	IplConvKernel * pStructureEle = cvCreateStructuringElementEx(3, 3, 1, 1,
			CV_SHAPE_ELLIPSE, NULL);//�����ṹԪ��

	//1.ͼ�����
	IplImage * pImgDiff = cvCreateImage(cvGetSize(pImgSubScr1),pImgSubScr1->depth,pImgSubScr1->nChannels);
	cvSub(pImgSubScr2,pImgSubScr1,pImgDiff);

	//2.��˹ƽ��
	IplImage * pImgSmth = cvCreateImage(cvGetSize(pImgSubScr1),pImgSubScr1->depth,pImgSubScr1->nChannels);
	cvSmooth( pImgDiff, pImgSmth,CV_GAUSSIAN,5,0,1,0);
	cvReleaseImage(&pImgDiff);
	pImgDiff = NULL;

	//3.ͼ��Ҷ�����
	stretchGrayValue(pImgSmth);

	//4.��ֵ�ָ����Ӧ��ֵ���̶���ֵ��Otu��ֵ��
	IplImage * pImgBinary = getThresholdImage(pImgSmth,3);
	cvReleaseImage(&pImgSmth);
	pImgSmth = NULL;

	//6.����ͼƬ֮�����������
	nAliveSpermNum = getSub2ImgContourInfor(pImgBinary, pImgContourShow, pSAveSpermRange);
	cvReleaseImage(&pImgBinary);
	pImgBinary = NULL;

	cvReleaseStructuringElement( &pStructureEle );//����ṹԪ��

	return nAliveSpermNum;
}

//����ͼƬ֮�����������
int getSub2ImgContourInfor(IplImage *pImgBinary,  IplImage *pImgContourShow, ParaRange *pSAveSpermRange)
{
	//cvZero(pImgContourShow);

	CvMemStorage *storage = cvCreateMemStorage();
	CvSeq* firstcontour = NULL;

	int nAllSpermNum = 0;
	double dConArea;
	//double dConMajLength;
	//double dConMinLength;

	CvBox2D GeoInfor;
	CvContourScanner scanner = cvStartFindContours(pImgBinary,
								storage,
								sizeof(CvContour),
								CV_RETR_LIST,
								CV_CHAIN_APPROX_SIMPLE,
								cvPoint(0,0));
	CvSeq * cTemp =NULL;
	while( (cTemp = cvFindNextContour(scanner) ) != NULL)//��ʼ����  
	{ 
		//���
		dConArea = fabs(cvContourArea(cTemp,CV_WHOLE_SEQ,0));

		//�����������pSAveSpermRange->dAreaMin������С��pSAveSpermRange->dAreaMax�������£���֮����ɾ����
		if(dConArea > 0.6*pSAveSpermRange->dAreaMin && dConArea < 1.5*pSAveSpermRange->dAreaMax && nAllSpermNum < MAXSPERMNUM )
		{ 
			//���Ʒ�������������
			cvDrawContours(pImgContourShow,cTemp,CV_RGB(0,255,0),CV_RGB(255,255,255),0);
			nAllSpermNum = nAllSpermNum + 1;

			/*//ע�ʹ˶�ԭ�򣺼��볤�����жϺ����ӱ��޳���̫������
			//���ģ������ᣬ�Ƕ�
			GeoInfor = cvMinAreaRect2(cTemp, NULL);
			if (GeoInfor.size.width >= GeoInfor.size.height)
			{
				dConMajLength = GeoInfor.size.width;
				dConMinLength = GeoInfor.size.height;
			}else
			{
				dConMajLength = GeoInfor.size.height;
				dConMinLength = GeoInfor.size.width;
			}

			if ((dConMajLength >= 0.7*pSAveSpermRange->dMajorLengthMin && dConMajLength <= pSAveSpermRange->dMajorLengthMax)
				&& (dConMinLength >= 0.7*pSAveSpermRange->dMinorLengthMin && dConMinLength <= pSAveSpermRange->dMinorLengthMax))
			{
				//���Ʒ�������������
				cvDrawContours(pImgContourShow,cTemp,CV_RGB(255,255,255),CV_RGB(255,255,255),0);
				nAllSpermNum = nAllSpermNum+1;
			}else
			{
				cvSubstituteContour(scanner,NULL);//ɾ����ǰ������
			}
			*/
		}
		else  
		{  
			cvSubstituteContour(scanner,NULL);//ɾ����ǰ������
		}  
	}
	firstcontour = cvEndFindContours(&scanner);//���ҵ����������ص�firstContour�С�

	//��Դ�ͷ�
	cvReleaseMemStorage(&storage);

	return nAllSpermNum;
}

//ͼ��Ҷ�����
void stretchGrayValue(IplImage *pImgScr)
{
	IplImage * pImgTemp1 = cvCreateImage(cvGetSize(pImgScr),pImgScr->depth,pImgScr->nChannels);
	IplImage * pImgTemp2 = cvCreateImage(cvGetSize(pImgScr),pImgScr->depth,pImgScr->nChannels);

	double dMinValue;
	double dMaxValue;
	double dDelta;
	cvMinMaxLoc( pImgScr, &dMinValue, &dMaxValue,
		NULL,
		NULL,
		NULL );
	dDelta = 255 / (dMaxValue - dMinValue);
	if (dDelta < 1)
	{
		dDelta = 1;
	}
	else if (dDelta > 255)
	{
		dDelta = 255;
	}
	cvSubS( pImgScr, cvScalar(dMinValue), pImgTemp1,NULL);
	cvSet( pImgTemp2, cvScalar(dDelta),NULL);
	cvMul( pImgTemp1, pImgTemp2, pImgScr, 1);

	cvReleaseImage(&pImgTemp1);
	cvReleaseImage(&pImgTemp2);
}
