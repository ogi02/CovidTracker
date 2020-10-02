class Contact {
	
	constructor(device_name1, device_name2) {
		this._device_name1 = device_name1;
		this._device_name2 = device_name2;
		this._timestamp = new Date();
	}

	get device_name1() {
		return this._device_name1;
	}

	get device_name2() {
		return this._device_name2;
	}

	get timestamp() {
		return this._timestamp;
	}

}

module.exports = {
	Contact: Contact
}