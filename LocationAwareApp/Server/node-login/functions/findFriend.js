'use strict';

const user = require('../models/user');
const config = require('../config/config.json');

exports.findFriend = name => 
	
	new Promise((resolve,reject) => {

		user.find({ name: name })

		.then(users => resolve(users[0]))

		.catch(err => reject({ status: 404, message: 'User Not Found !' }))

	});