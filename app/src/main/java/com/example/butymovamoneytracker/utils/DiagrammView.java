package com.example.butymovamoneytracker.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.butymovamoneytracker.R;

public class DiagrammView extends View {
    private long expense;
    private long income;
    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();

    public DiagrammView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        expensePaint.setColor(ContextCompat.getColor(context, R.color.red));
        incomePaint.setColor(ContextCompat.getColor(context, R.color.yellow));
        if (isInEditMode()){
            expense = 15000;
            income = 95000;
        }
    }

    public void update(long expense, long income){
        this.expense = expense;
        this.income = income;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPieCanvas(canvas);
    }

    private void drawPieCanvas(Canvas canvas){
        float expenseAngle = 360.f * expense / (expense + income);
        float incomeAngle = 360.f * income / (expense + income);
        int space = 10;
        int size = Math.min(getHeight(), getWidth()) - space*2;
        final int xMargin = (getWidth() - size)/2;
        final int yMargin = (getHeight() - size)/2;
        canvas.drawArc(xMargin-space, yMargin, getWidth()-xMargin-space, getHeight()-yMargin, 180-expenseAngle/2, expenseAngle, true, expensePaint);
        canvas.drawArc(xMargin+space, yMargin, getWidth()-xMargin+space, getHeight()-yMargin, 360-incomeAngle/2, incomeAngle, true, incomePaint);
    }
}