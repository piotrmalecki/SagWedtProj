package com.sag.model

import scala.slick.driver.MySQLDriver.simple._

case class bestBrands(id: Option[Int], ip: String, marka: Option[String], idMarka: Option[Long],
            punkty: Option[Long])
/**
 * Mapped customers table object.
 */
object BestBrands2 extends Table[bestBrands]("bestBrands") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")
  
  def marka = column[String]("marka",O.Nullable)
  
  def idMarka = column[Long]("idMarka", O.Nullable)
  
  def punkty = column[Long]("punkty",O.Nullable)

  def * = id.? ~ ip ~ marka.? ~ idMarka.? ~ punkty.? <>(bestBrands, bestBrands.unapply _)

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
  
    val findByIp = for {
    ip <- Parameters[String];
  
    c <- this if c.ip is ip 
  } yield c
  
  val findByMarka = for {
    marka <- Parameters[String]
    
    c <- this if c.marka is marka 
  } yield c
}