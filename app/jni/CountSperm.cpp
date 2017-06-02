#include "CountSperm.h"

#include<opencv/cv.h>   //cv.h OpenCV的主要功能头文件
#include <opencv/highgui.h>//显示图像用的，因为用到了显示图片

using namespace std;
using namespace cv;

//图像的ROI限定，相机虚焦问题引起
int nWidth = 1000;
int nHeight = 1000;

//精子特征初始化
ParaRange AveSpermRangeIni = {13,35,24,5,10,7,3,6,4};

//运行状态提示
/*说明
* nAlgStatus = 1;//分析成功
* nAlgStatus = 0;//内存异常
* nAlgStatus = -1;//输入参数异常
* nAlgStatus = -2;//数据写入异常
* nAlgStatus = -3;//未找到图像或图像文件名不规范或没有数据读取权限
* nAlgStatus = -4;//样本图像张数不够，无法进一步分析
* nAlgStatus = -5;//样本异常，活动精子数太少，结果可能不准确
* nAlgStatus = -6;//样本图像背景异常，可能样本在整体晃动
*/
int nAlgStatus = 1;

//精子目标的面积限定
double dMinArea = 8;//最小面积,pixel
double dMaxArea = 400;//最大面积,pixel

//获取视频图片张数
int getVideoFrameNum(char *pcVideoPath);

//获取图片地址及数量
int getImgFileSeq(const char *pcImgPath, char **ppcImageFile);

//背景图像生成
void genBackgroundImg(IplImage * pImgBackGround, const char **ppcFilePath, int const& nImgFileNum);

//获取精子数量
void getSpermCount(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, SSettings const& SParaInput, double *pdTestResult);

//获取精子总数（平均每张图）
int getSpermCountABC(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, IplImage * pImgBackGround, ParaRange *pSAveSpermRange);

//获取活动精子数量（平均两张图）
int getActiveSpermCount(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, ParaRange *pSAveSpermRange);

//获取子图像
IplImage *clipCenterImage(IplImage *pImgScr);

//图像灰度拉伸
void stretchGrayValue(IplImage *pImgScr);

//阈值分割
IplImage *getThresholdImage(IplImage *pImgSubScr, int nFlag);

//求数组平均值和标准差
double *calAveStd(const double *pdData, int nSpermNum);

//获取目标轮廓及其几何信息
int getImgContourInfor(IplImage *pImgBinary, IplImage *pImgContourShow, SpermInfor *pSSpermInfor);

//二值化的背景图片的轮廓处理
int getBackImgContourInfor(IplImage *pImgBackGround, IplImage *pImgBinary, ParaRange *pSAveSpermRange);

//两张图片之差的轮廓处理
int getSub2ImgContourInfor(IplImage *pImgBinary,  IplImage *pImgContourShow, ParaRange *pSAveSpermRange);

//统计目标几何特征范围
ParaRange getSpermParaRange(SpermInfor *pSSpermInfor, int nSpermNum);

//统计平均每幅图的精子数量
int getAveSpermCount( ParaRange *pSAveSpermRange, ParaRange *pSSpermRange, int const& nImgFileNum, int *nSpermNum);

//获取背景图片中的精子目标个数
int getBackImgSpermCount(const char * pcResultPath, int const& nImgFileNum, IplImage *pImgBackGround, ParaRange *pSAveSpermRange);

//获取活动精子个数
int getTwoImgAliveSpermCount(IplImage *pImgSubScr1,IplImage *pImgSubScr2, IplImage *pImgContourShow, ParaRange *pSAveSpermRange);

