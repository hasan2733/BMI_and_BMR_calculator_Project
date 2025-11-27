package bd.edu.seu.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
    private Paint paint;
    private Paint textPaint;
    private RectF rectF;

    // Default data
    private float[] data = {40, 25, 20, 15};
    private String[] labels = {"Carbs", "Protein", "Fats", "Veg"};

    private int[] colors = {
            Color.parseColor("#42A5F5"), // Blue
            Color.parseColor("#66BB6A"), // Green
            Color.parseColor("#FFA726"), // Orange
            Color.parseColor("#EF5350")  // Red
    };

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(32f);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        rectF = new RectF();
    }

    public void setDietPlan(String status) {
        if (status == null) status = "Normal";

        switch (status) {
            case "Underweight":
                data = new float[]{50, 25, 20, 5};
                break;
            case "Overweight":
                data = new float[]{25, 35, 15, 25};
                break;
            case "Obese":
                data = new float[]{15, 40, 15, 30};
                break;
            default: // Normal
                data = new float[]{40, 25, 20, 15};
                break;
        }
        invalidate(); // Redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = Math.min(getWidth(), getHeight()) / 2 * 0.8f;
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float currentAngle = 0;
        for (int i = 0; i < data.length; i++) {
            paint.setColor(colors[i]);
            float sweepAngle = (data[i] / 100f) * 360f;
            canvas.drawArc(rectF, currentAngle, sweepAngle, true, paint);

            // Draw Labels Inside
            float middleAngle = currentAngle + (sweepAngle / 2);
            double radians = Math.toRadians(middleAngle);
            float x = (float) (centerX + (radius * 0.65f) * Math.cos(radians));
            float y = (float) (centerY + (radius * 0.65f) * Math.sin(radians));

            if (data[i] > 10) {
                canvas.drawText(labels[i] + " " + (int)data[i] + "%", x, y, textPaint);
            }
            currentAngle += sweepAngle;
        }
    }
}