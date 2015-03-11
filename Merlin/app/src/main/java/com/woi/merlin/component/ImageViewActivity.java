package com.woi.merlin.component;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.woi.merlin.R;
import java.io.File;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by YeekFeiTan on 3/10/2015.
 */
public class ImageViewActivity extends Activity {

    private static final String ISLOCKED_ARG = "isLocked";

    private ViewPager mViewPager;
    private MenuItem menuLockItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_activity);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        setContentView(mViewPager);

        SamplePagerAdapter spa = new SamplePagerAdapter();
        if(getIntent().getStringArrayExtra("Images") != null) {
            String[] images = getIntent().getStringArrayExtra("Images");
            int position = getIntent().getIntExtra("openPosition", 0);
            spa.setsDrawables(images);

            mViewPager.setAdapter(spa);
            mViewPager.setCurrentItem(position);
        }

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
    }

    static class SamplePagerAdapter extends PagerAdapter {

        private String[] sDrawables = null;

//        private static final int[] sDrawables = { R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
//                R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper };

        @Override
        public int getCount() {
            return sDrawables.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
//            photoView.setImageResource(sDrawables[position]);
            photoView.setImageURI(Uri.fromFile(new File(sDrawables[position])));


            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public String[] getsDrawables() {
            return sDrawables;
        }

        public void setsDrawables(String[] sDrawables) {
            this.sDrawables = sDrawables;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuLockItem = menu.findItem(R.id.menu_lock);
        toggleLockBtnTitle();
        menuLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toggleViewPagerScrolling();
                toggleLockBtnTitle();
                return true;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void toggleViewPagerScrolling() {
        if (isViewPagerActive()) {
            ((HackyViewPager) mViewPager).toggleLock();
        }
    }

    private void toggleLockBtnTitle() {
        boolean isLocked = false;
        if (isViewPagerActive()) {
            isLocked = ((HackyViewPager) mViewPager).isLocked();
        }
        String title = (isLocked) ? "Unlock" : "Lock";
        if (menuLockItem != null) {
            menuLockItem.setTitle(title);
        }
    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

}