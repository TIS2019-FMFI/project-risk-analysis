package app.gui.graph;

import app.service.ChartService;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryStepRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class ChartRenderer {

    public static ChartPanel createRDCostssChart(String projectCode) throws IOException {
        //todo cumulative curve

        DefaultCategoryDataset monthlyCostsChart = new DefaultCategoryDataset();
        ChartService.getChartService().getRDCostsData(projectCode);
        LinkedHashMap<RDCosts.Period, BigDecimal> monthlyCostsData = RDCosts.getCosts();
        for(RDCosts.Period period : monthlyCostsData.keySet()){
            monthlyCostsChart.addValue(monthlyCostsData.get(period).doubleValue(), "costs", period.toString());

        }

        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(0,monthlyCostsChart);

        final CategoryItemRenderer renderer = new BarRenderer();
        plot.setRenderer(0,renderer);

        plot.mapDatasetToRangeAxis(1,1);

        CategoryAxis categoryAxis = new CategoryAxis("Period");
        plot.setDomainAxis(categoryAxis);
        plot.setRangeAxis(new NumberAxis("Costs"));
        plot.getDomainAxis().setCategoryMargin(0.001);


        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);

        plot.setWeight(10);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("R&D Project " + projectCode +" costs");
        chart.removeLegend();

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        return chartPanel;
    }
}
