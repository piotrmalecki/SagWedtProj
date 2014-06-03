
package com.sag.model

import scala.slick.driver.MySQLDriver.simple._

case class lovedProducts(id: Option[Int], ip: String, kategoria: Option[String], produkt: Option[String],punkty: Option[Int])
/**
 * Mapped customers table object.
 */
object LovedProducts2 extends Table[lovedProducts]("lovedProducts") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")
  
  def kategoria = column[String]("kategoria",O.Nullable)
  
  def produkt = column[String]("produkt",O.Nullable)

  def punkty = column[Int]("punkty",O.Nullable)
  
  def forInsert = ip ~ kategoria.? ~produkt.? ~punkty.? <>({ t => lovedProducts(None, t._1, t._2, t._3, t._4)}, {(u: lovedProducts) => Some((u.ip, u.kategoria, u.produkt, u.punkty))})

  def * = id.? ~ ip ~ kategoria.? ~ produkt.? ~ punkty.? <>(lovedProducts, lovedProducts.unapply _)
  
  
  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
  
    val findByIp = for {
    ip <- Parameters[String]
    c <- this if c.ip is ip
  } yield c
}