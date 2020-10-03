const { getRepository } = require('typeorm');
const Device = require('../../Model/Device').Device;

const deviceRepository = getRepository(Device);

const register = async (req, res) => {

	d = new Device(req.body.deviceName);

	const user = await deviceRepository.findOne({ name: d.name })

	if(!user) {
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

exports.register = register