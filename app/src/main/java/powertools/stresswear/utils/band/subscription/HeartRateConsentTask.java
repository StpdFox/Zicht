package powertools.stresswear.utils.band.subscription;

/**
 * Created by Matthijs Vos on 29-8-2016.
 */


        import android.app.Activity;
        import android.os.AsyncTask;

        import com.microsoft.band.BandException;
        import com.microsoft.band.sensors.HeartRateConsentListener;
        import powertools.stresswear.R;
        import powertools.stresswear.activities.MainActivity;
        import powertools.stresswear.utils.band.BandUtils;

        import java.lang.ref.WeakReference;

public class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {

    @Override
    protected Void doInBackground(WeakReference<Activity>... params) {
        try {
            if (BandUtils.getConnectedBandClient()) {

                if (params[0].get() != null) {
                    MainActivity.client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                        @Override
                        public void userAccepted(boolean consentGiven) {
                            new HeartRateSubscriptionTask().execute();
                        }
                    });
                }
            } else {
                MainActivity.appendToUI(MainActivity.sContext.getString(R.string.band_not_found), "Style.ALERT");
            }
        } catch (BandException e) {
            BandUtils.handleBandException(e);
        } catch (Exception e) {
            MainActivity.appendToUI(e.getMessage(), "Style.ALERT");
        }
        return null;
    }
}
