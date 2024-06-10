package Application.Objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NfcController {


    private String correctTagId;
    private boolean hasCorrectTagId;
    private String lastNfcResult;

    public NfcController() {
        System.out.println("NfcController instance created.");
        reset();
    }

    public void reset() {
        correctTagId = "";
        hasCorrectTagId = false;
        lastNfcResult = "";
    }

    public String getTagId()  { return correctTagId; }
    public boolean hasTagId() { return hasCorrectTagId; }

    public boolean readNfc() {
        // TODO NFCリーダーからデータを読み取る
        lastNfcResult = waitForNfc();

        switch (lastNfcResult) { // nfc result
            case "-1": // reader timeout
                System.err.println("NFC: Reader timeout.");
                break;
            case "-2": // incorrect card type | reader not found
                System.err.println("NFC: failed to get card data. check your card type or reader.");
                break;
            case "-3": // terminated
                System.err.println("NFC: terminated.");
                break;
            default:
                System.out.println("NFC: successfully read tag id.");
                correctTagId = lastNfcResult;
                hasCorrectTagId = true;
                return true;
        }
        return false;
    }

	private Process process;

    private String waitForNfc() {
        String data = "";
        try {
            
            // カレントワーキングディレクトリを取得
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            // ワーキングディレクトリ内を再帰的に検索し、目的のファイル名を持つファイルを見つける
            String filePath = Files.walk(currentDirectory)
            .filter(path -> path.getFileName().toString().equals("wait_for_nfc.py"))
            .findFirst().get().toString();
            
            // Pythonスクリプトの呼び出し
            ProcessBuilder pb = new ProcessBuilder("python", filePath);
            process = pb.start();

            // Pythonスクリプトの出力を取得
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                data = line;
                System.out.println("result :"+line); // 処理結果を表示する
            }

            // プロセスの終了を待機
            int exitCode = process.waitFor();
            process = null;
            System.out.println("Pythonスクリプトの終了コード: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void terminate() throws IOException {
    	if(process != null) {
    		// Pythonプロセスへのシグナル送信用のライターを取得
    		OutputStream outputStream = process.getOutputStream();
    		PrintWriter printWriter = new PrintWriter(outputStream);

    		// Pythonプロセスにterminate_process()関数を呼び出すシグナルを送信
    		printWriter.write("terminate\n");
    		printWriter.flush(); // バッファをフラッシュしてデータを送信
    		System.out.println("cancelled");

    	}else
    		System.out.println("terminate error: process is null");
    }

}
