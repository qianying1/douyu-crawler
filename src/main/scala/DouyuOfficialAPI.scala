/**
  * Created by lirsun on 7/31/16.
  */
trait DouyuOfficialAPI {

  val root = "http://open.douyucdn.cn/api/RoomApi"

  def getRooms(categoryId:String) = {
    s"$root/live/$categoryId"
  }

  def getSubCategoryForGame() = {
    s"$root/game"
  }

  def getRoom(roomId:String)={
    s"$root/room/$roomId"
  }


}
