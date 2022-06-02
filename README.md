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
## アプリケーション画面の説明

アプリケーションを起動すると以下のような画面になります．
![launch_screen](https://user-images.githubusercontent.com/48154936/171572753-1228e7ec-539c-468d-9b99-25abbfce56b0.png)

### メニューバー
- Help -> About アプケーションに関する情報を表示
- Settings -> Game Settings ゲーム設定ウィンドウを表示
- View 表に表示する項目の切り替え

### メイン画面の使い方

メイン画面には，中央部に試合内容を示す表，下部に試合実行用コントロールが配置されています．
![main_screen](https://user-images.githubusercontent.com/48154936/171579559-ce550d2b-9f53-4f62-b212-5b278b099b12.png)

- Name　プレイヤー識別名
- Player Class　プレイヤーのじゃんけんアルゴリズム
- Hand　じゃんけんで出した手

また，下部のProceedボタンをクリックするとボタンの右にあるスピナーの値だけ試合を実行します．

#### プレイヤーアルゴリズム

ビルトインのアルゴリズムとしては，以下の5種類のプレイヤークラスが定義されています．

- GUIHumanPlayer ...実行時に手を選択して出します.
- PatternPredictPlayer ...各プレイヤーの傾向を分析し,一番勝てそうな
手を出す.
- RandomPlayer ...ランダムに手を出す.
- RepeatPlayer ...グー・チョキ・パーをこの順で繰り返し出す.
- ComputerPlayer ...ランダムに手を出すが,勝つと次も同じ手を出す.

### プレイヤーアルゴリズムの新規作成

Playerクラスを継承したクラスを定義することで，独自のアルゴリズムを実装したPlayerクラスを作成できます．クラスの読み込み方法については後述します．

```java
import janken.system.core.Player;
import janken.system.manager.HistoryProvider;
import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;
import janken.system.datamodel.RecordBook;
import janken.system.datamodel.Record;

public class CustomPlayer extends Player {
    public CustomPlayer () {
        super (" CustomPlayer ");
    }
    public Hand throwHand ( HistoryProvider hp ) {
        // do something ...
    }
}
```

### 設定画面の使い方

Settings -> Game Settingsまたは 「, + ⌘ (macOS) / Ctrl + , (Linux/Windows)」により，設定画面を開くことができます．

![settings](https://user-images.githubusercontent.com/48154936/171583387-aab0594c-7576-4c8c-879c-4fdd7b8e7584.png)

設定画面では，以下の項目を設定することができます．

- プレイヤー数
- 勝敗決定のルール（default：勝ち負け数，score-base：点数方式）
- プレイヤーアルゴリズムの追加
- 試合実行毎に試合結果を確定するか否か（Proceedボタンを押す度に試合結果が初期化されます）
- 試合結果確定後にリザルトウィンドウを表示するか否か

#### プレイヤーアルゴリズムの追加

アルゴリズムの追加するには，「add Player classes」を選択後，コンパイル済みのクラスファイルを指定します．なお，クラスファイルは本プログラムと同じJDK 17以上のバージョンでコンパイルする必要があります．
