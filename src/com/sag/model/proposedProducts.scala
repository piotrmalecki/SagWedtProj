package com.sag.model

import scala.slick.driver.MySQLDriver.simple._

case class proposedProducts(id: Option[Int], ip: String, kategoria: Option[String], idProdukt: Option[Int])
/**
 * Mapped customers table object.
 */
object ProposedProducts2 extends Table[proposedProducts]("proposedProducts") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")
  
  def kategoria = column[String]("kategoria")
  
  def idProdukt = column[Int]("idProdukt")

  def * = id.? ~ ip ~ kategoria.? ~ idProdukt.? <>(proposedProducts, proposedProducts.unapply _)

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
}