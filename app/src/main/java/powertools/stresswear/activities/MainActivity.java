package powertools.stresswear.activities;

/**
 * Created by Surface Pro 3 on 29-8-2016.
 */


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.google.android.gms.analytics.Tracker;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.notifications.VibrationType;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.pages.FilledButton;
import com.microsoft.band.tiles.pages.FilledButtonData;
import com.microsoft.band.tiles.pages.FlowPanel;
import com.microsoft.band.tiles.pages.FlowPanelOrientation;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.band.tiles.pages.PageLayout;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.util.List;
import java.util.UUID;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import powertools.stresswear.R;
import powertools.stresswear.utils.band.BandSensorData;
import powertools.stresswear.utils.band.BandUtils;


public class MainActivity extends AppCompatActivity {

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;

    public static Context sContext;
    public static Activity sActivity;
    public static BandClient client = null;
    public static boolean band2 = false;
    public static BandInfo[] devices;

    private static final UUID tileId = UUID.fromString("cc0D508F-70A3-47D4-BBA3-812BADB1F8Aa");
    private static final UUID pageId1 = UUID.fromString("b1234567-89ab-cdef-0123-456789abcd00");

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public Switch logSwitch, backgroundLogSwitch;
    private TextView logStatus, backgroundLogStatus;
    private TextView txtStatus;
    private Tracker mTracker;
    private ScrollView scrollView;
    private AlertDialog mAlertDialog;
    private Uri mDestinationUri;
    private DirectoryChooserFragment mDialog;

    public static BandSensorData bandSensorData;

    private boolean checkCameraPermission(boolean b) {
        int result;
        if (b)
            result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        else
            result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission(boolean b) {
        if (b) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(MainActivity.this, getString(R.string.camera_permission), Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA}, 1);
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(MainActivity.this, getString(R.string.storage_permission), Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
    }


    public static void appendToUI(String string, String style) {
        Snackbar snackbar = Snackbar.make(sActivity.findViewById(R.id.main_content), string, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        switch (style) {
            case "Style.CONFIRM":
                view.setBackgroundColor(sContext.getResources().getColor(R.color.style_confirm));
                break;
            case "Style.INFO":
                view.setBackgroundColor(sContext.getResources().getColor(R.color.style_info));
                break;
            case "Style.ALERT":
                view.setBackgroundColor(sContext.getResources().getColor(R.color.style_alert));
                break;
        }
        snackbar.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, getString(R.string.camera_granted),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.camera_denied),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, getString(R.string.storage_granted),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.storage_denied),
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sContext = getApplicationContext();
        sActivity = this;

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", 0);
        editor = sharedPreferences.edit();

        bandSensorData = new BandSensorData();
        SectionsPagerAdapter mSectionsPagerAdapter;
        ViewPager mViewPager;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        if (!checkCameraPermission(true))
            requestCameraPermission(true);
        if (!checkCameraPermission(false))
            requestCameraPermission(false);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String name;
                switch (position) {

                    case 0:
                        name = "SENSORS";
                        break;
                    default:
                        name = "CfB";
                }
                ;

            }


            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.start();

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .allowNewDirectoryNameModification(true)
                .newDirectoryName("CfBCamera")
                .initialDirectory(sharedPreferences.getString("pic_location", "/storage/emulated/0/Zicht/Camera"))
                .build();


        new BandUtils().execute();

        CustomActivityOnCrash.install(this);

    }

    public void one(View view) {
        try {
            client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_ONE_TONE).await();
        } catch (InterruptedException e) {
        } catch (BandException e) {

        }
    }

    public void two(View view) {
        try {
            client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_TWO_TONE).await();
        } catch (InterruptedException e) {

        } catch (BandException e) {

        }
    }

    public void alarm(View view) {
        try {
            client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_ALARM).await();
        } catch (InterruptedException e) {

        } catch (BandException e) {

        }
    }

    public void timer(View view) {
        try {
            client.getNotificationManager().sendMessage(tileId, "Test", "Test", null, null);
            client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_TIMER).await();
        } catch (InterruptedException e) {
        } catch (BandException e) {
        }
    }

    public void popup(View v) {
        try {
            client.getNotificationManager().sendMessage(tileId, "test 2", "Dialog body", null, null).await();
        } catch (BandException e) {

        } catch (InterruptedException e) {

        }
    }

    public void addTile(View v) throws Exception {
        new StartTask().execute();


    }

    private class StartTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                appendToUI("Band is connected.\n");
                if (addTile()) {
                    updatePages();

                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                    return false;
                }
            } catch (BandException e) {

                return false;
            } catch (Exception e) {
                appendToUI(e.getMessage());
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {

            } else {

            }
        }
    }


    private void appendToUI(final String string) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });
    }

    private void removeTile() throws BandIOException, InterruptedException, BandException {
        if (doesTileExist()) {
            client.getTileManager().removeTile(tileId).await();
        }
    }


    private boolean doesTileExist() throws BandIOException, InterruptedException, BandException {
        List<BandTile> tiles = client.getTileManager().getTiles().await();
        for (BandTile tile : tiles) {
            if (tile.getTileId().equals(tileId)) {
                return true;
            }
        }
        return false;
    }

    private boolean addTile() throws Exception {
        if (doesTileExist()) {
            return true;
        }

		/* Set the options */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap tileIcon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.raw.b_icon, options);

        BandTile tile = new BandTile.Builder(tileId, "Button Tile", tileIcon)
                .setPageLayouts(createButtonLayout())
                .build();
        appendToUI("Button Tile is adding ...\n");
        if (client.getTileManager().addTile(this, tile).await()) {
            appendToUI("Button Tile is added.\n");
            return true;
        } else {
            appendToUI("Unable to add button tile to the band.\n");
            return false;
        }
    }

    private PageLayout createButtonLayout() {
        return new PageLayout(
                new FlowPanel(15, 0, 260, 105, FlowPanelOrientation.VERTICAL)
                        .addElements(new FilledButton(15, 0, 260, 105).setMargins(0, 5, 0, 0).setId(12).setBackgroundColor(Color.RED))
        );
    }

    private void updatePages() throws BandIOException {
        client.getTileManager().setPages(tileId,
                new PageData(pageId1, 0)
                        .update(new FilledButtonData(12, Color.BLUE))
        );
        appendToUI("Send button page data to tile page \n\n");
    }

    public void SwitchClick(View view) {
        logSwitch = (Switch) findViewById(R.id.log_switch);
        backgroundLogSwitch = (Switch) findViewById(R.id.backlog_switch);
        logStatus = (TextView) findViewById(R.id.logStatus);
        backgroundLogStatus = (TextView) findViewById(R.id.backlogStatus);
        switch (view.getId()) {
            case R.id.log_switch:
                setSwitch(logSwitch, logStatus, "log");
                if (logSwitch.isChecked())
                    backgroundLogSwitch.setVisibility(View.VISIBLE);
                else {
                    backgroundLogSwitch.setChecked(false);
                    setSwitch(backgroundLogSwitch, backgroundLogStatus, "backlog");
                    backgroundLogSwitch.setVisibility(View.GONE);
                }
                break;
            case R.id.backlog_switch:
                setSwitch(backgroundLogSwitch, backgroundLogStatus, "backlog");
                break;
        }
    }

    public void setSwitch(Switch s, TextView textView, String string) {
        if (!s.isChecked()) {
            editor.putBoolean(string, false);
            editor.apply();
            textView.setVisibility(View.GONE);
        } else {
            editor.putBoolean(string, true);
            editor.apply();
            textView.setVisibility(View.VISIBLE);
        }
    }


}
