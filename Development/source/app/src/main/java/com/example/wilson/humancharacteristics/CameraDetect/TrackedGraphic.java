package com.example.wilson.humancharacteristics.CameraDetect;

import com.example.wilson.humancharacteristics.ui.camera.GraphicOverlay;

/**
 * Created by enclaveit on 13/03/2018.
 */

abstract class TrackedGraphic<T> extends GraphicOverlay.Graphic {
    private int mId;

    TrackedGraphic(GraphicOverlay overlay) {
        super(overlay);
    }

    void setId(int id) {
        mId = id;
    }

    protected int getId() {
        return mId;
    }

    abstract void updateItem(T item);
}