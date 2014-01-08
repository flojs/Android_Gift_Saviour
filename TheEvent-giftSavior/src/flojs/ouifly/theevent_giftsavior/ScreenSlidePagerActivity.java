package flojs.ouifly.theevent_giftsavior;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


public class ScreenSlidePagerActivity extends FragmentActivity {

    private static final int NUM_PAGES = 7;

    
    //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
    private ViewPager mPager;
    //The pager adapter, which provides the pages to the view pager widget.
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_screen_slide_pager);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    //A simple pager adapter that represents 7 ScreenSlidePageFragment objects, in sequence.
     
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
        	super(fragmentManager);
		}

		@Override
        public android.support.v4.app.Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
