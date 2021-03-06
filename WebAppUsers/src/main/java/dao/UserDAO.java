package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.User;
public class UserDAO {
 // definicja parametrów połączenia z bazą danych PGSQL za pomocą JDBC
 private final String url =
 "jdbc:postgresql://localhost:5433/appusers?currentSchema=\"users\"";
 private final String user = "postgres";
 private final String password = "password";
 // koniec - parametrów połączenia z PGSQL
//instrukcje SQL do realizacji CRUID (Create, Read, Update, Insert, Delete)
//read / select data - all users
private static final String SELECT_ALL_USERS = "select * from users";
//read / select data user by id
private static final String SELECT_USER_BY_ID = "select id, first_name,last_name, year_of_study, email, personal_id, country from users where id =?";
//insert data
private static final String INSERT_USERS_SQL = "INSERT INTO users" + "(first_name, last_name, year_of_study, email, personal_id, country) VALUES (?,?, ?, ?, ?, ?);";
 Connection connection = null;

 /**
 * Connect to the PostgreSQL database
 *
 * @return a Connection object
 */
 //rozpoczęcie polączenia
 public Connection DBSQLConnection() {
 try {
 Class.forName("org.postgresql.Driver");
 connection = DriverManager.getConnection(url, user, password);
 if(connection.isValid(0)) System.out.println("Connection is working");
 }
 catch (SQLException e) {
 e.printStackTrace();
 }
 catch (ClassNotFoundException e) {
 e.printStackTrace();
 }
 System.out.println(connection);
 return connection;
 }
//zakończenie polączenia
private void DBSQLConnectionClose(){
if(connection==null) return;
try{
connection.close();
if(!connection.isValid(5)) System.out.println("Connection closed");
}
catch(SQLException e){
e.printStackTrace();}
}
// kontruktor, który nic nie wykonuje
public UserDAO() {
}
private void printSQLException(SQLException ex) {
	 for (Throwable e: ex) {
	 if (e instanceof SQLException) {
	 e.printStackTrace(System.err);
	 System.err.println("SQLState: " + ((SQLException) e).getSQLState());
	 System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
	 System.err.println("Message: " + e.getMessage());
	 Throwable t = ex.getCause();
	 while (t != null) {
	 System.out.println("Cause: " + t);
	 t = t.getCause();
	 }
	 }
	 }
	}
/*pobranie z bazy danych użytkownika o podanym id i zapisanie ich w obiekcie*/
public User selectUser(int id) {
 User user = null;
 //połączenie
 try(
		 Connection connection = DBSQLConnection();
		 //utworzenie obiektu reprezentującego prekompilowane zapytanie SQL
		 PreparedStatement preparedStatement =
		 connection.prepareStatement(SELECT_USER_BY_ID);)
		 {
		 //ustawienie parametru
		 preparedStatement.setInt(1, id);
		 //wyświetlenie zapytania w konsoli
		 System.out.println(preparedStatement);
		 // Wykonanie zapytania
		 ResultSet rs = preparedStatement.executeQuery();
		 // Proces obsługi rezultatu.
		 while (rs.next()) {
		 String t_first_name = rs.getString("first_name");
		 String t_last_name = rs.getString("last_name");
		 int t_year_of_study = rs.getInt("year_of_study");
		 String t_email = rs.getString("email");
		 Long t_personal_id = rs.getLong("personal_id");
		 String t_country = rs.getString("country");
		 user = new User(id, t_first_name, t_last_name, t_year_of_study,
		 t_email, t_personal_id, t_country);
		 }
		 }
		 catch (SQLException e) { printSQLException(e); }
		 return user;
		}
/* dodanie rekordu z danymi nowego użytkownika*/
public void insertUser(User user) throws SQLException
{
 System.out.println(INSERT_USERS_SQL);
 try(Connection connection = DBSQLConnection();
 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL))
 {
 preparedStatement.setString(1, user.getFirst_name());
 preparedStatement.setString(2, user.getLast_name());
 preparedStatement.setInt(3, user.getYear_of_study());
 preparedStatement.setString(4, user.getEmail());
 preparedStatement.setLong(5, (Long)user.getPersonal_id());
 preparedStatement.setString(6, user.getCountry());
 System.out.println(preparedStatement);
 preparedStatement.executeUpdate();
 }
 catch (SQLException e) { printSQLException(e); }
}
/* lista użytkowników */
 //połączenia
 public ArrayList<User> selectAllUsers() {
		ArrayList<User> users = new ArrayList<>();
		Connection connection = DBSQLConnection();
		
		//połączenia
		try (
			
			PreparedStatement preparedStatement =
			connection.prepareStatement(SELECT_ALL_USERS);){
				System.out.println(preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				// odebranie wyników z obiektu ResultSet.
				while (rs.next()) {
					int t_id = rs.getInt("id");
					String t_first_name = rs.getString("first_name");
					String t_last_name = rs.getString("last_name");
					int t_year_of_study = rs.getInt("year_of_study");
					String t_email = rs.getString("email");
					Long t_personal_id= rs.getLong("personal_id");
					String t_country = rs.getString("country");
					users.add(new User(t_id, t_first_name, t_last_name, t_year_of_study,
					t_email, t_personal_id, t_country));
					}
			}
			catch (SQLException e) { 
				printSQLException(e);
				System.out.println(connection);
				
			}
			return users;
	}

 
// metoda main do testowania
public static void main(String[] args)
{
UserDAO dao = new UserDAO();
User u1= dao.selectUser(1);
System.out.println(u1.getFirst_name());
ArrayList<User> u2=dao.selectAllUsers();
System.out.println(u2.toString());
System.out.println(u1.getLast_name());
System.out.println(u1.getPersonal_id());
dao.DBSQLConnection();
}
}
