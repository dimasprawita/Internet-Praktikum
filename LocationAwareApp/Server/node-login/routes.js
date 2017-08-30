'use strict';

const auth = require('basic-auth');
const jwt = require('jsonwebtoken');

const MongoClient = require('mongodb').MongoClient;
const url = "mongodb://localhost:27017/node-login";

const register = require('./functions/register');
const login = require('./functions/login');
const profile = require('./functions/profile');
const password = require('./functions/password');
const config = require('./config/config.json');
const del = require('./functions/delete');
const findFr = require('./functions/findFriend')
const user = require('./models/user');
const img = require('./models/image');
const chkIn = require('./functions/checkin');
const commFun = require('./functions/comment');

const googleMapsClient = require('@google/maps').createClient({
  key: 'AIzaSyD4msknaxGUuC2ZIA5AVRulS9CCK62Dspo'
});

const multer = require('multer')
const fileType = require('file-type')
const fs = require('fs')
const upload = multer({
    dest:'images/', 
    limits: {fileSize: 10000000, files: 1},
    fileFilter:  (req, file, callback) => {
    
        if (!file.originalname.match(/\.(jpg|jpeg)$/)) {

            return callback(new Error('Only Images are allowed !'), false)
        }

        callback(null, true);
    }
}).single('image')



