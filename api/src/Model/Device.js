class Device {
	
	constructor(name) {
		this._name = name;
		this._isInfected = false;
	}

	get name() {
		return this._name;
	}

	get isSick() {
		return this._isInfected;
	}

	sick() {
		this._isInfected = true;
	}

	recover() {
		this._isInfected = false;
	}

}

module.exports = {
	Device: Device
}