int getSpermCountMain(const char *pcImgPath, const char *pcResultPath, const double *dVar, double *pdTestResult)
{
	//输入参数提取
	if ((dVar[0] < 1e-4) || (dVar[1] < 3) || (dVar[2] < 1) || (dVar[3] < 1e-4) )
	{
		nAlgStatus = -1;
		return nAlgStatus;//输入参数异常
	}
	SSettings SParaInput = {
		dVar[0],//
		dVar[1],//
		dVar[2],//
		dVar[3]//
	};

	char *pcImgFileTemp[FILENUM] = {NULL};

	//获取图片地址及数量
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

			nAlgStatus = 0;//内存异常
			return nAlgStatus;
		}
	}

	int nImgNum = getImgFileSeq(pcImgPath,pcImgFileTemp);

	if (1 == nAlgStatus)
	{
		if (nImgNum == 0)
		{
			nAlgStatus = -3;//未找到图像或图像文件名不规范或没有数据读取权限
		}
		else if (nImgNum >= 1 && nImgNum <= 9)
		{
			nAlgStatus = -4;//样本图像张数不够，无法进一步分析
		}
		else if (nImgNum >= 10)//进入主程序
		{
			const char *ppcImageFile[FILENUM] = {NULL};
			for (int i = 0; i < nImgNum; i++)
			{
				ppcImageFile[i] = pcImgFileTemp[i];//用于const char* 与const char转换
			}
			getSpermCount(ppcImageFile, pcResultPath, nImgNum, SParaInput, pdTestResult);
		}
	}	
	
	//资源释放
	for (int i = 0; i < FILENUM; i++)
	{
		free(pcImgFileTemp[i]);
		pcImgFileTemp[i] = NULL;
	}

	return nAlgStatus;
}

