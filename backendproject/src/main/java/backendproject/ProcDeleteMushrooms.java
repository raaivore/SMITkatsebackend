package backendproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProcDeleteMushrooms {

    private static void deleteRecordFromMushrooms(Connection connection, int MushroomId ) {
        try {
            // Loome SQL päringu kõikide kirjete kustutamiseks tabelist
            String deleteQuery = "DELETE FROM mushrooms WHERE id = ?";
            
            // Loome PreparedStatement objekti
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            
            // Määrame parameetri väärtuse
            statement.setInt(1, MushroomId);
            
            // Käivitame kustutamise päringu
            statement.executeUpdate();
            
            // Sulgeme ressursid
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	public static void GetDeleteMushrooms(Connection connection, Timestamp LastModifiedTS ) {
        
        // SQL päring
         String sqlQuery = "SELECT mushroomid FROM deletemushrooms WHERE timestamp > ?";

        try {
            // Prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setTimestamp(1, LastModifiedTS); 

            // Käivitame päringu ja saame tulemused
            ResultSet resultSet = preparedStatement.executeQuery();

            // Töötleme tulemused
            while (resultSet.next()) {
                int mushroomId = resultSet.getInt("mushroomid");
                System.out.println("Kusttame Mushroom ID: " + mushroomId);
                deleteRecordFromMushrooms( connection, mushroomId );
            }

            // Sulgeme ressursid
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
