class Contact {
	
	constructor(deviceName1, deviceName2) {
		this.deviceName1 = deviceName1;
		this.deviceName2 = deviceName2;
		this.timestamp = new Date();
	}

}

module.exports = {
	Contact: Contact
}