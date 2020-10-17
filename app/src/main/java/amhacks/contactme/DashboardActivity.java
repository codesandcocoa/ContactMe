package amhacks.contactme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        CardView addProfile = (CardView) findViewById(R.id.add_profile_cardview);
        CardView settings = (CardView) findViewById(R.id.settings_cardview);
        CardView profile = (CardView) findViewById(R.id.profile_cardview);
        CardView main = (CardView) findViewById(R.id.main_cardview);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,AddProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}