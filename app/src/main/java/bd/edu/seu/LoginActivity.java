package bd.edu.seu;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bd.edu.seu.models.User;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);


        setContentView(R.layout.activity_login);

        if(mAuth.getCurrentUser()!=null)
        {
            checkRole(mAuth.getUid());
            return;
        }


        EditText etEmail = findViewById(R.id.loginEmail);
        EditText etPass = findViewById(R.id.loginPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        Button btnGoToRegister = findViewById(R.id.btnGoToRegister);

        tvForgotPassword.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        btnGoToRegister.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        });

        btnLogin.setOnClickListener(v->{
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            if(email.isEmpty() || pass.isEmpty())
            {
                Toast.makeText(LoginActivity.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(res -> {
                checkRole(mAuth.getUid());
            }).addOnFailureListener(e-> Toast.makeText(LoginActivity.this,"Login Error: " + e.getMessage(),Toast.LENGTH_SHORT).show());
        });

    }

    private void checkRole(String uId)
    {
        mDatabase.getReference("users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user!=null)
                {
                    if (user.getRole().equalsIgnoreCase("admin")) {
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"User not found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this,"DB Error: " + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}