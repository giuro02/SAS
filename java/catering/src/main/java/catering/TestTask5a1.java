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
import java.util.Date;
import java.util.Map;


public class TestTask5a1{
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User u = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            System.out.println("\nTEST CREATE SUMMARY SHEET");
            EventInfo event = CatERing.getInstance().getEventManager().getEventInfo().get(0);
            ServiceInfo service = event.getServices().get(0);
            Menu m = service.getMenu();
            m.setOwner(u);
            System.out.println("MENU: " + m.testString());

            SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createNewSheet(service);

            System.out.println("\nTEST ADD TASK");
            KitchenJob kj1 = CatERing.getInstance().getRecipeManager().getRecipes().get(0);
            Task t1 = CatERing.getInstance().getKitchenTaskManager().createTask(kj1);
            System.out.println(t1.toString());

            KitchenJob kj2 = CatERing.getInstance().getRecipeManager().getRecipes().get(1);
            Task t2 = CatERing.getInstance().getKitchenTaskManager().createTask(kj2);
            System.out.println(t2.toString());

            //assign
            System.out.println("\nTEST ASSIGN TASK");
            KitchenShift ks = new KitchenShift( LocalDateTime.of(2025, 1, 12, 17, 0, 0), LocalDateTime.of(2025, 1, 12, 20,0,0) ,LocalDateTime.of(2025, 5, 5, 0 , 0 , 0), "Torino", null, service );
            CatERing.getInstance().getKitchenTaskManager().assignTask(t1, ks);
            CatERing.getInstance().getKitchenTaskManager().assignTask(t2, ks);
            System.out.println("Kitchen Shift:\n" + ks.getTaskList().toString());
            System.out.println("\nTEST REMOVE ASSIGN TASK");
            //remove_assign
            CatERing.getInstance().getKitchenTaskManager().deleteAssignment(t2,ks);
            System.out.println("Kitchen Shift:\n" + ks.getTaskList().toString());

        } catch (UseCaseLogicException | SheetException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}