module.exports = router => {

	/**
	* This is route to ensure that the server is running
	*/
	router.get('/', (req, res) => res.end('NodeJS server is running successfully !'));

	/**
	* This is route to authenticate the user login's data (username and password)
	*/
	router.post('/authenticate', (req, res) => {

		const credentials = auth(req);

		if (!credentials) {

			res.status(400).json({ message: 'Invalid Request !' });

		} else {

			login.loginUser(credentials.name, credentials.pass)

			.then(result => {

				const token = jwt.sign(result, config.secret, { expiresIn: 1440 });
			
				res.status(result.status).json({ message: result.message, token: token });

			})

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});


	/**
	* This is route to register a new user
	*/
	router.post('/users', (req, res) => {

		const name = req.body.name;
		const email = req.body.email;
		const age = parseInt(req.body.age);
		const city = req.body.city;
		const password = req.body.password;

		if (!name || !email || !password || !name.trim() || !email.trim() || !password.trim()
			|| !age || !city || !city.trim() ) {

			res.status(400).json({message: 'Invalid Request !'});

		} else {

			register.registerUser(name, email, password, age, city)

			.then(result => {

				res.setHeader('Location', '/users/'+email);
				res.status(result.status).json({ message: result.message })
			})

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	/**
	* This is route to get the user's data and 
	* send it to the front end part
	*/
	router.get('/users/:id', (req,res) => {

		if (checkToken(req)) {

			profile.getProfile(req.params.id)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	/**
	* This is route to update user's data. 
	*/
	router.put('/users/:id/profile', (req,res) => {
		
		const id = req.params.id;
		const name = req.body.name;
		const age = parseInt(req.body.age);
		const city = req.body.city;
		
		if(checkToken(req))
		{			
			if(!id || !name || !age || !city){

				res.status(400).json({ message: 'Invalid Request !' });
			}

			else{
				
				profile.changeProfile(req.params.id, name, age, city)

				.then(result => res.status(result.status).json({ message: result.message }))

				.catch(err => res.status(err.status).json({ message: err.message }));
			}
		}
		else
		{
			res.status(401).json({ message: 'Invalid Token !' });
		}

	});

	/**
	* This is route to delete the user 
	*/
	router.delete('/users/:id', (req,res) => {

		if (checkToken(req)) {

			del.deleteUser(req.params.id)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	/**
	* This is route to change user's password
	*/
	router.put('/users/:id', (req,res) => {

		if (checkToken(req)) {

			const oldPassword = req.body.password;
			const newPassword = req.body.newPassword;

			if (!oldPassword || !newPassword || !oldPassword.trim() || !newPassword.trim()) {

				res.status(400).json({ message: 'Invalid Request !' });

			} else {

				password.changePassword(req.params.id, oldPassword, newPassword)

				.then(result => res.status(result.status).json({ message: result.message }))

				.catch(err => res.status(err.status).json({ message: err.message }));

			}
		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	/**
	* This is route to reset password. 
	*/
	router.post('/users/:id/password', (req,res) => {

		const email = req.params.id;
		const token = req.body.token;
		const newPassword = req.body.password;

		if (!token || !newPassword || !token.trim() || !newPassword.trim()) {

			password.resetPasswordInit(email)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			password.resetPasswordFinish(email, token, newPassword)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	/**
	* This is route to search some specific user. 
	* A name that a user wants to be searched by user
	* is attached as query
	*/
	router.get('/users/:id/search', (req,res) => {

		if (checkToken(req)) {

			const friend = req.query.name;
		
			findFr.findFriend(friend)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));	
		}

		 else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	/**
	* This is route to get the user's friend
	* The user's email is used as id
	*/
	router.get('/users/friends/:id', checkTokenNext, (req, res) => {
		req.user.getFriends(function(err, friends) {
			if (err){
				console.log("error : " , err)
				return res.status(500).json({ error: err })
			}
		    console.log('friends', friends);
		    return res.status(200).json({friends})
		})
	});

	/**
	* This is route to send a friend request to specific user
	* :id is the one who sent the request
	* :idrequested is the one who will receive the request
	*/
	router.post('/users/:id/:idrequested', checkTokenNext, (req,res) => {
		//const uid = req.params.id;
		const requester = req.user;

		user.findOne({email : req.params.idrequested}, function(err,result){

			if (!req.params.idrequested) {
				res.status(400).json({message: 'Invalid Request !'});
			} 
			else 
			{
				//console.log("everything ok");
				requester.friendRequest(result._id, function (err, request) {
			    if (err)
			    	return res.status(500).json({ error: err })
			    console.log('request', request);
			    res.status(200).json({ request: request })				    
				});
			};

		});
		
	});


	/**
	* This is route to accept a friend request to specific user
	* :id is the user who received the request
	*/
	router.post('/users/:id/requests/accept', checkTokenNext, (req, res) => {
		//const uid = req.params.id;
		MongoClient.connect(url, function(err, db) {
  			if (err) throw err;
  			//const requester = db.collection("userRelationships").findOne({requested:requested}).requester;
  			const requested = req.user;
  			db.collection("userRelationships").findOne({requested:requested}, function(err,result){
  				requested.acceptRequest(result._id, function(err, friendship) {
				if (err){
					//console.log("erro : " , err)
					res.status(500).json({ error: err })
				}
				    //console.log('friendship', friendship);
				    res.status(200).json({ friendship: friendship })
				    db.close();	
				})
  			})
  		});
  	});	

	/**
	* This is route to reject a friend request to specific user
	* :id is the one who wants to reject the friend request
	*/
	router.post('/users/requests/:id/reject', checkToken, (req, res) => {
		const uid = req.params.id;
		//console.log("something went ",uid);
		req.user.denyRequest(uid, function(err, denied) {
			if (err){
				//console.log("erro : " , err)
				return res.status(500).json({ error: err })
			}
		    //console.log('denied', denied);
		    return res.status(200).json({ denied: denied })
		})
	})

	/**
	* This is route to get the nearby places
	* @return {response} json response from google place API
	*/
	router.get('/places', (req,res) => 	{
		const t = req.query.type;
		const ll = req.query.location;
		const r = parseInt(req.query.radius);

		googleMapsClient.places({
		location: ll,
		query: t,
		radius: r,
		}, function(err, response) {
			if(!err) {
    			res.send(response.json);
			}
			else {
				res.status(401).json({ message: 'Places not found!' });
			}
		});
	});

	/**
	* This is route to get a place detail
	* :id is the id of current place
	* @return {response} json response from google place API
	*/
	router.get('/places/:id', (req,res) => 	{
		const placeID = req.params.id;

		googleMapsClient.place({
		placeid: placeID,
		language: 'en'
		}, function(err, response) {
			if(!err) {
				res.send(response.json);
			}
			else {
				res.status(401).json({ message: 'Places not found!' });
			}
		});
	});	

	/**
	* This is route to create a check in in current place
	* :id is the id of current place
	* @return {response} the succeeded message
	*/
	router.post('/places/:id/checkin', (req,res) => {
		const pid = req.params.id;
		const uid = req.query.email;

		if (!pid || !uid) {

			res.status(400).json({message: 'Invalid Request !'});

		} else {

			chkIn.createCheckIn(pid,uid)

			.then(result => {

				res.setHeader('Location', '/places/'+pid+'/checkin/'+uid);
				res.status(result.status).json({ message: result.message })
			})

			.catch(err => res.status(err.status).json({ message: err.message }));
		}

	});

	/**
	* This is route to get a check in in one place
	* :id is the id of current place
	* @return {number} the number of checkin on the place
	*/
	router.get('/places/:id/checkin', (req,res) => {
		const pid = req.params.id;

		if(!pid)
		{
			res.status(400).json({message: 'Invalid Request !'});
		}

		else{
			chkIn.getCheckIn(pid)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	/**
	* This is route to get user's check in in one place
	* :id is the id of current place
	* @return {number} the number of user's check in on the place
	*/
	router.get('/places/:id/checkin', (req,res) => {
		const pid = req.params.id;
		const uid = req.query.email;

		if(!pid || !uid)
		{
			res.status(400).json({message: 'Invalid Request !'});
		}

		else{
			chkIn.getUserCheckIn(pid,uid)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	/**
	* This is route to get comments from users in current place
	* :id is the id of current place
	* @return {comment} the comments from all user in current place
	*/
	router.get('/places/:id/comments', (req,res) => {
		const pid = req.params.id;

		if(!pid)
		{
			res.status(400).json({message: 'Invalid Request !'});
		}

		else
		{
			commFun.getPlaceComment(pid)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}

	});

	/**
	* This is route to post comment in current place
	* :id is the id of current place
	* @return {message} the succeeded message
	*/
	router.post('/places/:id/comments', (req,res) => {
		const pid = req.params.id;
		const uid = req.query.user;
		const com = req.query.comment;

		if (!pid || !uid || !com) {

			res.status(400).json({message: 'Invalid Request !'});

		} else {

			commFun.postComment(pid,uid,com)

			.then(result => {

				res.setHeader('Location', '/places/'+pid+'/comments/'+uid);
				res.status(result.status).json({ message: result.message })
			})

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	/**
	* This is route to delete a comment in current place
	* :id is the id of current place
	* @return {result} the deleted comment
	*/
	router.delete('/places/:id/comments', (req,res) => {
		const pid = req.params.id;
		const uid = req.query.user;
		const com = req.query.comment;

		if(!pid || !uid || !com)
		{
			res.status(400).json({message: 'Invalid Request !'});
		}

		else
		{
			commFun.deleteComment(pid,uid,com)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}

	});

	/**
	* This is route to give rate to a comment
	* :id is the id of current place
	* @return {comment} the already rated comment
	*/
	router.put('/places/:id/comments', (req,res) => {
		const pid = req.params.id;
		const uid = req.query.user;
		const com = req.query.comment;

		if(!pid || !uid || !com)
		{
			res.status(400).json({message: 'Invalid Request !'});
		}

		else
		{
			commFun.likeComment(pid,uid,com)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}

	});


	router.post('/images/upload', (req,res) => {
		 upload(req, res, function (err) {

        if (err) {

            res.status(400).json({ message: err.message })

        } else {

            let path = `/images/${req.file.filename}`
            
            var imgname = req.file.filename
            var imagepath = __dirname + "/images/" + imgname
            var newImg = fs.readFileSync(imagepath);
            var encImg = newImg.toString('base64');

            const newImage = new img({
            	data: Buffer(encImg, 'base64'),
            	contentType: req.file.mimetype
            });

            newImage.save()

            res.status(200).json({message: 'Image Uploaded Successfully !', path: path})
        }
    	})
	})

	router.get('/images/:imagename', (req, res) => {

	    let imagename = req.params.imagename
	    let imagepath = __dirname + "/images/" + imagename
	    let image = fs.readFileSync(imagepath)
	    let mime = fileType(image).mime

		res.writeHead(200, {'Content-Type': mime })
		res.end(image, 'binary')
	})

	/**
	* Check the token from the request
	* to ensure that the request comes from specific user
	* @param {req} user's request
	*/
	function checkToken(req) {

		const token = req.headers['x-access-token'];

		if (token) {

			try {

  				var decoded = jwt.verify(token, config.secret);

  				return decoded.message === req.params.id;

			} catch(err) {

				return false;
			}

		} else {

			return false;
		}
	}

	/**
	* Check the token from the request
	* to ensure that the request comes from specific user
	* also set the user's objectID
	* @param {req} user's request
	* @param {res} response to user's
	* @param {next} do next function
	*/
	function checkTokenNext(req, res, next) {

			const token = req.headers['x-access-token'];
			console.log("debugging ", token);
			if (token) {

				try {

	  				var decoded = jwt.verify(token, config.secret);
	  				
	  				// attaching user to request
	  				user.findOne({ email: req.params.id }, function(err, user) {
				 		if(err || !user)
				 			return false
				 		req.user = user
				 		console.log(req.params.id)
				 		console.log("before attaching ", user)
				 		next();
			 		})

				} catch(err) {

					return false;
				}

			} else {

				return false;
			}
		}
}