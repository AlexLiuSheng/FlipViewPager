package com.allenliu.flipviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FlipDemoActivity extends AppCompatActivity {
private FlipViewPager viewPager;
    private FlipViewPagerAdapter adapter;
    private List<Object> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_demo);
        init();
    }

    private void init() {
        viewPager= (FlipViewPager) findViewById(R.id.viewpager);
        data=new ArrayList<>();
        for(int i=0;i<2;i++){
            ImageView img=new ImageView(this);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            img.setImageResource(R.mipmap.ceshi);
            data.add(img);
        }
        adapter=new FlipViewPagerAdapter(data);
        viewPager.setAdapter(adapter,FlipViewPager.FLIP_LEFT);
      //  viewPager.setRotateOffset(100);
        viewPager.setOnFlipOverListener(new IFlipOverListener() {
            @Override
            public void flipOver() {
                Toast.makeText(FlipDemoActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
