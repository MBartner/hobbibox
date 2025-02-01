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

// Show next boxes user is going to recieve
public class QueueFragment extends android.support.v4.app.Fragment  {
    private final String TAG = getClass().getSimpleName();

    private FirebaseService mFirebaseService;
    private User mUser;

    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;

    private RecyclerView mQueueRecyclerView;
    private QueueAdapter mQueueAdapter;
    private SwipeRefreshLayout mSwipeContainer;

    private List<Box> mBoxes;
    public List<String> mQueue;

    public static QueueFragment newInstance() {
        QueueFragment queueFragment = new QueueFragment();
        return queueFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mUser = mFirebaseService.getUser();
        mBoxes = mFirebaseService.getAllBoxes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queue, container, false);
        fetchLayout(inflater, view);

        // Create recycler view and pull down to refresh recycler view
        mQueueRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFirebaseService.fetchQueue();
                updateUI();
                mSwipeContainer.setRefreshing(false);
            }
        });

        updateUI();

        return view;
    }

    // Update recycler view contents
    public void updateUI() {
        mQueue = mUser.getQueue();
        List<Box> showboxes = stringToBox(mBoxes, mQueue);
        if (mQueueAdapter == null) {
            mQueueAdapter = new QueueFragment.QueueAdapter(showboxes);
            mQueueRecyclerView.setAdapter(mQueueAdapter);
        } else {
            mQueueAdapter.setBoxes(showboxes);
            mQueueAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "updateUI()");
    }

    // Holder for recycler view
    private class QueueHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView boxNameTextView;
        private TextView companyNameTextView;
        private TextView priceTextView;
        private ImageView boxImageView;
        private TextView expectedDeliveryTextView;
        private Button removeFromQueueButton;

        private Box box;

        public QueueHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            boxNameTextView = (TextView)itemView.findViewById(R.id.queue_item_box_name);
            companyNameTextView = (TextView)itemView.findViewById(R.id.queue_item_box_company_name);
            priceTextView = (TextView)itemView.findViewById(R.id.queue_item_box_price);
            boxImageView = (ImageView)itemView.findViewById(R.id.queue_box_image_preview);
            removeFromQueueButton = (Button)itemView.findViewById(R.id.queue_order_again_button);
            expectedDeliveryTextView = (TextView)itemView.findViewById(R.id.queue_expected_delivery);

            removeFromQueueButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        removeFromQueueButton.setBackgroundColor(getResources().getColor(R.color.removeQueueButtonDark, null));
                    }
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        removeFromQueueButton.setBackgroundColor(getResources().getColor(R.color.removeQueueButton, null));
                        User user = mFirebaseService.getUser();
                        user.deleteFromQueue(box.getBoxID());
                        mFirebaseService.updateUser(user);
                        mUser.getQueue();
                        updateUI();
                    }
                    else{
                        removeFromQueueButton.setBackgroundColor(getResources().getColor(R.color.removeQueueButton, null));
                    }
                    return false;
                }
            });
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
            expectedDeliveryTextView.setText("Not Yet Shipped");
        }

        @Override
        public void onClick(View view) {
        }
    }

    // Adapter for recycler view
    private class QueueAdapter extends RecyclerView.Adapter<QueueHolder> {
        private List<Box> boxes;

        public QueueAdapter(List<Box> boxes) {
            this.boxes = boxes;
        }

        public void setBoxes(List<Box> boxes) {
            this.boxes = boxes;
        }

        @Override
        public QueueFragment.QueueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.queue_item_template, parent, false);
            return new QueueFragment.QueueHolder(view);
        }

        @Override
        public void onBindViewHolder(QueueFragment.QueueHolder holder, int position) {
            Box box = boxes.get(position);
            holder.bindBox(box);
        }

        @Override
        public int getItemCount() {
            return boxes.size();
        }
    }

    // Feches boxes on queue list
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
        mTopBarRelativeLayout = (RelativeLayout)view.findViewById(R.id.queue_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView)view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.queue_title));
        mQueueRecyclerView = (RecyclerView)view.findViewById(R.id.queue_recycler_view);
        mSwipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainerqueue);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }
}
