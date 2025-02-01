package hobbibox.hobbibox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import hobbibox.hobbibox.pages.CategoriesFragment;
import hobbibox.hobbibox.pages.HistoryFragment;
import hobbibox.hobbibox.pages.ProfileFragment;
import hobbibox.hobbibox.pages.QueueFragment;
import hobbibox.hobbibox.pages.SuggestionsFragment;
import hobbibox.hobbibox.service.FirebaseService;

// Hold viewpager
public class MainFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private FirebaseService mFirebaseService;
    private List<Fragment> mFragments;
    private List<ImageView> mImageViews;
    private List<RelativeLayout> mRelLayouts;
    private int[] mGreyIcons;
    private int[] mWhiteIcons;

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private int mCurrentFragment = 2;
    private int mFragmentHistory = 2;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        loadParallelLists(view);
        setIconColor(mCurrentFragment);

        // Create view pager to swipe across fragments
        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), mFragments);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentFragment, false);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());

        // Move to page when navigation space is touched
        for(final RelativeLayout relativeLayout: mRelLayouts){
            relativeLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mFragmentHistory = mCurrentFragment;
                    mViewPager.setCurrentItem(mRelLayouts.indexOf(relativeLayout), true);
                    setIconColor(mRelLayouts.indexOf(relativeLayout));
                }
            });
        }

        // Update UI when page is changed
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mRelLayouts.get(position).performClick();
                switch (position){

                    case 1 : ((HistoryFragment)mPagerAdapter.getItem(position)).updateUI();
                        break;
                    case 2 : ((SuggestionsFragment)mPagerAdapter.getItem(position)).updateUI();
                        break;
                    case 3 : ((QueueFragment)mPagerAdapter.getItem(position)).updateUI();
                        break;
                    case 4 : ((CategoriesFragment)mPagerAdapter.getItem(position)).updateUI();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Log.d(TAG, "onCreateView()");
        return view;
    }

    public void backPressed(){
        mRelLayouts.get(mFragmentHistory).performClick();
    }

    // Adapter for view pager
    private class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    // Change navigation bar icon color depending on page
    private void setIconColor(int index) {
        mCurrentFragment = index;
        for(ImageView imageView: mImageViews){
            int pos = mImageViews.indexOf(imageView);
            if(pos == index)
                imageView.setImageResource(mWhiteIcons[pos]);
            else
                imageView.setImageResource(mGreyIcons[pos]);

        }
    }

    // Load equally sized lists where positions correlate with one another
    private void loadParallelLists(View view){
        mFragments = new ArrayList<Fragment>();
        mFragments.add(ProfileFragment.newInstance());
        mFragments.add(HistoryFragment.newInstance());
        mFragments.add(SuggestionsFragment.newInstance());
        mFragments.add(QueueFragment.newInstance());
        mFragments.add(CategoriesFragment.newInstance());

        mImageViews = new ArrayList<ImageView>();
        mImageViews.add((ImageView)view.findViewById(R.id.profile_page_icon));
        mImageViews.add((ImageView)view.findViewById(R.id.history_page_icon));
        mImageViews.add((ImageView)view.findViewById(R.id.suggestions_page_icon));
        mImageViews.add((ImageView)view.findViewById(R.id.queue_page_icon));
        mImageViews.add((ImageView)view.findViewById(R.id.categories_page_icon));

        mRelLayouts = new ArrayList<RelativeLayout>();
        mRelLayouts.add((RelativeLayout)view.findViewById(R.id.profileIconLayout));
        mRelLayouts.add((RelativeLayout)view.findViewById(R.id.historyIconLayout));
        mRelLayouts.add((RelativeLayout)view.findViewById(R.id.suggestionsIconLayout));
        mRelLayouts.add((RelativeLayout)view.findViewById(R.id.queueIconLayout));
        mRelLayouts.add((RelativeLayout)view.findViewById(R.id.categoriesIconLayout));

        mGreyIcons = new int[5];
        mGreyIcons[0] = R.drawable.ic_profile_grey_24dp;
        mGreyIcons[1] = R.drawable.ic_history_grey_24dp;
        mGreyIcons[2] = R.drawable.ic_home_grey_24dp;
        mGreyIcons[3] = R.drawable.ic_cart_grey_24dp;
        mGreyIcons[4] = R.drawable.ic_categories_grey_24dp;

        mWhiteIcons = new int[5];
        mWhiteIcons[0] = R.drawable.ic_profile_white_24dp;
        mWhiteIcons[1] = R.drawable.ic_history_white_24dp;
        mWhiteIcons[2] = R.drawable.ic_home_white_24dp;
        mWhiteIcons[3] = R.drawable.ic_cart_white_24dp;
        mWhiteIcons[4] = R.drawable.ic_categories_white_24dp;
    }
}
