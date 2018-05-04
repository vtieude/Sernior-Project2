package com.example.wilson.humancharacteristics.ui.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.example.wilson.humancharacteristics.model.FaceResult;

public class FaceDetectView extends View {
    private Paint mPaint;
    private int mDisplayOrientation;
    private int mOrientation;
    private int previewWidth;
    private int previewHeight;
    private FaceResult mFace;
    private boolean isFront = false;
    private int mCurrentColorIndex = 0;

    private final int COLOR_CHOICES[] = {
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.GRAY
    };

    public FaceDetectView(Context context) {
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
    }

    public void setFace(FaceResult face) {
        mFace = face;
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
        if (mFace != null) {

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


            PointF mid = new PointF();
            mFace.getMidPoint(mid);

            if (mid.x != 0.0f && mid.y != 0.0f) {
                float eyesDis = mFace.eyesDistance();

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

                mCurrentColorIndex = mFace.getId()%COLOR_CHOICES.length;

                mPaint.setColor(COLOR_CHOICES[mCurrentColorIndex]);
                canvas.drawRect(rectF, mPaint);
            }

            canvas.restore();
        }
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
