const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');

const deviceRepository = getRepository(Device);

const report = async (req, res) => { 
    const device = deviceRepository.update({ name: req.body.name }, { isInfected: 1 });

    res.status(204).json();
};

exports.report = report;
