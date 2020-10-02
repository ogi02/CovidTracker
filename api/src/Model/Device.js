class Device {
	
	constructor(name) {
		this.name = name;
	}

	// get name() {
	// 	return this._name;
	// }

	// get isSick() {
	// 	return this._isInfected;
	// }

	sick() {
		this.isInfected = 1;
	}

	recover() {
		this.isInfected = 0;
	}

}

module.exports = {
	Device: Device
}