//获取图片地址及数量
int getImgFileSeq(const char *pcImgPath, char **ppcImageFile)
{
	int i;
	int nImgFileNum = 0;	
	char *pcImgName = (char *)malloc(sizeof(char)*50);
	if ( NULL == pcImgName )
	{
		nAlgStatus = 0;
		return 0;//内存异常
	}

	for(i = 0 ; i < FILENUM; i++)
	{
		char *pcFileNameTemp = NULL;
		FILE *fp = NULL;

		pcFileNameTemp = (char *)malloc(sizeof(char)*MAXPATHLENGTH);
		if ( NULL == pcFileNameTemp )
		{
			nAlgStatus = 0;
			break;//内存异常
		}

		strcpy(pcFileNameTemp, pcImgPath);		
		sprintf(pcImgName, "%03d.jpg", i);
		strcat(pcFileNameTemp, pcImgName); 

		fp = fopen(pcFileNameTemp, "r");   //没有这个文件则继续
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

//获取精子数量
void getSpermCount(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, SSettings const& SParaInput, double *pdTestResult)
{
	int nTotalSpermNum = 0;//精子总数A+B+C+D
	int nAliveSpermNum = 0;//活动精子数A+B+C
	int nActiveSpermNum = 0;//运动活跃的精子数A+B
	int nDeadSpermNum = 0;//完全不动的精子数D
	double dSLNum = 0;//直线运动精子数，单位亿个

	double dAliveSpermRatio = 0;//精子活率 = 活动精子数/精子总数
	double dActiveSpermRatio = 0;//精子活力 = A+B

	double dTotaSpermDensity;//精子密度,单位：10^6/mL
	double dAliveSpermDensity;//活动精子密度,单位：10^6/mL

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
		//背景图像生成
		pImgScrTemp = cvLoadImage(ppcFilePath[0],0);//0表示强制转化读取图像为灰度图
		pImgBackGround = clipCenterImage(pImgScrTemp);//分配内存
		genBackgroundImg(pImgBackGround, ppcFilePath, nImgFileNum);

		dRatio = nWidth * nHeight * SParaInput.dRatioImg * SParaInput.dRatioImg * SParaInput.dSampleDepth;
		if ((dRatio >= -EPSINON) && (dRatio <= EPSINON))
		{
			nAlgStatus = -1;
			break;//输入参数异常
		}else
		{
			//dRatio = dRatio * (1e-6);//单位：百万个/毫升
			dRatio = dRatio * (1e-4);//单位：亿个/毫升
		}

		//活动的精子数量A+B+C
		nAliveSpermNum = getSpermCountABC(ppcFilePath, pcResultPath, nImgFileNum, pImgBackGround, pSAveSpermRange);
		if (nAlgStatus == 0 || nAlgStatus == -2)
		{
			break;
		}

		//完全不动的精子数D
		if (nAliveSpermNum <= 5)
		{
			nAlgStatus = -5;//样本异常，活动精子数太少，结果可能不准确
			nDeadSpermNum = getBackImgSpermCount(pcResultPath, nImgFileNum, pImgBackGround,&AveSpermRangeIni);
		}else
		{
			nDeadSpermNum = getBackImgSpermCount(pcResultPath, nImgFileNum, pImgBackGround,pSAveSpermRange);
		}
		
		if (0 == nAlgStatus || -2 == nAlgStatus)
		{
			break;
		}

		//运动活跃的精子数A+B
		if (nAliveSpermNum > 5)
		{
			nActiveSpermNum = getActiveSpermCount(ppcFilePath, pcResultPath, nImgFileNum, pSAveSpermRange);
			if (0 == nAlgStatus || -2 == nAlgStatus)
			{
				break;
			}
			if (nActiveSpermNum > nAliveSpermNum)
			{
				nAlgStatus = -6;//样本图像背景异常，可能样本在整体晃动
				break;
			}
		}
		
		nTotalSpermNum = nAliveSpermNum + nDeadSpermNum;

		dAliveSpermRatio = nAliveSpermNum/(nTotalSpermNum + 0.000001);
		dActiveSpermRatio = nActiveSpermNum/(nTotalSpermNum + 0.000001);
		dTotaSpermDensity = nTotalSpermNum/(dRatio);
		dAliveSpermDensity = nAliveSpermNum/(dRatio);
		dSLNum = dTotaSpermDensity * dActiveSpermRatio * SParaInput.dVolume;//按活力计算，单位亿个

		pdTestResult[0] = nTotalSpermNum;
		pdTestResult[1] = dTotaSpermDensity;
		pdTestResult[2] = nAliveSpermNum;
		pdTestResult[3] = dAliveSpermDensity;
		pdTestResult[4] = dAliveSpermRatio;
		pdTestResult[5] = dActiveSpermRatio;

		pdTestResult[7] = dSLNum;

		pdTestResult[17] = dAliveSpermRatio - dActiveSpermRatio;//C级
		pdTestResult[18] = 1 - dAliveSpermRatio;//D级
		pdTestResult[19] = dActiveSpermRatio;//PR, A+B
		pdTestResult[20] = pdTestResult[17];//NP, C级
		pdTestResult[21] = pdTestResult[18];//IM, D级
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

	//背景图像生成（此处默认为白色背景）
	pImgScrTemp = cvLoadImage(ppcFilePath[0],0);//0表示强制转化读取图像为灰度图
	pImgSubScr1 = clipCenterImage(pImgScrTemp);//提取处理区域
	cvReleaseImage(&pImgScrTemp);

	//求两张图片中较大的元素
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
	int nAveImgSpermNum = 0;//平均每张图的精子数
	int nNormalSpermNum;
	int nLargeSpermNum;
	int nTotalSpermNum = 0;//精子总数
	double dLargeArea;

	int *nSpermNum = (int *)malloc(sizeof(int)*nImgFileNum);//每张图的精子数
	if (NULL == nSpermNum)
	{
		nAlgStatus = 0;
		return 0;//内存异常
	}

	ParaRange *pSSpermRange = (ParaRange *)malloc(sizeof(ParaRange)*nImgFileNum);//每张图精子的特征范围
	if (NULL == pSSpermRange)
	{
		free(nSpermNum);
		nSpermNum = NULL;
		nAlgStatus = 0;
		return 0;//内存异常
	}

	//开始图像处理//
	for (i = 0; i < nImgFileNum; i++)
	{
		//1.读取图像并提取子图
		IplImage * pImgScrTemp = cvLoadImage(ppcFilePath[i],0);
		IplImage * pImgScr = clipCenterImage(pImgScrTemp);
		cvReleaseImage(&pImgScrTemp);
		pImgScrTemp = NULL;

		//2.减背景
		IplImage * pImgScrDiff = cvCreateImage(cvGetSize(pImgScr),pImgScr->depth,pImgScr->nChannels);
		cvSub(pImgBackGround, pImgScr, pImgScrDiff, NULL);
		cvReleaseImage(&pImgScr);
		pImgScr = NULL;

		//3.高斯平滑
		IplImage * pImgScrDiffSmth = cvCreateImage(cvGetSize(pImgScrDiff),pImgScrDiff->depth,pImgScrDiff->nChannels);
		cvSmooth( pImgScrDiff, pImgScrDiffSmth,CV_GAUSSIAN,5,0,1,0);
		cvReleaseImage(&pImgScrDiff);
		pImgScrDiff = NULL;

		//4.图像灰度拉伸
		stretchGrayValue(pImgScrDiffSmth);

		//5.阈值分割（自适应阈值，固定阈值，Otu阈值）
		nThreshFlag = 1;
		IplImage * pImgBinary = getThresholdImage(pImgScrDiffSmth,nThreshFlag);
		cvReleaseImage(&pImgScrDiffSmth);
		pImgScrDiffSmth = NULL;

		//6.开运算
		IplConvKernel * pStructureEle = cvCreateStructuringElementEx(3, 3, 1, 1,
			CV_SHAPE_ELLIPSE, NULL);//创建结构元素
		cvMorphologyEx( pImgBinary, pImgBinary,
			NULL, pStructureEle,
			CV_MOP_OPEN, 1 );	
		cvReleaseStructuringElement( &pStructureEle );//清除结构元素

		//7.区域轮廓提取、绘制、精子特征获取
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
			return 0;//内存异常
		}
		
		pImgScrTemp = cvLoadImage(ppcFilePath[i],1);
		pImgScr = clipCenterImage(pImgScrTemp);
		IplImage * pImgContourShow = cvCreateImage(cvGetSize(pImgScr),pImgScr->depth,pImgScr->nChannels);
		//cvZero(pImgContourShow);
		cvCopy(pImgScr,pImgContourShow); //复制图像,位深、尺寸、通道需保持一致
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

			return 0;//样本异常，活动精子数太少，结果可能不准确
		}

		//图像存储
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
			return 0;//内存异常
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
			return 0;//内存异常
		}

		strcpy(pcFileNameTemp, pcResultPath);
		sprintf(pcImg, "Result_%03d.jpg", i);
		strcat(pcFileNameTemp, pcImg); 
		int nSaveStatus = cvSaveImage(pcFileNameTemp,pImgContourShow);//图片文件存储
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
			return 0;//数据写入异常
		}

		//8.计算筛选条件（面积、长轴、短轴）
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

			return 0;//内存异常
		}

		//9.计算该幅图的精子个数
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

		//资源释放
		free(pcFileNameTemp);
		pcFileNameTemp = NULL;
		free(pSSpermInfor);
		pSSpermInfor = NULL;
		free(pcImg);
		pcImg = NULL;
	}
	
	//10.统计平均每幅图的精子数量
	nAveImgSpermNum = getAveSpermCount( pSAveSpermRange, pSSpermRange, nImgFileNum, nSpermNum);

	//资源释放
	free(nSpermNum);	
	nSpermNum = NULL;
	free(pSSpermRange);
	pSSpermRange = NULL;

	return nAveImgSpermNum;
}

