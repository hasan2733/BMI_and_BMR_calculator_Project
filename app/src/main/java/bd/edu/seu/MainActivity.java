package bd.edu.seu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private Button btnDiet, btnHistory, btnViewTips;

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
        btnViewTips = findViewById(R.id.btnViewTips);

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

        btnViewTips.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Personalized Health Tips");

            String message = "";
            switch (lastStatus) {
                case "Underweight":
                    message = "1. Eat more frequently and choose nutrient-rich foods.\n2. Add healthy snacks between meals.\n3. Incorporate strength training to build muscle mass.";
                    break;
                case "Healthy Weight":
                    message = "1. Maintain a balanced diet and regular exercise routine.\n2. Ensure you get 7-8 hours of quality sleep.\n3. Stay hydrated throughout the day.";
                    break;
                case "Overweight":
                    message = "1. Focus on portion control and mindful eating.\n2. Increase physical activity, aiming for 30-60 minutes most days.\n3. Limit processed foods and sugary drinks.";
                    break;
                case "Obese":
                    message = "1. It is highly recommended to consult a doctor or a registered dietitian.\n2. Focus on a balanced, calorie-controlled diet.\n3. Incorporate regular, moderate-intensity exercise into your routine.";
                    break;
                default:
                    message = "Please calculate your BMI first to get personalized tips.";
                    break;
            }

            builder.setMessage(message);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
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

            // age validation
            if(age < 5 || age > 120)
            {
                if(age < 5)
                    Toast.makeText(this, "Age must be greater than 5", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Age must be less than 120", Toast.LENGTH_SHORT).show();
                return;
            }

            // weight validation
            if(age < 10)
            {
                if(wKg < 10 || wKg > 50)
                {
                    Toast.makeText(this, "Invalid weight for child age!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(age <= 20)
            {
                if(wKg <30 || wKg >90)
                {
                    Toast.makeText(this, "Invalid weight for teenager age!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(age <= 60)
            {
                if(wKg < 40 || wKg > 150)
                {
                    Toast.makeText(this, "Invalid weight for adult age!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(age > 60 )
            {
                if(wKg < 35 || wKg > 120)
                {
                    Toast.makeText(this, "Invalid weight for senior age!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Height validation
            if(age < 10 )
            {
                if(hCm < 70 || hCm > 150)
                {
                    Toast.makeText(this, "Invalid height for child age!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(age <= 20 )
            {
                if(hCm < 120 || hCm > 190)
                {
                    Toast.makeText(this, "Invalid height for teenager age!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(age <= 60 )
            {
                if(hCm < 140 || hCm > 210)
                {
                    Toast.makeText(this, "Invalid height for adult age!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if (age > 60 )
            {
                if(hCm < 130 || hCm > 200)
                {
                    Toast.makeText(this, "Invalid height for senior age!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            double bmi = wKg / (hM * hM);
            double bmr = (userGender.equalsIgnoreCase("Male"))
                    ? (10 * wKg + 6.25 * hCm - 5 * age + 5)
                    : (10 * wKg + 6.25 * hCm - 5 * age - 161);

            String status;
            if (bmi < 18.5) {
                status = "Underweight";
                tvStatus.setTextColor(Color.YELLOW);
            } else if (bmi >= 18.5 && bmi < 24.9) {
                status = "Healthy Weight";
                tvStatus.setTextColor(Color.GREEN);
            } else if (bmi >= 24.9 && bmi < 30) {
                status = "Overweight";
                tvStatus.setTextColor(Color.parseColor("#FFA500"));
            } else {
                status = "Obese";
                tvStatus.setTextColor(Color.RED);
            }

            tvBmi.setText(String.format("BMI: %.2f", bmi));
            tvBmr.setText(String.format("BMR: %.0f", bmr));
            tvStatus.setText("Status: " + status);

            lastBmr = bmr;
            lastStatus = status;
            btnDiet.setVisibility(View.VISIBLE);
            btnViewTips.setVisibility(View.VISIBLE);

            saveRecord(bmi, bmr, status);

        } catch (Exception e) { Toast.makeText(this, "Check Inputs", Toast.LENGTH_SHORT).show(); }
    }

    private void saveRecord(double bmi, double bmr, String s) {
        if(mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();
        String key = mDatabase.child("history").child(uid).push().getKey();
        BmiRecord rec = new BmiRecord(key, bmi, bmr, s, "", System.currentTimeMillis());
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
}
