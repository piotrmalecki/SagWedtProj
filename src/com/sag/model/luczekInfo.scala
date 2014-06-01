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
			produkt: Option[String], czasOgladania: Option[Int], dataOgladania: Option[java.util.Date], czyKupil: Option[Boolean],
			czyWKarcie: Option[Boolean], czyPrzeczytal: Option[Boolean])
/**
 * Mapped customers table object.
 */
object LuczekInfo extends Table[luczekInfo]("luczekInfo") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")

  def kategoria = column[String]("kategoria")
  
  def wyszukiwanie = column[String]("wyszukiwanie")

  def produkt = column[String]("produkt")
  
  def czasOgladania = column[Int]("czasOgladania")
  
  def dataOgladania = column[java.util.Date]("dataOgladania", O.Nullable)
  
  def czyKupil = column[Boolean]("czyKupil")
  
  def czyWKarcie = column[Boolean]("czyWKarcie")

  def czyPrzeczytal = column[Boolean]("czyPrzeczytal")
  
 // def * = id.? ~ firstName ~ lastName ~ birthday.? <>(Customer, Customer.unapply _)

  def * = id.? ~ ip ~ kategoria.? ~ wyszukiwanie.? ~ produkt.? ~ czasOgladania.? ~ dataOgladania.? ~ czyKupil.? ~ czyWKarcie.? ~ czyPrzeczytal.? <>(luczekInfo, luczekInfo.unapply _)

  implicit val dateTypeMapper = MappedTypeMapper.base[java.util.Date, java.sql.Date](
  {
    ud => new java.sql.Date(ud.getTime)
  }, {
    sd => new java.util.Date(sd.getTime)
  })

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
}