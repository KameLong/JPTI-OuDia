package kamelong.com.JPTI;

import kamelong.com.JPTI.JPTI.JPTIdata;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Created by kame on 2017/07/07.
 */
public class JPTI2SujiTaro {
    /**
     *
     * @param args コマンド引数
     *             第一引数 入力OuDiaファイルパス
     *             第二引数 出力JPTIファイルパス
     */
    public static void main(String[] args){
        System.out.println("このプログラムはカメロングが作成した、OuDiaファイルをJPTIフォーマットに変換するコマンドラインツールです。");
        System.out.println("使い方：http://kamelong.com/JPTI/");
        if(args.length!=2){
            System.out.println("引数が足りません。引数：[input OuDia file] [output JPTI file] ");
            return;
        }

        try {
            long time=System.currentTimeMillis();
            System.out.println("start "+(System.currentTimeMillis()-time));
            JSONObject json=new JSONObject(Files.lines(Paths.get(args[0]), Charset.forName("UTF-8"))
                    .collect(Collectors.joining(System.getProperty("line.separator"))));
            JPTIdata jpti=new JPTIdata(json);
            System.out.println("load JPTI "+(System.currentTimeMillis()-time));
            System.out.println("make SujiTaro "+(System.currentTimeMillis()-time));
            System.out.println("end "+(System.currentTimeMillis()-time));
        }catch(Exception e){
            e.printStackTrace();
        }



    }
}