//获取活动精子数量（平均两张图）
int getActiveSpermCount(const char **ppcFilePath, const char * pcResultPath, int const& nImgFileNum, ParaRange *pSAveSpermRange)
{
	int nAliveSpermNum = 0;
	int *nSingleImgAliveSpermNum = (int *)malloc(sizeof(int)*nImgFileNum);
	if (NULL == nSingleImgAliveSpermNum)
	{
		nAlgStatus = 0;
		return 0;//内存异常
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
		return 0;//内存异常
	}

	char *pcImg = (char *)malloc(sizeof(char)*50);
	if (NULL == pcImg)
	{
		free(nSingleImgAliveSpermNum);
		nSingleImgAliveSpermNum = NULL;
		free(pcFileNameTemp);
		pcFileNameTemp = NULL;

		nAlgStatus = 0;
		return 0;//内存异常
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
		nSingleImgAliveSpermNum[i] = getTwoImgAliveSpermCount(pImgSubScr1,pImgSubScr2,pImgContourShow, pSAveSpermRange);//注：图2-图1，因为白背景，图1被保留

		int nSaveStatus = cvSaveImage(pcFileNameTemp,pImgContourShow);//图片文件存储
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
			return 0;//数据写入异常
		}

		nAliveSpermNum = nAliveSpermNum + nSingleImgAliveSpermNum[i];
	}
	nAliveSpermNum = nAliveSpermNum/(nImgFileNum);

	//资源释放
	free(nSingleImgAliveSpermNum);
	nSingleImgAliveSpermNum = NULL;
	free(pcFileNameTemp);
	pcFileNameTemp = NULL;
	free(pcImg);
	pcImg = NULL;

	return nAliveSpermNum;
}

