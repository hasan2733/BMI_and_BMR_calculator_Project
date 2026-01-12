package bd.edu.seu;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import bd.edu.seu.models.User;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String ADMIN_SECRET_KEY = "TEACHER123";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        EditText etName = findViewById(R.id.regName);
        EditText etPass = findViewById(R.id.regPass);
        EditText etEmail = findViewById(R.id.regEmail);
        RadioGroup rgGender = findViewById(R.id.regGenderGroup);
        CheckBox cbAdmin = findViewById(R.id.cbAdmin);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvGoToLogin = findViewById(R.id.tvGoToLogin);
        EditText etAdminSecret = findViewById(R.id.etAdminKey);

        cbAdmin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etAdminSecret.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        tvGoToLogin.setOnClickListener(v->{
            finish();
        });

        btnRegister.setOnClickListener(v->{
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            int genderId = rgGender.getCheckedRadioButtonId();
            if(name.isEmpty() || email.isEmpty() || pass.isEmpty() || genderId==-1)
            {
                Toast.makeText(RegisterActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                return;
            }
            String role;
            if(cbAdmin.isChecked())
            {
                String key = etAdminSecret.getText().toString();
                if(key.equalsIgnoreCase(ADMIN_SECRET_KEY))
                {
                    role = "admin";
                }
                else
                {
                    Toast.makeText(this, "Incorrect Admin Key", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                role = "user";
            }

            RadioButton selectedGender = findViewById(genderId);
            String gender = selectedGender.getText().toString();

            mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(res->{
                String uId = mAuth.getUid();
                User user = new User(uId,name,email,gender,role);
                FirebaseDatabase.getInstance().getReference("users").child(uId).setValue(user).addOnSuccessListener(c->{
                    Toast.makeText(RegisterActivity.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                    
                    Intent intent;
                    if ("admin".equals(role)) {
                        intent = new Intent(RegisterActivity.this, AdminActivity.class);
                    } else {
                        intent = new Intent(RegisterActivity.this, MainActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(a->{
                    Toast.makeText(RegisterActivity.this,"Database Error",Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(b->{
                Toast.makeText(RegisterActivity.this,"Failure: " + b.getMessage(),Toast.LENGTH_SHORT).show();
            });

        });
    }
}