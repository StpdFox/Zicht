package powertools.stresswear.fragments.sensors;

/**
 * Created by Surface Pro 3 on 29-8-2016.
 */

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.BandAmbientLightEventListener;
import com.microsoft.band.sensors.BandBarometerEventListener;
import com.microsoft.band.sensors.BandCaloriesEventListener;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.BandUVEventListener;

import java.lang.ref.WeakReference;



import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandConnectionCallback;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.notifications.BandNotificationManager;
import com.microsoft.band.notifications.VibrationType;
import com.microsoft.band.personalization.BandPersonalizationManager;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.BandAmbientLightEventListener;
import com.microsoft.band.sensors.BandBarometerEventListener;
import com.microsoft.band.sensors.BandCaloriesEventListener;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSensorManager;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.BandUVEventListener;
import com.microsoft.band.tiles.BandTileManager;
import powertools.stresswear.R;
import powertools.stresswear.activities.MainActivity;
import powertools.stresswear.utils.band.listeners.AccelerometerEventListener;
import powertools.stresswear.utils.band.listeners.AllSensorsAccelerometerEventListener;
import powertools.stresswear.utils.band.listeners.AltimeterEventListener;
import powertools.stresswear.utils.band.listeners.AmbientLightEventListener;
import powertools.stresswear.utils.band.listeners.BarometerEventListener;
import powertools.stresswear.utils.band.listeners.CaloriesEventListener;
import powertools.stresswear.utils.band.listeners.ContactEventListener;
import powertools.stresswear.utils.band.listeners.DistanceEventListener;
import powertools.stresswear.utils.band.listeners.GsrEventListener;
import powertools.stresswear.utils.band.listeners.GyroscopeEventListener;
import powertools.stresswear.utils.band.listeners.HeartRateEventListener;
import powertools.stresswear.utils.band.listeners.PedometerEventListener;
import powertools.stresswear.utils.band.listeners.RRIntervalEventListener;
import powertools.stresswear.utils.band.listeners.SkinTemperatureEventListener;
import powertools.stresswear.utils.band.listeners.UVEventListener;
import powertools.stresswear.utils.band.subscription.Band1SubscriptionTask;
import powertools.stresswear.utils.band.subscription.Band2SubscriptionTask;
import powertools.stresswear.utils.band.subscription.HeartRateConsentTask;
import powertools.stresswear.utils.band.subscription.HeartRateSubscriptionTask;
import powertools.stresswear.utils.band.subscription.RRIntervalSubscriptionTask;

import com.robinhood.spark.SparkView;

import java.lang.ref.WeakReference;

public class SensorsFragment extends Fragment {

    public static WeakReference<Activity> reference;
    public static TextView band2TV, logTV;
    public static CardView accelerometerCard, altimeterCard, ambientLightCard, barometerCard,
            caloriesCard, contactCard, distanceCard, gsrCard, gyroscopeCard, heartRateCard,
            pedometerCard, rrIntervalCard, skinTemperatureCard, uvCard;