//获取子图像
IplImage *clipCenterImage(IplImage *pImgScr)
{
	IplImage *imgResult = NULL;
	CvRect SImROI;

	//图像ROI设定
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
	cvSetImageROI(pImgScr,SImROI);//设置源图像ROI
	imgResult = cvCreateImage(cvSize(nWidth,nHeight),pImgScr->depth,pImgScr->nChannels);//创建目标图像
	cvCopy(pImgScr,imgResult); //复制图像
	cvResetImageROI(pImgScr);//源图像用完后，清空ROI

	return imgResult;
}

//阈值分割
IplImage *getThresholdImage(IplImage *pImgSubScr, int nFlag)
{
	IplImage * pImgBinary =cvCreateImage(cvGetSize(pImgSubScr),pImgSubScr->depth,pImgSubScr->nChannels);

	/*
	IplImage * pImgBinaryTest = cvCreateImage(cvGetSize(pImgScrDiffSmth),pImgScrDiffSmth->depth,pImgScrDiffSmth->nChannels);
	cvAdaptiveThreshold( pImgScrDiffSmth, pImgBinary, 255,CV_ADAPTIVE_THRESH_GAUSSIAN_C,
                                  CV_THRESH_BINARY,11,-5);//自适应阈值，分割效果不太好，容易产生噪音
	*/

	//NOTE: Currently, the Otsu’s method is implemented only for 8-bit images.
	if (pImgSubScr->depth == 8)
	{
		/*do OTSU thresh with OpenCV2.4.10
		double dThreshValue = cvThreshold( pImgSubScr, pImgBinary,
			0, 255,
			CV_THRESH_BINARY|THRESH_OTSU);//Otsu阈值分割
		*/

		//do OTSU thresh with OpenCV3.0
		double dThreshValue = cvThreshold( pImgSubScr, pImgBinary,
			0, 255,
			CV_THRESH_BINARY|CV_THRESH_OTSU);//Otsu阈值分割

		switch (nFlag)
		{
			//表示处理去背景后的单张精子图片
		case 1 : 
			dThreshValue = 1.7*dThreshValue;
			break;

			//表示处理背景图片
		case 2 : 
			dThreshValue = 1.5*dThreshValue;
			break;

			//表示处理两张之差的图片
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
			CV_THRESH_BINARY);//固定阈值分割
	}

	return pImgBinary;
}

//平均值和标准差计算
double *calAveStd(const double *pdData, int nSpermNum)
{
	int i;
	double *dAveStdSub = (double *)malloc(sizeof(double)*2);
	double dSum = 0;

	//平均值计算	
	double dMean;
	
	for (i = 0; i < nSpermNum; i++)
	{
		dSum = dSum + pdData[i];
	}
	dMean = dSum/nSpermNum;

	//标准差计算
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

//获取目标轮廓及其几何信息
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
	while( (cTemp = cvFindNextContour(scanner) ) != NULL)//开始查找
	{ 

		//面积
		dConArea = fabs(cvContourArea(cTemp,CV_WHOLE_SEQ,0));

		//轮廓面积大于dMinArea，并且小于dMaxArea，则留下，反之，则删除。 
		if(dConArea > dMinArea && dConArea < dMaxArea && nAllSpermNum < MAXSPERMNUM )
		{  
			//绘制符合面积条件的轮廓
			cvDrawContours(pImgContourShow,cTemp,CV_RGB(0,0,255),CV_RGB(255,255,255),0);

			//质心，长短轴，角度
			GeoInfor = cvMinAreaRect2(cTemp, NULL);//最小外接矩形

			/*
			if (cTemp->total > 6)
			{
				GeoInfor = cvFitEllipse2(cTemp);//最小外接椭圆，不稳定，容易内部崩溃
			}else
			{
				GeoInfor = cvMinAreaRect2(cTemp, NULL);//最小外接矩形

			}
			*/

			//轮廓信息存储
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
			cvSubstituteContour(scanner,NULL);//删除当前的轮廓
		}  
	}
	firstcontour = cvEndFindContours(&scanner);//把找到的轮廓返回到firstContour中。

	//资源释放
	cvReleaseMemStorage(&storage); 

	return nAllSpermNum;
}

