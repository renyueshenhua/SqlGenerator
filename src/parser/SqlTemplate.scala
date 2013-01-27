/**
 *
 */
package parser

import scala.io._

/**
 * sql模板生成器
 * @author yanlv
 *
 */
object SqlTemplate {

  def genFieldBasicDefintion(fieldInfo: FieldInfo): String = {

    var sqlTemplate = loadSqlTemplate("field_basic_defintion")

    for {
      field <- fieldInfo.getClass.getDeclaredFields()
      fieldName = field.getName()
      if sqlTemplate.indexOf("{" + fieldName + "}") != -1
    } {
      field.setAccessible(true)

      val replaceValue = fieldName match {
        case "isNull" => if (field.get(fieldInfo).toString() == "true") "" else "NOT NULL"
        case "isPrimaryKey" => if (field.get(fieldInfo).toString() == "true") "PRIMARY_KEY" else ""
        case _ => field.get(fieldInfo).toString()
      }
      sqlTemplate = sqlTemplate.replace("{" + fieldName + "}", replaceValue)
    }

    sqlTemplate
  }

  def genFieldMemoDefintion(tableName: String, fieldInfo: FieldInfo): String = {

    var sqlTemplate = loadSqlTemplate("field_memo_defintion")

    for {
      field <- fieldInfo.getClass.getDeclaredFields()
      fieldName = field.getName()
      if sqlTemplate.indexOf("{" + fieldName + "}") != -1
    } {
      field.setAccessible(true)
      sqlTemplate = sqlTemplate.replace("{" + fieldName + "}", field.get(fieldInfo).toString())
    }

    sqlTemplate = sqlTemplate.replace("{tableName}", tableName)

    sqlTemplate
  }

  def genSeqDefintion(seqInfo: SeqInfo): String = {

    var sqlTemplate = loadSqlTemplate("seq_defintion")

    for {
      field <- seqInfo.getClass.getDeclaredFields()
      fieldName = field.getName()
      if sqlTemplate.indexOf("{" + fieldName + "}") != -1
    } {
      field.setAccessible(true)

      val replaceValue = field.get(seqInfo).toString()

      sqlTemplate = sqlTemplate.replace("{" + fieldName + "}", replaceValue)
    }

    sqlTemplate
  }

  def genSingleTableDefintion(tableName: String, tableConfig: TableConfig) = {

    val fieldInfos =
      for {
        fieldInfo <- tableConfig.fieldInfos
      } yield {
        genFieldBasicDefintion(fieldInfo)
      }

    val seqInfos =
      for {
        seqInfo <- tableConfig.seqInfos
      } yield {
        genSeqDefintion(seqInfo)
      }

    val fieldMemos =
      for {
        fieldInfo <- tableConfig.fieldInfos
      } yield {
        genFieldMemoDefintion(tableName, fieldInfo)
      }

    var sqlTemplate = loadSqlTemplate("create_table")

    sqlTemplate = sqlTemplate.replace("{description}", tableConfig.description)
    sqlTemplate = sqlTemplate.replace("{tableName}", tableName)
    sqlTemplate = sqlTemplate.replace("{field_memo_defintion}", fieldMemos.mkString("", "\r\n", ""))
    sqlTemplate = sqlTemplate.replace("{field_basic_defintion}", fieldInfos.mkString(" " * 4, ",\r\n" + " " * 4, ""))
    //sqlTemplate = sqlTemplate.replace("{seq_defintion}", seqInfos.mkString("", "\r\n", ""))
    sqlTemplate = sqlTemplate.replace("{constraint_defintion}", "")

    sqlTemplate

  }

  def main(args: Array[String]): Unit = {

    var tableConfig = TableConfig("FUND_TEST", "是", "100", "表的配置信息")
    tableConfig.addFieldInfo(FieldInfo("ID", "NUMBER(11)", "否", "seq配置信息", "是"))
    tableConfig.addFieldInfo(FieldInfo("PRE_ID", "VARCHAR(12)", "是", "配置信息", "否"))

    //tableConfig.addSeqInfo(SeqInfo("SEQ_NEXT_1", "1", "9999999", "100", "1"))
    //tableConfig.addSeqInfo(SeqInfo("SEQ_NEXT_2", "2", "9999999", "1", "100"))

    println(genSingleTableDefintion(tableConfig.tableName, tableConfig))

    // println(loadSqlTemplate("create_table"))

  }

  def loadSqlTemplate(templateType: String): String = {
    try {

      var classLoader = this.getClass().getClassLoader()
      var sqlfile = "template/" + templateType + ".sql"
      return Source.fromFile(classLoader.getResource(sqlfile).getFile(), "utf-8").getLines().mkString("\r\n")

    } catch {
      case ex: Exception => throw new RuntimeException("模板配置" + templateType + "对应的信息不存在", ex)
    }

    throw new IllegalArgumentException(templateType + "模板对应的配置信息不存在");

  }

}