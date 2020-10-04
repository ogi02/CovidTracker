class PendingNotification {
	
	constructor(infected, contacted, timestamp, lastKnownTransport1, lastKnownTransport2) {
		this.infectedId = infected;
		this.contactedId = contacted;
		this.timestamp = timestamp;
		this.lastKnownTransport1 = lastKnownTransport1;
		this.lastKnownTransport2 = lastKnownTransport2;
	}

}

module.exports = {
	PendingNotification: PendingNotification
}