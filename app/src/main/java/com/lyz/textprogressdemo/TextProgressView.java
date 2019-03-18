package com.lyz.textprogressdemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author liyanze
 * @create 2019/03/06
 * @Describe
 */
public class TextProgressView extends View {

    public static final int ORANGE = Color.parseColor("#ff9e46");
    public static final int YELLOW = Color.parseColor("#fff44d");
    public static final int LIGHT_BLUE = Color.parseColor("#51fafd");
    public static final int LIGHT_PURPLE = Color.parseColor("#bd9cff");
    public static final int PURPLE = Color.parseColor("#541bc5");
    public static final int LIGHT_GRAY = Color.parseColor("#666666");

    public int colorBg;
    public int colorProgress;
    public int colorBgText;
    public int colorProgressText;
    public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public float leftMargin;
    public float rightMargin;
    public float bgStrokeWidth;
    public float bigRadius;
    public float smallRadius;
    public float currentSize;
    public float maxSize;
    private ObjectAnimator animator;
    private float[] circlePositionSize;
    private float fullXLength = -1;
    private String[] aboveTextArr;
    private String[] underTextArr;
    private boolean isRoundCap = true;
    private float textSize;
    private long duration;

    public TextProgressView(Context context) {
        this(context, null);
    }

    public TextProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextProgressView);
        colorBg = typedArray.getColor(R.styleable.TextProgressView_tpvColorBg, LIGHT_PURPLE);
        colorProgress = typedArray.getColor(R.styleable.TextProgressView_tpvColorProgress, PURPLE);
        colorBgText = typedArray.getColor(R.styleable.TextProgressView_tpvColorBgText, LIGHT_GRAY);
        colorProgressText = typedArray.getColor(R.styleable.TextProgressView_tpvColorProgressText, Color.BLACK);
        leftMargin = typedArray.getDimension(R.styleable.TextProgressView_tpvContentMargin, 60);
        rightMargin = leftMargin;
        bgStrokeWidth = typedArray.getDimension(R.styleable.TextProgressView_tpvBgStrokeWidth, 30);
        bigRadius = typedArray.getDimension(R.styleable.TextProgressView_tpvBigRadius, 26);
        smallRadius = typedArray.getDimension(R.styleable.TextProgressView_tpvSmallRadius, 18);
        textSize = typedArray.getDimension(R.styleable.TextProgressView_tpvTextSize, 30);
        currentSize = typedArray.getInt(R.styleable.TextProgressView_tpvCurrentSize, 0);
        maxSize = typedArray.getInt(R.styleable.TextProgressView_tpvMaxSize, 100);
        duration = typedArray.getInt(R.styleable.TextProgressView_tpvDuration, 1000);
        typedArray.recycle();
    }

    public float getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(float leftMargin) {
        this.leftMargin = leftMargin;
        invalidate();
    }

    public float getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(float rightMargin) {
        this.rightMargin = rightMargin;
        invalidate();
    }

    public void setContentMargin(float margin) {
        this.leftMargin = margin;
        this.rightMargin = margin;
        invalidate();
    }

    public int getColorBg() {
        return colorBg;
    }

    public void setColorBg(int colorBg) {
        this.colorBg = colorBg;
        invalidate();
    }

    public int getColorProgress() {
        return colorProgress;
    }

    public void setColorProgress(int colorProgress) {
        this.colorProgress = colorProgress;
        invalidate();
    }

    public int getColorBgText() {
        return colorBgText;
    }

    public void setColorBgText(int colorBgText) {
        this.colorBgText = colorBgText;
        invalidate();
    }

    public float getBgStrokeWidth() {
        return bgStrokeWidth;
    }

    public void setBgStrokeWidth(float bgStrokeWidth) {
        this.bgStrokeWidth = bgStrokeWidth;
        invalidate();
    }

    public float getBigRadius() {
        return bigRadius;
    }

    public void setBigRadius(float bigRadius) {
        this.bigRadius = bigRadius;
        invalidate();
    }

    public float getSmallRadius() {
        return smallRadius;
    }

    public void setSmallRadius(float smallRadius) {
        this.smallRadius = smallRadius;
        invalidate();
    }

    public boolean isRoundCap() {
        return isRoundCap;
    }

    public void setRoundCap(boolean roundCap) {
        isRoundCap = roundCap;
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    /**
     * 初始化
     * @param circlePositionSize
     * @param aboveTextArr
     * @param underTextArr
     */
    public void init(@Nullable float[] circlePositionSize, @Nullable String[] aboveTextArr, @Nullable String[] underTextArr) {
        this.circlePositionSize = circlePositionSize;
        this.aboveTextArr = aboveTextArr;
        this.underTextArr = underTextArr;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (fullXLength < 0) {
            fullXLength = getWidth() - leftMargin - rightMargin;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //进度条背景
        drawBackgroud(canvas);
        //文字说明
        drawText(canvas, colorBgText, colorProgressText);
        //实际进度条
        drawProgress(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void anim(float endSize) {
        if (animator != null) {
            if (animator.isRunning()) {
                animator.cancel();
            }
        }
        animator = ObjectAnimator.ofFloat(this, "currentSize", 0, endSize);
        animator.setDuration(duration);
        animator.start();
    }

    public float getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(float currentSize) {
        this.currentSize = currentSize;
        invalidate();
    }

    public float getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(float maxSize) {
        this.maxSize = maxSize;
        invalidate();
    }

    private void drawBackgroud(Canvas canvas) {
        drawBgLine(canvas, colorBg);
        drawBgCircle(canvas, colorBg);
    }

    private void drawProgress(Canvas canvas) {
        if (currentSize > 0) {
            float left = leftMargin;
            if (isRoundCap) {
                left = leftMargin - bgStrokeWidth / 2f;
            }
            float right = leftMargin + fullXLength * currentSize / maxSize;
            if (currentSize == maxSize) {
                right = getWidth();
            }
            canvas.clipRect(left, 0, right, getHeight());
            drawBgLine(canvas, colorProgress);
            drawBgCircle(canvas, colorProgress);
        }
    }

    private void drawText(Canvas canvas, @ColorInt int colorBgText, @ColorInt int colorProgressText) {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        if (circlePositionSize == null) {
            return;
        }

        for (int i = 0; i < circlePositionSize.length; i++) {
            float v = circlePositionSize[i] / maxSize;
            float cx = leftMargin + v * fullXLength;

            if (aboveTextArr != null && !TextUtils.isEmpty(aboveTextArr[i])) {
                String aboveText = aboveTextArr[i];
                paint.setColor(colorBgText);
                //背景文字
                canvas.drawText(aboveText, cx, getCenterY() - bigRadius - textSize / 2f, paint);
                //达到进度
                if (circlePositionSize[i] <= currentSize) {
                    paint.setColor(colorProgressText);
                    canvas.drawText(aboveText, cx, getCenterY() - bigRadius - textSize / 2f, paint);
                }
            }

            if (underTextArr != null && !TextUtils.isEmpty(underTextArr[i])) {
                String underText = underTextArr[i];
                paint.setColor(colorBgText);
                canvas.drawText(underText, cx, getCenterY() + bigRadius + textSize + textSize / 2f, paint);
                if (circlePositionSize[i] <= currentSize) {
                    paint.setColor(colorProgressText);
                    canvas.drawText(underText, cx, getCenterY() + bigRadius + textSize + textSize / 2f, paint);
                }
            }

        }

    }

    private void drawBgLine(Canvas canvas, @ColorInt int color) {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(bgStrokeWidth);
        if (isRoundCap) {
            paint.setStrokeCap(Paint.Cap.ROUND);
        }
        canvas.drawLine(leftMargin, getCenterY(), getWidth() - rightMargin, getCenterY(), paint);
    }

    private void drawBgCircle(Canvas canvas, @ColorInt int color) {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        if (circlePositionSize == null) {
            return;
        }

        for (int i = 0; i < circlePositionSize.length; i++) {
            float v = circlePositionSize[i] / maxSize;
            float cx = leftMargin + v * fullXLength;

            paint.setColor(color);
            canvas.drawCircle(cx, getCenterY(), bigRadius, paint);

            paint.setColor(Color.WHITE);
            canvas.drawCircle(cx, getCenterY(), smallRadius, paint);
        }

    }

    private float getCenterY() {
        return getHeight() / 2f;
    }

    private float getCenterX() {
        return getWidth() / 2f;
    }


}