package hobbibox.hobbibox.pages.profile;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.Box;
import hobbibox.hobbibox.service.FirebaseService;

// Allows verified users to create boxes
public class CreateBoxFragment extends Fragment{
    private final String TAG = getClass().getSimpleName();
    private final int REQUEST_CODE_PICK_IMAGE = 1;

    private FirebaseService mFirebaseService;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;
    private ImageView mLeftCornerImageView;
    private RelativeLayout mLeftCornerRelativeLayout;

    private Button mCreateBoxButton;
    
    private EditText mBoxNameEditText;
    private EditText mCompanyNameEditText;
    private EditText mPriceEditText;
    private EditText mDescriptionEditText;
    private Spinner mCategorySpinner;
    private ImageButton mTakePictureImageButton;
    private ImageView mBoxImageView;


    public static CreateBoxFragment newInstance() {
        return new CreateBoxFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_box, container, false);
        fetchLayout(inflater, view);

        // Spinner set up
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.box_category_array, R.layout.spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categoryAdapter);
        mCategorySpinner.setSelection(0);

        // Back button
        mLeftCornerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        // Submit box to database
        mCreateBoxButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCreateBoxButton.setBackgroundColor(getResources().getColor(R.color.createBoxDark, null));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    Box box = new Box();
                    if(mBoxNameEditText.getText().toString().length() > 0 && mCompanyNameEditText.getText().toString().length() > 0 &&
                            mPriceEditText.getText().toString().length() > 0 && mDescriptionEditText.getText().toString().length() > 0 ){
                        box.setBoxName(mBoxNameEditText.getText().toString());
                        box.setCompany(mCompanyNameEditText.getText().toString());
                        box.setPrice(Integer.parseInt(mPriceEditText.getText().toString()));
                        box.setDescription(mDescriptionEditText.getText().toString());
                        box.setCategory(mCategorySpinner.getSelectedItem().toString());
                        box.setImageString(encodeImage());
                        mFirebaseService.addBox(box);
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), R.string.error_fill, Toast.LENGTH_SHORT).show();
                        mCreateBoxButton.setBackgroundColor(getResources().getColor(R.color.createBox, null));
                    }
                }
                else{
                    mCreateBoxButton.setBackgroundColor(getResources().getColor(R.color.createBox, null));
                }
                return false;
            }
        });

        mTakePictureImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent imagePickIntent = new Intent();
                imagePickIntent.setType("image/*");
                imagePickIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(imagePickIntent, "Select Picture"), REQUEST_CODE_PICK_IMAGE);
            }
        });
        

        Log.d(TAG, "onCreateView()");
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Receive image store in bitmap
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                Image targetImage;
                mBoxImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Convert image to string
    private String encodeImage() {
        BitmapDrawable drawable = (BitmapDrawable) mBoxImageView.getDrawable();
        if(drawable != null) {
            Bitmap bm = drawable.getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encodedImage;
        }
        return "";
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view) {
        mTopBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_create_box_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView) view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.create_box));
        mLeftCornerImageView = (ImageView) view.findViewById(R.id.left_corner_icon);
        mLeftCornerImageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mLeftCornerImageView.setVisibility(View.VISIBLE);
        mLeftCornerRelativeLayout = (RelativeLayout) view.findViewById(R.id.left_corner_icon_rel_layout);

        mCreateBoxButton = (Button)view.findViewById(R.id.create_box_button);
        mTakePictureImageButton = (ImageButton)view.findViewById(R.id.camera);

        mBoxNameEditText = (EditText) view.findViewById(R.id.create_box_name);
        mCompanyNameEditText = (EditText) view.findViewById(R.id.create_company_name);
        mPriceEditText = (EditText) view.findViewById(R.id.create_price);
        mDescriptionEditText = (EditText) view.findViewById(R.id.create_description);
        mCategorySpinner = (Spinner) view.findViewById(R.id.create_category_spinner);
        mTakePictureImageButton = (ImageButton) view.findViewById(R.id.camera);
        mBoxImageView = (ImageView) view.findViewById(R.id.photo);
    }
}
