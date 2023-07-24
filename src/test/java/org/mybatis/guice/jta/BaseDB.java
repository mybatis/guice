/*
 *    Copyright 2009-2023 the original author or authors.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

  public static DataSource createLocalDataSource(String dataSourceURL,
                                                 TransactionManager manager) throws Exception {
    executeScript(dataSourceURL + ";create=true", QUERY_CREATE_TABLE);

    DataSource recoverableDataSource = AgroalDataSource
            .from( new AgroalDataSourceConfigurationSupplier().connectionPoolConfiguration(
                    cp -> cp.maxSize( 10 )
                            .connectionFactoryConfiguration( cf -> cf.jdbcUrl( dataSourceURL )
                                                                     .principal( new NamePrincipal( USER ) ).credential(
                                            new SimplePassword( PASSWORD ) ) )
                            .transactionIntegration(
                                    new NarayanaTransactionIntegration(
                                            manager, new TransactionSynchronizationRegistryImple() ) ) ) );


    return recoverableDataSource;
  }

  public static DataSource createXADataSource(String dataSourceURL, TransactionManager manager)
      throws Exception {
    String className = "org.apache.derby.jdbc.EmbeddedDriver";
    Class.forName(className).newInstance();

    executeScript(dataSourceURL + ";create=true", QUERY_CREATE_TABLE);

    DataSource recoverableDataSource = AgroalDataSource
            .from( new AgroalDataSourceConfigurationSupplier().connectionPoolConfiguration(
                    cp -> cp.maxSize( 10 )
                            .connectionFactoryConfiguration( cf -> cf.jdbcUrl( dataSourceURL )
                                                                     .principal( new NamePrincipal( USER ) ).credential(
                                            new SimplePassword( PASSWORD ) ) )
                            .transactionIntegration(
                                    new NarayanaTransactionIntegration(
                                            manager, new TransactionSynchronizationRegistryImple() ) ) ) );

    return recoverableDataSource;
  }

  public static void dropTable(String dataSourceURL) throws Exception {
    executeScript(dataSourceURL, QUERY_DROP_TABLE);
  }

  public static void clearTable(String dataSourceURL) throws Exception {
    executeScript(dataSourceURL, QUERY_DELETE);
  }

  public static void insertRow(Connection con, int id, String name) throws Exception {
    PreparedStatement stmt = con.prepareStatement(QUERY_INSERT);
    stmt.setInt(1, id);
    stmt.setString(2, name);
    stmt.executeUpdate();
    stmt.close();
  }

  public static List<Integer> readRows(String dataSourceURL, String dataSourceName) throws Exception {
    List<Integer> list = new ArrayList<Integer>(2);
    Connection connection = DriverManager.getConnection(dataSourceURL, USER, PASSWORD);
    PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT);
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
      int id = rs.getInt(1);
      list.add(id);
      LOGGER.info("read {} from {}", id, dataSourceName);
    }
    rs.close();
    stmt.close();
    connection.close();

    return list;
  }

  private static void executeScript(String dataSourceName, String query) throws Exception {
    Connection connection = DriverManager.getConnection(dataSourceName, USER, PASSWORD);
    Statement stmt = connection.createStatement();
    stmt.execute(query);
    stmt.close();
    connection.close();
  }
}
