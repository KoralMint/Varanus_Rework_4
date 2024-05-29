package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.text.SimpleDateFormat;

public class tc_StatesUser implements Initializable{

	@FXML private AnchorPane user_element;
    @FXML private Text user_id;
    @FXML private Text user_name;
    @FXML private Text datetime;

/*functions-----------------------------------------*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
	}
    
    public void setUserdata( int _user_id, String _user_name, String _datetime ) {
    	//user_id
    	DecimalFormat idf = new DecimalFormat("0000000");
    	user_id.setText(idf.format(_user_id));

    	//user_name ellipsis
    	_user_name = EllipseStr(_user_name,18);
    		
    	user_name.setText(_user_name);
    	
    	//date time
    	datetime.setText( convertDateTimeFormat(_datetime) );
    }
    
    //user_name
    public static String EllipseStr(String inputString, int maxCount) {
    	float count = 0;
        float unitchars[] = new float[inputString.length()];
        String ellipsed = "";
        
        //count
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            // 文字が半角(1単位)か全角(2単位)かを判定
            if(isHalfWidth(c) ) {
            	unitchars[i] = 1.3f;
            	count += 1.3f;
            }else {
            	unitchars[i] = 2;
            	count += 2;
            }
        }
        
        //ellipse
        boolean isOver = false;
        if(count > maxCount) {
        	count = maxCount-2;
        	isOver = true;
        }
        float count2 = 0;
        for (int j=0; count2<count; j++) {
        	ellipsed += inputString.charAt(j);
        	count2+=unitchars[j];
        }
        if(isOver) ellipsed+="...";
        return ellipsed;
    }
    public static boolean isHalfWidth(char c) {
        // 文字が半角か全角かを判定
        return !(c >= '\uff61' && c <= '\uff9f') && (c <= '\u007e' ||
        		(c >= '\uff10' && c <= '\uff5a') || (c >= '\uff21' && c <= '\uff3a'));
    }
    
    //date time
    public static String convertDateTimeFormat(String inputDateTime) {
        // 入力フォーマット
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        // 出力フォーマット
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd HH:mm");

        try {
            // 文字列をDate型に変換
            Date date = inputFormat.parse(inputDateTime);

            // 新しいフォーマットで文字列に変換
            String outputDateTime = outputFormat.format(date);

            return outputDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return "<FormatError>";
        }
    }
}
