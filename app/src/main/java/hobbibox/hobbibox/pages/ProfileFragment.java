package hobbibox.hobbibox.pages;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.pages.profile.BillingActivity;
import hobbibox.hobbibox.pages.profile.BoxWithUsActivity;
import hobbibox.hobbibox.pages.profile.PaymentActivity;
import hobbibox.hobbibox.pages.profile.PhoneNumberActivity;
import hobbibox.hobbibox.pages.profile.ShippingActivity;
import hobbibox.hobbibox.pages.profile.SubscriptionsActivity;
import hobbibox.hobbibox.service.FirebaseService;

// Screen to allow user to edit parts of their profile
public class ProfileFragment extends android.support.v4.app.Fragment  {
    private final String TAG = getClass().getSimpleName();
    private final int REQUEST_CODE_PHONE_NUMBER = 1;
    private final int REQUEST_CODE_SUB_SETTINGS = 2;
    private final int REQUEST_CODE_SHIPPING_INFO = 3;
    private final int REQUEST_CODE_BILLING_INFO = 4;
    private final int REQUEST_CODE_PAYMENT_INFO = 5;
    private final int REQUEST_CODE_BOX_WITH_US = 6;

    private FirebaseService mFirebaseService;
    private User mUser;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;
    private ImageView mProfileRightCornerImageView;
    private RelativeLayout mProfileRightCornerRelativeLayout;
    private ImageView mProfileLeftCornerImageView;
    private RelativeLayout mProfileLeftCornerRelativeLayout;

    private Button mProfileSignOutButton;

    private RelativeLayout mPhoneNumberRelativeLayout;
    private RelativeLayout mSubscriptionSettingsRelativeLayout;
    private RelativeLayout mShippingInformationRelativeLayout;
    private RelativeLayout mBillingInformationRelativeLayout;
    private RelativeLayout mPaymentInformationRelativeLayout;
    private RelativeLayout mBoxWithUsRelativeLayout;

    private TextView mUserNameTextView;
    private TextView mPhoneNumberTextView;
    private TextView mSubscriptionTextView;
    private TextView mShippingTextView;
    private TextView mBillingTextView;
    private TextView mPaymentInfoTextView;
    private TextView mBoxWithUsTextView;

    private LinearLayout mAboutPageLinearLayout;
    private RelativeLayout mProfileNavRelativeLayout;

    Bundle mBndlanimation;

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
        mBndlanimation = ActivityOptions.makeCustomAnimation(getActivity().getApplicationContext(), R.anim.activity_start_new, R.anim.activity_start_old).toBundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fetchLayout(inflater, view);

