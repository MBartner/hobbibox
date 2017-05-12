package hobbibox.hobbibox.pages.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.service.FirebaseService;

// Display and update user phone information
public class PhoneNumberFragment extends Fragment {
    private FirebaseService mFirebaseService;
    private User mUser;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;
    private ImageView mLeftCornerImageView;
    private RelativeLayout mLeftCornerRelativeLayout;
    private ImageView mEditImageView;
    private EditText mPhoneNumberEditText;
    private Button mConfirmButton;

    public static PhoneNumberFragment newInstance() {
        return new PhoneNumberFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_phone_number, container, false);
        fetchLayout(inflater, view);
        loadContent();

        // Edit fields
        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumberEditText.setEnabled(true);
                mConfirmButton.setVisibility(View.VISIBLE);
            }
        });

        // Submit phone number to database
        mConfirmButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mConfirmButton.setBackgroundColor(getResources().getColor(R.color.confirmButtonDark, null));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    if(mPhoneNumberEditText.getText().toString().length() > 0) {
                        mUser.setPhoneNumber(mPhoneNumberEditText.getText().toString());
                        mFirebaseService.updateUser(mUser);
                    }
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                else{
                    mConfirmButton.setBackgroundColor(getResources().getColor(R.color.confirmButton, null));
                }
                return false;
            }
        });

        // back button
        mLeftCornerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view) {
        mTopBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.phone_number_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView) view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.profile_title));
        mLeftCornerImageView = (ImageView) view.findViewById(R.id.left_corner_icon);
        mLeftCornerImageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mLeftCornerImageView.setVisibility(View.VISIBLE);
        mLeftCornerRelativeLayout = (RelativeLayout) view.findViewById(R.id.left_corner_icon_rel_layout);
        mEditImageView = (ImageView)view.findViewById(R.id.profile_phone_number_edit_image_view);
        mPhoneNumberEditText = (EditText)view.findViewById(R.id.profile_phone_number_edit_text);
        mConfirmButton = (Button)view.findViewById(R.id.profile_phone_number_change_confirm_button);
    }

    // Load user content
    private void loadContent(){
        mPhoneNumberEditText.setText(mUser.getPhoneNumber());
    }

}
