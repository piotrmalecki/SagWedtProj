
package com.sag.model

import scala.slick.driver.MySQLDriver.simple._

case class lovedProducts(id: Option[Int], ip: String, kategoria: Option[String], idProdukt: Option[Int])
/**
 * Mapped customers table object.
 */
object LovedProducts2 extends Table[lovedProducts]("lovedProducts") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")
  
  def kategoria = column[String]("kategoria")
  
  def idProdukt = column[Int]("idProdukt")

  def * = id.? ~ ip ~ kategoria.? ~ idProdukt.? <>(lovedProducts, lovedProducts.unapply _)

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
}