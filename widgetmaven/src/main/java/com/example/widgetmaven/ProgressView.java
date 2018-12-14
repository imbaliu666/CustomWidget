package com.example.widgetmaven;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * 项目名： CustomWidget
 * 包名：   com.example.imbaliu.customwidget
 * 文件名： ProgressView
 * 创建者： LFY
 * 创建时间： 2018/12/14 9:23
 * 描述：   TODO
 */

public class ProgressView extends View {
    private Boolean isvisible = false;

    private float startprogress = 270;

    //圆环内文字
    String progressText;


    private Paint fillArcPaint;


    //渐变数组
    int startColor = Color.parseColor("#5b81fd");
    int endColor = Color.parseColor("#b37df1");

    private int[] arcColors = new int[]{startColor, endColor, startColor

    };


    private RectF oval;

    private BlurMaskFilter mBlur;

    // view重绘的标记

    private boolean reset = false;



    private int arcradus = 30;

    //初始化进度

    private int progress = 0;

    //设置进度最大值

    private int max = 100;

    public ProgressView(Context context, AttributeSet attrs) {

        super(context, attrs);

        initPaint();
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressView, 0, 0);
        startprogress = array.getFloat(R.styleable.ProgressView_pg_startprogress, 0);
        progress = array.getInteger(R.styleable.ProgressView_pg_endprogress, progress);
        max = array.getInteger(R.styleable.ProgressView_pg_max, 100);
        isvisible = array.getBoolean(R.styleable.ProgressView_pg_text_isvisible, false);
        arcradus = array.getInteger(R.styleable.ProgressView_pg_width, arcradus);
        for (int i = 0; i < arcColors.length; i++) {
            arcColors[i] = array.getColor(R.styleable.ProgressView_pg_solid, arcColors[i]);
        }
        oval = new RectF();

    }


    //初始化画笔操作

    private void initPaint() {

        //初始化画笔操作

        fillArcPaint = new Paint();

        // 设置是否抗锯齿

        fillArcPaint.setAntiAlias(true);

        // 帮助消除锯齿

        fillArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        // 设置中空的样式

        fillArcPaint.setStyle(Paint.Style.STROKE);

        fillArcPaint.setDither(true);

        fillArcPaint.setStrokeJoin(Paint.Join.ROUND);

    }


    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (reset) {

            canvas.drawColor(Color.TRANSPARENT);

            reset = false;

        }

        drawcircle(canvas);


    }


    private void drawcircle(Canvas canvas) {

        int height = getMeasuredWidth();

        int width = getMeasuredWidth();

        //半径 = 宽/2-圆环的宽度

        int radius = width / 2 - arcradus;

        int cx = width / 2;

        int cy = height / 2;


        // 环形颜色填充

        SweepGradient sweepGradient =

                new SweepGradient(cx, cy, arcColors, null);

        fillArcPaint.setShader(sweepGradient);

        // 设置画笔为白色


        // 模糊效果

        fillArcPaint.setMaskFilter(mBlur);

        // 设置线的类型,边是圆的

        fillArcPaint.setStrokeCap(Paint.Cap.ROUND);


        //设置圆弧的宽度

        fillArcPaint.setStrokeWidth(arcradus + 1);

        // 确定圆弧的绘制位置，也就是里面圆弧坐标和外面圆弧坐标

        oval.set(width / 2 - radius, height / 2 - radius, width

                / 2 + radius, height / 2 + radius);

        // 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心


        canvas.drawArc(oval,

                startprogress,

                ((float) progress / max) * 360,

                false,

                fillArcPaint);


        //第三步:画圆环内百分比文字
        if (!isvisible) {
            Paint paint = new Paint();
            Rect rect = new Rect();

            paint.setColor(getResources().getColor(R.color.tv_reward_bg));
            paint.setTextSize(10);
            paint.setStrokeWidth(0);
            progressText = getProgressText();
            paint.getTextBounds(progressText, 0, progressText.length(), rect);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  //获得文字的基准线
            canvas.drawText(progressText, getMeasuredWidth() / 2 - rect.width() / 2, baseline, paint);
        }
    }

    public int getProgress() {

        return progress;

    }

    private String getProgressText() {
        return (int) ((progress / 100) * 100) + "%";
    }


    public void setProgress(int progress) {

        this.progress = progress;

        this.invalidate();

    }


    public int getMax() {

        return max;

    }


    public void setMax(int max) {

        this.max = max;

    }

    public int[] getArcColors() {

        return arcColors;

    }


    public void setArcColors(int[] arcColors) {

        this.arcColors = arcColors;

//		this.invalidate();

    }


    /**
     * 描述：重置进度
     *
     * @throws
     */

    public void reset() {

        reset = true;

        this.progress = 0;

        this.invalidate();

    }
}
