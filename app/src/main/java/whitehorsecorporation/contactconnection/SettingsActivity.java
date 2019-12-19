package whitehorsecorporation.contactconnection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity
{
    private Button UpdateAccountSettings;
    private EditText userName, userStatus,phonenumber,workat,age,youraddress,workaddress,homeservice,specialmessage,monams,monpms,
    tueams,tuepms,wedams,wedpms,thuams,thupms,friams,fripms,satams,satpms,sunams,sunpms;
    private CircleImageView userProfileImage;
    private ImageView shopbackground;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private static final int GalleryPick = 1;
    private StorageReference UserProfileImagesRef;
    private ProgressDialog loadingBar;

    private Toolbar SettingsToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        InitializeFields();

        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UpdateSettings();
            }
        });


        RetrieveUserInfo();


        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });
        shopbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,ShopBackgroundActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        new AlertDialog.Builder(this)
                .setTitle("Important Things")
                .setMessage("Only name, workas and phone number is important")
                .show();

    }



    private void InitializeFields()
    {
        UpdateAccountSettings = (Button) findViewById(R.id.update_settings_button);
        userName = (EditText) findViewById(R.id.set_user_name);
        userStatus = (EditText) findViewById(R.id.set_profile_status);
        phonenumber = (EditText) findViewById(R.id.phoneNumber);
        workat = (EditText) findViewById(R.id.workat);
        age = (EditText) findViewById(R.id.age);
        youraddress = (EditText) findViewById(R.id.youraddress);
        workaddress = (EditText) findViewById(R.id.workaddress);
        homeservice = (EditText) findViewById(R.id.homeservice);
        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);
        loadingBar = new ProgressDialog(this);
        shopbackground = (ImageView)findViewById(R.id.usershopbackground);


        specialmessage = (EditText)findViewById(R.id.specialmessage);
        monams = (EditText) findViewById(R.id.monam);
        monpms = (EditText) findViewById(R.id.monpm);
        tueams = (EditText) findViewById(R.id.tueam);
        tuepms = (EditText) findViewById(R.id.tuepm);
        wedams = (EditText) findViewById(R.id.wedam);
        wedpms = (EditText) findViewById(R.id.wedpm);
        thuams = (EditText) findViewById(R.id.thuam);
        thupms = (EditText) findViewById(R.id.thupm);
        friams = (EditText) findViewById(R.id.friam);
        fripms = (EditText) findViewById(R.id.fripm);
        satams = (EditText) findViewById(R.id.satam);
        satpms = (EditText) findViewById(R.id.satpm);
        sunams = (EditText) findViewById(R.id.sunam);
        sunpms = (EditText) findViewById(R.id.sunpm);


        SettingsToolBar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(SettingsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Account Settings");
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode==RESULT_OK)
            {
                loadingBar.setTitle("Set Profile Image");
                loadingBar.setMessage("Please wait, your profile image is updating...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();


                StorageReference filePath = UserProfileImagesRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(SettingsActivity.this, "Profile Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                            final String downloaedUrl = task.getResult().getDownloadUrl().toString();

                            RootRef.child("Users").child(currentUserID).child("image")
                                    .setValue(downloaedUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(SettingsActivity.this, "Image save in Database, Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().toString();
                                                Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }
    }


    private void UpdateSettings()
    {
        String setUserName = userName.getText().toString();
        String setStatus = userStatus.getText().toString();
        String setPhoneNumber = phonenumber.getText().toString();
        String setworkat = workat.getText().toString();
        String setage = age.getText().toString();
        String setyouraddress = youraddress.getText().toString();
        String setworkaddress = workaddress.getText().toString();
        String sethomeservices = homeservice.getText().toString();
        String setspecialmessage = specialmessage.getText().toString();
        String setmonam = monams.getText().toString();
        String setmonpm = monpms.getText().toString();
        String settueam = tueams.getText().toString();
        String settuepm = tuepms.getText().toString();
        String setwedam = wedams.getText().toString();
        String setwedpm = wedpms.getText().toString();
        String setthuam = thuams.getText().toString();
        String setthupm = thupms.getText().toString();
        String setfriam = friams.getText().toString();
        String setfripm = fripms.getText().toString();
        String setsatam = satams.getText().toString();
        String setsatpm = satpms.getText().toString();
        String setsunam = sunams.getText().toString();
        String setsunpm = sunpms.getText().toString();


        if (TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(this, "Please write your user name first....", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setStatus))
        {
            Toast.makeText(this, "Please write what work you do....", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setPhoneNumber))
        {
            Toast.makeText(this, "Please write your Phone Number....", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> profileMap = new HashMap<>();
                profileMap.put("uid", currentUserID);
                profileMap.put("name", setUserName);
                profileMap.put("status", setStatus);
                profileMap.put("phonenumber", setPhoneNumber);
                profileMap.put("workat", setworkat);
                profileMap.put("age", setage);
                profileMap.put("youraddress", setyouraddress);
                profileMap.put("workaddress", setworkaddress);
                profileMap.put("homeservice",sethomeservices);
                profileMap.put("specialmessage",setspecialmessage);
                profileMap.put("monam",setmonam);
                profileMap.put("monpm",setmonpm);
                profileMap.put("tueam",settueam);
                profileMap.put("tuepm",settuepm);
                profileMap.put("wedam",setwedam);
                profileMap.put("wedpm",setwedpm);
                profileMap.put("thuam",setthuam);
                profileMap.put("thupm",setthupm);
                profileMap.put("friam",setfriam);
                profileMap.put("fripm",setfripm);
                profileMap.put("satam",setsatam);
                profileMap.put("satpm",setsatpm);
                profileMap.put("sunam",setsunam);
                profileMap.put("sunpm",setsunpm);

            RootRef.child("Users").child(currentUserID).updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                SendUserToHomeActivity();
                                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



    private void RetrieveUserInfo()
    {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image"))) && (dataSnapshot.hasChild("phonenumber")) && (dataSnapshot.hasChild("shopback")))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrievesStatus = dataSnapshot.child("status").getValue().toString();
                            String retrievePhoneNumber = dataSnapshot.child("phonenumber").getValue().toString();
                            String retrieveworkat = dataSnapshot.child("workat").getValue().toString();
                            String retrieveage = dataSnapshot.child("age").getValue().toString();
                            String retrievesyouraddress = dataSnapshot.child("youraddress").getValue().toString();
                            String retrieveworkaddress = dataSnapshot.child("workaddress").getValue().toString();
                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();
                            String retrievehomeservices = dataSnapshot.child("homeservice").getValue().toString();
                            String retrievespecialmessage = dataSnapshot.child("specialmessage").getValue().toString();
                            String retrievemonam = dataSnapshot.child("monam").getValue().toString();
                            String retrievemonpm = dataSnapshot.child("monpm").getValue().toString();
                            String retrievetueam = dataSnapshot.child("tueam").getValue().toString();
                            String retrievetuepm = dataSnapshot.child("tuepm").getValue().toString();
                            String retrievewedam = dataSnapshot.child("wedam").getValue().toString();
                            String retrievewedpm = dataSnapshot.child("wedpm").getValue().toString();
                            String retrievethuam = dataSnapshot.child("thuam").getValue().toString();
                            String retrievethupm = dataSnapshot.child("thupm").getValue().toString();
                            String retrievefriam = dataSnapshot.child("friam").getValue().toString();
                            String retrievefripm = dataSnapshot.child("fripm").getValue().toString();
                            String retrievesatam = dataSnapshot.child("satam").getValue().toString();
                            String retrievesatpm = dataSnapshot.child("satpm").getValue().toString();
                            String retrievesunam = dataSnapshot.child("sunam").getValue().toString();
                            String retrievesunpm = dataSnapshot.child("sunpm").getValue().toString();
                            String retrieveShopBackground = dataSnapshot.child("shopback").getValue().toString();


                            userName.setText(retrieveUserName);
                            userStatus.setText(retrievesStatus);
                            phonenumber.setText(retrievePhoneNumber);
                            workat.setText(retrieveworkat);
                            age.setText(retrieveage);
                            youraddress.setText(retrievesyouraddress);
                            workaddress.setText(retrieveworkaddress);
                            homeservice.setText(retrievehomeservices);
                            Picasso.get().load(retrieveProfileImage).into(userProfileImage);
                            specialmessage.setText(retrievespecialmessage);
                            monams.setText(retrievemonam);
                            monpms.setText(retrievemonpm);
                            tueams.setText(retrievetueam);
                            tuepms.setText(retrievetuepm);
                            wedams.setText(retrievewedam);
                            wedpms.setText(retrievewedpm);
                            thuams.setText(retrievethuam);
                            thupms.setText(retrievethupm);
                            friams.setText(retrievefriam);
                            fripms.setText(retrievefripm);
                            satams.setText(retrievesatam);
                            satpms.setText(retrievesatpm);
                            sunams.setText(retrievesunam);
                            sunpms.setText(retrievesunpm);
                            Picasso.get().load(retrieveShopBackground).into(shopbackground);


                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("phonenumber")))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrievesStatus = dataSnapshot.child("status").getValue().toString();
                            String retrievePhoneNumber = dataSnapshot.child("phonenumber").getValue().toString();
                            String retrieveworkat = dataSnapshot.child("workat").getValue().toString();
                            String retrieveage = dataSnapshot.child("age").getValue().toString();
                            String retrievesyouraddress = dataSnapshot.child("youraddress").getValue().toString();
                            String retrieveworkaddress = dataSnapshot.child("workaddress").getValue().toString();
                            String retrievehomeservices = dataSnapshot.child("homeservice").getValue().toString();
                            String retrievespecialmessage = dataSnapshot.child("specialmessage").getValue().toString();
                            String retrievemonam = dataSnapshot.child("monam").getValue().toString();
                            String retrievemonpm = dataSnapshot.child("monpm").getValue().toString();
                            String retrievetueam = dataSnapshot.child("tueam").getValue().toString();
                            String retrievetuepm = dataSnapshot.child("tuepm").getValue().toString();
                            String retrievewedam = dataSnapshot.child("wedam").getValue().toString();
                            String retrievewedpm = dataSnapshot.child("wedpm").getValue().toString();
                            String retrievethuam = dataSnapshot.child("thuam").getValue().toString();
                            String retrievethupm = dataSnapshot.child("thupm").getValue().toString();
                            String retrievefriam = dataSnapshot.child("friam").getValue().toString();
                            String retrievefripm = dataSnapshot.child("fripm").getValue().toString();
                            String retrievesatam = dataSnapshot.child("satam").getValue().toString();
                            String retrievesatpm = dataSnapshot.child("satpm").getValue().toString();
                            String retrievesunam = dataSnapshot.child("sunam").getValue().toString();
                            String retrievesunpm = dataSnapshot.child("sunpm").getValue().toString();


                            userName.setText(retrieveUserName);
                            userStatus.setText(retrievesStatus);
                            phonenumber.setText(retrievePhoneNumber);
                            workat.setText(retrieveworkat);
                            age.setText(retrieveage);
                            youraddress.setText(retrievesyouraddress);
                            workaddress.setText(retrieveworkaddress);
                            homeservice.setText(retrievehomeservices);
                            specialmessage.setText(retrievespecialmessage);
                            monams.setText(retrievemonam);
                            monpms.setText(retrievemonpm);
                            tueams.setText(retrievetueam);
                            tuepms.setText(retrievetuepm);
                            wedams.setText(retrievewedam);
                            wedpms.setText(retrievewedpm);
                            thuams.setText(retrievethuam);
                            thupms.setText(retrievethupm);
                            friams.setText(retrievefriam);
                            fripms.setText(retrievefripm);
                            satams.setText(retrievesatam);
                            satpms.setText(retrievesatpm);
                            sunams.setText(retrievesunam);
                            sunpms.setText(retrievesunpm);

                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("shopback")) && (dataSnapshot.hasChild("image")))
                        {

                            String retrieveShopBackground = dataSnapshot.child("shopback").getValue().toString();


                            Picasso.get().load(retrieveShopBackground).into(shopbackground);


                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("shopback")))
                        {

                            String retrieveShopBackground = dataSnapshot.child("shopback").getValue().toString();


                            Picasso.get().load(retrieveShopBackground).into(shopbackground);


                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image")))
                        {

                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();



                            Picasso.get().load(retrieveProfileImage).into(userProfileImage);


                        }
                        else
                        {
                            Toast.makeText(SettingsActivity.this, "Please set & update your profile information...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    private void SendUserToHomeActivity()
    {
        Intent homeIntent = new Intent(SettingsActivity.this, HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}
