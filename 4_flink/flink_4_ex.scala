import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010

object flink_4_ex {
  //kafka参数
  val topic = "mn_buy_ticket_1"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val props = new Properties
    props.put("bootstrap.servers", bootstrapServers)
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val input = env.addSource(new FlinkKafkaConsumer010[String](topic, new SimpleStringSchema(), props))

    // 自定义MySQLWriter类，将数据Sink到mysql
    val sink = new MySQLWriter("jdbc:mysql://localhost:3306/test", "user26", "pass@bingo26")
    input.addSink(sink)


    env.execute("flink_4_ex")
  }

}
