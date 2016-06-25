package com.aman.thinkin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import me.gujun.android.taggroup.TagGroup;

public class ProfileActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{


    public static FirebaseListAdapter postAdapter;
    TextView tvDisplayFName,tvDisplayLName,tvDisplayBranch,tvDisplaySection,tvDisplayYear,tvDisplayPosts,tvDisplayRating;
    Firebase mRoot, mPosts;
    RecyclerView rvPosts;
    // static FirebaseRecyclerAdapter<Post, PostViewHolder> postAdapter;
    static AuthData authData;
    Date timeStamp;
    static String postID;
    private static String FIREBASE_URL = "https://think-in.firebaseio.com/";
        private TabLayout tabLayout;

        //This is our viewPager
        private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpVariables();
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Posts");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setContentScrimColor(Color.BLUE);
        collapsingToolbarLayout.setStatusBarScrimColor(Color.GREEN);


        final Firebase mRoot = new Firebase("https://think-in.firebaseio.com/");
        AuthData authData = mRoot.getAuth();
        Firebase mUser = mRoot.child("users/" + authData.getUid());
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               /* Map<String,String> profileData = dataSnapshot.getValue(Map.class);
                for(Map.Entry<String,String> me: profileData.entrySet()){
                    switch(me.getKey()){
                        case "section":
                            tvDisplaySection.setText(me.getValue());
                            break;
                        case "branch":
                            tvDisplayBranch.setText(me.getValue());
                            break;
                        case "year":
                            tvDisplayYear.setText(me.getValue());
                            break;
                        case "fname":
                            tvDisplayFName.setText(me.getValue());
                            break;
                        case "lname":
                            tvDisplayLName.setText(me.getValue());
                            break;
                    }
                }*/
                if (dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    tvDisplayFName.setText(user.getFname().toString()+" ");
                    tvDisplayLName.setText(" "+user.getLname().toString());
                    tvDisplayBranch.setText(user.getBranch().toString()+" ");
                    tvDisplaySection.setText(user.getSection().toString());
                    tvDisplayYear.setText(" "+user.getYear().toString()+" year");


                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


       /* mPosts = mRoot.child("posts");

        rvPosts.setHasFixedSize(true); //for performance improvement
        rvPosts.setLayoutManager(new LinearLayoutManager(this));    //for vertical list


        authData = mRoot.getAuth();

        //using built-in firebase adapter from firebase ui to automatically handle firebase stuff

        final AuthData finalAuthData = authData;
      //  Firebase post=mPosts.getRoot();
       // if (finalAuthData.toString() == post.getPostedBy()) {

            postAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.view_posts, PostViewHolder.class, mPosts.orderByChild("postedBy")) {

                @Override
                protected void populateViewHolder(final PostViewHolder postViewHolder, final Post post, int i) {
                    Log.w("FIREBASE_ADAPTER", "called");
                    //populating views in card view
                    // Log.w("auth", finalAuthData.toString());
                    //Log.w("id",post.getPostedBy());
                    //  if(finalAuthData.toString()==post.getPostedBy()) {
                    postViewHolder.tvTitle.setText(post.getTitle());
                    postViewHolder.tvContent.setText(post.getContent());
                    postViewHolder.tvUpvotes.setText(String.valueOf(post.getUpvotes()));
                    postViewHolder.tvPostTime.setText(getDate(post.getTimeStamp()));
                    postViewHolder.tgDisplayTags.setTags(post.getTagList());

                    //Query name of user who has posted

                    Query queryName = mRoot.child("users/" + post.getPostedBy() + "/fname");
                    queryName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null)
                                // Log.w("POST::",dataSnapshot.getValue(String.class));
                                postViewHolder.tvPostedBy.setText(dataSnapshot.getValue(String.class));

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }

            // }
            //get contextual time
        private String getDate ( long timeStamp){
            long timestamp = -timeStamp;
            Date date = new Date();
            long difference = Math.abs(timestamp - date.getTime()) / 1000;

            if (difference < 60) {
                String time = String.valueOf(difference);
                if (time.equals("1"))
                    return time + " sec ago";
                else return time + " secs ago";

            } else if (difference >= 60 && difference < 3600) {
                String time = String.valueOf(difference / 60);
                if (time.equals("1"))
                    return time + " min ago";
                else return time + " mins ago";
            } else if (difference >= 3600 && difference < 3600 * 24) {
                String time = String.valueOf(difference / 3600);
                if (time.equals("1"))
                    return time + " hr ago";
                else return time + " hrs ago";
            } else {
                String time = String.valueOf(difference / (3600 * 24));
                if (time.equals("1"))
                    return time + " day ago";
                else return time + " days ago";
            }
        }

        };
   // }
//        postAdapter.notifyDataSetChanged();


       // postAdapter.notifyAll();

       // rvPosts.setAdapter(postAdapter);
        int postForSelect = postAdapter.getItemCount();
        Log.w("count", String.valueOf(postAdapter.getItemCount()));
        while(postForSelect!=0)
        {
            Post postFor = postAdapter.getItem(postForSelect);
            Firebase upvotePostRef = new Firebase(FIREBASE_URL+"/posts/"+postFor.getPostID()+"/postedBy");
            Log.w("post id",postFor.getPostID());
            Log.w("up",upvotePostRef.toString());
            Log.w("auth",authData.toString());

            // upvotePostRef.setValue(postForUpvote.getUpvotes()+1);
            if(upvotePostRef.toString()!=authData.toString())
            {
               postAdapter.notifyItemRemoved(postForSelect);
            }
            postForSelect--;
        }

        rvPosts.setAdapter(postAdapter);

*/
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Notifications"));
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        //tabLayout.addTab(tabLayout.newTab().setText("Your Tab Title"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);



    }

