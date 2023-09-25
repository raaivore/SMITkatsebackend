package backendproject;

import org.postgis.Point;

public class Utils {
	
	/* Katsume ilma Jacksonita läbi ajada */
	
	public static Point  GetPoinFromJSONLocation( String JSONLocation ) {
		
        // Eemaldame sulud "[" ja "]" ning jagame koordinaadid koma kohalt
        String[] coordinatesArray = JSONLocation.replaceAll("[\\[\\]]", "").split(",");

        // Eraldame koordinaadid
        double x = Double.parseDouble(coordinatesArray[0].trim());
        double y = Double.parseDouble(coordinatesArray[1].trim());

        // Väljastame koordinaadid
        System.out.println("X coordinate: " + x);
        System.out.println("Y coordinate: " + y);
        
        Point point = new Point( x, y );
        
        return point;
		
	}

	public static String MakeGeoJSONString(Point Location, String Description) {
		String GeoJSON = null;
		GeoJSON = "{\"type\": \"Feature\",\"geometry\":{\"type\": \"Point\", \"coordinates\":[";
		GeoJSON = GeoJSON + String.valueOf(Location.getX()) + "," + String.valueOf(Location.getY()) + "]},";
		GeoJSON = GeoJSON + "\"properties\":{\"description\": " + Description + "}}";
		return GeoJSON;
	}
}
