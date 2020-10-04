const { createConnection } = require('typeorm');

createConnection().then(() => {

	// Include express
	const express = require('express');
	const app = express();
	module.exports = app;

	// Get function for routes
	const registerDevice = require('./Routes/RegisterDevice');
	const reportContact = require('./Routes/ReportContact');
	const reportInfectedness = require('./Routes/ReportInfectedness');
	const checkForContact = require('./Routes/CheckForContact');
	const sensorData = require('./Routes/SensorData');

	app.use('/', express.json());

	app.use('/', (req, res, next) => {
		console.log('\n\n');
		console.log(req.body);
		next();
	});

	// Set post request handlers
	app.post('/register-device', registerDevice.registerDevice);
	app.post('/report-contact', reportContact.reportContact);
	app.post('/report-infectedness', reportInfectedness.reportInfectedness);
	app.post('/check-for-contact', checkForContact.checkForContact);
	app.post('/sensor-data', sensorData.getSensorData);

	app.listen(process.env.SERVER_PORT);

}).catch(err => console.log(err));
