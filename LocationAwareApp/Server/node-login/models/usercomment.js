'use strict';

/**
* The schema for user's comment.
* It will store the user ID, user's comment
* the time the comment is created and
* the number of like for that comment
*/


const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const UserCommentSchema = new Schema({
	userID			: String,
	comments 		: String,
	created_at		: String,
	total_like		: Number
});

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/node-login');

module.exports = mongoose.model('user_comments', UserCommentSchema);