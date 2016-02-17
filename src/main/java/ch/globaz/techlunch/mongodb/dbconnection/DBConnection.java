package ch.globaz.techlunch.mongodb.dbconnection;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DBConnection {


	private static final String HOST = "localhost";
	private static final int PORT = 27018;
	
	
	public static MongoDatabase getMongoDBInstanceFor (MongoDBs db) {
		return getClient().getDatabase(db.dbName());
	}

	private static MongoClient getClient() {
		// To connect to mongodb server
        return new MongoClient(HOST, PORT );
	}
}
