const { createConnection } = require('typeorm');

createConnection().then(() => {

	const express = require('express');
	const app = express();
	module.exports = app;

	const registerDevice = require('./Routes/RegisterDevice');
	const contact = require('./Routes/Contact');
	const report = require('./Routes/Report');
	const checkForContact = require('./Routes/CheckForContact');

	app.use('/', (req, res, next) => {
		console.log(req);
		next();
	})

	app.post('/registerDevice', registerDevice.register);
	app.post('/reportContact', contact.contact);
	app.post('/report', report.report);
	// app.get('/checkForContact', checkForContact.sth);

	app.listen(3000)

}).catch(err => console.log(err));
