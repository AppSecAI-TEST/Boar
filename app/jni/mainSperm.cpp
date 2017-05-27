#include <jni.h>
#include <opencv2/opencv.hpp>
#include <vector>
#include <stdio.h>
#include "CountSperm.h"

#include "cv.h"
#include "highgui.h"

extern "C" {

using namespace cv;
using namespace std;



JNIEXPORT jfloatArray JNICALL Java_com_join_utils_Arithmetic_countSperm
        (JNIEnv *jev, jobject obj, jstring photoPath, jstring path,jfloatArray params_map)

{
jboolean bo = 1;
const char *photoP = jev->GetStringUTFChars(photoPath,&bo);
const char *pa = jev->GetStringUTFChars(path,&bo);
jfloat *pathini = jev->GetFloatArrayElements(params_map,&bo);

int iNumOutputs = 22;
jfloatArray jintarr = jev->NewFloatArray(22);
float *arr = getSpermCountMain((char *)photoP, (char *)pa, (float *)pathini);
jev->SetFloatArrayRegion(jintarr,0,22,arr);
return jintarr;
}


}
