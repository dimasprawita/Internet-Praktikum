'use strict'

const PlaceComment = require('../models/placecomment');
const UserComment = require('../models/usercomment');


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

	    UserComment.find({comments:com})

	    .then(() => resolve({ status: 201, message: 'Comment Deleted Sucessfully !' }))

		.catch(err => {

			if (err.code == 11000) {
						
				reject({ status: 409, message: 'Conflict !' });

			} else {

				reject({ status: 500, message: 'Internal Server Error !' });
			}
		});

		PlaceComment.find({})
	});


exports.postComment = (venueID,userID,comment) =>

	new Promise((resolve,reject) => {

		const pID = venueID;
	    const uID = userID;
	    const com = comment;

	    const uc = new UserComment({
	    	userID			: uID,
			comments 		: com
	    });

		const pc = new PlaceComment({
			venueID			: pID,
			comments 		: uc
		});

		uc.save()
		pc.save()

		/*pc.save(function(err){
			if(err) return handleError(err);

			uc.save(function(err) {
				if(err) return handleError(err)
			});

		})*/

		.then(() => resolve({ status: 201, message: 'Comment Registered Sucessfully !' }))

		.catch(err => {

			if (err.code == 11000) {
						
				reject({ status: 409, message: 'Conflict !' });

			} else {

				reject({ status: 500, message: 'Internal Server Error !' });
			}
		});
	});