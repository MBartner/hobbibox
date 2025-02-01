package hobbibox.hobbibox.pages.profile;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.service.FirebaseService;

// Allow user to be granted permission to create boxes
public class VerifyFragment extends Fragment {
    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;
    private ImageView mLeftCornerImageView;
    private RelativeLayout mLeftCornerRelativeLayout;

    private Button mApplyButton;
    private List<EditText> mRequiredEditTexts;
    private List<TextView> mRequiredTextViews;
    private CheckBox mAcceptTermsCheckBox;
    private Toast mTermsToast;
    private Toast mIncompleteToast;
    private ScrollView mVerifyScrollView;
    private Button mReviewTermsButton;
    private EditText mCompanyNameEditText;
    private LayoutInflater mInflater;
    private FirebaseService mFirebaseService;
    private User mUser;
    private DatabaseReference mVerifiedDbReference;

    public static VerifyFragment newInstance() {
        return new VerifyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify, container, false);
        mInflater = inflater;
        fetchLayout(inflater, view);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
        mVerifiedDbReference = mFirebaseService.getVerifiedDBReference();

        // Return to verify screen without sending application
        mLeftCornerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        // Updates text color of checkbox when checked
        mAcceptTermsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mAcceptTermsCheckBox.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.grey));
                }
            }
        });

        // Make sure all required fields are filled out and terms of service is accepted and then pushes to the database
        mApplyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mApplyButton.setBackgroundColor(getResources().getColor(R.color.applyButtonDark));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    mApplyButton.setBackgroundColor(getResources().getColor(R.color.applyButton));

                    boolean isCompleted = true;
                    for(EditText editText : mRequiredEditTexts) {
                        if (editText.getText().toString().length() == 0) {
                            isCompleted = false;
                            mRequiredTextViews.get(mRequiredEditTexts.indexOf(editText)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red));
                        } else {
                            mRequiredTextViews.get(mRequiredEditTexts.indexOf(editText)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.grey));
                        }
                    }
                    if(isCompleted) {
                        if (mAcceptTermsCheckBox.isChecked()) {
                            Map<String, String> verificationApplication = new HashMap<String, String>();
                            verificationApplication.put("Full Name", mRequiredEditTexts.get(0).getText().toString());
                            verificationApplication.put("DOB month", mRequiredEditTexts.get(1).getText().toString());
                            verificationApplication.put("DOB Year", mRequiredEditTexts.get(2).getText().toString());
                            verificationApplication.put("DOB Day", mRequiredEditTexts.get(3).getText().toString());
                            verificationApplication.put("Address", mRequiredEditTexts.get(4).getText().toString());
                            verificationApplication.put("City", mRequiredEditTexts.get(5).getText().toString());
                            verificationApplication.put("State", mRequiredEditTexts.get(6).getText().toString());
                            verificationApplication.put("Zipcode", mRequiredEditTexts.get(7).getText().toString());
                            verificationApplication.put("SSN", mRequiredEditTexts.get(8).getText().toString() +
                                    mRequiredEditTexts.get(9).toString() +
                                    mRequiredEditTexts.get(10).toString());
                            verificationApplication.put("Company", mCompanyNameEditText.getText().toString());
                            mVerifiedDbReference.push().setValue(verificationApplication);

                            mUser.setVerified(1);
                            mFirebaseService.updateUser(mUser);

                            // In real life, verification would be manual but for the sake of the demo
                            // it happens automatically.
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        }
                        else{
                            mAcceptTermsCheckBox.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red));
                            mTermsToast.show();
                            mVerifyScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mVerifyScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }
                    }
                    else{
                        mIncompleteToast.show();
                    }
                }
                return false;
            }
        });

        // Show terms and service
        mReviewTermsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.terms_of_service);
                dialog.show();
            }
        });


        return view;
    }

    // Load layout items into member fields and inflate top bar
    private void fetchLayout(LayoutInflater inflater, View view){
        mTopBarRelativeLayout = (RelativeLayout)view.findViewById(R.id.fragment_verify_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView) view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.verify_title));
        mLeftCornerImageView= (ImageView)view.findViewById(R.id.left_corner_icon);
        mLeftCornerImageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mLeftCornerImageView.setVisibility(View.VISIBLE);
        mLeftCornerRelativeLayout = (RelativeLayout)view.findViewById(R.id.left_corner_icon_rel_layout);
        mApplyButton = (Button)view.findViewById(R.id.apply_button);
        mReviewTermsButton = (Button)view.findViewById(R.id.verify_review_terms_button);
        mCompanyNameEditText = (EditText)view.findViewById(R.id.verify_company_edit_text);

        mRequiredEditTexts = new ArrayList<EditText>();
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_name_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_month_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_day_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_year_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_addr_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_city_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_state_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_zip_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_ssn_part1_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_ssn_part2_edit_text));
        mRequiredEditTexts.add((EditText)view.findViewById(R.id.verify_ssn_part3_edit_text));

        mRequiredTextViews = new ArrayList<TextView>();
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_name_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_dob_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_dob_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_dob_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_addr_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_city_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_state_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_zip_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_ssn_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_ssn_text_view));
        mRequiredTextViews.add((TextView)view.findViewById(R.id.verify_ssn_text_view));

        mVerifyScrollView = (ScrollView)view.findViewById(R.id.verify_scroll_view);
        mAcceptTermsCheckBox = (CheckBox)view.findViewById(R.id.verify_accept_terms_check_box);
        mTermsToast = Toast.makeText(getActivity(), R.string.verify_error_terms, Toast.LENGTH_SHORT);
        mIncompleteToast = Toast.makeText(getActivity(), R.string.verify_error_incomplete, Toast.LENGTH_SHORT);
    }


}
