package app.exporter;

import app.App;
import app.db.Project;
import app.db.SAP;

import java.awt.*;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import app.gui.graph.Period;
import app.gui.project.ProjectFilter;
import app.service.ChartService;
import app.service.ProjectService;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;


import javax.imageio.ImageIO;

public class PdfExporter {

    public static void exportPdf(List<Project> projectData, List<SAP> sapData) throws IOException, DocumentException {

        String dest = "C:/tmp/samp.pdf";
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
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
            List<String> attributes = project.getAllAttributes();
            for(String att : attributes){
                cell = new PdfPCell(new Phrase(att, font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                projectTable.addCell(cell);
            }
        }
        document.add(projectTable);
/* adding chart - not good approach, resolution is baad
        LinkedHashMap<Period, BigDecimal> monthlyCostsData = ChartService.getChartService().getRDCostsData(projectData.get(0).getProjectNumber(), ProjectFilter.getInstance().getFrom(), ProjectFilter.getInstance().getTo());
        BigDecimal planned = ProjectService.getProjectService().getPlanedDDCosts(projectData.get(0).getProjectNumber());
        JFreeChart ch = getBarLineChart(monthlyCostsData, "Project "+projectData.get(0).getProjectNumber()+" R&D costs", planned);

        BufferedImage chartImage = ch.createBufferedImage( 300, 200, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        Image iTextImage = Image.getInstance(baos.toByteArray());
        document.add(iTextImage);
*/
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

        document.add(sapTable);
        document.close();
    }

    public static JFreeChart getBarLineChart(LinkedHashMap<Period, BigDecimal> data, String title, BigDecimal planned){

        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();

        for(Period p : data.keySet()){
            barDataset.addValue(data.get(p).doubleValue(), "bar", p.toString());
            lineDataset.addValue(data.get(p).doubleValue(), "bar", p.toString());
        }

        final CategoryItemRenderer barRenderer = new BarRenderer();

        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(barDataset);
        plot.setRenderer( barRenderer);

        plot.setDomainAxis(new CategoryAxis("period"));
        plot.setRangeAxis(new NumberAxis("costs"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        final ValueAxis cumulativeAxis = new NumberAxis("cumulative");
        plot.setRangeAxis(1, cumulativeAxis);

        final CategoryItemRenderer lineRenderer = new LineAndShapeRenderer();
        plot.setDataset(1, lineDataset);
        plot.setRenderer(1, lineRenderer);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle(title);

        return chart;
    }
}
