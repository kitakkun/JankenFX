# JankenFX

JankenFXは，JavaFXを用いて作成されたじゃんけんシミュレーションGUIアプリケーションです．大学の講義において，「じゃんけんを行うアプリケーションを作成する」機会があり，その際に2, 3週間程度かけて作成したものになります．課題の要件としてはCUIで動作すれば良いというものでしたが，それでは面白味に欠けると感じたため，JavaFXを独学で勉強し実装を行いました．

※調べて理解して実装の繰り返しであったので，ソースコードは綺麗とは言い難いですし，あまり適切でないプログラム実装をしてしまっているかもしれません．

## 実行環境のセットアップ
実行可能要件を以下に示します．
- OpenJDK version 17.0.2以降
- JavaFX SDK 17.0.1以降

JDKの他にJavaFX SDKを別途ダウンロードする必要があります．JavaFX SDKはGluonなどから自分のプラットフォームに合ったものを適宜ダウンロードしてください．

JavaFX SDKのダウンロード→https://gluonhq.com/products/javafx/

## 実行
Jarファイル実行時にはJavaFXのパスを通してやる必要があります．JavaFXのパスを通すには，--module-pathにJavaFX SDKのlibディレクトリのパスを指定したのち，--add-modules=javafx.fxml,javafx.controlsとします．
```
java -jar --module-path=/path/to/javafx/lib --add-modules=javafx.fxml,javafx.controls JankenFX.jar
```
