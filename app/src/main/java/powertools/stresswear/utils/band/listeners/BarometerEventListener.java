package powertools.stresswear.utils.band.listeners;

/**
 * Created by Matthijs Vos on 29-8-2016.
 */


import android.os.Environment;
import android.widget.TextView;

import com.microsoft.band.sensors.BandBarometerEvent;
import com.microsoft.band.sensors.BandBarometerEventListener;
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

public class BarometerEventListener implements BandBarometerEventListener {

    private TextView textView;
    private boolean graph;

    public void setViews(TextView textView, boolean graph) {
        this.textView = textView;
        this.graph = graph;
    }

    @Override
    public void onBandBarometerChanged(final BandBarometerEvent event) {
        if (event != null) {
            if (graph)
                MainActivity.sActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SensorActivity.chartAdapter.add((float) event.getAirPressure());
                    }
                });

            SensorsFragment.appendToUI(MainActivity.sContext.getString(R.string.air_pressure)
                    + String.format(" = %.3f hPa\n", event.getAirPressure())
                    + MainActivity.sContext.getString(R.string.air_temperature)
                    + String.format(" = %.2f °C = %.2f F",
                    event.getTemperature(), 1.8 * event.getTemperature() + 32), textView);

            if (MainActivity.sharedPreferences.getBoolean("log", false)) {
                MainActivity.bandSensorData.setBarometerData(event);

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zichtd" + File.separator + "Barometer");
                if (file.exists() || file.isDirectory()) {
                    try {
                        Date date = new Date();
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Zicht" + File.separator + "Barometer" + File.separator + "Barometer_" + DateFormat.getDateInstance().format(date) + ".csv").exists()) {
                            String str = DateFormat.getDateTimeInstance().format(event.getTimestamp());
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "Barometer" + File.separator + "Barometer_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                            csvWriter.writeNext(new String[]{String.valueOf(event.getTimestamp()),
                                    str, String.valueOf(event.getAirPressure()),
                                    String.valueOf(event.getTemperature())});
                            csvWriter.close();
                        } else {
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "Zicht" + File.separator + "Barometer" + File.separator + "Barometer_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                            csvWriter.writeNext(new String[]{MainActivity.sContext.getString(R.string.timestamp), MainActivity.sContext.getString(R.string.date_time), MainActivity.sContext.getString(R.string.air_pressure), MainActivity.sContext.getString(R.string.air_temperature)});
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
