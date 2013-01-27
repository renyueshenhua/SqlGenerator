/**
 *
 */
package parser

/**
 * @author yanlv
 *
 */
class FieldInfo extends ToString {

  var fieldName: String = _
  var fieldType: String = _
  var isNull: Boolean = _
  var fieldMemo: String = _
  var isPrimaryKey: Boolean = _

  def crateFieldSql() = {
    var sql = "";
    sql + this.fieldName + " " + this.fieldType + " " + (if (isNull) " " else "NOT NULL ") + (if (isPrimaryKey) "PRIMARY KEY" else "")
  }
}

object FieldInfo {

  def apply(fieldName: String, fieldType: String, isNull: String, isPrimaryKey: String, fieldMemo: String): FieldInfo = {

    val fieldInfo = new FieldInfo;

    fieldInfo.fieldName = fieldName.trim().toUpperCase()
    fieldInfo.fieldType = fieldType.trim().toUpperCase()

    var isNullFormat = isNull.trim()

    if (isNullFormat == null || isNullFormat == "" || isNullFormat == "否" || isNullFormat == "N") {
      fieldInfo.isNull = false
    } else {
      fieldInfo.isNull = true
    }

    fieldInfo.fieldMemo = fieldMemo.trim()

    if (isPrimaryKey.trim() == "Y" || isPrimaryKey.trim() == "是") {
      fieldInfo.isPrimaryKey = true
    } else {
      fieldInfo.isPrimaryKey = false
    }

    fieldInfo;

  }

  def unapply(str: String): Option[(String, String, String, String, String)] = {

    var strArr = str.split(",")

    if (strArr.length == 5) Some((strArr(0), strArr(1), strArr(2), strArr(3), strArr(4))) else None

  }

  def main(args: Array[String]): Unit = {

    println(FieldInfo("ID", "NUMBER(11)", "是", "seq", "否").crateFieldSql)
  }
}