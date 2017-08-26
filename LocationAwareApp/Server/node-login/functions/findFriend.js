'use strict';

const user = require('../models/user');
const config = require('../config/config.json');

exports.findFriend = name => 
	
	new Promise((resolve,reject) => {

		user.find({ name: name })

		.then(users => resolve(users[0]))

		.catch(err => reject({ status: 404, message: 'User Not Found !' }))

		/*.then(users => {

			if (users.length == 0) {

				reject({ status: 404, message: 'User Not Found !' });

			} else {

				let user = users[0];
				return user.deleteOne();
			}
		})

		.then(() => resolve({ status: 201, message: 'User Deleted Sucessfully !' }))

		.catch(err => reject({ status: 500, message: 'Internal Server Error !' }))*/

	});