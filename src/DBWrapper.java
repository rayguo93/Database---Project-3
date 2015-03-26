import java.util.ArrayList;
import java.sql.* ;

/**
 * 
 * USAGE:
 * 1) Instantiate the DBWrapper ( DBWrapper db = new DBWrapper(); )
 * 2) Make as many queries as you want (e.g. db.query1(3); )
 * 3) Call close when done ( db.close(); )
 * 
 */

public class DBWrapper {
	
	// SQL connection specific
	private Connection con;
	private Statement statement;
    private int sqlCode = 0;
    private String sqlState = "00000";
	
    // Table names
	private final String CUSTOMER_TABLE 		= "customer";
	private final String ADVERTISEMENT_TABLE 	= "advertisement";
	private final String TAG_TABLE 				= "tag";
	private final String AD_TAG_MAP_TABLE 		= "ad_tag_map";
	private final String CONVO_TABLE 			= "convo";
	private final String MESSAGE_TABLE 			= "message";
    
	public DBWrapper() {
		// Register the driver.  You must register the driver before you can use it.
		try {
			DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ;
		} catch (Exception cnfe){
			System.out.println("Class not found");
		}
		
		init();
	}

	/******** FIVE QUERIES 
	 * @throws SQLException ********/
	// query 1
	public ArrayList<Query1Result> query1(int x) throws SQLException {
		System.out.println("QUERY 1");
		String selectSQL = "select tag_name, count(*) count from ad_tag_map group by tag_name order by count desc fetch first " + x + " rows only";
		ResultSet rs = statement.executeQuery(selectSQL);
		ArrayList<Query1Result> results = new ArrayList<Query1Result>();
		while(rs.next()) {
			String name = rs.getString(1);
			int count = rs.getInt(2);
			results.add(new Query1Result(name, count));
		}
		return results;
	}
	
	// query 2
	public Query2Result query2() throws SQLException {		
		System.out.println("QUERY 2");
		String selectSQL = "select username, s from customer inner join (select creator_id, sum(unique_view_count) s from advertisement group by creator_id order by s desc fetch first 1 rows only) A on A.creator_id = customer.uid";		
		ResultSet rs = statement.executeQuery(selectSQL);
		rs.next();
		String username = rs.getString(1);
		int viewCount = rs.getInt(2);
		return new Query2Result(username, viewCount);
	}
	
	// query 3
	public ArrayList<String> query3(int x) throws SQLException {
		System.out.println("QUERY 3");
		String selectSQL = "select content from message order by created DESC fetch first " + x + " rows only";
		ResultSet rs = statement.executeQuery(selectSQL);
		ArrayList<String> results = new ArrayList<String>();
		while(rs.next()) {
			String content = rs.getString(1);
			results.add(content);
		}
		return results;
	}
	
	// query 4
	public ArrayList<String> query4(int price) throws SQLException {
		System.out.println("QUERY 4");
		String selectSQL = "select distinct c1.username from customer c1, advertisement a1, advertisement a2 where c1.uid = a1.creator_id and a1.price > " + price + " and c1.uid = a2.creator_id and a2.price > " + price + " and a1.ad_id != a2.ad_id";
		ResultSet rs = statement.executeQuery(selectSQL);
		ArrayList<String> results = new ArrayList<String>();
		while(rs.next()) {
			String username = rs.getString(1);
			System.out.println(username);
			results.add(username);
		}
		return results;
	}
	
	// query 5
	public ArrayList<Query5Result> query5(int numAds, int numMessages) throws SQLException {
		System.out.println("QUERY 5");
		String selectSQL = "(select username, email from customer inner join (select uid from message group by uid having count(*) > " + numMessages + ") M on customer.uid = M.uid) intersect (select username, email from customer inner join (select creator_id from advertisement group by creator_id having count(*) > " + numAds + ") A on customer.uid = A.creator_id)";
		ResultSet rs = statement.executeQuery(selectSQL);
		ArrayList<Query5Result> results = new ArrayList<Query5Result>();
		while(rs.next()) {
			String username = rs.getString(1);
			String email = rs.getString(2);
			results.add(new Query5Result(username, email));
		}
		return results;
	}
	
	
	/******** Database initialization ********/
	private void init() {
		openConnection();
		createTables();
		insertData();
	}

	private void openConnection() {
		String url = "jdbc:db2://db2.cs.mcgill.ca:50000/cs421";
		String user = "cs421g31";
		String pswd = "eddouncy";
		System.out.println("Attempting to connect.");
		try {
			con = DriverManager.getConnection (url, user, pswd);
			System.out.println("Connection successful.");
			statement = con.createStatement();
		} catch (SQLException e) {
			System.out.println("Connection failed.");
			System.out.println(e);
            sqlCode = e.getErrorCode();
            sqlState = e.getSQLState();
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
		}
	}

