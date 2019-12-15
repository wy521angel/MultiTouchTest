package com.wy521angel.multitouchtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MultiTouchView2 extends View {
    private static final float IMAGE_WIDTH = Utils.dp2px(200);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    float offsetX;
    float offsetY;
    float originalOffsetX;
    float originalOffsetY;
    float downX;
    float downY;

    public MultiTouchView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        bitmap = Utils.getBitmapByDrawableResources(getResources(), R.drawable.gem,
                (int) IMAGE_WIDTH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //所有触摸手指的总坐标值相加和
        float sumX = 0;
        float sumY = 0;
        int pointerCount = event.getPointerCount();
        boolean isPointerUp = event.getActionMasked() == MotionEvent.ACTION_POINTER_UP;
        for (int i = 0; i < pointerCount; i++) {
            if (isPointerUp && i == event.getActionIndex()) {
                //如果是抬起的事件 isPointerUp 并且当前遍历的是抬起的手指则不需要叠加坐标
            } else {
                sumX += event.getX(i);
                sumY += event.getY(i);
            }
        }

        //如果是抬起手指的操作，此处需要减一
        if (isPointerUp) {
            pointerCount --;
        }
        //多点触控的虚拟中心点坐标值
        float focusX = sumX / pointerCount;
        float focusY = sumY / pointerCount;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                downX = focusX;
                downY = focusY;
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = originalOffsetX +//初始偏移
                        focusX - downX;//由于手指移动导致的偏移
                offsetY = originalOffsetY +
                        focusY - downY;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }
}
