const EntitySchema = require('typeorm').EntitySchema;
const PendingNotification = require('../Model/PendingNotification').PendingNotification;

module.exports = new EntitySchema({
    name: 'PendingNotification',
    target: PendingNotification,
    columns: {
        id: {
            primary: true,
            type: 'int',
            generated: true
        }
    },
    relations: {
        infected: {
            target: 'Device',
            type: 'many-to-one',
            eager: true
        },
        contacted: {
            target: 'Device',
            type: 'many-to-one',
            eager: true
        }
    }
})