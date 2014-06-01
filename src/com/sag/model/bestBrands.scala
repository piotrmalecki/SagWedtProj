package com.sag.model

import scala.slick.driver.MySQLDriver.simple._

case class bestBrands(id: Option[Int], ip: String, marka: Option[String], idMarka: Option[Int],
            punkty: Option[Int])
/**
 * Mapped customers table object.
 */
object BestBrands2 extends Table[bestBrands]("bestBrands") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")
  
  def marka = column[String]("kategoria")
  
  def idMarka = column[Int]("idkategoria")
  
  def punkty = column[Int]("punkty")

  def * = id.? ~ ip ~ marka.? ~ idMarka.? ~ punkty.? <>(bestBrands, bestBrands.unapply _)

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
}