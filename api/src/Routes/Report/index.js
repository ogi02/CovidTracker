const express = require('express');
const { getRepository } = require('typeorm');
const Device = require('../../Entity/DeviceSchema');

const deviceRepository = getRepository(Device);

const report = name => { 
    const device = deviceRepository.update({ name: name }, { isInfected: 1 });
};

exports.report = report;
