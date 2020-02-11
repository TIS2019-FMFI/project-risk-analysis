package app.exporter;

import app.App;
import app.db.Project;
import app.db.SAP;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import app.gui.MyAlert;
import app.gui.graph.ChartRenderer;
import app.gui.graph.Period;
import app.gui.project.ProjectFilter;
import app.service.ChartService;
import app.service.ProjectService;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


import javax.imageio.ImageIO;

/**
 * umožnuje vytvorenie PDF súboru o projekte pozostávajúcich z
 * tabuľky o projekte, grafov a tabuľky so SAP údajmi
 */
public class PdfExporter {

    /**
     * exportovanie údajov o projekte do PDF súboru
     * @param projectData
     * @param sapData
     * @throws IOException
     * @throws DocumentException
     */
    public static void exportPdf(List<Project> projectData, List<SAP> sapData) throws IOException, DocumentException {

        String directoryName = App.getPropertiesManager().getProperty("file.location");
        if(directoryName == null || "".equals(directoryName)){
            MyAlert.showWarning("Nenastavili ste priečinok na export súborov,\n súbor bol exportovaný do priečinka C:/files");
            directoryName="C:/files";
        }
        String fileName = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss'.pdf'").format(new Date());

        //if directory does not exist, create one
        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(directoryName+"/"+fileName));
        document.open();

