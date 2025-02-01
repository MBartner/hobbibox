package hobbibox.hobbibox.pages.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.service.FirebaseService;

// Display and edit subscription preferences
public class SubscriptionsFragment extends Fragment {
    
    private FirebaseService mFirebaseService;
    private User mUser;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;
    private ImageView mLeftCornerImageView;
    private RelativeLayout mLeftCornerRelativeLayout;

    private ImageView mEditImageView;
    private Button mConfirmButton;

    private RadioGroup mRadioGroup;
    private RadioButton mWeeklyRadioButton;
    private RadioButton mBiWeeklyRadioButton;
    private RadioButton mMonthlyRadioButton;
    private RadioButton mBiMonthlyRadioButton;
    private RadioButton mQuarterlyRadioButton;
    private RadioButton mSemiAnnuallyRadioButton;

    public static SubscriptionsFragment newInstance() {
        return new SubscriptionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_subscription_settings, container, false);
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

        // Edit fields
        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioGroup.setEnabled(true);
                mWeeklyRadioButton.setEnabled(true);
                mBiWeeklyRadioButton.setEnabled(true);
                mMonthlyRadioButton.setEnabled(true);
                mBiMonthlyRadioButton.setEnabled(true);
                mQuarterlyRadioButton.setEnabled(true);
                mSemiAnnuallyRadioButton.setEnabled(true);
                mConfirmButton.setVisibility(View.VISIBLE);
            }
        });

        // Submit subscription data to database
        mConfirmButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mConfirmButton.setBackgroundColor(getResources().getColor(R.color.confirmButtonDark, null));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    switch (mRadioGroup.getCheckedRadioButtonId()){
                        case R.id.profile_subscription_settings_biweekly_radioButton:
                            mUser.setSubscriptionValue(1);
                            break;
                        case R.id.profile_subscription_settings_monthly_radioButton:
                            mUser.setSubscriptionValue(2);
                            break;
                        case R.id.profile_subscription_settings_bimonthly_radioButton:
                            mUser.setSubscriptionValue(3);
                            break;
                        case R.id.profile_subscription_settings_trimonthly_radioButton:
                            mUser.setSubscriptionValue(4);
                            break;
                        case R.id.profile_subscription_settings_halfyear_radioButton:
                            mUser.setSubscriptionValue(5);
                            break;
                        default:
                            mUser.setSubscriptionValue(0);
                            break;
                    }
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

        return view;
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view) {
        mTopBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.subscription_settings_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView)view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.profile_title));
        mLeftCornerImageView = (ImageView) view.findViewById(R.id.left_corner_icon);
        mLeftCornerImageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mLeftCornerImageView.setVisibility(View.VISIBLE);
        mLeftCornerRelativeLayout = (RelativeLayout) view.findViewById(R.id.left_corner_icon_rel_layout);

        mConfirmButton = (Button)view.findViewById(R.id.profile_sub_settings_change_confirm_button);
        mEditImageView = (ImageView)view.findViewById(R.id.profile_sub_settings_edit_image_view);
        mRadioGroup = (RadioGroup)view.findViewById(R.id.profile_sub_settings_radiogroup);

        mWeeklyRadioButton = (RadioButton)view.findViewById(R.id.profile_subscription_settings_weekly_radioButton);
        mBiWeeklyRadioButton = (RadioButton)view.findViewById(R.id.profile_subscription_settings_biweekly_radioButton);
        mMonthlyRadioButton = (RadioButton)view.findViewById(R.id.profile_subscription_settings_monthly_radioButton);
        mBiMonthlyRadioButton = (RadioButton)view.findViewById(R.id.profile_subscription_settings_bimonthly_radioButton);
        mQuarterlyRadioButton = (RadioButton)view.findViewById(R.id.profile_subscription_settings_trimonthly_radioButton);
        mSemiAnnuallyRadioButton = (RadioButton)view.findViewById(R.id.profile_subscription_settings_halfyear_radioButton);
    }

    // Update preexisting fields
    private void loadContent(){
        switch (mUser.getSubscriptionValue()) {
            case 1:
                mRadioGroup.check(R.id.profile_subscription_settings_biweekly_radioButton);
                break;
            case 2:
                mRadioGroup.check(R.id.profile_subscription_settings_monthly_radioButton);
                break;
            case 3:
                mRadioGroup.check(R.id.profile_subscription_settings_bimonthly_radioButton);
                break;
            case 4:
                mRadioGroup.check(R.id.profile_subscription_settings_trimonthly_radioButton);
                break;
            case 5:
                mRadioGroup.check(R.id.profile_subscription_settings_halfyear_radioButton);
                break;
            default:
                mRadioGroup.check(R.id.profile_subscription_settings_weekly_radioButton);
                break;
        }
    }
}
