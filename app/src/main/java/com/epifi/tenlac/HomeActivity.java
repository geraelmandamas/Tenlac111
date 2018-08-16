package com.epifi.tenlac;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private Context mContext;
    private ImageView IconChat;
    private TextView TVDialog;
    private Button Btn1Dialog,BtnLogin,Btn2Dialog,BtnRegister;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myref;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = HomeActivity.this;


        myref = FirebaseDatabase.getInstance().getReference();





        IconChat = (ImageView) findViewById(R.id.IconChat);


        setUpFirebaseAuth();

    }
    private void setUpFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUser(user);
                if (user != null) {
                    //userID = mAuth.getCurrentUser().getUid();
                    userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference uidRef = rootRef.child("userspuntos").child(userID);
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
    private void checkCurrentUser(final FirebaseUser user){

        IconChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    setUpAuthetication();


                }else{
                    Intent l = new Intent(mContext, CargandoActivity.class);
                    startActivity(l);
                }
            }

        });



    }
    private void setUpAuthetication(){
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(mContext);
        View mView = getLayoutInflater().inflate(R.layout.dialog_elegir,null);
        TVDialog = (TextView) mView.findViewById(R.id.TVRegistrate);
        Btn1Dialog = (Button) mView.findViewById(R.id.BtnLogin);
        Btn2Dialog = (Button) mView.findViewById(R.id.BtnRegister);

        //Actions for Btn1Dialog
        Btn1Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);

            }
        });
        //Actions for Btn2Dialog
        Btn2Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RegisterActivity.class);
                startActivity(intent);

            }
        });

        alertDialogbuilder.setView(mView);
        AlertDialog dialog = alertDialogbuilder.create();
        dialog.show();



    }
}
