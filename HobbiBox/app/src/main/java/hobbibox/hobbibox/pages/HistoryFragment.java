package hobbibox.hobbibox.pages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

// Show passed boxes the user has received
public class HistoryFragment extends android.support.v4.app.Fragment  {
    private final String TAG = getClass().getSimpleName();

    private FirebaseService mFirebaseService;
    private User mUser;

    private TextView mTopBarTextView;
    private RelativeLayout mTopBarRelativeLayout;

    private RecyclerView mHistoryRecyclerView;
    private HistoryAdapter mHistoryAdapter;
    private SwipeRefreshLayout mSwipeContainer;

    private List<Box> mBoxes;
    private List<String> mHistory;


    public static HistoryFragment newInstance() {
        HistoryFragment historyFragment = new HistoryFragment();
        return historyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
        mBoxes = mFirebaseService.getAllBoxes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        fetchLayout(inflater, view);

        // Create recycler view
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Refresh history on drag down
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFirebaseService.fetchHistory();
                updateUI();
                mSwipeContainer.setRefreshing(false);
            }
        });

        updateUI();

        return view;
    }

    // update recycler view
    public void updateUI() {
        //mFirebaseService.fetchHistory();
        mHistory = mUser.getHistory();
        List<Box> showboxes = stringToBox(mBoxes, mHistory);
        if (mHistoryAdapter == null) {
            mHistoryAdapter = new HistoryFragment.HistoryAdapter(showboxes);
            mHistoryRecyclerView.setAdapter(mHistoryAdapter);
        } else {
            mHistoryAdapter.setBoxes(showboxes);
            mHistoryAdapter.notifyDataSetChanged();
        }

        Log.d(TAG, "updateUI()");
    }


    // Holder for recycler view
    private class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView boxImageView;
        private TextView boxNameTextView;
        private TextView companyNameTextView;
        private TextView priceTextView;
        private TextView receivedDeliveryTextView;

        private Box box;

        public HistoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            boxImageView = (ImageView)itemView.findViewById(R.id.history_box_image_preview);
            boxNameTextView = (TextView)itemView.findViewById(R.id.history_item_box_name);
            companyNameTextView = (TextView)itemView.findViewById(R.id.history_item_box_company_name);
            priceTextView = (TextView)itemView.findViewById(R.id.history_item_box_price);
            receivedDeliveryTextView = (TextView)itemView.findViewById(R.id.history_date_delivered);
        }

        public void bindBox(final Box box) {
            this.box = box;

            boxNameTextView.setText(box.getBoxName());
            companyNameTextView.setText(box.getCompany());
            priceTextView.setText("$" + Integer.toString(box.getPrice()));
            try {
                byte[] decodedString = Base64.decode(box.getImageString(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                decodedByte = Bitmap.createScaledBitmap(decodedByte, 100, 100, true);
                boxImageView.setImageBitmap(decodedByte);
            }
            catch(Exception e){
                Log.d(TAG, e.toString());
            }
            receivedDeliveryTextView.setText("Today");

        }

        @Override
        public void onClick(View view) {
        }
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
        private List<Box> boxes;

        public HistoryAdapter(List<Box> boxes) {
            this.boxes = boxes;
        }

        public void setBoxes(List<Box> boxes) {
            this.boxes = boxes;
        }

        @Override
        public HistoryFragment.HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.history_item_template, parent, false);
            return new HistoryFragment.HistoryHolder(view);
        }

        @Override
        public void onBindViewHolder(HistoryFragment.HistoryHolder holder, int position) {
            Box box = boxes.get(position);
            holder.bindBox(box);
        }

        @Override
        public int getItemCount() {
            return boxes.size();
        }
    }

    // Fetches boxes that are on history list
    private List<Box> stringToBox(List<Box> boxes, List<String> strings){
        List<Box> historyBoxes = new ArrayList<Box>();
        for(String str : strings){
            if(!str.equals("Empty")) {
                for (Box box : boxes) {
                    if (box.getBoxID().equals(str)) {
                        historyBoxes.add(box);
                        break;
                    }
                }
            }
        }
        return historyBoxes;
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view){
        mTopBarRelativeLayout = (RelativeLayout)view.findViewById(R.id.history_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView)view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.history_title));
        mHistoryRecyclerView = (RecyclerView)view.findViewById(R.id.history_recycler_view);
        mSwipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainerhistory);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }
}
