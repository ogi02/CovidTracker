const { createConnection } = require('typeorm');

createConnection().then(() => {

	const express = require('express');
	const app = express();
	module.exports = app;

	const registerDevice = require('./Routes/RegisterDevice');
	const contact = require('./Routes/Contact');
	const report = require('./Routes/Report');
	const checkForContact = require('./Routes/CheckForContact');

	app.post('/registerDevice', registerDevice.register);
	// app.post('/contact', contact.sth);
	// app.post('/report', report.sth);
	// app.get('/checkForContact', checkForContact.sth);

	registerDevice.register("d1", "sth");

}).catch(err => console.log(err));
