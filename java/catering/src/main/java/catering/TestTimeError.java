package catering;


import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.EventInfo;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.menu.Menu;
import catering.businesslogic.menu.Section;
import catering.businesslogic.recipe.KitchenJob;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.shift.KitchenShift;
import catering.businesslogic.summarySheet.KitchenTaskManager;

import catering.businesslogic.summarySheet.SheetException;
import catering.businesslogic.summarySheet.SummarySheet;
import catering.businesslogic.summarySheet.Task;
import catering.businesslogic.user.User;
import javafx.collections.ObservableList;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.Duration;
import java.sql.Timestamp;
import java.util.Map;


public class TestTimeError{
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User u = CatERing.getInstance().getUserManager().getCurrentUser();

            System.out.println("\nTEST CREATE SUMMARY SHEET");
            EventInfo event = CatERing.getInstance().getEventManager().getEventInfo().get(0);
            ServiceInfo service = event.getServices().get(0);
            Menu m = service.getMenu();
            m.setOwner(u);
            System.out.println("MENU: " + m.testString());

            SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createNewSheet(service);

            System.out.println("\nTEST ADD TASK");
            KitchenJob kj1 = CatERing.getInstance().getRecipeManager().getRecipes().get(0);
            KitchenJob kj2 = CatERing.getInstance().getRecipeManager().getRecipes().get(1);
            Task t1 = CatERing.getInstance().getKitchenTaskManager().createTask(kj1);
            Task t2 = CatERing.getInstance().getKitchenTaskManager().createTask(kj2);

            System.out.println(t1.toString());
            //get cook
            CatERing.getInstance().getUserManager().fakeLogin("Marinella");
            int cook_id = CatERing.getInstance().getUserManager().getCurrentUser().getId();
            System.out.println("\nTEST MODIFY TASK");
            CatERing.getInstance().getKitchenTaskManager().modifyInfoTaskAssignment(t1,cook_id,50,"20", "2");
            CatERing.getInstance().getKitchenTaskManager().modifyInfoTaskAssignment(t2,cook_id,200000,"20", "2");
            KitchenShift ks = new KitchenShift( LocalDateTime.of(2025, 1, 10, 17, 0, 0), LocalDateTime.of(2025, 1, 10, 21,0,0) ,LocalDateTime.of(2025,5,5,0,0,0), "Torino", null, service );

            //assign
            CatERing.getInstance().getKitchenTaskManager().assignTask(t1, ks);
            CatERing.getInstance().getKitchenTaskManager().assignTask(t2, ks);


            System.out.println("Kitchen Shift:\n" + ks.getTaskList().toString());
            System.out.println("modified task: "+ t1);

        } catch (UseCaseLogicException | SheetException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}


