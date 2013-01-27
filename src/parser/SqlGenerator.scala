/**
 *
 */
package parser
import scala.io._
import java.io._
import java.io.FileInputStream
import scala.collection.mutable

/**
 * sql语句生成器
 * @author yanlv
 *
 */
object SqlGenerator {

  def main(args: Array[String]): Unit = {

    println(createSql(new File("/Users/yanlv/code/test.txt")));

  }

  def createSql(configFile: File) = {

    var tableConfig = ConfigParser.readTableConfigFile(configFile)
    var tableSqlList = new mutable.ListBuffer[(String, String)]
    val dir = configFile.getParentFile()

    if (tableConfig.needSplit) {
      for (i <- 0 until tableConfig.splitNum) {
        val tableSuffix = "0" * (2 - i.toString().length()) + i.toString()
        val tableName = tableConfig.tableName + tableSuffix

        createTableSqlFile(dir, SqlTemplate.genSingleTableDefintion(tableName, tableConfig), tableName)
      }
    } else {

      createTableSqlFile(dir, SqlTemplate.genSingleTableDefintion(tableConfig.tableName, tableConfig), tableConfig.tableName)

    }

  }

  def seqSql(configFile: File) = {

    var seqConfig = ConfigParser.readSeqConfigFile(configFile)
    val dir = configFile.getParentFile()
    val sqlList = new mutable.ListBuffer[String]

    seqConfig.foreach {
      sqlList + SqlTemplate.genSeqDefintion(_)
    }

    withPrintWriter(new File(dir, "seq.sql")) {

      writer => writer.write(sqlList.mkString("\r\n"))
    }

  }

  def createTableSqlFile(dir: File, createSql: String, fileName: String) = {

    var sqlFileName = new File(dir, fileName + ".sql")

    if (sqlFileName.exists()) sqlFileName.delete()

    sqlFileName.createNewFile()

    withPrintWriter(sqlFileName) {
      writer => writer.println(createSql)
    }
  }

  def withPrintWriter(file: File)(op: PrintWriter => Unit) {
    val writer = new PrintWriter(file, "utf-8")
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }

}
    
  

