package app.gui.graph;

import app.service.ChartService;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.logging.Logger;


public class ChartRenderer {

     static Logger logger = Logger.getLogger(ChartRenderer.class.toString());

    public static ChartPanel createRDCostsChart(String projectCode) throws IOException {

        LinkedHashMap<Period, BigDecimal> monthlyCostsData = ChartService.getChartService().getRDCostsData(projectCode);

        return createCostsChart(monthlyCostsData, "Project "+projectCode+" R&D costs");
    }

    public static ChartPanel createProjectCostsChart(String projectCode){

        LinkedHashMap<Period, BigDecimal> monthlyCostsData = ChartService.getChartService().getCostsData(projectCode);
        logger.info(monthlyCostsData.entrySet().size()+" size of data");

        return createCostsChart(monthlyCostsData, "Project "+projectCode+" costs");
    }

    public static ChartPanel createProjectPrototypeChart(String projectCode){
        LinkedHashMap<Period, BigDecimal> monthlyCostsData = ChartService.getChartService().getPrototypeCosts(projectCode);

        return createCostsChart(monthlyCostsData, "Project "+projectCode+" prototype costs");
    }

    private static ChartPanel createCostsChart(LinkedHashMap<Period, BigDecimal> data, String title){

        //todo cumulative curve

        DefaultCategoryDataset monthlyCostsChart = new DefaultCategoryDataset();

        for(Period period : data.keySet()){
            monthlyCostsChart.addValue(data.get(period).doubleValue(), "costs", period.toString());

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
        chart.setTitle(title);
        chart.removeLegend();

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        return chartPanel;
    }
}
