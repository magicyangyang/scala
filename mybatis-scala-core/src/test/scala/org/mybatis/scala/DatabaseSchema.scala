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
package org.mybatis.scala

import org.mybatis.scala.mapping._
import org.mybatis.scala.session.Session

/**
 * This object provides the feature of creating database tables.
 */
object DatabaseSchema {

  /**
   * Prepares tables for testing.
   * @param session the session object to connect to databases.
   */
  def prepare(implicit session: Session): Unit = {
    bind foreach {
      case perform: Perform => perform()
    }
  }

  /**
   * Creates a table containing user data.
   */
  val createUserTable = new Perform {
    def xsql =
      <xsql>
        CREATE TABLE IF NOT EXISTS user(
        id int NOT NULL,
        name VARCHAR(64) NOT NULL,
        email VARCHAR(256) NOT NULL,
        PRIMARY KEY (id)
        )
      </xsql>
  }

  /**
   * Creates a table containing blog data.
   */
  val createBlogTable = new Perform {
    def xsql =
      <xsql>
        CREATE TABLE IF NOT EXISTS blog(
          id int NOT NULL,
          title VARCHAR(128) NOT NULL,
          PRIMARY KEY (id)
        )
      </xsql>
  }

  /**
   * Creates a table containing entries posted to blogs.
   */
  val createEntryTable = new Perform {
    def xsql =
      <xsql>
        CREATE TABLE IF NOT EXISTS entry(
          id int  NOT NULL,
          body BLOB NOT NULL,
          blog_id INTEGER NOT NULL,
          PRIMARY KEY (id)
        )
      </xsql>
  }

  def bind = Seq(createUserTable, createBlogTable, createEntryTable)
}
