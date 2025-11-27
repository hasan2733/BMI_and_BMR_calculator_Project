package bd.edu.seu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import bd.edu.seu.models.BmiRecord;
import bd.edu.seu.models.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userGender = "";
    private double lastBmr = 0;
    private String lastStatus = "";

    // Inputs
    private RadioGroup rgWeight, rgHeight;
    private EditText etAge, etWeight, etCm, etFt, etIn;
    private LinearLayout layCm, layFt;
    private TextView tvBmi, tvBmr, tvStatus, tvTips, tvWelcome;
    private Button btnDiet, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Bind Views
        tvWelcome = findViewById(R.id.tvWelcome);
        tvBmi = findViewById(R.id.tvResultBmi);
        tvBmr = findViewById(R.id.tvResultBmr);
        tvStatus = findViewById(R.id.tvResultSuggestion);
        tvTips = findViewById(R.id.tvHealthTips);

        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etCm = findViewById(R.id.etHeightCm);
        etFt = findViewById(R.id.etHeightFt);
        etIn = findViewById(R.id.etHeightIn);

        rgWeight = findViewById(R.id.rgWeightUnit);
        rgHeight = findViewById(R.id.rgHeightUnit);
        layCm = findViewById(R.id.layoutHeightCm);
        layFt = findViewById(R.id.layoutHeightFt);

        btnDiet = findViewById(R.id.btnDietChart);
        btnHistory = findViewById(R.id.btnHistory);

        loadUser();

        // Toggles
        rgHeight.setOnCheckedChangeListener((g, id) -> {
            layCm.setVisibility(id == R.id.rbCm ? View.VISIBLE : View.GONE);
            layFt.setVisibility(id == R.id.rbCm ? View.GONE : View.VISIBLE);
        });

        rgWeight.setOnCheckedChangeListener((g, id) -> {
            etWeight.setHint(id == R.id.rbKg ? "Weight (Kg)" : "Weight (Lbs)");
        });

        findViewById(R.id.btnCalculate).setOnClickListener(v -> calculate());

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Open Diet Chart
        btnDiet.setOnClickListener(v -> {
            if(lastBmr == 0) return;
            Intent i = new Intent(this, DietChartActivity.class);
            i.putExtra("BMR", lastBmr);
            i.putExtra("STATUS", lastStatus);
            startActivity(i);
        });

        // Open History
        btnHistory.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, AdminUserReportActivity.class);
                intent.putExtra("TARGET_UID", mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });
    }

    private void calculate() {
        if(userGender.isEmpty()) { Toast.makeText(this, "Loading user info...", Toast.LENGTH_SHORT).show(); return; }
        try {
            double age = Double.parseDouble(etAge.getText().toString());
            double wVal = Double.parseDouble(etWeight.getText().toString());

            double wKg = (rgWeight.getCheckedRadioButtonId() == R.id.rbKg) ? wVal : wVal / 2.20462;
            double hM, hCm;

            if (rgHeight.getCheckedRadioButtonId() == R.id.rbCm) {
                hCm = Double.parseDouble(etCm.getText().toString());
                hM = hCm / 100.0;
            } else {
                double ft = etFt.getText().toString().isEmpty() ? 0 : Double.parseDouble(etFt.getText().toString());
                double in = etIn.getText().toString().isEmpty() ? 0 : Double.parseDouble(etIn.getText().toString());
                hM = ((ft * 12) + in) * 0.0254;
                hCm = hM * 100;
            }

            double bmi = wKg / (hM * hM);
            double bmr = (userGender.equalsIgnoreCase("Male"))
                    ? (10 * wKg + 6.25 * hCm - 5 * age + 5)
                    : (10 * wKg + 6.25 * hCm - 5 * age - 161);

            String status = getStatus(bmi);
            String tips = getTips(bmi);

            tvBmi.setText(String.format("BMI: %.2f", bmi));
            tvBmr.setText(String.format("BMR: %.0f", bmr));
            tvStatus.setText("Status: " + status);
            tvTips.setText(tips);

            lastBmr = bmr;
            lastStatus = status;
            btnDiet.setVisibility(View.VISIBLE);

            saveRecord(bmi, bmr, status, tips);

        } catch (Exception e) { Toast.makeText(this, "Check Inputs", Toast.LENGTH_SHORT).show(); }
    }

    private void saveRecord(double bmi, double bmr, String s, String tips) {
        if(mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();
        String key = mDatabase.child("history").child(uid).push().getKey();
        BmiRecord rec = new BmiRecord(key, bmi, bmr, s, tips, System.currentTimeMillis());
        mDatabase.child("history").child(uid).child(key).setValue(rec);
    }

    private void loadUser() {
        if(mAuth.getCurrentUser() != null) {
            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(s -> {
                User u = s.getValue(User.class);
                if(u != null) {
                    tvWelcome.setText("Hi " + u.getName());
                    userGender = u.getGender();
                }
            });
        }
    }

    private String getStatus(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    private String getTips(double bmi) {
        if (bmi < 18.5) return "Eat more frequently.";
        if (bmi < 25) return "Maintain activity.";
        if (bmi < 30) return "Cut sugar.";
        return "Consult a doctor.";
    }
}