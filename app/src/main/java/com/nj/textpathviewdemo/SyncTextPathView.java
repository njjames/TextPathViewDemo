package com.nj.textpathviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nj on 2018/3/8.
 */

public class SyncTextPathView extends View {
    private String mText;    //需要绘制的文字
    private PathMeasure mPathMeasure;  //绘制path的辅助类
    private float mLengthSum;  //文字path的总长度
    private Paint mTextPaint;  //绘制文字的画笔
    private Path mFontPath;    //文字的全部路径
    private Path mDst;    //需要画的路径
    private float mStop;  //绘制部分的文字路径长度
    private float[] mPos = new float[2];
    private Paint mDrawPaint;  //绘制路径的画笔

    public SyncTextPathView(Context context) {
        this(context, null);
    }

    public SyncTextPathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SyncTextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPathMeasure = new PathMeasure();
        mTextPaint = new Paint();
        mFontPath = new Path();
        mDst = new Path();
        mDrawPaint = new Paint();
    }

    public void setText(String text) {
        mText = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mDst, mDrawPaint);
    }

    public void initTextPath() {
        //根据文字获取其路径，保存到mFontPath中
        mTextPaint.getTextPath(mText, 0, mText.length(), 0, mTextPaint.getTextSize(), mFontPath);
        //让PathMesure与一个Path关联
        mPathMeasure.setPath(mFontPath, false);
        //获取第一个片段的路径长度
        mLengthSum = mPathMeasure.getLength();
        //循环获取后面每个片段的路径长度，然后合计到mKengthSum中
        while (mPathMeasure.nextContour()) {
            mLengthSum += mPathMeasure.getLength();
        }
    }

    public void drawPath(float progress) {
        mStop = mLengthSum * progress;
        //重置路径
        mPathMeasure.setPath(mFontPath, false);
        mDst.reset();
        //循环获取需要绘制的路径，当需要绘制的路径长度大于当前片段的路径长度时，进入循环
        while (mStop > mPathMeasure.getLength()) {
            //把要绘制的长度减去当前片段的路径长度
            mStop = mStop - mPathMeasure.getLength();
            //并把当前的片段加入到要绘制的mDst
            mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDst, true);
            //如果能获取到下一个片段就继续循环，否则跳出循环
            if (!mPathMeasure.nextContour()) {
                break;
            }
        }
        //此时只有剩余的路径（例如不够一个片段的），也添加到绘制的路径中
        mPathMeasure.getSegment(0, mStop, mDst, true);

        mPathMeasure.getPosTan(mStop, mPos, null);
        //重绘
        postInvalidate();
    }

}
