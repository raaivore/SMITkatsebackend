package backendproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BackendMain {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/mushroom_database";
    private static final String USER = "mushroomer";
    private static final String PASSWORD = "";
   
    private static void deleteAllRecordsFromTable(Connection connection, String tableName) {
        try {
            // Loome SQL päringu kõikide kirjete kustutamiseks tabelist
            String deleteQuery = "DELETE FROM " + tableName;
            
            // Loome PreparedStatement objekti
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            
            // Käivitame kustutamise päringu
            statement.executeUpdate(deleteQuery);
            
            System.out.println("Kõik kirjed on kustutatud tabelist: " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void ProcClean(Connection connection) {
        deleteAllRecordsFromTable(connection, "addmushrooms");
        deleteAllRecordsFromTable(connection, "deletemushrooms");
        deleteAllRecordsFromTable(connection, "updatemushrooms");
        deleteAllRecordsFromTable(connection, "querymushrooms");
    }
  		
    public static void main(String[] args) {
        Connection connection = null;
        boolean Clean = false;
        Timestamp LastModifiedTS = Timestamp.valueOf("1970-01-01 00:00:00");
        
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.equals("clean")) {
                    Clean = true;
                    break;  
                }
            }
        }
        
        try {
            // Connect to the PostgreSQL database
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            // SQL päring vanima timestamp-i leidmiseks
            String query = "SELECT timestamp AS oldest_timestamp FROM mushrooms";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Kui leiti tulemus
            while (resultSet.next()) {
            	Timestamp oldest_timestamp = resultSet.getTimestamp("oldest_timestamp");
            	if( oldest_timestamp.after(LastModifiedTS) )
            		LastModifiedTS = oldest_timestamp;
            } 
            System.out.println("Vanim timestamp: " + LastModifiedTS);

        } catch (SQLException e) {
            e.printStackTrace();
        } 

        ProcDeleteMushrooms.GetDeleteMushrooms(connection, LastModifiedTS);
        ProcAddMushrooms.GetAddMushrooms(connection, LastModifiedTS);
        ProcUpdateMushrooms.GetUpdateMushrooms(connection, LastModifiedTS);

        if(Clean)
        	ProcClean(connection);
        
        ProcQueryMushrooms.ProcQuery( connection );
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace( );
        }
    }
}
