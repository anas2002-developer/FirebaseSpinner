package com.anas.firebasespinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    EditText eItem;
    Spinner spinner;
    TextView txtSpinnerSelect;

    DatabaseReference db_ref;
    ValueEventListener valueEventListener;
    ArrayList<String> arrItem;
    ArrayAdapter<String> arrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        eItem = findViewById(R.id.eItem);
        btnAdd = findViewById(R.id.btnAdd);
        txtSpinnerSelect = findViewById(R.id.txtSpinnerSelect);


        db_ref= FirebaseDatabase.getInstance().getReference("Spinner");
        arrItem = new ArrayList<>();
        arrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,arrItem);
        spinner.setAdapter(arrAdapter);

        fetching_item();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = eItem.getText().toString().trim();

              db_ref.push().setValue(item)
                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              Toast.makeText(MainActivity.this, "Added to Firebase", Toast.LENGTH_SHORT).show();
                              eItem.setText("");
                              arrItem.clear();
                              fetching_item();
                              arrAdapter.notifyDataSetChanged();
                          }
                      });
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currItem = spinner.getSelectedItem().toString();
                txtSpinnerSelect.setText(currItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void fetching_item(){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mydata : snapshot.getChildren())
                    arrItem.add(mydata.getValue().toString());
                arrAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}