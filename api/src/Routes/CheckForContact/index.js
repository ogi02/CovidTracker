const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');

const contactRepository = getRepository(Contact);
const deviceRepository = getRepository(Device)

const checkForContact = async (req, res) => {
    const getDeviceId = async () => await (await deviceRepository.findOne({ name: req.body.name })).id;
    const deviceId = await getDeviceId();

    contactInfectedDevices = await contactRepository.find({
        where: [
            {device1: deviceId },
            {device2: deviceId },
        ]
    });

    res.send.json({isContacted: contactInfectedDevices ? false : true});
};

exports.checkForContact = checkForContact;