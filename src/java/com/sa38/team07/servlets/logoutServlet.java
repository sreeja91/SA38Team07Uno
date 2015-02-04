/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sa38.team07.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gauri_000
 */
@WebServlet(name = "logoutServlet", urlPatterns = {"/logout"})
public class logoutServlet extends HttpServlet {

     /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {
            req.logout();
            HttpSession sess = req.getSession();
            if (!(sess == null)) {
                sess.invalidate();
            }
        } catch (IllegalStateException ex) {
            System.out.println("logout failed");
        } catch (ServletException ex) {
            System.out.println("unable to logout");
        }

        //res.sendRedirect(req.getContextPath() + "/Login.html");
    }

}
