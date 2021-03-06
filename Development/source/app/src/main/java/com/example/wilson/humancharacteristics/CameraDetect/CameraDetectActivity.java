package com.example.wilson.humancharacteristics.CameraDetect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.wilson.Tensorflow.Classifier;
import com.example.wilson.Tensorflow.TensorFlowImageClassifier;
import com.example.wilson.humancharacteristics.Author.AuthorInformationActivity;
import com.example.wilson.humancharacteristics.MainActivity;
import com.example.wilson.humancharacteristics.PhotoDetect.PhotoDetectActivity;
import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.Setting.SettingActivity;
import com.example.wilson.humancharacteristics.Storage.StorageActivity;
import com.example.wilson.humancharacteristics.bean.HumanModel;
import com.example.wilson.humancharacteristics.model.FaceResult;
import com.example.wilson.humancharacteristics.model.HumanEmotion;
import com.example.wilson.humancharacteristics.ui.camera.FaceDetectView;
import com.example.wilson.humancharacteristics.ui.camera.FaceOverlayView;
import com.example.wilson.humancharacteristics.utils.CameraErrorCallback;
import com.example.wilson.humancharacteristics.utils.ImageUtils;
import com.example.wilson.humancharacteristics.utils.Util;

import android.hardware.Camera;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.opencv.imgproc.Imgproc.cvtColor;

public final class CameraDetectActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    // Number of Cameras in device.
    private int numberOfCameras;

    public static final String TAG = CameraDetectActivity.class.getSimpleName();

    private Camera mCamera;
    private int cameraId = 0;

    // Let's keep track of the display rotation and orientation also:
    private int mDisplayRotation;
    private int mDisplayOrientation;
    private int mPreDisplayRotation = 0;
    private boolean checkRecognize = false;
    private int previewWidth;
    private int previewHeight;

    // The surface view for the camera data
    private SurfaceView mView;

    // Draw rectangles and other fancy stuff:
    private FaceOverlayView mFaceView;

    // Log all errors:
    private final CameraErrorCallback mErrorCallback = new CameraErrorCallback();

    private FaceDetectView mFaceDetectView;

    private static int MAX_FACE = 1;
    private int saveValue = 0;
    private boolean initValue = false;
    MyAsyncTask myAsyncTask;
    private boolean isThreadWorking = false;
    private Handler handler;
    private FaceDetectThread detectThread = null;
    private int prevSettingWidth;
    private int prevSettingHeight;
    private android.media.FaceDetector fdet;

    private FaceResult faces[];
    private FaceResult faces_previous[];
    private int Id = 0;
    private HumanModel humanModel;
    private String BUNDLE_CAMERA_ID = "camera";
    private HumanEmotion emotionHuman;



    //RecylerView face image
    private HashMap<Integer, Integer> facesCount = new HashMap<>();
//    private RecyclerView recyclerView;
    private ArrayList<Bitmap> facesBitmap;


    //Face use for recognition
    private ArrayList<org.opencv.core.Mat> faceRects;

    private boolean checkCreateModel = false;
    private boolean checkUpdate = true;
    private ImageButton takePhotoCamera;
    private ImageButton getPhotoCamera;

    private ImageButton image;
    private static final int INPUT_SIZE = 224;

    private String attracttiveResult = "";
    private String trustworthyResult = "";
    private String dominantResult = "";
    private String threadResult = "";
    private String likeabilityResult = "";
    private String competentResult = "";
    private String extrovertedResult = "";
    private Boolean checkCharateristicsSetting = false;
    public ProgressBar progress;
    public TextView textwaitmodel;
    private SharedPreferences setting;
    public TextView textcharacterRecognize, textEmotion;
    private Executor executor = Executors.newSingleThreadExecutor();
    private DrawerLayout drawerLayout;
    private Boolean checkEmotionModel = false;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }


    //==============================================================================================
    // Activity Methods
    //==============================================================================================

    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_camera_viewer);

        mView = (SurfaceView) findViewById(R.id.surfaceview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_start, R.string.navigation_start);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Now create the OverlayView:

        mFaceView = new FaceOverlayView(this);
        mFaceDetectView = new FaceDetectView(this);
        addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addContentView(mFaceDetectView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        takePhotoCamera = findViewById(R.id.takePhoto);
        getPhotoCamera = findViewById(R.id.getPhoto);
        image = findViewById(R.id.takePhoto);
        textcharacterRecognize = findViewById(R.id.text_characteristic);
        textEmotion = findViewById(R.id.text_for_emotion);
        image.setEnabled(false);
        // Create and Start the OrientationListener:
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


        setting = PreferenceManager.getDefaultSharedPreferences(this);
        MAX_FACE = setting.getInt("amount_face", 1);


        checkCharateristicsSetting = setting.getBoolean("switch_character", true);
        takePhotoCamera.setOnClickListener(this);
        getPhotoCamera.setOnClickListener(this);
        handler = new Handler();
        faces = new FaceResult[MAX_FACE];
        faces_previous = new FaceResult[MAX_FACE];
        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceResult(this.getApplicationContext());
            faces_previous[i] = new FaceResult(this.getApplicationContext());
        }

