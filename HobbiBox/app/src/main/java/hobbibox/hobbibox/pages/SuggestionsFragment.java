package hobbibox.hobbibox.pages;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.Box;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.service.FirebaseService;

// Show boxes corresponding to user's interests
public class SuggestionsFragment extends Fragment  {
    private final String TAG = getClass().getSimpleName();

    private FirebaseService mFirebaseService;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;

    private RecyclerView mBoxRecyclerView;
    private BoxAdapter mBoxAdapter;

    public static SuggestionsFragment newInstance() {
        SuggestionsFragment suggestionsFragment = new SuggestionsFragment();

        return suggestionsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestions, container, false);
        fetchLayout(inflater, view);

        // create recycler view
        mBoxRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        Log.d(TAG, "onCreateView()");
        return view;
    }

    // Update recycler view items
    public void updateUI() {
        List<Box> boxes;
        User user = mFirebaseService.getUser();

        // If user has interest, fetch boxes of interest
        if(user.getInterests().size() > 1){
            boxes = refineBoxes(user.getInterests());
        }
        else {
             boxes = mFirebaseService.getAllBoxes();
        }
        if (mBoxAdapter == null) {
            mBoxAdapter = new BoxAdapter(boxes);
            mBoxRecyclerView.setAdapter(mBoxAdapter);
        } else {
            mBoxAdapter.setBoxes(boxes);
            mBoxAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "updateUI()");
    }

    // Holder for recycler view
    private class BoxHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView boxNameTextView;
        private TextView companyNameTextView;
        private TextView priceTextView;
        private ImageView boxImageView;
        private Button addToQueueButton;
        private Button buyNowButton;

        private Box box;

        public BoxHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);

            boxNameTextView = (TextView)itemView.findViewById(R.id.list_item_box_name);
            companyNameTextView = (TextView)itemView.findViewById(R.id.list_item_company_name);
            priceTextView = (TextView)itemView.findViewById(R.id.list_item_box_price);
            boxImageView = (ImageView) itemView.findViewById(R.id.list_item_image);
            addToQueueButton = (Button)itemView.findViewById(R.id.suggestion_get_button);

            // Add box to end of queue
            addToQueueButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        addToQueueButton.setBackgroundColor(getResources().getColor(R.color.boxAddToQueueDark, null));
                    }
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        addToQueueButton.setBackgroundColor(getResources().getColor(R.color.boxAddToQueue, null));
                        User user = mFirebaseService.getUser();
                        user.addToQueue(box.getBoxID());
                        mFirebaseService.updateUser(user);
                    }
                    else {
                        addToQueueButton.setBackgroundColor(getResources().getColor(R.color.boxAddToQueue, null));
                    }
                    return false;
                }
            });

            buyNowButton = (Button)itemView.findViewById(R.id.suggestion_buy_now_button);

            // Add box to top of queue
            buyNowButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        buyNowButton.setBackgroundColor(getResources().getColor(R.color.boxBuyNowDark, null));
                    }
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        buyNowButton.setBackgroundColor(getResources().getColor(R.color.boxBuyNow, null));
                        User user = mFirebaseService.getUser();
                        user.addToQueue(1, box.getBoxID());
                        mFirebaseService.updateUser(user);
                    }
                    else{
                        buyNowButton.setBackgroundColor(getResources().getColor(R.color.boxBuyNow, null));
                    }
                    return false;
                }
            });
        }

        public void bindBox(Box box) {
            this.box = box;
            boxNameTextView.setText(box.getBoxName());
            companyNameTextView.setText(box.getCompany());
            priceTextView.setText("$" + String.valueOf(box.getPrice()));
            try {
                byte[] decodedString = Base64.decode(box.getImageString(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                decodedByte = Bitmap.createScaledBitmap(decodedByte, 100, 100, true);
                boxImageView.setImageBitmap(decodedByte);
            }
            catch(Exception e){
                Log.d(TAG, e.toString());
            }
        }

        // Display the boxes information if held
        @Override
        public boolean onLongClick(View view){
            TextView dialog_box_name;
            TextView dialog_description;

            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.box_description_dialog);
            dialog_box_name = (TextView) dialog.findViewById(R.id.dialog_box_name);
            dialog_description = (TextView) dialog.findViewById(R.id.dialog_description);
            dialog_box_name.setText(box.getBoxName());
            dialog_description.setText(box.getDescription());
            dialog.show();

            return false;
        }

    }

    // Adapter for recycler view
    private class BoxAdapter extends RecyclerView.Adapter<BoxHolder> {
        private List<Box> boxes;

        public BoxAdapter(List<Box> boxes) {
            this.boxes = boxes;
        }

        public void setBoxes(List<Box> boxes) {
            this.boxes = boxes;
        }

        @Override
        public BoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.box_template_2, parent, false);
            return new BoxHolder(view);
        }

        @Override
        public void onBindViewHolder(BoxHolder holder, int position) {
            Box box = boxes.get(position);
            holder.bindBox(box);
        }

        @Override
        public int getItemCount() {
            return boxes.size();
        }
    }

    // Fetch boxes of user interest
    private List<Box> refineBoxes(List<String> interests){
        List<Box> refinedBoxes = new ArrayList<Box>();
        for(Box box : mFirebaseService.getAllBoxes()){
            for(String str : interests){
                if(box.getCategory().equals(str)){
                    refinedBoxes.add(box);
                }
            }
        }
        return refinedBoxes;
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view){
        mTopBarRelativeLayout = (RelativeLayout)view.findViewById(R.id.suggestions_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView)view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.suggestions_title));

        mBoxRecyclerView = (RecyclerView)view.findViewById(R.id.suggestions_recycler_view);

    }

}
