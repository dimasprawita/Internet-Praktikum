'use strict';

const user = require('../models/user');
const config = require('../config/config.json');

exports.deleteUser = email => 
	
	new Promise((resolve,reject) => {

		user.findOneAndRemove({ email: email }, (err, users) => {
			if(err) {
				reject({status: 500, message: 'Internal Server Error !'})
			}
			else {
				resolve({ status: 204, message: 'User Deleted Sucessfully !' })
			}
		})

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