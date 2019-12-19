package whitehorsecorporation.contactconnection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity{

    Button btn_contacts,btn_search,btn_add_person,btn_yourads,btn_supportperson,btn_otherads;
    private long backPressedTime;
    private Toast backToast;

    private Toolbar mToolbar;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private String currentUserID;

    private AdView mAdView;

/////////////////////////double click exit////////////////////////////////////
    @Override
    public void onBackPressed() {
        if(backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast=Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }
//////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    /*    ShimmerFrameLayout containera = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_containera);
        containera.startShimmerAnimation();
        ShimmerFrameLayout containerb = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_containerb);
        containerb.startShimmerAnimation();
        ShimmerFrameLayout containerc = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_containerc);
        containerc.startShimmerAnimation();
        ShimmerFrameLayout containerd = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_containerd);
        containerd.startShimmerAnimation();
        ShimmerFrameLayout containere = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_containere);
        containere.startShimmerAnimation();
        ShimmerFrameLayout containerf = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_containerf);
        containerf.startShimmerAnimation();
*/
        btn_contacts = (Button) findViewById(R.id.buttonSearch);
        btn_search = (Button) findViewById(R.id.buttonChat);
        btn_add_person = (Button) findViewById(R.id.buttonSetting);
        btn_yourads = (Button) findViewById(R.id.buttonYourAds);
        btn_otherads = (Button)findViewById(R.id.buttonOtherAds);
        btn_supportperson = (Button) findViewById(R.id.buttonSupport);
//////////////////giving action to button/////////////////////////////////

        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact = new Intent(HomeActivity.this, search.class);
                startActivity(contact);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(search);
            }
        });
        btn_add_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addperson = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(addperson);
            }
        });
        btn_yourads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yourads = new Intent(HomeActivity.this, YourAdsActivity.class);
                startActivity(yourads);
            }
        });
        btn_otherads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent othersads = new Intent(HomeActivity.this, OthersAdsActivity.class);
                startActivity(othersads);
            }
        });
        btn_supportperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addperson = new Intent(HomeActivity.this, SupportPerson.class);
                startActivity(addperson);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Contact Connection");



        MobileAds.initialize(this,"ca-app-pub-5799575171454791~7150247034");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-5799575171454791/1190525101");
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        if (currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            updateUserStatus("online");

            VerifyUserExistance();
        }
    }


    @Override
    protected void onStop()
    {
        super.onStop();

        if (currentUser != null)
        {
            updateUserStatus("offline");
        }
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (currentUser != null)
        {
            updateUserStatus("offline");
        }
    }



    private void VerifyUserExistance()
    {
        String currentUserID = mAuth.getCurrentUser().getUid();

        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if ((dataSnapshot.child("name").exists()))
                {

                }
                else
                {
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.drawermenu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_home_option)
        {

        }
        if (item.getItemId() == R.id.main_find_friends_option)
        {
            SendUserToFindFriendsActivity();
        }
        if (item.getItemId() == R.id.main_create_group_option)
        {
            RequestNewGroup();
        }
        if (item.getItemId() == R.id.main_settings_option)
        {
            SendUserToSettingsActivity();
        }
        if (item.getItemId() == R.id.main_like_option)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Like This App ?")
                    .setMessage("You Can Support Me, By Paying Me Any Amount You Like, On My Kotak 811 Account VPA ID : 7303330266@kotak or Paytm no. 7303330266")
                    .show();
        }
        if (item.getItemId() == R.id.main_about_option)
        {
            Intent intent = new Intent(HomeActivity.this,AboutActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.main_logout_option)
        {
            updateUserStatus("offline");
            mAuth.signOut();
            SendUserToLoginActivity();
        }

        return true;
    }


    private void RequestNewGroup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Blog Page Name :");

        final EditText groupNameField = new EditText(HomeActivity.this);
        groupNameField.setHint("e.g Yaaro Ki Toli");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(HomeActivity.this, "Please write Blog Page Name...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }



    private void CreateNewGroup(final String groupName)
    {
        RootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(HomeActivity.this, groupName + " group is Created Successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

    }

    private void SendUserToSettingsActivity()
    {
        Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    private void SendUserToFindFriendsActivity()
    {
        Intent findFriendsIntent = new Intent(HomeActivity.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
    }



    private void updateUserStatus(String state)
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time", saveCurrentTime);
        onlineStateMap.put("date", saveCurrentDate);
        onlineStateMap.put("state", state);

        RootRef.child("Users").child(currentUserID).child("userState")
                .updateChildren(onlineStateMap);

        }
}

