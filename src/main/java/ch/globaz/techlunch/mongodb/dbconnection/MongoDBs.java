package ch.globaz.techlunch.mongodb.dbconnection;

public enum MongoDBs {

	TEST("test"),
	TECHLUNCH("techlunch");
	
	private String dbName;
	
	MongoDBs(String dbName){
		this.dbName = dbName;
	}
	
	public String dbName(){
		return this.dbName;
	}
}
