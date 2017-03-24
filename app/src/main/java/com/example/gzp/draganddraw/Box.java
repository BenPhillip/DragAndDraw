package com.example.gzp.draganddraw;

import android.graphics.PointF;

/**
 * Created by Ben on 2017/3/23.
 */

public class Box {
    private PointF mCurrent;
    private PointF mOrigin;


    public Box(PointF origin) {
        mCurrent=origin;
        mOrigin=origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }
    public PointF getCurrent() {
        return mCurrent;
    }
    public void setCurrent(PointF current) {
        mCurrent = current;
    }


}
