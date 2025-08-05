import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class BudgetTracker extends JFrame {

    JTextField incomeAmount = new JTextField(15);
    JTextField incomeCategory = new JTextField(15);
    JTextField expenseAmount = new JTextField(15);
    JTextField expenseCategory = new JTextField(15);

    Map<String, Double> incomeData = new HashMap<>();
    Map<String, Double> expenseData = new HashMap<>();

    JPanel reportPanel;
    JPanel incomeChartPanel;
    JPanel expenseChartPanel;
    JPanel summaryPanel;
    JLabel incomeLabel;
    JLabel expenseLabel;
    JLabel balanceLabel;

    public BudgetTracker() {
        setTitle("Budget Visualizer and Expense Tracker");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            setIconImage(new ImageIcon("uniuyo_logo.png").getImage());
        } catch (Exception e) {}

        DatabaseManager.initDatabase();

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Income", incomePanel());
        tabs.add("Expenses", expensePanel());
        tabs.add("Reports", createReportsPanel());
        tabs.add("About", aboutPanel());

        add(tabs);
        setVisible(true);
    }

    private JPanel incomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x33, 0x32, 0x31));
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setForeground(Color.WHITE);
        panel.add(amountLabel, gbc);

        gbc.gridx = 1;
        panel.add(incomeAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setForeground(Color.WHITE);
        panel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        panel.add(incomeCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton save = new JButton("Submit Income");
        save.setPreferredSize(new Dimension(200, 40));
        save.setBackground(new Color(46, 139, 87));
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        save.setFont(new Font("SansSerif", Font.BOLD, 16));
        save.addActionListener(this::handleAddIncome);

        panel.add(save, gbc);

        return panel;
    }

    private JPanel expensePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x33, 0x32, 0x31));
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setForeground(Color.WHITE);
        panel.add(amountLabel, gbc);

        gbc.gridx = 1;
        panel.add(expenseAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setForeground(Color.WHITE);
        panel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        panel.add(expenseCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton save = new JButton("Submit Expense");
        save.setPreferredSize(new Dimension(200, 40));
        save.setBackground(new Color(46, 139, 87));
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        save.setFont(new Font("SansSerif", Font.BOLD, 16));
        save.addActionListener(this::handleAddExpense);

        panel.add(save, gbc);

        return panel;
    }

    private JPanel createReportsPanel() {
        reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBackground(new Color(0x33, 0x32, 0x31));
        summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        summaryPanel.setBackground(new Color(0x33, 0x32, 0x31));
        incomeLabel = new JLabel();
        expenseLabel = new JLabel();
        balanceLabel = new JLabel();
        incomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        expenseLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        incomeLabel.setForeground(Color.WHITE);
        expenseLabel.setForeground(Color.WHITE);
        balanceLabel.setForeground(Color.WHITE);
        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(balanceLabel);
        reportPanel.add(summaryPanel, BorderLayout.NORTH);

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2));
        chartsPanel.setBackground(new Color(0x33, 0x32, 0x31));
        incomeChartPanel = ChartUtils.createPieChartPanel(incomeData, "Income Breakdown");
        expenseChartPanel = ChartUtils.createBarChartPanel(expenseData, "Expense Overview", "Category", "Amount");
        chartsPanel.add(incomeChartPanel);
        chartsPanel.add(expenseChartPanel);
        reportPanel.add(chartsPanel, BorderLayout.CENTER);

        updateSummaryLabels();
        return reportPanel;
    }

    private JPanel aboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x33, 0x32, 0x31));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel aboutContent = new JPanel();
        aboutContent.setLayout(new BoxLayout(aboutContent, BoxLayout.Y_AXIS));
        aboutContent.setBackground(new Color(0x33, 0x32, 0x31));

        JLabel imgLabel = new JLabel();
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon yesufuIcon = new ImageIcon("Yesufu.jpg");
        if (yesufuIcon.getIconWidth() > 0 && yesufuIcon.getIconHeight() > 0) {
            Image scaledImg = yesufuIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(scaledImg));
        } else {
            imgLabel.setText("[Image not found]");
            imgLabel.setForeground(Color.WHITE);
        }

        JTextArea aboutText = new JTextArea();
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setFont(new Font("Serif", Font.PLAIN, 16));
        aboutText.setForeground(Color.WHITE);
        aboutText.setBackground(new Color(0x33, 0x32, 0x31));
        aboutText.setText(
            "Budget Visualizer and Expense Tracker\n" +
            "-----------------------------------------\n" +
            "Computer Science Department\n" +
            "Developer Name: Yesufu El-Kamil Edima\n" +
            "Developer RegNo: 22/SC/CO/1170\n" +
            "DeveloperEmail: yesufu@gmail.com\n" +
            "Developer Role: Frontend Engineer & Java Developer\n" +
            "Developer School: University of Uyo\n" +
            "DeveloperPurpose: To create a user-friendly application for tracking and visualizing personal finances on COS222(Java) Project\n" +
            "This app helps track income and expenses and visualize data for smarter financial decisions."
        );

        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        aboutContent.add(Box.createVerticalStrut(20));
        aboutContent.add(imgLabel);
        aboutContent.add(Box.createVerticalStrut(20));
        aboutContent.add(scrollPane);

        panel.add(aboutContent, BorderLayout.CENTER);
        return panel;
    }

    private void refreshReportsPanel() {
        updateSummaryLabels();
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2));
        chartsPanel.setBackground(new Color(0x33, 0x32, 0x31));
        incomeChartPanel = ChartUtils.createPieChartPanel(incomeData, "Income Breakdown");
        expenseChartPanel = ChartUtils.createBarChartPanel(expenseData, "Expense Overview", "Category", "Amount");
        chartsPanel.add(incomeChartPanel);
        chartsPanel.add(expenseChartPanel);
        reportPanel.remove(1);
        reportPanel.add(chartsPanel, BorderLayout.CENTER);
        reportPanel.revalidate();
        reportPanel.repaint();
    }

    private void updateSummaryLabels() {
        double totalIncome = incomeData.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpense = expenseData.values().stream().mapToDouble(Double::doubleValue).sum();
        double balance = totalIncome - totalExpense;
        incomeLabel.setText("Total Income: " + String.format("%.2f", totalIncome));
        expenseLabel.setText("Total Expense: " + String.format("%.2f", totalExpense));
        balanceLabel.setText("Balance: " + String.format("%.2f", balance));
    }

    private void handleAddIncome(ActionEvent e) {
        try {
            double amount = Double.parseDouble(incomeAmount.getText());
            String category = incomeCategory.getText();
            String date = LocalDate.now().toString();
            DatabaseManager.addIncome(amount, category, date);
            incomeData.put(category, incomeData.getOrDefault(category, 0.0) + amount);
            refreshReportsPanel();
            JOptionPane.showMessageDialog(this, "Income added successfully!");
            incomeAmount.setText("");
            incomeCategory.setText("");
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void handleAddExpense(ActionEvent e) {
        try {
            double amount = Double.parseDouble(expenseAmount.getText());
            String category = expenseCategory.getText();
            String date = LocalDate.now().toString();
            DatabaseManager.addExpense(amount, category, date);
            expenseData.put(category, expenseData.getOrDefault(category, 0.0) + amount);
            refreshReportsPanel();
            JOptionPane.showMessageDialog(this, "Expense added successfully!");
            expenseAmount.setText("");
            expenseCategory.setText("");
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetTracker::new);
    }
}
