package database;

public class Query5Result {
	
	String username;
	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	String email;
	
	public Query5Result(String username, String email) {
		this.username = username;
		this.email = email;
	}
	
}
