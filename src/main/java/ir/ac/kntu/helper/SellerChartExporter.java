package ir.ac.kntu.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SellerChartExporter {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void exportSellerChart(String agencyCode) {
        try (
            Connection conn = DriverManager.getConnection(DB_URL)
        ) {
            int sellerId = getSellerId(conn, agencyCode);
            if (sellerId == -1) {
                System.out.println("Seller not found.");
                return;
            }

            List<String[]> tableRows = new ArrayList<>();
            Map<String, Double> categoryTotals = getCategoryTotals(conn, sellerId, tableRows);
            String htmlContent = buildHtml(categoryTotals, tableRows);

            File htmlFile = writeToFile(htmlContent);
            openInBrowser(htmlFile);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static int getSellerId(Connection conn, String agencyCode) throws SQLException {
        String sql = "SELECT id FROM sellers WHERE agency_code = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, agencyCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;
        }
    }

    private static Map<String, Double> getCategoryTotals(Connection conn, int sellerId, List<String[]> tableRows) throws SQLException {
        String sql = "SELECT name, price, date FROM purchases WHERE seller_id = ?";
        Map<String, Double> totals = new LinkedHashMap<>();
        totals.put("Mobile", 0.0);
        totals.put("Book", 0.0);
        totals.put("Laptop", 0.0);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String date = rs.getString("date");

                switch (name.toLowerCase()) {
                    case "mobile" -> totals.merge("Mobile", price, Double::sum);
                    case "book" -> totals.merge("Book", price, Double::sum);
                    case "laptop" -> totals.merge("Laptop", price, Double::sum);
                }

                tableRows.add(new String[]{name, String.format("%.2f", price), date});
            }
        }
        return totals;
    }

    private static String buildHtml(Map<String, Double> totals, List<String[]> tableRows) {
        String labels = String.join(",", totals.keySet().stream().map(s -> "\"" + s + "\"").toList());
        String data = String.join(",", totals.values().stream().map(v -> String.format("%.2f", v)).toList());

        StringBuilder html = new StringBuilder();
        html.append("""
            <html>
            <head>
                <title>Seller Purchase Chart</title>
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
                <style>
                    body { font-family: Arial; margin: 40px; }
                    table { border-collapse: collapse; width: 80%; margin-top: 40px; }
                    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <h2>Seller Purchase Summary</h2>
                <canvas id="pieChart" width="400" height="400"></canvas>
                <script>
                    const ctx = document.getElementById('pieChart').getContext('2d');
                    new Chart(ctx, {
                        type: 'pie',
                        data: {
                            labels: [""").append(labels).append("""
                            ],
                            datasets: [{
                                data: [""").append(data).append("""
                                ],
                                backgroundColor: ['#ff6384', '#36a2eb', '#4bc0c0']
                            }]
                        },
                        options: {
                            plugins: {
                                legend: {
                                    position: 'bottom'
                                },
                                title: {
                                    display: true,
                                    text: 'Total Purchases by Product Type'
                                },
                                datalabels: {
                                    color: '#fff',
                                    formatter: (value) => '$' + value.toFixed(2),
                                    font: {
                                        weight: 'bold',
                                        size: 14
                                    }
                                }
                            }
                        },
                        plugins: [ChartDataLabels]
                    });
                </script>
                <h3>Purchase Details</h3>
                <table>
                    <tr><th>Name</th><th>Price</th><th>Date</th></tr>
        """);

        for (String[] row : tableRows) {
            html.append("<tr><td>").append(row[0]).append("</td><td>")
                .append(row[1]).append("</td><td>")
                .append(row[2]).append("</td></tr>\n");
        }

        html.append("""
                </table>
            </body>
            </html>
        """);

        return html.toString();
    }

    private static File writeToFile(String html) throws IOException {
        File file = new File("seller_purchases_chart.html");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(html);
        }
        return file;
    }

    private static void openInBrowser(File file) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(file.toURI());
        }
    }
}
