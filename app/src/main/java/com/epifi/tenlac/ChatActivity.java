package com.epifi.tenlac;

///Mis libros
// 1 Ghost fleet
//2 La luna es una curel amante


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private ImageView IconBack;
    private Context mContext;
    private static final String TAG = "HomeActivity";
    private static final int Activity_NUM = 0;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myref,myEmail;
    private String userID;
    private EditText ET1;
    private ImageView BtnSend;
    private String emailUsuario;
    LinearLayout layout;
    RelativeLayout layout_2;
    ScrollView scrollView;
    //Firebase Add
    private AdView mAdView;
    private Boolean botonEnvio = false;
    //Design
    private RecyclerView RVChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mContext = ChatActivity.this;
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        setUpFirebaseAuth();
        BtnSend = (ImageView) findViewById(R.id.BtnSend);
        ET1 = (EditText) findViewById(R.id.ET1);
        IconBack = (ImageView) findViewById(R.id.IconBackChat);
        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent q = new Intent(mContext,CargandoActivity.class);
                startActivity(q);


            }
        });
        myref = FirebaseDatabase.getInstance().getReference();

        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStringNull(ET1.getText().toString())){
                    Toast.makeText(mContext,"Introduzca texto",Toast.LENGTH_SHORT).show();
                }else {
                    userID = mAuth.getCurrentUser().getUid();
                    botonEnvio = true;
                    Mensaje mensaje = new Mensaje(ET1.getText().toString(),emailUsuario);
                    // myref.child(userID).child("listas").push().setValue(lista);
                    DatabaseReference newActividad = myref.child("users").child(userID).child("mensajes").push();
                    newActividad.setValue(mensaje);
                    ET1.setText("");
                }
            }
        });





        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                String mensage = dataSnapshot.child("users").child(userID).child("mensajes").child("mensaje").getValue(String.class);
                    String sendBy = dataSnapshot.child("users").child(userID).child("mensajes").child("sendBy").getValue(String.class);

                    if (sendBy == emailUsuario){
                        addMessageBox(sendBy + mensage, 1);

                    }else {

                        addMessageBox(sendBy + mensage, 2);



                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                emailUsuario = dataSnapshot.child("users").child(userID).child("email").getValue(String.class);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









    }
    private void setUpFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUser(user);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }


            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Check if the "user " is signed in
    private void checkCurrentUser(FirebaseUser user){
        if (user == null ){
            Intent intent = new Intent(mContext,LoginActivity.class);
            startActivity(intent);

        }

    }
    private boolean isStringNull(String string){
        if (string.equals("")){
            return true;
        }else {
            return false;
        }

    }
    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.buble_chat);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.buble_chat);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
