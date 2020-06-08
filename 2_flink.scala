import java.util.{Properties, UUID}
import scala.util.control.Breaks._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import scala.collection.mutable.ListBuffer
import scala.util.parsing.json._

object Main {
  /**
    * 输入的主题名称
    */
  val inputTopic = "mn_buy_ticket_demo2"
  /**
    * kafka地址
    */
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def parseJson(json:Option[Any]):Map[String,Any]= json match{
    case Some(map: Map[String, Any]) => map
  }

  def getDest(x:String): String ={
    try{
      val jsonJ = JSON.parseFull(x)
      return parseJson(jsonJ).get("destination").toString
    } catch{
      case ex: Exception => {
        return null
      }
    }
  }
  val wordCount = new scala.collection.mutable.HashMap[String,Int]


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")


    val kafkaConsumer = new FlinkKafkaConsumer010[String](inputTopic,
      new SimpleStringSchema, kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)

    val inputKafkaStream = env.addSource(kafkaConsumer)
    inputKafkaStream.map{ x =>
      val des = getDest(x)
      if (wordCount.contains(des)) {
        wordCount(des) += 1
      } else {
        wordCount(des) = 1
      }
      (wordCount)
    }.map{word =>
      val sorted = collection.mutable.LinkedHashMap(word.toSeq.sortWith(_._2 > _._2):_*)
      var i = 0
      breakable { for (key <- sorted.keySet) {
        i += 1
        if (i >= 6)
          break
        else if(key == null)
          i -= 1
        else println(key.toString.substring(4) +" - "+sorted.get(key).toString.substring(4))
      }
      }
      println("XXXXXXXXXXXXXXXXXXXXXXXX")
    }

    env.execute()
  }


}
