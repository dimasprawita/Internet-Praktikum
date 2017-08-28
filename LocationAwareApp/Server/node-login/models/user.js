'use strict';

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

/*const options = { 
    personModelName:            'user',
    friendshipModelName:        'Friendship', 
    friendshipCollectionName:   'userRelationships',
};

const FriendsOfFriends = require('friends-of-friends')(mongoose, options);*/

const userSchema = mongoose.Schema({ 

	name 			: String,
	email			: String, 
	hashed_password	: String,
	created_at		: String,
	temp_password	: String,
	temp_password_time: String
	
});

//userSchema.plugin(FriendsOfFriends.plugin,options);

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/node-login');

module.exports = mongoose.model('user', userSchema);
//module.exports = mongoose.model(options.personModelName, userSchema);        