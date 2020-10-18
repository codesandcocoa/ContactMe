package amhacks.contactme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    private LinearLayout profileLayout, ScanQRLayout, walletLayout, mycardLayout,settingsLayout,requestsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        profileLayout = (LinearLayout) findViewById(R.id.profile_llt);
        ScanQRLayout  = (LinearLayout) findViewById(R.id.qr_scan_llt);
        walletLayout = (LinearLayout) findViewById(R.id.wallet_llt);
        mycardLayout = (LinearLayout) findViewById(R.id.mycard_llt);
        settingsLayout = (LinearLayout) findViewById(R.id.settings_llt);
        requestsLayout = (LinearLayout) findViewById(R.id.requests_llt);

        mycardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DashboardActivity.this, MyCardActivity.class);
                startActivity(myIntent);
            }
        });

        requestsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reqIntent = new Intent(DashboardActivity.this, RequestsActivity.class);
                startActivity(reqIntent);
            }
        });

        ScanQRLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntent = new Intent(DashboardActivity.this,AddCardActivity.class);
                startActivity(sIntent);
            }
        });



    }
}