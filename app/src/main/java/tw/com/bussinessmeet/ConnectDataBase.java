package tw.com.bussinessmeet;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDataBase {
    public void connect(){
        String driverName = "net.sourceforge.jtds.jdbc.Driver";
        String dbURL = "jdbc:jtds:sqlserver://127.0.0.1:1433/bussiness_meet";
        String userName = "10646012";
        String userPwd = "12345";
        Connection dbConn;
        try{
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
