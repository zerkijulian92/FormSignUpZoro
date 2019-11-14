package com.takatutustudio.formloginzoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextInputEditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    Button btnSignUp;
    TextView clearAll;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //regitrasi element
        edtName             = findViewById(R.id.edt_name);
        edtEmail            = findViewById(R.id.edt_email);
        edtPassword         = findViewById(R.id.edt_password);
        edtConfirmPassword  = findViewById(R.id.edt_confirmpassword);
        btnSignUp           = findViewById(R.id.btn_signup);

        clearAll            = findViewById(R.id.clear_all);

        //Menonaktifkan button ketika editText kosong
        edtName.addTextChangedListener(loginTextWatcher);
        edtEmail.addTextChangedListener(loginTextWatcher);
        edtPassword.addTextChangedListener(loginTextWatcher);
        edtConfirmPassword.addTextChangedListener(loginTextWatcher);

        //fungsi Clear All
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Text = edtName.getText().toString();
                edtEmail.getText().toString();
                edtPassword.getText().toString();
                edtConfirmPassword.getText().toString();
                edtConfirmPassword.getText().toString();

                if (Text.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Silahkan inputkan Data Baru !!!",Toast.LENGTH_SHORT).show();
                }else {
                    edtName.setText("");
                    edtEmail.setText("");
                    edtPassword.setText("");
                    edtConfirmPassword.setText("");
                }
            }
        });

        //fungis btnSignUp
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //menyimpan data kepada local storage ( HP )
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, edtName.getText().toString());
                editor.apply();

                //simpan kepada firebase
                reference = FirebaseDatabase.getInstance().getReference().child("List Pendaftaran Akun").child(edtName.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("Name").setValue(edtName.getText().toString());
                        dataSnapshot.getRef().child("Email").setValue(edtEmail.getText().toString());
                        dataSnapshot.getRef().child("Password").setValue(edtPassword.getText().toString());
                        dataSnapshot.getRef().child("Confirm Password").setValue(edtConfirmPassword.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Toast.makeText(getApplicationContext(), "Terimakasih " + edtName.getText().toString() + " telah mendaftar di sistem kami",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //fungsi Menonaktifkan button ketika editText kosong
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nameInput        = edtName.getText().toString().trim();
            String emailInput       = edtEmail.getText().toString().trim();
            String passwordInput    = edtPassword.getText().toString().trim();
            String confirmpassInput = edtConfirmPassword.getText().toString().trim();

            btnSignUp.setEnabled(!nameInput.isEmpty() && !emailInput.isEmpty()
            && !passwordInput.isEmpty() && !confirmpassInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
