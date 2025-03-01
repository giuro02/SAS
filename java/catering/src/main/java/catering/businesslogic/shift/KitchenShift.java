package catering.businesslogic.shift;

import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.summarySheet.Task;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.security.Provider;
import java.time.LocalDateTime;
import java.time.Duration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class KitchenShift extends Shift {

    private String place;

    private ArrayList<Task> taskList;

    public KitchenShift(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime deadline, String place, ArrayList<Task> taskList, ServiceInfo service ) {
        super(startTime, endTime, deadline,service);
        this.place= place;
        this.taskList = taskList == null ? new ArrayList<>() : taskList;
        this.setService(service) ;
        service.addShift(this);
    }

    public KitchenShift(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime deadline, String place, int id) {
        super(startTime, endTime, deadline, id);
        this.place= place;
        taskList = new ArrayList<>();
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public String getPlace(){
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public boolean addTask(Task task){
        return taskList.add(task);
    }

    public boolean removeTask(Task task){
        return taskList.remove(task);
    }

    public boolean enoughTime(long estimatedTime){

        for (Task t :taskList) {
            if(t.getEstimatedTime()!=-1)
                estimatedTime+=t.getEstimatedTime();
        }

        long difference = Duration.between (this.getStartTime(),this.getEndTime()).toMinutes();

        return difference >= estimatedTime;
    }

    public int getTaskIndex(Task task) {
        return taskList.indexOf(task); // Restituisce -1 se il task non è trovato
    }

    @Override
    public String toString(){
        return super.toString()+ (place != null ? " - luogo del turno: "+this.place : "");
    }

    //

    public static ArrayList<KitchenShift> loadKitchenShiftFor(int service_id) {
        ArrayList<KitchenShift> result = new ArrayList<>();
        String query = "SELECT * FROM Shifts WHERE service_id = " + service_id + " AND type = 'kitchen';";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                KitchenShift shift = new KitchenShift(rs.getObject("start_time", LocalDateTime.class), rs.getObject("end_time", LocalDateTime.class), rs.getObject("deadline", LocalDateTime.class), rs.getString("place"), null, null);
                shift.taskList = Task.loadTaskFor(shift.getId());
                shift.setService(ServiceInfo.loadServiceById(rs.getInt("service_id")));
                result.add(shift);
            }
        });
        return result;
    }

    //@Override
    /*public void addTask(Task task) {
        if (task != null) {
            super.addTask(task); // Usa il metodo della classe base
            System.out.println("Task aggiunto a KitchenShift: " + task); //come gli passiamo il nome
        } else {
            System.out.println("Errore: il task non può essere null.");
        }
    }*/

    // Metodo per ottenere i task (opzionale)
    public ArrayList<Task> getKitchenShift() {
        return taskList;
    }
}
