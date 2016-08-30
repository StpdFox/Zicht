package powertools.stresswear.utils.band.listeners;

/**
 * Created by Matthijs Vos on 29-8-2016.
 */


        import android.os.Environment;
        import android.widget.TextView;

        import com.microsoft.band.sensors.BandSkinTemperatureEvent;
        import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
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

public class SkinTemperatureEventListener implements BandSkinTemperatureEventListener {

    private TextView textView;
    private boolean graph;

    public void setViews(TextView textView, boolean graph) {
        this.textView = textView;
        this.graph = graph;
    }

    @Override
    public void onBandSkinTemperatureChanged(final BandSkinTemperatureEvent bandSkinTemperatureEvent) {
        if (bandSkinTemperatureEvent != null) {
            if (graph)
                MainActivity.sActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SensorActivity.chartAdapter.add(bandSkinTemperatureEvent.getTemperature());
                    }
                });

            SensorsFragment.appendToUI(MainActivity.sContext.getString(R.string.temperature)
                    + String.format(" = " + bandSkinTemperatureEvent.getTemperature() + " °C"
                    + " = %.2f F", 1.8 * bandSkinTemperatureEvent.getTemperature() + 32), textView);

            if (MainActivity.sharedPreferences.getBoolean("log", false)) {
                MainActivity.bandSensorData.setSkinTemperatureData(bandSkinTemperatureEvent);

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zicht" + File.separator + "SkinTemperature");
                if (file.exists() || file.isDirectory()) {
                    try {
                        Date date = new Date();
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zicht" + File.separator + "SkinTemperature" + File.separator + "SkinTemperature_" + DateFormat.getDateInstance().format(date) + ".csv").exists()) {
                            String str = DateFormat.getDateTimeInstance().format(bandSkinTemperatureEvent.getTimestamp());
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "SkinTemperature" + File.separator + "SkinTemperature_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                            csvWriter.writeNext(new String[]{String.valueOf(bandSkinTemperatureEvent.getTimestamp()),
                                    str, String.valueOf(bandSkinTemperatureEvent.getTemperature())});
                            csvWriter.close();
                        } else {
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "SkinTemperature" + File.separator + "SkinTemperature_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                            csvWriter.writeNext(new String[]{MainActivity.sContext.getString(R.string.timestamp), MainActivity.sContext.getString(R.string.date_time), MainActivity.sContext.getString(R.string.temperature)});
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
