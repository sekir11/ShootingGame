//import java.awt.*;
//import java.awt.event.*;
//import java.applet.*;
//import java.util.*;
//
///**
// * 超簡易シューティングアプレット
// * @author 最低級黒豚
// */
//
//public class Shooting extends Applet implements Runnable
//{
//    //ゲームモード。0:タイトル 1:ゲーム中 2:クリア 3:ゲームオーバー
//    int mode;
//
//    //クリアするのにかかった時間。
//    int time;
//
//    //敵の残数
//    int enemyNokori;
//
//    //敵円盤の数
//    final int ENEMY_HOR_AMOUNT = 8;
//    final int ENEMY_VER_AMOUNT = 4;
//
//    //敵が１ループにおいて弾丸を撃つ確率
//    final int ENEMY_SHOT = 2;
//
//    //自機を表現するオブジェクト
//    Jibun jibun;
//
//    //敵を表現するオブジェクト
//    Enemy enemy[][];
//
//    //自分の弾丸を表現するオブジェクト
//    MyShell ms;
//
//    //自分の弾丸を表現するオブジェクトの配列
//    ArrayList myshells;
//
//    //画面上で生きている自分の弾丸の数
//    int myShellNum;
//
//    //敵の弾丸を表現するオブジェクト
//    HimShell hs;
//
//    //敵の弾丸を表現するオブジェクトの配列
//    ArrayList himshells;
//
//    //画面上で生きている敵の弾丸の数
//    int himShellNum;
//
//    //画像。自機や敵などのコンストラクタに渡す
//    Image jbn,ebn,mys,his;
//
//    //敵の動き方。true：右に動く false:左に動く
//    boolean eneMoveRight;
//
//    //キー入力は１ループに１回だけにする。 true:許可 false:だめ
//    boolean keyAdmit;
//
//    //initメソッド
//    public void init()
//    {
//        myshells = new ArrayList();
//        himshells = new ArrayList();
//
//        jbn = getImage(getClass().getResource("jibun.gif"));
//        ebn = getImage(getClass().getResource("enban.gif"));
//        mys = getImage(getClass().getResource("myShell.gif"));
//        his = getImage(getClass().getResource("himShell.gif"));
//
//        //自分のインスタンスを生成。aliveを使わない故、最初に１回呼ぶだけ
//        jibun = new Jibun(jbn);
//
//        enemy = new Enemy[ENEMY_HOR_AMOUNT][ENEMY_VER_AMOUNT];
//
//        //初期状態を整える
//        setCondition();
//
//        this.setBackground(new Color(0,0,50));
//
//        enableEvents(AWTEvent.KEY_EVENT_MASK);
//
//        new Thread(this).start();
//
//        repaint();
//    }
//
//    //初期状態を整える
//    void setCondition(){
//        //敵のインスタンスを生成
//        for(int i = 0; i < ENEMY_HOR_AMOUNT; i++)
//            for(int j = 0; j < ENEMY_VER_AMOUNT; j++)
//                enemy[i][j] = new Enemy(ebn, (i*33)+(j*10)+25, (j*20)+30);
//
//        //変数初期化
//        eneMoveRight = true;
//        keyAdmit = true;
//        mode = 0;
//        time = 0;
//        enemyNokori = ENEMY_HOR_AMOUNT * ENEMY_VER_AMOUNT;
//        myShellNum = 0;
//        himShellNum = 0;
//
//        //弾丸を全て削除
//        myshells.clear();
//        himshells.clear();
//    }
//
//    //キーイベント処理
//    public void processKeyEvent(KeyEvent e)
//    {
//        if(e.getID() == KeyEvent.KEY_PRESSED)
//        {
//            if(e.getKeyCode() == KeyEvent.VK_ENTER){
//                //ゲーム中じゃないとき
//                if(mode == 0)
//                    mode = 1;
//                else if(mode == 2 || mode == 3){
//                    mode = 0;
//                    setCondition();
//                }
//            }
//            //ゲーム中でキー入力許可のとき
//            if(keyAdmit && mode == 1){
//                switch(e.getKeyCode())
//                {
//                    case KeyEvent.VK_UP:
//                        jibun.y -= 5;
//                        break;
//                    case KeyEvent.VK_DOWN:
//                        jibun.y += 5;
//                        break;
//                    case KeyEvent.VK_RIGHT:
//                        jibun.x += 5;
//                        break;
//                    case KeyEvent.VK_LEFT:
//                        jibun.x -= 5;
//                        break;
//                    case KeyEvent.VK_SPACE:
//                        myshells.add(new MyShell(mys, jibun.x
//                                + (jibun.sizeX/2) - 1, jibun.y - 15));
//                        myShellNum++;
//                        break;
//                }
//                keyAdmit = false;
//            }
//
//            repaint();
//        }
//    }
//
//    //描画メソッド
//    public void paint(Graphics g)
//    {
//        g.setColor(new Color(255,255,255));
//
//        if(mode == 0){
//            g.drawString("ゲーム始めたきゃエンター押しな。",80,250);
//            g.drawString("ごめんなさい、エンター押してください。",65,270);
//            g.setFont(new Font("Serif",Font.BOLD,30));
//            g.drawString("TENUKI SHOOTING",30,150);
//        }
//        else{
//            //自分を描く
//            g.drawImage(jibun.img, jibun.x, jibun.y, this);
//
//            //敵の群れを描く
//            for(int i = 0; i < ENEMY_HOR_AMOUNT; i++){
//                for(int j = 0; j < ENEMY_VER_AMOUNT; j++){
//                    if(enemy[i][j].alive)
//                        g.drawImage(enemy[i][j].img,
//                                enemy[i][j].x, enemy[i][j].y, this);
//                }
//            }
//
//            //自分の弾丸を描く
//            for(int i = 0; i < myShellNum; i++){
//                ms = (MyShell)myshells.get(i);
//                g.drawImage(ms.img, ms.x, ms.y, this);
//            }
//
//            //敵の弾丸を描く
//            for(int i = 0; i < himShellNum; i++){
//                hs = (HimShell)himshells.get(i);
//                g.drawImage(hs.img, hs.x, hs.y, this);
//            }
//
//            //敵の残数と経過時間を表示
//            g.drawString("敵残数："+ Integer.toString(enemyNokori), 10,15);
//            g.drawString("時間："+ Integer.toString((int)(time/10)),80,15);
//
//            if(mode == 2){
//                g.drawString("全クリです。嬉しいですか？嬉しくないですね。",
//                        50, 170);
//                g.drawString("エンターでタイトルに戻ります。",80,190);
//            }
//            else if(mode == 3){
//                g.drawString("殺られました。ゲームオーバーじゃきに。",60,170);
//                g.drawString("エンターでタイトルに戻るぜ。",90,190);
//            }
//        }
//    }
//
//    //runメソッド
//    public void run()
//    {
//        //無限ループ
//        while(true)
//        {
//            //ゲーム中のときだけ処理
//            if(mode == 1)
//                playingNow();
//
//            try{
//                Thread.sleep(100);
//            }
//            catch(Exception e){}
//        }
//    }
//
//    //ゲーム中の処理
//    void playingNow()
//    {
//        //敵の群れの移動制御
//        if(enemy[0][0].x < 10 || enemy[0][0].x > 50)
//            eneMoveRight = !eneMoveRight;
//
//        //敵の群れが動く
//        if(eneMoveRight == true){
//            for(int i = 0; i < ENEMY_HOR_AMOUNT; i++)
//                for(int j = 0; j < ENEMY_VER_AMOUNT; j++)
//                    enemy[i][j].x++;
//        }else{
//            for(int i = 0; i < ENEMY_HOR_AMOUNT; i++)
//                for(int j = 0; j < ENEMY_VER_AMOUNT; j++)
//                    enemy[i][j].x--;
//        }
//
//        //自分の弾丸が動く
//        for(int i = 0; i < myShellNum; i++){
//            ms = (MyShell)myshells.get(i);
//            ms.y -= 10;
//
//            //弾丸が敵に当たれば、敵＆弾丸死亡
//            for(int j = 0; j < ENEMY_HOR_AMOUNT; j++){
//                for(int k = 0; k < ENEMY_VER_AMOUNT; k++){
//                    if(ms.x + ms.sizeX >= enemy[j][k].x
//                            && ms.x <= (enemy[j][k].x + enemy[j][k].sizeX)
//                            && ms.y >= (enemy[j][k].y - ms.sizeY)
//                            && ms.y <= (enemy[j][k].y + enemy[j][k].sizeY)
//                            && enemy[j][k].alive){
//                        enemy[j][k].alive = false;
//                        ms.alive = false;
//                        enemyNokori--;
//                    }
//                }
//            }
//
//            //敵がいなくなったらクリア
//            if(enemyNokori == 0)
//                mode = 2;
//
//            //弾丸が外に出たら弾丸死亡
//            if(ms.y + ms.sizeY < 0)
//                ms.alive = false;
//
//            //死亡した弾丸を配列から削除
//            if(ms.alive == false){
//                myshells.remove(i);
//                myShellNum--;
//                i--;
//            }
//        }
//
//        //敵が弾丸を撃つ
//        for(int i = 0; i < ENEMY_HOR_AMOUNT; i++){
//            for(int j = 0; j < ENEMY_VER_AMOUNT; j++){
//                if(ENEMY_SHOT > (int)(100 * Math.random())
//                        && enemy[i][j].alive){
//                    himshells.add(new HimShell(his, enemy[i][j].x
//                            + (enemy[i][j].sizeX / 2), enemy[i][j].y + 15));
//                    himShellNum++;
//                }
//            }
//        }
//
//        //敵の弾丸が動く
//        for(int i = 0; i < himShellNum; i++){
//            hs = (HimShell)himshells.get(i);
//            hs.y += 10;
//
//            //弾丸が自分に当たったらゲームオーバー
//            //少しくらい掠っても死なないように判定は甘めに調整
//            if(hs.x >= (jibun.x - hs.sizeX)+4
//                    && hs.x <= (jibun.x + jibun.sizeX)-4
//                    && hs.y >= (jibun.y - hs.sizeY)+10
//                    && hs.y <= (jibun.y + jibun.sizeY-2)){
//                mode = 3;
//            }
//
//            //弾丸が画面外に出たら、配列から削除
//            if(hs.y > 400){
//                himshells.remove(i);
//                himShellNum--;
//                i--;
//            }
//        }
//
//        keyAdmit = true;
//        time++;
//        repaint();
//    }
//}
//
