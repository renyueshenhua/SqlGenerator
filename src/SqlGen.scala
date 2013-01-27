/**
 *
 */

import java.io._

import parser._
/**
 * 命令行交互工具
 * @author yanlv
 *
 */
object SqlGen {

  def main(args: Array[String]): Unit = {

    println("请输入表配置文件路径或者目录:");

    var tableFilePath = readLine()

    var tableFile = new File(tableFilePath)

    if (tableFile.isFile() && tableFile.exists()) {
      SqlGenerator.createSql(tableFile);
      println("表配置sql生成完成,所在目录:" + tableFile.getParentFile().getPath());
    } else {
      println("文件路径" + tableFilePath + "无效");
    }

    if (tableFile.isDirectory()) {
      tableFile.listFiles().foreach {
        SqlGenerator.createSql(_);
      }
      println("表配置sql生成完成,所在目录:" + tableFile.getPath());
    }

    println("请输入seq配置文件");
    var seqFilePath = readLine()
    var seqFile = new File(seqFilePath)

    if (seqFile.isFile() && seqFile.exists()) {
      SqlGenerator.seqSql(seqFile);
      println("seq配置sql生成完成,所在目录:" + seqFile.getParentFile().getPath());
    } else {
      println("文件路径" + seqFilePath + "无效");
    }

  }

}