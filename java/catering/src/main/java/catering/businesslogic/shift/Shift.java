package catering.businesslogic.shift;

import catering.businesslogic.summarySheet.Task;

import java.util.ArrayList;
import java.util.Date;

public class Shift {
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

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

    //per recuperare taskList da shift direttamente
    // Metodo per accedere a shiftList tramite cast
    public ArrayList<Task> getTasksList() {
        if (this instanceof KitchenShift) {
            return ((KitchenShift) this).getTaskList();
        }
        throw new UnsupportedOperationException("ShiftList non disponibile per questa classe.");
    }


    @Override
    public String toString(){
        return id + " - Inizio turno: " + this.startTime + " Fine turno: "+this.endTime+" - Deadline disponibilità: "+this.deadline;
    }

    public void addTask(Task task) {
        if (task != null) {
            getTasksList().add(task);
            System.out.println("Task aggiunto con successo: " );
        } else {
            System.out.println("Errore: il task non può essere null.");
        }
    }

}