        // Log out of user and return to login screen
        mProfileSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseService.signOut();
                getActivity().finish();
            }
        });

        // Edit phone number
        mPhoneNumberRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), PhoneNumberActivity.class), REQUEST_CODE_PHONE_NUMBER, mBndlanimation);
            }
        });

        // Edit subscription preferences
        mSubscriptionSettingsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), SubscriptionsActivity.class), REQUEST_CODE_SUB_SETTINGS, mBndlanimation);
            }
        });

        // Edit shipping info
        mShippingInformationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), ShippingActivity.class), REQUEST_CODE_SHIPPING_INFO, mBndlanimation);
            }
        });

        // Edit billing info
        mBillingInformationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), BillingActivity.class), REQUEST_CODE_BILLING_INFO, mBndlanimation);
            }
        });

        // Edit payment info
        mPaymentInformationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), PaymentActivity.class), REQUEST_CODE_PAYMENT_INFO, mBndlanimation);
            }
        });

        // View verification status / create box
        mBoxWithUsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), BoxWithUsActivity.class), REQUEST_CODE_BOX_WITH_US, mBndlanimation);
            }
        });

        // Show about page for hobbibox
        mProfileRightCornerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileNavRelativeLayout.setVisibility(View.INVISIBLE);
                mAboutPageLinearLayout.setVisibility(View.VISIBLE);
                mProfileLeftCornerImageView.setVisibility(View.VISIBLE);
                mProfileRightCornerRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        // Hide about page for hobbibox
        mProfileLeftCornerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileNavRelativeLayout.setVisibility(View.VISIBLE);
                mAboutPageLinearLayout.setVisibility(View.INVISIBLE);
                mProfileLeftCornerImageView.setVisibility(View.INVISIBLE);
                mProfileRightCornerRelativeLayout.setVisibility(View.VISIBLE);
            }
        });

        updateUI();

        return view;
    }

    // Update information hints
    private void updateUI(){
        mUserNameTextView.setText(mUser.getEmail());
        mPhoneNumberTextView.setText(mUser.getPhoneNumber());
        String subscriptionPreview;
        switch (mUser.getSubscriptionValue()) {
            case 0:
                subscriptionPreview = getResources().getString(R.string.weekly);
                break;
            case 1:
                subscriptionPreview = getResources().getString(R.string.biweekly);
                break;
            case 2:
                subscriptionPreview = getResources().getString(R.string.monthly);
                break;
            case 3:
                subscriptionPreview = getResources().getString(R.string.bimonthly);
                break;
            case 4:
                subscriptionPreview = getResources().getString(R.string.quarterly);
                break;
            case 5:
                subscriptionPreview = getResources().getString(R.string.semiAnnually);
                break;
            default:
                subscriptionPreview = getResources().getString(R.string.weekly);
        }
        mSubscriptionTextView.setText(subscriptionPreview);
        mShippingTextView.setText(mUser.getShip_add1());
        mBillingTextView.setText(mUser.getBill_add1());
        mPaymentInfoTextView.setText(mUser.getCardCompany());
        if(mUser.getVerified() == 1){
            mBoxWithUsTextView.setText(R.string.business_verified);
        }
        else{
            mBoxWithUsTextView.setText(R.string.profile_get_verified);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) { // Event that user doesn't sign in
            return;
        }

        // Refetch user and update possible changes to hints
        mUser = mFirebaseService.getUser();
        updateUI();
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view) {
        mTopBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.profile_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView) view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.profile_title));
        mProfileRightCornerImageView = (ImageView) view.findViewById(R.id.right_corner_icon);
        mProfileRightCornerImageView.setImageResource(R.drawable.ic_help_white_24dp);
        mProfileRightCornerRelativeLayout = (RelativeLayout) view.findViewById(R.id.right_corner_icon_rel_layout);
        mProfileLeftCornerRelativeLayout = (RelativeLayout)view.findViewById(R.id.left_corner_icon_rel_layout);
        mProfileLeftCornerImageView = (ImageView)view.findViewById(R.id.left_corner_icon);
        mProfileNavRelativeLayout = (RelativeLayout)view.findViewById(R.id.profile_navigation_view_rel_layout);

        mProfileSignOutButton = (Button)view.findViewById(R.id.profile_sign_out_button);

        mPhoneNumberRelativeLayout = (RelativeLayout)view.findViewById(R.id.profile_phone_number_rel_layout);
        mSubscriptionSettingsRelativeLayout = (RelativeLayout)view.findViewById(R.id.profile_subscriptions_settings_rel_layout);
        mShippingInformationRelativeLayout = (RelativeLayout)view.findViewById(R.id.profile_shipping_info_rel_layout);
        mBillingInformationRelativeLayout = (RelativeLayout)view.findViewById(R.id.profile_billing_info_rel_layout);
        mPaymentInformationRelativeLayout = (RelativeLayout)view.findViewById(R.id.profile_payment_info_rel_layout);
        mBoxWithUsRelativeLayout = (RelativeLayout)view.findViewById(R.id.profile_business_info_rel_layout);

        mUserNameTextView = (TextView)view.findViewById(R.id.profile_username_preview_textview); 
        mPhoneNumberTextView = (TextView)view.findViewById(R.id.profile_phone_number_preview_textview);
        mSubscriptionTextView = (TextView)view.findViewById(R.id.profile_sub_settings_choice_preview_textview);
        mShippingTextView = (TextView)view.findViewById(R.id.shipping_address_preview_text_view);
        mBillingTextView = (TextView)view.findViewById(R.id.billing_address_preview_text_view);
        mPaymentInfoTextView = (TextView)view.findViewById(R.id.payment_info_card_type_preview_textview);
        mBoxWithUsTextView = (TextView)view.findViewById(R.id.business_verified_preview_textview);

        mAboutPageLinearLayout = (LinearLayout)view.findViewById(R.id.about_page_lin_layout);
    }
}