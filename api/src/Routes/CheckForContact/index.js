const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');
const PendingNotification = require('../../Entity/PendingNotificationSchema');

const deviceRepository = getRepository(Device);
const pendingNotificationRepository = getRepository(PendingNotification);

const checkForContact = async (req, res) => {

    // Get user and check if it exists
    const device = await deviceRepository.findOne({ name: req.body.deviceName });
    if (!device) {
        res.status(404).json();
        return;
    }

    console.log('sensor-data');

    // Return notification and remove notification pending
    const notification = await pendingNotificationRepository.findOne({ contacted: device });
    if (notification) {
        await pendingNotificationRepository.delete({ contacted: device });

        res.status(200).json({
            isContacted: true,
            transport1: notification.lastKnownTransport1,
            transport2: notification.lastKnownTransport2,
            timestamp: notification.timestamp
        });

    } else {
        res.status(200).json({ isContacted: false });
    }
};

exports.checkForContact = checkForContact;