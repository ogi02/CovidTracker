package cc.holdinga.covidtracker.models;

public class CheckForContactResponse {
    private boolean isContacted;

    public CheckForContactResponse(boolean isContacted) {
        this.isContacted = isContacted;
    }

    public boolean getIsContacted() {
        return isContacted;
    }
}
