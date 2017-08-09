package com.join.camera;
/**
 * 回调路径,拍照张数,到20张的时候开始调用算法
 */
public interface IPictureCallback2 {
    /**
     * 给回调路径的路径给算法使用,拍照张数,到20张的时候开始调用算法,
     * @param tag StartDetection handler 发送消息时会标记 用于区分存储的路径  在把这个数回调回去区分算法需要哪个路径
     * @param path
     */
    public void photoPrepared2(int tag, String path);
}
