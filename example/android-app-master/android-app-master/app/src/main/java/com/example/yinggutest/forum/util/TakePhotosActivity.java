package com.example.yinggutest.forum.util;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.yinggutest.R;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.util.ArrayList;

public class TakePhotosActivity extends TakePhotoActivity {

    private static final String TAG = "TakePhotosActivity";

    public static final String TAKE_PHOTO_TYPE = "Take_Photo_type";
    public static final int ON_TAKE_PHOTOS = 1;
    public static final int ON_SELECT_PICTURES = 2;
    private TakePhotoHelper takePhotoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photos);

        //new 对象（构造方法中完成初始化）
        takePhotoHelper = new TakePhotoHelper(getTakePhoto());
        //拍摄或者从相册获取,默认从相册选择
        if (getIntent().getIntExtra(TAKE_PHOTO_TYPE, ON_SELECT_PICTURES) == ON_SELECT_PICTURES) {
            takePhotoHelper.onSelectPictures(2, 2, false, 800, 800);
        } else {
            takePhotoHelper.onTakePhotos(false, 800, 800);
        }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        finish();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        Log.e(TAG, "takeSuccess: "+result.getClass() );

        showOneImg(result.getImages().get(0));
        //showImg(result.getImages());
    }

    /**
     * 只需要一张图片
     */
    private void showOneImg(TImage tImage) {
        // 原图路径：getOriginalPath() 压缩图路径：getCompressPath()
        Intent intent = new Intent();
        intent.putExtra("TakePhotosActivity_images", tImage.getCompressPath());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 多张图片
     */
    private void showImg(ArrayList<TImage> images) {
        Intent intent = new Intent();
        intent.putExtra("TakePhotosActivity_images", images);
        setResult(RESULT_OK, intent);
        finish();
    }


}