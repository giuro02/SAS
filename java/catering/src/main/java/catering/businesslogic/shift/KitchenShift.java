package catering.businesslogic.shift;

import catering.businesslogic.summarySheet.Task;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KitchenShift extends Shift {

    private String place;
    private ArrayList<Task> taskList;

    public KitchenShift(Date startTime, Date endTime, Date deadline, ArrayList<Task> taskList, String place) {
        super(startTime, endTime, deadline);
        this.taskList = taskList;
        this.place= place;
        taskList = new ArrayList<>();
    }

    public KitchenShift(Date startTime, Date endTime, Date deadline, String place, int id) {
        super(startTime, endTime, deadline, id);
        this.place= place;
        taskList = new ArrayList<>();
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public String getPlace(){
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
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

        long difference = (this.getendTime().getTime() - this.getstartTime().getTime())/60000;

        return difference >= estimatedTime;
    }

    public int getTaskIndex(String task) {
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
                KitchenShift shift = new KitchenShift(rs.getTimestamp("start_date"), rs.getTimestamp("end_date"), rs.getTimestamp("deadline"), rs.getString("place"), rs.getInt("id"));
                shift.taskList = Task.loadTaskFor(shift.getId());
                result.add(shift);
            }
        });
        return result;
    }
}
