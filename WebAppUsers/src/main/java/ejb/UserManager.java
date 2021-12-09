package ejb;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import dao.UserDAO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import model.User;

@Model
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user;
	private ArrayList<User> users;
	private UserDAO userDAO=new UserDAO();
	
	@PostConstruct
	private void init(){
		user=new User();
		UserDAO udao=new UserDAO();
		setUsers(udao.selectAllUsers());
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	 
	public String insertUser(User u) throws SQLException {
		userDAO.insertUser(u);
		return ("index");
	}
	
	public String selectUser(int id) throws SQLException {
		user = new User();
		setUser(userDAO.selectUser(id));
		return ("update");
	}

	
}
