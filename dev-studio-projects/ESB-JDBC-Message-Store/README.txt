1) Create data spurce
<datasource>
         <name>jdbc_ms_db</name>
         <description>The datasource used for testing</description>
         <jndiConfig>
            <name>jdbc/jdbc_ms_db</name>
         </jndiConfig>
         <definition type="RDBMS">
            <configuration>
               <url>jdbc:mysql://localhost:3306/jdbc_ms_db</url>
               <username>user</username>
               <password>pass</password>
               <defaultAutoCommit>true</defaultAutoCommit>
               <driverClassName>com.mysql.jdbc.Driver</driverClassName>
               <maxActive>50</maxActive>
               <maxWait>60000</maxWait>
               <testOnBorrow>true</testOnBorrow>
               <validationQuery>SELECT 1</validationQuery>
               <validationInterval>30000</validationInterval>
            </configuration>
         </definition>
      </datasource>

2) Create table


CREATE TABLE jdbc_message_store(
indexId BIGINT( 20 ) NOT NULL AUTO_INCREMENT ,
msg_id VARCHAR( 200 ) NOT NULL ,
message BLOB NOT NULL ,
PRIMARY KEY ( indexId )
)

3) 
curl -v  -X POST -d "<msg>hello</msg>" -H "Content-Type:application/xml" http://localhost:8280/addtojdbcstore 
