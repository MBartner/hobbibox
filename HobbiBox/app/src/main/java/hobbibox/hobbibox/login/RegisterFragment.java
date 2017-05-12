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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hobbibox.hobbibox.R;
import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.service.FirebaseService;


public class RegisterFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private final int MIN_LENGTH = 0;
    private final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final Pattern passPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);
    private EditText mNewUserNameEditText;
    private EditText mNewPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mSubmitRegisterButton;
    private Button mRegisterCancel;
    private FirebaseService mFirebaseService;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mNewUserNameEditText = (EditText)view.findViewById(R.id.new_user_name_edit_text);
        mNewPasswordEditText = (EditText)view.findViewById(R.id.new_password_edit_text);
        mConfirmPasswordEditText = (EditText) view.findViewById(R.id.confirm_password_edit_text);
        mSubmitRegisterButton = (Button)view.findViewById(R.id.submit_register_button);
        mRegisterCancel = (Button)view.findViewById(R.id.register_cancel);

        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());

        mSubmitRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for valid email and password combo and create user
                String email = mNewUserNameEditText.getText().toString();
                String password = mNewPasswordEditText.getText().toString();
                String confirmPass = mConfirmPasswordEditText.getText().toString();
                if(email != null && password != null && confirmPass != null && email.length() > MIN_LENGTH && password.length() > MIN_LENGTH && confirmPass.length() > MIN_LENGTH) {
                    if (mConfirmPasswordEditText.getText().toString().equals(mNewPasswordEditText.getText().toString())) {
                        int valid = isValidLogin(email, password);
                        if(valid == 0) {
                            mFirebaseService.getmAuth().createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                            if (task.isSuccessful()) {
                                                getActivity().setResult(Activity.RESULT_OK);
                                                getActivity().finish();
                                            } else {
                                                Toast.makeText(getActivity(), R.string.invalid_register, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else if(valid == 1)
                            Toast.makeText(getActivity(), R.string.error_email, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), R.string.error_password, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.diff_passwords, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), R.string.error_null, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegisterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;
    }

    // Uses regex to check if valid email and password
    private int isValidLogin(String email, String pass) {
        Matcher emailMatcher = emailPattern.matcher(email);
        Matcher passMatcher = passPattern.matcher(pass);
        if(!emailMatcher.matches()){
            return 1;
        }
        if(!passMatcher.matches()){
            return 2;
        }
        return 0;
    }
    /* Password Rules
        One number
        Lowercase Letter
        Uppercase Letter
        No whitespace
        At least 8 characters
    */

}
