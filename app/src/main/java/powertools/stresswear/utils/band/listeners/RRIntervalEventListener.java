package powertools.stresswear.utils.band.listeners;

/**
 * Created by Matthijs Vos on 29-8-2016.
 */


        import android.os.Environment;
        import android.widget.TextView;

        import com.microsoft.band.sensors.BandRRIntervalEvent;
        import com.microsoft.band.sensors.BandRRIntervalEventListener;
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

public class RRIntervalEventListener implements BandRRIntervalEventListener {

    private TextView textView;
    private boolean graph;

    public void setViews(TextView textView, boolean graph) {
        this.textView = textView;
        this.graph = graph;
    }

    @Override
    public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
        if (event != null) {
            if (graph)
                MainActivity.sActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SensorActivity.chartAdapter.add((float) event.getInterval());
                    }
                });

            SensorsFragment.appendToUI(MainActivity.sContext.getString(R.string.rr)
                    + String.format(" = %.3f s\n", event.getInterval()), textView);

            if (MainActivity.sharedPreferences.getBoolean("log", false)) {
                MainActivity.bandSensorData.setRrIntervalData(event);

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zichtd" + File.separator + "RRInterval");
                if (file.exists() || file.isDirectory()) {
                    try {
                        Date date = new Date();
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zicht" + File.separator + "RRInterval" + File.separator + "RRInterval_" + DateFormat.getDateInstance().format(date) + ".csv").exists()) {
                            String str = DateFormat.getDateTimeInstance().format(event.getTimestamp());
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "RRInterval" + File.separator + "RRInterval_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                            csvWriter.writeNext(new String[]{String.valueOf(event.getTimestamp()), str,
                                    String.valueOf(event.getInterval())});
                            csvWriter.close();
                        } else {
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "RRInterval" + File.separator + "RRInterval_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                            csvWriter.writeNext(new String[]{MainActivity.sContext.getString(R.string.timestamp), MainActivity.sContext.getString(R.string.date_time), MainActivity.sContext.getString(R.string.rr)});
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
