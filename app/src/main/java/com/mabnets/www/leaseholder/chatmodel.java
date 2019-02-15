package com.mabnets.www.leaseholder;

public class chatmodel {
    public String messagee;
    public boolean issend;

    public chatmodel(String messagee, boolean issend) {
        this.messagee = messagee;
        this.issend = issend;
    }

    public chatmodel() {
    }

    public String getMessagee() {
        return messagee;
    }

    public void setMessagee(String messagee) {
        this.messagee = messagee;
    }

    public boolean isIssend() {
        return issend;
    }

    public void setIssend(boolean issend) {
        this.issend = issend;
    }
}
