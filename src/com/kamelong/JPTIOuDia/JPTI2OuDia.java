package com.kamelong.JPTIOuDia;

import com.kamelong.JPTIOuDia.JPTI.JPTI;
import com.kamelong.JPTIOuDia.OuDia.OuDiaFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by kame on 2017/09/05.
 */
public class JPTI2OuDia {
    /**
     * @param args コマンド引数
     *             第一引数 入力OuDiaファイルパス
     *             第二引数 出力JPTIファイルパス
     */
    public static void main(String[] args) {
        System.out.println("このプログラムはカメロングが作成した、OuDiaファイルをJPTIフォーマットに変換するコマンドラインツールです。");
        System.out.println("使い方：http://kamelong.com/JPTI/");
        if (args.length != 2) {
            System.out.println("引数が足りません。引数：[input OuDia file] [output JPTI file] ");
            return;
        }

        try {
            long time = System.currentTimeMillis();
            System.out.println("start " + (System.currentTimeMillis() - time));
            JPTI jpti = new JPTI(new File(args[0]));

            System.out.println("load Oudia " + (System.currentTimeMillis() - time));
            OuDiaFile diaFile = new OuDiaFile(jpti, jpti.getService(0));
            System.out.println("make JPTI " + (System.currentTimeMillis() - time));
            diaFile.makeOuDiaText(new File(args[1]),true);
            System.out.println("end " + (System.currentTimeMillis() - time));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
