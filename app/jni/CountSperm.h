/*
* Copyright (c) 2017.06 ���ڴ���ҽ�ƿƼ����޹�˾
* All right reserved.
*
* �ļ����ƣ�CountSperm.h
* ժҪ�����ڷ���ϵ��ͼƬ�о��ӵĻ����������˶�����
* �ײ��㷨�⣺OpenCV3.0.0���ɼ���OpenCV2.4.10
*
	ϵͳ�������壺
	double dRatioImg = 4/7.0;//ͼ��Ŵ���,um/pixel
	double dSampleDepth = 10;//������ȣ�оƬ��6��7um
	double dVolume = 80;// ÿ����������λml
	double dFrameRate = 15;//�������Ƶ��

	����ο���
	char *pcImgPath = "E:/SpermAnalysis/Analysis/Samples/TT-2/";//ͼ���ַ
	char *pcResultPath = "E:/SpermAnalysis/Analysis/Samples/TT-2/ResultImgs/";//���������ַ
	double dVar[4] = {dRatioImg, dSampleDepth, dVolume, dFrameRate};//ϵͳ����

	���òο���
	double pdTestResult[22] = {0};
	int nStatus = getSpermCountMain(pcImgPath, pcResultPath, dVar, pdTestResult);//�㷨����״̬

	�㷨����״̬˵����
	* nStatus = 1;//�����ɹ�
	* nStatus = 0;//�ڴ��쳣
	* nStatus = -1;//��������쳣
	* nStatus = -2;//����д���쳣
	* nStatus = -3;//δ�ҵ�ͼ���ͼ���ļ������淶��û�����ݶ�ȡȨ��

	* nStatus = -4;//����ͼ�������������޷���һ������
	* nStatus = -5;//�����쳣���������̫�٣�������ܲ�׼ȷ
	* nStatus = -6;//����ͼ�񱳾��쳣����������������ζ�
	*
	*
	*** do something else ***
	*
	*
	*
	����ο���
	pdTestResult[0] = nTotalSpermNum;//�����ľ�������
	pdTestResult[1] = dTotaSpermDensity;//�����ܶ�,�ڸ�/����
	pdTestResult[2] = nAliveSpermNum;//�����Ļ�ľ�����
	pdTestResult[3] = dAliveSpermDensity;//������ܶ�,�ڸ�/����
	pdTestResult[4] = dAliveSpermRatio;//���ӻ���
	pdTestResult[5] = dActiveSpermRatio;//���ӻ���
	pdTestResult[7] = dSLNum;//��Ч������,�ڸ�
*
* ��ǰ�汾��V1.1
* ���ߣ�He Yi
* ������ڣ�2017.06.02
*
* ȡ���汾��V1.0
* ԭ���ߣ�He Yi
* ������ڣ�2017.05.05
*/

#ifndef _COUNTSPERM_H_
#define _COUNTSPERM_H_

#define FILENUM 100
#define MAXSPERMNUM 2500
#define MAXPATHLENGTH 200
#define EPSINON 1e-10

// �㷨��������
typedef struct tag_alg_data_in
{
    double  ratioImg;           // ͼ��Ŵ��ʣ�um/pixel
    double  sampleDepth;        // ������ȣ�оƬ��6��7um
	double  Volume;				// ÿ������
    //double  ratioDilute;      // ��Һ����ϡ�ͱ�
    double  frameRate;          // �������Ƶ��

    char    *imgPath;           // ����ͼ���ļ���·��
    char    *resultPath;        // ��������ļ���·��
}alg_data_in_t;

// �㷨�������
typedef struct tag_alg_data_out
{

}alg_data_out_t;

//ϵͳ�����趨
struct SSettings
{
	double dRatioImg;//ͼ��Ŵ���,um/pixel
	double dSampleDepth;//������ȣ�оƬ��6��7um
	double dVolume;	// ÿ������
	double dFrameRate;//�������Ƶ��
};

//����������Ϣ
struct SpermInfor
{
	double dPosX;//����x
	double dPosY;//����y

	double dMajAxsLen;//���᳤��
	double dMinAxsLen;//���᳤��
	double dAngle;//�Ƕ�
	double dArea;//���

	int nItem;//���
	bool bIsMatchPre;//�Ƿ�ƥ�䵽ǰһʱ�̵ľ���
	int nItemMatchPre;//ƥ�䵽��ǰһʱ�̾��ӵ����
	bool bIsMatchNex;//�Ƿ�ƥ�䵽��һʱ�̵ľ���
	int nItemMatchNex;//ƥ�䵽�ĺ�һʱ�̾��ӵ����
};

//���Ӽ���������Χ�޶�
struct ParaRange
{
	double dAreaMin;//�����Сֵ
	double dAreaMax;//������ֵ
	double dAreaAve;//���ƽ��ֵ

	double dMajorLengthMin;//������Сֵ
	double dMajorLengthMax;//�������ֵ
	double dMajorLengthAve;//����ƽ��ֵ

	double dMinorLengthMin;//������Сֵ
	double dMinorLengthMax;//�������ֵ
	double dMinorLengthAve;//����ƽ��ֵ
};

#ifdef __cplusplus
extern "C"
{
#endif

//�㷨���
int getSpermCountMain(const char *pcImgPath, const char *pcResultPath, const double *dVar, double *pdTestResult);

#ifdef __cplusplus
}
#endif


#endif // _COUNTSPERM_H_