const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');

const contactRepository = getRepository(Contact);
const deviceRepository = getRepository(Device)

const checkForContact = async (req, res) => {

    const device = await deviceRepository.findOne({ name: req.body.deviceName });

    if (device) {
        
        contacts = await contactRepository.find({
            where: [
                {device1: device.id },
                {device2: device.id }, 
            ]
        });

        let isContacted = false;

        contacts.forEach(contact => {
            if ((contact.device1.isSick === 1 && contact.device1.id !== device.id) ||
                (contact.device2.isSick === 1 && contact.device2.id !== device.id)) {
                isContacted = true;
            }
        });

        res.status(200).json({isContacted: isContacted});

        return;   
    }

    res.status(404).json();
};

exports.checkForContact = checkForContact;