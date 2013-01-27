/**
 *
 */
package parser

import scala.collection.mutable

/**
 * 生成文件表的配置信息
 * @author yanlv
 *
 */
class TableConfig extends ToString{

  var tableName: String = _
  var needSplit: Boolean = _
  var splitNum: Int = _
  var description: String = _

  var fieldInfos: mutable.ListBuffer[FieldInfo] = new mutable.ListBuffer
  var seqInfos: mutable.ListBuffer[SeqInfo] = new mutable.ListBuffer

  def addFieldInfo(fieldInfo: FieldInfo): Unit = {
    this.fieldInfos + fieldInfo
  }

  def addSeqInfo(seqInfo: SeqInfo): Unit = {
    this.seqInfos + seqInfo
  }
}

object TableConfig {

  def apply(tableName: String, needSplit: String, splitNum: String, description: String) = {
    var tableConfig = new TableConfig

    tableConfig.tableName = tableName.trim();
    tableConfig.needSplit = if (needSplit.trim() == "是" || needSplit.trim() == "Y") true else false
    tableConfig.splitNum = splitNum.trim().toInt;
    tableConfig.description = description.trim()

    tableConfig

  }

  def unapply(str: String): Option[(String, String, String, String)] = {
    if (str.startsWith("TableConfig:")) {

      var strArr = str.split(":")(1).split(",")

      if (strArr.length == 4) Some((strArr(0), strArr(1), strArr(2), strArr(3))) else None

    } else None
  }
}