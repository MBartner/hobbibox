package hobbibox.hobbibox.pages.profile;

import android.support.v4.app.Fragment;

import hobbibox.hobbibox.helper.SingleFragmentActivity;

public class VerifyActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return VerifyFragment.newInstance();
    }

}
