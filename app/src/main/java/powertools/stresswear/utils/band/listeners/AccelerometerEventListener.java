package powertools.stresswear.utils.band.listeners;

/**
 * Created by Surface Pro 3 on 29-8-2016.
 */

        import android.os.Environment;
        import android.util.Log;
        import android.widget.TextView;

        import com.microsoft.band.sensors.BandAccelerometerEvent;
        import com.microsoft.band.sensors.BandAccelerometerEventListener;
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

public class AccelerometerEventListener implements BandAccelerometerEventListener {

    private TextView textView;
    private boolean graph;

    public void setViews(TextView textView, boolean graph) {
        this.textView = textView;
        this.graph = graph;
    }

    @Override
    public void onBandAccelerometerChanged(final BandAccelerometerEvent event) {
        if (event != null) {
            if (graph)
                MainActivity.sActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SensorActivity.chartAdapter.add(event.getAccelerationX());
                    }
                });

            SensorsFragment.appendToUI(String.format(" X = %.3f (m/s²) \n Y = %.3f (m/s²)\n Z = %.3f (m/s²)",
                    event.getAccelerationX(),
                    event.getAccelerationY(),
                    event.getAccelerationZ()), textView);


            MainActivity.bandSensorData.setAccelerometerData(event);

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zicht" + File.separator + "Accelerometer");
            if (file.exists() || file.isDirectory()) {
                try {
                    Date date = new Date();
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zicht" + File.separator + "Accelerometer" + File.separator + "Accelerometer_" + DateFormat.getDateInstance().format(date) + ".csv").exists()) {
                        String str = DateFormat.getDateTimeInstance().format(event.getTimestamp());
                        CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "Accelerometer" + File.separator + "Accelerometer_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                        csvWriter.writeNext(new String[]{String.valueOf(event.getTimestamp()),
                                str, String.valueOf(event.getAccelerationX()),
                                String.valueOf(event.getAccelerationY()),
                                String.valueOf(event.getAccelerationZ())});

                        csvWriter.close();
                    } else {
                        CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "Accelerometer" + File.separator + "Accelerometer_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                        csvWriter.writeNext(new String[]{MainActivity.sContext.getString(R.string.timestamp), MainActivity.sContext.getString(R.string.date_time), "X", "Y", "Z"});
                        csvWriter.close();
                    }
                } catch (IOException e) {
                    Log.e("CSV", e.toString());
                }
            } else {
                file.mkdirs();
            }

        }
    }
}
