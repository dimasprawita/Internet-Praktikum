'use strict';

const CheckIn = require('../models/checkin');

/**
 * function to create check in. It will
 * save the userID and venueID in the schema
 */
exports.createCheckIn = (venID, userID) => 

	new Promise((resolve,reject) => {

	    const vID = venID;
		const uID = userID;

		const newCheckIn = new CheckIn({
			userID : uID,
			venueID : venID
		});

		newCheckIn.save()

		.then(() => resolve({ status: 201, message: 'CheckIn Registered Sucessfully !' }))

		.catch(err => {

			if (err.code == 11000) {
						
				reject({ status: 409, message: 'User Already Registered !' });

			} else {

				reject({ status: 500, message: 'Internal Server Error !' });
			}
		});
	});


/**
 * function to get place's check in. 
 */
exports.getCheckIn = (venID) => 

	new Promise((resolve,reject) => {

		//var numCheckIn = CheckIn.find({ venueID: venID });
		CheckIn.find({ venueID: venID })

		.then(results => resolve(results.length))

		.catch(err => reject({ status: 500, message: 'Internal Server Error !' }))

	});

/**
 * function to get user check in in a place. 
 */
exports.getUserCheckIn = (venID,userID) =>
	
	new Promise((resolve,reject) => {

		//var numCheckIn = CheckIn.find({ venueID: venID });
		CheckIn.find({ venueID: venID }).where('userID').equals(userID)

		.then(results => resolve(results.length))

		.catch(err => reject({ status: 500, message: 'Internal Server Error !' }))

	});



