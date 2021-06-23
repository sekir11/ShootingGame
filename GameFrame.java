import java.awt.*;          //Frame
import java.awt.event.*;    //WindowListener,MouseLisnter
import java.util.ArrayList;

//1画面のちらつきをなくすAnimationFrameを継承する
public class GameFrame extends AnimationFrame implements WindowListener,KeyListener,Runnable{

    //ゲームモード。0:タイトル 1:ゲーム中 2:クリア 3:ゲームオーバー
    int mode;

    int stage = 1;
    int maxStage = 3;

    //敵円盤の数
    final int ENEMY_HOR_AMOUNT = 8;
    final int ENEMY_VER_AMOUNT = 4;

    //敵が１ループにおいて弾丸を撃つ確率
    final int ENEMY_SHOT = 1;

    //自機を表現するオブジェクト
    Jibun jibun;

    //敵を表現するオブジェクト
    Enemy enemy[][];

    //自分の弾丸を表現するオブジェクト
    MyShell ms;

    //自分の弾丸を表現するオブジェクトの配列
    ArrayList myshells;

    //画面上で生きている自分の弾丸の数
    int myShellNum;

    //敵の弾丸を表現するオブジェクト
    HimShell hs;

    //敵の弾丸を表現するオブジェクトの配列
    ArrayList himshells;

    //画面上で生きている敵の弾丸の数
    int himShellNum;

    //敵の残数
    int enemyNokori;

    //キー入力は１ループに１回だけにする。 true:許可 false:だめ
    boolean keyAdmit;

    //敵の動き方。true：右に動く false:左に動く
    boolean eneMoveRight;

    //画像。自機や敵などのコンストラクタに渡す
    Image jbn,ebn,mys,his;

    //ライフ
    int life;

    //他のクラスから参照できるよう定数で宣言
    public static final int FRAME_W = 800;      //フレームの横幅
    public static final int FRAME_H = 600;      //フレームの縦幅

    private Thread th = null;   //Threadオブジェクトを宣言

    public GameFrame(String title){
        //2スーパークラスにタイトルと画面の幅、高さを渡す
        super(title,FRAME_W,FRAME_H);

        addKeyListener(this);

        setSize(FRAME_W,FRAME_H);
        setVisible(true);

        myshells = new ArrayList();
        himshells = new ArrayList();

        jbn = getToolkit().getImage("img/player.png");
        ebn = getToolkit().getImage("img/enemy1.png");
        mys = getToolkit().getImage("img/bullet.png");
        his = getToolkit().getImage("img/bullet.png");

        //自分のインスタンスを生成。aliveを使わない故、最初に１回呼ぶだけ
        jibun = new Jibun(jbn);

        enemy = new Enemy[ENEMY_HOR_AMOUNT][ENEMY_VER_AMOUNT];

        //初期状態を整える
        setCondition();

        th = new Thread(this);  //thを生成する
        th.start();             //run()を実行する
    }

    //背景ちらつき防止
    public void update(Graphics g){
        paint(g);
    }

    //初期状態を整える
    void setCondition(){
        //敵のインスタンスを生成
        for(int i = 0; i < ENEMY_HOR_AMOUNT; i++)
            for(int j = 0; j < ENEMY_VER_AMOUNT; j++)
                enemy[i][j] = new Enemy(ebn, (i*60)+(j*10)+25, (j*40)+80);

        //変数初期化
        eneMoveRight = true;
        keyAdmit = true;
        mode = 0;
        enemyNokori = ENEMY_HOR_AMOUNT * ENEMY_VER_AMOUNT;
        myShellNum = 0;
        himShellNum = 0;
        life = 5;

        //弾丸を全て削除
        myshells.clear();
        himshells.clear();
    }

    //3paintの代わりにaniPaintを使う
    public void aniPaint(Graphics g){
        g.setColor(Color.WHITE);

        if(mode == 0){
            g.drawString("EnterKey を押してゲームをスタートしてください。",80,250);
            g.setFont(new Font("Serif",Font.BOLD,30));
        } else {
            //自分を描く
            g.drawImage(jibun.getImage(), jibun.getX(), jibun.getY(), 50, 50, this);

            //敵の群れを描く
            for(int i = 0; i < ENEMY_HOR_AMOUNT; i++){
                for(int j = 0; j < ENEMY_VER_AMOUNT; j++){
                    if(enemy[i][j].getAlive())
                        g.drawImage(enemy[i][j].getImage(),
                                enemy[i][j].getX(), enemy[i][j].getY(), 50, 50, this);
                }
            }

            //自分の弾丸を描く
            for(int i = 0; i < myShellNum; i++){
                ms = (MyShell)myshells.get(i);
                g.drawImage(ms.getImage(), ms.getX(), ms.getY(), 10, 10, this);
            }

            //敵の弾丸を描く
            for(int i = 0; i < himShellNum; i++){
                hs = (HimShell)himshells.get(i);
                g.drawImage(hs.getImage(), hs.getX(), hs.getY(), 10, 10,  this);
            }

            //敵の残数を表示
            g.drawString("ステージ："+ stage + " / " + maxStage, 10,60);
            g.drawString("敵残数："+ enemyNokori, 230,60);
            g.drawString("残りライフ："+ life, 400,60);

            if(mode == 2){
                g.drawString("ゲームクリアです！", 50, 170);
                g.drawString("Enter で次のステージへ。",80,210);
            }
            if(mode == 3){
                g.drawString("ゲームクリアです！", 50, 170);
                g.drawString("Enter でタイトルに戻ります。",80,210);
            }
            else if(mode == 4){
                g.drawString("ゲームオーバーです。",60,170);
                g.drawString("Enter でタイトルに戻ります。",90,210);
            }
        }

    }


