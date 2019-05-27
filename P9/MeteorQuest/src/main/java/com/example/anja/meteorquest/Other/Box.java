package com.example.anja.meteorquest.Other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Box extends View {
        private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public Box(Context context) {
            super(context);
            //color hex is [transparency][red][green][blue]
            mPaint.setColor(Color.rgb(155, 187, 174));
            this.setTop(10);
            this.setBottom(10);
            this.setRight(10);
            this.setLeft(10);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRect(300,300,300,300, mPaint);
        }

}
