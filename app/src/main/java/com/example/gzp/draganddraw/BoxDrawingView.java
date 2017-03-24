package com.example.gzp.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Ben on 2017/3/23.
 */

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private static final String SAVED_SUPER_VIEW ="savedInstanceState" ;
    private static final String SAVED_BOX="boxen";

    private Box mCurrentBox;
    private ArrayList<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;


    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context,attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox=null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox=null;
                break;
        }

        Log.i(TAG, "onTouchEvent: "+action+" at x="+current.x+",y="+current.y);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box :
                mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    /**
     * View视图有ID时，才可以调用它们
     *需要保存BoxDrawingView的View父视图的状态。
     * 在Bundle中保存super.onSaveInstanceState()方法结果，
     * 然后调用super.onRestoreInstanceState(Parcelable)方法把结果发送给超类。
     * @return 将数据保存在Bundle中
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Log.i(TAG, "onSaveInstanceState: saved sucesess");
        super.onSaveInstanceState();

        Bundle bundle= new Bundle();
        bundle.putParcelable(SAVED_SUPER_VIEW,super.onSaveInstanceState());
        bundle.putParcelableArrayList(SAVED_BOX,mBoxen);

        return bundle;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.i(TAG, "onRestoreInstanceState: saved");
        if(state instanceof Bundle){
            Bundle bundle=(Bundle)state;
            state = bundle.getParcelable(SAVED_SUPER_VIEW);
            mBoxen = bundle.getParcelableArrayList(SAVED_BOX);
            Log.i(TAG, "onRestoreInstanceState: "+mBoxen.size());
        }
        super.onRestoreInstanceState(state);
    }

}
