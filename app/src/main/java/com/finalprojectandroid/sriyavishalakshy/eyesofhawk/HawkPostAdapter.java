package com.finalprojectandroid.sriyavishalakshy.eyesofhawk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * This is an Adapter class for the Recycler View of Posts
 * This class is used to display the posts about events on campus as well as
 * Food Alert Posts
 *@author sriyavishalakshy
 */
public class HawkPostAdapter extends RecyclerView.Adapter<HawkPostAdapter.ViewHolder>  {

    private  List<HawkPost> post_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    /**
     *Constructor that initializes the
     * @param post_list
     */
    public HawkPostAdapter(List<HawkPost> post_list){

        this.post_list = post_list;

    }

    /**
     *This method is used to inflate the posts view with data from the db
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public HawkPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hawk_view_layout, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    /**
     *This method is used to inilize the view post with each of the object data
     * fetched from list of data
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(HawkPostAdapter.ViewHolder holder, int position) {
        String desc_data = post_list.get(position).getDesc();
        holder.setViewParameters(post_list.get(position));


    }

    @Override
    public int getItemCount() {
        return post_list.size();
    }

    /**
     * This is View Holder class that populates every card in the list
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private TextView descView;
        private TextView roomNo;
        private TextView userName;
        private TextView postDate;
        private TextView eventDesc;



        public ViewHolder(View itemView) {

            super(itemView);
            mView=itemView;
        }

        /**
         *This method initializes each of the card post on the view with
         * the object data this populates the hawk_view_layout.xml file
         * @param object
         */
        public void setViewParameters(HawkPost object){

            descView = mView.findViewById(R.id.post_location);
            descView.setText(object.getDesc());
            roomNo =  mView.findViewById(R.id.post_room_no);
            roomNo.setText(object.getRoom_no());
            userName = mView.findViewById(R.id.hawkid_user_name);
            userName.setText(object.getUser_id());
            postDate= mView.findViewById(R.id.blog_date);
            postDate.setText(String.valueOf(object.getTimestamp()));
            eventDesc= mView.findViewById(R.id.post_event);
            eventDesc.setText(String.valueOf(object.getEvent_info()));


        }
    }
}
