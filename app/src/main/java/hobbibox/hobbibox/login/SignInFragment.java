package hobbibox.hobbibox.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import hobbibox.hobbibox.R;
import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.service.FirebaseService;

// Allows users to sign in
public class SignInFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private final int MIN_LENGTH = 0;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private Button mSubmitSignInButton;
    private Button mSigninCancel;
    private FirebaseService mFirebaseService;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mUserNameEditText = (EditText)view.findViewById(R.id.user_name_edit_text);
        mPasswordEditText = (EditText)view.findViewById(R.id.password_edit_text);
        mSubmitSignInButton = (Button)view.findViewById(R.id.submit_signin_button);
        mSigninCancel = (Button)view.findViewById(R.id.signin_cancel);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());

        // Tests if user account is valid
        mSubmitSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mUserNameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if(email != null && password != null && email.length() > MIN_LENGTH && password.length() > MIN_LENGTH){
                    mFirebaseService.getmAuth().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    if (task.isSuccessful()) {
                                        getActivity().setResult(Activity.RESULT_OK);
                                        getActivity().finish();
                                    }
                                    else{
                                        Toast.makeText(getActivity(), R.string.invalid_signin, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getActivity(), R.string.error_null, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Return users to main login screen
        mSigninCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;
    }
}
