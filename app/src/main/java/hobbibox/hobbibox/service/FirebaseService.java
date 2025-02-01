package hobbibox.hobbibox.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hobbibox.hobbibox.helper.Variable;
import hobbibox.hobbibox.DependencyFactory;
import hobbibox.hobbibox.nonUI.Box;
import hobbibox.hobbibox.nonUI.Category;
import hobbibox.hobbibox.nonUI.User;

// Handle database updates and requests
public class FirebaseService {
    private final String TAG = getClass().getSimpleName();
    private final String DB_USERS = "Users/";
    private final String DB_BOXES = "Boxes/";
    private final String DB_CATEGORIES = "Categories/";
    private final String DB_VERIFIED_APPLICATIONS = "VerifiedApplications/";

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private User mUser;
    private ValueEventListener mUserValueEventListener;
    private ValueEventListener mBoxValueEventListener;
    private ValueEventListener mCategoriesValueEventListener;
    private Variable mVariable;
    private boolean isUpdated;
    private boolean loginStatus;
    private boolean forLogin;
    private List<Box> mAllBoxes;
    private List<Category> mCategories;

    private final String DB_HISTORY = "/history";
    private List<String> mHistory;
    private ValueEventListener mHistoryValueEventListener;
    private final String DB_QUEUE = "/queue";
    private List<String> mQueue;
    private ValueEventListener mQueueValueEventListener;

    public FirebaseService(){
        mAuth = FirebaseAuth.getInstance();
        mVariable = DependencyFactory.getUserLoaded();
        mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser == null){
            mVariable.noEvent();
        }
        loginStatus = false;
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if(!loginStatus) {
                        Log.d(TAG, "In");
                        forLogin = true;
                        mFirebaseUser = user;
                        mVariable.reset();
                        fetchUser();
                        loginStatus = true;
                    }
                }
                else {
                    Log.d(TAG, "Out");
                    loginStatus = false;
                    mFirebaseUser = null;
                }
            }
        };
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        isUpdated = false;
    }

    public void forOnStartLogin(){
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void forOnStopLogin(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // Sign out user
    public void signOut(){
        mAuth.signOut();
        isUpdated = false;
        mUser = null;
        mAllBoxes = null;
        mCategories = null;
        forLogin = true;
    }

    // Authenticate user sign in / register
    public FirebaseAuth getmAuth(){
        return mAuth;
    }

    public DatabaseReference getVerifiedDBReference(){
        return mFirebaseDatabase.getReference(DB_VERIFIED_APPLICATIONS);
    }

    // Return user object
    public User getUser(){
        return mUser;
    }

    // Fetch user data from database
    public void fetchUser(){
        mUser = new User();
        mUser.setEmail(mAuth.getCurrentUser().getEmail());
        if(!isUpdated) {
            mUserValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    isUpdated = true;
                    if (user != null) {
                        mUser = user;
                    }
                    else {
                        mFirebaseDatabase.getReference("Users/" + mFirebaseUser.getUid()).setValue(mUser);
                    }
                    mFirebaseDatabase.getReference("Users/" + mFirebaseUser.getUid()).removeEventListener(mUserValueEventListener);
                    fetchBoxes();
                    fetchCategories();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mFirebaseDatabase.getReference("Users/" + mFirebaseUser.getUid()).removeEventListener(mUserValueEventListener);
                }
            };
            mFirebaseDatabase.getReference("Users/" + mFirebaseUser.getUid()).addListenerForSingleValueEvent(mUserValueEventListener);
        }
    }

    // Update user locally and in database
    public void updateUser(User user){
        if(isUpdated) {
            mUser = user;
            mFirebaseDatabase.getReference("Users/" + mFirebaseUser.getUid()).setValue(mUser);
        }
    }

    // Get boxes
    public List<Box> getAllBoxes(){
        return mAllBoxes;
    }

    // Fetch boxes from database
    public void fetchBoxes(){
        if(mAllBoxes == null)
            mAllBoxes = new ArrayList<Box>();
        if(isUpdated){
            mBoxValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Box> boxes = new ArrayList<Box>();
                    for(DataSnapshot boxData : dataSnapshot.getChildren() ){
                        Box temp = boxData.getValue(Box.class);
                        temp.setBoxID(boxData.getKey());
                        boxes.add(temp);
                    }
                    mAllBoxes = boxes;
                    mFirebaseDatabase.getReference(DB_BOXES).removeEventListener(mBoxValueEventListener);
                    if(forLogin) {
                        mVariable.doEvent();
                        Log.d(TAG, "Boxes loaded");
                        forLogin = false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mFirebaseDatabase.getReference(DB_BOXES).removeEventListener(mBoxValueEventListener);
                }
            };
            mFirebaseDatabase.getReference(DB_BOXES).addListenerForSingleValueEvent(mBoxValueEventListener);
        }
    }

    // Get list of categories
    public List<Category> getCategories(){
        return mCategories;
    }

    // Fetch categories from database
    public void fetchCategories(){
        mCategories = new ArrayList<Category>();
        if(isUpdated){
            mCategoriesValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot categoryData : dataSnapshot.getChildren() ){
                        Category category = new Category();
                        category.setName(categoryData.getKey());
                        category.setImageString(categoryData.getValue(String.class));
                        mCategories.add(category);
                    }
                    mFirebaseDatabase.getReference(DB_CATEGORIES).removeEventListener(mBoxValueEventListener);
                    mVariable.doEvent();
                    Log.d(TAG, "Categories loaded");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mFirebaseDatabase.getReference(DB_CATEGORIES).removeEventListener(mCategoriesValueEventListener);
                }
            };
            mFirebaseDatabase.getReference(DB_CATEGORIES).addListenerForSingleValueEvent(mCategoriesValueEventListener);
        }
    }

    // Add box to database
    public void addBox(Box box){
        mFirebaseDatabase.getReference(DB_BOXES).push().setValue(box);
        fetchBoxes();
    }

    public List<String> getHistory(){
        return mHistory;
    }

    public void fetchHistory(){
        if(isUpdated){
            mHistoryValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
                    mHistory = (List<String>)dataSnapshot.getValue(t);
                    mUser.setHistory(mHistory);
                    mFirebaseDatabase.getReference(DB_BOXES + mFirebaseUser.getUid() + DB_HISTORY).removeEventListener(mHistoryValueEventListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mFirebaseDatabase.getReference(DB_BOXES + mFirebaseUser.getUid() + DB_HISTORY).removeEventListener(mHistoryValueEventListener);
                }
            };
            mFirebaseDatabase.getReference(DB_USERS + mFirebaseUser.getUid() + DB_HISTORY).addListenerForSingleValueEvent(mHistoryValueEventListener);
        }
    }

    public List<String> getQueue(){
        return mQueue;
    }

    public void fetchQueue(){
        if(isUpdated){
            mQueueValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
                    mQueue = (List<String>)dataSnapshot.getValue(t);
                    mUser.setQueue(mQueue);
                    mFirebaseDatabase.getReference(DB_BOXES + mFirebaseUser.getUid() + DB_QUEUE).removeEventListener(mQueueValueEventListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mFirebaseDatabase.getReference(DB_BOXES + mFirebaseUser.getUid() + DB_QUEUE).removeEventListener(mQueueValueEventListener);
                }
            };
            mFirebaseDatabase.getReference(DB_USERS + mFirebaseUser.getUid() + DB_QUEUE).addListenerForSingleValueEvent(mQueueValueEventListener);
        }
    }
}
