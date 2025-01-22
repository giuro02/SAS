package catering.businesslogic.shift;

import java.util.Date;

public class Shift {
    private int id;
    private Date startTime;
    private Date endTime;
    private Date deadline;

    public Shift(Date startTime, Date endTime, Date deadline) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
    }

    public Shift(Date startTime, Date endTime, Date deadline, int id) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
    }

    public Date getstartTime() {
        return startTime;
    }

    public Date getendTime() {
        return endTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setstartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setendTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public long getShiftLength(){
        return (this.endTime.getTime()-this.startTime.getTime())/60000;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString(){
        return id + " - Inizio turno: " + this.startTime + " Fine turno: "+this.endTime+" - Deadline disponibilità: "+this.deadline;
    }
}
