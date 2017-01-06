import java.text.SimpleDateFormat
import java.util.Date

import com.ning.http.client.{AsyncHttpClient, AsyncHttpClientConfig}
import org.elasticsearch.action.index.IndexRequest
import play.api.libs.json._
import rx.lang.scala.Observable

/**
  * Created by lirsun on 8/3/16.
  */
object SimpleCrawler extends DouyuAndroidAPIs{
  implicit val root = defaultRoot;
  val config= new AsyncHttpClientConfig.Builder().build()
  lazy val asyncHttpClient = new AsyncHttpClient(config);
  lazy val esClient= EsClient
//  def run: Unit = {
//    val parsed = Json.parse(asyncHttpClient.prepareGet(getAllShows(20, 0)).execute().get().getResponseBody)
//    print(Json.prettyPrint(parsed))
//  }

  val formatter = {
    new SimpleDateFormat("yyyyMMdd'T'HHmmssZ")
  }

  def getAllRoomStats(maxPage:Int = 999) = {
    Observable.apply[JsObject]{
      sub=>
        var hasNext=true
        var count = 0
        val step = 20
        sub.onStart()
        while(hasNext){
          val parsed = Json.parse(asyncHttpClient.prepareGet(getAllShows(step, count*step)).execute().get().getResponseBody)
          val data = (parsed \ "data").asInstanceOf[JsArray]
          val error = (parsed \"error").asInstanceOf[JsNumber].value
          val now = new Date()
          hasNext = error==0 && data.value.length == step && count<maxPage
          count+=1
          data.value.foreach{
            v=>
              val vjson = v.as[JsObject]
              val showtime = new Date(((vjson \ "show_time").asInstanceOf[JsString].value).toLong*1000)
              val dateAdded =  vjson + ("date"-> Json.toJson(formatter.format(now))) -"show_time"+ ("show_time"->Json.toJson(formatter.format(showtime)))
              sub.onNext(dateAdded)
          }
        }
        sub.onCompleted()
    }
  }

  def run: Unit ={
    getAllRoomStats().foreach {
      roomJson=>
        val roomStartStr = roomJson.toString()
        println(roomStartStr)
        val req = new IndexRequest("douyu", "room").source(roomStartStr)
        esClient.loader.add(req)
    }
  }

}
