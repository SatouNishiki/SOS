package prgc.snct.sos.Activities;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import org.afree.chart.AFreeChart;
import org.afree.graphics.geom.RectShape;

/**
 * Created by çqï„ on 2015/10/02.
 */
public class ChartView extends View {

    private AFreeChart chart;
    private RectShape chartArea;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        chartArea = new RectShape();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chartArea.setWidth(w);
        chartArea.setHeight(h/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.chart.draw(canvas, chartArea);
    }

    public void setChart(AFreeChart chart) {
        this.chart = chart;
    }

}
