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
    int trackingPointerId;//追踪手指的id

    public MultiTouchView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        bitmap = Utils.getBitmapByDrawableResources(getResources(), R.drawable.gem,
                (int) IMAGE_WIDTH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                trackingPointerId = event.getPointerId(0);//拿到初始手指的id
                downX = event.getX();
                downY = event.getY();
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                int index = event.findPointerIndex(trackingPointerId);
                offsetX = originalOffsetX +//初始偏移
                        event.getX(index) - downX;//由于手指移动导致的偏移
                offsetY = originalOffsetY +
                        event.getY(index) - downY;
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int actionIndex = event.getActionIndex();
                trackingPointerId = event.getPointerId(actionIndex);//拿到新落下手指的id
                downX = event.getX(actionIndex);
                downY = event.getY(actionIndex);
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                actionIndex = event.getActionIndex();
                //如果抬起的手指就是追踪的手指需要处理，否则不处理
                if (trackingPointerId == event.getPointerId(actionIndex)) {
                    int newIndex;//抬起手指后，事件的手指继承者的 index
                    //此处约定抬起追踪的手指后，将事件移交给最大号的手指
                    //比如现在有5个手指触摸到View，index 分别为 0 1 2 3 4
                    // 抬起一个后，将事件传递给最大号的手指
                    if (actionIndex == event.getPointerCount() - 1) {
                        //如果抬起的手指是5号最大号手指 index 为 4，需要将事件移交给剩下的最大号手指，也就是4号手指 index 为3
                        newIndex = event.getPointerCount() - 2;
                    } else {
                        //如果抬起的手指是3号不是最大号手指 ，需要将事件移交给最大号手指，也就是5号手指 index 为4
                        newIndex = event.getPointerCount() - 1;
                    }
                    trackingPointerId = event.getPointerId(newIndex);//拿到继承手指的id
                    downX = event.getX();
                    downY = event.getY();
                    originalOffsetX = offsetX;
                    originalOffsetY = offsetY;
                }
                break;

        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }
}
