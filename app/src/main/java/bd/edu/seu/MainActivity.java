package bd.edu.seu;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bd.edu.seu.models.BmiRecord;
import bd.edu.seu.models.User;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    private String userGender = "";
    private String lastStatus = "";
    private double lastBmr = 0.0;
    private RadioGroup rgWeightUnit,rgHeightUnit;
    private EditText etAge,etWeight,etCm,etFt,etIn;
    private TextView tvBmi,tvBmr,tvStatus,tvWelcome;
    private LinearLayout layCm,layFt;
    Button btnCalculate,btnHistory,btnLogout,btnDietChart,btnViewTips;

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        tvWelcome = findViewById(R.id.tvWelcome);
        tvBmi = findViewById(R.id.tvResultBmi);
        tvBmr = findViewById(R.id.tvResultBmr);
        tvStatus = findViewById(R.id.tvResultSuggestion);

        layCm = findViewById(R.id.layoutHeightCm);
        layFt = findViewById(R.id.layoutHeightFt);

        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etCm = findViewById(R.id.etHeightCm);
        etFt = findViewById(R.id.etHeightFt);
        etIn = findViewById(R.id.etHeightIn);

        rgWeightUnit = findViewById(R.id.rgWeightUnit);
        rgHeightUnit = findViewById(R.id.rgHeightUnit);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnHistory = findViewById(R.id.btnHistory);
        btnLogout = findViewById(R.id.btnLogout);
        btnDietChart = findViewById(R.id.btnDietChart);
        btnViewTips = findViewById(R.id.btnViewTips);

        load();

        rgHeightUnit.setOnCheckedChangeListener((g,id)->{
            if(id == R.id.rbCm)
            {
                layCm.setVisibility(View.VISIBLE);
                layFt.setVisibility(View.GONE);
            }
            else
            {
                layCm.setVisibility(View.GONE);
                layFt.setVisibility(View.VISIBLE);
            }
        });

        rgWeightUnit.setOnCheckedChangeListener((g,id)->{
            if(id == R.id.rbKg)
            {
                etWeight.setHint("Weight (kg)");
            }
            else
            {
                etWeight.setHint("Weight (lb)");
            }
        });

        btnHistory.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this,AdminUserReportActivity.class));
        });

        btnHistory.setOnClickListener(v->{
            mAuth.signOut();
            finish();
        });

        btnCalculate.setOnClickListener(v->{
            calculate();
        });

        //Diet Chart Button
        btnDietChart.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this,DietChartActivity.class);
            intent.putExtra("BMR",lastBmr);
            intent.putExtra("STATUS",lastStatus);
            startActivity(intent);
        });

        //history Button
        btnHistory.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this,AdminUserReportActivity.class);
            i.putExtra("TARGET_UID",mAuth.getUid());
            startActivity(i);
        });


        //View Tips Button
        btnViewTips.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Personalized Diet Tips");
            String message = "";
            switch (lastStatus)
            {
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
            builder.setPositiveButton("OK",null);
            builder.show();
        });

        btnLogout.setOnClickListener(v->{
            mAuth.signOut();
            finish();
        });
    }
    private void load()
    {
        if(mAuth.getCurrentUser() != null)
        {
            mDatabase.getReference("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user!=null)
                    {
                        tvWelcome.setText("Welcome, " + user.getName());
                        userGender = user.getGender();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvWelcome.setText("Error: " + error.getMessage());
                }
            });
        }
    }

    private void calculate()
    {
        if(userGender.isEmpty())
        {
            Toast.makeText(MainActivity.this,"Please login first",Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            double age = Double.parseDouble(etAge.getText().toString().trim());
            double weight = Double.parseDouble(etWeight.getText().toString().trim());
            double wKg = (rgWeightUnit.getCheckedRadioButtonId() == R.id.rbKg) ? weight : weight * 0.453592;
            double hM,hCm=0.0;
            if(R.id.rbCm == rgHeightUnit.getCheckedRadioButtonId())
            {
                hCm = Double.parseDouble(etCm.getText().toString().trim());
                hM = hCm/100;
            }
            else
            {
                double ft = Double.parseDouble(etFt.getText().toString().trim());
                double in = Double.parseDouble(etIn.getText().toString().trim());
                hM = (ft*0.3048) + (in*0.0254);
                hCm = hM*100;
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

            double bmi = wKg/(hM*hM);
            double bmr = userGender.equalsIgnoreCase("Male")
                    ? 66.47 + (13.75 * wKg) + (5.003 * hCm) - (6.755 * age)
                    : 655.1 + (9.563 * wKg) + (1.85 * hCm) - (4.676 * age);

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
            tvBmi.setText("BMI: " + String.format("%.2f",bmi));
            tvBmr.setText("BMR: " + String.format("%.2f",bmr));
            tvStatus.setText("Status: " + status);
            lastStatus = status;
            lastBmr = bmr;
            appendWeightGoal(wKg,hM);
            btnDietChart.setVisibility(LinearLayout.VISIBLE);
            btnViewTips.setVisibility(LinearLayout.VISIBLE);
            save(bmi,bmr,status);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Calculates the ideal weight range and appends target goals to the UI.
     */
    private void appendWeightGoal(double currentKg, double hM)
    {
        // Ideal BMI range: 18.5 to 24.9
        double minWeight = 18.5 * (hM * hM);
        double maxWeight = 24.9 * (hM * hM);

        StringBuilder goalMsg = new StringBuilder();
        goalMsg.append("\n\nIdeal Weight: ").append(String.format("%.1f", minWeight))
                .append(" - ").append(String.format("%.1f", maxWeight)).append(" Kg");

        if (currentKg > maxWeight) {
            double diff = currentKg - maxWeight;
            goalMsg.append("\nGoal: Lose ").append(String.format("%.1f", diff)).append(" Kg");
        } else if (currentKg < minWeight) {
            double diff = minWeight - currentKg;
            goalMsg.append("\nGoal: Gain ").append(String.format("%.1f", diff)).append(" Kg");
        } else {
            goalMsg.append("\nGoal: You are at a perfect weight!");
        }

        tvStatus.append(goalMsg.toString());
    }
    private void save(double bmi,double bmr,String status)
    {
        String key = mDatabase.getReference("history").child(mAuth.getCurrentUser().getUid()).push().getKey();
        if(key!=null)
        {
            mDatabase.getReference("history").child(mAuth.getCurrentUser().getUid()).child(key).setValue(new BmiRecord(key,bmi,bmr,"",status,System.currentTimeMillis()))
                    .addOnSuccessListener(v-> Toast.makeText(MainActivity.this,"Saved",Toast.LENGTH_SHORT).show()).addOnFailureListener(v->
                            Toast.makeText(MainActivity.this,"Error: " + v.getMessage(),Toast.LENGTH_SHORT).show());
        }
    }
}