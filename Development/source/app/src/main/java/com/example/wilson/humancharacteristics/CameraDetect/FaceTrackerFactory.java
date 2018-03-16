package com.example.wilson.humancharacteristics.CameraDetect;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.wilson.humancharacteristics.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

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

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        final int selectedColor = Color.MAGENTA;

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

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
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float cx = translateX(face.getPosition().x + face.getWidth() / 2);
        float cy = translateY(face.getPosition().y + face.getHeight() / 2);
//        canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mFacePositionPaint);
//        canvas.drawText("id: " + getId(), cx + ID_X_OFFSET, cy + ID_Y_OFFSET, mIdPaint);

        // Draws an oval around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = cx - xOffset;
        float top = cy - yOffset;
        float right = cx + xOffset;
        float bottom = cy + yOffset;
        canvas.drawRect(left, top, right, bottom, mBoxPaint);
        for ( Landmark landmark : face.getLandmarks() ) {
            int lx = (int) ( landmark.getPosition().x * 1 );
            int ly = (int) ( landmark.getPosition().y * 1 );
            canvas.drawCircle( lx, ly, 5, mBoxPaint );
        }
    }
}