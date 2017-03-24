package com.example.gzp.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ben on 2017/3/23.
 * 为Box实现Parcelable接口
 *然后我们就可以在View.onSaveInstanceState()方法中存储 ArrayList of Objects: mBoxes，调用
 *Bundle.putParcelableArrayList(String key, ArrayList<? extends Parcelable> value)
 */

public class Box implements Parcelable{
    private PointF mCurrent;
    private PointF mOrigin;


    public Box(PointF origin) {
        mCurrent=origin;
        mOrigin=origin;
    }

    protected Box(Parcel in) {
        //读取的顺序一定要和写出的顺序一致
        mCurrent =(PointF) in.readValue(null);
        mOrigin=(PointF) in.readValue(null);

    }

    public static final Parcelable.Creator<Box> CREATOR = new Parcelable.Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };

    public PointF getOrigin() {
        return mOrigin;
    }
    public PointF getCurrent() {
        return mCurrent;
    }
    public void setCurrent(PointF current) {
        mCurrent = current;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mCurrent);
        dest.writeValue(mOrigin);
    }
}
