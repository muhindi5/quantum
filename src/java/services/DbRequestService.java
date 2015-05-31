/*
 * Copyright 2015
 *  http://wazza.co.ke
 * 10:31:57 PM  : May 30, 2015
 */
package services;

import domain.Vehicle;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Process all database requests on request from specific servlets. Database
 * connection already setup by servlet: ConnectionInit
 *
 * @author kelli
 */
public class DbRequestService {

    private static ResultSet results = null;
    private static Statement statement;
    private static ArrayList<Vehicle> vehicleList;

    public static ArrayList<Vehicle> processQueryRequest(Connection connection, String sql) {
            try {
                vehicleList = new ArrayList<>();
                statement = connection.createStatement();
                results = statement.executeQuery(sql);
                        while (results.next()) {
                            int vehicleId = results.getInt("vehicle_id");
                            String reg_num = results.getString("registration_num");
                            String make = results.getString("make");
                            String model = results.getString("model");
                            String color = results.getString("color");
                            Date year = results.getDate("_year");
                            int satNav = results.getInt("sat_nav");
                            int advEnt = results.getInt("adv_ent");
                            int chauffered = results.getInt("chauffered");
                            String image = results.getString("image_file_name");
                            String imageFile = "/var/alexi_images/" + image;
                            Vehicle current = new Vehicle(vehicleId, reg_num, make, model, color, year, satNav, advEnt, chauffered, imageFile);
                            vehicleList.add(current);
                        }
                    } 
             catch (SQLException sqle) {
                Logger.getLogger(DbRequestService.class.getCanonicalName()).log(Level.SEVERE, sqle.getLocalizedMessage());
            }
        return vehicleList;
    }
}
