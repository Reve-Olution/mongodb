package ch.globaz.techlunch.mongodb.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bson.Document;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.ReplicaSetStatus;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ch.globaz.techlunch.mongodb.dbconnection.DBConnection;
import ch.globaz.techlunch.mongodb.dbconnection.MongoDBs;

public class InsertDataAtFixedRateReplica {
	
	 
	 static Integer cpt = 0;
	 
	 private static final Logger mongoLogger = Logger.getLogger( "org.mongodb" );
	 private static final Logger logger = Logger.getLogger(InsertDataAtFixedRateReplica.class.getName());
	 
		public static void main(String[] args) {
			mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.
			//logger.setUseParentHandlers(false);
			
			final List<ServerAddress> addrs = new ArrayList<>();
			 addrs.add( new ServerAddress( "127.0.0.1" , 27018 ) );
			 addrs.add( new ServerAddress( "127.0.0.1" , 27019 ) );
			 addrs.add( new ServerAddress( "127.0.0.1" , 27020 ) );

			 final MongoClient mongoClient = new MongoClient(addrs);
			 final MongoDatabase db = mongoClient.getDatabase(MongoDBs.TECHLUNCH.dbName());
			
			 db.drop();
			
			Timer t = new Timer();
			
			t.scheduleAtFixedRate(new TimerTask() {
				  @Override
				  public void run() {
				    
					try{
						insertOne(db, cpt,mongoClient);
						cpt++;
						
					}catch (Throwable t){
						logger.severe(t.getMessage());
						//System.out.println("Exception: "  +t.getMessage());
						//t.printStackTrace();
					}  
				  }
				}, 5000, 5000);
			
			
		}
		
		private static void insertOne(MongoDatabase db, int cpt,MongoClient client){
			
			logger.info("Trying to insert data");
			
			MongoCollection<Document> c = db.getCollection("test_fixed_rate");
			
			if(null != client.getReplicaSetStatus().getMaster()){
				Document d = new Document()
						.append("master", getMasterInfosAsString(client))
						.append("test"+cpt, "ok");
				
				c.insertOne(d);
			
				logger.info("Data inserted: " + d.toJson());
				logger.info(getMasterInfosAsString(client));
				
				
				//System.out.println(getMasterInfosAsString(client));
			}else{
				logger.warning("Data not inserted: Master not found");
			}
			
		}
		
		private static String getMasterInfosAsString(MongoClient client){
			ReplicaSetStatus s = client.getReplicaSetStatus();
			ServerAddress a = s.getMaster();
			
			
				String host = a.getHost();
				int port = a.getPort();
				
				
				return "Primary: [" + host
						+ ":" + port + "]";  
		}
}
