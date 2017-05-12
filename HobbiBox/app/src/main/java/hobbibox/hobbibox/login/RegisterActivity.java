package hobbibox.hobbibox.login;

import android.support.v4.app.Fragment;

import hobbibox.hobbibox.helper.SingleFragmentActivity;

public class RegisterActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return RegisterFragment.newInstance();
    }
}