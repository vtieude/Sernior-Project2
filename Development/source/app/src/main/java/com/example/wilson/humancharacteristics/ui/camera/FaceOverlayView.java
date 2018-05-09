package com.example.wilson.humancharacteristics.ui.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.example.wilson.humancharacteristics.model.FaceResult;

import java.text.DecimalFormat;

/**
 * Created by enclaveit on 22/03/2018.
 */

public class FaceOverlayView extends View {

    private Paint mPaint;
    private Paint mTextPaint;
    private int mDisplayOrientation;
    private int mOrientation;
    private int previewWidth;
    private int previewHeight;
    private FaceResult[] mFaces;
    private double fps;
    private boolean isFront = false;
    private boolean isRecognizing = false;

    private int mCurrentColorIndex = 0;

    private final int COLOR_CHOICES[] = {
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.GRAY
    };

    public FaceOverlayView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        // We want a green box around the face:
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int stroke = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(stroke);
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();

        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, metrics);
        mTextPaint.setTextSize(size);


        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    public void setFPS(double fps) {
        this.fps = fps;
    }

    public void setFaces(FaceResult[] faces, boolean isRecognizing) {
        mFaces = faces;
        this.isRecognizing = isRecognizing;
        invalidate();
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public void setDisplayOrientation(int displayOrientation) {
        mDisplayOrientation = displayOrientation;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFaces != null && mFaces.length > 0) {

            float scaleX = (float) getWidth() / (float) previewWidth;
            float scaleY = (float) getHeight() / (float) previewHeight;

            switch (mDisplayOrientation) {
                case 90:
                case 270:
                    scaleX = (float) getWidth() / (float) previewHeight;
                    scaleY = (float) getHeight() / (float) previewWidth;
                    break;
            }

            canvas.save();
            canvas.rotate(-mOrientation);
            RectF rectF = new RectF();
            for (FaceResult face : mFaces) {

                PointF mid = new PointF();
                face.getMidPoint(mid);

                if (mid.x != 0.0f && mid.y != 0.0f) {
                    float eyesDis = face.eyesDistance();

                rectF.set(new RectF(
                    (mid.x - eyesDis * 1.2f) * scaleX,
                    (mid.y - eyesDis * 0.65f) * scaleY,
                    (mid.x + eyesDis * 1.2f) * scaleX,
                    (mid.y + eyesDis * 1.75f) * scaleY));
                if (isFront) {
                    float left = rectF.left;
                    float right = rectF.right;
                    rectF.left = getWidth() - right;
                    rectF.right = getWidth() - left;
                }

                mCurrentColorIndex = face.getId()%COLOR_CHOICES.length;

                mPaint.setColor(COLOR_CHOICES[mCurrentColorIndex]);
                mTextPaint.setColor(COLOR_CHOICES[mCurrentColorIndex]);
                canvas.drawRect(rectF, mPaint);

                if(this.isRecognizing){
                    canvas.drawText("Attractive: " + String.valueOf(Integer.parseInt(face.getAttractive().substring(1,2))+ 1), rectF.left, rectF.bottom + mTextPaint.getTextSize() , mTextPaint);
                    canvas.drawText("TrustWorthy: " + String.valueOf(Integer.parseInt(face.getTrustworthy().substring(1,2))+ 1), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 2, mTextPaint);
                    canvas.drawText("Dominant: " + String.valueOf(Integer.parseInt(face.getDominant().substring(1,2))+ 1), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 3, mTextPaint);
                    canvas.drawText("Thread: " + String.valueOf(Integer.parseInt(face.getThread().substring(1,2))+ 1), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 4, mTextPaint);
                    canvas.drawText("Likeability: " + String.valueOf(Integer.parseInt(face.getLikeability().substring(1,2))+ 1), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 5, mTextPaint);
                    canvas.drawText("Competent: "+ String.valueOf(Integer.parseInt(face.getCompetent().substring(1,2))+ 1), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 6, mTextPaint);
                    canvas.drawText("Extroved: "+ String.valueOf(Integer.parseInt(face.getExtroverted().substring(1,2))+ 1), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 7, mTextPaint);
                }

                }
            }
            canvas.restore();
        }

//        DecimalFormat df2 = new DecimalFormat(".##");
//        canvas.drawText("Detected_Frame/s: " + df2.format(fps) + " @ " + previewWidth + "x" + previewHeight, mTextPaint.getTextSize(), mTextPaint.getTextSize(), mTextPaint);
    }

    public void setPreviewWidth(int previewWidth) {
        this.previewWidth = previewWidth;
    }

    public void setPreviewHeight(int previewHeight) {
        this.previewHeight = previewHeight;
    }

    public void setFront(boolean front) {
        isFront = front;
    }
}