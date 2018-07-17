/**
 *    Copyright 2011-2015 the original author or authors.
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
package org.mybatis.scala.samples.performance

import org.mybatis.scala.mapping._
import org.mybatis.scala.config._
import org.mybatis.scala.session._
import org.mybatis.scala.mapping.Binding._
import scala.language.postfixOps

class TestBean {
  var id : Int = 0
  var name : String = _
}

object ProfileTest extends App {

  // Simple DDL
  val createTable = new Perform {
    def xsql = 
      <xsql>
        CREATE TABLE IF NOT EXISTS test1 (
            id_ int not null,
            name_ varchar(255),
            primary key (id_)
        )
      </xsql>
  }
  
  // Simple Insert
  val insert = new Insert[TestBean] {
    def xsql = 
      <xsql>
        INSERT INTO test1(name_) VALUES ({"name"?})
      </xsql>
  }
  
  // Simple select
  val select = new SelectList[TestBean] {
    def xsql =
      <xsql>
        SELECT id_ as id, name_ as name
        FROM test1
      </xsql>
  }
  
  // Datasource configuration
  val config = Configuration(
    Environment(
      "default", 
      new JdbcTransactionFactory(), 
      new PooledDataSource(
        "org.hsqldb.jdbcDriver",
        "jdbc:hsqldb:mem:scala",
        "sa",
        ""
      )
    )
  )

  // Add the data access method to the default namespace
  config += createTable
  config += insert
  config += select
  
  // Insert 1.000.000 rows
  val context = config.createPersistenceContext  
  context.transaction { implicit s =>
    createTable()
    for (i <- 1 to 1000000) {
      val bean = new TestBean()
      bean.name = "Bean " + i
      insert(bean)
    }
  }
  
  // Read 1.000.000 rows into memory
  context.readOnly { implicit s =>
    val resultSeq = select()  
  }
  
  // Read 1.000.000 rows one by one
  context.readOnly { implicit s =>
    select.handle[TestBean] { context =>
      val bean = context.getResultObject
    }
  }
  
}
