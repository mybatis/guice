/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.datasource.helper;

import static com.google.inject.name.Names.named;
import static com.google.inject.util.Providers.guicify;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;

/**
 * Helper to bind <code>JDBC.driver</code> and <code>JDBC.url</code> properties.
 */
public enum JdbcHelper implements Module {

  Cache("jdbc:Cache://${JDBC.host|localhost}:${JDBC.port|1972}/${JDBC.schema}", "com.intersys.jdbc.CacheDriver"),

  Daffodil_DB("jdbc:daffodilDB://${JDBC.host|localhost}:${JDBC.port|3456}/${JDBC.schema}",
      "in.co.daffodil.db.rmi.RmiDaffodilDBDriver"),

  DB2("jdbc:db2://${JDBC.host|localhost}:${JDBC.port|50000}/${JDBC.schema}", "com.ibm.db2.jcc.DB2Driver"),

  DB2_DataDirect("jdbc:datadirect:db2://${JDBC.host|localhost}:${JDBC.port|50000}/DatabaseName=${JDBC.schema}",
      "com.ddtek.jdbc.db2.DB2Driver"),

  DB2_AS400_JTOpen("jdbc:as400://${JDBC.host|localhost}", "com.ibm.as400.access.AS400JDBCDriver"),

  Firebird("jdbc:firebirdsql:${JDBC.host|localhost}/${JDBC.port|3050}:${JDBC.schema}", "org.firebirdsql.jdbc.FBDriver"),

  FrontBase("jdbc:FrontBase://${JDBC.host|localhost}/${JDBC.schema}", "jdbc.FrontBase.FBJDriver"),

  HP_Neoview("jdbc:hpt4jdbc://${neoview.system}:${JDBC.port}/:", "com.hp.t4jdbc.HPT4Driver"),

  HSQLDB_Server("jdbc:hsqldb:hsql://${JDBC.host|localhost}:${JDBC.port|9001}/${JDBC.schema}", "org.hsqldb.jdbcDriver"),

  HSQLDB_Embedded("jdbc:hsqldb:${JDBC.schema}", "org.hsqldb.jdbcDriver"),

  HSQLDB_IN_MEMORY_NAMED("jdbc:hsqldb:mem:${JDBC.schema|aname}", "org.hsqldb.jdbcDriver"),

  H2_IN_MEMORY_PRIVATE("jdbc:h2:mem", "org.h2.Driver"),

  H2_IN_MEMORY_NAMED("jdbc:h2:mem:${JDBC.schema}", "org.h2.Driver"),

  H2_SERVER_TCP("jdbc:h2:tcp://${JDBC.host|localhost}:${JDBC.port|9001}/${JDBC.schema}", "org.h2.Driver"),

  H2_SERVER_SSL("jdbc:h2:ssl://${JDBC.host|localhost}:${JDBC.port|9001}/${JDBC.schema}", "org.h2.Driver"),

  H2_FILE("jdbc:h2:file://${JDBC.schema}", "org.h2.Driver"),

  H2_EMBEDDED("jdbc:h2:${JDBC.schema}", "org.h2.Driver"),

  Informix(
      "jdbc:informix-sqli://${JDBC.host|localhost}:${JDBC.port|1533}/${JDBC.schema}:informixserver=${dbservername}",
      "com.informix.jdbc.IfxDriver"),

  Informix_DataDirect(
      "jdbc:datadirect:informix://${JDBC.host|localhost}:${JDBC.port|1533};InformixServer=${informixserver};DatabaseServer=${JDBC.schema}",
      "com.ddtek.jdbc.informix.InformixDriver"),

  Derby_Server("jdbc:derby://${JDBC.host|localhost}:${JDBC.port|1527}/${JDBC.schema}",
      "org.apache.derby.jdbc.ClientDriver"),

  Derby_Embedded("jdbc:derby:${JDBC.schema};create=${derby.create|false}", "org.apache.derby.jdbc.EmbeddedDriver"),

  JDataStore("jdbc:borland:dslocal:${JDBC.schema}", "com.borland.datastore.jdbc.DataStoreDriver"),

  JDBC_ODBC_Bridge("jdbc:odbc:${ODBC.datasource}", "sun.jdbc.odbc.JdbcOdbcDriver"),

  MariaDB("jdbc:mysql://${JDBC.host|localhost}:${JDBC.port|3306}/${JDBC.schema}", "org.mariadb.jdbc.Driver"),

  MariaDB_Aurora("jdbc:mysql:aurora//${JDBC.host|localhost}:${JDBC.port|3306}/${JDBC.schema}",
      "org.mariadb.jdbc.Driver"),

  MariaDB_Sequential("jdbc:mysql:sequential//${JDBC.host|localhost}:${JDBC.port|3306}/${JDBC.schema}",
      "org.mariadb.jdbc.Driver"),

  MariaDB_Loadbalance("jdbc:mysql:loadbalance//${JDBC.host|localhost}:${JDBC.port|3306}/${JDBC.schema}",
      "org.mariadb.jdbc.Driver"),

  MariaDB_Replication("jdbc:mysql:replication//${JDBC.host|localhost}:${JDBC.port|3306}/${JDBC.schema}",
      "org.mariadb.jdbc.Driver"),

  MaxDB("jdbc:sapdb://${JDBC.host|localhost}:${JDBC.port|7210}/${JDBC.schema}", "com.sap.dbtech.jdbc.DriverSapDB"),

