import java.math.BigInteger

import com.ning.http.client.{AsyncHttpClient, ListenableFuture, Response}
import java.security.MessageDigest

/**
  * Created by lirsun on 7/31/16.
  * http://430.io/-xie-dou-yu-tv-web-api-some-douyutv-api/
  */

trait DouyuAndroidAPIs {

  def defaultRoot = "http://capi.douyucdn.cn/api/v1"

  def getAllShows (limit:Int, offset:Int)(implicit root:String)={
    s"$root/live?limit=$limit&offset=$offset"
  }

  def getCategories()(implicit root:String)={
    s"$root/getColumnList"
  }

  def getSubCategories(cat:String)(implicit root:String)={
    s"$root/getColumnDetail?shortName=$cat"
  }

  def getCategoryRooms(channel:Int, limit: Int, offset:Int)(implicit root:String) = {
    s"$root/getColumnRoom/$channel?limit=$limit&offset=$offset"
  }

  def getSubCategoryRooms(channel:Int, limit: Int, offset:Int)(implicit root:String) = {
    s"$root/live/$channel?&limit=$limit&offset=$offset"
  }

  def getRoom(roomId:Int)(implicit root:String)={
    val time = System.currentTimeMillis() / 1000L;
    val mid = "?aid=android&cdn=ws&client_sys=android&time="
    val token = md5(s"room/$roomId?$mid${time}1231")
    s"$root/room/$roomId?$mid$time&auth=$token"
  }

  def searchRoom(keyword:String, limit:Int, offset:Int)(implicit root:String)={
    s"$root/searchNew/$keyword/1?limit=$limit&offset=$offset"
  }


  def md5(s: String) = {
    val digest =MessageDigest.getInstance("MD5").digest(s.getBytes)

    val bigInt = new BigInteger(1,digest);
    var hashtext = bigInt.toString(16);
    // Now we need to zero pad it if you actually want the full 32 chars.
    while(hashtext.length() < 32 ){
      hashtext = "0"+hashtext;
    }
    hashtext
  }



}
