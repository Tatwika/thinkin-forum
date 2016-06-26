package com.aman.thinkin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.firebase.client.core.Context;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import me.gujun.android.taggroup.TagGroup;

public class Fragment_Posts extends Fragment {
    Firebase mRoot, mPosts;
    RecyclerView rvPosts;
    //static FirebaseRecyclerAdapter<Post, PostViewHolder> postAdapter;
    static AuthData authData;
    Date timeStamp;
    static String postID;
    private static String FIREBASE_URL = "https://think-in.firebaseio.com/";

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
       /* final Firebase mRoot = new Firebase("https://think-in.firebaseio.com/");
        mPosts = mRoot.child("posts");

        rvPosts.setHasFixedSize(true); //for performance improvement
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));    //for vertical list


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

        rvPosts.setAdapter(postAdapter);*/

        return inflater.inflate(R.layout.fragment_posts, container, false);


    }
   /* public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle, tvContent, tvPostedBy, tvUpvotes,tvPostTime;

        ImageButton ibPostMenuButton;
        TagGroup tgDisplayTags;
        Button bUpvote;
        private android.content.Context context;
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
                    Post postForUpvote = (Post) ProfileActivity.postAdapter.getItem(getAdapterPosition());
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
                    final Post post = (Post) ProfileActivity.postAdapter.getItem(getAdapterPosition());
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
                                    Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show();
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
