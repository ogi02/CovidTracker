const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');
const PendingNotification = require('../../Entity/PendingNotificationSchema');

const deviceRepository = getRepository(Device);
const contactRepository = getRepository(Contact);
const pendingNotificationRepository = getRepository(PendingNotification);

const reportInfectedness = async (req, res) => {
    // Get user by name and check if it exists
    const device = await deviceRepository.findOne({ name: req.body.deviceName });
    if (!device) {
        res.status(422).json();
        return;
    }

    console.log('report-infectedness');
    console.log(req.body.deviceName);

    // Set user to infected
    await deviceRepository.update({ name: req.body.deviceName }, { isInfected: 1 });

    //Get all contacts for the infected user
    const contacts = await contactRepository.find({
        where: [
            {device1: device.id },
            {device2: device.id }, 
        ]
    });

    // Get all contacted users for the infected user 
    let contactedInfo = [];
    contacts.forEach( c => {

        if (!contactedInfo.includes(c.device1.id)) {
            if (c.device1.name !== device.name && !c.device1.isInfected) {
                const date = c.timestamp.toUTCString().slice(0, 17).replace();
                const time = c.timestamp.getHours() + ':' + c.timestamp.getMinutes();

                contactedInfo.push({
                    id: c.device1.id,
                    lastKnownTransport1: c.lastKnownTransport1,
                    lastKnownTransport2: c.lastKnownTransport2,
                    timestamp: date + time,
                    longitude: c.longitude,
                    latitude: c.latitude
                });
            }
        }

        if (!contactedInfo.includes(c.device2.id)) {
            if (c.device2.name !== device.name && !c.device2.isInfected) {
                const date = c.timestamp.toUTCString().slice(0, 17).replace();
                const time = c.timestamp.getHours() + ':' + c.timestamp.getMinutes();

                contactedInfo.push({
                    id: c.device2.id,
                    lastKnownTransport1: c.lastKnownTransport1,
                    lastKnownTransport2: c.lastKnownTransport2,
                    timestamp: date + time,
                    longitude: c.longitude,
                    latitude: c.latitude
                });
            }
        }

    });

    // Add contacted users to pending notifications
    let pendingNotifications = [];
    contactedInfo.forEach(obj => pendingNotifications.push({ 
        infected: device.id,
        contacted: obj.id,
        lastKnownTransport1: obj.lastKnownTransport1,
        lastKnownTransport2: obj.lastKnownTransport2,
        timestamp: obj.timestamp
    }));

    // Insert the notification
    const currentPendingNotifications = await pendingNotificationRepository.find({ infected: device.id });
    if (currentPendingNotifications.length === 0) {
        await pendingNotificationRepository.manager.insert(PendingNotification, pendingNotifications);
    }

    res.status(204).json();
};

exports.reportInfectedness = reportInfectedness;
