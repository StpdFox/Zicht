package powertools.stresswear.utils.band.listeners;

/**
 * Created by Matthijs Vos on 29-8-2016.
 */


        import android.os.Environment;
        import android.util.Log;
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
        import java.lang.reflect.Array;
        import java.nio.ByteBuffer;
        import java.nio.IntBuffer;
        import java.text.DateFormat;
        import java.util.Arrays;
        import java.util.Date;
        import java.util.UUID;

public class HeartRateEventListener implements BandHeartRateEventListener {
    private static final UUID tileId = UUID.fromString("cc0D508F-70A3-47D4-BBA3-812BADB1F8Aa");
    private SensorActivity sensor;
    private TextView textView;
    private boolean graph;
    private int i=1;
    private int[] AV= new int[10];
    int sum = 0;
    double average;


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
            int HR = event.getHeartRate();
            i=1;
            shove(HR);
            System.out.println("HR = " + HR+", AV = " + average);
            average(AV);
            compare(HR);
        }
    }
    public void shove(int x){
        int p = 1;

        //Kijk of de array leeg is, zo ja vul hem met de huidige hartslag
        if(AV[AV.length-1]==0){
            for(int i = AV.length-1;i >=p;i--) {

                AV[i] = x;
            }
        }
        //Schuif alle gettallen 1 positie op en zet de huidige hartslag op positie 1
        for(int i = AV.length-1;i >=p;i--) {

            AV[i] = AV[i - 1];
        }
        AV[0] = x;

    }
    public void average(int[] data) {

        sum=0;
        for(int i=0; i < data.length; i++){
            if(data[i]!=0) {
                sum = sum + data[i];
            }
        }
        average = (double)sum/data.length;

    }

    public void compare(final int Heartrate) {
        final double x1 = average * 1.07;
        final double x2 = average * 0.9;

        if (Heartrate > x1) {
            Log.d("", "Hartslag stijgt");
            new Thread(new Runnable() {
                public void run() {
                    try {
                        MainActivity.client.getNotificationManager().sendMessage(tileId, "Test", "Test", null,null);
                        MainActivity.client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_ONE_TONE).await();
                    } catch (InterruptedException e) {
                    } catch (BandException e) {

                    }
                }
            }).start();
        }
        if (Heartrate < x2) {
            Log.d("", "Hartslag daalt");
        }

    }

}
