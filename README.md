# FlipViewPager
flipviewpager
 
        viewPager.setAdapter(adapter,FlipViewPager.FLIP_LEFT);
        viewPager.setRotateOffset(100);
        viewPager.setOnFlipOverListener(new IFlipOverListener() {
            @Override
            public void flipOver() {
                Toast.makeText(FlipDemoActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });
