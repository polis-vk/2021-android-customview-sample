package ru.ok.technopolis.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class WaveView extends View {

    private static final int DEFAULT_ITEM_WIDTH_DP = 2;
    private static final int DEFAULT_ITEM_COLOR = Color.BLACK;

    private final Path wavePath = new Path();
    private final Paint linePaint = new Paint();

    private final int itemWidth;
    private final int[] originalData;

    private int[] measuredData;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int itemWidthFromAttr = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ITEM_WIDTH_DP, displayMetrics) + 0.5f);
        int itemColorFromAttr = DEFAULT_ITEM_COLOR;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
            itemWidthFromAttr = typedArray.getDimensionPixelSize(R.styleable.WaveView_itemWidth, itemWidthFromAttr);
            itemColorFromAttr = typedArray.getColor(R.styleable.WaveView_itemColor, itemColorFromAttr);
            typedArray.recycle();
        }
        itemWidth = itemWidthFromAttr;
        originalData = WaveRepository.getWaveData();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(itemColorFromAttr);
        linePaint.setStrokeWidth(itemWidthFromAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int width = originalData.length * itemWidth * 2 - itemWidth + leftPadding + rightPadding;
        width = resolveSize(width, widthMeasureSpec);
        int itemCount = (measuredWidth + itemWidth - leftPadding - rightPadding) / (itemWidth * 2);
        measuredData = LinearInterpolation.interpolateArray(originalData, itemCount);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (measuredData == null) {
            return;
        }
        wavePath.reset();
        int paddingTop = getPaddingTop();
        int measuredHeight = getMeasuredHeight() - paddingTop - getPaddingBottom();
        int currentX = itemWidth + getPaddingLeft();
        for (int data : measuredData) {
            float height = ((float) data / WaveRepository.MAX_VOLUME) * measuredHeight;
            float startY = (float) measuredHeight / 2f - height / 2f + paddingTop;
            float endY = startY + height;
            wavePath.moveTo(currentX, startY);
            wavePath.lineTo(currentX, endY);
            currentX += itemWidth * 2;
        }
        canvas.drawPath(wavePath, linePaint);
    }

}
