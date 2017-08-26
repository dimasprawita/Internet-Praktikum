var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/node-login";

	//without to array
	MongoClient.connect(url, function(err, db) {
		if (err) throw err;
		//Find all documents in the customers collection:
		db.collection("users").find({}, function(err, result) {
		    if (err) throw err;
		    console.log(result);
		    db.close();
		});
	});

	MongoClient.connect(url, function(err, db) {
  		if (err) throw err;
  		db.collection("users").find({}).toArray(function(err, result) {
	    if (err) throw err;
	    console.log(result);
	    db.close();
  });
});

