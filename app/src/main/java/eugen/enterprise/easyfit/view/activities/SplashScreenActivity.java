package eugen.enterprise.easyfit.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import eugen.enterprise.easyfit.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, 1000);
    }
}
