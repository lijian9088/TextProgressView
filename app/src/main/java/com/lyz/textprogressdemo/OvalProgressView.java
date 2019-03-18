package com.lyz.textprogressdemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author liyanze
 * @create 2019/03/13
 * @Describe
 */
public class OvalProgressView extends View {

    public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
    private float strokeWidth = 2f;
    private float leftMargin;
    private float topMargin;
    private float rightMargin;
    private float bottomMargin;
    private float smallRoundRectFOffset;
    private float progress = 0;
    private float max = 100;
    private ObjectAnimator animator;
    private long duration = 1000;
    private int bgColor;
    private int progressColor;
    private int progressTextColor;
    private int[] percentColors;
    private float progressTextSize;

    public OvalProgressView(Context context) {
        this(context, null);
    }

    public OvalProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgColor = Color.BLACK;
        progressColor = Color.RED;
        progressTextColor = Color.BLACK;
        percentColors = new int[]{
                Color.parseColor("#fe4ba5"),
                Color.parseColor("#00e1d9")
        };
    }

    public int[] getPercentColors() {
        return percentColors;
    }

    public void setPercentColors(int[] percentColors) {
        this.percentColors = percentColors;
        invalidate();
    }

    public void anim(float endProgress) {
        if (animator != null) {
            if (animator.isRunning()) {
                animator.cancel();
            }
        }
        animator = ObjectAnimator.ofFloat(this, "progress", 0, endProgress);
        animator.setDuration(duration);
        animator.start();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (progress <= 20) {
            bgColor = percentColors[0];
            progressColor = percentColors[0];
        } else {
            bgColor = percentColors[1];
            progressColor = percentColors[1];
        }
        invalidate();
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
        invalidate();
    }

    public float getProgressTextSize() {
        return progressTextSize;
    }

    public void setProgressTextSize(float progressTextSize) {
        this.progressTextSize = progressTextSize;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        leftMargin = getPaddingLeft() + strokeWidth;
        topMargin = getPaddingTop() + strokeWidth;
        rightMargin = getPaddingRight() + strokeWidth;
        bottomMargin = getPaddingBottom() + strokeWidth;
        if (smallRoundRectFOffset <= 0) {
            smallRoundRectFOffset = h / 10f;
        }
        if (progressTextSize <= 0) {
            progressTextSize = h / 2f;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawProgress(canvas);
        drawProgressText(canvas);
    }

    private void drawBg(Canvas canvas) {

        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        RectF rectF = new RectF(leftMargin, topMargin, getWidth() - rightMargin, getHeight() - bottomMargin);
        float ry = (getHeight() - topMargin - bottomMargin) / 2f;
        canvas.drawRoundRect(rectF, ry, ry, paint);
    }

    private void drawProgress(Canvas canvas) {

        paint.setColor(progressColor);
        paint.setStyle(Paint.Style.FILL);

        RectF rectF = new RectF(
                leftMargin + smallRoundRectFOffset,
                topMargin + smallRoundRectFOffset,
                getWidth() - rightMargin - smallRoundRectFOffset,
                getHeight() - bottomMargin - smallRoundRectFOffset);

        float ry = (getHeight() - topMargin - bottomMargin - smallRoundRectFOffset * 2f) / 2f;
        float progressRight = rectF.left + (rectF.right - rectF.left) * (progress / max);

        canvas.save();
        canvas.clipRect(rectF.left, rectF.top, progressRight, rectF.bottom);
        canvas.drawRoundRect(rectF, ry, ry, paint);
        canvas.restore();
    }

    private void drawProgressText(Canvas canvas) {
        paint.setColor(progressTextColor);
        paint.setTextAlign(Paint.Align.CENTER);

        float p = progress * 100 / max;
        String text = Math.round(p) + "%";
        drawTextCenter(canvas, text, progressTextSize);
    }

    private void drawTextCenter(Canvas canvas, String text, float textSize) {
        paint.setTextSize(textSize);
        paint.getFontMetrics(fontMetrics);
        float offset = (fontMetrics.ascent + fontMetrics.descent) / 2;
        canvas.drawText(text, getWidth() / 2f, getHeight() / 2f - offset, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = resolveSize(400, widthMeasureSpec);
        int height = resolveSize(100, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private float getCenterY() {
        return getHeight() / 2f;
    }

    private float getCenterX() {
        return getWidth() / 2f;
    }
}
