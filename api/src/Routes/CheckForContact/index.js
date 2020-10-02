const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');

const deviceRepository = getRepository(Device);

const checkForContact = device => {
    contactInfectedDevices = deviceRepository.find({ isInfected: 1 });
    console.log(contactInfectedDevices);
};

exports.checkForContact = checkForContact;