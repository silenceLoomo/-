import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time



object Main {
  val target="b"
  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    val text = env.socketTextStream("localhost", 9999)
    /*val stream = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.contains(target)
      }
    }.map {
      ("发现目标："+_)
    }*/

    val counts = text.flatMap{_.toLowerCase.split("\\W+") filter {
      _.contains(target)
    }}
        .map((counts =>words(counts,1)))
        .keyBy("times")
        .timeWindow(Time.seconds(30))
        .aggregate(new sumAggregate)

    //stream.print()
    counts.print()
    env.execute("Window Stream WordCount")
  }

  case class words (word:String , times: Int)
  class sumAggregate extends AggregateFunction[words,(String,Int),Int]{
    override def createAccumulator() = ("",  0)

    override def add(item: words, accumulator: (String, Int)) =
      (item.word, accumulator._2 + 1)

    override def getResult(accumulator:(String, Int)) = (accumulator._2)

    override def merge(a: (String, Int), b: (String, Int)) =
      (a._1 ,a._2 + b._2)
  }

}