package ch.globaz.techlunch.mongodb.example;

import java.util.Timer;
import java.util.TimerTask;

import org.bson.Document;

import com.mongodb.DB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import ch.globaz.techlunch.mongodb.dbconnection.DBConnection;
import ch.globaz.techlunch.mongodb.dbconnection.MongoDBs;

public class InsertDataAtFixedRate {

	
	static Integer cpt = 0;
	
	public static void main(String[] args) {
		
		Timer t = new Timer();
		final MongoDatabase db = DBConnection.getMongoDBInstanceFor(MongoDBs.TECHLUNCH);
		
		t.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
			    insertOne(db, cpt);
				cpt++;  
			  }
			}, 5000, 5000);
	}
	
	private static void insertOne(MongoDatabase db, int cpt){
		MongoCollection c = db.getCollection("test_fixed_rate");
		
		c.insertOne(new Document()
				.append("test"+cpt, "ok"));
	}
}
