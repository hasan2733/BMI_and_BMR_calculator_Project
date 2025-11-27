package bd.edu.seu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import bd.edu.seu.adapters.UserAdapter;
import bd.edu.seu.models.User;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView rv;
    private UserAdapter adapter;
    private List<User> userList;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tvTotal = findViewById(R.id.tvTotalUsers);
        rv = findViewById(R.id.rvUserList);

        rv.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        adapter = new UserAdapter(userList, user -> {
            Intent intent = new Intent(AdminActivity.this, AdminUserReportActivity.class);
            intent.putExtra("TARGET_UID", user.getUid());
            intent.putExtra("TARGET_NAME", user.getName());
            startActivity(intent);
        });

        rv.setAdapter(adapter);

        findViewById(R.id.btnAdminLogout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        loadAllUsers();
    }

    private void loadAllUsers() {
        FirebaseDatabase.getInstance().getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            User u = data.getValue(User.class);
                            // Only show regular users, not admins
                            if (u != null && "user".equals(u.getRole())) {
                                userList.add(u);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        tvTotal.setText("Total Registered Users: " + userList.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        tvTotal.setText("Error loading users");
                    }
                });
    }
}