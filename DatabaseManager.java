import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/budget_tracker";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Arua08141";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initDatabase() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS income (
                    id SERIAL PRIMARY KEY,
                    amount DECIMAL(10, 2),
                    category VARCHAR(50),
                    date DATE
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS expense (
                    id SERIAL PRIMARY KEY,
                    amount DECIMAL(10, 2),
                    category VARCHAR(50),
                    date DATE
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS savings (
                    id SERIAL PRIMARY KEY,
                    target DECIMAL(10, 2),
                    current DECIMAL(10, 2)
                );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addIncome(double amount, String category, String date) throws SQLException {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO income (amount, category, date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, category);
            pstmt.setDate(3, Date.valueOf(date));
            pstmt.executeUpdate();
        }
    }

    public static void addExpense(double amount, String category, String date) throws SQLException {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO expense (amount, category, date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, category);
            pstmt.setDate(3, Date.valueOf(date));
            pstmt.executeUpdate();
        }
    }

    static void addSavings(double target, double current) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}