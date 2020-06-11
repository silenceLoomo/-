//import java.util.stream.Collector
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


object flink_5_ex {

    /*case class target(name:String)

    class MyProcessWindowFunction extends ProcessWindowFunction[target,String,String,Int]{
      override def process(key:String,context: Context,input: Iterable[target],out:Collector[Int]): Unit ={
        val value = 0
        val tg = target.toString()
        val kaf = new KafkaSearch(tg)
        kaf.search()
        out.collect(value)
      }
    }*/

    def main(args: Array[String]) {
    /*val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    val text = env.socketTextStream("localhost", 9999)

      val counts = text.flatMap{_.toLowerCase.split("\\W+")}
        .map(x=>target(x))
        .keyBy(_.name)
        .timeWindow(Time.seconds(30),Time.seconds(10))
        .process(function = new MyProcessWindowFunction()[target, String, Int, TimeWindow]

        )*/
      val line = scala.io.StdIn.readLine
      val kafka = new KafkaSearch(line)
      kafka.search()

    }
}