    private void setUpVariables() {
        tvDisplayBranch = (TextView)findViewById(R.id.tvDisplayBranch);
        tvDisplaySection = (TextView)findViewById(R.id.tvDisplaySection);
        tvDisplayYear = (TextView)findViewById(R.id.tvDisplayYear);
        tvDisplayFName = (TextView)findViewById(R.id.tvDisplayFName);
        tvDisplayLName = (TextView)findViewById(R.id.tvDisplayLName);
        tvDisplayPosts=(TextView)findViewById(R.id.tvDisplayPosts);
        tvDisplayRating=(TextView)findViewById(R.id.tvDisplayRating);
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);



    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
  /*  public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle, tvContent, tvPostedBy, tvUpvotes,tvPostTime;

        ImageButton ibPostMenuButton;
        TagGroup tgDisplayTags;
        Button bUpvote;
        private Context context;
        public PostViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvPostedBy = (TextView) itemView.findViewById(R.id.tvPostedBy);
            tvUpvotes = (TextView) itemView.findViewById(R.id.tvUpvotes);
            tvPostTime = (TextView) itemView.findViewById(R.id.tvPostTime);
            tgDisplayTags = (TagGroup) itemView.findViewById(R.id.tgDisplayPostTags);
            bUpvote = (Button) itemView.findViewById(R.id.bUpvote);
            ibPostMenuButton = (ImageButton)itemView.findViewById(R.id.ibPostMenuButton);
            ibPostMenuButton.setOnClickListener(this);
            bUpvote.setOnClickListener(this);
            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bUpvote:
                    Post postForUpvote = ProfileActivity.postAdapter.getItem(getAdapterPosition());
                    Firebase upvoteListRef = new Firebase(FIREBASE_URL+"/posts/"+postForUpvote.getPostID()+"/upvoteList");
                    ArrayList<String> upvoteList = new ArrayList<String>(Arrays.asList(postForUpvote.getUpvoteList()));
                    Log.w("List item",upvoteList.toString());
                    if(!upvoteList.contains(authData.getUid())){
                        upvoteList.add(authData.getUid());
                        upvoteListRef.setValue(upvoteList.toArray());
                        Firebase upvotePostRef = new Firebase(FIREBASE_URL+"/posts/"+postForUpvote.getPostID()+"/upvotes");
                        upvotePostRef.setValue(postForUpvote.getUpvotes()+1);
                    }
                    else Toast.makeText(context,"Already Upvoted",Toast.LENGTH_SHORT).show();

                    break;
                case R.id.ibPostMenuButton:
                    PopupMenu popupMenu = new PopupMenu(context,ibPostMenuButton);
                    View menuParent = (View)view.getParent();
                    final Post post = ProfileActivity.postAdapter.getItem(getAdapterPosition());
                    if(post.getPostedBy().equals(authData.getUid())){
                        popupMenu.getMenu().add(0,0,0,"Edit");
                        popupMenu.getMenu().add(0,1,1,"Delete");
                    }
                    else
                        popupMenu.getMenu().add(0,2,2,"Report");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case 0:
                                    Toast.makeText(context,"Edit coming soon!",Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    String postID = post.getPostID();
                                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                    Firebase postRef = new Firebase(FIREBASE_URL).child("posts").child(postID);
                                    postRef.setValue(null);
                                    break;
                                case 2:
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    break;
                default:
                    Log.d("THINKIN", "onClick " + getAdapterPosition());
                    Intent startCommentActivity = new Intent(context,CommentActivity.class);
                    Log.w("PostID::",ProfileActivity.postAdapter.getRef(getAdapterPosition()).getKey());
                    startCommentActivity.putExtra("postID",PostsActivity.postAdapter.getRef(getAdapterPosition()).getKey());
                    TextView title = (TextView)view.findViewById(R.id.tvTitle);
                    TextView content = (TextView)view.findViewById(R.id.tvContent);
                    TextView postedBy = (TextView)view.findViewById(R.id.tvPostedBy);
                    startCommentActivity.putExtra("POST_TITLE",title.getText());
                    startCommentActivity.putExtra("POST_CONTENT",content.getText());
                    startCommentActivity.putExtra("POSTED_BY",postedBy.getText());
                    context.startActivity(startCommentActivity);
            }
        }

    }*/
}




