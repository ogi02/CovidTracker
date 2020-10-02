const { getRepository } = require('typeorm');
const Device = require('../../Model/Device').Device;
const Contact = require('../../Model/Contact').Contact;

const deviceRepository = getRepository(Device);
const contactRepository = getRepository(Contact);

const contact = async (req, res) => {

	d1 = await deviceRepository.findOne({ name: req.body.deviceName1 });
	d2 = await deviceRepository.findOne({ name: req.body.deviceName2 });

	if(d1 && d2) {
		c = new Contact(d1.id, d2.id);

		await contactRepository.insert({
			device1: c.deviceName1,
			device2: c.deviceName2,
			timestamp: c.timestamp
		});

		res.sendStatus(201);
	} else {
		res.sendStatus(404);
	}

}

exports.contact = contact