package cc.holdinga.covidtracker.models;

public class SensorData {
    private final double max;
    private final double min;
    private final double mean;
    private final double std;

    public SensorData(double max, double min, double mean, double std) {
        this.max = max;
        this.min = min;
        this.mean = mean;
        this.std = std;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getMean() {
        return mean;
    }

    public double getStd() {
        return std;
    }
}
