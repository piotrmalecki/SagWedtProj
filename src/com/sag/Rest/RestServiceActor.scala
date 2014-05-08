package com.sag.Rest

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import com.sag.model.ProfilDAO
import com.sag.model._
import java.text.{ParseException, SimpleDateFormat}
import java.util.Date
import net.liftweb.json.Serialization._
import net.liftweb.json.{DateFormat, Formats}
import scala.Some
import spray.http._
import spray.httpx.unmarshalling._
import spray.routing._

/**
 * REST Service actor.
 */
class RestServiceActor extends Actor with RestService {

  implicit def actorRefFactory = context

  def receive = runRoute(rest)
}

/**
 * REST Service
 */
trait RestService extends HttpService with SLF4JLogging {

  val profilService = new ProfilDAO

  implicit val executionContext = actorRefFactory.dispatcher

  implicit val liftJsonFormats = new Formats {
    val dateFormat = new DateFormat {
      val sdf = new SimpleDateFormat("yyyy-MM-dd")

      def parse(s: String): Option[Date] = try {
        Some(sdf.parse(s))
      } catch {
        case e: Exception => None
      }

      def format(d: Date): String = sdf.format(d)
    }
  }
  
  val rest = respondWithMediaType(MediaTypes.`application/json`) {
    path("profil") {
      post {
        entity(Unmarshaller(MediaTypes.`application/json`) {
          case httpEntity: HttpEntity =>
            read[Profil](httpEntity.asString(HttpCharsets.`UTF-8`))
        }) {
          profil: Profil =>
            ctx: RequestContext =>
              handleRequest(ctx, StatusCodes.Created) {
                log.debug("Creating profil: %s".format(profil))
                profilService.create(profil)
              }
        }
      } ~
        get {
          parameters('ip.as[String] ?, 'kategoria.as[String] ?, 'wyszukiwanie.as[String] ?).as(SearchModel) {
            searchParameters: SearchModel => {
              ctx: RequestContext =>
                handleRequest(ctx) {
                  log.debug("Searching for profils with parameters: %s".format(searchParameters))
                  profilService.search(searchParameters)
                }
            }
          }
        }
    } ~
      path("profil" / IntNumber) {
        profilId =>
            get {
              ctx: RequestContext =>
                handleRequest(ctx) {
                  log.debug("Retrieving profil with id %d".format(profilId))
                  profilService.get(profilId)
                }
            }
      }
  }

  /**
   * Handles an incoming request and create valid response for it.
   *
   * @param ctx         request context
   * @param successCode HTTP Status code for success
   * @param action      action to perform
   */
  protected def handleRequest(ctx: RequestContext, successCode: StatusCode = StatusCodes.OK)(action: => Either[Failure, _]) {
    action match {
      case Right(result: Object) =>
        ctx.complete(successCode, write(result))
      case Left(error: Failure) =>
        ctx.complete(error.getStatusCode, net.liftweb.json.Serialization.write(Map("error" -> error.message)))
      case _ =>
        ctx.complete(StatusCodes.InternalServerError)
    }
  }
}