import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.Map;

public class ChartUtils {

    public static JPanel createPieChartPanel(Map<String, Double> data, String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        data.forEach(dataset::setValue);
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
        return new ChartPanel(chart);
    }

    public static JPanel createBarChartPanel(Map<String, Double> data, String title, String categoryAxis, String valueAxis) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((key, value) -> dataset.addValue(value, "Amount", key));
        JFreeChart chart = ChartFactory.createBarChart(title, categoryAxis, valueAxis, dataset);
        return new ChartPanel(chart);
    }
}