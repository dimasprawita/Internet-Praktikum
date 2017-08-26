'use strict';

const auth = require('basic-auth');
const jwt = require('jsonwebtoken');

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

	router.get('/', (req, res) => res.end('NodeJS server is running successfully !'));

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

	router.post('/users', (req, res) => {

		const name = req.body.name;
		const email = req.body.email;
		const password = req.body.password;

		if (!name || !email || !password || !name.trim() || !email.trim() || !password.trim()) {

			res.status(400).json({message: 'Invalid Request !'});

		} else {

			register.registerUser(name, email, password)

			.then(result => {

				res.setHeader('Location', '/users/'+email);
				res.status(result.status).json({ message: result.message })
			})

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	router.get('/users/:id', (req,res) => {

		if (checkToken(req)) {

			profile.getProfile(req.params.id)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	router.delete('/users/:id', (req,res) => {

		if (checkToken(req)) {

			del.deleteUser(req.params.id)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

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

	router.get('/users/:id/search', (req,res) => {
		//if(!err) {

		if (checkToken(req)) {

			const friend = req.query.name;
		
			findFr.findFriend(friend)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));	
		}

		 else {

			res.status(401).json({ message: 'Invalid Token !' });
		}

		/*const friend = req.query.name;
		
		findFr.findFriend(friend)

		.then(result => res.json(result))

		.catch(err => res.status(err.status).json({ message: err.message }));*/

				
		//const fn = "\'"+friend+"\'";
		/*us.find({ name: fn }, (err, result) => {
			if(err) {
				res.status(500).send()
			}
			else{
				rest.status(200).send(result)
			}*/
			/*if(!friends){
				return res.status(404).send()
			}
			else{ 
				res.status(200).send(result)
			}
		})*/
	});

	/*const requireLoggedIn = (req, res) => {
		if(req.user) {
			return
		} else {
			return res.status(401).send()
		}
	}

	//todo: ensure user logged in
	router.post('/users/friends/add/:id', requireLoggedIn, (req, res) => {
		const userToAddId = req.params.id
		let user = req.user
		if(user.friends.length > 0) {
			user.friends.push({ id: userToAddId, accepted: false })
		} else {
			user.friends = [{ id: userToAddId, accepted: false }]
		}
		user.save((err) => {
			if(err){
				return res.status(500).send()
			}
			else {return res.status(200).send()
			}
		})
	})*/

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
				//var data = response.json.results;
    			res.send(response.json);
			}
			else {
				res.status(401).json({ message: 'Places not found!' });
			}
		});
	});

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
}