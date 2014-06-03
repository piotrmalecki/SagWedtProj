package com.sag.model


import com.sag.model._
import java.sql._
import scala.Some
import scala.slick.driver.MySQLDriver.simple.Database.threadLocalSession
import scala.slick.driver.MySQLDriver.simple._
import slick.jdbc.meta.MTable
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import akka.event.slf4j.SLF4JLogging


class ProfilDAO  {
//  private val db = Database.forURL(url = "jdbc:mysql://%s:%d/%s".format(dbHost, dbPort, dbName),
//    user = dbUser, password = dbPassword, driver = "com.mysql.jdbc.Driver")
    val db = Database.forURL(url = "jdbc:mysql://%s:%d/%s".format("188.226.184.116", 3306, "sag-wedt"),
    user = "sag-wedt-user", password = "elkatomojamilosc", driver = "com.mysql.jdbc.Driver")

  // create tables if not exist
  db.withSession {
    if (MTable.getTables("luczekInfo").list().isEmpty) {
      LuczekInfo2.ddl.create
    }
    if (MTable.getTables("lovedProducts").list().isEmpty) {
      LovedProducts2.ddl.create
    }
    if (MTable.getTables("bestPrices").list().isEmpty) {
      BestPrices2.ddl.create
    }
    if (MTable.getTables("proposedProducts").list().isEmpty) {
      ProposedProducts2.ddl.create
    }
    if (MTable.getTables("bestBrands").list().isEmpty) {
      BestBrands2.ddl.create
    }
    if (MTable.getTables("bestCategories").list().isEmpty) {
      BestCategories2.ddl.create
    }
    
     if (MTable.getTables("cat-notebook").list().isEmpty) {
      Notebooks.ddl.create
    }
  }
    