	private void createTables() {
		try {
			// creating customer
		    String createSQL = "create table " + CUSTOMER_TABLE + " (uid int not null, username varchar(30) not null,	email varchar(30) not null,	phone varchar(15),	password varchar(30) not null,	cart_cost decimal default 0 not null check(cart_cost >= 0),	cart_ads_count int default 0 not null check(cart_ads_count >= 0), is_banned int default 0, primary key (uid))";
		    statement.executeUpdate(createSQL);
		    System.out.println("Created table customer successfully");
		    
		    // creating advertisement
		    createSQL = "create table " + ADVERTISEMENT_TABLE + " (ad_id int not null, creator_id int not null, created timestamp not null, description varchar(1000),	name varchar(50) not null,	price decimal check(price >= 0), unique_view_count int default 0 not null,	primary key (ad_id), foreign key (creator_id) references customer(uid))";
		    statement.executeUpdate(createSQL);
		    System.out.println("Created table advertisement successfully");
		    
		    // creating tag
		    createSQL = "create table " + TAG_TABLE + " ( tag_name varchar(20) not null, primary key (tag_name))";
		    statement.executeUpdate(createSQL);
		    System.out.println("Created table tag successfully");
		    
		    // creating ad_tag_map
		    createSQL = "create table " + AD_TAG_MAP_TABLE + " ( ad_id int not null, tag_name varchar(20) not null, primary key (ad_id, tag_name), foreign key (tag_name) references tag(tag_name))";
		    statement.executeUpdate(createSQL);
		    System.out.println("Created table ad_tag_map successfully");
		    
		    // creating convo
		    createSQL = "create table " + CONVO_TABLE + " ( convo_id int not null, title varchar(50), created timestamp,  initiator_uid int not null, receiver_uid int not null, ad_id int, primary key (convo_id), foreign key (initiator_uid) references customer(uid), foreign key (receiver_uid) references customer(uid), foreign key (ad_id) references advertisement(ad_id))";
		    statement.executeUpdate(createSQL);
		    System.out.println("Created table convo successfully");
		    
		    // creating message
		    createSQL = "create table " + MESSAGE_TABLE + " ( msg_id int not null, content varchar(1000) not null, created timestamp,  uid int not null, convo_id int not null, primary key (msg_id), foreign key (uid) references customer(uid), foreign key (convo_id) references convo(convo_id))";
		    statement.executeUpdate(createSQL);
		    System.out.println("Created table message successfully");
		    
		    
		}
		catch (SQLException e) {
			System.out.println("e: " + e);
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            
            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }
		
	}
	
