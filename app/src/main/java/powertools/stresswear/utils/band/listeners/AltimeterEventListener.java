package powertools.stresswear.utils.band.listeners;

/**
 * Created by Matthijs Vos on 29-8-2016.
 */


        import android.os.Environment;
        import android.util.Log;
        import android.widget.TextView;

        import com.microsoft.band.sensors.BandAltimeterEvent;
        import com.microsoft.band.sensors.BandAltimeterEventListener;
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

public class AltimeterEventListener implements BandAltimeterEventListener {

    private TextView textView;
    private boolean graph;

    public void setViews(TextView textView, boolean graph) {
        this.textView = textView;
        this.graph = graph;
    }

    @Override
    public void onBandAltimeterChanged(final BandAltimeterEvent event) {
        if (event != null) {
            if (graph)
                MainActivity.sActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SensorActivity.chartAdapter.add(event.getRate());
                    }
                });

            try {
                SensorsFragment.appendToUI(new StringBuilder()
                                .append(MainActivity.sContext.getString(R.string.total_gain_today))
                                .append(String.format(" = %d cm\n", event.getTotalGainToday()))
                                .append(MainActivity.sContext.getString(R.string.total_gain))
                                .append(String.format(" = %d cm\n", event.getTotalGain()))
                                .append(MainActivity.sContext.getString(R.string.total_loss))
                                .append(String.format(" = %d cm\n", event.getTotalLoss()))
                                .append(MainActivity.sContext.getString(R.string.stepping_gain))
                                .append(String.format(" = %d cm\n", event.getSteppingGain()))
                                .append(MainActivity.sContext.getString(R.string.stepping_loss))
                                .append(String.format(" = %d cm\n", event.getSteppingLoss()))
                                .append(MainActivity.sContext.getString(R.string.steps_ascended))
                                .append(String.format(" = %d\n", event.getStepsAscended()))
                                .append(MainActivity.sContext.getString(R.string.steps_descended))
                                .append(String.format(" = %d\n", event.getStepsDescended()))
                                .append(MainActivity.sContext.getString(R.string.alt_rate))
                                .append(String.format(" = %f cm/s\n", event.getRate()))
                                .append(MainActivity.sContext.getString(R.string.stairs_ascended_today))
                                .append(String.format(" = %d\n", event.getFlightsAscendedToday()))
                                .append(MainActivity.sContext.getString(R.string.stairs_ascended))
                                .append(String.format(" = %d\n", event.getFlightsAscended()))
                                .append(MainActivity.sContext.getString(R.string.stairs_descended))
                                .append(String.format(" = %d\n", event.getFlightsDescended())).toString()
                        , textView);

                if (MainActivity.sharedPreferences.getBoolean("log", false)) {
                    MainActivity.bandSensorData.setAltimeterData(event);

                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CompanionForBand" + File.separator + "Altimeter");
                    if (file.exists() || file.isDirectory()) {
                        try {
                            Date date = new Date();
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                            if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CompanionForBand" + File.separator + "Altimeter" + File.separator + "Altimeter_" + DateFormat.getDateInstance().format(date) + ".csv").exists()) {
                                String str = DateFormat.getDateTimeInstance().format(event.getTimestamp());
                                CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "CompanionForBand" + File.separator + "Altimeter" + File.separator + "Altimeter_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                                csvWriter.writeNext(new String[]{String.valueOf(event.getTimestamp()), str,
                                        String.valueOf(event.getTotalGainToday()),
                                        String.valueOf(event.getTotalGain()),
                                        String.valueOf(event.getTotalLoss()),
                                        String.valueOf(event.getSteppingGain()),
                                        String.valueOf(event.getSteppingLoss()),
                                        String.valueOf(event.getStepsAscended()),
                                        String.valueOf(event.getStepsDescended()),
                                        String.valueOf(event.getRate()),
                                        String.valueOf(event.getFlightsAscendedToday()),
                                        String.valueOf(event.getFlightsAscended()),
                                        String.valueOf(event.getFlightsDescended())});
                                csvWriter.close();
                            } else {
                                CSVWriter csvWriter = new CSVWriter(new FileWriter(path + File.separator + "CompanionForBand" + File.separator + "Altimeter" + File.separator + "Altimeter_" + DateFormat.getDateInstance().format(date) + ".csv", true));
                                csvWriter.writeNext(new String[]{MainActivity.sContext.getString(R.string.timestamp), MainActivity.sContext.getString(R.string.date_time), MainActivity.sContext.getString(R.string.total_gain_today),
                                        MainActivity.sContext.getString(R.string.total_gain), MainActivity.sContext.getString(R.string.total_loss), MainActivity.sContext.getString(R.string.stepping_gain),
                                        MainActivity.sContext.getString(R.string.stepping_loss), MainActivity.sContext.getString(R.string.steps_ascended), MainActivity.sContext.getString(R.string.steps_descended),
                                        MainActivity.sContext.getString(R.string.alt_rate), MainActivity.sContext.getString(R.string.stairs_ascended_today), MainActivity.sContext.getString(R.string.stairs_ascended), MainActivity.sContext.getString(R.string.stairs_descended)});
                                csvWriter.close();
                            }
                        } catch (IOException e) {
                            Log.e("CSV", e.toString());
                        }
                    } else {
                        file.mkdirs();
                    }
                }
            } catch (Exception e) {
                SensorsFragment.appendToUI(e.toString(), textView);
            }
        }
    }
}