    public static BandAccelerometerEventListener bandAllSensorsAccelerometerEventListener;
    public static BandAccelerometerEventListener bandAccelerometerEventListener;
    public static BandAltimeterEventListener bandAltimeterEventListener;
    public static BandAmbientLightEventListener bandAmbientLightEventListener;
    public static BandBarometerEventListener bandBarometerEventListener;
    public static BandCaloriesEventListener bandCaloriesEventListener;
    public static BandContactEventListener bandContactEventListener;
    public static BandDistanceEventListener bandDistanceEventListener;
    public static BandGsrEventListener bandGsrEventListener;
    public static BandGyroscopeEventListener bandGyroscopeEventListener;
    public static BandHeartRateEventListener bandHeartRateEventListener;
    public static BandPedometerEventListener bandPedometerEventListener;
    public static BandRRIntervalEventListener bandRRIntervalEventListener;
    public static BandSkinTemperatureEventListener bandSkinTemperatureEventListener;
    public static BandUVEventListener bandUVEventListener;
    View.OnClickListener cardViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView sensorName = (TextView) v.findViewById(R.id.txtName);
            Button one = (Button) v.findViewById(R.id.ONE_TONE);
            Intent intent = new Intent(getContext(), SensorActivity.class);
            intent.putExtra("sensor_name", sensorName.getText().toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String transitionName = getResources().getString(R.string.transition_sensor_name);

                ActivityOptions transitionActivityOptions = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), sensorName, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());
            } else {
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
            }
        }
    };

    public static void appendToUI(final String string, final TextView textView) {
        try {
            MainActivity.sActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(string);

                }
            });
        } catch (Exception e) {
            //
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        heartRateCard = (CardView) view.findViewById(R.id.heartRate_card);
        rrIntervalCard = (CardView) view.findViewById(R.id.rrInterval_card);
        reference = new WeakReference<Activity>(getActivity());
        band2TV = (TextView) view.findViewById(R.id.band2TxtStatus);
        accelerometerCard = (CardView) view.findViewById(R.id.accelerometer_card);
        altimeterCard = (CardView) view.findViewById(R.id.altimeter_card);
        ambientLightCard = (CardView) view.findViewById(R.id.ambientLight_card);
        barometerCard = (CardView) view.findViewById(R.id.barometer_card);
        caloriesCard = (CardView) view.findViewById(R.id.calories_card);
        contactCard = (CardView) view.findViewById(R.id.contact_card);
        distanceCard = (CardView) view.findViewById(R.id.distance_card);
        gsrCard = (CardView) view.findViewById(R.id.gsr_card);
        gyroscopeCard = (CardView) view.findViewById(R.id.gyroscope_card);

        pedometerCard = (CardView) view.findViewById(R.id.pedometer_card);

        skinTemperatureCard = (CardView) view.findViewById(R.id.skinTemperature_card);
        uvCard = (CardView) view.findViewById(R.id.uv_card);

        CardView[] cardViews = {accelerometerCard, altimeterCard, ambientLightCard, barometerCard,
                caloriesCard, contactCard, distanceCard, gsrCard, gyroscopeCard, heartRateCard,
                pedometerCard, rrIntervalCard, skinTemperatureCard, uvCard};
        String[] sensors = {"Accelerometer", "Altimeter", "Ambient Light", "Barometer", "Calories",
                "Contact", "Distance", "GSR", "Gyroscope", "Heart Rate", "Pedometer", "RR Interval",
                "Skin Temperature", "UV"};
        bandHeartRateEventListener = new HeartRateEventListener();
        bandRRIntervalEventListener = new RRIntervalEventListener();
        bandAllSensorsAccelerometerEventListener = new AllSensorsAccelerometerEventListener();
        bandAccelerometerEventListener = new AccelerometerEventListener();
        bandAltimeterEventListener = new AltimeterEventListener();
        bandAmbientLightEventListener = new AmbientLightEventListener();
        bandBarometerEventListener = new BarometerEventListener();
        bandCaloriesEventListener = new CaloriesEventListener();
        bandContactEventListener = new ContactEventListener();
        bandDistanceEventListener = new DistanceEventListener();
        bandGsrEventListener = new GsrEventListener();
        bandGyroscopeEventListener = new GyroscopeEventListener();

        bandPedometerEventListener = new PedometerEventListener();

        bandSkinTemperatureEventListener = new SkinTemperatureEventListener();
        bandUVEventListener = new UVEventListener();

        for (int i = 0; i < cardViews.length; i++) {
            ((TextView) cardViews[i].findViewById(R.id.txtName)).setText(sensors[i]);
            cardViews[i].setOnClickListener(cardViewOnClickListener);
        }

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        logTV = (TextView) view.findViewById(R.id.logStatus);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            logTV.setText(getResources().getString(R.string.permit_log));
        else
            logTV.setText(getResources().getString(R.string.log_text));
        ((Switch) view.findViewById(R.id.log_switch)).setChecked(MainActivity.sharedPreferences.getBoolean("log", false));
        if (!MainActivity.sharedPreferences.getBoolean("log", false))
            view.findViewById(R.id.logStatus).setVisibility(View.GONE);
        else {
            view.findViewById(R.id.logStatus).setVisibility(View.VISIBLE);
            view.findViewById(R.id.backlog_switch).setVisibility(View.VISIBLE);
        }
        ((Switch) view.findViewById(R.id.backlog_switch)).setChecked(MainActivity.sharedPreferences.getBoolean("backlog", false));
        if (!MainActivity.sharedPreferences.getBoolean("backlog", false))
            view.findViewById(R.id.backlogStatus).setVisibility(View.GONE);
        else
            view.findViewById(R.id.backlogStatus).setVisibility(View.VISIBLE);
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            if (MainActivity.client != null)
                if (!MainActivity.sharedPreferences.getBoolean("backlog", false))
                    MainActivity.client.getSensorManager().unregisterAllListeners();
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AccelerometerEventListener) bandAccelerometerEventListener).setViews((TextView) accelerometerCard.findViewById(R.id.txtData), false);
        ((AltimeterEventListener) bandAltimeterEventListener).setViews((TextView) altimeterCard.findViewById(R.id.txtData), false);
        ((AmbientLightEventListener) bandAmbientLightEventListener).setViews((TextView) ambientLightCard.findViewById(R.id.txtData), false);
        ((BarometerEventListener) bandBarometerEventListener).setViews((TextView) barometerCard.findViewById(R.id.txtData), false);
        ((CaloriesEventListener) bandCaloriesEventListener).setViews((TextView) caloriesCard.findViewById(R.id.txtData), false);
        ((ContactEventListener) bandContactEventListener).setViews((TextView) contactCard.findViewById(R.id.txtData), false);
        ((DistanceEventListener) bandDistanceEventListener).setViews((TextView) distanceCard.findViewById(R.id.txtData), false);
        ((GsrEventListener) bandGsrEventListener).setViews((TextView) gsrCard.findViewById(R.id.txtData), false);
        ((GyroscopeEventListener) bandGyroscopeEventListener).setViews((TextView) gyroscopeCard.findViewById(R.id.txtData), false);
        ((HeartRateEventListener) bandHeartRateEventListener).setViews((TextView) heartRateCard.findViewById(R.id.txtData), false);
        ((PedometerEventListener) bandPedometerEventListener).setViews((TextView) pedometerCard.findViewById(R.id.txtData), false);
        ((RRIntervalEventListener) bandRRIntervalEventListener).setViews((TextView) rrIntervalCard.findViewById(R.id.txtData), false);
        ((SkinTemperatureEventListener) bandSkinTemperatureEventListener).setViews((TextView) skinTemperatureCard.findViewById(R.id.txtData), false);
        ((UVEventListener) bandUVEventListener).setViews((TextView) uvCard.findViewById(R.id.txtData), false);

        new Band1SubscriptionTask().execute();
        new HeartRateSubscriptionTask().execute((TextView) heartRateCard.findViewById(R.id.txtData));
        new Band2SubscriptionTask().execute();
        new RRIntervalSubscriptionTask().execute((TextView) rrIntervalCard.findViewById(R.id.txtData));
    }

}