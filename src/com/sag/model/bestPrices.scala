package com.sag.model

import scala.slick.driver.MySQLDriver.simple._

case class bestPrices(id: Option[Int], ip: String, cenaOd: Double,
    cenaDo: Double, punkty: Option[Int])
/**
 * Mapped customers table object.
 */
object BestPrices2 extends Table[bestPrices]("bestPrices") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")
  
  def cenaOd = column[Double]("cenaOd")
  
  def cenaDo = column[Double]("cenaDo")
  
  def punkty = column[Int]("punkty",O.Nullable)

  def * = id.? ~ ip ~ cenaOd ~ cenaDo ~ punkty.? <>(bestPrices, bestPrices.unapply _)

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
  
    val findByIp = for {
    ip <- Parameters[String]
    c <- this if c.ip is ip
  } yield c
}