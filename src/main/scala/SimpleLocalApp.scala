import java.util.Date

/**
  * Created by lirsun on 8/5/16.
  */
object SimpleLocalApp extends App {
  println(s"Start ${new Date}")
  while(true){
    println("Running...")
    SimpleCrawler.run
    println("Sleeping...")
    Thread.sleep(300000)
  }
  sys.exit(0)
}
