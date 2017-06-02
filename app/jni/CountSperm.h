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

//����״̬��ʾ
/*˵��
* nAlgStatus = 1;//�����ɹ�
*
* nAlgStatus = 0;//�ڴ��쳣
* nAlgStatus = -1;//��������쳣
* nAlgStatus = -2;//����д���쳣
* nAlgStatus = -3;//δ�ҵ�ͼ���ͼ���ļ������淶��û�����ݶ�ȡȨ��
* nAlgStatus = -4;//����ͼ�������������޷���һ������
*
* nAlgStatus = -5;//�����쳣���������̫�٣�������ܲ�׼ȷ,////��1��ͼƬ����,��������:�����ľ�������+�����ܶ�,����Ϊ0
* nAlgStatus = -6;//����ͼ�񱳾��쳣����������������ζ�////��ȫ��ͼƬ����,������Ϊ0
*/

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
// int algorithmMain(alg_data_out_t *dataOut, const alg_data_in_t *dataIn);

#ifdef __cplusplus
}
#endif


#endif // _COUNTSPERM_H_