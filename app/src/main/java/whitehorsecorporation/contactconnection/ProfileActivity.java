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
import android.view.View;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
{
    private String receiverUserID, senderUserID, Current_State;

    private CircleImageView userProfileImage;
    private ImageView usershopbackgroundimg;
    private TextView userProfileName, userProfileStatus,userphonenumber,userworksat,usersage,userhomeaddress,userworkaddress,
    userhomeservice,profilespecialmessage,monamp,monpmp,tueamp,tuepmp,wedamp,wedpmp,thuamp,thupmp,friamp,fripmp,satamp,satpmp,sunamp,sunpmp;
    private Button SendMessageRequestButton, DeclineMessageRequestButton,callButton;
    private DatabaseReference UserRef, ChatRequestRef, ContactsRef, NotificationRef;
    private FirebaseAuth mAuth;
    private static final int REQUEST_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        NotificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");


        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();


        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_image);
        usershopbackgroundimg = (ImageView) findViewById(R.id.profileshopback);
        userProfileName = (TextView) findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_status);
        userphonenumber = (TextView) findViewById(R.id.visit_phonenumber_status);
        callButton = (Button)findViewById(R.id.call_button);
        userworksat = (TextView) findViewById(R.id.visit_workat_status);
        usersage = (TextView) findViewById(R.id.visit_age_status);
        userhomeaddress = (TextView) findViewById(R.id.visit_youraddress_status);
        userworkaddress = (TextView) findViewById(R.id.visit_workaddress_status);
        userhomeservice = (TextView)findViewById(R.id.visit_homeservice_status);
        profilespecialmessage = (TextView)findViewById(R.id.visit_special_message_status);
        monamp = (TextView)findViewById(R.id.monamt);
        monpmp = (TextView)findViewById(R.id.monpmt);
        tueamp = (TextView)findViewById(R.id.tueamt);
        tuepmp = (TextView)findViewById(R.id.tuepmt);
        wedamp = (TextView)findViewById(R.id.wedamt);
        wedpmp = (TextView)findViewById(R.id.wedpmt);
        thuamp = (TextView)findViewById(R.id.thuamt);
        thupmp = (TextView)findViewById(R.id.thupmt);
        friamp = (TextView)findViewById(R.id.friamt);
        fripmp = (TextView)findViewById(R.id.fripmt);
        satamp = (TextView)findViewById(R.id.satamt);
        satpmp = (TextView)findViewById(R.id.satpmt);
        sunamp = (TextView)findViewById(R.id.sunamt);
        sunpmp = (TextView)findViewById(R.id.sunpmt);
        SendMessageRequestButton = (Button) findViewById(R.id.send_message_request_button);
        DeclineMessageRequestButton = (Button) findViewById(R.id.decline_message_request_button);
        Current_State = "new";


        RetrieveUserInfo();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }



    private void RetrieveUserInfo()
    {
        UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if ((dataSnapshot.exists())  &&  (dataSnapshot.hasChild("image"))  &&  (dataSnapshot.hasChild("shopback")))
                {
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    String usershopback = dataSnapshot.child("shopback").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();
                    String userPhone = dataSnapshot.child("phonenumber").getValue().toString();
                    String userworkat = dataSnapshot.child("workat").getValue().toString();
                    String userage = dataSnapshot.child("age").getValue().toString();
                    String userhomeaddresses = dataSnapshot.child("youraddress").getValue().toString();
                    String userworkaddresses = dataSnapshot.child("workaddress").getValue().toString();
                    String userhomeservices = dataSnapshot.child("homeservice").getValue().toString();
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

                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                    Picasso.get().load(usershopback).placeholder(R.drawable.shopbackground).into(usershopbackgroundimg);
                    userProfileName.setText(userName);
                    userProfileStatus.setText(userstatus);
                    userphonenumber.setText(userPhone);
                    userworksat.setText(userworkat);
                    usersage.setText(userage);
                    userhomeaddress.setText(userhomeaddresses);
                    userworkaddress.setText(userworkaddresses);
                    userhomeservice.setText(userhomeservices);
                    profilespecialmessage.setText(retrievespecialmessage);
                    monamp.setText(retrievemonam);
                    monpmp.setText(retrievemonpm);
                    tueamp.setText(retrievetueam);
                    tuepmp.setText(retrievetuepm);
                    wedamp.setText(retrievewedam);
                    wedpmp.setText(retrievewedpm);
                    thuamp.setText(retrievethuam);
                    thupmp.setText(retrievethupm);
                    friamp.setText(retrievefriam);
                    fripmp.setText(retrievefripm);
                    satamp.setText(retrievesatam);
                    satpmp.setText(retrievesatpm);
                    sunamp.setText(retrievesunam);
                    sunpmp.setText(retrievesunpm);

                    ManageChatRequests();
                }
                else
                if ((dataSnapshot.exists()) &&  (dataSnapshot.hasChild("image")))
                {
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();
                    String userPhone = dataSnapshot.child("phonenumber").getValue().toString();
                    String userworkat = dataSnapshot.child("workat").getValue().toString();
                    String userage = dataSnapshot.child("age").getValue().toString();
                    String userhomeaddresses = dataSnapshot.child("youraddress").getValue().toString();
                    String userworkaddresses = dataSnapshot.child("workaddress").getValue().toString();
                    String userhomeservices = dataSnapshot.child("homeservice").getValue().toString();
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

                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                    userProfileName.setText(userName);
                    userProfileStatus.setText(userstatus);
                    userphonenumber.setText(userPhone);
                    userworksat.setText(userworkat);
                    usersage.setText(userage);
                    userhomeaddress.setText(userhomeaddresses);
                    userworkaddress.setText(userworkaddresses);
                    userhomeservice.setText(userhomeservices);
                    profilespecialmessage.setText(retrievespecialmessage);
                    monamp.setText(retrievemonam);
                    monpmp.setText(retrievemonpm);
                    tueamp.setText(retrievetueam);
                    tuepmp.setText(retrievetuepm);
                    wedamp.setText(retrievewedam);
                    wedpmp.setText(retrievewedpm);
                    thuamp.setText(retrievethuam);
                    thupmp.setText(retrievethupm);
                    friamp.setText(retrievefriam);
                    fripmp.setText(retrievefripm);
                    satamp.setText(retrievesatam);
                    satpmp.setText(retrievesatpm);
                    sunamp.setText(retrievesunam);
                    sunpmp.setText(retrievesunpm);

                    ManageChatRequests();
                }
                else
                {

                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();
                    String userPhone = dataSnapshot.child("phonenumber").getValue().toString();
                    String userworkat = dataSnapshot.child("workat").getValue().toString();
                    String userage = dataSnapshot.child("age").getValue().toString();
                    String userhomeaddresses = dataSnapshot.child("youraddress").getValue().toString();
                    String userworkaddresses = dataSnapshot.child("workaddress").getValue().toString();
                    String userhomeservices = dataSnapshot.child("homeservice").getValue().toString();
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

                    userProfileName.setText(userName);
                    userProfileStatus.setText(userstatus);
                    userphonenumber.setText(userPhone);
                    userworksat.setText(userworkat);
                    usersage.setText(userage);
                    userhomeaddress.setText(userhomeaddresses);
                    userworkaddress.setText(userworkaddresses);
                    userhomeservice.setText(userhomeservices);
                    profilespecialmessage.setText(retrievespecialmessage);
                    monamp.setText(retrievemonam);
                    monpmp.setText(retrievemonpm);
                    tueamp.setText(retrievetueam);
                    tuepmp.setText(retrievetuepm);
                    wedamp.setText(retrievewedam);
                    wedpmp.setText(retrievewedpm);
                    thuamp.setText(retrievethuam);
                    thupmp.setText(retrievethupm);
                    friamp.setText(retrievefriam);
                    fripmp.setText(retrievefripm);
                    satamp.setText(retrievesatam);
                    satpmp.setText(retrievesatpm);
                    sunamp.setText(retrievesunam);
                    sunpmp.setText(retrievesunpm);


                    ManageChatRequests();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void ManageChatRequests()
    {
        ChatRequestRef.child(senderUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.hasChild(receiverUserID))
                        {
                            String request_type = dataSnapshot.child(receiverUserID).child("request_type").getValue().toString();

                            if (request_type.equals("sent"))
                            {
                                Current_State = "request_sent";
                                SendMessageRequestButton.setText("Cancel Chat Request");
                            }
                            else if (request_type.equals("received"))
                            {
                                Current_State = "request_received";
                                SendMessageRequestButton.setText("Accept Chat Request");

                                DeclineMessageRequestButton.setVisibility(View.VISIBLE);
                                DeclineMessageRequestButton.setEnabled(true);

                                DeclineMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        CancelChatRequest();
                                    }
                                });
                            }
                        }
                        else
                        {
                            ContactsRef.child(senderUserID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot)
                                        {
                                            if (dataSnapshot.hasChild(receiverUserID))
                                            {
                                                Current_State = "friends";
                                                SendMessageRequestButton.setText("Remove this Contact");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        if (!senderUserID.equals(receiverUserID))
        {
            SendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    SendMessageRequestButton.setEnabled(false);

                    if (Current_State.equals("new"))
                    {
                        SendChatRequest();
                    }
                    if (Current_State.equals("request_sent"))
                    {
                        CancelChatRequest();
                    }
                    if (Current_State.equals("request_received"))
                    {
                        AcceptChatRequest();
                    }
                    if (Current_State.equals("friends"))
                    {
                        RemoveSpecificContact();
                    }
                }
            });
        }
        else
        {
            SendMessageRequestButton.setVisibility(View.INVISIBLE);
        }
    }



    private void RemoveSpecificContact()
    {
        ContactsRef.child(senderUserID).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ContactsRef.child(receiverUserID).child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                SendMessageRequestButton.setEnabled(true);
                                                Current_State = "new";
                                                SendMessageRequestButton.setText("Send Message");

                                                DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                DeclineMessageRequestButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }



    private void AcceptChatRequest()
    {
        ContactsRef.child(senderUserID).child(receiverUserID)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ContactsRef.child(receiverUserID).child(senderUserID)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                ChatRequestRef.child(senderUserID).child(receiverUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    ChatRequestRef.child(receiverUserID).child(senderUserID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    SendMessageRequestButton.setEnabled(true);
                                                                                    Current_State = "friends";
                                                                                    SendMessageRequestButton.setText("Remove this Contact");

                                                                                    DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                                                    DeclineMessageRequestButton.setEnabled(false);
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }




    private void CancelChatRequest()
    {
        ChatRequestRef.child(senderUserID).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ChatRequestRef.child(receiverUserID).child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                SendMessageRequestButton.setEnabled(true);
                                                Current_State = "new";
                                                SendMessageRequestButton.setText("Send Message");

                                                DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                DeclineMessageRequestButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private void makePhoneCall() {
        String number = userphonenumber.getText().toString();
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(ProfileActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SendChatRequest()
    {
        ChatRequestRef.child(senderUserID).child(receiverUserID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ChatRequestRef.child(receiverUserID).child(senderUserID)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                chatNotificationMap.put("from", senderUserID);
                                                chatNotificationMap.put("type", "request");

                                                NotificationRef.child(receiverUserID).push()
                                                        .setValue(chatNotificationMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    SendMessageRequestButton.setEnabled(true);
                                                                    Current_State = "request_sent";
                                                                    SendMessageRequestButton.setText("Cancel Chat Request");
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
