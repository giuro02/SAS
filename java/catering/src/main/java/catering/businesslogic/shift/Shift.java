package catering.businesslogic.shift;

import catering.businesslogic.summarySheet.Task;

import java.util.ArrayList;
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

    public Shift(Date startTime, Date endTime, Date deadline, int id) { //NON CAPIRO' MAI PERCHE' CI SONO DUE COSTRUTTORI
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getstartTime() {
        return startTime;
    }

    public Date getendTime() {
        return endTime;
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

    public Date getDeadline() {
        return deadline;
    }

    public long getShiftLength(){
        return (this.endTime.getTime()-this.startTime.getTime())/60000;
    }


    //per recuperare taskList da shift direttamente
    // Metodo per accedere a shiftList tramite cast
    /*public ArrayList<Task> getTasksList() { //HA SENSO METTERLA QUA? A STO POINT METTIAMOLA SOLO IN KITCHENSHIFT
        if (this instanceof KitchenShift) {
            return ((KitchenShift) this).getTaskList();
        }
        throw new UnsupportedOperationException("ShiftList non disponibile per questa classe.");
    }*/


    @Override
    public String toString(){
        return id + " - Inizio turno: " + this.startTime + " Fine turno: "+this.endTime+" - Deadline disponibilità: "+this.deadline;
    }

    /*public void addTask(Task task) { //A STO POINT SOLO IN KITCHENSHIFT
        if (task != null) {
            getTasksList().add(task);
            System.out.println("Task aggiunto con successo: " );
        } else {
            System.out.println("Errore: il task non può essere null.");
        }
    }*/

}
