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


public class TestTask{
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            System.out.println("\nTEST CREATE SUMMARY SHEET");
            EventInfo event = CatERing.getInstance().getEventManager().getEventInfo().get(0);
            ServiceInfo service = event.getServices().get(0);
            System.out.println("MENU" + service.getMenu().testString());

            SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createNewSheet(service);
            System.out.println(s.testString());

            System.out.println("\nTEST ADD TASK");
            KitchenJob kj1 = CatERing.getInstance().getRecipeManager().getRecipes().get(0);
            Task t1 = CatERing.getInstance().getKitchenTaskManager().createTask(kj1);
            System.out.println(t1.toString());

            KitchenJob kj2 = CatERing.getInstance().getRecipeManager().getRecipes().get(1);
            Task t2 = CatERing.getInstance().getKitchenTaskManager().createTask(kj2);
            System.out.println(t2.toString());
            System.out.println(s.testString());

            System.out.println("\nTEST ORDER TASK");
            CatERing.getInstance().getKitchenTaskManager().orderTask(t1, 1);
            System.out.println(s.testString());

            System.out.println("\nTEST ASSIGN TASK");
            KitchenShift ks = new KitchenShift( LocalDateTime.of(2025, 1, 12, 17, 0, 0), LocalDateTime.of(2025, 1, 12, 20,0,0) ,null, "Torino", null, service );
            CatERing.getInstance().getKitchenTaskManager().assignTask(t1, ks);
            System.out.println(ks);

            System.out.println("\nTEST GET SHIFTS");
            ArrayList<KitchenShift> shifts = service.getKitchenShifts();
            for(KitchenShift shift: shifts)
                System.out.println(shift);

        } catch (UseCaseLogicException | SheetException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
