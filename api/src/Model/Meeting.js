export class Meeting {

	constructor(device_id1, device_id2) {
		this._device_id1 = device_id1;
		this._device_id2 = device_id2;
		this._timestamp = new Date()
	}

	get device_id1() {
		return this._device_id1;
	}

	get device_id2() {
		return this._device_id2;
	}

	get timestamp() {
		return this._timestamp;
	}

}