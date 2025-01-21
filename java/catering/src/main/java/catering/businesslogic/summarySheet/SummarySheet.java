package catering.businesslogic.summarySheet;

import catering.persistence.BatchUpdateHandler;
import catering.persistence.PersistenceManager;

import catering.businesslogic.user.User;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.menu.*;
import catering.businesslogic.recipe.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SummarySheet {

    private static Map<Integer, SummarySheet> loadedSheets = new HashMap<Integer, SummarySheet>();

    private int id;
    private User owner;
    private ServiceInfo service;
    private ArrayList<Task> taskList;

    public SummarySheet() {

    }

    public SummarySheet(ServiceInfo service, User user) {
        this.owner = user;
        this.service = service;
        this.id=0;
        this.taskList = new ArrayList<>();

        Menu menu = service.getMenu();
        for (MenuItem mi : menu.getFreeItems()) {
            Recipe recipe = mi.getItemRecipe();
            Task task = new Task(recipe);
            taskList.add(task);
            for (KitchenJob kj : recipe.getPreparations()) {
                Task task2 = new Task(kj);
                taskList.add(task2);
            }
        }

        for (Section s : menu.getSections()) {
            for (MenuItem mi : s.getItems()) {
                Recipe recipe = mi.getItemRecipe();
                Task task = new Task(recipe);
                taskList.add(task);
                for (KitchenJob kj : recipe.getPreparations()) {
                    Task task2 = new Task(kj);
                    taskList.add(task2);
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public ServiceInfo getService() {
        return service;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public int getTaskListSize() {
        return taskList.size();
    }

    public ArrayList<Task> addTask(KitchenJob kj) {
        Task t = new Task(kj);
        ArrayList<Task> newTasks = new ArrayList<Task>();
        newTasks.add(t);
        if (kj instanceof Recipe) {
            for (KitchenJob job : ((Recipe)kj).getPreparations()) {
                t = new Task(job);
                newTasks.add(t);
            }
        }
        taskList.addAll(newTasks);
        return newTasks;
    }

    public void setTask(Task task, int position) {
        int pos = taskList.indexOf(task);
        Task oldTask = taskList.set(position, task);
        taskList.add(position + 1, oldTask);
        taskList.remove(position < pos ? pos + 1 : pos);
    }

    public boolean containsTask(Task task) {
        return taskList.contains(task);
    }

    public boolean isOwner(User user) {
        return owner.equals(user);
    }

    public int removeTask(Task task) {
        task.removeShift();
        int ret = taskList.indexOf(task);
        taskList.remove(task);
        return ret;
    }

    public SummarySheet regenerateSheet() {
        this.taskList.clear();
        SummarySheet newSheet= new SummarySheet(this.service, this.owner);
        return newSheet;
    }

    public String testString() {
        String result = this.toString();

        result += "\nTasks:\n";

        for (Task t : taskList) {
            result += t;
        }
        result += "\n";
        return result;
    }

    @Override
    public String toString() {
        return "autore: " + owner.getUserName() + ", servizio associato: " + service + "\n";
    }

    // STATIC METHODS FOR PERSISTENCE

    public static void saveNewSheet(SummarySheet s) {
        String sheetInsert = "INSERT INTO catering.Sheets (owner_id, service_id) VALUES (?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(sheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, s.owner.getId());
                ps.setInt(2, s.service.getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    s.id = rs.getInt(1);
                }
            }
        });

        if (result[0] > 0) { // foglio effettivamente inserito
            // salva i tasks
            if (s.taskList.size() > 0) {
                Task.saveAllNewTasks(s.id, s.taskList, 0);
            }
            loadedSheets.put(s.id, s);
        }
    }

    public static void deleteSheet(int id) {
        // delete tasks
        String delTasks = "DELETE FROM Tasks WHERE sheet_id = " + id;
        PersistenceManager.executeUpdate(delTasks);

        String del = "DELETE FROM Sheets WHERE id = " + id;
        PersistenceManager.executeUpdate(del);
        loadedSheets.remove(id);
    }

    public static void saveTasksOrder(SummarySheet sheet) {
        String upd = "UPDATE Tasks SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, sheet.taskList.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, sheet.taskList.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }
}

