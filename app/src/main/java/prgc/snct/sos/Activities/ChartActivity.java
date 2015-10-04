package prgc.snct.sos.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.SolidColor;

import prgc.snct.sos.R;

/**
 * Created by çqï„ on 2015/10/02.
 */
public class ChartActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("XYSeries");
        series.add(1, 5.3);
        series.add(2, 6.1);
        series.add(3, 4.8);
        series.add(4, 6.5);
        series.add(5, 5.2);
        series.add(6, 6.1);
        series.add(7, 4.3);
        dataset.addSeries(series);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, 10f);
        renderer.setSeriesPaintType(0, new SolidColor(Color.rgb(255, 166, 0)));
        AFreeChart chart = ChartFactory.createXYLineChart("title", "X",
                "Y", dataset, PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaintType(new SolidColor(Color.GRAY));//îwåiÇÃêF
        chart.setBorderPaintType(new SolidColor(Color.BLACK));//ògê¸ÇÃêF

        ChartView spcv = (ChartView) findViewById(R.id.chart_view);
        spcv.setChart(chart);

    }

}
