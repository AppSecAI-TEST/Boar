/*
* Copyright (c) 2017.06 深圳创怀医疗科技有限公司
* All right reserved.
*
* 文件名称：CountSperm.h
* 摘要：用于分析系列图片中精子的基本参数和运动特征
* 底层算法库：OpenCV3.0.0，可兼容OpenCV2.4.10
*
	系统参数定义：
	double dRatioImg = 4/7.0;//图像放大率,um/pixel
	double dSampleDepth = 10;//样本厚度，芯片杯6或7um
	double dVolume = 80;// 每剂容量，单位ml
	double dFrameRate = 15;//相机采样频率

	输入参考：
	char *pcImgPath = "E:/SpermAnalysis/Analysis/Samples/TT-2/";//图像地址
	char *pcResultPath = "E:/SpermAnalysis/Analysis/Samples/TT-2/ResultImgs/";//分析结果地址
	double dVar[4] = {dRatioImg, dSampleDepth, dVolume, dFrameRate};//系统参数

	调用参考：
	double pdTestResult[22] = {0};
	int nStatus = getSpermCountMain(pcImgPath, pcResultPath, dVar, pdTestResult);//算法运行状态

	算法运行状态说明：
	* nStatus = 1;//分析成功
	* nStatus = 0;//内存异常
	* nStatus = -1;//输入参数异常
	* nStatus = -2;//数据写入异常
	* nStatus = -3;//未找到图像或图像文件名不规范或没有数据读取权限

	* nStatus = -4;//样本图像张数不够，无法进一步分析
	* nStatus = -5;//样本异常，活动精子数太少，结果可能不准确
	* nStatus = -6;//样本图像背景异常，可能样本在整体晃动
	*
	*
	*** do something else ***
	*
	*
	*
	输出参考：
	pdTestResult[0] = nTotalSpermNum;//被检测的精子总数
	pdTestResult[1] = dTotaSpermDensity;//精子密度,亿个/毫升
	pdTestResult[2] = nAliveSpermNum;//被检测的活动的精子数
	pdTestResult[3] = dAliveSpermDensity;//活动精子密度,亿个/毫升
	pdTestResult[4] = dAliveSpermRatio;//精子活率
	pdTestResult[5] = dActiveSpermRatio;//精子活力
	pdTestResult[7] = dSLNum;//有效精子数,亿个
*
* 当前版本：V1.1
* 作者：He Yi
* 完成日期：2017.06.02
*
* 取代版本：V1.0
* 原作者：He Yi
* 完成日期：2017.05.05
*/

#ifndef _COUNTSPERM_H_
#define _COUNTSPERM_H_

#define FILENUM 100
#define MAXSPERMNUM 2500
#define MAXPATHLENGTH 200
#define EPSINON 1e-10

// 算法输入数据
typedef struct tag_alg_data_in
{
    double  ratioImg;           // 图像放大率，um/pixel
    double  sampleDepth;        // 样本厚度，芯片杯6或7um
	double  Volume;				// 每剂容量
    //double  ratioDilute;      // 精液样本稀释比
    double  frameRate;          // 相机采样频率

    char    *imgPath;           // 样本图像文件的路径
    char    *resultPath;        // 样本结果文件的路径
}alg_data_in_t;

// 算法输出数据
typedef struct tag_alg_data_out
{

}alg_data_out_t;

//系统参数设定
struct SSettings
{
	double dRatioImg;//图像放大率,um/pixel
	double dSampleDepth;//样本厚度，芯片杯6或7um
	double dVolume;	// 每剂容量
	double dFrameRate;//相机采样频率
};

//单个精子信息
struct SpermInfor
{
	double dPosX;//质心x
	double dPosY;//质心y

	double dMajAxsLen;//长轴长度
	double dMinAxsLen;//短轴长度
	double dAngle;//角度
	double dArea;//面积

	int nItem;//序号
	bool bIsMatchPre;//是否匹配到前一时刻的精子
	int nItemMatchPre;//匹配到的前一时刻精子的序号
	bool bIsMatchNex;//是否匹配到后一时刻的精子
	int nItemMatchNex;//匹配到的后一时刻精子的序号
};

//精子几何特征范围限定
struct ParaRange
{
	double dAreaMin;//面积最小值
	double dAreaMax;//面积最大值
	double dAreaAve;//面积平均值

	double dMajorLengthMin;//长轴最小值
	double dMajorLengthMax;//长轴最大值
	double dMajorLengthAve;//长轴平均值

	double dMinorLengthMin;//短轴最小值
	double dMinorLengthMax;//短轴最大值
	double dMinorLengthAve;//短轴平均值
};

#ifdef __cplusplus
extern "C"
{
#endif

//算法入口
int getSpermCountMain(const char *pcImgPath, const char *pcResultPath, const double *dVar, double *pdTestResult);

#ifdef __cplusplus
}
#endif


#endif // _COUNTSPERM_H_