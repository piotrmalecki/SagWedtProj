package com.sag.model


import scala.slick.driver.MySQLDriver.simple._


/**
 * Profil entity.
 *
 * @param id  unique id
 * @param ip     	IP
 * @param kategoria  
 * @param wyszukiwanie 
 * @param produkt
 * @param czasOgladania
 * @param dataOgladania
 * @param czyKupil
 * @param czyWKarcie
 * @param czyPrzeczytal
 */
case class luczekInfo(id: Option[Int], ip: String, kategoria: Option[String], wyszukiwanie:Option[String],
			produkt: Option[String], czasOgladania: Option[Int], dataOgladania: Option[Long], czyKupil: Option[Boolean],
			czyWKarcie: Option[Boolean], czyPrzeczytal: Option[Boolean])
/**
 * Mapped customers table object.
 */
object LuczekInfo2 extends Table[luczekInfo]("luczekInfo") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")

  def kategoria = column[String]("kategoria")
  
  def wyszukiwanie = column[String]("wyszukiwanie")

  def produkt = column[String]("produkt")
  
  def czasOgladania = column[Int]("czasOgladania")
  
  def dataOgladania = column[Long]("dataOgladania", O.Nullable)
  
  def czyKupil = column[Boolean]("czyKupil")
  
  def czyWKarcie = column[Boolean]("czyWKarcie")

  def czyPrzeczytal = column[Boolean]("czyPrzeczytal")
  
 // def * = id.? ~ firstName ~ lastName ~ birthday.? <>(Customer, Customer.unapply _)

  def * = id.? ~ ip ~ kategoria.? ~ wyszukiwanie.? ~ produkt.? ~ czasOgladania.? ~ dataOgladania.? ~ czyKupil.? ~ czyWKarcie.? ~ czyPrzeczytal.? <>(luczekInfo, luczekInfo.unapply _)

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
}