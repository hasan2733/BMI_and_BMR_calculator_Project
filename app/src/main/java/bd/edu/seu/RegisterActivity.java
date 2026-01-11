package bd.edu.seu;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
      EditText etEmail = findViewById(R.id.regEmail);
      EditText etPass = findViewById(R.id.regPass);
      EditText etAdminKey = findViewById(R.id.etAdminKey);
      CheckBox cbAdmin = findViewById(R.id.cbAdmin);
      RadioGroup rgGender = findViewById(R.id.regGenderGroup);
      TextView tvGoToLogin = findViewById(R.id.tvGoToLogin);
      Button btnRegister = findViewById(R.id.btnRegister);

      tvGoToLogin.setOnClickListener(v->{
          finish();
      });
      if(cbAdmin.isChecked())
      {
          etAdminKey.setVisibility(TextView.VISIBLE);
      }
      btnRegister.setOnClickListener(v->{
          String name = etName.getText().toString().trim();
          String pass = etPass.getText().toString().trim();
          String email = etEmail.getText().toString().trim();
          int genderId = rgGender.getCheckedRadioButtonId();
          if(name.isEmpty() || pass.isEmpty() || email.isEmpty() || genderId==-1)
          {
              Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
              return;
          }
          String gender = R.id.rbMale == genderId? "Male" : "Female";
          String role;
          if(cbAdmin.isChecked())
          {
              String adminKey = etAdminKey.getText().toString().trim();
              if(adminKey.equalsIgnoreCase("TEACHER123"))
              {
                  role = "admin";
              }
              else
              {
                  role = "user";
              }
          } else {
              role = "";
          }
          mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(res -> {
              String uId = res.getUser().getUid();
              User user = new User(uId,name,email,pass,role);
              FirebaseDatabase.getInstance().getReference("users").child(uId).setValue(user).addOnSuccessListener(aVoid ->{
                  Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                  if(role.equalsIgnoreCase("admin"))
                  {
                      startActivity(new Intent(RegisterActivity.this, AdminActivity.class));
                  }
                  else
                  {
                      startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                  }
                  finish();
              }).addOnFailureListener(e-> Toast.makeText(RegisterActivity.this,"DB error: ",Toast.LENGTH_SHORT).show());
          }).addOnFailureListener(e-> Toast.makeText(RegisterActivity.this,"Auth Error: " + e.getMessage(),Toast.LENGTH_SHORT).show());
      });
  }

}