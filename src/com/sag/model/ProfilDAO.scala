package com.sag.model


import com.sag.config.Configuration
import com.sag.model._
import java.sql._
import scala.Some
import scala.slick.driver.MySQLDriver.simple.Database.threadLocalSession
import scala.slick.driver.MySQLDriver.simple._
import slick.jdbc.meta.MTable


class ProfilDAO extends Configuration {
//  private val db = Database.forURL(url = "jdbc:mysql://%s:%d/%s".format(dbHost, dbPort, dbName),
//    user = dbUser, password = dbPassword, driver = "com.mysql.jdbc.Driver")
    private val db = Database.forURL(url = "jdbc:mysql://%s:%d/%s".format("localhost", 3306, "profilsag"),
    user = "root", password = "admin", driver = "com.mysql.jdbc.Driver")

  // create tables if not exist
  db.withSession {
    if (MTable.getTables("profiles").list().isEmpty) {
      Profiles.ddl.create
    }
  }
    
     /**
   * Saves profil entity into database.
   *
   * @param profilr entity to
   * @return saved profil entity
   */
  def create(profil: Profil): Either[Failure, Profil] = {
    try {
      val id = db.withSession {
        Profiles returning Profiles.idProfil insert profil
      }
      Right(profil.copy(idProfil = Some(id)))
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  
  /**
   * Retrieves specific profil from database.
   *
   * @param id id of the profil to retrieve
   * @return profil entity with specified id
   */
  def get(id: Int): Either[Failure, Profil] = {
    try {
      db.withSession {
        Profiles.findById(id).firstOption match {
          case Some(profil: Profil) =>
            Right(profil)
          case _ =>
            Left(notFoundError(id))
        }
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  
  def search(params: SearchModel): Either[Failure,List[Profil]] = {
   // implicit val typeMapper = Profiles.dateTypeMapper

    try {
      db.withSession {
        val query = for {
          profil <- Profiles if {
          Seq(
            params.kategoria.map(profil.kategoria is _),
           params.wyszukiwanie.map(profil.wyszukiwanie is _),
           params.ip.map(profil.ip is _)
          ).flatten match {
            case Nil => 
              ConstColumn.TRUE
            case seq => 
              seq.reduce(_ && _)
          }
        }
        } yield profil

       Right(query.run.toList)
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  
  def makeProfil(listProfils:Either[Failure,List[Profil]]): Either[Failure,List[Profil]] ={
    
   // var newList = List[Profil]
	// var ll = List[Map[Int,Profil]]()
   //listProfils.foreach( item => {
     
   //  var count = item.czasOgladania.get
   //  if(item.czyKupil.get) count = count *4 ;
  //   if(item.czyPrzeczytal.get) count = count *3 ;
  ///   if(item.czyWKarcie.get) count = count *2 ;
  //   ll ::= Map (count -> item) 
  // } )

try{
  Right(listProfils.right.get.sortWith((p1, p2) => 
  getCount(p1) > getCount(p2)))
}
catch{
      case e: SQLException =>
        Left(databaseError(e))
   }
  }
  
  def getCount(profil: Profil): Int = {
  var count = profil.czasOgladania.get
  if(profil.czyKupil.get) count = count *4 
  if(profil.czyPrzeczytal.get) count = count *3 
  if(profil.czyWKarcie.get) count = count *2 
  count
}
   /**
   * Produce database error description.
   *
   * @param e SQL Exception
   * @return database error description
   */
  protected def databaseError(e: SQLException) =
    Failure("%d: %s".format(e.getErrorCode, e.getMessage), FailureType.DatabaseFailure)

  /**
   * Produce customer not found error description.
   *
   * @param customerId id of the customer
   * @return not found error description
   */
  protected def notFoundError(customerId: Long) =
    Failure("Customer with id=%d does not exist".format(customerId), FailureType.NotFound)
}
    