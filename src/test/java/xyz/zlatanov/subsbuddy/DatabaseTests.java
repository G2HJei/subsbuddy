package xyz.zlatanov.subsbuddy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import lombok.val;

@SpringBootTest
public class DatabaseTests {

	@Autowired
	MongoTemplate mongo;

	// @Test // to be tested with deployment on test environment for acceptance testing
	void database_setup_works() {
		val collection = "db_test";
		if (mongo.collectionExists(collection)) {
			mongo.dropCollection(collection);
		}
		val hogan = mongo.insert(Document.parse("{\"hulk\":\"hogan\"}"), collection);
		assertNotNull(hogan.get("_id"));
		mongo.remove(hogan, collection);
		val found = mongo.find(Query.query(Criteria.where("_id").is(hogan.get("_id"))), String.class, collection);
		assertTrue(found.isEmpty());
		mongo.dropCollection(collection);
	}
}