//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Face Detect RGB");

        if (icicle != null)
            cameraId = icicle.getInt(BUNDLE_CAMERA_ID, 0);
        if(checkCreateModel == false){
            initTensorFlowAndLoadModel();
        }
        Menu menuNav = navigationView.getMenu();
        MenuItem navitem= menuNav.findItem(R.id.camera_start);
        navitem.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress = (ProgressBar)findViewById(R.id.progressBarCamera);
                            textwaitmodel = (TextView)findViewById(R.id.text_wait_model);
                            progress.setVisibility(View.VISIBLE);
                            textwaitmodel.setVisibility(View.VISIBLE);
                        }
                    });
                    humanModel = new HumanModel(getAssets());
                    emotionHuman = new HumanEmotion(getAssets());
                    checkEmotionModel = setting.getBoolean("switch_emotion", false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.setVisibility(View.INVISIBLE);
                            textwaitmodel.setVisibility(View.INVISIBLE);
                        }
                    });
                    checkCreateModel = true;
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }

            }
        });
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        SurfaceHolder holder = mView.getHolder();
        holder.addCallback(this);
        // YCrCb format used for images
        holder.setFormat(ImageFormat.NV21);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.switchCam:

                if (numberOfCameras == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Switch Camera").setMessage("Your device have one camera").setNeutralButton("Close", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
                cameraId = (cameraId + 1) % numberOfCameras;
                if (checkCreateModel == true){
                    recreate();
                    checkCreateModel = false;
                    return true;
                }
                else return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        MAX_FACE = setting.getInt("amount_face", 1);
        checkEmotionModel = setting.getBoolean("switch_emotion", false);
        checkCharateristicsSetting = setting.getBoolean("switch_character", true);
        checkRecognize = false;
        saveValue = 0;
        takePhotoCamera.setOnClickListener(this);
        getPhotoCamera.setOnClickListener(this);
        handler = new Handler();
        faces = new FaceResult[MAX_FACE];
        faces_previous = new FaceResult[MAX_FACE];
        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceResult(this.getApplicationContext());
            faces_previous[i] = new FaceResult(this.getApplicationContext());
        }
        mFaceView.setFaces(faces,false);
        textcharacterRecognize.setText("");
        textEmotion.setText("");
        Log.i(TAG, "onResume");
        startPreview();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        MAX_FACE = setting.getInt("amount_face", 1);
        checkEmotionModel = setting.getBoolean("switch_emotion", false);
        checkCharateristicsSetting = setting.getBoolean("switch_character", true);
        checkRecognize = false;
        saveValue = 0;
        takePhotoCamera.setOnClickListener(this);
        getPhotoCamera.setOnClickListener(this);
        handler = new Handler();
        faces = new FaceResult[MAX_FACE];
        faces_previous = new FaceResult[MAX_FACE];
        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceResult(this.getApplicationContext());
            faces_previous[i] = new FaceResult(this.getApplicationContext());
        }
        mFaceView.setFaces(faces,false);
        textcharacterRecognize.setText("");
        textEmotion.setText("");
        Log.i(TAG, "onPause");
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(checkCreateModel == true){
                    humanModel.getAttracttiveHuman().onDestroy();
                    humanModel.getExtrovertedHuman().onDestroy();
                    humanModel.getCompetentHuman().onDestroy();
                    humanModel.getDominantHuman().onDestroy();
                    humanModel.getLikeabilityHuman().onDestroy();
                    humanModel.getThreadHuman().onDestroy();
                    humanModel.getTrustworthyHuman().onDestroy();
                    checkCreateModel = false;
                    emotionHuman.onDestroy();
                }
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CAMERA_ID, cameraId);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        //Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                if (cameraId == 0) cameraId = i;
            }
        }

        mCamera = Camera.open(cameraId);

        Camera.getCameraInfo(cameraId, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mFaceView.setFront(true);
            mFaceDetectView.setFront(true);
        }


        try {
            mCamera.setPreviewDisplay(mView.getHolder());
        } catch (Exception e) {
            Log.e(TAG, "Could not preview the image.", e);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // We have no surface, return immediately:
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }

        configureCamera(width, height);
        setDisplayOrientation();
        setErrorCallback();

        // Create media.FaceDetector
        float aspect = (float) previewHeight / (float) previewWidth;
        fdet = new android.media.FaceDetector(prevSettingWidth, (int) (prevSettingWidth * aspect), MAX_FACE);

        startPreview();
    }

    private void setErrorCallback() {
        mCamera.setErrorCallback(mErrorCallback);
    }

    private void setDisplayOrientation() {
        // Now set the display orientation:
        mDisplayRotation = Util.getDisplayRotation(CameraDetectActivity.this);
        mDisplayOrientation = Util.getDisplayOrientation(mDisplayRotation, cameraId);

        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }
        if (mFaceDetectView != null) {
            mFaceDetectView.setDisplayOrientation(mDisplayOrientation);
        }

    }

    private void configureCamera(int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // Set the PreviewSize and AutoFocus:
        setOptimalPreviewSize(parameters, width, height);
        setAutoFocus(parameters);
        // And set the parameters:
        mCamera.setParameters(parameters);
    }

    private void setOptimalPreviewSize(Camera.Parameters cameraParameters, int width, int height) {
        List<Camera.Size> previewSizes = cameraParameters.getSupportedPreviewSizes();
        float targetRatio = (float) width / height;
        Camera.Size previewSize = Util.getOptimalPreviewSize(this, previewSizes, targetRatio);
        previewWidth = previewSize.width;
        previewHeight = previewSize.height;

        Log.e(TAG, "previewWidth" + previewWidth);
        Log.e(TAG, "previewHeight" + previewHeight);

        /**
         * Calculate size to scale full frame bitmap to smaller bitmap
         * Detect face in scaled bitmap have high performance than full bitmap.
         * The smaller image size -> detect faster, but distance to detect face shorter,
         * so calculate the size follow your purpose
         */
        if (previewWidth / 4 > 360) {
            prevSettingWidth = 360;
            prevSettingHeight = 270;
        } else if (previewWidth / 4 > 320) {
            prevSettingWidth = 320;
            prevSettingHeight = 240;
        } else if (previewWidth / 4 > 240) {
            prevSettingWidth = 240;
            prevSettingHeight = 160;
        } else {
            prevSettingWidth = 160;
            prevSettingHeight = 120;
        }

        cameraParameters.setPreviewSize(previewSize.width, previewSize.height);

        mFaceView.setPreviewWidth(previewWidth);
        mFaceView.setPreviewHeight(previewHeight);
        mFaceDetectView.setPreviewWidth(previewWidth);
        mFaceDetectView.setPreviewHeight(previewHeight);
    }

    private void setAutoFocus(Camera.Parameters cameraParameters) {
        List<String> focusModes = cameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    private void startPreview() {
        if (mCamera != null) {
            isThreadWorking = false;
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            counter = 0;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.setPreviewCallbackWithBuffer(null);
        mCamera.setErrorCallback(null);
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onPreviewFrame(byte[] _data, Camera _camera) {
        if (!isThreadWorking) {
            if (counter == 0)
                start = System.currentTimeMillis();

            isThreadWorking = true;
            waitForFdetThreadComplete();
            detectThread = new FaceDetectThread(handler, this);
            detectThread.setData(_data);
            detectThread.start();
        }
    }

    private void waitForFdetThreadComplete() {
        if (detectThread == null) {
            return;
        }

        if (detectThread.isAlive()) {
            try {
                detectThread.join();
                detectThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

        // fps detect face (not FPS of camera)
    long start, end;
    int counter = 0;
    double fps;

    @Override
    public  void onClick(View v) {
        Intent mIntent = null;
        switch (v.getId()) {
            case R.id.takePhoto:
                try {
                    myAsyncTask = new MyAsyncTask(this);
                    myAsyncTask.execute(faces);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.getPhoto:
                try {
                    if(checkCreateModel){
                        mIntent = new Intent(CameraDetectActivity.this, PhotoDetectActivity.class);
                        startActivity(mIntent);
                        checkCreateModel = true;
                    }
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
    }


    int getNumFace(android.media.FaceDetector.Face[] fullResults){
        int numFace = 0;
        for (int i = 0; i < MAX_FACE; i++) {
            if (fullResults[i] != null) {
                numFace++;
            }
        }
        if (numFace == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textcharacterRecognize.setText("");
                    textEmotion.setText("");
                }
            });
        }
        return numFace;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent myIntent = null;
        progress.setVisibility(View.INVISIBLE);
        textwaitmodel.setVisibility(View.INVISIBLE);
        switch (item.getItemId()) {
            case R.id.camera_start:
                break;
            case R.id.storage_homepage:
                myIntent = new Intent(this, StorageActivity.class);
                break;
            case R.id.setting_homepage:
                myIntent = new Intent(this, SettingActivity.class);
                break;
            case R.id.exit_homepage:
                CameraDetectActivity.this.finishAffinity(); System.exit(0);
                break;
            case R.id.author_homepage:
                myIntent = new Intent(this, AuthorInformationActivity.class);
                break;
            default: break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        this.startActivity(myIntent);
        return true;
    }

    /**
     * Do face detect in thread
     */

    private class FaceDetectThread extends Thread {
        private Handler handler;
        private byte[] data = null;
        private Context ctx;
        private Bitmap faceCroped;

        public FaceDetectThread(Handler handler, Context ctx) {
            this.ctx = ctx;
            this.handler = handler;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public void run() {
            float aspect = (float) previewHeight / (float) previewWidth;
            int w = prevSettingWidth;
            int h = (int) (prevSettingWidth * aspect);

            Bitmap bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.RGB_565);
            // face detection: first convert the image from NV21 to RGB_565
            YuvImage yuv = new YuvImage(data, ImageFormat.NV21,
                    bitmap.getWidth(), bitmap.getHeight(), null);
            // TODO: make rect a member and use it for width and height values above
            Rect rectImage = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            // TODO: use a threaded option or a circular buffer for converting streams?
            //see http://ostermiller.org/convert_java_outputstream_inputstream.html
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            if (!yuv.compressToJpeg(rectImage, 100, baout)) {
                Log.e("CreateBitmap", "compressToJpeg failed");
            }

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(
                    new ByteArrayInputStream(baout.toByteArray()), null, bfo);

            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, w, h, false);

            float xScale = (float) previewWidth / (float) prevSettingWidth;
            float yScale = (float) previewHeight / (float) h;

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotate = mDisplayOrientation;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && mDisplayRotation % 180 == 0) {
                if (rotate + 180 > 360) {
                    rotate = rotate - 180;
                } else
                    rotate = rotate + 180;
            }

            switch (rotate) {
                case 90:
                    bmp = ImageUtils.rotate(bmp, 90);
                    xScale = (float) previewHeight / bmp.getWidth();
                    yScale = (float) previewWidth / bmp.getHeight();
                    break;
                case 180:
                    bmp = ImageUtils.rotate(bmp, 180);
                    break;
                case 270:
                    bmp = ImageUtils.rotate(bmp, 270);
                    xScale = (float) previewHeight / (float) h;
                    yScale = (float) previewWidth / (float) prevSettingWidth;
                    break;
            }

            fdet = new android.media.FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACE);

            android.media.FaceDetector.Face[] fullResults = new android.media.FaceDetector.Face[MAX_FACE];
            fdet.findFaces(bmp, fullResults);

            final int numFace = getNumFace(fullResults);

            for (int i = 0; i < MAX_FACE; i++) {

                if (fullResults[i] == null) {
                    faces[i].clear();
                } else {
                    PointF mid = new PointF();
                    fullResults[i].getMidPoint(mid);

                    mid.x *= xScale;
                    mid.y *= yScale;

                    float eyesDis = fullResults[i].eyesDistance() * xScale;
                    float confidence = fullResults[i].confidence();
                    float pose = fullResults[i].pose(android.media.FaceDetector.Face.EULER_Y);
                    int idFace = Id;

                    Rect rect = new Rect(
                            (int) (mid.x - eyesDis * 1.20f),
                            (int) (mid.y - eyesDis * 0.55f),
                            (int) (mid.x + eyesDis * 1.20f),
                            (int) (mid.y + eyesDis * 1.85f));

                    /**
                     * Only detect face size > 100x100
                     */
                    if (rect.height() * rect.width() > 100 * 100) {
                        for (int j = 0; j < MAX_FACE; j++) {
                            float eyesDisPre = faces_previous[j].eyesDistance();
                            PointF midPre = new PointF();
                            faces_previous[j].getMidPoint(midPre);

                            RectF rectCheck = new RectF(
                                    (midPre.x - eyesDisPre * 1.5f),
                                    (midPre.y - eyesDisPre * 1.15f),
                                    (midPre.x + eyesDisPre * 1.5f),
                                    (midPre.y + eyesDisPre * 1.85f));

                            if (rectCheck.contains(mid.x, mid.y) && (System.currentTimeMillis() - faces_previous[j].getTime()) < 2000) {
                                idFace = faces_previous[j].getId();
                                break;
                            }
                        }

                        if (idFace == Id) Id++;

                        faces[i].setFace(idFace, mid, eyesDis, confidence, pose, System.currentTimeMillis());

                        faces_previous[i].set(faces[i].getId(), faces[i].getMidEye(), faces[i].eyesDistance(), faces[i].getConfidence(), faces[i].getPose(), faces[i].getTime());

// 9.16
                        //
                        // if focus in a face 5 frame -> take picture face display in RecyclerView
                        // because of some first frame have low quality
                        //
                        if (facesCount.get(idFace) == null) {
                            facesCount.put(idFace, 0);
                        } else {
                            int count = facesCount.get(idFace) + 1;
                            if (count <= 5)
                                facesCount.put(idFace, count);
                        }
                    }
                    //
                    // Crop Face to display in RecylerView
                    //
                    faceCroped = ImageUtils.cropFace(faces[i], bitmap, rotate);

                    if (checkEmotionModel && numFace == 1 && checkCreateModel) {
                        Bitmap bmp32 = Bitmap.createScaledBitmap(faceCroped, INPUT_SIZE, INPUT_SIZE, false);
                        bmp32 =  toGrayscale(bmp32);
                        final String text = emotionHuman.recognizeImage(bmp32).toString();
//                        faces[i].setBitmapFaceCrop(bmp32);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textEmotion.setText(text);
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textEmotion.setText("");
                            }
                        });
                    }

                    if((saveValue != numFace || !initValue) && checkCreateModel && checkCharateristicsSetting ){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (numFace > 1) {
                                    textcharacterRecognize.setText("");
                                }
                                progress.setVisibility(View.VISIBLE);
                                image.setEnabled(false);
                                textwaitmodel.setText(R.string.recognizing_face_value);
                                textwaitmodel.setVisibility(View.VISIBLE);
                            }
                        });

                        Mat rgba = new Mat(faceCroped.getHeight(), faceCroped.getWidth(), CvType.CV_8UC4);
                        Bitmap bmp_crop = faceCroped.copy(Bitmap.Config.ARGB_8888, true);

                        Utils.bitmapToMat(bmp_crop, rgba);

                        Mat rgb = new Mat(faceCroped.getHeight(), faceCroped.getWidth(), CvType.CV_8UC3);
                        cvtColor(rgba, rgb, Imgproc.COLOR_RGBA2BGR, 3);
                        final String text = findLandmark(rgb.getNativeObjAddr());
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    drawLine(rgb.getNativeObjAddr());
                        cvtColor(rgb, rgb, Imgproc.COLOR_GRAY2RGBA);
                        Utils.matToBitmap(rgb, bmp_crop);
                        rgb.release();
                        faces[i].setBitmapFaceCrop(faceCroped);
                        final int finalI1 = i;
                        if (faceCroped != null) {
                            Bitmap bmp32 = Bitmap.createScaledBitmap(faceCroped, INPUT_SIZE, INPUT_SIZE, false);
                            faces[i].setAttractive(humanModel.getAttracttiveHuman().recognizeImage(bmp32));
                            faces[i].setTrustworthy(humanModel.getTrustworthyHuman().recognizeImage(bmp32));
                            faces[i].setDominant(humanModel.getDominantHuman().recognizeImage(bmp32));
                            faces[i].setThread(humanModel.getThreadHuman().recognizeImage(bmp32));
                            faces[i].setLikeability(humanModel.getLikeabilityHuman().recognizeImage(bmp32));
                            faces[i].setCompetnent(humanModel.getCompetentHuman().recognizeImage(bmp32));
                            faces[i].setExtroverted(humanModel.getExtrovertedHuman().recognizeImage(bmp32));
                            final int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setVisibility(View.INVISIBLE);
                                    textwaitmodel.setVisibility(View.INVISIBLE);
                                    if (image.getVisibility() == View.INVISIBLE) {
                                        image.setVisibility(View.VISIBLE);
                                    }
                                    if (numFace == 1) {
                                        textcharacterRecognize.setText(
                                            faces[finalI].getAttracttiveDescription()+". "+faces[finalI].getTrustworthyDescription()+"\n"+
                                                    faces[finalI].getDominantDescription()   +". "+faces[finalI].getThreadDescription()+"\n"+
                                                    faces[finalI].getLikeabilityDescription()+". "+faces[finalI].getCompetentDescription()+"\n"+
                                                    faces[finalI].getExtrovertedDescription());
                                    }
                                    else {
                                        textcharacterRecognize.setText("");
                                        textEmotion.setText("");
                                    }
                                    image.setEnabled(true);

                                }
                            });
                            checkRecognize = true;
                            checkUpdate = false;
                            initValue = true;
                        }
                        else {
                            checkRecognize =false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setVisibility(View.INVISIBLE);
                                    textwaitmodel.setVisibility(View.INVISIBLE);
                                 }
                            });
                        }
                    }
                }
            }
            saveValue = numFace;
            handler.post(new Runnable() {
                public void run() {
                        mFaceView.setFaces(faces, checkRecognize);
                        //calculate FPS
                        end = System.currentTimeMillis();
                        counter++;
                        double time = (double) (end - start) / 1000;
                        if (time != 0)
                            fps = counter / time;

                        mFaceView.setFPS(fps);

                        if (counter == (Integer.MAX_VALUE - 1000))
                            counter = 0;
                        isThreadWorking = false;
//                    }
                }
            });
        }
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    public native void drawLine(long img);
    public native String findLandmark(long img);
}



