package hobbibox.hobbibox.pages.profile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.service.FirebaseService;

// Allow users to get verified to make and distribute their own boxes
public class BoxWithUsFragment extends Fragment {
    private final int REQUEST_CODE_GET_VERIFIED = 1;
    private final int REQUEST_CODE_CREATE_BOX = 2;
    private FirebaseService mFirebaseService;
    private User mUser;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;
    private ImageView mLeftCornerImageView;
    private RelativeLayout mLeftCornerRelativeLayout;

    private Button mGetVerifiedButton;
    private Button mCreateBoxButton;

    public static BoxWithUsFragment newInstance() {
        return new BoxWithUsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_box_with_us, container, false);
        fetchLayout(inflater, view);
        loadContent();

        // Back button
        mLeftCornerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        // Launch VerifyActivity
        mGetVerifiedButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mGetVerifiedButton.setBackgroundColor(getResources().getColor(R.color.getVerifiedDark, null));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    startActivityForResult(new Intent(getActivity().getApplicationContext(), VerifyActivity.class), REQUEST_CODE_GET_VERIFIED);
                }
                else{
                    mGetVerifiedButton.setBackgroundColor(getResources().getColor(R.color.getVerified, null));
                }
                return false;
            }
        });

        // Luanch CreateBoxActivity
        mCreateBoxButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCreateBoxButton.setBackgroundColor(getResources().getColor(R.color.createBoxDark, null));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    startActivityForResult(new Intent(getActivity().getApplicationContext(), CreateBoxActivity.class), REQUEST_CODE_CREATE_BOX);
                }
                else{
                    mCreateBoxButton.setBackgroundColor(getResources().getColor(R.color.createBox, null));
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        // when verified, reload content to show create box options
        else if(requestCode == REQUEST_CODE_GET_VERIFIED){
            Toast.makeText(getActivity().getApplicationContext(), R.string.verification_confirmation, Toast.LENGTH_SHORT).show();
            loadContent();
        }
        else if(requestCode == REQUEST_CODE_CREATE_BOX){
            mCreateBoxButton.setBackgroundColor(getResources().getColor(R.color.createBox, null));
        }
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view) {
        mTopBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.box_with_us_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView) view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.profile_title));
        mLeftCornerImageView = (ImageView) view.findViewById(R.id.left_corner_icon);
        mLeftCornerImageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mLeftCornerImageView.setVisibility(View.VISIBLE);
        mLeftCornerRelativeLayout = (RelativeLayout) view.findViewById(R.id.left_corner_icon_rel_layout);

        mGetVerifiedButton = (Button)view.findViewById(R.id.profile_get_verified_button);
        mCreateBoxButton = (Button)view.findViewById(R.id.profile_create_box_button);
    }

    // Load user content
    private void loadContent(){
        mUser = mFirebaseService.getUser();
        if(mUser.getVerified() == 1){
            mGetVerifiedButton.setVisibility(View.GONE);
            mCreateBoxButton.setVisibility(View.VISIBLE);
        }
    }
}