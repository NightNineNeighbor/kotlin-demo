import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.Socket

object MultipartAlphabetSender {
    @JvmStatic
    fun main(args: Array<String>) {
        val socket = Socket("localhost",8080)
        socket.soTimeout  = 1000
        val out = socket.getOutputStream()
        val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))

        writer.write(firstPacket)
        (1 .. 1000).forEach {
            writer.write(contentPacket)
            //flush()가 호출되어야 요청이 날아간다.
            writer.flush()
        }
        writer.write(lastPacket)
        writer.flush()
        writer.close()
        socket.close()
        println("PROGRAM END")

    }
}

val filePath = "/file"
val fileStreamPath = "/file-stream"

val firstPacket = """POST $fileStreamPath HTTP/1.1
Accept: */*
Host: localhost:8080
Accept-Encoding: gzip, deflate, br
Content-Type: multipart/form-data; boundary=--------------------------578488101370229075502939
Content-Length: 8605646

----------------------------578488101370229075502939
Content-Disposition: form-data; name="file"; filename="abc.txt"
Content-Type: text/plain

abcdefghijklmnopqrstuvwxyz
""".replace("\n","\r\n")

val contentPacket ="""abcdefghijklmnopqrstuvwxyz
abcdefghijklmnopqrstuvwxyz
""".replace("\n","\r\n")

val lastPacket = """abcdefghijklmnopqrstuvwxyz


----------------------------578488101370229075502939--""".replace("\n","\r\n")