package com.example.zhuyong.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhuyong on 2017/7/2.
 */

public class CircleProgressBar extends View {

    private int mCurrent = 0;//当前进度
    private Paint mPaintCurrent;
    private float mCircularRadius;//初始圆的半径
    private int mCircularColorBg = Color.RED;//初始圆的背景色
    private int mProgressCircularColorBg = Color.RED;//进度圆的背景色
    private int location;//从哪个位置开始
    private float startAngle;//开始角度
    private int TIME = 10;//默认每隔10ms重绘一次
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                handler.postDelayed(this, TIME);
                mCurrent++;
                if (mCurrent == 100) {
                    mCurrent = -100;
                }
                invalidate();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaintCurrent = new Paint();
        //获取属性值
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        location = array.getInt(R.styleable.CircleProgressBar_progress_location, 2);
        mCircularRadius = array.getDimension(R.styleable.CircleProgressBar_progress_circular_radius, dip2px(context, 4));//默认4dp
        mCircularColorBg = array.getColor(R.styleable.CircleProgressBar_circular_color_bg, mCircularColorBg);
        mProgressCircularColorBg = array.getColor(R.styleable.CircleProgressBar_progress_circular_color_bg, mCircularColorBg);
        array.recycle();
        initPaint();
    }


    private void initPaint() {
        //画笔->进度圆弧
        mPaintCurrent.setAntiAlias(true);
        mPaintCurrent.setStyle(Paint.Style.FILL);
        mPaintCurrent.setColor(mProgressCircularColorBg);

        if (location == 1) {
            startAngle = -180;
        } else if (location == 2) {//默认从上侧开始
            startAngle = -90;
        } else if (location == 3) {
            startAngle = 0;
        } else if (location == 4) {
            startAngle = 90;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画最外层的大圆环
         */
        int centre = getWidth()/2; //获取圆心的x坐标
        int radius = (int) (centre - mCircularRadius/2); //圆环的半径
        mPaintCurrent.setAntiAlias(true);
        mPaintCurrent.setColor(mCircularColorBg);
        mPaintCurrent.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centre, centre, radius, mPaintCurrent);



        //绘制当前进度
        RectF rectF = new RectF(mCircularRadius / 2, mCircularRadius / 2, getWidth() - mCircularRadius / 2, getHeight() - mCircularRadius / 2);
        float sweepAngle = 360 * mCurrent / 100;
        canvas.drawArc(rectF, startAngle, sweepAngle, true, mPaintCurrent);
    }

    /**
     * 开始
     */
    public void start() {
        handler.postDelayed(runnable, TIME); //每隔TIMEms执行
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
