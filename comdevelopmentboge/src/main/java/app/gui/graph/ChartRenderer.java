package app.gui.graph;

import app.service.ChartService;
import app.service.ProjectService;
import javafx.beans.binding.Bindings;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.scene.chart.XYChart;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.logging.Logger;


public class ChartRenderer {

     static Logger logger = Logger.getLogger(ChartRenderer.class.toString());

    public static StackPane createRDCostsChart(String projektDef, java.sql.Date from, java.sql.Date to) throws IOException {

        LinkedHashMap<Period, BigDecimal> monthlyCostsData = ChartService.getChartService().getRDCostsData(projektDef, from, to);
        BigDecimal planned = ProjectService.getProjectService().getPlanedDDCosts(projektDef);

        return createBarChart(monthlyCostsData, "Project "+projektDef+" R&D costs", planned);
    }

    public static StackPane createProjectCostsChart(String projektDef,java.sql.Date from, java.sql.Date to) throws IOException {

        LinkedHashMap<Period, BigDecimal> monthlyCostsData = ChartService.getChartService().getCostsData(projektDef, from, to);

        return createBarChart(monthlyCostsData, "Project "+projektDef+" costs", BigDecimal.ZERO);
    }

    public static StackPane createProjectPrototypeChart(String projektDef,java.sql.Date from, java.sql.Date to) throws IOException {
        LinkedHashMap<Period, BigDecimal> monthlyCostsData = ChartService.getChartService().getPrototypeCosts(projektDef, from, to);
        BigDecimal planned = ProjectService.getProjectService().getPrototypeCosts(projektDef);
        return createBarChart(monthlyCostsData, "Project "+projektDef+" prototype costs", planned);
    }

    public static StackPane createPrototypeRevenuesChart(String projektDef, java.sql.Date from, java.sql.Date to) throws IOException {
        LinkedHashMap<Period, BigDecimal> monthlyRevenuesData = ChartService.getChartService().getPrototypeRevenues(projektDef, from, to);

        return createBarChart(monthlyRevenuesData, "Project "+projektDef+" prototype revenues", BigDecimal.ZERO);
    }

    public static StackPane createRDTimeCostsChart(String projektDef, java.sql.Date from, java.sql.Date to) throws IOException {
        LinkedHashMap<Period, BigDecimal> monthlyCostsData = ChartService.getChartService().getRDTimeCosts(projektDef, from, to);

        return createBarChart(monthlyCostsData, "Project "+projektDef+" R&D time costs", BigDecimal.ZERO);
    }

    public static StackPane createSummaryProjectRevenues(String projektDef, java.sql.Date from, java.sql.Date to){
        LinkedHashMap<String, BigDecimal> revenues = ChartService.getChartService().getRevenuesPerForm(projektDef, from, to);

        return createPieChart(revenues, "Project "+projektDef+" revenues");
    }

    public static StackPane createSummaryProjectCosts(String projektDef, java.sql.Date from, java.sql.Date to){
        LinkedHashMap<String, BigDecimal> revenues = ChartService.getChartService().getCostsPerForm(projektDef, from, to);

        return createPieChart(revenues, "Project "+projektDef+" costs");
    }


    public static StackPane createBarChart(LinkedHashMap<Period, BigDecimal> data, String title, BigDecimal planned) throws IOException {

        StackPane pane = new StackPane();

        // x-axis and y-axis  for both charts:
        final javafx.scene.chart.CategoryAxis xAxis = new javafx.scene.chart.CategoryAxis();
        xAxis.setLabel("period");
        final javafx.scene.chart.NumberAxis yAxis1 = new javafx.scene.chart.NumberAxis();
        yAxis1.setLabel("costs");

        final javafx.scene.chart.NumberAxis cumulativeYAxis = new javafx.scene.chart.NumberAxis();
        cumulativeYAxis.setLabel("cumulative");

        // first chart:
        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis1);
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.getStylesheets().addAll(ChartRenderer.class.getResource("bar-chart-style.css").toExternalForm());
        barChart.getYAxis().setVisible(true);
        barChart.getYAxis().setSide(Side.LEFT);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for(Period p:data.keySet()){
            series1.getData().add(new XYChart.Data<>(p.toString(), data.get(p)));
        }

        LinkedHashMap<Period, BigDecimal> cumulativeData = getCumulativeData(data);

        // second chart (overlaid):
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, cumulativeYAxis);
        lineChart.setLegendVisible(false);
        lineChart.setTitle(title);
        lineChart.setAnimated(false);
        lineChart.setAlternativeRowFillVisible(false);
        lineChart.setAlternativeColumnFillVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.getXAxis().setVisible(false);
        lineChart.getYAxis().setVisible(true);
        lineChart.getYAxis().setSide(Side.RIGHT);
        lineChart.translateXProperty().bind(
                lineChart.getYAxis().widthProperty().subtract(lineChart.getYAxis().tickLengthProperty())
        );
        lineChart.setCreateSymbols(false);

        lineChart.getStylesheets().addAll(ChartRenderer.class.getResource("line-chart-style.css").toExternalForm());
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        for(Period p:cumulativeData.keySet()){
            series2.getData().add(new XYChart.Data<>(p.toString(), cumulativeData.get(p)));
        }
        barChart.getData().add(series1);
        lineChart.getData().add(series2);

        if(planned != null && planned.compareTo(BigDecimal.ZERO) > 0){
            XYChart.Series<String, Number> limit = new XYChart.Series<>();
            for(Period p:data.keySet()){
                limit.getData().add(new XYChart.Data<>(p.toString(), planned));
            }
            //either RD or Prototype costs, have its planned limit, show a line
            lineChart.getData().add(limit);

        }

        pane.getChildren().clear();
        pane.getChildren().addAll(barChart, lineChart);


        return pane;
    }

    public static StackPane createPieChart(LinkedHashMap<String, BigDecimal> data, String title){

        StackPane pane = new StackPane();

        PieChart pieChart = new PieChart();
        pieChart.setTitle(title);

        for(String form:data.keySet()){
            System.out.println(form);
            PieChart.Data pieChartData = new PieChart.Data(form, data.get(form).doubleValue());
            pieChart.getData().add(pieChartData);
        }
        pieChart.getData().forEach(d ->
                d.nameProperty().bind(
                        Bindings.concat(
                                d.getName(), " ", String.format("%.2f", d.getPieValue()), " €"
                        )
                )
        );
        pieChart.setLabelLineLength(50);
        pieChart.setLegendVisible(true);
        pieChart.setLegendSide(Side.BOTTOM);
        Group group  = new Group(pieChart);
        pane.getChildren().add(group);
        return pane;
    }


    public static LinkedHashMap<Period, BigDecimal> getCumulativeData(LinkedHashMap<Period, BigDecimal> data){

        LinkedHashMap<Period, BigDecimal> cumulativeData = new LinkedHashMap<>();

        Period currentPeriod = null;
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for(Period p: data.keySet()){
            count ++;
            if(currentPeriod==null) {
                currentPeriod = p;
                cumulativeData.put(currentPeriod, sum);

            } else if(!p.equals(currentPeriod)){
                cumulativeData.put(currentPeriod, sum);
                currentPeriod = p;
            }
            sum = sum.add(data.get(p));

            if(count == data.size()){
                cumulativeData.put(currentPeriod, sum);
            }

        }

        return cumulativeData;
    }

}
