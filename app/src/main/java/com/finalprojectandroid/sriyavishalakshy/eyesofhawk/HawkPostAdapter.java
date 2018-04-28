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

public class HawkPostAdapter extends RecyclerView.Adapter<HawkPostAdapter.ViewHolder>  {

    private  List<HawkPost> post_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    /**
     *
     * @param post_list
     */
    public HawkPostAdapter(List<HawkPost> post_list){

        this.post_list = post_list;

    }

    /**
     *
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
     *
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
         *
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
