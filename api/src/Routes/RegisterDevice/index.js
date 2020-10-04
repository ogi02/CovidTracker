const { getRepository } = require('typeorm');
const Device = require('../../Model/Device').Device;

const deviceRepository = getRepository(Device);

const registerDevice = async (req, res) => {

	// Create device from device name
	d = new Device(req.body.deviceName);

	if(!d.name) {
		res.status(422).json();
		return;
	}

	console.log('register-device');

	// Get device and check if it exists
	const device = await deviceRepository.findOne({ name: d.name });

	if(!device) {
		// Insert device into database
		await deviceRepository.insert({
			name: d.name,
			isInfected: d.isInfected
		});

		res.status(201).json();
	}
	else {
		res.status(204).json();
	}

}

exports.registerDevice = registerDevice;
