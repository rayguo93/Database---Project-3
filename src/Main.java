import java.sql.SQLException;


public class Main {

	public static void main(String[] args) {
		DBWrapper db = new DBWrapper();
		try {
			db.query5(1, 2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.close();
	}
	
}
