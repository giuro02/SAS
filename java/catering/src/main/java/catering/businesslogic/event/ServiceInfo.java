package catering.businesslogic.event;

import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;
import catering.businesslogic.menu.Menu;
import catering.businesslogic.shift.KitchenShift;
import catering.businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

// import org.postgresql.util.ReaderInputStream;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class ServiceInfo implements EventItemInfo {

    private int id;
    private String name;
    private Date date;
    private Time timeStart;
    private Time timeEnd;
    private int participants;
    private Menu menu;
    private User handler;
    private ArrayList<KitchenShift> kitchenShiftList;

    public ServiceInfo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public ArrayList<KitchenShift> getKitchenShifts(){
        return kitchenShiftList;
    }

    public User getHandler(){
        return this.handler;
    }

    public void getHandler(User handler){
        this.handler=handler;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void addShift(KitchenShift s){
        this.kitchenShiftList.add(s);
    }

    public String toString() {
        return name + ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp.";
    }

    //public static ArrayList<ServiceInfo> loadServiceInfoForEvent(int event_id) {
    public static ObservableList<ServiceInfo> loadServiceInfoForEvent(int event_id) {
        //ArrayList<ServiceInfo> result = new ArrayList<>();
        ObservableList<ServiceInfo> result = FXCollections.observableArrayList();;
        String query = "SELECT id, name, service_date, time_start, time_end, expected_participants, menu_id, handler_id " +
                "FROM Services WHERE event_id = " + event_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String s = rs.getString("name");
                ServiceInfo serv = new ServiceInfo(s);
                serv.id = rs.getInt("id");
                serv.date = rs.getDate("service_date");
                serv.timeStart = rs.getTime("time_start");
                serv.timeEnd = rs.getTime("time_end");
                serv.participants = rs.getInt("expected_participants");
                int menu_id = rs.getInt("menu_id");
                serv.menu = Menu.loadMenuById(menu_id);
                int handler_id = rs.getInt("handler_id");
                serv.handler = User.loadUserById(handler_id);
                serv.kitchenShiftList = KitchenShift.loadKitchenShiftFor(serv.id);
                result.add(serv);
            }
        });
        return result;
    }

    public static ServiceInfo loadServiceById(int serviceId) {
        String query = "SELECT * FROM Services WHERE id = " + serviceId;
        ServiceInfo ris[] = new ServiceInfo[1];
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String s = rs.getString("name");
                ServiceInfo serv = new ServiceInfo(s);
                serv.id = rs.getInt("id");
                serv.date = rs.getDate("service_date");
                serv.timeStart = rs.getTime("time_start");
                serv.timeEnd = rs.getTime("time_end");
                serv.participants = rs.getInt("expected_participants");
                int menu_id = rs.getInt("menu_id");
                serv.menu = Menu.loadMenuById(menu_id);
                int handler_id = rs.getInt("handler_id");
                serv.handler = User.loadUserById(handler_id);
                serv.kitchenShiftList = KitchenShift.loadKitchenShiftFor(serv.id);

                ris[0]=serv;
            }
        });
        return ris[0];
    }
}
