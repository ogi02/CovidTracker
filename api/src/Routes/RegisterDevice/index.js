const { getRepository } = require('typeorm');
const Device = require('../../Model/Device').Device;

const deviceRepository = getRepository(Device);

const register = async (req, res) => {

	d = new Device(req.body.name);

	const user = await deviceRepository.findOne({ name: d.name })

	if(!user) {
		await deviceRepository.insert({
			name: d.name,
			isInfected: d.isInfected
		});

		res.sendStatus(201);
	}
	else {
		res.sendStatus(204);
	}

}

exports.register = register