/*
 * Copyright 2015
 *  http://wazza.co.ke
 * 5:47:42 PM  : May 29, 2015
 */

package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.LoginControl;

/**
 *
 * @author kelli
 * Get user login details and authenticate...
 */
//@WebServlet(name = "Authenticate", urlPatterns = {"/Authenticate"})
public class Authenticate extends HttpServlet {
    
    private Connection connection;
    private boolean valid;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userName = request.getParameter("tx_user");
        String password = request.getParameter("tx_password");
        Logger.getLogger(Authenticate.class.getName()).log(Level.INFO,"user:{0}",userName);
        Logger.getLogger(Authenticate.class.getName()).log(Level.INFO,"password:{0}",password);

        //Read connection object from context
        
        ServletContext context = request.getServletContext();
        connection =(Connection) context.getAttribute("connector");
        
        /**User authentication
         * 1. Get the stored record for this user
         * 2. Extract the password 
         * 3. Generate a hash from the entered password using retrieved salt value
         * 4. Compare the generated hash to the retrieved hash
         */
        String sql = "select * from users where email_address = '"+userName+"'";
        Logger.getLogger(Authenticate.class.getName()).log(Level.INFO,"sql statement",sql);
        
        try{
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql); //expect 1 result
        rs.next();
        String retrievedEmailAddr = rs.getString("email_address");
        String retrievedPassHash = rs.getString("password");
        String retrievedSalt = rs.getString("salt");
        valid = LoginControl.validatePasswordHash(password, retrievedPassHash, retrievedSalt);
        
        }
        catch(SQLException sqe){
                    Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE,"SQL Exception: ",sqe);
        }
        if(valid){
            //redirect to catalog page
             //TODO should call the ItemsCatalog servlet
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/catalog.jsp");
        dispatcher.forward(request, response);
        }else{
            //store error attribute and show login page+error
            request.setAttribute("error", "user name or passord not correct");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
        }
    }
    
//    private boolean authenticateUser()

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
