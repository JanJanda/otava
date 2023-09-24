package io.github.janjanda.otava.web_worker;

import java.sql.*;

public final class WebWorker {
    public static void main(String[] args) throws SQLException {
        Connection conn = getConnection();
        findNewJob(conn);
    }

    private static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/otava", "root", System.getenv("OTAVA_DB_PSW"));
        conn.setAutoCommit(false);
        System.out.println("Connected to database");
        return conn;
    }

    private static RequestData findNewJob(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             PreparedStatement updateRow = conn.prepareStatement("UPDATE `validations` SET `state` = 'working' WHERE `id` = ?;");
             PreparedStatement selectData = conn.prepareStatement("SELECT * FROM `validations` WHERE `id` = ?;")) {

            if (!stmt.execute("SELECT MIN(`id`) FROM `validations` WHERE `state` = 'queueing';")) return null;
            ResultSet minId = stmt.getResultSet();
            minId.next();
            String rowId = minId.getString(1);
            if (rowId == null) return null;

            updateRow.setString(1, rowId);
            updateRow.executeUpdate();

            selectData.setString(1, rowId);
            ResultSet rs = selectData.executeQuery();
            rs.next();
            RequestData requestData = new RequestData(rs.getString("id"), rs.getString("language"), rs.getString("style"),
                    rs.getString("passive-tables"), rs.getString("active-tables"), rs.getString("passive-descriptors"), rs.getString("active-descriptors"));

            conn.commit();

            return requestData;
        }
    }

    private record RequestData(String id, String language, String style, String passiveTables, String activeTables, String passiveDescriptors, String activeDescriptors) {}
}
