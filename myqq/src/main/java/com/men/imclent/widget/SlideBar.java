package com.men.imclent.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.men.imclent.R;
import com.men.imclent.adater.ContactAdapter;
import com.men.imclent.utils.StringUtils;

import java.util.List;

public class SlideBar extends View {
    private String zimus[] ={"搜","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private float slideBarHeight;
    private float slideBarWidth;
    private Paint paint;
    private TextView tvFloat;
    private RecyclerView rvRecycler;
    private ContactAdapter adapter;

    public SlideBar(Context context) {
        this(context,null);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(getResources().getDimension(R.dimen.slidebar_size));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        slideBarHeight = getMeasuredHeight();
        slideBarWidth =getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i <zimus.length ; i++) {
//            float boundWidth = getTextBoundsWidth(zimus[i]);
//            float boundHeight=getTextBoundsHeight(zimus[i]);
            float drawW = slideBarWidth/2;
            float drawH =slideBarHeight/zimus.length*(i+1);
            canvas.drawText(zimus[i],drawW, drawH,paint);
        }
    }

    public float getTextBoundsWidth(String zimu){
        Rect bounds = new Rect();
        paint.getTextBounds(zimu,0,zimu.length(),bounds);
        return bounds.width();
    }

    public float getTextBoundsHeight(String zimu){
        Rect bounds = new Rect();
        paint.getTextBounds(zimu,0,zimu.length(),bounds);
        return bounds.width();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String startChar=null;
        if (tvFloat == null) {
            ViewGroup viewGroup = (ViewGroup) getParent();
            tvFloat = (TextView) viewGroup.findViewById(R.id.tv_float);
            rvRecycler = (RecyclerView) viewGroup.findViewById(R.id.rv_recycler);
            adapter = (ContactAdapter) rvRecycler.getAdapter();

        }

        switch (event.getAction()) {
                       case (MotionEvent.ACTION_DOWN):
                           setBackgroundResource(R.color.inactive);
                           tvFloat.setVisibility(View.VISIBLE);
                           startChar = zimus[getIndex(event.getY())];
                           tvFloat.setText(startChar);
                           scorllRecyclerview(startChar);
                           break;
                       case (MotionEvent.ACTION_MOVE):
                           startChar = zimus[getIndex(event.getY())];
                           tvFloat.setText(startChar);
                           scorllRecyclerview(startChar);
                           break;
                       case (MotionEvent.ACTION_UP):
                           Log.d("onTouchEvent","onTouchEvent..");
                           setBackgroundColor(Color.TRANSPARENT );
                           tvFloat.setVisibility(View.GONE);
                           break;
               }
        return true;
    }

    private void scorllRecyclerview(String startChar) {
        List<String> contacts = adapter.getContacts();
        if (contacts != null && contacts.size() > 0) {
            for (int i = 0; i <contacts.size() ; i++) {
                String firstChar = StringUtils.getFirstChar(contacts.get(i));
                if (firstChar.equals(startChar)) {
                    rvRecycler.smoothScrollToPosition(i);
                    break;
                }
            }
        }
    }

    private int getIndex(float y) {
        int selectionHeight=(int)slideBarHeight/zimus.length;
        int result = (int) (y/selectionHeight);
        return result<0?0:result>zimus.length-1?zimus.length-1:result;
    }
}
