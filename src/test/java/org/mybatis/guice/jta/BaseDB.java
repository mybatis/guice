/*
 *    Copyright 2009-2026 the original author or authors.
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
package org.mybatis.guice.jta;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionSynchronizationRegistryImple;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.security.NamePrincipal;
import io.agroal.api.security.SimplePassword;
import io.agroal.narayana.NarayanaTransactionIntegration;

import jakarta.transaction.TransactionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseDB {
  private static final Logger LOGGER = LoggerFactory.getLogger(BaseDB.class);

  public static final String NAME_DB1 = "target/db1";
  public static final String NAME_DB2 = "target/db2";
  public static final String URL_DB1 = "jdbc:derby:" + NAME_DB1;
  public static final String URL_DB2 = "jdbc:derby:" + NAME_DB2;
  static final String USER = "SA";
  static final String PASSWORD = "";

  static final String QUERY_CREATE_TABLE = "create table table1 (" + "id integer not null,"
      + "name varchar(80) not null," + "constraint pk_table1 primary key (id) )";
  static final String QUERY_DROP_TABLE = "drop table table1";
  static final String QUERY_INSERT = "insert into table1 (id, name) values (?,?)";
  static final String QUERY_SELECT = "select id from table1";
  static final String QUERY_DELETE = "delete from table1";

  public static DataSource createLocalDataSource(String dataSourceURL, TransactionManager manager) throws Exception {
    executeScript(dataSourceURL + ";create=true", QUERY_CREATE_TABLE);

    // Use connectable mode for local datasource when mixed with XA datasources
    return AgroalDataSource.from(new AgroalDataSourceConfigurationSupplier().connectionPoolConfiguration(cp -> cp
        .maxSize(10)
        .connectionFactoryConfiguration(
            cf -> cf.jdbcUrl(dataSourceURL).principal(new NamePrincipal(USER)).credential(new SimplePassword(PASSWORD)))
        .transactionIntegration(
            new NarayanaTransactionIntegration(manager, new TransactionSynchronizationRegistryImple(), null, true))));
  }

  public static DataSource createXADataSource(String dataSourceURL, TransactionManager manager) throws Exception {
    executeScript(dataSourceURL + ";create=true", QUERY_CREATE_TABLE);

    // Use Derby's XA DataSource for proper XA support
    return AgroalDataSource
        .from(
            new AgroalDataSourceConfigurationSupplier().connectionPoolConfiguration(cp -> cp.maxSize(10)
                .connectionFactoryConfiguration(cf -> cf
                    .connectionProviderClassName("org.apache.derby.jdbc.EmbeddedXADataSource")
                    .xaProperty("databaseName", dataSourceURL.replace("jdbc:derby:", ""))
                    .xaProperty("createDatabase", "create").xaProperty("user", USER).xaProperty("password", PASSWORD))
                .transactionIntegration(
                    new NarayanaTransactionIntegration(manager, new TransactionSynchronizationRegistryImple()))));
  }

  public static void dropTable(String dataSourceURL) throws Exception {
    executeScript(dataSourceURL, QUERY_DROP_TABLE);
  }

  public static void clearTable(String dataSourceURL) throws Exception {
    executeScript(dataSourceURL, QUERY_DELETE);
  }

  public static void insertRow(Connection con, int id, String name) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(QUERY_INSERT)) {
      stmt.setInt(1, id);
      stmt.setString(2, name);
      stmt.executeUpdate();
    }
  }

  public static List<Integer> readRows(String dataSourceURL, String dataSourceName) throws Exception {
    List<Integer> list = new ArrayList<Integer>(2);
    try (Connection connection = DriverManager.getConnection(dataSourceURL, USER, PASSWORD);
        PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT); ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        int id = rs.getInt(1);
        list.add(id);
        LOGGER.info("read {} from {}", id, dataSourceName);
      }
    }

    return list;
  }

  private static void executeScript(String dataSourceName, String query) throws Exception {
    try (Connection connection = DriverManager.getConnection(dataSourceName, USER, PASSWORD);
        Statement stmt = connection.createStatement()) {
      stmt.execute(query);
    }
  }
}
