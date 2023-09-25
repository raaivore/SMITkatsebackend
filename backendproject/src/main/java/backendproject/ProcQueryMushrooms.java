package backendproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.postgis.PGgeometry;
import org.postgis.Point;

public class ProcQueryMushrooms {

	public static void AddNewQueryResult(Connection connection, Point AddLocation, String AddDescription) {
		// SQL insert statement with NOW()
		String sqlInsert = "INSERT INTO querymushrooms (timestamp, features) VALUES (NOW(), ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
			// Set parameters
			String GeoJSON =  Utils.MakeGeoJSONString( AddLocation, AddDescription );
			preparedStatement.setObject(1, GeoJSON, Types.OTHER);

			System.out.println("Mushroom GeoJSON - " + GeoJSON + "added to the querymushrooms.");

			// Execute the SQL statement
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void ProcQuery(Connection connection ) {
		// SQL päring
		try {
			String sqlQuery = "SELECT location, description FROM mushrooms";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					PGgeometry geometry = (PGgeometry) resultSet.getObject("location");
					Point point = (Point) geometry.getGeometry();
			        String description = resultSet.getString("description");
					AddNewQueryResult( connection, point, description);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
