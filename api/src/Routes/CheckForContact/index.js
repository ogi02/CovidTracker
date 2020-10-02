const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');

const contactRepository = getRepository(Contact);
const deviceRepository = getRepository(Device)

const checkForContact = async device => {
    const getDeviceId = async () => await (await deviceRepository.findOne({ name: device })).id;
    const deviceId = await getDeviceId();

    contactInfectedDevices = await contactRepository.find({ device2: deviceId } || { device2: deviceId });
    console.log(contactInfectedDevices);
};

exports.checkForContact = checkForContact;