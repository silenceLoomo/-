import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.PreparedStatement;


public class SparkSQLConnect {
	 public static void main(String[] args) throws SQLException {
		 
		String username = null;
		String password = null;
		String url = null;
		Properties properties = new Properties();
		properties.setProperty("driverClassName", "org.apache.hive.jdbc.HiveDriver");
		properties.setProperty("user", username);
	    properties.setProperty("password", password);
	    
	    Connection con = DriverManager.getConnection(url, properties);
	    Statement stmt = con.createStatement();
		String sql1 = "SHOW DATABASES";
		System.out.println("Running"+sql1);
		ResultSet res = stmt.executeQuery(sql1);
		while(res.next()){
			System.out.println(res.getString("database"));
			System.out.println();
		}
		
		getTables(con);
	 }
	 
	 public static String convertDatabaseCharsetType(String in, String type) {  
	        String dbUser;  
	        if (in != null) {  
	            if (type.equals("oracle")) {  
	                dbUser = in.toUpperCase();  
	            } else if (type.equals("postgresql")) {  
	                dbUser = "public";  
	            } else if (type.equals("mysql")) {  
	                dbUser = null;  
	            } else if (type.equals("mssqlserver")) {  
	                dbUser = null;  
	            } else if (type.equals("db2")) {  
	                dbUser = in.toUpperCase();  
	            } else {  
	                dbUser = in;  
	            }  
	        } else {  
	            dbUser = "public";  
	        }  
	        return dbUser;  
	    }  

	 
	 private static void getTables(Connection conn) throws SQLException {  
	        DatabaseMetaData dbMetData = conn.getMetaData();  
	        // mysql convertDatabaseCharsetType null  
	        ResultSet rs = dbMetData.getTables(null,  
	                convertDatabaseCharsetType("root", "mysql"), null,  
	                new String[] { "TABLE", "VIEW" });  

	        while (rs.next()) {  
	            if (rs.getString(4) != null  
	                    && (rs.getString(4).equalsIgnoreCase("TABLE") || rs  
	                            .getString(4).equalsIgnoreCase("VIEW"))) {  
	                String tableName = rs.getString(3).toLowerCase();  
	                System.out.print("tableName: "+tableName + "\t");  
	                // 根据表名提前表里面信息：  
	                ResultSet colRet = dbMetData.getColumns(null, "%", tableName,  
	                        "%");  
	                System.out.println();  
	                while (colRet.next()) {  
	                    String columnName = colRet.getString("COLUMN_NAME");  
	                    String columnType = colRet.getString("TYPE_NAME");  
	                    int datasize = colRet.getInt("COLUMN_SIZE");  
	                    int digits = colRet.getInt("DECIMAL_DIGITS");  
	                    int nullable = colRet.getInt("NULLABLE");  
	                    System.out.println(columnName + " " + columnType + " "+  
	                            datasize + " " + digits + " " + nullable);  
	                }  

	            }  
	        }  
	        System.out.println();  

	        //         resultSet数据下标从1开始 
	        ResultSet tableRet =  conn.getMetaData().getTables(null, null, "%", new String[] { "TABLE" });  
	        while (tableRet.next()) {  
	            System.out.print(tableRet.getString(3) + "\t");  
	        }  
	        System.out.println();  
	    }  

	 	
}
