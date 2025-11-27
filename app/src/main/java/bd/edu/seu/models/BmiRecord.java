package bd.edu.seu.models;

public class BmiRecord {
    private String recordId;
    private double bmi;
    private double bmr;
    private String suggestion;
    private String tips;
    private long timestamp;

    public BmiRecord() {}

    public BmiRecord(String recordId, double bmi, double bmr, String suggestion, String tips, long timestamp) {
        this.recordId = recordId;
        this.bmi = bmi;
        this.bmr = bmr;
        this.suggestion = suggestion;
        this.tips = tips;
        this.timestamp = timestamp;
    }

    public double getBmi() { return bmi; }
    public double getBmr() { return bmr; }
    public String getSuggestion() { return suggestion; }
    public String getTips() { return tips; }
    public long getTimestamp() { return timestamp; }
}