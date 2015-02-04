SA38Team07 Uno Project

Softwares Required:

Netbeans 8.0,
 Jersey_SSE Library,
 Mysql 6.1CE,
 HTML5,
 JAVAEE 7


JDBC Realm:

Name: DBRealm
JAAS Context: 			jdbcRealm
JNDI: 				jdbc/sa38team07
User table: 			logintable
User name column: 		username
Password Column: 		password
Group table: 			grouptable
Group Table User Name Column: 	username
Group Name column: 		groupid
Password encryption algorithm: 	none
Digest algorithm: 		SHA-256

--------

JDBC Pool name 	sa38team07pool

Resource type: 	 	java.sql.driver
Database vender: 	MySql
Database Schema: 	sa38team07Uno
JDBC Resource		jdbc/sa38team07

ManagedScheduledExecutorService	concurrent/unoThreadpool

----------------------------
