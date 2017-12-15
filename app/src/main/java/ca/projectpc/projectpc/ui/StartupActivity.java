package ca.projectpc.projectpc.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ca.projectpc.projectpc.R;
import ca.projectpc.projectpc.api.IServiceCallback;
import ca.projectpc.projectpc.api.Service;
import ca.projectpc.projectpc.api.ServiceResult;
import ca.projectpc.projectpc.api.service.SystemService;
import pl.aprilapps.easyphotopicker.EasyImage;

public class StartupActivity extends AppCompatActivity {
    // TODO: Change back
    public static final String API_ENDPOINT = "http://192.168.0.101:4040/api/";//"https://ppc.indigogames.ca/api/";
    public static final int API_TIMEOUT = 25000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        // Initialize API
        Service.setServerUrl(API_ENDPOINT);
        Service.setTimeout(API_TIMEOUT);

        // Initialize EasyImage
        EasyImage.configuration(this)
                .setImagesFolderName("local")
                .saveInAppExternalFilesDir()
                .saveInRootPicturesDirectory()
                .setCopyExistingPicturesToPublicLocation(true);

        // TODO: Get required permissions (File reading permissions, GPS permissions)

        // Check system service
        try {
            SystemService systemService = Service.get(SystemService.class);
            systemService.getVersion(new IServiceCallback<SystemService.VersionResult>() {
                @Override
                public void onEnd(ServiceResult<SystemService.VersionResult> result) {
                    try {
                        if (!result.hasError()) {
                            // Navigate to login activity
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            throw result.getException();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        Toast.makeText(getBaseContext(), "Unable to connect to server",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();

            Toast.makeText(this, "Unable to get service", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}