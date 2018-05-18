package com.example.wilson.Tensorflow;

import android.graphics.Bitmap;
import android.graphics.RectF;

import java.util.List;

/**
 * Created by Wilson on 5/9/2018.
 */

public interface ClassifierEmotion {
    /**
     * An immutable result returned by a Classifier describing what was recognized.
     */
    public class Recognition {
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        private final String id;

        /**
         * Display name for the recognition.
         */
        private final String title;

        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        private final Float confidence;

        /** Optional location within the source image for the location of the recognized object. */
        private RectF location;

        public Recognition(
                final String id, final String title, final Float confidence, final RectF location) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        public RectF getLocation() {
            return new RectF(location);
        }

        public void setLocation(RectF location) {
            this.location = location;
        }

        @Override
        public String toString() {
            String resultString = "";
            float percentConfidence = 1;
            if (id != null) {
                percentConfidence = (float) ((Integer.parseInt(id) + 1) / 7.0);
//                resultString += id + " " + percentConfidence;
            }

//            if (title != null) {
//                resultString += title + " ";
//            }

            if (confidence != null) {
                resultString += String.valueOf(id);
            }
//
//            if (location != null) {
//                resultString += location + " ";
//            }
            return id;

//            return resultString.trim();
        }
    }

    List<ClassifierEmotion.Recognition> recognizeImage(Bitmap bitmap);

    void enableStatLogging(final boolean debug);

    String getStatString();

    void close();
}
