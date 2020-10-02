const { createConnection } = require('typeorm');

<<<<<<< HEAD
createConnection().then(function () {
    const d = require('./Routes/Report');
    console.log(d.report())
=======
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

>>>>>>> 64875695a0d9f626685d8713e94ee67c28e09ee0
}).catch(err => console.log(err));
