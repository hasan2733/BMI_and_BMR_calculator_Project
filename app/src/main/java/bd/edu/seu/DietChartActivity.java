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
        TextView tvHealthTip = findViewById(R.id.tvHealthTip);
        PieChartView pieChart = findViewById(R.id.pieChartView);
        Button btnHistory = findViewById(R.id.btnDietHistory);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        // Update Chart
        if (pieChart != null) pieChart.setDietPlan(status);

        // Update Text
        tvTitle.setText("Plan for: " + status);
        tvCal.setText(String.format("Daily Target: %.0f Calories", bmr * 1.2));

        setTipsAndBreakdown(tvBreakdown, tvHealthTip, status);

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

    private void setTipsAndBreakdown(TextView tvBreakdown, TextView tvTip, String status) {
        String text;
        String tip;
        switch (status) {
            case "Underweight":
                text = "• Increase calorie intake with nutrient-dense foods.\n" +
                        "• Include complex carbs: brown rice, sweet potatoes, oats.\n" +
                        "• Add healthy fats: avocados, nuts, seeds, olive oil.\n" +
                        "• Eat protein-rich foods: eggs, dairy, lean meat, legumes.";
                tip = "💡 Aim for 5–6 smaller meals throughout the day. " +
                        "Add calorie boosters like peanut butter, cheese, or full-fat yogurt to meals.";
                break;
            case "Overweight":
                text = "• Focus on portion control and whole foods.\n" +
                        "• Reduce refined carbs (white rice, bread, sugary items).\n" +
                        "• Increase lean protein: chicken, fish, tofu, eggs.\n" +
                        "• Fill half your plate with non-starchy vegetables.";
                tip = "💡 Drink a glass of water 15–20 minutes before meals. " +
                        "Walk 30–45 minutes daily and aim for consistent, enjoyable activity.";
                break;
            case "Obese":
                text = "• Prioritize low-calorie, high-volume foods.\n" +
                        "• Choose high-fiber vegetables and moderate lean protein.\n" +
                        "• Limit processed carbs and sugary drinks.\n" +
                        "• Focus on sustainable, gradual changes.";
                tip = "💡 Start with low-impact exercises like walking or swimming. " +
                        "Consult a doctor or dietitian before making major changes.";
                break;
            default: // Normal
                text = "• Maintain a balanced and varied diet.\n" +
                        "• Include a mix of complex carbs, lean proteins, and healthy fats.\n" +
                        "• Eat plenty of fruits and vegetables daily.\n" +
                        "• Practice mindful portion sizes.";
                tip = "💡 Stay active with regular exercise you enjoy. " +
                        "Aim for at least 2–3 liters of water daily and prioritize sleep.";
                break;
        }
        tvBreakdown.setText(text);
        tvTip.setText(tip);
    }
}