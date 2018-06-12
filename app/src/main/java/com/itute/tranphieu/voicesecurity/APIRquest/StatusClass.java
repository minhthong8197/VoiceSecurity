package com.itute.tranphieu.voicesecurity.APIRquest;

public class StatusClass {
    private Boolean statusSucceeded = false;
    private Boolean isEnrolling = false;
    private String identifiedProfileId = "00000000-0000-0000-0000-000000000000";
    private Double remainingEnrollmentSpeechTime = 30.0;
    private String failedMessage = "Non message";

    public Boolean getStatusSucceeded() {
        return statusSucceeded;
    }

    public void setStatusSucceeded(Boolean statusSucceeded) {
        this.statusSucceeded = statusSucceeded;
    }

    public String getIdentifiedProfileId() {
        return identifiedProfileId;
    }

    public void setIdentifiedProfileId(String identifiedProfileId) {
        this.identifiedProfileId = identifiedProfileId;
    }

    public Double getRemainingEnrollmentSpeechTime() {
        return remainingEnrollmentSpeechTime;
    }

    public void setRemainingEnrollmentSpeechTime(Double remainingEnrollmentSpeechTime) {
        this.remainingEnrollmentSpeechTime = remainingEnrollmentSpeechTime;
    }

    public String getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
    }

    public Boolean getEnrolling() {
        return isEnrolling;
    }

    public void setEnrolling(Boolean enrolling) {
        isEnrolling = enrolling;
    }

    @Override
    public String toString() {
        return "StatusClass{" +
                "statusSucceeded=" + statusSucceeded +
                ", isEnrolling=" + isEnrolling +
                ", identifiedProfileId='" + identifiedProfileId + '\'' +
                ", remainingEnrollmentSpeechTime=" + remainingEnrollmentSpeechTime +
                ", failedMessage='" + failedMessage + '\'' +
                '}';
    }
}
