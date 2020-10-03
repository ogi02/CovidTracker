const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');
const Contact = require('../../Entity/ContactSchema');
const PendingNotification = require('../../Entity/PendingNotificationSchema');

const deviceRepository = getRepository(Device);
const contactRepository = getRepository(Contact);
const pendingNotificationRepository = getRepository(PendingNotification);

const report = async (req, res) => {
    const device = await deviceRepository.findOne({ name: req.body.deviceName });
    if (!device) {
        res.status(422).json();
        return;
    }

    await deviceRepository.update({ name: req.body.deviceName }, { isInfected: 1 });

    const contacts = await contactRepository.find({
        where: [
            {device1: device.id },
            {device2: device.id }, 
        ]
    });

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

    let pendingNotifications = [];
    contactedIds.forEach( id => pendingNotifications.push({ infected: device.id, contacted: id }));

    const currentPendingNotifications = await pendingNotificationRepository.find({ infected: device.id });
    if (currentPendingNotifications.length === 0) {
        await pendingNotificationRepository.manager.insert(PendingNotification, pendingNotifications);
    }

    res.status(204).json();
};

exports.report = report;
