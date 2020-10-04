const fetch = require("node-fetch");	
const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');

const deviceRepository = getRepository(Device);

const ml_url = 'http://gorchilov.net:5000/';

const getSensorData = async (req, res) => {

	// Get device name from request
	const deviceName = req.body.deviceName;

	// Get device from database and check if it exists
	const device = await deviceRepository.findOne({ name: deviceName });

	if (!device) {
		res.status(404).json();
		return;
	}

	// Send request to ml
	const request = await fetch(ml_url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Accept': 'application/json'
		},
		body: JSON.stringify(req.body.sensorsData)
	});

	// Wait for response from ml
	const response = await request.json();

	// Update database
	await deviceRepository.update({ name: deviceName }, { lastKnownTransport: response.transport });

	res.status(200).json();

};

exports.getSensorData = getSensorData;