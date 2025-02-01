package hobbibox.hobbibox.pages.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.service.FirebaseService;

// Displays payment information and allows user to edit
public class PaymentFragment  extends Fragment {
    private FirebaseService mFirebaseService;
    private User mUser;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;
    private ImageView mLeftCornerImageView;
    private RelativeLayout mLeftCornerRelativeLayout;

    private ImageView mEditImageView;
    private Button mConfirmButton;
    
    private RadioGroup mRadioGroup;
    private RadioButton mCreditRadioButton;
    private RadioButton mDebitRadioButton;
    private EditText mCardCompanyEditText;
    private EditText mNameOnCardEditText;
    private EditText mCardNumberEditText;
    private EditText mSecurityCodeEditText;
    private Spinner mExpirationMonthSpinner;
    private Spinner mExpirationYearSpinner;

    private ArrayAdapter<CharSequence> mExpirationMonthAdapter;
    private ArrayAdapter<CharSequence> mExpirationYearAdapter;
    
    public static PaymentFragment newInstance() {
        return new PaymentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
        // Spinner adapters
        mExpirationMonthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.expiration_month_array, android.R.layout.simple_spinner_item);
        mExpirationMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExpirationYearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.expiration_year_array, android.R.layout.simple_spinner_item);
        mExpirationMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_payment_info, container, false);
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

        // Submit changes to database
        mConfirmButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mConfirmButton.setBackgroundColor(getResources().getColor(R.color.confirmButtonDark, null));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    switch(mRadioGroup.getCheckedRadioButtonId()){
                        case R.id.profile_payment_debit_card_button:
                            mUser.setCardType(1);
                            break;
                        default:
                            mUser.setCardType(0);
                    }
                    mUser.setCardCompany(mCardCompanyEditText.getText().toString());
                    mUser.setNameOnCard(mNameOnCardEditText.getText().toString());
                    mUser.setCardNumber(mCardNumberEditText.getText().toString());
                    mUser.setSecurityCode(mSecurityCodeEditText.getText().toString());
                    mUser.setExpirationDateMonth(Integer.parseInt((String)mExpirationMonthSpinner.getSelectedItem()));
                    mUser.setExpirationDateYear(Integer.parseInt((String)mExpirationYearSpinner.getSelectedItem()));
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

        // Allow user to edit fields and submit them to the database
        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioGroup.setEnabled(true);
                mCreditRadioButton.setEnabled(true);
                mDebitRadioButton.setEnabled(true);
                mCardCompanyEditText.setEnabled(true);
                mNameOnCardEditText.setEnabled(true);
                mCardNumberEditText.setEnabled(true);
                mSecurityCodeEditText.setEnabled(true);
                mExpirationMonthSpinner.setAdapter(mExpirationMonthAdapter);
                mExpirationYearSpinner.setAdapter(mExpirationYearAdapter);
                mConfirmButton.setVisibility(View.VISIBLE);
                if(mUser.getExpirationDateMonth() == 0 || mUser.getExpirationDateYear() == 0) {
                    mExpirationMonthSpinner.setSelection(0);
                    mExpirationYearSpinner.setSelection(0);
                }
                else{
                    mExpirationMonthSpinner.setSelection(mExpirationMonthAdapter.getPosition(Integer.toString(mUser.getExpirationDateMonth())));
                    mExpirationYearSpinner.setSelection(mExpirationYearAdapter.getPosition(Integer.toString(mUser.getExpirationDateYear())));
                }
            }
        });

        return view;
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view) {
        mTopBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.payment_info_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView) view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.profile_title));
        mLeftCornerImageView = (ImageView) view.findViewById(R.id.left_corner_icon);
        mLeftCornerImageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mLeftCornerImageView.setVisibility(View.VISIBLE);
        mLeftCornerRelativeLayout = (RelativeLayout) view.findViewById(R.id.left_corner_icon_rel_layout);

        mEditImageView = (ImageView) view.findViewById(R.id.profile_payment_info_edit_image_view);
        mConfirmButton = (Button) view.findViewById(R.id.profile_payment_info_change_confirm_button);

        mRadioGroup = (RadioGroup)view.findViewById(R.id.profile_payment_card_type_radiogroup);
        mCreditRadioButton = (RadioButton)view.findViewById(R.id.profile_payment_credit_card_button);
        mDebitRadioButton = (RadioButton)view.findViewById(R.id.profile_payment_debit_card_button);
        mCardCompanyEditText = (EditText)view.findViewById(R.id.profile_payment_card_company_edit_text);
        mNameOnCardEditText = (EditText)view.findViewById(R.id.profile_payment_nameOnCard_edit_text);
        mCardNumberEditText = (EditText)view.findViewById(R.id.profile_payment_cardnumber_edit_text); 
        mSecurityCodeEditText = (EditText)view.findViewById(R.id.profile_payment_security_code_edit_text);
        mExpirationMonthSpinner = (Spinner)view.findViewById(R.id.profile_payment_expiration_date_month_spinner);
        mExpirationYearSpinner = (Spinner)view.findViewById(R.id.profile_payment_expiration_date_year_spinner);
    }

    // Load user data into fields
    private void loadContent(){
        switch(mUser.getCardType()){
            case 1:
                mDebitRadioButton.setChecked(true);
                break;
            default:
                mCreditRadioButton.setChecked(true);
        }
        mCardCompanyEditText.setText(mUser.getCardCompany());
        mNameOnCardEditText.setText(mUser.getNameOnCard());
        mCardNumberEditText.setText(mUser.getCardNumber());
        mSecurityCodeEditText.setText(mUser.getSecurityCode());
    }
}
