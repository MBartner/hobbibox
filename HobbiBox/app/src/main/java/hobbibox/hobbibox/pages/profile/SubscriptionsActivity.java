package hobbibox.hobbibox.pages.profile;

import android.support.v4.app.Fragment;

import hobbibox.hobbibox.R;
import hobbibox.hobbibox.helper.SingleFragmentActivity;

public class SubscriptionsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SubscriptionsFragment.newInstance();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.activity_finish_old, R.anim.activity_finish_new);
    }
}