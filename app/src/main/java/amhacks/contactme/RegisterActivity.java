package amhacks.contactme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText EmailET, PassET, ConfPassET;
    private Button RegisterButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailET = (EditText) findViewById(R.id.register_email_et);
        PassET = (EditText) findViewById(R.id.register_password_et);
        ConfPassET = (EditText) findViewById(R.id.register_conf_password_et);
        RegisterButton  = (Button) findViewById(R.id.register_button);
        progressDialog = new ProgressDialog(this);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, cpass;
                email = EmailET.getText().toString();
                password = PassET.getText().toString();
                cpass = ConfPassET.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cpass))
                {
                    Toast.makeText(RegisterActivity.this, "Fill all the credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(password.equals(cpass))
                    {
                        progressDialog.setTitle("Please wait");
                        progressDialog.setMessage("We are creating your cardspace");
                        progressDialog.show();
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            Intent DashIntent = new Intent(RegisterActivity.this, AddProfileActivity.class);
                                            DashIntent.putExtra("type","new");
                                            DashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(DashIntent);
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
