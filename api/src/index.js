const { createConnection } = require('typeorm');

createConnection().then(() => {

	const express = require('express');
	const app = express();
	module.exports = app;

	const registerDevice = require('./Routes/RegisterDevice');
	const reportContact = require('./Routes/Contact');
	const reportInfectedness = require('./Routes/Report');
	const checkForContact = require('./Routes/CheckForContact');

	app.use('/', express.json());

	app.use('/', (req, res, next) => {
		console.log('\n\n');
		console.log(req.body);
		next();
	});

	app.post('/register-device', registerDevice.registerDevice);
	app.post('/report-contact', reportContact.reportContact);
	app.post('/report-infectedness', reportInfectedness.reportInfectedness);
	app.post('/check-for-contact', checkForContact.checkForContact);

	app.listen(3000);

}).catch(err => console.log(err));
