package com.raizlabs.freshair;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

class LinePageIndicator extends View {

    private static final int DEFAULT_UNSELECTED_COLOR = Color.argb(50, 120, 120, 120);
    private static final int DEFAULT_SELECTED_COLOR = Color.argb(140, 0, 0, 0);

    private static final int DEFAULT_THICKNESS_DIPS = 5;
    private static final int DEFAULT_LINE_GAP_DIPS = 10;
    private static final int DEFAULT_MAX_LINE_WIDTH_DIPS = 50;

    private final Paint unselectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int lineGap;
    private int maxLineWidth;
    private ViewPager viewPager;

    private int currentPagerPosition;
    private float currentPagerOffset;

    public LinePageIndicator(Context context) {
        super(context);
        init();
    }

    public LinePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.LinePageIndicator);
        readArray(arr);
        arr.recycle();
    }

    public LinePageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.LinePageIndicator, defStyleAttr, 0);
        readArray(arr);
        arr.recycle();
    }

    private void init() {
        setUnselectedColor(DEFAULT_UNSELECTED_COLOR);
        setSelectedColor(DEFAULT_SELECTED_COLOR);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        setLineThickness(Utils.dipsToPixels(DEFAULT_THICKNESS_DIPS, metrics));
        setLineGap((int) Utils.dipsToPixels(DEFAULT_LINE_GAP_DIPS, metrics));
        setMaxLineWidth((int) Utils.dipsToPixels(DEFAULT_MAX_LINE_WIDTH_DIPS, metrics));
    }

    private void readArray(TypedArray arr) {
        setSelectedColor(arr.getColor(R.styleable.LinePageIndicator_linePageIndicatorSelectedColor, getSelectedColor()));
        setUnselectedColor(arr.getColor(R.styleable.LinePageIndicator_linePageIndicatorUnselectedColor, getUnselectedColor()));

        setLineThickness(arr.getDimension(R.styleable.LinePageIndicator_linePageIndicatorLineThickness, getLineThickness()));
        setLineGap(arr.getDimensionPixelSize(R.styleable.LinePageIndicator_linePageIndicatorLineGap, getLineGap()));
        setMaxLineWidth(arr.getDimensionPixelSize(R.styleable.LinePageIndicator_linePageIndicatorMaxLineWidth, getMaxLineWidth()));
    }

    public void setUnselectedColor(int color) {
        unselectedPaint.setColor(color);
        postInvalidate();
    }

    public int getUnselectedColor() {
        return unselectedPaint.getColor();
    }

    public void setSelectedColor(int color) {
        selectedPaint.setColor(color);
        postInvalidate();
    }

    public int getSelectedColor() {
        return selectedPaint.getColor();
    }

    public float getLineThickness() {
        return unselectedPaint.getStrokeWidth();
    }

    /**
     * Sets the thickness of the individual page lines
     *
     * @param thickness The line thickness in pixels
     */
    public void setLineThickness(float thickness) {
        unselectedPaint.setStrokeWidth(thickness);
        selectedPaint.setStrokeWidth(thickness);
        postInvalidate();
    }

    /**
     * Sets the gap between two adjacent page lines
     *
     * @param gap The gap in pixels
     */
    public void setLineGap(int gap) {
        this.lineGap = gap;
        postInvalidate();
    }

    public int getLineGap() {
        return this.lineGap;
    }

    /**
     * Sets the max width that is allowed for each line. Width is clamped
     * to this value and lines are centered if we have more space than we
     * need.
     *
     * @param width The width in pxiels
     */
    public void setMaxLineWidth(int width) {
        this.maxLineWidth = width;
        postInvalidate();
    }

    public int getMaxLineWidth() {
        return this.maxLineWidth;
    }


    public void setViewPager(ViewPager pager) {
        if (this.viewPager != null) {
            this.viewPager.removeOnPageChangeListener(pageChangeListener);
        }
        this.viewPager = pager;
        pager.addOnPageChangeListener(pageChangeListener);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int itemCount = 3;
        if (!isInEditMode()) {
            if (viewPager == null) return;
            if (viewPager.getAdapter() == null) return;
            itemCount = viewPager.getAdapter().getCount();
        }

        if (itemCount == 0) return;

        int totalViewWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        float startX = getPaddingLeft();
        int strokeY = ((getHeight() - getPaddingTop() - getPaddingBottom()) / 2) + getPaddingTop();

        float gapAmount = lineGap * (itemCount - 1);
        float lineWidth = ((float) totalViewWidth - gapAmount) / (float) itemCount;

        if (lineWidth > maxLineWidth) {
            lineWidth = maxLineWidth;

            float centerX = ((float) totalViewWidth / 2) + (float) getPaddingLeft();
            float totalLineWidth = (lineWidth * itemCount) + gapAmount;
            startX = centerX - (totalLineWidth / 2);
        }

        for (int i = 0; i < itemCount; i++) {
            float stopX = startX + lineWidth;
            if (i == currentPagerPosition) {
                float flip = startX + (stopX - startX) * currentPagerOffset; // Lerp: start + (end - start) * t
                canvas.drawLine(startX, strokeY, flip, strokeY, unselectedPaint);
                canvas.drawLine(flip, strokeY, stopX, strokeY, selectedPaint);
            } else if (i == currentPagerPosition + 1) {
                float flip = startX + (stopX - startX) * currentPagerOffset; // Lerp: start + (end - start) * t
                canvas.drawLine(startX, strokeY, flip, strokeY, selectedPaint);
                canvas.drawLine(flip, strokeY, stopX, strokeY, unselectedPaint);
            } else {
                canvas.drawLine(startX, strokeY, stopX, strokeY, unselectedPaint);
            }

            startX = stopX + lineGap;
        }
    }

    private final OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPagerPosition = position;
            currentPagerOffset = positionOffset;
            postInvalidate();
        }

        @Override
        public void onPageSelected(int position) {
            currentPagerPosition = position;
            currentPagerOffset = 0;
            postInvalidate();
        }
    };
}