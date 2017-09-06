package com.kamelong.JPTIOuDia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.kamelong.JPTIOuDia.JPTI.JPTI;
import com.kamelong.JPTIOuDia.OuDia.OuDiaFile;

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
            OuDiaFile diaFile=new OuDiaFile(new File(args[0]));

            System.out.println("load Oudia "+(System.currentTimeMillis()-time));
            JPTI jpti = new JPTI(diaFile);
            diaFile=null;
            System.out.println("make JPTI "+(System.currentTimeMillis()-time));

            jpti.makeJSONdata(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));
            System.out.println("end "+(System.currentTimeMillis()-time));

        }catch(Exception e){
            e.printStackTrace();
        }



    }
}
