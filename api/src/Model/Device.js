class Device {
	
	constructor(name) {
		this._name = name;
		this._isSick = false;
	}

	get name() {
		return this._name;
	}

	get isSick() {
		return this._isSick;
	}

	sick() {
		this._isSick = true;
	}

	recover() {
		this._isSick = false;
	}

}

module.exports = {
	Device: Device
}