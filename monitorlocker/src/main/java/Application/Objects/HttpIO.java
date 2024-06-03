package Application.Objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpIO {

    private String method;
    private URL url;
    private String body;
    private boolean setDoInput, setDoOutput;

    public HttpIO(String method, String url, String body, boolean setDoInput, boolean setDoOutput){
        try {
            this.method = method;
            this.url = new URL(url);
            this.body = body;
            this.setDoInput = setDoInput;
            this.setDoOutput = setDoOutput;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public HttpIO(String method, String url, String body){
        this(method, url, body, true, true);
    }
    public HttpIO(String url, String body){
        this("", url, body, true, true);
    }
    public HttpIO(String url){
        this("", url, "", true, true);
    }


    public JsonNode post() throws NullPointerException, Exception{
        
        if(method == "") method = "POST";
        if(method != "POST"){
            throw new Exception("Method is not POST");
        }

        //1.接続するための設定をする
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(setDoInput);
        conn.setDoOutput(setDoOutput);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // 2.接続を確立する
        conn.connect();

        // 3.リクエスとボディに書き込みを行う
        //HttpURLConnectionからOutputStreamを取得し、json文字列を書き込む
        PrintStream ps = new PrintStream(conn.getOutputStream());
        ps.print(body);
        ps.close();

        // 4.レスポンスを受け取る
        if (conn.getResponseCode() != 200) {
            //エラー処理
            throw new Exception("HTTP Response Code: " + conn.getResponseCode());
        }

        //HttpURLConnectionからInputStreamを取得し、読み出す
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        // 5.InputStreamを閉じる
        br.close();

        //結果は呼び出し元に返しておく
        return jsonify( sb.toString() );
    }
    

    public JsonNode get() throws NullPointerException, Exception{
        if(method == "") method = "GET";
        if(method != "GET"){
            throw new Exception("Method is not GET");
        }

        //1.接続するための設定をする
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(setDoInput);
        conn.setDoOutput(setDoOutput);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // 2.接続を確立する
        conn.connect();

        // 3.レスポンスを受け取る
        if (conn.getResponseCode() != 200) {
            //エラー処理
            throw new Exception("HTTP Response Code: " + conn.getResponseCode());
        }

        //HttpURLConnectionからInputStreamを取得し、読み出す
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        // 4.InputStreamを閉じる
        br.close();

        //結果は呼び出し元に返しておく
        return jsonify( sb.toString() );
    }

    private JsonNode jsonify (String str) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(str);
    }
}
