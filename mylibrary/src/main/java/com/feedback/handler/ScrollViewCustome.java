package com.feedback.handler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Abandah on 6/30/2020.
 */
public class ScrollViewCustome extends ScrollView {

    public ScrollViewCustome(Context context) {
        super(context);
    }

    public ScrollViewCustome(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewCustome(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollViewCustome(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

/*
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(null);
    }
*/

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
      //  if(ev.getPointerCount() <2){
      //      return false;
       // }
            return super.onTouchEvent(ev);
    }
}
