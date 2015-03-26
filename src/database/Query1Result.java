package database;

public class Query1Result {
	
	private String name;
	public String getName() {
		return name;
	}

	private int count;
	public int getCount() {
		return count;
	}
	
	public Query1Result(String name, int count) {
		this.name = name;
		this.count = count;
	}
	
}
