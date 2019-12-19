package whitehorsecorporation.contactconnection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OthersAdsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView OtherAdsRecyclerList;
    private DatabaseReference UsersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_ads);


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        OtherAdsRecyclerList = (RecyclerView) findViewById(R.id.other_ads_recycler_list);
        OtherAdsRecyclerList.setLayoutManager(new LinearLayoutManager(this));


        mToolbar = (Toolbar) findViewById(R.id.other_ads_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Ads Of The World");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(UsersRef, Contacts.class)
                        .build();

        FirebaseRecyclerAdapter<Contacts, FindFriendsActivity.FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, FindFriendsActivity.FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsActivity.FindFriendViewHolder holder, final int position, @NonNull Contacts model)
                    {
                        Picasso.get().load(model.getAds()).placeholder(R.drawable.ads).into(holder.otherad);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String visit_user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(OthersAdsActivity.this, ProfileActivity.class);
                                profileIntent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindFriendsActivity.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.others_ads_display_layout, viewGroup, false);
                        FindFriendsActivity.FindFriendViewHolder viewHolder = new FindFriendsActivity.FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };

        OtherAdsRecyclerList.setAdapter(adapter);

        adapter.startListening();
    }



    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        ImageView otherad;


        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);

            otherad = itemView.findViewById(R.id.image_view_upload);
        }

    }
}
