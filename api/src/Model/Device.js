class Device {
	
	constructor(name) {
		this._name = name;
	}

	get name() {
		return this._name;
	}

	get isSick() {
		return this._isInfected;
	}

	sick() {
		this._isInfected = 1;
	}

	recover() {
		this._isInfected = 0;
	}

}

module.exports = {
	Device: Device
}