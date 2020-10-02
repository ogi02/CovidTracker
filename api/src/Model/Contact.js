class Contact {
	
	constructor(device_name1, device_name2) {
		this.device_name1 = device_name1;
		this.device_name2 = device_name2;
		this.timestamp = new Date();
	}

}

module.exports = {
	Contact: Contact
}