//统计目标几何特征范围
ParaRange getSpermParaRange(SpermInfor *pSSpermInfor, int nSpermNum)
{
	ParaRange pSSpermRange = {0,0,0,0,0,0,0,0,0};
	double *pdAveStd = NULL;
	double *pdData = (double *)malloc(sizeof(double)*nSpermNum);
	if (NULL == pdData)
	{
		nAlgStatus = 0;
		return pSSpermRange;//内存异常
	}

	int i;

	//面积范围
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

	//长轴范围
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

	//短轴范围
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

//统计平均每幅图的精子数量
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

//获背景图片中的精子目标个数
int getBackImgSpermCount(const char * pcResultPath, int const& nImgFileNum, IplImage *pImgBackGround, ParaRange *pSAveSpermRange)
{
	int nBackgroundImgSpermNum = 0;

	IplConvKernel * pStructureEle = cvCreateStructuringElementEx(3, 3, 1, 1,
			CV_SHAPE_ELLIPSE, NULL);//创建结构元素

	//1.背景图片反向
	IplImage * pImgBackImgInv = cvCreateImage(cvGetSize(pImgBackGround),pImgBackGround->depth,pImgBackGround->nChannels);
	cvNot(pImgBackGround, pImgBackImgInv);

	//2.中值滤波去背景
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

	//3.高斯平滑
	IplImage * pImgBackSmth = cvCreateImage(cvGetSize(pImgBackGround),pImgBackGround->depth,pImgBackGround->nChannels);
	cvSmooth( pImgDiff, pImgBackSmth,CV_GAUSSIAN,5,0,1,0);
	cvReleaseImage(&pImgDiff);
	pImgDiff = NULL;

	//4.灰度拉伸
	stretchGrayValue(pImgBackSmth);

	//5.阈值分割（自适应阈值，固定阈值，Otu阈值）
	IplImage * pImgBinary = getThresholdImage(pImgBackSmth,2);
	cvReleaseImage(&pImgBackSmth);
	pImgBackSmth = NULL;

	//6.开运算
	cvMorphologyEx( pImgBinary, pImgBinary,
		NULL, pStructureEle,
		CV_MOP_OPEN, 1 );

	//7.获取背景图片中的区域轮廓、精子目标个数，并绘制到原图中
	char *pcFileNameTemp = (char *)malloc(sizeof(char)*MAXPATHLENGTH);
	if (NULL == pcFileNameTemp)
	{
		cvReleaseImage(&pImgBinary);
		cvReleaseStructuringElement( &pStructureEle );//清除结构元素

		nAlgStatus = 0;
		return 0;//内存异常
	}
	char *pcImg = (char *)malloc(sizeof(char)*50);
	if (NULL == pcImg)
	{
		free(pcFileNameTemp);
		pcFileNameTemp = NULL;
		cvReleaseImage(&pImgBinary);
		cvReleaseStructuringElement( &pStructureEle );//清除结构元素

		nAlgStatus = 0;
		return 0;//内存异常
	}

	//此处读图修改，应该优化处理，不然计算量太大！！！
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
		int nSaveStatus = cvSaveImage(pcFileNameTemp,pImgContourShow);//图片文件存储
		if (0 == nSaveStatus)
		{
			free(pcFileNameTemp);
			pcFileNameTemp = NULL;
			free(pcImg);
			pcImg = NULL;
			cvReleaseImage(&pImgContourShow);
			cvReleaseImage(&pImgBinary);
			cvReleaseStructuringElement( &pStructureEle );//清除结构元素

			nAlgStatus = -2;
			return 0;//数据写入异常
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

	//8.释放资源
	cvReleaseImage(&pImgBinary);
	cvReleaseStructuringElement( &pStructureEle );//清除结构元素

	return nBackgroundImgSpermNum;
}

//二值化的背景图片的轮廓处理
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

	while( (cTemp = cvFindNextContour(scanner) ) != NULL)//开始查找  
	{ 
		//面积
		dConArea = fabs(cvContourArea(cTemp,CV_WHOLE_SEQ,0));

		//轮廓面积大于dMinArea，并且小于dMaxArea，则留下，反之，则删除。
		if(dConArea > 0.7*pSAveSpermRange->dAreaMin && dConArea < 1.5*pSAveSpermRange->dAreaMax && nAllSpermNum < MAXSPERMNUM )
		{  	
			/*/质心，长短轴，角度，这三个判断效果不是很好，先不用
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
				//绘制符合面积条件的轮廓
				cvDrawContours(pImgContourShow,cTemp,CV_RGB(255,255,255),CV_RGB(255,255,255),0);
				nAllSpermNum = nAllSpermNum+1;
			}else
			{
				cvSubstituteContour(scanner,NULL);//删除当前的轮廓
			}		
			*/

			//绘制符合面积条件的轮廓
			cvDrawContours(pImgContourShow,cTemp,CV_RGB(255,0,0),CV_RGB(255,255,255),0);
			nAllSpermNum = nAllSpermNum+1;
		}
		else  
		{  
			cvSubstituteContour(scanner,NULL);//删除当前的轮廓
		}  
	}
	firstcontour = cvEndFindContours(&scanner);//把找到的轮廓返回到firstContour中。

	cvReleaseMemStorage(&storage); 

	return nAllSpermNum;
}

//获取活动精子个数
int getTwoImgAliveSpermCount(IplImage *pImgSubScr1,IplImage *pImgSubScr2, IplImage *pImgContourShow, ParaRange *pSAveSpermRange)
{
	int nAliveSpermNum = 0;

	IplConvKernel * pStructureEle = cvCreateStructuringElementEx(3, 3, 1, 1,
			CV_SHAPE_ELLIPSE, NULL);//创建结构元素

	//1.图像相减
	IplImage * pImgDiff = cvCreateImage(cvGetSize(pImgSubScr1),pImgSubScr1->depth,pImgSubScr1->nChannels);
	cvSub(pImgSubScr2,pImgSubScr1,pImgDiff);

	//2.高斯平滑
	IplImage * pImgSmth = cvCreateImage(cvGetSize(pImgSubScr1),pImgSubScr1->depth,pImgSubScr1->nChannels);
	cvSmooth( pImgDiff, pImgSmth,CV_GAUSSIAN,5,0,1,0);
	cvReleaseImage(&pImgDiff);
	pImgDiff = NULL;

	//3.图像灰度拉伸
	stretchGrayValue(pImgSmth);

	//4.阈值分割（自适应阈值，固定阈值，Otu阈值）
	IplImage * pImgBinary = getThresholdImage(pImgSmth,3);
	cvReleaseImage(&pImgSmth);
	pImgSmth = NULL;

	//6.两张图片之差的轮廓处理
	nAliveSpermNum = getSub2ImgContourInfor(pImgBinary, pImgContourShow, pSAveSpermRange);
	cvReleaseImage(&pImgBinary);
	pImgBinary = NULL;

	cvReleaseStructuringElement( &pStructureEle );//清除结构元素

	return nAliveSpermNum;
}

//两张图片之差的轮廓处理
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
	while( (cTemp = cvFindNextContour(scanner) ) != NULL)//开始查找  
	{ 
		//面积
		dConArea = fabs(cvContourArea(cTemp,CV_WHOLE_SEQ,0));

		//轮廓面积大于pSAveSpermRange->dAreaMin，并且小于pSAveSpermRange->dAreaMax，则留下，反之，则删除。
		if(dConArea > 0.6*pSAveSpermRange->dAreaMin && dConArea < 1.5*pSAveSpermRange->dAreaMax && nAllSpermNum < MAXSPERMNUM )
		{ 
			//绘制符合条件的轮廓
			cvDrawContours(pImgContourShow,cTemp,CV_RGB(0,255,0),CV_RGB(255,255,255),0);
			nAllSpermNum = nAllSpermNum + 1;

			/*//注释此段原因：加入长短轴判断后，粒子被剔除的太严重了
			//质心，长短轴，角度
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
				//绘制符合条件的轮廓
				cvDrawContours(pImgContourShow,cTemp,CV_RGB(255,255,255),CV_RGB(255,255,255),0);
				nAllSpermNum = nAllSpermNum+1;
			}else
			{
				cvSubstituteContour(scanner,NULL);//删除当前的轮廓
			}
			*/
		}
		else  
		{  
			cvSubstituteContour(scanner,NULL);//删除当前的轮廓
		}  
	}
	firstcontour = cvEndFindContours(&scanner);//把找到的轮廓返回到firstContour中。

	//资源释放
	cvReleaseMemStorage(&storage);

	return nAllSpermNum;
}

//图像灰度拉伸
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
