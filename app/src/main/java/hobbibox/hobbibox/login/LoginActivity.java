package hobbibox.hobbibox.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import hobbibox.hobbibox.MainActivity;
import hobbibox.hobbibox.helper.OnVariableChangedListener;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.helper.Variable;
import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.service.FirebaseService;

// Main Login screen: allows users to select sign in or register
public class LoginActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private final int REQUEST_CODE_MAIN = 1;
    private final int REQUEST_LOGIN = 2;
    private final int REQUEST_REGISTER = 3;

    private FirebaseService mFirebaseService;
    private Button mSignInButton;
    private Button mRegisterButton;
    private Variable mVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        mVariable = DependencyFactory.getUserLoaded();
        mVariable.setOnVariableChangedListener(new OnVariableChangedListener() {

            // Called when the user is signed in and the resources have been loaded
            @Override
            public void onVariableChanged() {
                Log.d(TAG, "User received from database.");
                startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), REQUEST_CODE_MAIN);
            }

            // Called when the user is not signed in
            @Override
            public void noActivity(){
                loadLogin();
            }
        });
        mFirebaseService = DependencyFactory.getFirebaseService(getApplicationContext());

        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseService.forOnStartLogin();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseService.forOnStopLogin();
    }

    @Override
    public void onBackPressed() {
        // Disable onBackPressed to keep users from proceeding without signing in
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Load login screen
        if(requestCode == REQUEST_CODE_MAIN){
            loadLogin();
            return;
        }
        if (resultCode != RESULT_OK) { // Event that user doesn't sign in
            Log.d(TAG, "Register/Signin cancelled.");
            return;
        }
        setContentView(R.layout.welcome);
        Log.d(TAG, "Register/SignIn confirmed");
    }

    // Load sign in and register screen
    private void loadLogin(){
        setContentView(R.layout.activity_login);
        mSignInButton = (Button)findViewById(R.id.signin_button);
        mRegisterButton = (Button)findViewById(R.id.register_button);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_REGISTER);
            }
        });
    }
}
