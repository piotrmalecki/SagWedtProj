package com.sag.model


import scala.slick.driver.MySQLDriver.simple._


case class Notebook(id: Int, screenSize: Float, screenResolution: String, processorBrand: String,
			processorModel: String, ramSize: Int, hardDriveSize: Int, weight: Int,
			brand: String, model: String, price: Float)
/**
 * Mapped customers table object.
 */
object Notebooks extends Table[Notebook]("cat-notebook") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def screenSize = column[Float]("screen-size")

  def screenResolution = column[String]("screen-resolution")
  
  def processorBrand = column[String]("processor-brand")

  def processorModel = column[String]("processor-model")
  
  def ramSize = column[Int]("ram-size")
  
  def hardDriveSize = column[Int]("hard-drive-size")
  
  def weight = column[Int]("weight")
  
  def brand = column[String]("brand")

  def model = column[String]("model")
  
  def price = column[Float]("price")
  
 // def * = id.? ~ firstName ~ lastName ~ birthday.? <>(Customer, Customer.unapply _)

  def * = id ~ screenSize ~ screenResolution ~ processorBrand ~ processorModel ~ ramSize ~ hardDriveSize ~ weight ~ brand ~ model ~price <>(Notebook, Notebook.unapply _)

  val findById = for {
    id <- Parameters[Int]
    c <- this if c.id is id
  } yield c
}