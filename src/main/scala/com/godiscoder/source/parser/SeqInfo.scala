/**
 *
 */
package com.godiscoder.source.parser



/**
 * seq配置信息
 * @author yanlv
 *
 */
class SeqInfo extends ToString {

  var seqName: String = _
  var minValue: Int = 1
  var maxValue: BigDecimal = BigDecimal("9999999999999999999999")
  var start: Int = 1
  var increment: Int = 1

}

object SeqInfo {

  val defaultMinValue = "1"
  val defaultMaxValue = BigDecimal("9999999999999999999999").toString()
  val defaultStart = "1"
  val defaultIncrement = "1"

  def apply(seqName: String, minvalue: String, maxvalue: String, start: String, increment: String) = {

    var seqInfo = new SeqInfo

    seqInfo.seqName = seqName.trim()
    seqInfo.minValue = if (minvalue.trim() == "" || minvalue.trim().toInt <= 1) 1 else minvalue.trim().toInt
    seqInfo.maxValue = if (maxvalue.trim() == "" || BigDecimal(maxvalue.trim()) <= seqInfo.maxValue) BigDecimal(maxvalue.trim()) else seqInfo.maxValue
    seqInfo.start = if (start.trim() == "") 1 else start.trim().toInt
    seqInfo.increment = if (increment.trim() == "") 1 else increment.trim().toInt

    seqInfo

  }

  def unapply(str: String): Option[(String, String, String, String, String)] = {

    var strArr = str.split(",")

    if (strArr.length == 1) Some(strArr(0), defaultMinValue, defaultMaxValue, defaultStart, defaultIncrement)
    else if (strArr.length == 2) Some(strArr(0), strArr(1), defaultMaxValue, defaultStart, defaultIncrement)
    else if (strArr.length == 3) Some(strArr(0), strArr(1), strArr(2), defaultStart, defaultIncrement)
    else if (strArr.length == 4) Some(strArr(0), strArr(1), strArr(2), strArr(3), defaultIncrement)
    else if (strArr.length == 5) Some(strArr(0), strArr(1), strArr(2), strArr(3), strArr(4))
    else None

  }

}

