package bd.edu.seu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private static final String ADMIN_SECRET = "TEACHER123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        EditText etName = findViewById(R.id.regName);
        EditText etEmail = findViewById(R.id.regEmail);
        EditText etPass = findViewById(R.id.regPass);
        EditText etAdminKey = findViewById(R.id.etAdminKey);
        RadioGroup rgGender = findViewById(R.id.regGenderGroup);
        CheckBox cbAdmin = findViewById(R.id.cbAdmin);
        Button btnReg = findViewById(R.id.btnRegister);
        TextView tvLogin = findViewById(R.id.tvGoToLogin);

        cbAdmin.setOnCheckedChangeListener((v, isChecked) ->
                etAdminKey.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        tvLogin.setOnClickListener(v -> finish());

        btnReg.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            int genderId = rgGender.getCheckedRadioButtonId();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || genderId == -1) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = "user";
            if (cbAdmin.isChecked()) {
                if (!etAdminKey.getText().toString().equals(ADMIN_SECRET)) {
                    Toast.makeText(this, "Invalid Admin Key", Toast.LENGTH_SHORT).show();
                    return;
                }
                role = "admin";
            }

            String gender = (genderId == R.id.rbMale) ? "Male" : "Female";
            String finalRole = role;

            mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(res -> {
                String uid = res.getUser().getUid();
                User user = new User(uid, name, email, gender, finalRole);

                // SAVE TO REALTIME DATABASE
                FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            if(finalRole.equals("admin")) startActivity(new Intent(this, AdminActivity.class));
                            else startActivity(new Intent(this, MainActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "DB Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }).addOnFailureListener(e -> Toast.makeText(this, "Auth Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}