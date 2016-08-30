package powertools.stresswear.utils.band.listeners;

/**
 * Created by Matthijs Vos on 29-8-2016.
 */


        import android.os.Environment;
        import android.widget.TextView;

        import com.microsoft.band.BandException;
        import com.microsoft.band.notifications.VibrationType;
        import com.microsoft.band.sensors.BandHeartRateEvent;
        import com.microsoft.band.sensors.BandHeartRateEventListener;
        import com.opencsv.CSVWriter;
        import powertools.stresswear.R;
        import powertools.stresswear.activities.MainActivity;
        import powertools.stresswear.fragments.sensors.SensorActivity;
        import powertools.stresswear.fragments.sensors.SensorsFragment;

        import java.io.File;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.text.DateFormat;
        import java.util.Date;

public class HeartRateEventListener implements BandHeartRateEventListener {
    private SensorActivity sensor;
    private TextView textView;
    private boolean graph;

    public void setViews(TextView textView, boolean graph) {
        this.textView = textView;
        this.graph = graph;
    }

    @Override
    public void onBandHeartRateChanged(final BandHeartRateEvent event) {
        if (event != null) {
            if (graph)
                MainActivity.sActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SensorActivity.chartAdapter.add((float) event.getHeartRate());
                        if(event.getHeartRate() >=90) {
                            try {MainActivity.client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_ALARM).await();
                            } catch (InterruptedException e) {
// handle InterruptedException
                            } catch (BandException e) {
// handle BandException
                            }
                        }
                    }
                });

            SensorsFragment.appendToUI(MainActivity.sContext.getString(R.string.heart_rate) + String.format(" = %d ", event.getHeartRate())
                    + MainActivity.sContext.getString(R.string.beats_per_minute) + "\n" + MainActivity.sContext.getString(R.string.quality)
                    + String.format(" = %s", event.getQuality()), textView);

            if (MainActivity.sharedPreferences.getBoolean("log", false)) {
                MainActivity.bandSensorData.setHeartRateData(event);

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zicht" + File.separator + "HeartRate");
                if (file.exists() || file.isDirectory()) {
                    try {
                        Date date = new Date();
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zicht" + File.separator + "HeartRate" + File.separator + "HeartRate_" + DateFormat.getDateInstance().format(date) + ".csv").exists()) {
                            String str = DateFormat.getDateTimeInstance().format(event.getTimestamp());
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "HeartRate" + File.separator + "HeartRate_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                            csvWriter.writeNext(new String[]{String.valueOf(event.getTimestamp()),
                                    str, String.valueOf(event.getHeartRate()), String.valueOf(event.getQuality())});
                            csvWriter.close();
                        } else {
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "HeartRate" + File.separator + "HeartRate_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                            csvWriter.writeNext(new String[]{MainActivity.sContext.getString(R.string.timestamp), MainActivity.sContext.getString(R.string.date_time), MainActivity.sContext.getString(R.string.heart_rate), MainActivity.sContext.getString(R.string.quality)});
                            csvWriter.close();
                        }
                    } catch (IOException e) {
                        //
                    }
                } else {
                    file.mkdirs();
                }
            }
        }
    }
}
