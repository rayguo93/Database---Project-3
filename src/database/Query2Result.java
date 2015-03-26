package database;

public class Query2Result {
	
	private String username;
	public String getUsername() {
		return username;
	}

	public int getViewCount() {
		return viewCount;
	}

	private int viewCount;
	
	public Query2Result(String username, int viewCount) {
		this.username = username;
		this.viewCount = viewCount;
	}
	
}

