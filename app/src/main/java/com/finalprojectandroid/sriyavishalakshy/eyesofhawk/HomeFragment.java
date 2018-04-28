package com.finalprojectandroid.sriyavishalakshy.eyesofhawk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *This is the fragment class that is generated in order to fill the home page fragments
 * The home screen has two fragments one to populate general events on campus
 * and other for events with food involved
 *This uses a recycleadapter
 * @author sriyavishalakshy
 */
public class HomeFragment extends Fragment {

    private RecyclerView post_list_view;
   private List<HawkPost> post_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private HawkPostAdapter postRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     *This creates the view for the home screen which has
     * a list of all evets that are happening on campus along with the dates
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        post_list = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        post_list = new ArrayList<>();
        //used to inflate the home list with database from firebase
        post_list_view = view.findViewById(R.id.hawk_post_list);

        firebaseAuth = FirebaseAuth.getInstance();

        postRecyclerAdapter = new HawkPostAdapter(post_list);
        post_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        post_list_view.setAdapter(postRecyclerAdapter);
        post_list_view.setHasFixedSize(true);

        if(firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            post_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if(reachedBottom){

                        loadMorePost();

                    }

                }
            });
        //Posts about general events around campus are being stored in this db and are fetched to populate the list
            Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        if (isFirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            post_list.clear();

                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                HawkPost blogPost = doc.getDocument().toObject(HawkPost.class);

                                if (isFirstPageFirstLoad) {

                                    post_list.add(blogPost);

                                } else {

                                    post_list.add(0, blogPost);

                                }


                                postRecyclerAdapter.notifyDataSetChanged();

                            }
                        }

                        isFirstPageFirstLoad = false;

                    }

                }

            });

        }

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * This method is used to load more posts that are being loaded as they being added
     */
    public void loadMorePost(){

        if(firebaseAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                HawkPost blogPost = doc.getDocument().toObject(HawkPost.class);
                                post_list.add(blogPost);

                                postRecyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    }

                }
            });

        }

    }

}
