package com.join.camera;

public interface IPictureCallback2 {
    /**
     * 回调路径,拍照张数,到20张的时候开始调用算法
     */
    public void photoPrepared(int tag, String path);
}
