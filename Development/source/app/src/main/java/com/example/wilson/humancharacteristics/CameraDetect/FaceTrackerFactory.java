package com.example.wilson.humancharacteristics.CameraDetect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import static com.google.android.gms.vision.face.Landmark.BOTTOM_MOUTH;
import static com.google.android.gms.vision.face.Landmark.LEFT_CHEEK;
import static com.google.android.gms.vision.face.Landmark.LEFT_EAR;
import static com.google.android.gms.vision.face.Landmark.LEFT_EAR_TIP;
import static com.google.android.gms.vision.face.Landmark.LEFT_EYE;
import static com.google.android.gms.vision.face.Landmark.LEFT_MOUTH;
import static com.google.android.gms.vision.face.Landmark.NOSE_BASE;
import static com.google.android.gms.vision.face.Landmark.RIGHT_CHEEK;
import static com.google.android.gms.vision.face.Landmark.RIGHT_EAR;
import static com.google.android.gms.vision.face.Landmark.RIGHT_EAR_TIP;
import static com.google.android.gms.vision.face.Landmark.RIGHT_EYE;
import static com.google.android.gms.vision.face.Landmark.RIGHT_MOUTH;

/**
 * Created by enclaveit on 13/03/2018.
 */

class FaceTrackerFactory implements MultiProcessor.Factory<Face> {
    private GraphicOverlay mGraphicOverlay;

    FaceTrackerFactory(GraphicOverlay graphicOverlay) {
        mGraphicOverlay = graphicOverlay;
    }

    @Override
    public Tracker<Face> create(Face face) {
        FaceGraphic graphic = new FaceGraphic(mGraphicOverlay);
        return new GraphicTracker<>(mGraphicOverlay, graphic);
    }
}

class FaceGraphic extends TrackedGraphic<Face> {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static int mCurrentColorIndex = 0;

    private Mat tmpMat;
    private Bitmap bmp32;

    private Paint mIdPaint;
    private Paint mBoxPaint;

    private PointF mBottomMouthPosition;
    private PointF mLeftCheekPosition;
    private PointF mLeftEarPosition;
    private PointF mLeftEarTipPosition;
    private PointF mLeftEyePosition;
    private PointF mLeftMouthPosition;
    private PointF mNoseBasePosition;
    private PointF mRightCheekPosition;
    private PointF mRightEarPosition;
    private PointF mRightEarTipPosition;
    private PointF mRightEyePosition;
    private PointF mRightMouthPosition;
    private GraphicOverlay overlay = getOverlay();
    private Bitmap myBitmap = BitmapFactory.decodeResource(overlay.getResources(), R.drawable.wearmask2);


    private int mLandmarkX, mLandmarkY;


    private volatile Face mFace;


    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        final int selectedColor = Color.MAGENTA;


        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateItem(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position, size, and ID on the supplied canvas.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(Canvas canvas) {
        if (mFace == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float cx = translateX(mFace.getPosition().x + mFace.getWidth() / 2);
        float cy = translateY(mFace.getPosition().y + mFace.getHeight() / 2);


        //        canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mFacePositionPaint);
        //        canvas.drawText("id: " + getId(), cx + ID_X_OFFSET, cy + ID_Y_OFFSET, mIdPaint);

        // Draws an oval around the face.
        float xOffset = scaleX(mFace.getWidth() / 2.0f);
        float yOffset = scaleY(mFace.getHeight() / 2.0f);
        float left = cx - xOffset;
        float top = cy - yOffset;
        float right = cx + xOffset;
        float bottom = cy + yOffset;


        canvas.drawRoundRect(left, top, right, bottom, 20, 20, mBoxPaint);

        for ( Landmark landmark : mFace.getLandmarks() ) {

/*----------------  After ---------------------*/

            switch (landmark.getType()){
                case BOTTOM_MOUTH:{
                    mBottomMouthPosition = landmark.getPosition();
                    break;
                }
                case LEFT_CHEEK:{
                    mLeftCheekPosition = landmark.getPosition();
                    break;
                }
                case LEFT_EAR:{
                    mLeftEyePosition = landmark.getPosition();
                    break;
                }
                case LEFT_EAR_TIP:{
                    mLeftEarTipPosition = landmark.getPosition();
                    break;
                }
                case LEFT_EYE:{
                    mLeftEyePosition = landmark.getPosition();
                    break;
                }
                case LEFT_MOUTH:{
                    mLeftMouthPosition = landmark.getPosition();
                    break;
                }
                case NOSE_BASE:{
                    mNoseBasePosition = landmark.getPosition();
                    break;
                }
                case RIGHT_CHEEK:{
                    mRightCheekPosition = landmark.getPosition();
                    break;
                }
                case RIGHT_EAR:{
                    mRightEarPosition = landmark.getPosition();
                    break;
                }
                case RIGHT_EAR_TIP:{
                    mRightEarTipPosition = landmark.getPosition();
                    break;
                }
                case RIGHT_EYE:{
                    mRightEarPosition = landmark.getPosition();
                    break;
                }
                case RIGHT_MOUTH:{
                    mRightMouthPosition = landmark.getPosition();
                    break;
                }
                default:{
                    break;
                }
            }
//            mLandmarkX = (int) ( landmark.getPosition().x * 1 );
//            mLandmarkY = (int) ( landmark.getPosition().y * 1 );
//            canvas.drawCircle( mLandmarkX, mLandmarkY, 3, mBoxPaint );
        }
        myBitmap =  Bitmap.createScaledBitmap(myBitmap, (int) mFace.getWidth(),
                (int) mFace.getHeight(), false);
        canvas.drawBitmap(myBitmap, left, top, mBoxPaint);
    }

    //Native code C++
    private native void calculateFaceRotation(long imgMat);

}