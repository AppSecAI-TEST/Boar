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

JNIEXPORT jint JNICALL Java_com_join_utils_Arithmetic_countSperm
        (JNIEnv *jev, jobject obj, jstring photoPath, jstring path,jdoubleArray params_in_map, jdoubleArray params_out_map)

{
jboolean bo = 1;
const char *photoP = jev->GetStringUTFChars(photoPath,&bo);
const char *pa = jev->GetStringUTFChars(path,&bo);
jdouble *parain = jev->GetDoubleArrayElements(params_in_map,&bo);
jdouble *paraout = jev->GetDoubleArrayElements(params_out_map,&bo);

int nStatus = getSpermCountMain((char *)photoP, (char *)pa, (double *)parain, (double *)paraout);

jev->SetDoubleArrayRegion(params_out_map,0,22,paraout);

return nStatus;
}


}
