/**
 * Database Handler
 * Abstract superclass for all subclass database files.
 * 
 * Date Created: 2011-08-26 19:08
 * @author PatPeter
 */
package lib.PatPeter.SQLibrary;

/*
 *  MySQL
 */
import java.net.MalformedURLException;

/*
 *  SQLLite
 */
//import java.io.File;
//import java.sql.DatabaseMetaData;

/*
 *  Both
 */
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.logging.Logger;

public abstract class DatabaseHandler {
	protected Logger log;
	protected final String PREFIX;
	protected final String DATABASE_PREFIX;
	protected Connection connection;
	protected enum Statements {
		SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, // Data manipulation statements
		CREATE, ALTER, DROP, TRUNCATE, RENAME  // Data definition statements
	}
	
	/*
	 *  MySQL, SQLLite
	 */
	
	public DatabaseHandler(Logger log, String prefix, String dp) {
		this.log = log;
		this.PREFIX = prefix;
		this.DATABASE_PREFIX = dp;
		this.connection = null;
	}
	
	/**
	 * <b>writeInfo</b><br>
	 * <br>
	 * &nbsp;&nbsp;Writes information to the console.
	 * <br>
	 * <br>
	 * @param toWrite - the <a href="http://download.oracle.com/javase/6/docs/api/java/lang/String.html">String</a>
	 * of content to write to the console.
	 */
	protected void writeInfo(String toWrite) {
		if (toWrite != null) {
			this.log.info(this.PREFIX + this.DATABASE_PREFIX + toWrite);
		}
	}
	
	/**
	 * <b>writeError</b><br>
	 * <br>
	 * &nbsp;&nbsp;Writes either errors or warnings to the console.
	 * <br>
	 * <br>
	 * @param toWrite - the <a href="http://download.oracle.com/javase/6/docs/api/java/lang/String.html">String</a>
	 * written to the console.
	 * @param severe - whether console output should appear as an error or warning.
	 */
	protected void writeError(String toWrite, boolean severe) {
		if (toWrite != null) {
			if (severe) {
				this.log.severe(this.PREFIX + this.DATABASE_PREFIX + toWrite);
			} else {
				this.log.warning(this.PREFIX + this.DATABASE_PREFIX + toWrite);
			}
		}
	}
	
	/**
	 * <b>initialize</b><br>
	 * <br>
	 * &nbsp;&nbsp;Used to check whether the class for the SQL engine is installed.
	 * <br>
	 * <br>
	 */
	abstract boolean initialize();
	
	/**
	 * <b>open</b><br>
	 * <br>
	 * &nbsp;&nbsp;Opens a connection with the database.
	 * <br>
	 * <br>
	 * @return the success of the method.
	 * @throws MalformedURLException - cannot access database because of a syntax error in the jdbc:// protocol.
	 * @throws InstantiationException - cannot instantiate an interface or abstract class.
	 * @throws IllegalAccessException - cannot access classes, fields, methods, or constructors that are private.
	 */
	abstract Connection open()
		throws MalformedURLException, InstantiationException, IllegalAccessException;
	
	/**
	 * <b>close</b><br>
	 * <br>
	 * &nbsp;&nbsp;Closes a connection with the database.
	 * <br>
	 * <br>
	 */
	abstract void close();
	
	/**
	 * <b>getConnection</b><br>
	 * <br>
	 * &nbsp;&nbsp;Gets the connection variable 
	 * <br>
	 * <br>
	 * @return the <a href="http://download.oracle.com/javase/6/docs/api/java/sql/Connection.html">Connection</a> variable.
	 * @throws MalformedURLException - cannot access database because of a syntax error in the jdbc:// protocol.
	 * @throws InstantiationException - cannot instantiate an interface or abstract class.
	 * @throws IllegalAccessException - cannot access classes, fields, methods, or constructors that are private.
	 */
	abstract Connection getConnection()
		throws MalformedURLException, InstantiationException, IllegalAccessException;
	
	/**
	 * <b>checkConnection</b><br>
	 * <br>
	 * Checks the connection between Java and the database engine.
	 * <br>
	 * <br>
	 * @return the status of the connection, true for up, false for down.
	 */
	abstract boolean checkConnection();
	
