package com.eegsmart.changcolor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Xie on 2016/7/26.
 */
public class LineView extends View {
    private static final String TAG = "LineView";
    private int width;
    private int height;
    private int endHeight;
    private int heightSum;
    private int widthSum;
    private int startX;
    private int endX;
    private int startY;

    private int[] sleepList;
    private float sigleX;
    private int[] remList;


    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        Paint linePaint = new Paint();
        Paint remPaint = new Paint();

        paint.setColor(0xff000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dipToPx(1));
        paint.setAntiAlias(true); //去锯齿

        linePaint.setColor(Color.BLUE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dipToPx(1));
        linePaint.setAntiAlias(true); //去锯齿

        canvas.drawLine(startX, endHeight, endX, endHeight, paint);
        canvas.drawLine(startX, startY, startX, endHeight, paint);

        canvas.drawLine(startX - dipToPx(3), startY + dipToPx(3), startX, startY, paint);
        canvas.drawLine(startX + dipToPx(3), startY + dipToPx(3), startX, startY, paint);

        canvas.drawLine(endX - dipToPx(3), endHeight + dipToPx(3), endX, endHeight, paint);
        canvas.drawLine(endX - dipToPx(3), endHeight - dipToPx(3), endX, endHeight, paint);

        canvas.drawLine(startX, dipToPx(80), startX + dipToPx(5), dipToPx(80), paint);
        canvas.drawLine(endX - dipToPx(8), endHeight, endX - dipToPx(8), endHeight - dipToPx(5), paint);

        if (sleepList != null && sleepList.length >= 2) {
            sigleX = (endX - dipToPx(18)) * 1.0f / sleepList.length;
            for (int i = 0; i < sleepList.length - 1; i++) {
                float h1 = (endHeight - heightSum * (sleepList[i] * 1.0f/ 6));
                float h2 = (endHeight - heightSum * (sleepList[i + 1] * 1.0f/ 6));
                float x1 = startX + i * sigleX;
                float x2 = startX + (i + 1) * sigleX;
                canvas.drawLine(x1, h1, x2, h1, linePaint);
                canvas.drawLine(x2, h1, x2, h2, linePaint);
            }
        }

        canvas.drawLine(startX, 2 * endHeight, endX, 2 * endHeight, paint);
        canvas.drawLine(startX, endHeight + dipToPx(20), startX, 2 * endHeight, paint);

        canvas.drawLine(startX - dipToPx(3), endHeight + dipToPx(20) + dipToPx(3), startX, endHeight + dipToPx(20), paint);
        canvas.drawLine(startX + dipToPx(3), endHeight + dipToPx(20) + dipToPx(3), startX, endHeight + dipToPx(20), paint);

        canvas.drawLine(endX - dipToPx(3), 2 * endHeight + dipToPx(3), endX, 2 * endHeight, paint);
        canvas.drawLine(endX - dipToPx(3), 2 * endHeight - dipToPx(3), endX, 2 * endHeight, paint);

        canvas.drawLine(startX, endHeight + dipToPx(40), startX + dipToPx(5), endHeight + dipToPx(40), paint);
        canvas.drawLine(endX - dipToPx(8), 2 * endHeight, endX - dipToPx(8), 2 * endHeight - dipToPx(5), paint);

        remPaint.setColor(Color.BLUE);
        remPaint.setStyle(Paint.Style.STROKE);
        remPaint.setStrokeWidth(dipToPx(3));
        remPaint.setAntiAlias(true); //去锯齿
        if (remList != null) {
            sigleX = (endX - dipToPx(18)) * 1.0f / remList.length;
            for (int i = 0; i < remList.length; i++) {
                float x1 = startX + i * sigleX;
                if (remList[i] == 1) {
                    canvas.drawLine(x1, 2 * endHeight, x1, endHeight + dipToPx(40), remPaint);
                } else {
                    canvas.drawLine(x1, 2 * endHeight, x1, endHeight + dipToPx(40) + (endHeight - dipToPx(40)) / 2, remPaint);
                }
            }
        }
    }


    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            //MeasureSpec.EXACTLY表示该view设置的确切的数值
            width = widthSize;
            height = heightSize;
        } else {
        }
//        Log.e(TAG, "width:" + width + " heigth:" + height);
        endHeight = height / 2 - dipToPx(10); //结束柱状图的Y坐标
        heightSum = endHeight - dipToPx(80);
        widthSum = width - dipToPx(28);
        startX = dipToPx(10);
        endX = width - dipToPx(10);
        startY = dipToPx(60);
        setMeasuredDimension(width, height);
    }


    //设置sleep的数据
    public void setSleepList(int[] sleepList) {
        this.sleepList = sleepList;
        postInvalidate();
    }

    public void setRemList(int[] remList) {
        this.remList = remList;
        postInvalidate();
    }


}
