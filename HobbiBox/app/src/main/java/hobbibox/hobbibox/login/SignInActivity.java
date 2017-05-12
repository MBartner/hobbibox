package hobbibox.hobbibox.login;

import android.support.v4.app.Fragment;

import hobbibox.hobbibox.helper.SingleFragmentActivity;

/**
 * Created by 0x10 on 5/3/17.
 */

public class SignInActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SignInFragment.newInstance();
    }
}
