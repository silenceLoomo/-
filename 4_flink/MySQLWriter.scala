import java.sql.{Connection, DriverManager}

import com.google.gson.Gson
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}


class MySQLWriter (url: String, user: String, pwd: String) extends RichSinkFunction[String] {
  var conn: Connection = _

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    Class.forName("com.mysql.jdbc.Driver")
    conn = DriverManager.getConnection(url, user, pwd)
    conn.setAutoCommit(false)
  }

  override def invoke(value: String, context: SinkFunction.Context[_]): Unit = {
    val g = new Gson()
    val s = g.fromJson(value, classOf[Message])
    println(value)
    val p = conn.prepareStatement("replace into student(username,buy_time,buy_address,origin,destination) values(?,?,?,?,?)")
    p.setString(1, s.username)
    p.setString(2, s.buy_time)
    p.setString(3, s.buy_address)
    p.setString(4, s.origin)
    p.setString(4, s.destination)
    p.execute()
    conn.commit()
  }

  override def close(): Unit = {
    super.close()
    conn.close()
  }
  case class Message(username:String ,buy_time:String,buy_address:String,origin:String,destination:String)
}
