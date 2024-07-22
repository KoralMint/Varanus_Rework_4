package Application.Objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class NfcController {


    private String correctTagId;
    private String status;
    private short statusCode;

    public NfcController() {
        System.out.println("NfcController instance created.");
        reset();
    }

    public void reset() {
        correctTagId = "";
        status = "";
        statusCode = 0;
    }

    public String getTagId()  { return correctTagId; }
    public boolean hasTagId() { return !correctTagId.equals(""); }
    public String getStatus() { return status; }
    public short getStatusCode() { return statusCode; }

    public boolean readNfc() {
        String resultStatus = waitForNfc();

        switch (resultStatus) { // nfc result
            case "-1": // reader timeout
                status = "Reader timeout.";
                statusCode = -1;
                break;
            case "-2": // incorrect card type | reader not found
                status = "Incorrect card type or reader not found.";
                statusCode = -2;
                break;
            case "-3": // terminated
                status = "Terminated.";
                statusCode = -3;
                break;
            default:
                status = "successfully read tag id.";
                statusCode = 0;
                correctTagId = resultStatus;
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
            Optional<Path> filePathOpt = Files.walk(currentDirectory)
                .filter(path -> path.getFileName().toString().equals("wait_for_nfc.py"))
                .findFirst();

            if (filePathOpt.isPresent()) {
                String filePath = filePathOpt.get().toString();
                System.out.println("ファイルのパス: " + filePath);

                // Pythonスクリプトの呼び出し
                ProcessBuilder pb = new ProcessBuilder("python3", filePath);
                process = pb.start();
                // プロセスの出力を処理するコードをここに追加できます

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
            
            } else {
                System.out.println("指定されたファイルは見つかりませんでした。");
                data = "-2";
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            data = "-2";
        }

        return data;
    }

    public void terminate(){
        System.err.println("* terminate function is abolished.");
    }

}
