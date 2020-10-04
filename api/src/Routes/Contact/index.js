const { getRepository } = require('typeorm');
const Device = require('../../Model/Device').Device;
const Contact = require('../../Model/Contact').Contact;

const deviceRepository = getRepository(Device);
const contactRepository = getRepository(Contact);

const reportContact = async (req, res) => {

	d1 = await deviceRepository.findOne({ name: req.body.deviceName1 });
	d2 = await deviceRepository.findOne({ name: req.body.deviceName2 });

	if (d1.name === d2.name) {
		res.status(422).json();
		return;
	}
	if (d1 && d2) {
		c = new Contact(d1.id, d2.id);

		await contactRepository.insert({
			device1: c.device1Id,
			device2: c.device2Id,
			timestamp: c.timestamp
		});

		res.status(201).json();
	} else {
		res.status(404).json();
	}

}

exports.reportContact = reportContact