	private void insertData() {
	    try {
			String insertSQL = "insert into " + CUSTOMER_TABLE + " values (1, 'ale', 'ale@ale.com', '4944944', 'secret', 0, 0, 0) union all values (2, 'steven', 'steven@st.ca', '4944944', 'evenmoresecrete', 0, 0, 0) union all values (3, 'lorent', 'lo@rent.com', '4976644', 'topsecrete', 0 , 0, 0) union all values (4, 'raydoe', 'ra@ra.com', '4911144', 'thiswillbehashed', 100, 1, 0) union all values (5, 'heydoe', 'doe@koe.com', '4944999', 'hashmap', 500, 1, 0) union all values (6, 'aldoe', 'hey@al.doe', '4922999', 'doenotdo', 0, 0, 0) union all values (7, 'aldo', 'hey@al.do', '4922998', 'donotdoe', 0, 0, 0)";
			statement.executeUpdate(insertSQL);
			System.out.println("Inserted data in table customer");
			
			insertSQL = "insert into " + ADVERTISEMENT_TABLE + " values (1, 1, TIMESTAMP('2014-12-01-00.00.00'), 'apple iphone 4 new', 'ad1', cast(null as decimal), 0) union all  values (2, 1, TIMESTAMP('2014-12-02-00.00.00'), cast(null as varchar(1000)), 'ad2', 10.50, 3) union all  values (3, 1, TIMESTAMP('2014-12-03-00.00.00'), cast(null as varchar(1000)), 'ad3', 200.00, 0) union all  values (4, 2, TIMESTAMP('2014-12-02-00.00.00'), cast(null as varchar(1000)), 'ad4', cast(null as decimal), 5) union all  values (5, 2, TIMESTAMP('2014-12-03-00.00.00'), cast(null as varchar(1000)), 'ad5', 1000.00, 50) union all  values (6, 2, TIMESTAMP('2014-12-04-00.00.00'), 'new phone!', 'ad6', 500.00, 100) union all  values (7, 3, TIMESTAMP('2014-12-03-00.00.00'), cast(null as varchar(1000)), 'ad7', 5.25, 0) union all  values (8, 3, TIMESTAMP('2014-12-04-00.00.00'), cast(null as varchar(1000)), 'ad8', cast(null as decimal), 0) union all  values (9, 3, TIMESTAMP('2014-12-05-00.00.00'), 'best sell nice phone', 'ad9', cast(null as decimal), 1) union all  values (10, 4, TIMESTAMP('2014-12-01-00.00.00'), cast(null as varchar(1000)), 'ad10', 15.00, 20) union all  values (11, 4, TIMESTAMP('2014-12-03-00.00.00'), cast(null as varchar(1000)), 'ad11', 20.00, 75) union all  values (12, 4, TIMESTAMP('2014-12-05-00.00.00'), cast(null as varchar(1000)), 'ad12', cast(null as decimal), 10) union all 	 values (13, 5, TIMESTAMP('2014-12-04-00.00.00'), cast(null as varchar(1000)), 'ad13', 20.00, 4) union all  values (14, 5, TIMESTAMP('2014-12-05-00.00.00'), cast(null as varchar(1000)), 'ad14', 300.00, 0) union all  values (15, 5, TIMESTAMP('2014-12-06-00.00.00'), cast(null as varchar(1000)), 'ad15', 10.00, 10) union all  values (16, 5, TIMESTAMP('2014-12-07-00.00.00'), cast(null as varchar(1000)), 'ad16', 15.00, 30) union all  values (17, 5, TIMESTAMP('2014-12-07-00.00.00'), cast(null as varchar(1000)), 'ad17', cast(null as decimal),  25) union all  values (18, 5, TIMESTAMP('2014-12-08-00.00.00'), cast(null as varchar(1000)), 'ad18', 5.00,  50) union all  values (19, 6, TIMESTAMP('2014-12-07-00.00.00'), cast(null as varchar(1000)), 'ad19', 10.00,  25) union all  values (20, 6, TIMESTAMP('2014-12-08-00.00.00'), cast(null as varchar(1000)), 'ad20', 5.00,  5)";
			statement.executeUpdate(insertSQL);
			System.out.println("Inserted data in table advertisement");

			insertSQL = "insert into " + TAG_TABLE + " values ('apple') union all values ('iphone4') union all values ('iphone4s') union all values ('iphone5') union all values ('ipad2') union all values ('samsung galaxy s4') union all values ('ipad air') union all values ('nokia')";
			statement.executeUpdate(insertSQL);
			System.out.println("Inserted data in table tag");

			insertSQL = "insert into " + AD_TAG_MAP_TABLE + " values (1, 'apple') union all values (1, 'iphone4') union all values (1, 'iphone4s') union all values (1, 'iphone5') union all values (2, 'nokia') union all values (3, 'samsung galaxy s4') union all values (4, 'iphone5') union all values (5, 'ipad air') union all values (6, 'ipad2') union all values (7, 'nokia') union all values (8, 'apple') union all values (8, 'iphone4') union all values (8, 'iphone4s') union all values (9, 'iphone4') union all values (10, 'nokia') union all values (11, 'nokia') union all values (12, 'nokia') union all values (13, 'nokia') union all values (14, 'apple') union all values (14, 'iphone4s') union all values (15, 'nokia') union all values (16, 'nokia') union all values (17, 'ipad2') union all values (18, 'nokia')";
			statement.executeUpdate(insertSQL);
			System.out.println("Inserted data in table ad_tag_map");

			insertSQL = "insert into " + CONVO_TABLE + " values (1, 'interested in', TIMESTAMP('2014-12-10-00.00.00'), 1, 2, 4) union all values (2, cast(null as varchar(50)), TIMESTAMP('2014-12-11-00.00.00'), 1, 3, 7) union all values (3, 'I want', TIMESTAMP('2014-12-12-00.00.00'), 1, 4, 10) union all values (4, 'too expensive', TIMESTAMP('2014-12-12-00.00.00'), 2, 1, 1) union all values (5, cast(null as varchar(50)), TIMESTAMP('2014-12-12-00.00.00'), 3, 5, 14) union all values (6, cast(null as varchar(50)), TIMESTAMP('2014-12-13-00.00.00'), 4, 2, 5) union all values (7, cast(null as varchar(50)), TIMESTAMP('2014-12-14-00.00.00'), 5, 2, 6) union all values (8, 'Very interested', TIMESTAMP('2014-12-15-00.00.00'), 2, 3, 8) union all values (9, 'Willing to pay more if necessary', TIMESTAMP('2014-12-15-00.00.00'), 1, 2, 4) union all values (10, cast(null as varchar(50)), TIMESTAMP('2014-12-16-00.00.00'), 1, 5, 18)";
			statement.executeUpdate(insertSQL);
			System.out.println("Inserted data in table convo");

			insertSQL = "insert into " + MESSAGE_TABLE + " values (1, 'Hey', TIMESTAMP('2014-12-10-13.00.23'), 1, 1) union all values (2, 'Hello, what do you want?', TIMESTAMP('2014-12-10-13.30.44'), 2, 1) union all values (3, 'I want to buy', TIMESTAMP('2014-12-10-13.31.23'), 1, 1) union all values (4, 'ok', TIMESTAMP('2014-12-10-13.32.20'), 2, 1) union all values (5, 'ty', TIMESTAMP('2014-12-10-13.32.40'), 1, 1) union all values (6, 'Hello', TIMESTAMP('2014-12-11-01.01.01'), 1, 2) union all values (7, 'Sold', TIMESTAMP('2014-12-12-17.10.58'), 3, 2) union all values (8, 'Let me buy', TIMESTAMP('2014-12-12-13.50.18'), 1, 3) union all values (9, 'dont sell to anyone else', TIMESTAMP('2014-12-12-18.05.40'), 2, 4) union all values (10, 'wtf', TIMESTAMP('2014-12-14-02.34.12'), 1, 4) union all values (11, 'bro, I NEED THIS', TIMESTAMP('2014-12-12-12.12.12'), 3, 5) union all values (12, 'ok, I''ll double the price then', TIMESTAMP('2014-12-12-12.15.23'), 5, 5) union all values (13, 'wait wtf why?', TIMESTAMP('2014-12-12-12.16.23'), 3, 5) union all values (14, 'Supply and demand bro', TIMESTAMP('2014-12-12-12.16.43'), 5, 5) union all values (15, '...', TIMESTAMP('2014-12-12-12.20.00'), 3, 5) union all values (16, 'hellow', TIMESTAMP('2014-12-13-00.00.00'), 4, 6) union all values (17, 'yo', TIMESTAMP('2014-12-14-01.01.13'), 5, 7) union all values (18, 'give it to me', TIMESTAMP('2014-12-16-19.00.01'), 1, 10) union all values (19, 'maybe', TIMESTAMP('2014-12-16-19.01.01'), 5, 10) union all values (20, 'why maybe?', TIMESTAMP('2014-12-16-19.01.31'), 1, 10) union all values (21, 'cause prove you have money', TIMESTAMP('2014-12-16-19.02.32'), 5, 10) union all values (22, 'I''m bill gates', TIMESTAMP('2014-12-16-19.05.23'), 1, 10) union all values (23, 'kk I''ll set price at 1 million', TIMESTAMP('2014-12-16-19.05.59'), 5, 10) union all values (24, 'lol wow ok peace', TIMESTAMP('2014-12-16-20.00.13'), 1, 10) union all values (25, 'jokes, it''s yours', TIMESTAMP('2014-12-16-21.14.14'), 5, 10) union all values (26, 'ok thanks', TIMESTAMP('2014-12-16-21.14.45'), 1, 10)";
			statement.executeUpdate(insertSQL);
			System.out.println("Inserted data in table message");

		} catch (SQLException e) {
			System.out.println("e: " + e);
			e.printStackTrace();
		}
		
	}
	
