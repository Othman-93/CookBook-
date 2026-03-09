package cookbook.controller;

import cookbook.model.AdminDataHandler;
import cookbook.model.User;

import java.util.ArrayList;

/**
 * this is the admin controllar class.
 */

public class AdminController {

  private final AdminDataHandler adminDataHandler;

  public AdminController() {
    this.adminDataHandler = new AdminDataHandler();
  }

  // User Management Methods
  public void addUser(User user) {
    adminDataHandler.addUser(user);
  }

  public void deleteUser(User user) {
    String username = user.getUsername(); 
    adminDataHandler.deleteUser(username); 
    
  }

  // Update User Methods
  public void modifyUsername(User user, String newUsername) {
    adminDataHandler.modifyUsername(user, newUsername);
  }

  public void modifyFirstName(User user, String newFirstName) {
    adminDataHandler.updateAdminFirstName(user.getUsername(), newFirstName);
  }

  public void modifyLastName(User user, String newLastName) {
    adminDataHandler.updateAdminLastName(user.getUsername(), newLastName);
  }

  public void modifyPassword(String username, String newPassword) {
    adminDataHandler.updateAdminPassword(username, newPassword);
  }


  public void modifyAdminStatus(User user, boolean isAdmin) {
    adminDataHandler.modifyAdminStatus(user, isAdmin);
  }

  // User Retrieval Methods
  public ArrayList<String> getAllUsernames() {
    return adminDataHandler.getAllUsersname();
  }

  public String getAdminFirstName(String username) {
    return adminDataHandler.getAdminFirstName(username);
  }

  public String getAdminLastName(String username) {
    return adminDataHandler.getAdminLastName(username);
  }
}
