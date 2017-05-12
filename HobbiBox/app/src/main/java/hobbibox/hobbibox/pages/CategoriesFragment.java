package hobbibox.hobbibox.pages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.R;
import hobbibox.hobbibox.nonUI.Category;
import hobbibox.hobbibox.nonUI.User;
import hobbibox.hobbibox.service.FirebaseService;

// Display different categories of boxes and allow user to choose interests
public class CategoriesFragment extends android.support.v4.app.Fragment  {
    private final String TAG = getClass().getSimpleName();

    private FirebaseService mFirebaseService;
    private RelativeLayout mTopBarRelativeLayout;
    private TextView mTopBarTextView;

    private List<Category> mCategories;
    private CategoryAdapter mCategoryAdapater;
    private RecyclerView mCategoryRecyclerView;

    public static CategoriesFragment newInstance() {
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        return categoriesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseService = DependencyFactory.getFirebaseService(getActivity().getApplicationContext());
        mCategories = mFirebaseService.getCategories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        fetchLayout(inflater, view);

        // Create recycler view
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    // Update elements inside recycler view
    public void updateUI() {
        if (mCategoryAdapater == null) {
            mCategoryAdapater = new CategoryAdapter(mCategories);
            mCategoryRecyclerView.setAdapter(mCategoryAdapater);
        } else {
            mCategoryAdapater.setCategories(mCategories);
            mCategoryAdapater.notifyDataSetChanged();
        }
        Log.d(TAG, "updateUI()");
    }

    // Fetch layout elements
    private void fetchLayout(LayoutInflater inflater, View view){
        mTopBarRelativeLayout = (RelativeLayout)view.findViewById(R.id.categories_relative_layout);
        inflater.inflate(R.layout.action_bar, mTopBarRelativeLayout);
        mTopBarTextView = (TextView)view.findViewById(R.id.top_bar_title);
        mTopBarTextView.setText(getResources().getString(R.string.categories_title));
        mCategoryRecyclerView = (RecyclerView)view.findViewById(R.id.categories_recyclerview);
    }

    // Holder for recycler view
    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView categoryName;
        private ImageView categoryImageView;
        private LinearLayout categoryLinearLayout;

        private Category category;

        public CategoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            categoryName = (TextView)itemView.findViewById(R.id.category_name);
            categoryImageView = (ImageView)itemView.findViewById(R.id.category_image);
            categoryLinearLayout = (LinearLayout)itemView.findViewById(R.id.categories_lin_layout);
        }

        public void bindBox(final Category category) {
            categoryLinearLayout.setBackgroundColor(getResources().getColor(R.color.white, null));
            categoryName.setTextColor(getResources().getColor(R.color.grey, null));
            this.category = category;

            categoryName.setText(category.getName());
            try {
                byte[] decodedString = Base64.decode(category.getImageString(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                categoryImageView.setImageBitmap(decodedByte);
            }
            catch (Exception e){
                Log.d(TAG, e.toString());
            }

            User user = mFirebaseService.getUser();
            if(user.getInterests().contains(category.getName())){
                categoryLinearLayout.setBackgroundColor(getResources().getColor(R.color.blue, null));
                categoryName.setTextColor(getResources().getColor(R.color.white, null));
            }
        }

        // If a category is clicked, it is added to the user's interest list and filters the suggestions fragment
        @Override
        public void onClick(View view) {
            User user = mFirebaseService.getUser();
            
            if(!user.getInterests().contains(category.getName())){
                user.addInterest(category.getName());
                mFirebaseService.updateUser(user);
                updateUI();
            }
            else{
                user.deleteInterest(category.getName());
                mFirebaseService.updateUser(user);
                updateUI();
            }
        }
    }

    // Adapter for recycler view
    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<Category> categories;

        public CategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.category_list_item, parent, false);
            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {
            Category category = categories.get(position);
            holder.bindBox(category);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

    }

}
