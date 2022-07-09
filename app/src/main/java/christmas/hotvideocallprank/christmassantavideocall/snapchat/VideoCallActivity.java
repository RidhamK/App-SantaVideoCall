package christmas.hotvideocallprank.christmassantavideocall.snapchat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hotvideocallprank.christmassantavideocall.R;

import christmas.hotvideocallprank.christmassantavideocall.snapchat.model.VideoAttrs;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class VideoCallActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private Button btnVideo, btnCallEnd, btnFlipCamera;
    private TextView callerName;
    private SurfaceView surfaceView, surfaceViewSmall;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private VideoAttrs videoAttrs;
    private int cameraId;
    private VideoView videoView;
    private boolean flashmode = false;
    private int rotation;
    private MediaPlayer mediaPlayer;
    private AssetFileDescriptor afd;
    private final int MAX_VOLUME = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera);
        String item = getIntent().getExtras().getString("item");
        videoAttrs = new Gson().fromJson(item, VideoAttrs.class);
        cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        btnFlipCamera = findViewById(R.id.btn_flip_cam);
        btnVideo = findViewById(R.id.btn_video);
        btnFlipCamera = findViewById(R.id.btn_flip_cam);
        btnCallEnd = findViewById(R.id.btn_end_call);
        videoView = findViewById(R.id.videoView);
        callerName = findViewById(R.id.caller_name);
        String callName = videoAttrs.getName();
        callerName.setVisibility(View.VISIBLE);
        callerName.setText(callName + " is\n" + "calling you...");
        btnVideo.setOnClickListener(this);
        btnFlipCamera.setOnClickListener(this);
        btnCallEnd.setOnClickListener(this);
        //flashCameraButton = (Button) findViewById(R.id.flash);
        //captureImage = (Button) findViewById(R.id.captureImage);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceViewSmall = findViewById(R.id.surfaceViewSmall);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        btnFlipCamera.setOnClickListener(this);
        //captureImage.setOnClickListener(this);
        //flashCameraButton.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Camera.getNumberOfCameras() > 1) {
            btnFlipCamera.setVisibility(View.VISIBLE);
        }
        //if(!getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
        //flashCameraButton.setVisibility(View.GONE);
        //}
        mediaPlayer = new MediaPlayer();
        try {
            afd = getAssets().openFd("ringtone.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.setLooping(true);
            float vol = ((float) 2 / MAX_VOLUME);
            mediaPlayer.setVolume(vol, vol);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.start();
        openCamera(cameraId);
    }

    // This function is called when user accept or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Checking whether user granted the permission or not.
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (mediaPlayer != null && !mediaPlayer.isPlaying())
//                mediaPlayer.start();
//            openCamera(cameraId);
//        } else {
//            Toast.makeText(FrontCameraActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!openCamera(cameraId)) {
            alertCameraDialog();
        }
    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
                camera.setErrorCallback(new Camera.ErrorCallback() {
                    @Override
                    public void onError(int error, Camera camera) {
                    }
                });
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Camera.Parameters params = c.getParameters();
        showFlashButton(params);
        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }
        params.setRotation(rotation);
    }

    private void showFlashButton(Camera.Parameters params) {
        boolean showFlash = (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null) && params.getSupportedFlashModes() != null && params.getSupportedFocusModes().size() > 1;
        // flashCameraButton.setVisibility(showFlash ? View.VISIBLE : View.INVISIBLE);
    }

    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.flash:
//                flashOnButton();
//                break;
            case R.id.btn_flip_cam:
                flipCamera();
                break;
//            case R.id.captureImage:
//                takeImage();
//                break;
            case R.id.btn_end_call:
                try {
                    if (mediaPlayer != null) {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                } catch (Exception e) {
                }
                finish();
                break;
            case R.id.btn_video:
                surfaceViewSmall.setVisibility(View.VISIBLE);
                surfaceView.setVisibility(View.GONE);
                surfaceHolder = surfaceViewSmall.getHolder();
                surfaceHolder.addCallback(this);
                if (mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    mediaPlayer.release();
                }
                loadVideoView();
                btnVideo.setVisibility(View.GONE);
                callerName.setVisibility(View.GONE);
                btnCallEnd.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void flipCamera() {
        int id = (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT
                : Camera.CameraInfo.CAMERA_FACING_BACK);
        if (!openCamera(id)) {
            alertCameraDialog();
        }
    }

    private void alertCameraDialog() {
        Toast.makeText(getApplicationContext(), "Error to open Camera", Toast.LENGTH_SHORT).show();
    }

    private AlertDialog.Builder createAlert(Context context, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
        //   dialog.setIcon(R.drawable.);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;
    }

    private void flashOnButton() {
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_TORCH
                        : Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//        }
    }

    private void loadVideoView() {
        videoView.setVisibility(View.VISIBLE);
        if (videoAttrs.isInternal()) {
            videoView.setVideoURI(Uri.parse(videoAttrs.getUri()));
        } else {
            videoView.setVideoPath(videoAttrs.getVideoPath());
        }

        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mp.setLooping(true);
                    }
                });
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