	/******** Database closing ********/
	public void close() {
		dropTables();
		closeConnection();
	}


	private void dropTables() {
		try{
			
			String dropSQL = "drop table message";
		    statement.executeUpdate(dropSQL);
			System.out.println("Dropped message table");
			
			dropSQL = "drop table convo";
		    statement.executeUpdate(dropSQL);
			System.out.println("Dropped convo table");

			dropSQL = "drop table ad_tag_map";
		    statement.executeUpdate(dropSQL);
			System.out.println("Dropped ad_tag_map table");

			dropSQL = "drop table tag";
		    statement.executeUpdate(dropSQL);
			System.out.println("Dropped tag table");
			
			dropSQL = "drop table advertisement";
		    statement.executeUpdate(dropSQL);
			System.out.println("Dropped advertisement table");
			
			dropSQL = "drop table customer";
		    statement.executeUpdate(dropSQL);
			System.out.println("Dropped customer table");
			

	    }
		catch (SQLException e) {
			System.out.println("e: " + e);
	        sqlCode = e.getErrorCode(); // Get SQLCODE
	        sqlState = e.getSQLState(); // Get SQLSTATE
	        
	        // Your code to handle errors comes here;
	        // something more meaningful than a print would be good
	        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
	}

	private void closeConnection() {
		System.out.println("Attempting to close connection.");
		try {
			statement.close();
			con.close();
			System.out.println("Connection closed successfully.");
		} catch (SQLException e) {
			System.out.println("e: " + e);
			System.out.println("Something went wrong while attempting to close connection.");
            sqlCode = e.getErrorCode();
            sqlState = e.getSQLState();
            
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
		}
	}
	
}
