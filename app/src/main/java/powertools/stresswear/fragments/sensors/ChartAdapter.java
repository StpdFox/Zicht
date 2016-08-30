package powertools.stresswear.fragments.sensors;

/**
 * Created by Surface Pro 3 on 29-8-2016.
 */
import android.graphics.RectF;

import com.robinhood.spark.SparkAdapter;

public class ChartAdapter extends SparkAdapter {
    private final float[] yData;
    int i = 0;

    public ChartAdapter() {
        yData = new float[50];
    }

    public void add(Float y) {
        yData[i] = y;
        i = (i < 49) ? i + 1 : 0;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return yData.length;
    }

    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }

    @Override
    public float getX(int index) {
        return super.getX(index);
    }

    @Override
    public RectF getDataBounds() {
        return super.getDataBounds();
    }

    @Override
    public boolean hasBaseLine() {
        return false;
    }

    @Override
    public float getBaseLine() {
        return super.getBaseLine();
    }
}
