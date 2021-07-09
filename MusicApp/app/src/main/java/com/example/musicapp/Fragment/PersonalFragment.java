package com.example.musicapp.Fragment;

import android.app.Activity;
import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicapp.AuthenticationActivity;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PersonalFragment extends Fragment implements FirebaseReference {
    private View view;
    private FirebaseAuth.AuthStateListener authListener;
    private int MY_REQUEST_CODE = 2009;

    private Button btnLogin;
    private TextView userEmail;
    private ImageView userAvatar;

    FirebaseUser user;

    private void init() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    userEmail.setText(user.getDisplayName());
                    Picasso.get().load(user.getPhotoUrl()).fit().centerCrop().into(userAvatar);
                    btnLogin.setText("Logout");
                }

                if (user == null) {
                    userEmail.setText("Login");
                    btnLogin.setText("Login");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        if (authListener != null) {
            FirebaseAuth.getInstance().addAuthStateListener(authListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);

        userAvatar = (ImageView) view.findViewById(R.id.userAvatar);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        btnLogin = (Button) view.findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Log.d("sign out", "Singing out");
                    FirebaseAuth.getInstance().signOut();
                    return;
                }
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });

        init();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == 2009 ) {
            if (data != null) {
                Bundle mBundle = data.getExtras();
                user = mBundle.getParcelable("user");

                Toast.makeText(view.getContext(), "Login Successful!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
