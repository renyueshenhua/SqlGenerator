/**
 *
 */
package parser

/**
 * 这里面反射对象的内部
 * @author yanlv
 *
 */
trait ToString {

  override def toString(): String = {

    return this.getClass().getDeclaredFields().foldLeft("") {

      (tempString, field) => field.setAccessible(true); tempString + field.getName() + ":" + field.get(this) + "\r\n"

    }

  }

}