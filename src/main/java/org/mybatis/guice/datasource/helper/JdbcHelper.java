/*
 *    Copyright 2010 The myBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.datasource.helper;

/**
 * 
 *
 * @version $Id$
 */
public enum JdbcHelper {

    Cache("jdbc:Cache://${JDBC.host}:${JDBC.port|1972}/${JDBC.schema}", "com.intersys.jdbc.CacheDriver"),

    Daffodil_DB("jdbc:daffodilDB://${JDBC.host}:${JDBC.port|3456}/${JDBC.schema}", "in.co.daffodil.db.rmi.RmiDaffodilDBDriver"),

    DB2("jdbc:db2://${JDBC.host}:${JDBC.port|50000}/${JDBC.schema}", "com.ibm.db2.jcc.DB2Driver"),

    DB2_DataDirect("jdbc:datadirect:db2://${JDBC.host}:${JDBC.port|50000}/DatabaseName=${JDBC.schema}", "com.ddtek.jdbc.db2.DB2Driver"),

    DB2_AS400_JTOpen("jdbc:as400://${JDBC.host}", "com.ibm.as400.access.AS400JDBCDriver"),

    Firebird("jdbc:firebirdsql:${JDBC.host}/${JDBC.port|3050}:${JDBC.schema}", "org.firebirdsql.jdbc.FBDriver"),

    FrontBase("jdbc:FrontBase://${JDBC.host}/${JDBC.schema}", "jdbc.FrontBase.FBJDriver"),

    HP_Neoview("jdbc:hpt4jdbc://${system}:${JDBC.port}/:", "com.hp.t4jdbc.HPT4Driver"),

    HSQLDB_Server("jdbc:hsqldb:hsql://${JDBC.host}:${JDBC.port|9001}/${JDBC.schema}", "org.hsqldb.jdbcDriver"),

    HSQLDB_Embedded("jdbc:hsqldb:${JDBC.schema}", "org.hsqldb.jdbcDriver"),

    Informix("jdbc:informix-sqli://${JDBC.host}:${JDBC.port|1533}/${JDBC.schema}:informixserver=${dbservername}", "com.informix.jdbc.IfxDriver"),

    Informix_DataDirect("jdbc:datadirect:informix://${JDBC.host}:${JDBC.port|1533};InformixServer=${informixserver};DatabaseServer=${JDBC.schema}", "com.ddtek.jdbc.informix.InformixDriver"),

    Derby_Server("jdbc:derby://${JDBC.host}:${JDBC.port|1527}/${JDBC.schema}", "org.apache.derby.jdbc.ClientDriver"),

    Derby_Embedded("jdbc:derby:${JDBC.schema}", "org.apache.derby.jdbc.EmbeddedDriver"),

    JDataStore("jdbc:borland:dslocal:${JDBC.schema}", "com.borland.datastore.jdbc.DataStoreDriver"),

    JDBC_ODBC_Bridge("jdbc:odbc:${ODBC.datasource}", "sun.jdbc.odbc.JdbcOdbcDriver"),

    MaxDB("jdbc:sapdb://${JDBC.host}:${JDBC.port|7210}/${JDBC.schema}", "com.sap.dbtech.jdbc.DriverSapDB"),

    McKoi("jdbc:mckoi://${JDBC.host}:${JDBC.port|9157}/${JDBC.schema}", "com.mckoi.JDBCDriver"),

    Mimer("jdbc:mimer:${protocol}://${JDBC.host}:${JDBC.port|1360}/${JDBC.schema}", "com.mimer.jdbc.Driver"),

    MySQL("jdbc:mysql://${JDBC.host}:${JDBC.port|3306}/${JDBC.schema}", "com.mysql.jdbc.Driver"),

    Netezza("jdbc:netezza://${JDBC.host}:${JDBC.port|5480}/${JDBC.schema}", "org.netezza.Driver"),

    Oracle_Thin("jdbc:oracle:thin:@${JDBC.host}:${JDBC.port|1521}:${JDBC.schema}", "oracle.jdbc.OracleDriver"),

    Oracle_OCI("jdbc:oracle:oci:@${JDBC.host}:${JDBC.port|1521}:${JDBC.schema}", "oracle.jdbc.OracleDriver"),

    Oracle_DataDirect("jdbc:datadirect:oracle://${JDBC.host}:${JDBC.port|1521};ServiceName=${servicename}", "com.ddtek.jdbc.oracle.OracleDriver"),

    Pervasive("jdbc:pervasive://${JDBC.host}:${JDBC.port}/${JDBC.schema}", "pervasive.jdbc.PervasiveDriver"),

    Pointbase_Embedded("jdbc:pointbase:embedded:${:database}", "com.pointbase.jdbc.jdbcUniversalDriver"),

    Pointbase_Server("jdbc:pointbase:server://${JDBC.host}:${JDBC.port|9092}/${JDBC.schema}", "com.pointbase.jdbc.jdbcUniversalDriver"),

    PostgreSQL("jdbc:postgresql://${JDBC.host}:${JDBC.port|5432}/${JDBC.schema}", "org.postgresql.Driver"),

    Progress("jdbc:jdbcProgress:T:${JDBC.host}:${JDBC.port|2055}:${JDBC.schema}", "com.progress.sql.jdbc.JdbcProgressDriver"),

    SQL_Server_DataDirect("jdbc:datadirect:sqlserver://${JDBC.host}:${JDBC.port|1433};DatabaseName=${JDBC.schema}", "com.ddtek.jdbc.sqlserver.SQLServerDriver"),

    SQL_Server_jTDS("jdbc:jtds:sqlserver://${JDBC.host}:${JDBC.port|1433};DatabaseName=${JDBC.schema}", "net.sourceforge.jtds.jdbc.Driver"),

    SQL_Server_MS_Driver("jdbc:microsoft:sqlserver://${JDBC.host}:${JDBC.port|1433};DatabaseName=${JDBC.schema}", "com.microsoft.jdbc.sqlserver.SQLServerDriver"),

    SQL_Server_2005_MS_Driver("jdbc:sqlserver://${JDBC.host}:${JDBC.port|1433};DatabaseName=${JDBC.schema}", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),

    Sybase_ASE_jTDS("jdbc:jtds:sybase://${JDBC.host}:${JDBC.port|5000};DatabaseName=${JDBC.schema}", "net.sourceforge.jtds.jdbc.Driver"),

    Sybase_ASE_JConnect("jdbc:sybase:Tds:${JDBC.host}:${JDBC.port|5000}/${JDBC.schema}", "com.sybase.jdbc3.jdbc.SybDriver"),

    Sybase_SQL_Anywhere_JConnect("jdbc:sybase:Tds:${JDBC.host}:${JDBC.port|2638}/${JDBC.schema}", "com.sybase.jdbc3.jdbc.SybDriver"),

    Sybase_DataDirect("jdbc:datadirect:sybase://${JDBC.host}:${JDBC.port|2048};ServiceName=${servicename}", "com.ddtek.jdbc.sybase.SybaseDriver");

    private final String urlTemplate;

    private final String driverClass;

    JdbcHelper(String urlTemplate, String driverClass) {
        this.urlTemplate = urlTemplate;
        this.driverClass = driverClass;
    }

    public String getUrlTemplate() {
        return this.urlTemplate;
    }

    public String getDriverClass() {
        return this.driverClass;
    }

}
