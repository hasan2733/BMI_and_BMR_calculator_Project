package bd.edu.seu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Auto-login check
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            checkRole(FirebaseAuth.getInstance().getCurrentUser().getUid());
            return;
        }

        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.loginEmail);
        EditText etPass = findViewById(R.id.loginPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnReg = findViewById(R.id.btnGoToRegister);

        btnReg.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(res -> checkRole(res.getUser().getUid()))
                    .addOnFailureListener(e -> Toast.makeText(this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    private void checkRole(String uid) {
        // Updated to use Realtime Database
        FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            if ("admin".equals(user.getRole())) {
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "User data not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "DB Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}