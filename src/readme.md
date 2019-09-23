# 2019/09/29進捗報告
## OuDia->JPTIへの変換
+ route1(横浜->海老名)
+ route2(横浜->湘南台)
+ route3(武蔵小杉->海老名)

の3ファイルを用意した(駅順は史実通り、時刻は適当)

com.kamelong.OuDiaJPTIconvertor.OuDia2JPTI.kt内のmainを実行すると
SQLite形式のファイルtest.sqlite3が出力される。

####注意
+ 3路線は別々のRouteになる。
+ 3路線が通る駅は同じ駅名だが、stationIDは別々

これらはOuDiaデータ側にstationIDがない以上避けられない課題

## JPTI->OuDiaへの変換①
つくばエクスプレスをJPTIに変換して、output.sqlite3に保存している。
このJPTI内にはserviceはつくエク1つだけである。
com.kamelong.OuDiaJPTIconvertor.JPTI2OuDia.ktのmain関数を実行すると、最初のserviceつまり つくエクがOuDiaファイルとして出力される。

## JPTI->OuDiaへの変換②
OuDia->JPTIの変換で作ったtest.sqlite3を編集して、同一駅は同一IDを持つようにする(tripのstop_id,routeStationのstation_idも忘れずに書き換えること)

com.kamelong.OuDiaJPTIconvertor.JPTI2OuDia2.ktのmain関数を実行すると複数serviceの時刻表を統合したoudファイルが得られる。

統合方法の指定は少し面倒な作業になる。
1. 時刻表に追加したい駅のリスト(stationList)を作る
1. 各サービス毎に、時刻表に追加したい区間を設定する(両端の駅はrouteStationで設定)

この2つの情報が必要となる。

サンプルとして、横浜->二俣川->海老名の時刻表を作製してresult.oudとして保存している。