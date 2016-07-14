package com.allenliu.flipviewpager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Allen Liu on 2016/5/18.
 */
public class FlipViewPager extends ViewPager {
    private int rotateOffset;
    /**
     * 显示最左边
     */
    public static final int FLIP_LEFT = 1;
    /**
     * 显示最右边
     */
    public static final int FLIP_RIGHT = 2;

    private ImageView imageView;
    private TextView textView;
    private IFlipOverListener listener;
    private int currentDirection=2;
    /**
     * 是否是最后一夜
     */
   private boolean isFlipOver=false;
    public FlipViewPager(Context context) {
        super(context);
        init();
    }

    public FlipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
  public void setOnFlipOverListener(IFlipOverListener listener){
      this.listener=listener;
  }
    private void init() {
        rotateOffset = dip2px(getContext(), 100);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PagerAdapter adapter = getAdapter();
                if (adapter instanceof FlipViewPagerAdapter) {
                    if (currentDirection==FLIP_RIGHT&&position == ((FlipViewPagerAdapter) adapter).getData().size() - 2) {
                        if (positionOffsetPixels > rotateOffset) {
                            setCurrentItem(((FlipViewPagerAdapter) adapter).getData().size() - 2);
                            if(isFlipOver)
                                ObjectAnimator.ofFloat(imageView,"rotation",180f).setDuration(0).start();
                            textView.setText(getContext().getString(R.string.release_info));
                            isFlipOver=true;
                        }else{
                            float rotation=( ((float)positionOffsetPixels/(float) rotateOffset))*180f;
                            ObjectAnimator.ofFloat(imageView,"rotation",rotation).setDuration(0).start();
                            textView.setText(getContext().getString(R.string.slide_info));
                            isFlipOver=false;
                        }
                    }else if(currentDirection==FLIP_LEFT&&position==0){
                        float pixels=getScreenWidth(getContext())-positionOffsetPixels;
                        if (pixels >rotateOffset) {
                            setCurrentItem(1);
                            if(isFlipOver)
                                ObjectAnimator.ofFloat(imageView,"rotation",180f).setDuration(0).start();
                            textView.setText(getContext().getString(R.string.release_info));
                            isFlipOver=true;
                        }else{

                            float rotation=( ((float)pixels/(float) rotateOffset))*180f;
                            ObjectAnimator.ofFloat(imageView,"rotation",rotation).setDuration(0).start();
                            textView.setText(getContext().getString(R.string.slide_info));
                            isFlipOver=false;
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                PagerAdapter adapter = getAdapter();
                if (adapter instanceof FlipViewPagerAdapter) {
                    if (currentDirection==FLIP_RIGHT&&position == ((FlipViewPagerAdapter) adapter).getData().size() - 1) {
                        setCurrentItem(((FlipViewPagerAdapter) adapter).getData().size() - 2);
                    }else if(currentDirection==FLIP_LEFT&&position==0){
                        setCurrentItem(1);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               if(state==SCROLL_STATE_SETTLING){
                   if(isFlipOver){
                       if(listener!=null){
                           listener.flipOver();
                       }
                       isFlipOver=false;
                   }
               }
            }
        });
    }

    public void setAdapter(PagerAdapter adapter, int direction) {
        super.setAdapter(adapter);
        addFlipView(direction);

    }
    private void addFlipView(int direction) {
        View flipView;
          PagerAdapter adapter = getAdapter();
            if (direction == FLIP_RIGHT)
                flipView = LayoutInflater.from(getContext()).inflate(R.layout.filp_right_layout, null);
            else
                flipView = LayoutInflater.from(getContext()).inflate(R.layout.flip_left_layout, null);
            imageView = (ImageView) flipView.findViewById(R.id.iv_arrow);
            textView = (TextView) flipView.findViewById(R.id.tv_hint);
            //事先将图旋转180
            if (direction == FLIP_LEFT)
                ObjectAnimator.ofFloat(imageView, "rotation", 180f).setDuration(0).start();
            currentDirection = direction;

            if (adapter instanceof FlipViewPagerAdapter) {
                FlipViewPagerAdapter fadapter = (FlipViewPagerAdapter) adapter;
                List<Object> data = fadapter.getData();

                if (!data.contains(flipView) && data.size() > 0) {
                    if (direction == FLIP_RIGHT) {
                        data.add(flipView);
                        fadapter.setData(data);
                        fadapter.notifyDataSetChanged();
                        setCurrentItem(0);
                    }  else if (direction == FLIP_LEFT) {
                        data.add(0, flipView);
                        fadapter.setData(data);
                        fadapter.notifyDataSetChanged();
                        setCurrentItem(1);
                    }

                }
            }

    }

  public void setRotateOffset(int offset){
      this.rotateOffset=offset;
  }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    private float getScreenWidth(Context context){
        final float scale = context.getResources().getDisplayMetrics().widthPixels;
        return  scale;
    }
}
