'use strict';

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const imgSchema = mongoose.Schema({ 

	data 			: Buffer,
	contentType		: String
	
});

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/node-login');

module.exports = mongoose.model('image', imgSchema); 