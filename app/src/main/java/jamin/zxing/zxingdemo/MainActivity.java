package jamin.zxing.zxingdemo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.client.android.camera.CameraConfigurationUtils;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    SurfaceView sv;
    private Camera mCamera;
    SurfaceHolder holder;
    Point screenResolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sv = (SurfaceView) findViewById(R.id.qrcode_sv);
        holder = sv.getHolder();
        doCamera();
        screenResolution = new Point();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screenResolution.x = dm.widthPixels;
        screenResolution.y = dm.heightPixels;
        Log.d("CameraConfiguration", "Resolution x: " + screenResolution.x + " y: " + screenResolution.y);
    }

    /**
     * 创建时间：2017/6/16
     * 创建者：huzan
     * 描述：c初始化camera
     */
    public void doCamera() {
        sv.setVisibility(View.VISIBLE);
        mCamera = getCamera();
        holder = sv.getHolder();
        holder.addCallback(MainActivity.this);
        sv.setFocusable(true);
        sv.setFocusableInTouchMode(true);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    Camera.Parameters param = mCamera.getParameters();
//                    param.setPictureFormat(ImageFormat.JPEG);
//                    CameraConfigurationUtils.setBestPreviewFPS(param);
//                    CameraConfigurationUtils.findBestPreviewSizeValue(param, screenResolution);
//                    CameraConfigurationUtils.setFocus(param, true, true, true);
//                    CameraConfigurationUtils.setFocusArea(param);
//                    CameraConfigurationUtils.setBarcodeSceneMode(param);
                    mCamera.autoFocus(null);
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 获取相机资源
     *
     * @return carema
     */
    private Camera getCamera()
    {
        try {
            return Camera.open();
        } catch (Exception e) {
            return null;
        }
    }

    private void startPreview(Camera c, SurfaceHolder h) {
        try {
            c.setPreviewDisplay(h);
            c.setDisplayOrientation(90);
//            setCameraDisplayOrientation(this,c);
            c.startPreview();

            Camera.Parameters param = mCamera.getParameters();
            param.setPictureFormat(ImageFormat.JPEG);
            CameraConfigurationUtils.setBestPreviewFPS(param);
            Point previewSize = CameraConfigurationUtils.findBestPreviewSizeValue(param, screenResolution);
            param.setPreviewSize(previewSize.x, previewSize.y);
            CameraConfigurationUtils.setFocus(param, true, true, true);
            CameraConfigurationUtils.setFocusArea(param);
            CameraConfigurationUtils.setBarcodeSceneMode(param);

        } catch (Exception e) {
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startPreview(mCamera, surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mCamera.stopPreview();
        startPreview(mCamera, surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera= getCamera();
            if (holder != null&&mCamera!=null) {
                startPreview(mCamera, holder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();

    }
}
