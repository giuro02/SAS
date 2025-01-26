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


public class TestTask2a1{
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
            SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createNewSheet(service);
            System.out.println(s.testString());

            System.out.println("\nTEST REMOVE TASK");
            ArrayList<Task> tasks = s.getTaskList();
            CatERing.getInstance().getKitchenTaskManager().deleteTask(tasks.get(0));

            CatERing.getInstance().getKitchenTaskManager().deleteTask(tasks.get(3));
            System.out.println(s.testString());

            System.out.println("\nTEST ADD AND REMOVE TASK");
            KitchenJob kj1 = CatERing.getInstance().getRecipeManager().getRecipes().get(0);
            Task t1 = CatERing.getInstance().getKitchenTaskManager().createTask(kj1);
            System.out.println("ADD");
            System.out.println(t1.toString());
            System.out.println(s.testString());

            CatERing.getInstance().getKitchenTaskManager().deleteTask(t1);
            System.out.println("REMOVE");
            System.out.println(s.testString());


        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}

