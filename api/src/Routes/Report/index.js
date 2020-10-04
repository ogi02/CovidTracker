const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');
const PendingNotification = require('../../Entity/PendingNotificationSchema');

const deviceRepository = getRepository(Device);
const contactRepository = getRepository(Contact);
const pendingNotificationRepository = getRepository(PendingNotification);

const reportInfectedness = async (req, res) => {
    //Get user by name and check if it exists
    const device = await deviceRepository.findOne({ name: req.body.deviceName });
    if (!device) {
        res.status(422).json();
        return;
    }

    //Set user to infected
    await deviceRepository.update({ name: req.body.deviceName }, { isInfected: 1 });

    //Get all contacts for the infected user
    const contacts = await contactRepository.find({
        where: [
            {device1: device.id },
            {device2: device.id }, 
        ]
    });
    
    //Get all contacted users for the infected user 
    let contactedIds = [];
    contacts.forEach( c => {
        if (!contactedIds.includes(c.device1.id)) {
            if (c.device1.name !== device.name) {
                contactedIds.push(c.device1.id);
            }        
        }
        if (!contactedIds.includes(c.device2.id)) {
            if (c.device2.name !== device.name) {
                contactedIds.push(c.device2.id);
            }
        }
    });

    //Add contacted users to pending notifications
    let pendingNotifications = [];
    contactedIds.forEach( id => pendingNotifications.push({ infected: device.id, contacted: id }));

    //Insert the notification
    const currentPendingNotifications = await pendingNotificationRepository.find({ infected: device.id });
    if (currentPendingNotifications.length === 0) {
        await pendingNotificationRepository.manager.insert(PendingNotification, pendingNotifications);
    }

    res.status(204).json();
};

exports.reportInfectedness = reportInfectedness;