     /**
   * Saves profil entity into database.
   *
   * @param profilr entity to
   * @return saved profil entity
   */
  def create(profil: luczekInfo): Either[Failure, luczekInfo] = {
    try {
      val id = db.withSession {
         var punkty = getCount(profil)
         LovedProducts2.filter(_.ip === profil.ip).filter(_.kategoria===profil.kategoria).filter(_.produkt===profil.produkt).firstOption match {
          case Some(profil2: lovedProducts) =>
            updateLovedbyKategory(profil2.ip,profil2.punkty.get +punkty,profil2.kategoria.get, profil2.produkt.get)
          case _ =>
            Left( LovedProducts2.forInsert insert lovedProducts(None,profil.ip, profil.kategoria, profil.produkt, Some(punkty)))
        }
    	  //LovedProducts2.forInsert insert lovedProducts(None,"12.12", Some("monitor"), Some("H23"), Some(0))
    	  //LovedProducts2.insert("12.12", "monitor", "H23", 0)
    	  //val lo = lovedProducts()
    	// var lo: lovedProducts = {lo.ip = "1"}
    	// LovedProducts2 += ()
    	  //INSERT INTO `COCKTAIL` (`ID`,`NAME`,`PRICE_CURRENCY`,`PRICE_AMOUNT`) VALUES (?,?,?,?)v
      }
      Right(profil.copy())
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  def updateLovedbyKategory(mIp: String, wartosc: Int, kategoria:String, produkt:String) = {
    try {
      db.withSession {
         val q = for { l <- LovedProducts2 if l.ip === mIp && l.kategoria === kategoria && l.produkt ===produkt } yield l.punkty
        	q.update(wartosc)
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
      def createLoved(profil: lovedProducts): Either[Failure, lovedProducts] = {
    try {
      
     val id = db.withSession {
         
      }
      Right(profil.copy())
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  
  
   def createBrand(profil: bestBrands): Either[Failure, bestBrands] = {
    try {
      
     val id = db.withSession {
         BestBrands2.filter(_.ip === profil.ip).filter(_.marka===profil.marka).firstOption match {
          case Some(profil2: bestBrands) =>
            updateBrandByIp(profil2.ip,profil2.punkty.get +1,profil2.marka.get)
          case _ =>
            Left(BestBrands2 returning BestBrands2.id insert profil)
        }
      }
      Right(profil.copy())
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   
   def updateBrandByIp(mIp: String, wartosc: Long, value2:String) = {
    try {
      db.withSession {
         val q = for { l <- BestBrands2 if l.ip === mIp && l.marka === value2} yield l.punkty
        		 q.update(wartosc)
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   
    def createCategorie(profil: bestCategories): Either[Failure, bestCategories] = {
    try {
      
     val id = db.withSession {
         BestCategories2.filter(_.ip === profil.ip).filter(_.kategoria===profil.kategoria).firstOption match {
          case Some(profil2: bestCategories) =>
            updateCategoryIp(profil2.ip,profil2.punkty.get +1,profil2.kategoria.get)
          case _ =>
            Left(BestCategories2 returning BestCategories2.id insert profil)
        }
      }
      Right(profil.copy())
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   
   def updateCategoryIp(mIp: String, wartosc: Int, kategoria:String) = {
    try {
      db.withSession {
         val q = for { l <- BestCategories2 if l.ip === mIp && l.kategoria === kategoria } yield l.punkty
        		 q.update(wartosc)
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   
       def createPrice(profil: bestPrices): Either[Failure, bestPrices] = {
    try {
      
     val id = db.withSession {
         BestPrices2.filter(_.ip === profil.ip).filter(_.cenaDo===profil.cenaDo).filter(_.cenaOd===profil.cenaOd).firstOption match {
          case Some(profil2: bestPrices) =>
            updatePriceByIp(profil2.ip,profil2.punkty.get +1, profil2.cenaOd, profil2.cenaDo)
          case _ =>
            Left(BestPrices2 returning BestPrices2.id insert profil)
        }
      }
      Right(profil.copy())
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   
   def updatePriceByIp(mIp: String, wartosc: Int, cenaOd: Double, cenaDo: Double) = {
    try {
      db.withSession {
         val q = for { l <- BestPrices2 if l.ip === mIp && l.cenaOd === cenaOd && l.cenaDo === cenaDo } yield l.punkty
        		 q.update(wartosc)
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   
   
       def createProducts(profil: proposedProducts): Either[Failure, proposedProducts] = {
    try {
      
     val id = db.withSession {
        
        
      }
      Right(profil.copy())
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
  def get(id: Int): Either[Failure, luczekInfo] = {
    try {
      db.withSession {
        LuczekInfo2.findById(id).firstOption match {
          case Some(profil: luczekInfo) =>
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
  
    def getBrand(ip: String): Either[Failure, bestBrands] = {
    try {
      db.withSession {
        BestBrands2.findByIp(ip).firstOption match {
          case Some(profil: bestBrands) =>
            Right(profil)
          case _ =>
            Left(notFoundError(1))
        }
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  
  def search(params: SearchModel): Either[Failure,List[luczekInfo]] = {
   // implicit val typeMapper = Profiles.dateTypeMapper

    try {
      db.withSession {
        val query = for {
          profil <- LuczekInfo2 if {
          Seq(
           params.ip.map(profil.ip is _)
          ).flatten match {
            case Nil => 
              ConstColumn.TRUE
            case seq => 
              seq.reduce(_ && _)
          }
        }
        } yield profil

       Right(query.run.toList.take(5))
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  
    def getAllBrandsSorted(params: SearchModel): Either[Failure,List[bestBrands]] = {
    try {
      db.withSession {
        val query = for {
          profil <- BestBrands2 if {
          Seq(
           params.ip.map(profil.ip is _)
          ).flatten match {
            case Nil => 
              ConstColumn.TRUE
            case seq => 
              seq.reduce(_ && _)
          }
        } 
        } yield profil
       Right(query.run.toList.take(5))
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
      def makePartProfilBrand(listProfils:Either[Failure,List[bestBrands]]): Either[Failure,List[bestBrands]] ={

	  try{
    	Right(listProfils.right.get.sortWith((p1, p2) => 
    	getCount(p1) > getCount(p2)))
	  }
	  	catch{
      case e: SQLException =>
        Left(databaseError(e))
	  		}
  		}
  
  			def getCount(profil: bestBrands): Long = {
	  			var count = profil.punkty.get
	  			count
  			}
        def makePartProfilCategories(listProfils:Either[Failure,List[bestCategories]]): Either[Failure,List[bestCategories]] ={

    try{
    	Right(listProfils.right.get.sortWith((p1, p2) => 
    	getCount(p1) > getCount(p2)))
    }
catch{
      case e: SQLException =>
        Left(databaseError(e))
   }
  }
  def getCount(profil: bestCategories): Long = {
  var count = profil.punkty.get
  count
}
 def getAllCategoriesSorted(params: SearchModel): Either[Failure,List[bestCategories]] = {
    try {
      db.withSession {
        val query = for {
          profil <- BestCategories2 if {
          Seq(
           params.ip.map(profil.ip is _)
          ).flatten match {
            case Nil => 
              ConstColumn.TRUE
            case seq => 
              seq.reduce(_ && _)
          }
        } 
        } yield profil
       Right(query.run.toList.take(5))
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   def makePartProfilPrices(listProfils:Either[Failure,List[bestPrices]]): Either[Failure,List[bestPrices]] ={

    try{
    	Right(listProfils.right.get.sortWith((p1, p2) => 
    	getCount(p1) > getCount(p2)))
    }
catch{
      case e: SQLException =>
        Left(databaseError(e))
   }
  }
  def getCount(profil: bestPrices): Long = {
  var count = profil.punkty.get
  count
}
  
  def getAllPricesSorted(params: SearchModel): Either[Failure,List[bestPrices]] = {
    try {
      db.withSession {
        val query = for {
          profil <- BestPrices2 if {
          Seq(
           params.ip.map(profil.ip is _)
          ).flatten match {
            case Nil => 
              ConstColumn.TRUE
            case seq => 
              seq.reduce(_ && _)
          }
        } 
        } yield profil
       Right(query.run.toList.take(5))
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  
   def getAllNotebooks(): Either[Failure,List[Notebook]] = {
    try {
      db.withSession {
        val query = for {
          profil <- Notebooks 
        } yield profil
       Right(query.run.toList.take(5))
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
  def simpleQuery() : Either[Failure, _]  = {
    try{
      db.withSession{
       Right(println((Q.u + "SELECT * FROM `sag-wedt`.`cat-notebook`").execute))
      }
    }
    catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   def getAllLovedSorted(params: SearchModel): Either[Failure,List[lovedProducts]] = {
    try {
      db.withSession {
        val query = for {
          profil <- LovedProducts2 if {
          Seq(
           params.ip.map(profil.ip is _)
          ).flatten match {
            case Nil => 
              ConstColumn.TRUE
            case seq => 
              seq.reduce(_ && _)
          }
        } 
        } yield profil
       Right(query.run.toList.take(5))
      }
    } catch {
      case e: SQLException =>
        Left(databaseError(e))
    }
  }
   
     def makePartProfilLoved(listProfils:Either[Failure,List[lovedProducts]]): Either[Failure,List[lovedProducts]] ={

    try{
    	Right(listProfils.right.get.sortWith((p1, p2) => 
    	getCount(p1) > getCount(p2)))
    }
catch{
      case e: SQLException =>
        Left(databaseError(e))
   }
  }
  def getCount(profil: lovedProducts): Long = {
  var count = profil.punkty.get
  count
}
  def makeProfil(listProfils:Either[Failure,List[luczekInfo]]): Either[Failure,List[luczekInfo]] ={

try{
  Right(listProfils.right.get.sortWith((p1, p2) => 
  getCount(p1) > getCount(p2)))
}
catch{
      case e: SQLException =>
        Left(databaseError(e))
   }
  }
  
  def getCount(profil: luczekInfo): Int = {
  var count = profil.czasOgladania.get
  if(profil.czyPrzeczytal.get) count = count *4 
  if(profil.czyWKarcie.get) count = count *3 
  if(profil.czyKupil.get) count = count *2 
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
    