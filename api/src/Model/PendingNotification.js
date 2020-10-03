class PendingNotification {
	
	constructor(infected, contacted) {
		this.infectedId = infected;
		this.contactedId = contacted;
	}

}

module.exports = {
	PendingNotification: PendingNotification
}