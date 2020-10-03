const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');

const contactRepository = getRepository(Contact);
const deviceRepository = getRepository(Device)

const checkForContact = async (req, res) => {
    const getDeviceId = async () => {
        const device = await deviceRepository.findOne({ name: req.body.name });

        return device ? device.id : null;
    }
    const deviceId = await getDeviceId() ? await getDeviceId() : null;
    if (deviceId) {
        contactInfectedDevices = await contactRepository.find({
            where: [
                {device1: deviceId },
                {device2: deviceId }, 
            ]
        });
        res.json({isContacted: contactInfectedDevices.length !== 0 ? true : false});
        return;   
    }
    res.status(404).send('Device do not exist in our database');
};

exports.checkForContact = checkForContact;