  McKoi("jdbc:mckoi://${JDBC.host|localhost}:${JDBC.port|9157}/${JDBC.schema}", "com.mckoi.JDBCDriver"),

  Mimer("jdbc:mimer:${mimer.protocol}://${JDBC.host|localhost}:${JDBC.port|1360}/${JDBC.schema}",
      "com.mimer.jdbc.Driver"),

  MySQL("jdbc:mysql://${JDBC.host|localhost}:${JDBC.port|3306}/${JDBC.schema}", "com.mysql.jdbc.Driver"),

  Netezza("jdbc:netezza://${JDBC.host|localhost}:${JDBC.port|5480}/${JDBC.schema}", "org.netezza.Driver"),

  Oracle_Thin("jdbc:oracle:thin:@${JDBC.host|localhost}:${JDBC.port|1521}:${oracle.sid|ORCL}",
      "oracle.jdbc.OracleDriver"),

  Oracle_Service("jdbc:oracle:thin:@//${JDBC.host|localhost}:${JDBC.port|1521}/${oracle.servicename|ORCL}",
      "oracle.jdbc.OracleDriver"),

  Oracle_OCI("jdbc:oracle:oci:@${JDBC.host|localhost}:${JDBC.port|1521}:${oracle.sid|ORCL}",
      "oracle.jdbc.OracleDriver"),

  Oracle_DataDirect(
      "jdbc:datadirect:oracle://${JDBC.host|localhost}:${JDBC.port|1521};ServiceName=${oracle.servicename|ORCL}",
      "com.ddtek.jdbc.oracle.OracleDriver"),

  Pervasive("jdbc:pervasive://${JDBC.host|localhost}:${JDBC.port}/${JDBC.schema}", "pervasive.jdbc.PervasiveDriver"),

  Pointbase_Embedded(
      "jdbc:pointbase:embedded:${JDBC.schema},database.home=${pointbase.home},create=${pointbase.create|false}",
      "com.pointbase.jdbc.jdbcUniversalDriver"),

  Pointbase_Server(
      "jdbc:pointbase:server://${JDBC.host|localhost}:${JDBC.port|9092}/${JDBC.schema},database.home=${pointbase.home},create=${pointbase.create|false}",
      "com.pointbase.jdbc.jdbcUniversalDriver"),

  PostgreSQL("jdbc:postgresql://${JDBC.host|localhost}:${JDBC.port|5432}/${JDBC.schema}", "org.postgresql.Driver"),

  Progress("jdbc:jdbcProgress:T:${JDBC.host|localhost}:${JDBC.port|2055}:${JDBC.schema}",
      "com.progress.sql.jdbc.JdbcProgressDriver"),

  SQL_Server_DataDirect(
      "jdbc:datadirect:sqlserver://${JDBC.host|localhost}:${JDBC.port|1433};DatabaseName=${JDBC.schema|Northwind}",
      "com.ddtek.jdbc.sqlserver.SQLServerDriver"),

  SQL_Server_jTDS(
      "jdbc:jtds:sqlserver://${JDBC.host|localhost}:${JDBC.port|1433};DatabaseName=${JDBC.schema|Northwind};domain=${sqlserver.domain}",
      "net.sourceforge.jtds.jdbc.Driver"),

  SQL_Server_MS_Driver(
      "jdbc:microsoft:sqlserver://${JDBC.host|localhost}:${JDBC.port|1433};DatabaseName=${JDBC.schema|Northwind}",
      "com.microsoft.jdbc.sqlserver.SQLServerDriver"),

  SQL_Server_2005_MS_Driver(
      "jdbc:sqlserver://${JDBC.host|localhost}:${JDBC.port|1433};DatabaseName=${JDBC.schema|Northwind}",
      "com.microsoft.sqlserver.jdbc.SQLServerDriver"),

  SQLITE_FILE("jdbc:sqlite:${JDBC.schema}", "org.sqlite.JDBC"),

  SQLITE_IN_MEMORY("jdbc:sqlite::memory:", "org.sqlite.JDBC"),

  Sybase_ASE_jTDS("jdbc:jtds:sybase://${JDBC.host|localhost}:${JDBC.port|5000};DatabaseName=${JDBC.schema}",
      "net.sourceforge.jtds.jdbc.Driver"),

  Sybase_ASE_JConnect("jdbc:sybase:Tds:${JDBC.host|localhost}:${JDBC.port|5000}/${JDBC.schema}",
      "com.sybase.jdbc3.jdbc.SybDriver"),

  Sybase_SQL_Anywhere_JConnect("jdbc:sybase:Tds:${JDBC.host|localhost}:${JDBC.port|2638}/${JDBC.schema}",
      "com.sybase.jdbc3.jdbc.SybDriver"),

  Sybase_DataDirect("jdbc:datadirect:sybase://${JDBC.host|localhost}:${JDBC.port|2048};ServiceName=${JDBC.schema}",
      "com.ddtek.jdbc.sybase.SybaseDriver");

  private static final String JDBC_DRIVER = "JDBC.driver";

  private static final String JDBC_URL = "JDBC.url";

  private final String urlTemplate;

  private final String driverClass;

  JdbcHelper(String urlTemplate, String driverClass) {
    this.urlTemplate = urlTemplate;
    this.driverClass = driverClass;
  }

  @Override
  public void configure(Binder binder) {
    binder.bindConstant().annotatedWith(named(JDBC_DRIVER)).to(driverClass);
    binder.bind(Key.get(String.class, named(JDBC_URL))).toProvider(guicify(new JdbcUrlAntFormatter(urlTemplate)));
  }

}
