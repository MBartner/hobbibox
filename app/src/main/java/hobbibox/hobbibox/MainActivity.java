package hobbibox.hobbibox;

import android.support.v4.app.Fragment;

import hobbibox.hobbibox.helper.SingleFragmentActivity;
import hobbibox.hobbibox.pages.SuggestionsFragment;

public class MainActivity extends SingleFragmentActivity {
    private MainFragment mMainFragment;

    @Override
    protected Fragment createFragment() {
        mMainFragment = MainFragment.newInstance();
        return mMainFragment;
    }

    // Return to last page view on viewpage when back is pressed
    @Override
    public void onBackPressed() {
       mMainFragment.backPressed();
    }
}