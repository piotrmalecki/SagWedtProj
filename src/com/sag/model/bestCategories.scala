package com.sag.model

import scala.slick.driver.MySQLDriver.simple._

case class bestCategories(id: Option[Int], ip: String, kategoria: Option[String], idkategoria: Option[Int],
            punkty: Option[Int])
/**
 * Mapped customers table object.
 */
object BestCategories2 extends Table[bestCategories]("bestCategories") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def ip = column[String]("ip")
  
  def kategoria = column[String]("kategoria")
  
  def idkategoria = column[Int]("idkategoria")
  
  def punkty = column[Int]("punkty")

  def * = id.? ~ ip ~ kategoria.? ~ idkategoria.? ~ punkty.? <>(bestCategories, bestCategories.unapply _)

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
}