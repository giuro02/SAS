package catering.businesslogic.shift;

import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.summarySheet.Task;

//import java.util.Date;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;

public class Shift {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime deadline;
    private ServiceInfo service;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ServiceInfo getService() {
        return service;
    }

    public void setService(ServiceInfo service) {
        this.service = service;
    }

    public Shift(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime deadline, ServiceInfo service) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
        this.service = service;
    }

    public Shift(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime deadline, int id) {
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

    public LocalDateTime getstartTime() {
        return startTime;
    }

    public LocalDateTime getendTime() {
        return endTime;
    }

    public void setstartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setendTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public long getShiftLength(){
        return Duration.between(startTime,endTime).toMinutes();                                 //-this.startTime.getTime())/60000;
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
