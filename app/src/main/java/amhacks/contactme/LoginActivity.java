package amhacks.contactme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView RegisterLink, ForgotLink;
    private EditText EmailET, PasswordET;
    private Button RegisterButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RegisterLink = (TextView) findViewById(R.id.register_link);
        ForgotLink = (TextView) findViewById(R.id.forgot_password_link);
        EmailET = (EditText) findViewById(R.id.login_email_et);
        PasswordET = (EditText) findViewById(R.id.login_password_et);
        RegisterButton = (Button) findViewById(R.id.login_login_button);
        loadingBar = new ProgressDialog(this);

        RegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = EmailET.getText().toString();
                password = PasswordET.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Fill all the credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Please wait");
                    loadingBar.setMessage("We are authenticating your credentials");
                    loadingBar.show();
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(dashboardIntent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        String msg = task.getException().getMessage();
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }
}