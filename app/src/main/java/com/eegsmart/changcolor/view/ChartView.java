package com.eegsmart.changcolor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;


import com.eegsmart.changcolor.R;
import com.eegsmart.changcolor.enity.Histogram;

import java.util.ArrayList;

/**
 * Created by Xie on 2016/7/26.
 */
public class ChartView extends View {
    private static final String TAG = "ChartView";
    private final Paint wakePaint;
    private final Paint xulinePaint;
    private Paint textPaint;
    private Paint linePaint;
    private Paint remPaint;
    private Paint lightPaint;
    private Paint middlePaint;
    private Paint deepPaint;
    private int width;
    private int height;

    private int endHeight;
    private int heightSum;
    private int line1Height;
    private int line2Height;
    private int line3Height;
    private int line4Height;
    private int line5Height;
    private int line6Height;
    private int widthSum;
    private int startX;
    private int endX;
    private LinearGradient linearGradient1;
    private LinearGradient linearGradient2;
    private LinearGradient linearGradient3;
    private LinearGradient linearGradient4;
    private LinearGradient linearGradient5;
    private int wakeHeight;
    private float oneTextWidth;
    private int singleHeight;
    private int singleWidth;
    private Bitmap sleepBitmap;
    private Bitmap wakeBitmap;
    private RectF bitmapRectF;
    //    private List<Map<Integer, Float>> dataList = new ArrayList<>();
    private ArrayList<Histogram> dataList = new ArrayList<>();
    private int hours = 9;
    private int start = 22;
    private int end;
    private float drawStartX;
    private float drawEndX;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lightPaint = new Paint();
        middlePaint = new Paint();
        deepPaint = new Paint();
        remPaint = new Paint();
        wakePaint = new Paint();
        linePaint = new Paint();
        xulinePaint = new Paint();
        textPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        sleepBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.sleep);
        wakeBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.wake);
        bitmapRectF = new RectF();

        linePaint.setColor(0XFF4F4B70);
        linePaint.setStrokeWidth(dipToPx(1));
        //画高度横线
        canvas.drawLine(0, line1Height - dipToPx(1), width, line1Height - dipToPx(1), linePaint);
        canvas.drawLine(0, line2Height - dipToPx(1), width, line2Height - dipToPx(1), linePaint);
        canvas.drawLine(0, line3Height - dipToPx(1), width, line3Height - dipToPx(1), linePaint);
        canvas.drawLine(0, line4Height - dipToPx(1), width, line4Height - dipToPx(1), linePaint);
        canvas.drawLine(0, line5Height + dipToPx(1), width, line5Height + dipToPx(1), linePaint);

        textPaint.setColor(0xfff7d175);
        textPaint.setStrokeWidth(0);
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        oneTextWidth = textPaint.measureText("2");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        singleWidth = (int) ((width - (hours + 1) * oneTextWidth / 2) / hours + 1.0);
        int timeStart = 0;
        for (int i = 0; i <= hours; i++) {
            timeStart = start + i >= 24 ? start + i - 24 : start + i;
            canvas.drawText(timeStart + "", startX + i * singleWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
        }
//        canvas.drawText("23", startX, endHeight + oneTextWidth + dipToPx(5), textPaint);
//        canvas.drawText("0", startX + 1f * singleWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
//        canvas.drawText("1", startX + 2f * singleWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
//        canvas.drawText("2", startX + 3f * singleWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
//        canvas.drawText("3", startX + 4f * singleWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
//        canvas.drawText("4", startX + 5f * singleWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
//        canvas.drawText("5", startX + 6f * singleWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
//        canvas.drawText("6", startX + 7f * singleWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
//        canvas.drawText("7", endX - dipToPx(10), endHeight + oneTextWidth + dipToPx(5), textPaint);

        xulinePaint.setColor(0xffffffff);
        xulinePaint.setStrokeWidth(dipToPx(1));
        xulinePaint.setStyle(Paint.Style.STROKE);
        xulinePaint.setAntiAlias(true);
        PathEffect effect = new DashPathEffect(new float[]{15, 15, 15, 15}, 0);
        Path path = new Path();
        //画虚线
        path.moveTo(startX + 91, dipToPx(50));
        path.lineTo(startX + 91, endHeight);
        xulinePaint.setPathEffect(effect);
        canvas.drawPath(path, xulinePaint);

        path.moveTo(endX - 91, dipToPx(50));
        path.lineTo(endX - 91, endHeight);
        xulinePaint.setPathEffect(effect);
        canvas.drawPath(path, xulinePaint);

//        linearGradient5 = new LinearGradient(startX, wakeHeight, startX + 90, endHeight, 0xffff3600, 0xffff7e00, Shader.TileMode.MIRROR);
//        wakePaint.setShader(linearGradient5);
//        canvas.drawRect(startX, wakeHeight, startX + 90, endHeight, wakePaint);// 长方形
//        linearGradient5 = new LinearGradient(endX - 90, wakeHeight, endX, endHeight, 0xffff3600, 0xffff7e00, Shader.TileMode.MIRROR);
//        wakePaint.setShader(linearGradient5);
//        canvas.drawRect(endX - 90, wakeHeight, endX, endHeight, wakePaint);// 长方形

//        //画图片
//        canvas.drawBitmap(sleepBitmap, startX + 90 - sleepBitmap.getWidth() / 2, dipToPx(25), linePaint);
//        canvas.drawBitmap(wakeBitmap, endX - 90 - wakeBitmap.getWidth() / 2, dipToPx(25), linePaint);

        drawStartX = startX;
        if (dataList != null && dataList.size() > 0) {
            for (Histogram histogram : dataList) {
                int key = histogram.key;
                float value = histogram.value;
                float width = value * (widthSum);
                drawHistogram(key, width, canvas);
            }

        }
    }

    private void drawHistogram(int key, float width, Canvas canvas) {
        switch (key) {
            case 5:
                drawEndX = drawStartX + width;
                linearGradient5 = new LinearGradient(drawStartX, wakeHeight, drawEndX, endHeight, 0xffff3600, 0xffff7e00, Shader.TileMode.MIRROR);
                wakePaint.setShader(linearGradient5);
                canvas.drawRect(drawStartX, wakeHeight, drawEndX, endHeight, wakePaint);// 长方形
                drawStartX = drawEndX;
                break;
            case 4:
                drawEndX = drawStartX + width;
                linearGradient1 = new LinearGradient(drawStartX, line1Height, drawEndX, endHeight, 0xffffa800, 0xfffbdc88, Shader.TileMode.MIRROR);
                lightPaint.setShader(linearGradient1);
                canvas.drawRect(drawStartX, line1Height, drawEndX, endHeight, lightPaint);// 长方形
                drawStartX = drawEndX;
                break;
            case 3:
                drawEndX = drawStartX + width;
                linearGradient2 = new LinearGradient(drawStartX, line2Height, drawEndX, endHeight, 0xff0fcd29, 0Xffddff00, Shader.TileMode.MIRROR);
                middlePaint.setShader(linearGradient2);
                canvas.drawRect(drawStartX, line2Height, drawEndX, endHeight, middlePaint);// 长方形
                drawStartX = drawEndX;

                break;
            case 2:
                drawEndX = drawStartX + width;
                linearGradient3 = new LinearGradient(drawStartX, line3Height, drawEndX, endHeight, 0xff00c6ff, 0xff00ffc6, Shader.TileMode.MIRROR);
                deepPaint.setShader(linearGradient3);
                canvas.drawRect(drawStartX, line3Height, drawEndX, endHeight, deepPaint);// 长方形
                drawStartX = drawEndX;
                break;
            case 1:
                drawEndX = drawStartX + width;
                linearGradient4 = new LinearGradient(drawStartX, line4Height, drawEndX, endHeight, 0xff1034e3, 0xff38bfff, Shader.TileMode.MIRROR);
                remPaint.setShader(linearGradient4);
                canvas.drawRect(drawStartX, line4Height, drawEndX, endHeight, remPaint);// 长方形
                drawStartX = drawEndX;
                break;
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
        heightSum = endHeight - dipToPx(60);
        widthSum = width - dipToPx(20);
        startX = dipToPx(10);
        endX = width - dipToPx(10);
        singleHeight = heightSum / 5;
        singleWidth = widthSum / 8;
        wakeHeight = dipToPx(60);
        line1Height = dipToPx(60) + singleHeight;
        line2Height = dipToPx(60) + singleHeight * 2;
        line3Height = dipToPx(60) + singleHeight * 3;
        line4Height = dipToPx(60) + singleHeight * 4;
        line5Height = endHeight;

        setMeasuredDimension(width, height);
    }

    //设置睡眠的开始点与结束点
    public void setSleepTime(int start, int end) {
        this.start = start;
        this.end = end;
        hours = end < start ? end + 24 - start : end - start;
        singleWidth = widthSum / hours;
    }

    //设置睡眠的数据
//    public void setDataList(List<Map<Integer, Float>> dataList) {
//        this.dataList = dataList;
//        singleWidth = widthSum / hours;
//        postInvalidate();
//    }

    public void setDataList(ArrayList<Histogram> dataList) {
        this.dataList = dataList;
        singleWidth = widthSum / hours;
        postInvalidate();
    }

}
