'use strict';

/**
* a model for comment in specific place
* the schema for thi model will include the schema
* of user's comment.
*/

const mongoose = require('mongoose');
const UserCommentSchema = require('./usercomment')

const Schema = mongoose.Schema;

const PlaceCommentSchema = new Schema({
	venueID			: String,
	comments 		: {type: mongoose.Schema.Types.ObjectId, ref: 'user_comments'}	
});

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/node-login');

module.exports = mongoose.model('place_comments', PlaceCommentSchema);    