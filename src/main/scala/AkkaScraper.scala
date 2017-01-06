import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import play.api.libs.json.{JsNumber, JsString}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by lirsun on 9/2/16.
  */

object Util {
  implicit def iterExt[A](iter: Iterable[A]) = new {
    def top[B](n: Int, f: A => B)(implicit ord: Ordering[B]): List[A] = {
      def updateSofar (sofar: List [A], el: A): List [A] = {
        //println (el + " - " + sofar)

        if (ord.compare(f(el), f(sofar.head)) > 0)
          (el :: sofar.tail).sortBy (f)
        else sofar
      }

      val (sofar, rest) = iter.splitAt(n)
      (sofar.toList.sortBy (f) /: rest) (updateSofar (_, _)).reverse
    }
  }
}

case class Task(roomId: Int)

case object RefreshTop

case object UpdateTick

case object Parse

//case class Data(roomId:Int, )

class Master(topN:Int, savePath:String) extends Actor{
  import Util._

  import scala.concurrent.ExecutionContext.Implicits.global


  val log = Logging(context.system, this)

  val roomMap = collection.mutable.Map[Int, ActorRef]()

  val tick = context.system.scheduler.schedule(0 millis, 30000 millis, self, UpdateTick)

  override def preStart() = {
    log.info("Master Starting")
  }

  override def receive: Receive = {
    case UpdateTick =>{
      self ! RefreshTop
    }

    case RefreshTop => {
      log.info("Receive Refresh Top")
      val topRoom = SimpleCrawler.getAllRoomStats(1).map{
        json=>
          ((json \ "room_id").asInstanceOf[JsString].value.toInt,
            (json \"online").asInstanceOf[JsNumber].value)
      }.toBlocking.toIterable.top(topN, _._2)

      topRoom.foreach({
        room=>
          val (roomId, viewers) = room
          val roomParser = roomMap.getOrElse(roomId, context.actorOf(Props(new Caller(roomId))))
          roomParser ! Parse
      })
      log.info(topRoom.toString())
    }



  }
}



class Caller(val roomId:Int) extends Actor with DouyuOfficialAPI{
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val log = Logging(context.system, this)
  val roomUrl = getRoom(roomId.toString)
  val http = Http(context.system)

  override def receive: Receive = {
    case Parse => {
      log.info("connecting... {}", roomUrl)
      val responseFuture: Future[HttpResponse] = http.singleRequest(HttpRequest(uri=getRoom(roomId.toString)))
      responseFuture.onComplete {
        case Success(response) => {
          Unmarshal(response).to[String].foreach(println(_))
        }
        case Failure(_) => println("request failed")
      }
    }
  }
}



class Indexer extends Actor{
  override def receive: Receive = ???
}


object App {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Scraper")
    val master = system.actorOf(Props(new Master(5, "test")))
    master ! UpdateTick
  }
}
