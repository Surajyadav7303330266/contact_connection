package whitehorsecorporation.contactconnection;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;


    public MessageAdapter(List<Messages> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;
        public ImageView messageSenderPicture, messageReceiverPicture;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = (TextView) itemView.findViewById(R.id.sender_messsage_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
        }
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_messages_layout, viewGroup, false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, final int i) {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(i);

        String fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).placeholder(R.drawable.profile_image).into(messageViewHolder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        messageViewHolder.receiverMessageText.setVisibility(View.GONE);
        messageViewHolder.receiverProfileImage.setVisibility(View.GONE);
        messageViewHolder.senderMessageText.setVisibility(View.GONE);
        messageViewHolder.messageSenderPicture.setVisibility(View.GONE);
        messageViewHolder.messageReceiverPicture.setVisibility(View.GONE);


        if (fromMessageType.equals("text")) {
            if (fromUserID.equals(messageSenderId)) {
                messageViewHolder.senderMessageText.setVisibility(View.VISIBLE);

                messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_message_layout);
                messageViewHolder.senderMessageText.setTextColor(Color.BLACK);
                messageViewHolder.senderMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
            } else {
                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);

                messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_layout);
                messageViewHolder.receiverMessageText.setTextColor(Color.BLACK);
                messageViewHolder.receiverMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
            }
        } else if (fromMessageType.equals("image")) {
            if (fromUserID.equals(messageSenderId)) {
                messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(messageViewHolder.messageSenderPicture);
            } else {
                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(messageViewHolder.messageReceiverPicture);
            }
        } else if (fromMessageType.equals("pdf") || (fromMessageType.equals("docx"))) {
            if (fromUserID.equals(messageSenderId)) {
                messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/contact-connection-429b9.appspot.com/o/file.png?alt=media&token=7bcb0792-fb96-4a95-81eb-7ebea6c917ef").into(messageViewHolder.messageSenderPicture);
            } else {
                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/contact-connection-429b9.appspot.com/o/file.png?alt=media&token=7bcb0792-fb96-4a95-81eb-7ebea6c917ef").into(messageViewHolder.messageReceiverPicture);
            }
        }
        if (fromUserID.equals(messageSenderId)) {
            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userMessagesList.get(i).getType().equals("pdf") || userMessagesList.get(i).getType().equals("docx")) {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Delete For me",
                                        "Download and view This document",
                                        "Cancel",
                                        "Delete For Everyone"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message ?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0)
                                {
                                    deleteSentMessages(i,messageViewHolder);
                                   // Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                   // messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                                else if (which == 1)
                                {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(i).getMessage()));
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                } else if (which == 3)
                                {
                                    deleteForEveryoneMessages(i,messageViewHolder);
                                    //  Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    //  messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    } else if (userMessagesList.get(i).getType().equals("text")) {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Delete For me",
                                        "Cancel",
                                        "Delete For Everyone"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message ?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0)
                                {
                                    deleteSentMessages(i,messageViewHolder);
                                    //  Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    //  messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                                else if (which == 2)
                                {
                                    deleteForEveryoneMessages(i,messageViewHolder);
                                    //  Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    //  messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    } else if (userMessagesList.get(i).getType().equals("image")) {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Delete For me",
                                        "View This Image",
                                        "Cancel",
                                        "Delete For Everyone"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message ?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    deleteSentMessages(i,messageViewHolder);
                                    // Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    // messageViewHolder.itemView.getContext().startActivity(intent);
                                } else if (which == 1) {
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), ImageViewerActivity.class);
                                    intent.putExtra("url", userMessagesList.get(i).getMessage());
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                } else if (which == 3) {
                                    deleteForEveryoneMessages(i,messageViewHolder);
                                    //  Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    //   messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                }
            });
        } else {
            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userMessagesList.get(i).getType().equals("pdf") || userMessagesList.get(i).getType().equals("docx")) {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Delete For me",
                                        "Download and view This document",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message ?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    deleteReceiveMessages(i,messageViewHolder);
                                    //    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    //    messageViewHolder.itemView.getContext().startActivity(intent);
                                } else if (which == 1) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(i).getMessage()));
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    } else if (userMessagesList.get(i).getType().equals("text")) {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Delete For me",
                                        "Cancel",
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message ?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    deleteReceiveMessages(i,messageViewHolder);
                                    //   Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    //   messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    } else if (userMessagesList.get(i).getType().equals("image")) {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Delete For me",
                                        "View This Image",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message ?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    deleteReceiveMessages(i,messageViewHolder);
                                    //    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    //    messageViewHolder.itemView.getContext().startActivity(intent);
                                } else if (which == 1) {
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), ImageViewerActivity.class);
                                    intent.putExtra("url", userMessagesList.get(i).getMessage());
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    private void deleteSentMessages(final int i, final MessageViewHolder holder) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages")
                .child(userMessagesList.get(i).getFrom())
                .child(userMessagesList.get(i).getTo())
                .child(userMessagesList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Error Occurred...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void deleteReceiveMessages(final int i, final MessageViewHolder holder) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages")
                .child(userMessagesList.get(i).getTo())
                .child(userMessagesList.get(i).getFrom())
                .child(userMessagesList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Error Occurred...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void deleteForEveryoneMessages(final int i, final MessageViewHolder holder) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages")
                .child(userMessagesList.get(i).getTo())
                .child(userMessagesList.get(i).getFrom())
                .child(userMessagesList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    rootRef.child("Messages")
                            .child(userMessagesList.get(i).getTo())
                            .child(userMessagesList.get(i).getFrom())
                            .child(userMessagesList.get(i).getMessageID())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(holder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Error Occurred...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}


