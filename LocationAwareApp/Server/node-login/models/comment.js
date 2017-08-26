'use strict';

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const CommentSchema = new Schema({
	userID			: String,
	venueID			: String,
	comments 		: String	
});

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/node-login');

module.exports = mongoose.model('comments', CheckInSchema);    