	/**
	 * <b>query</b><br>
	 * &nbsp;&nbsp;Sends a query to the SQL database.
	 * <br>
	 * <br>
	 * @param query - the SQL query to send to the database.
	 * @return the table of results from the query.
	 * @throws MalformedURLException - cannot access database because of a syntax error in the jdbc:// protocol.
	 * @throws InstantiationException - cannot instantiate an interface or abstract class.
	 * @throws IllegalAccessException - cannot access classes, fields, methods, or constructors that are private.
	 */
	abstract ResultSet query(String query)
		throws MalformedURLException, InstantiationException, IllegalAccessException;
	
	/**
	 * <b>getStatement</b><br>
	 * 
	 * <br>
	 * <br>
	 */
	protected Statements getStatement(String query) {
		String trimmedQuery = query.trim();
		if (trimmedQuery.substring(0,6).equals("SELECT"))
			return Statements.SELECT;
		else if (trimmedQuery.substring(0,6).equals("INSERT"))
			return Statements.INSERT;
		else if (trimmedQuery.substring(0,6).equals("UPDATE"))
			return Statements.UPDATE;
		else if (trimmedQuery.substring(0,6).equals("DELETE"))
			return Statements.DELETE;
		else if (trimmedQuery.substring(0,6).equals("CREATE"))
			return Statements.CREATE;
		else if (trimmedQuery.substring(0,5).equals("ALTER"))
			return Statements.ALTER;
		else if (trimmedQuery.substring(0,4).equals("DROP"))
			return Statements.DROP;
		else if (trimmedQuery.substring(0,8).equals("TRUNCATE"))
			return Statements.TRUNCATE;
		else if (trimmedQuery.substring(0,6).equals("RENAME"))
			return Statements.RENAME;
		else if (trimmedQuery.substring(0,2).equals("DO"))
			return Statements.DO;
		else if (trimmedQuery.substring(0,7).equals("REPLACE"))
			return Statements.REPLACE;
		else if (trimmedQuery.substring(0,4).equals("LOAD"))
			return Statements.LOAD;
		else if (trimmedQuery.substring(0,7).equals("HANDLER"))
			return Statements.HANDLER;
		else if (trimmedQuery.substring(0,4).equals("CALL"))
			return Statements.CALL;
		else
			return Statements.SELECT;
	}
	
	/**
	 * <b>createTable</b><br>
	 * <br>
	 * &nbsp;&nbsp;Creates a table in the database based on a specified query.
	 * <br>
	 * <br>
	 * @param query - the SQL query for creating a table.
	 * @return the success of the method.
	 */
	abstract boolean createTable(String query);
	
	/**
	 * <b>checkTable</b><br>
	 * <br>
	 * &nbsp;&nbsp;Checks a table in a database based on the table's name.
	 * <br>
	 * <br>
	 * @param table - name of the table to check.
	 * @return success of the method.
	 * @throws MalformedURLException - cannot access database because of a syntax error in the jdbc:// protocol.
	 * @throws InstantiationException - cannot instantiate an interface or abstract class.
	 * @throws IllegalAccessException - cannot access classes, fields, methods, or constructors that are private.
	 */
	abstract boolean checkTable(String table)
		throws MalformedURLException, InstantiationException, IllegalAccessException;
	
	/**
	 * <b>wipeTable</b><br>
	 * <br>
	 * &nbsp;&nbsp;Wipes a table given its name.
	 * <br>
	 * <br>
	 * @param table - name of the table to wipe.
	 * @return success of the method.
	 * @throws MalformedURLException - cannot access database because of a syntax error in the jdbc:// protocol.
	 * @throws InstantiationException - cannot instantiate an interface or abstract class.
	 * @throws IllegalAccessException - cannot access classes, fields, methods, or constructors that are private.
	 */
	abstract boolean wipeTable(String table)
		throws MalformedURLException, InstantiationException, IllegalAccessException;
	
	/*
	 *  SQLLite
	 */
	
	/*
	 * <b>retry</b><br>
	 * <br>
	 * Retries.
	 * <br>
	 * <br>
	 * @param query The SQL query.
	 */
	//abstract void retry(String query);
	
	/*
	 * Retries a result.
	 * 
	 * @param query The SQL query to retry.
	 * @return The SQL query result.
	 */
	//abstract ResultSet retryResult(String query);
}