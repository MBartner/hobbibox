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

// Displays billing information and allows user to edit
public class BillingFragment extends Fragment{
    private FirebaseService mFirebaseService;
    private User mUser;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;
    private ImageView mLeftCornerImageView;
    private RelativeLayout mLeftCornerRelativeLayout;

    private ImageView mEditImageView;
    private Button mConfirmButton;

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mAddressLineOneEditText;
    private EditText mAddressLineTwoEditText;
    private EditText mCityEditText;
    private EditText mStateEditText;
    private EditText mCountryEditText;
    private EditText mZipCodeEditText;
    
    public static BillingFragment newInstance() {
        return new BillingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_billing_address, container, false);
        fetchLayout(inflater, view);
        loadContent();

        // Back button
        mLeftCornerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        // Submit changes in billing address to database
        mConfirmButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mConfirmButton.setBackgroundColor(getResources().getColor(R.color.confirmButtonDark, null));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    mUser.setBill_fname(mFirstNameEditText.getText().toString());
                    mUser.setBill_lname(mLastNameEditText.getText().toString());
                    mUser.setBill_add1(mAddressLineOneEditText.getText().toString());
                    mUser.setBill_add2(mAddressLineTwoEditText.getText().toString());
                    mUser.setBill_city(mCityEditText.getText().toString());
                    mUser.setBill_state(mStateEditText.getText().toString());
                    mUser.setBill_country(mCountryEditText.getText().toString());
                    mUser.setBill_zipcode(mZipCodeEditText.getText().toString());
                    mFirebaseService.updateUser(mUser);
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                else{
                    mConfirmButton.setBackgroundColor(getResources().getColor(R.color.confirmButton, null));
                }
                return false;
            }
        });

        // Allow changes to be made to billing info
        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstNameEditText.setEnabled(true);
                mLastNameEditText.setEnabled(true);
                mAddressLineOneEditText.setEnabled(true);
                mAddressLineTwoEditText.setEnabled(true);
                mCityEditText.setEnabled(true);
                mStateEditText.setEnabled(true);
                mCountryEditText.setEnabled(true);
                mZipCodeEditText.setEnabled(true);
                mConfirmButton.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    // Feches layout elements
    private void fetchLayout(LayoutInflater inflater, View view) {
        mTopBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.billing_address_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView) view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.profile_title));
        mLeftCornerImageView = (ImageView) view.findViewById(R.id.left_corner_icon);
        mLeftCornerImageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mLeftCornerImageView.setVisibility(View.VISIBLE);
        mLeftCornerRelativeLayout = (RelativeLayout) view.findViewById(R.id.left_corner_icon_rel_layout);

        mEditImageView = (ImageView)view.findViewById(R.id.profile_billing_info_edit_image_view);
        mConfirmButton = (Button)view.findViewById(R.id.profile_billing_change_confirm_button);

        mFirstNameEditText = (EditText)view.findViewById(R.id.profile_billing_first_name_edit_text);
        mLastNameEditText = (EditText)view.findViewById(R.id.profile_billing_last_name_edit_text);
        mAddressLineOneEditText = (EditText)view.findViewById(R.id.profile_billing_street_address1_edit_text);
        mAddressLineTwoEditText = (EditText)view.findViewById(R.id.profile_billing_street_address2_edit_text);
        mCityEditText = (EditText)view.findViewById(R.id.profile_billing_city_edit_text);
        mStateEditText = (EditText)view.findViewById(R.id.profile_billing_state_edit_text);
        mCountryEditText = (EditText)view.findViewById(R.id.profile_billing_country_edit_text);
        mZipCodeEditText = (EditText)view.findViewById(R.id.profile_billing_zipcode_edit_text);
    }

    // Loads preset user content
    private void loadContent(){
        mFirstNameEditText.setText(mUser.getBill_fname());
        mLastNameEditText.setText(mUser.getBill_lname());
        mAddressLineOneEditText.setText(mUser.getBill_add1());
        mAddressLineTwoEditText.setText(mUser.getBill_add2());
        mCityEditText.setText(mUser.getBill_city());
        mStateEditText.setText(mUser.getBill_state());
        mCountryEditText.setText(mUser.getBill_country());
        mZipCodeEditText.setText(mUser.getBill_zipcode());
    }
}
