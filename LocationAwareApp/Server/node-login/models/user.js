'use strict';

/**
* create a user model and implements the node module
* friend-of-friends to extend the functionality of
* user model.
*/

const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const options = { 
    personModelName:            'Player',
    friendshipModelName:        'Friend_Relationships', 
    friendshipCollectionName:   'userRelationships',
};

const FriendsOfFriends = require('friends-of-friends')(mongoose, options);

const userSchema = mongoose.Schema({ 

	name 			: String,
	email			: String, 
	hashed_password	: String,
	created_at		: String,
	age				: Number,
	city			: String,
	temp_password	: String,
	temp_password_time: String
	
});

userSchema.plugin(FriendsOfFriends.plugin,options);

var user = mongoose.model(options.personModelName, userSchema);


mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/node-login');

module.exports = mongoose.model('user', userSchema);
//module.exports = mongoose.model(options.personModelName, userSchema);        