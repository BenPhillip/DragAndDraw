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
    private PointF mCurrentPoint;
    private PointF mFirstPoint;
    private PointF mSecondPoint;
    private float mDegress =0;


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
        //获取当前手指的点数
        int pointIndex=event.getPointerCount()-1;
        mCurrentPoint =new PointF(event.getX(pointIndex), event.getY(pointIndex));
//        String action ="";

        switch (event.getActionMasked()) {
                //  第一个点按住
            case MotionEvent.ACTION_DOWN:
//                action = "ACTION_DOWN";
                mFirstPoint=mCurrentPoint;
                if (mCurrentBox == null) {
                    mCurrentBox = new Box(mFirstPoint);
                    mBoxen.add(mCurrentBox);
                }
                break;
            //有点在屏幕上移动时
            case MotionEvent.ACTION_MOVE:
//                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(mCurrentPoint);
                }else{
                    mDegress+=getDegree();
                }
                invalidate();
                break;
            //最后一个点松开
            case MotionEvent.ACTION_UP:
//                action = "ACTION_UP";
                mCurrentBox=null;
//                mFirstPoint=null;

                break;
            //当屏幕上已经有一个点被按住，此时再按下其他点时触发
            case MotionEvent.ACTION_POINTER_DOWN:
//                action = "ACTION_POINTER_DOWN";
                mSecondPoint=mCurrentPoint;
                mCurrentBox=null;
                 break;
            //当屏幕上有多个点被按住，松开其中一个点时触发
            case MotionEvent.ACTION_POINTER_UP:
//                action = "ACTION_POINTER_UP";
//                mSecondPoint=null;
                 break;
            //取消手势
            case MotionEvent.ACTION_CANCEL:
//                action = "ACTION_CANCEL";
                Log.i(TAG, "onTouchEvent: cancel");
                mCurrentBox=null;
                break;
            default:
//                action="";
//                current = new PointF(event.getX(), event.getY());
//                Log.w(TAG, "onTouchEvent: no found Constant Value" );
                break;
        }

//        Log.i(TAG, "onTouchEvent: "+action+" at x="+current.x+",y="+current.y);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.save();

        canvas.drawPaint(mBackgroundPaint);

        canvas.rotate(mDegress,canvas.getWidth()/2,getHeight()/2);

        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);


        }
//        canvas.restore();
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

    private double getDistance(PointF pf1, PointF pf2) {
        float disX = pf2.x - pf1.x;
        float disY = pf2.y - pf1.y;
        return Math.sqrt(disX * disX + disY * disY);
    }

    // 弧度换算成角度
    private double radianToDegree(double radian) {
        return radian * 180 / Math.PI;
    }
    //计算旋转的角度。
    private float getDegree() {
        double a = getDistance(mFirstPoint, mSecondPoint);
        double b = getDistance(mSecondPoint, mCurrentPoint);
        double c = getDistance(mFirstPoint, mCurrentPoint);
        double cosb = (a * a + c * c - b * b) / (2 * a * c);
        if (cosb >= 1) {
            cosb = 1f;
        }
        double radian = Math.acos(cosb);
        float newDegree = (float) radianToDegree(radian);
        PointF firstToSecond =
                new PointF((mSecondPoint.x - mFirstPoint.x),
                        (mSecondPoint.y - mFirstPoint.y));
        PointF firstToCurrent
                = new PointF((mCurrentPoint.x - mFirstPoint.x),
                (mCurrentPoint.y - mFirstPoint.y));
        //向量叉乘结果, 如果结果为负数， 表示为逆时针， 结果为正数表示顺时针
        float result = firstToSecond.x * firstToCurrent.y
                - firstToSecond.y * firstToCurrent.x;
        if (result < 0) {
            newDegree = -newDegree;
        }
        return newDegree;
    }

}
