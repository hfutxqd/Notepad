package com.hfutxqd.notepad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class LinedTextView extends TextView {
        private Rect mRect;
        private Paint mPaint;

        // This constructor is used by LayoutInflater
        public LinedTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0x800000BB);
        }

        /**
         * This is called to draw the LinedEditText object
         * @param canvas The canvas on which the background is drawn.
         */
        @Override
        protected void onDraw(Canvas canvas) {

            int count = getLineCount();
            Rect r = mRect;
            Paint paint = mPaint;
            /*
             * Draws one line in the rectangle for every line of text in the EditText
             */
            for (int i = 0; i < count; i++) {

                int baseline = getLineBounds(i, r);
                canvas.drawLine(r.left, baseline + ScreenInfo.dp_to_px(8), r.right, baseline + ScreenInfo.dp_to_px(8), paint);
            }
            super.onDraw(canvas);
        }
    }