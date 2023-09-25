package backendproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.postgis.Point;

public class ProcUpdateMushrooms {

	public static void UpdateMushroom(Connection connection, int MushroomId, Point newLocation, String newDescription) {

		// SQL update statement
		String sqlUpdate = "UPDATE mushrooms SET timestamp = NOW(), description = ?, location = ST_GeomFromText(?, 4326)  WHERE id = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
			// Set parameters
			preparedStatement.setString(1, newDescription);
	        preparedStatement.setString(2, "POINT(" + newLocation.getX() + " " + newLocation.getY() + ")");
			preparedStatement.setInt(3, MushroomId);

			// Execute the SQL statement
			int rowsUpdated = preparedStatement.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Row with ID " + MushroomId + " updated successfully.");
			} else {
				System.out.println("No row with ID " + MushroomId + " found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void GetUpdateMushrooms(Connection connection, Timestamp LastModifiedTS ) {
		String sqlQuery = "SELECT features->'geometry'->'coordinates' AS coordinates, "
				+ "features->'properties'->'description' AS description, mushroomid " + "FROM updatemushrooms WHERE timestamp > ?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setTimestamp(1, LastModifiedTS);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String coordinates = resultSet.getString("coordinates");
				String description = resultSet.getString("description");
                int mushroomId = resultSet.getInt("mushroomid");

                System.out.println("Coordinates: " + coordinates);
				System.out.println("Description: " + description);

				Point locationGeometry = Utils.GetPoinFromJSONLocation(coordinates);

				UpdateMushroom(connection, mushroomId, locationGeometry, description );
			}

			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
