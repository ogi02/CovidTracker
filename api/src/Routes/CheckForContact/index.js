const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');
const PendingNotification = require('../../Entity/PendingNotificationSchema');

const deviceRepository = getRepository(Device);
const pendingNotificationRepository = getRepository(PendingNotification);

const checkForContact = async (req, res) => {

    //Get user and check if it exists
    const device = await deviceRepository.findOne({ name: req.body.deviceName });
    if (!device) {
        res.status(404).json();
        return
    }

    //Return if contacted and remove notification pending
    const isContacted = await pendingNotificationRepository.findOne({ infected: device });
    if (isContacted) {
        await pendingNotificationRepository.delete({ infected: device });
        res.status(200).json({isContacted: true});
    } else {
        res.status(200).json({isContacted: false});
    }
};

exports.checkForContact = checkForContact;