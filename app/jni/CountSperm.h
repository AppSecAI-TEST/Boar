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

//运行状态提示
/*说明
* nAlgStatus = 1;//分析成功
*
* nAlgStatus = 0;//内存异常
* nAlgStatus = -1;//输入参数异常
* nAlgStatus = -2;//数据写入异常
* nAlgStatus = -3;//未找到图像或图像文件名不规范或没有数据读取权限
* nAlgStatus = -4;//样本图像张数不够，无法进一步分析
*
* nAlgStatus = -5;//样本异常，活动精子数太少，结果可能不准确,////有1张图片返回,计算结果有:被检测的精子总数+精子密度,其余为0
* nAlgStatus = -6;//样本图像背景异常，可能样本在整体晃动////有全部图片返回,计算结果为0
*/

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
// int algorithmMain(alg_data_out_t *dataOut, const alg_data_in_t *dataIn);

#ifdef __cplusplus
}
#endif


#endif // _COUNTSPERM_H_