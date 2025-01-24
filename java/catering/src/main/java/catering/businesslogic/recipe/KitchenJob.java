package catering.businesslogic.recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

public interface KitchenJob {
    int getId();
    String getName();
    void setId(int id);
    void setName(String name);

    public static KitchenJob loadKitchenJobById(int id) {
        String query = "SELECT * FROM KitchenJobs WHERE id = " + id;
        KitchenJob[] ret = new KitchenJob[1];
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                if (!Objects.equals(rs.getString("type"), "prep"))
                    ret[0] = (KitchenJob) Recipe.getRecipe(rs.getInt("id"), rs.getString("name"));
                /*else
                    ret[0] = Preparation.getPreparation(rs.getInt("id"), rs.getString("name"));*/
            }
        });
        return ret[0];
    }
}