    public void run(){
        while(th != null){
            
            repaint();

            //ゲーム中のときだけ処理
            if(mode == 1)
                playingNow();
            
            try{
                Thread.sleep(30);   //30m秒とめる
            }catch(InterruptedException e){
            }
        }
    }

    //以下WindowListenerのメソッドをオーバーライド
    public void windowClosing(WindowEvent evt){
        dispose();
    }
    public void windowOpened(WindowEvent evt){}
    public void windowIconified(WindowEvent evt){}
    public void windowDeiconified(WindowEvent evt){}
    public void windowClosed(WindowEvent evt){}
    public void windowActivated(WindowEvent evt){}
    public void windowDeactivated(WindowEvent evt){}
    
    //以下KeyListenerインターフェースのメソッドをオーバーライド
    public void keyPressed(KeyEvent e){

        if(e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                //ゲーム中じゃないとき
                if (mode == 0) {
                    mode = 1;
                } else if (mode == 2) {
                  stage++;
                  setCondition();
                  mode=1;
                } else if (mode == 3 || mode == 4) {
                    mode = 0;
                    setCondition();
                }
            }
            if (mode == 1) {

                int cd = e.getKeyCode();    //押されたキーを取得
                //System.out.println(cd);
                if (cd == KeyEvent.VK_UP) {
                    jibun.addY(-10);
                } else if (cd == KeyEvent.VK_DOWN) {
                    jibun.addY(10);
                } else if (cd == KeyEvent.VK_RIGHT) {
                    jibun.addX(10);
                } else if (cd == KeyEvent.VK_LEFT) {
                    jibun.addX(-10);
                } else if (cd == KeyEvent.VK_SPACE) {
                    myshells.add(new MyShell(mys, jibun.getX()
                            + (jibun.getSizeX() / 2) - 1, jibun.getY() - 15));
                    myShellNum++;
                }
            }
        }

    }

    //ゲーム中の処理
    void playingNow()
    {
        //敵の群れの移動制御
        if(enemy[0][0].getX() < 10 || enemy[0][0].getX() > 50)
            eneMoveRight = !eneMoveRight;

        //敵の群れが動く
        if(eneMoveRight == true){
            for(int i = 0; i < ENEMY_HOR_AMOUNT; i++)
                for(int j = 0; j < ENEMY_VER_AMOUNT; j++)
                    enemy[i][j].addX(1);
        }else{
            for(int i = 0; i < ENEMY_HOR_AMOUNT; i++)
                for(int j = 0; j < ENEMY_VER_AMOUNT; j++)
                    enemy[i][j].addX(-1);
        }

        //自分の弾丸が動く
        for(int i = 0; i < myShellNum; i++){
            ms = (MyShell)myshells.get(i);
            ms.addY(-10);

            //弾丸が敵に当たれば、敵＆弾丸死亡
            for(int j = 0; j < ENEMY_HOR_AMOUNT; j++){
                for(int k = 0; k < ENEMY_VER_AMOUNT; k++){
                    if(ms.getX() + ms.getSizeX() >= enemy[j][k].getX()
                            && ms.getX() <= (enemy[j][k].getX() + enemy[j][k].getSizeX())
                            && ms.getY() >= (enemy[j][k].getY() - ms.getSizeY())
                            && ms.getY() <= (enemy[j][k].getY() + enemy[j][k].getSizeY())
                            && enemy[j][k].getAlive()){
                        enemy[j][k].setAlive(false);
                        ms.setAlive(false);
                        enemyNokori--;
                    }
                }
            }

            //敵がいなくなったらクリア
            if(enemyNokori == 0) {
                if (stage == maxStage) {
                    mode = 3;
                } else {
                    mode = 2;
                }

            }

            //弾丸が外に出たら弾丸死亡
            if(ms.getY() + ms.getSizeY() < 0)
                ms.setAlive(false);

            //死亡した弾丸を配列から削除
            if(ms.getAlive() == false){
                myshells.remove(i);
                myShellNum--;
                i--;
            }
        }

        //敵が弾丸を撃つ
        for(int i = 0; i < ENEMY_HOR_AMOUNT; i++){
            for(int j = 0; j < ENEMY_VER_AMOUNT; j++){
                if(ENEMY_SHOT > (int)(100 * Math.random())
                        && enemy[i][j].getAlive()){
                    himshells.add(new HimShell(his, enemy[i][j].getX()
                            + (enemy[i][j].getSizeX() / 2), enemy[i][j].getY() + 15));
                    himShellNum++;
                }
            }
        }

        //敵の弾丸が動く
        for(int i = 0; i < himShellNum; i++){
            hs = (HimShell)himshells.get(i);
            hs.addY(2);

            //弾丸が自分に当たったらゲームオーバー
            //少しくらい掠っても死なないように判定は甘めに調整
            if(hs.getX() >= (jibun.getX() - hs.getSizeX())+4
                    && hs.getX() <= (jibun.getX() + jibun.getSizeX())-4
                    && hs.getY() >= (jibun.getY() - hs.getSizeY())+10
                    && hs.getY() <= (jibun.getY() + jibun.getSizeY()-2)){
                life--;
                hs.setX(10000);
                hs.setY(10000);
                if (life == 0) {
                    mode = 4;
                }
            }

            //弾丸が画面外に出たら、配列から削除
            if(hs.getY() > 550){
                himshells.remove(i);
                himShellNum--;
                i--;
            }
        }

        keyAdmit = true;
        repaint();
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}

}