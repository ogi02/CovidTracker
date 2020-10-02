class Contact {
	
	constructor(device1, device2) {
		this.device1Id = device1;
		this.device2Id = device2;
		this.timestamp = new Date();
	}

}

module.exports = {
	Contact: Contact
}