/**
 *
 */
package parser

import java.io._
import scala.io._
import scala.collection.mutable

/**
 * 用来进行配置文件的文件读取
 * @author yanlv
 *
 */
object ConfigParser {

  def main(args: Array[String]): Unit = {
    println(readTableConfigFile(new File("/Users/yanlv/code/test.txt")));
    println(readSeqConfigFile(new File("/Users/yanlv/code/seqtest.txt")));
  }

  def readTableConfigFile(file: File): TableConfig = {

    var tableConfig = new TableConfig

    Source.fromFile(file, "utf-8").getLines().foreach {
      line =>
        line match {
          case TableConfig(tableName, needSplit, splitNum, description) =>
            tableConfig = TableConfig(tableName, needSplit, splitNum, description)
          case FieldInfo(fieldName, fieldType, isNull, fieldMemo, isPrimaryKey) =>
            tableConfig.addFieldInfo(FieldInfo(fieldName, fieldType, isNull, fieldMemo, isPrimaryKey))
          case _ => throw new IllegalArgumentException("文件配置错误:" + line)
        }
    }

    tableConfig

  }

  def readSeqConfigFile(file: File): mutable.ListBuffer[SeqInfo] = {

    var tableConfig = new TableConfig

    var seqInfos: mutable.ListBuffer[SeqInfo] = new mutable.ListBuffer

    Source.fromFile(file, "utf-8").getLines().foreach {
      line =>
        line match {
          case SeqInfo(seqName, minValue, maxValue, start, increment) =>
            seqInfos + SeqInfo(seqName, minValue, maxValue, start, increment)
          case _ => throw new IllegalArgumentException("文件配置错误:" + line)
        }
    }

    seqInfos

  }

}

