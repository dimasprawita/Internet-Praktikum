'use strict';

const user = require('../models/user');

exports.getProfile = email => 
	
	new Promise((resolve,reject) => {

		user.find({ email: email }, { name: 1, email: 1, created_at: 1, _id: 0, age:1, city:1 })

		.then(users => resolve(users[0]))

		.catch(err => reject({ status: 500, message: 'Internal Server Error !' }))

	});

exports.changeProfile = (email, name, age, city) => 
	
	new Promise((resolve,reject) => {

		const email = email;
		const name = name;
		const age = age;
		const city = city;

		user.find({ email: email })

		.then(users => {
			let result = users[0];
			result.name = name;
			result.age = age;
			result.city = city;
			return user.save();
		})

		.then(user => resolve({ status: 200, message: 'Profile Updated Sucessfully !' }))

		.catch(err => reject({ status: 500, message: 'Internal Server Error !' }))

	});