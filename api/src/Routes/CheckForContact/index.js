const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');
const PendingNotification = require('../../Entity/PendingNotificationSchema');

const deviceRepository = getRepository(Device);
const pendingNotificationRepository = getRepository(PendingNotification);

const checkForContact = async (req, res) => {

    const device = await deviceRepository.findOne({ name: req.body.deviceName });

    if (device) {

        const isContacted = await pendingNotificationRepository.findOne({ infected: device });

        if (isContacted) {
            await pendingNotificationRepository.delete({ infected: device });
            res.status(200).json({isContacted: true});
        } else {
            res.status(200).json({isContacted: false});
        }

        return;   
    }

    res.status(404).json();
};

exports.checkForContact = checkForContact;