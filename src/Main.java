import config.Config;
import view.AdminInterface;

import java.sql.SQLException;

/**
 * Created by magnusrasmussen on 24/11/2016.
 */
public class Main {
    public static void main(String[] args){


        Config.setDbUrl("localhost");
        Config.setDbPort("3306");
        Config.setDbName("Bookit");
        Config.setDbPassword("1234");
        Config.setDbUserName("root");

        AdminInterface adminInterface = new AdminInterface();
        try {
            adminInterface.menu();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
