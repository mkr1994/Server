import config.Config;
import view.AdminView;

import java.sql.SQLException;

/**
 * Mainclass for admintui, need to be startet seperately from server.
 * Created by magnusrasmussen on 24/11/2016.
 */
public class Main {
    public static void main(String[] args){

        setDBDetails();

        AdminView adminInterface = new AdminView();

        try {
            adminInterface.menu();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * DB details needed for successful connection
     */
    public static void setDBDetails(){
        Config.setDbUrl("localhost");
        Config.setDbPort("3306");
        Config.setDbName("Bookit");
        Config.setDbPassword("1234");
        Config.setDbUserName("root");
    }
}
