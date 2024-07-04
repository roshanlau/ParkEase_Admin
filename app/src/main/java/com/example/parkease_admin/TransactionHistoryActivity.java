package com.example.parkease_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.parkease_admin.object.Parking;
import com.example.parkease_admin.object.Transaction;
import com.example.parkease_admin.object.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {
    DatabaseReference databaseTransactions, databaseUsers;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> lvAdapter;
    Spinner spinnerUser;
    ListView lvTransactionHistory;
    List<Transaction> transactions;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        //init ui
        spinnerUser = findViewById(R.id.spinner_transactionHist_userID);
        lvTransactionHistory = findViewById(R.id.lv_transactionHist_transaction);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseTransactions = FirebaseDatabase.getInstance().getReference("transactions");

        transactions = new ArrayList<>();
        users = new ArrayList<>();

        lvAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        lvTransactionHistory.setAdapter(lvAdapter);

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        spinnerUser.setAdapter(spinnerAdapter);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                spinnerAdapter.clear();
                for(DataSnapshot userDataSnapshot : snapshot.getChildren()){
                    User user = userDataSnapshot.getValue(User.class);
                    users.add(user);
                    spinnerAdapter.add(user.getUserName() + " : " + user.getUserId());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Query query = FirebaseDatabase.getInstance()
                        .getReference("transactions")
                        .orderByChild("userID")
                        .equalTo(users.get(position).getUserId());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        transactions.clear();
                        lvAdapter.clear();
                        for (DataSnapshot transactionDataSnapshot : snapshot.getChildren()) {
                            Transaction transaction = transactionDataSnapshot.getValue(Transaction.class);
                            transactions.add(transaction);
                            lvAdapter.insert(transaction.getTransactionType() + "\nRM " + String.format("%.2f", transaction.getTransactionAmount()) + "\n" + transaction.getTransactionTime(), 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}