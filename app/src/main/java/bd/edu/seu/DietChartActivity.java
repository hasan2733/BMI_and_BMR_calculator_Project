package bd.edu.seu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import bd.edu.seu.views.PieChartView;

public class DietChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_chart);

        double bmr = getIntent().getDoubleExtra("BMR", 2000);
        String status = getIntent().getStringExtra("STATUS");
        if (status == null) status = "Normal";

        TextView tvTitle = findViewById(R.id.tvDietTitle);
        TextView tvCal = findViewById(R.id.tvDailyCalories);
        TextView tvBreakdown = findViewById(R.id.tvBreakdown);
        PieChartView pieChart = findViewById(R.id.pieChartView);
        Button btnHistory = findViewById(R.id.btnDietHistory);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        // Update Chart
        if (pieChart != null) pieChart.setDietPlan(status);

        // Update Text
        tvTitle.setText("Plan for: " + status);
        tvCal.setText(String.format("Daily Target: %.0f Calories", bmr * 1.2));

        setTipsAndBreakdown(tvBreakdown, status);
        tvBreakdown.setVisibility(View.VISIBLE);

        // History Button Logic
        if (btnHistory != null) {
            btnHistory.setOnClickListener(v -> {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intent = new Intent(DietChartActivity.this, AdminUserReportActivity.class);
                intent.putExtra("TARGET_UID", uid);
                startActivity(intent);
            });
        }

        // Go Back Button Logic
        if (btnGoBack != null) {
            btnGoBack.setOnClickListener(v -> finish());
        }
    }

    private void setTipsAndBreakdown(TextView tv, String status) {
        String text;
        String tip;
        switch (status) {
            case "Underweight":
                text = "• Increase Carbs (Rice, Potato).\n• Add Healthy Fats (Nuts).";
                tip = "\n💡 HEALTH TIP: Eat 5-6 small meals. Add extra cheese or butter to meals.";
                break;
            case "Overweight":
                text = "• Reduce Rice/Bread.\n• Increase Lean Protein.";
                tip = "\n💡 HEALTH TIP: Walk 30 mins daily. Drink water before meals.";
                break;
            case "Obese":
                text = "• Low Carb.\n• High Fiber Veggies.";
                tip = "\n💡 HEALTH TIP: Try swimming or low-impact cardio. Consult a doctor.";
                break;
            default: // Normal
                text = "• Balanced Diet.\n• Moderate Carbs & Protein.";
                tip = "\n💡 HEALTH TIP: Maintain active lifestyle and drink 3L water.";
                break;
        }
        tv.setText(text + tip);
    }
}