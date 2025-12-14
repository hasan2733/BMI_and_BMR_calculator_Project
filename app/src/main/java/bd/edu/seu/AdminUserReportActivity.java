package bd.edu.seu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import bd.edu.seu.models.BmiRecord;

public class AdminUserReportActivity extends AppCompatActivity {

    private ArrayList<String> reports;
    private ArrayList<String> recordKeys;
    private ArrayAdapter<String> adapter;
    private String targetUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_report);

        targetUid = getIntent().getStringExtra("TARGET_UID");
        String name = getIntent().getStringExtra("TARGET_NAME");

        TextView title = findViewById(R.id.tvReportTitle);
        ListView listView = findViewById(R.id.lvReportList);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        // Handle missing name
        if (title != null) {
            if (name != null) {
                title.setText(name + "\n(Long Press to Delete)");
            } else {
                title.setText("History\n(Long Press to Delete)");
                if (targetUid != null) fetchUserName(targetUid, title);
            }
        }

        reports = new ArrayList<>();
        recordKeys = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reports);
        listView.setAdapter(adapter);

        loadHistory();

        // Go Back Button Logic
        if (btnGoBack != null) {
            btnGoBack.setOnClickListener(v -> finish());
        }

        // Delete Listener
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteDialog(position);
            return true;
        });
    }

    private void fetchUserName(String uid, TextView titleView) {
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String fetchedName = snapshot.getValue(String.class);
                            if (titleView != null && fetchedName != null) {
                                titleView.setText(fetchedName + "\n(Long Press to Delete)");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void loadHistory() {
        if (targetUid != null) {
            FirebaseDatabase.getInstance().getReference("history").child(targetUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reports.clear();
                            recordKeys.clear();

                            if(!snapshot.exists()) {
                                reports.add("No calculations recorded yet.");
                            } else {
                                for(DataSnapshot child : snapshot.getChildren()) {
                                    BmiRecord rec = child.getValue(BmiRecord.class);
                                    if(rec != null) {
                                        Date date = new Date(rec.getTimestamp());
                                        String row = "📅 " + date.toString().substring(0, 16) +
                                                "\n📊 BMI: " + String.format("%.2f", rec.getBmi()) +
                                                " (" + rec.getSuggestion() + ")" +
                                                "\n💡 Tip: " + rec.getTips();

                                        reports.add(row);
                                        recordKeys.add(child.getKey());
                                    }
                                }
                                Collections.reverse(reports);
                                Collections.reverse(recordKeys);

                                Toast.makeText(AdminUserReportActivity.this, "Tip: Long press an item to delete it", Toast.LENGTH_LONG).show();
                            }
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AdminUserReportActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showDeleteDialog(int position) {
        if (recordKeys.isEmpty() || position >= recordKeys.size()) return;

        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Are you sure you want to delete this history item?")
                .setPositiveButton("Delete", (dialog, which) -> deleteRecord(position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteRecord(int position) {
        String keyToDelete = recordKeys.get(position);

        FirebaseDatabase.getInstance().getReference("history")
                .child(targetUid)
                .child(keyToDelete)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    reports.remove(position);
                    recordKeys.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                );
    }
}