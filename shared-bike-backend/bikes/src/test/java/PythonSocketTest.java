import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class PythonSocketTest {

    public static Socket socket=null;

    String csvFilePath = "shared-bike-backend/common/bike_positions.csv"; // 指定CSV文件路径
    String content = csvFilePath;

    @Test
    public void main(){
        System.out.println(getKeyWord(content));
    }

    public static List<String> getKeyWord(String content){
        Socket socket = null;
        String Code_Adress = "127.0.0.1";
        List<String>result=new LinkedList<>();
        try {
            socket = new Socket("127.0.0.1", 8081);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            outputStream.write(content.getBytes());

            int len = inputStream.read(bytes);

            System.out.println(len);
            String str = new String(bytes,0,len);
            String[] list=str.split(",");
            Collections.addAll(result, list);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
