package kamelong.com.JPTI;

import kamelong.com.JPTI.JPTI.JPTIdata;
import kamelong.com.JPTI.OuDia.OuDiaDiaFile;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by kame on 2017/06/26.
 */
public class OuDia2JPTI {
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
            OuDiaDiaFile diaFile=new OuDiaDiaFile(new File(args[0]));

            System.out.println("load Oudia "+(System.currentTimeMillis()-time));
            JPTIdata jpti = new JPTIdata(diaFile);
            System.out.println("make JPTI "+(System.currentTimeMillis()-time));
            jpti.makeJSONdata(new FileWriter(args[1]));
            System.out.println("end "+(System.currentTimeMillis()-time));
        }catch(Exception e){
            e.printStackTrace();
        }



    }
}
