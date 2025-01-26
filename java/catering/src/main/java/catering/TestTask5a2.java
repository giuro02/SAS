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
import javafx.collections.ObservableList;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.Duration;
import java.sql.Timestamp;
import java.util.Map;


public class TestTask5a2{
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            System.out.println("\nTEST CREATE SUMMARY SHEET");
            EventInfo event = CatERing.getInstance().getEventManager().getEventInfo().get(0);
            ServiceInfo service = event.getServices().get(0);
            System.out.println("MENU" + service.getMenu().testString());

            SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createNewSheet(service);

            System.out.println("\nTEST ADD TASK");
            KitchenJob kj1 = CatERing.getInstance().getRecipeManager().getRecipes().get(0);
            Task t1 = CatERing.getInstance().getKitchenTaskManager().createTask(kj1);
            System.out.println(t1.toString());

            System.out.println("\nTEST MODIFY TASK");
            CatERing.getInstance().getKitchenTaskManager().modifyInfoTaskAssignment(t1,null,50,"20", "2");
            System.out.println("modified task: "+ t1);

        } catch (UseCaseLogicException | SheetException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}

