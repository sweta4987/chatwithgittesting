import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;

//java class to create stored procedures and tables
public class sweExecStoredProc {

    public static void main(String[] args) {

        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement()) {

            createTable(stmt);
            createStoredProcedure(stmt);

            // Create test data as an example.
            StringBuffer buffer = new StringBuffer(4000);
            for (int i = 0; i < 4000; i++)
                buffer.append((char) ('A'));

            try (PreparedStatement pstmt = con.prepareStatement(
                    "UPDATE Document_JDBC_Sample " + "SET DocumentSummary = ? WHERE (DocumentID = 1)")) {

                pstmt.setString(1, buffer.toString());
                pstmt.executeUpdate();
            }

            // Query test data by using a stored procedure.
            try (SQLServerCallableStatement cstmt = (SQLServerCallableStatement) con
                    .prepareCall("{call GetLargeDataValue(?, ?, ?, ?)}")) {

                cstmt.setInt(1, 1);
                cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
                cstmt.registerOutParameter(3, java.sql.Types.CHAR);
                cstmt.registerOutParameter(4, java.sql.Types.LONGVARCHAR);

                // Display the response buffering mode.
                System.out.println("Response buffering mode is: " + cstmt.getResponseBuffering());

                cstmt.execute();
                System.out.println("DocumentID: " + cstmt.getInt(2));
                System.out.println("Document_Title: " + cstmt.getString(3));

                try (Reader reader = cstmt.getCharacterStream(4)) {

                    System.out.println("Document_Summary in half: " + stringOutput1);
                    System.out.println("Document_Summary: " + stringOutput2);
                }
            }
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//java function to create stored procedure in sql db
    private static void swestrproc(Statement stmt) throws SQLException {
        String outputProcedure = "GetLargeDataValue";

        String sql = " IF EXISTS (select * from sysobjects where id = object_id(N'" + outputProcedure
                + "') and OBJECTPROPERTY(id, N'IsProcedure') = 1)" + " DROP PROCEDURE " + outputProcedure;
        stmt.execute(sql);

        sql = "CREATE PROCEDURE " + outputProcedure + " @p0 int, @p1 int OUTPUT, @p2 char(50) OUTPUT, "
                + "@p3 varchar(max) OUTPUT " + " AS" + " SELECT top 1 @p1=DocumentID, @p2=Title,"
                + " @p3=DocumentSummary FROM Document_JDBC_Sample where DocumentID = @p0";

        stmt.execute(sql);
    }
//java function to create table in sql db
    private static void swecreateTbl(Statement stmt) throws SQLException {
        stmt.execute("if exists (select * from sys.objects where name = 'sweDocument_JDBC_Sample')"
                + "drop table sweDocument_JDBC_Sample");

        String sql = "CREATE TABLE sweDocument_JDBC_Sample(" + "[DocumentID] [int] NOT NULL identity,"
                + "[Title] [char](50) NOT NULL," + "[DocumentSummary] [varchar](max) NULL)";

        stmt.execute(sql);

        sql = "INSERT sweDocument_JDBC_Sample VALUES ('title3','summary3') ";
        stmt.execute(sql);
    }
}