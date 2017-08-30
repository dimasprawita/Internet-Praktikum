'use strict';

/**
* The model for check in.
* It will store the user id and the place id
*/

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const CheckInSchema = new Schema({
	userID			: String,
	venueID			: String 	
});

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/node-login');

module.exports = mongoose.model('checkin', CheckInSchema);    