        //project projectTable
        PdfPTable projectTable = new PdfPTable(9);
        projectTable.setWidthPercentage(100);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);

        //projectTable header
        PdfPCell cell = new PdfPCell(new Phrase("projectNr", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Customer", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        cell = new PdfPCell(new Phrase("ProjectName", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        cell = new PdfPCell(new Phrase("PartNumber", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        cell = new PdfPCell(new Phrase("ROS", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        cell = new PdfPCell(new Phrase("ROCE", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Volumes", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Offered/planed D&D costs", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Offered/planed Prototype costs", font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        projectTable.addCell(cell);

        font = FontFactory.getFont(FontFactory.HELVETICA, 8);
        //projectTable content
        for(Project project : projectData){
            List<String> attributes = project.getAllAttributesValues();
            for(String att : attributes){
                cell = new PdfPCell(new Phrase(att, font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                projectTable.addCell(cell);
            }
        }
        projectTable.setSpacingAfter(72f);
        document.add(projectTable);
        // adding charts

        //R&D costs chart
        LinkedHashMap<Period, BigDecimal> data = ChartService.getChartService().getRDCostsData(projectData.get(0).getProjectNumber(), ProjectFilter.getInstance().getFrom(), ProjectFilter.getInstance().getTo());
        BigDecimal planned = ProjectService.getProjectService().getPlanedDDCosts(projectData.get(0).getProjectNumber());
        Image RDCostsChartImage = createLineBarChartImage(data, "Project "+projectData.get(0).getProjectNumber() + " R&D Costs", planned);
        document.add(RDCostsChartImage);

        //Prototype costs chart
        data = ChartService.getChartService().getPrototypeCosts(projectData.get(0).getProjectNumber(), ProjectFilter.getInstance().getFrom(), ProjectFilter.getInstance().getTo());
        planned = ProjectService.getProjectService().getPrototypeCosts(projectData.get(0).getProjectNumber());
        Image prototypeCostsChartImage = createLineBarChartImage(data, "Project "+projectData.get(0).getProjectNumber() + " prototype costs", planned);
        document.add(prototypeCostsChartImage);

        //project costs
        data = ChartService.getChartService().getCostsData(projectData.get(0).getProjectNumber(), ProjectFilter.getInstance().getFrom(), ProjectFilter.getInstance().getTo());
        Image projectCostsChartImage = createLineBarChartImage(data, "Project "+projectData.get(0).getProjectNumber() +" project costs", BigDecimal.ZERO);
        document.add(projectCostsChartImage);

        //prototype revenues
        data = ChartService.getChartService().getPrototypeRevenues(projectData.get(0).getProjectNumber(), ProjectFilter.getInstance().getFrom(), ProjectFilter.getInstance().getTo());
        Image prototypeRevenuesChart = createLineBarChartImage(data, "Project "+projectData.get(0).getProjectNumber() + " revenues", BigDecimal.ZERO);
        document.add(prototypeRevenuesChart);

        //pie chart costs
        LinkedHashMap<String, BigDecimal> pieChartData = ChartService.getChartService().getCostsPerForm(projectData.get(0).getProjectNumber(), ProjectFilter.getInstance().getFrom(), ProjectFilter.getInstance().getTo());
        Image revenuesPieChartImage = createPieChartImage(pieChartData, "Project "+projectData.get(0).getProjectNumber() + " costs");
        document.add(revenuesPieChartImage);

        pieChartData = ChartService.getChartService().getRevenuesPerForm(projectData.get(0).getProjectNumber(), ProjectFilter.getInstance().getFrom(), ProjectFilter.getInstance().getTo());
        Image costsPieChartImage = createPieChartImage(pieChartData, "Project "+projectData.get(0).getProjectNumber() + " revenues");
        document.add(costsPieChartImage);

        //sap projectTable
        PdfPTable sapTable = new PdfPTable(15);
        sapTable.setWidthPercentage(100);
        font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 4);


        //we want to display header even if there is no data
        SAP s = new SAP();
        List<String> attributeNames = s.getAllAttributesNames();
        for(String name : attributeNames){
            cell = new PdfPCell(new Phrase(name, font));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            sapTable.addCell(cell);
        }

        // sapTable content
        font = FontFactory.getFont(FontFactory.HELVETICA, 5);
        for(SAP sap : sapData) {
            List<String> attributes = sap.getAllAttributesValues();
            for (String value : attributes) {
                cell = new PdfPCell(new Phrase(value, font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                sapTable.addCell(cell);
            }
        }
        sapTable.setSpacingBefore(72f);
        document.add(sapTable);
        document.close();
    }

    /**
     * vytvorí graf pozostávajúci zo sĺpcového grafu a čiarového grafu
     * @param data všetky dáta o projekte zo SAP
     * @param title názov grafu
     * @param costsLimit maximálna hodnota kumulatívnych údajov v grafe
     * @return várti graf
     */
    public static JFreeChart getBarLineChart(LinkedHashMap<Period, BigDecimal> data, String title, BigDecimal costsLimit){

        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset limit = new DefaultCategoryDataset();

        LinkedHashMap<Period, BigDecimal> cumulativeData = ChartRenderer.getCumulativeData(data);

        for(Period p : data.keySet()){
            barDataset.addValue(data.get(p).doubleValue(), "monthly costs", p.toString());
            lineDataset.addValue(cumulativeData.get(p).doubleValue(), "cumulative costs", p.toString());
            limit.addValue(costsLimit.doubleValue(), "planned costs", p.toString());
        }

        final CategoryItemRenderer barRenderer = new BarRenderer();

        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(barDataset);
        plot.setRenderer( barRenderer);

        plot.setDomainAxis(new CategoryAxis("period"));
        plot.setRangeAxis(0,new NumberAxis("costs"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);


        final CategoryItemRenderer lineRenderer = new LineAndShapeRenderer();
        plot.setDataset(1, lineDataset);
        plot.setRenderer(1, lineRenderer);

        final LineAndShapeRenderer limitLineRenderer = new LineAndShapeRenderer();
        limitLineRenderer.setSeriesStroke(
                0, new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,
                        1.0f, new float[] {10.0f, 6.0f}, 0.0f
                )
        );

        final ValueAxis cumulativeAxis = new NumberAxis("cumulative");
        plot.setRangeAxis(1, cumulativeAxis);

        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);
        if(costsLimit.compareTo(BigDecimal.ZERO) > 0){
            plot.setDataset(2, limit);
            plot.setRenderer(2,limitLineRenderer);
            plot.mapDatasetToRangeAxis(2, 1);
            plot.getRendererForDataset(plot.getDataset(2)).setSeriesPaint(0, Color.orange);

        }

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle(title);

        return chart;
    }

    /**
     * vytvorí koláčový graf
     * @param data všetky dáta o projekte zo SAP
     * @param title názov grafu
     * @return vráti koláčový graf
     */
    private static JFreeChart getPieChart(LinkedHashMap<String, BigDecimal> data, String title){

        DefaultPieDataset dataset = new DefaultPieDataset( );

        for(String form:data.keySet()) {
            dataset.setValue(form + " " + String.format("%.2f", data.get(form)) + "€", data.get(form));
        }

        JFreeChart chart = ChartFactory.createPieChart(
                title,   // chart title
                dataset, // data
                true,
                true,
                false);

        return chart;
    }

    /**
     * z grafu vytvorí obraázok, ktorý možno použiť pri vytváraní PDF
     * @param data všetky dáta o projekte zo SAP
     * @param title názov grafu
     * @param planned plánovaná maximálna kumulatívna hodnota
     * @return
     * @throws IOException
     * @throws BadElementException
     */
    private static Image createLineBarChartImage(LinkedHashMap<Period, BigDecimal> data, String title, BigDecimal planned) throws IOException, BadElementException {
        JFreeChart ch = getBarLineChart(data, title, planned);

        BufferedImage chartImage = ch.createBufferedImage( 800, 500, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        Image iTextImage = Image.getInstance(baos.toByteArray());
        iTextImage.scaleAbsolute(500f, 300f);

        iTextImage.setAlignment(Element.ALIGN_CENTER);

        return iTextImage;
    }
    /**
     * z grafu vytvorí obraázok, ktorý možno použiť pri vytváraní PDF
     * @param data všetky dáta o projekte zo SAP
     * @param title názov grafu
     * @return
     * @throws IOException
     * @throws BadElementException
     */
    private static Image createPieChartImage(LinkedHashMap<String, BigDecimal> data, String title) throws IOException, BadElementException {
        JFreeChart ch = getPieChart(data, title);

        BufferedImage chartImage = ch.createBufferedImage( 800, 500, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        Image iTextImage = Image.getInstance(baos.toByteArray());
        iTextImage.scaleAbsolute(500f, 300f);

        iTextImage.setAlignment(Element.ALIGN_CENTER);

        return iTextImage;
    }
}
