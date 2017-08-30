'use strict'

const PlaceComment = require('../models/placecomment');
const UserComment = require('../models/usercomment');

/**
 * function to create to get place's comment. 
 */
exports.getPlaceComment = (venueID) => 

	new Promise((resolve,reject) => {

		const pID = venueID;

		PlaceComment.find({ venueID: pID }).populate('comments')

		.then(results => resolve(results))

		.catch(err => reject({ status: 500, message: 'Internal Server Error !' }))

	});

exports.deleteComment = (venueID,userID,comment) =>
	
	new Promise((resolve,reject) => {

		const pID = venueID;
	    const uID = userID;
	    const com = comment;

	    UserComment.find({ userID : uID }).where('comments').equals(com)

	    .then(results => {
	    	let user = results[0];
	    	
	    	new Promise((resolve,reject) => {
	    		PlaceComment.find({comments : user._id})
	    	.then(results => {
	    		let place = results[0];
	    		place.remove(0)
	    	})
	    	});
	    	
	    	user.remove();
	    	resolve(user);
	    	//resolve({status: 200, message: 'Successfully delete a comment !'});
	    })

		.catch(err => {
			if (err.code == 11000) {
						
				reject({ status: 409, message: 'Conflict !' });

			} else {

				reject({ status: 500, message: 'Internal Server Error !' });
			}
		});
	});

exports.likeComment = (venueID,userID,comment) =>

	new Promise((resolve,reject) => {

		const pID = venueID;
	    const uID = userID;
	    const com = comment;

	    UserComment.find({ userID : uID }).where('comments').equals(com)

	    .then(results => {
	    	let user = results[0];
	    	user.total_like += 1;
	    	return user.save();
	    })

	    //.then(user => resolve({ status: 200, message: 'Successfully like a comment !' }))
	    .then(user => resolve(user))

		.catch(err => {
			if (err.code == 11000) {
						
				reject({ status: 409, message: 'Conflict !' });

			} else {

				reject({ status: 500, message: 'Internal Server Error !' });
			}
		});
	});


exports.postComment = (venueID,userID,comment) =>

	new Promise((resolve,reject) => {

		const pID = venueID;
	    const uID = userID;
	    const com = comment;
	    const like = 0;

	    const uc = new UserComment({
	    	userID			: uID,
			comments 		: com,
			created_at		: new Date(),
			total_like		: like
	    });

		const pc = new PlaceComment({
			venueID			: pID,
			comments 		: uc
		});

		uc.save()
		pc.save()

		.then(() => resolve({ status: 201, message: 'Comment Registered Sucessfully !' }))

		.catch(err => {

			if (err.code == 11000) {
						
				reject({ status: 409, message: 'Conflict !' });

			} else {

				reject({ status: 500, message: 'Internal Server Error !' });
			}
		});
	});