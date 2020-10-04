const fetch = require("node-fetch");	
const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');

const deviceRepository = getRepository(Device);

const ml_url = 'http://gorchilov.net:5000/';

const getSensorData = async (req, res) => {

	const deviceName = req.body.deviceName;

	const device = await deviceRepository.findOne({ name: deviceName });

	if (!device) {
		res.status(404).json();
		return;
	}

	delete req.body.deviceName;

	const request = await fetch(ml_url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Accept': 'application/json'
		},
		body: JSON.stringify(req.body)
	});

	const response = await request.json();

	console.log(response.transport);

	await deviceRepository.update({ name: deviceName }, { lastKnownTransport: response.transport });

	res.status(200).json();

};

exports.getSensorData = getSensorData;