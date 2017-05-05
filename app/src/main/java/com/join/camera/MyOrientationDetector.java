package com.join.camera;

import android.content.Context;
import android.util.Log;
import android.view.OrientationEventListener;

public class MyOrientationDetector extends OrientationEventListener {
    int Orientation;
    public MyOrientationDetector(Context context ) {
        super(context );
    }
    @Override
    public void onOrientationChanged(int orientation) {
        Log.i("jjjjj","onOrientationChanged:"+orientation);
        this.Orientation=orientation;
        Log.d("jjjj","当前的传感器方向为"+orientation);
    }

    public int getOrientation(){
        return Orientation;
    }
}
