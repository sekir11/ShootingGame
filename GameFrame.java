import java.awt.*;          //Frame
import java.awt.event.*;    //WindowListener,MouseLisnter
import java.util.ArrayList;

//1画面のちらつきをなくすAnimationFrameを継承する
public class GameFrame extends AnimationFrame implements WindowListener,KeyListener,Runnable{

    //ゲームモード。0:タイトル 1:ゲーム中 2:クリア 3:ゲームオーバー
    GameMode mode = new GameMode();

    Stage stage = new Stage();

    //自機を表現するオブジェクト
    Player player = new Player(getToolkit().getImage("img/player.png"));

    //敵を表現するオブジェクト
    Enemy[][] enemies = new Enemy[Enemy.ENEMY_HOR_AMOUNT][Enemy.ENEMY_VER_AMOUNT];

    //自分の弾丸を表現するオブジェクト
    PlayerShell playerShell;

    //自分の弾丸を表現するオブジェクトの配列
    java.util.List<PlayerShell> playerShellList = new ArrayList();

    //敵の弾丸を表現するオブジェクト
    EnemyShell enemyShell;

    //敵の弾丸を表現するオブジェクトの配列
    java.util.List<EnemyShell> enemyShellList = new ArrayList();

    //画面上で生きている敵の弾丸の数
    int himShellNum;

    //敵の残数
    int enemyNokori;

    //画像。敵などのコンストラクタに渡す
    Image mys,his;
    Image[] enemy = new Image[Enemy.ENEMY_KIND_AMOUNT];

    //他のクラスから参照できるよう定数で宣言
    public static final int FRAME_W = 800;      //フレームの横幅
    public static final int FRAME_H = 600;      //フレームの縦幅

    private final Thread thread = new Thread(this);   //Threadオブジェクトを宣言

    public GameFrame(String title){
        //2スーパークラスにタイトルと画面の幅、高さを渡す
        super(title, FRAME_W, FRAME_H);

        addKeyListener(this);

        setSize(FRAME_W, FRAME_H);
        setVisible(true);

        enemy[0] = getToolkit().getImage("img/enemy1.png");
        enemy[1] = getToolkit().getImage("img/enemy2.png");
        enemy[2] = getToolkit().getImage("img/enemy3.png");
        mys = getToolkit().getImage("img/bullet.png");
        his = getToolkit().getImage("img/bullet.png");

        //初期状態を整える
        setCondition();

        thread.start();             //run()を実行する
    }

    //背景ちらつき防止
    public void update(Graphics g){
        paint(g);
    }

    //初期状態を整える
    void setCondition(){
        //敵のインスタンスを生成
        enemies = Enemy.generateEnemies(enemy);

        //変数初期化
        mode.resetGameMode();
        player.resetLife();
        enemyNokori = Enemy.ENEMY_HOR_AMOUNT * Enemy.ENEMY_VER_AMOUNT;
        himShellNum = 0;

        //弾丸を全て削除
        playerShellList.clear();
        enemyShellList.clear();
    }

    //3paintの代わりにaniPaintを使う
    public void aniPaint(Graphics g){
        g.setColor(Color.WHITE);

        if(mode.getMode() == 0){
            g.drawString("EnterKey を押してゲームをスタートしてください。",80,250);
            g.setFont(new Font("Serif",Font.BOLD,30));
        } else {

            player.drawImage(g, this);
            Enemy.drawImage(g, this, enemies);
            PlayerShell.drawImage(g, this, playerShellList);
            EnemyShell.drawImage(g, this, enemyShellList);

            //敵の残数を表示
            g.drawString("ステージ："+ stage.getStage() + " / " + stage.maxStage, 10,60);
            g.drawString("敵残数："+ enemyNokori, 230,60);
            g.drawString("残りライフ："+ player.getLife(), 400,60);

            if(mode.getMode() == 2){
                g.drawString("ゲームクリアです！", 50, 170);
                g.drawString("Enter で次のステージへ。",80,210);
            }
            else if(mode.getMode() == 3){
                g.drawString("ゲームクリアです！", 50, 170);
                g.drawString("Enter でタイトルに戻ります。",80,210);
            }
            else if(mode.getMode() == 4){
                g.drawString("ゲームオーバーです。",60,170);
                g.drawString("Enter でタイトルに戻ります。",90,210);
            }
        }

    }


    public void run(){
        while (thread != null) {
            
            repaint();

            //ゲーム中のときだけ処理
            if (mode.getMode() == 1)
                playingNow();
            
            try {
                Thread.sleep(30);   //30m秒とめる
            } catch (InterruptedException ignored) {
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
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){

        if(e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                //ゲーム中じゃないとき
                if (mode.getMode() == 0) {
                    mode.setMode(1);
                } else if (mode.getMode() == 2) {
                    stage.addStage();
                  setCondition();
                  mode.setMode(1);
                } else if (mode.getMode() == 3 || mode.getMode() == 4) {
                    mode.resetGameMode();
                    stage.resetStage();
                    setCondition();
                }
            }
            if (mode.getMode() == 1) {

                int cd = e.getKeyCode();    //押されたキーを取得
                if (cd == KeyEvent.VK_RIGHT) {
                    if (player.getX() <= FRAME_W - 50) {
                        player.addX(10);
                    }
                } else if (cd == KeyEvent.VK_LEFT) {
                    if (player.getX() >= 0) {
                        player.addX(-10);
                    }
                } else if (cd == KeyEvent.VK_SPACE) {
                    playerShellList.add(new PlayerShell(mys, player.getX()
                            + (player.getSizeX() / 2) - 1, player.getY() - 15));
                }
            }
        }

    }

    //ゲーム中の処理
    void playingNow() {

        //敵の群れが動く
        for(int i = 0; i < Enemy.ENEMY_HOR_AMOUNT; i++)
            for(int j = 0; j < Enemy.ENEMY_VER_AMOUNT; j++)
                enemies[i][j].addX(enemies[i][j].changeMoveDirect());


        //自分の弾丸が動く
        for(int i = 0; i < playerShellList.size(); i++){
            playerShell = playerShellList.get(i);
            playerShell.addY(-10);

            //弾丸が敵に当たれば、敵＆弾丸死亡
            for(int j = 0; j < Enemy.ENEMY_HOR_AMOUNT; j++){
                for(int k = 0; k < Enemy.ENEMY_VER_AMOUNT; k++){
                    if(playerShell.getX() + playerShell.getSizeX() >= enemies[j][k].getX()
                            && playerShell.getX() <= (enemies[j][k].getX() + enemies[j][k].getSizeX())
                            && playerShell.getY() >= (enemies[j][k].getY() - playerShell.getSizeY())
                            && playerShell.getY() <= (enemies[j][k].getY() + enemies[j][k].getSizeY())
                            && enemies[j][k].getAlive()){
                        enemies[j][k].setAlive(false);
                        playerShell.setAlive(false);
                        enemyNokori--;
                    }
                }
            }

            //敵がいなくなったらクリア
            if(enemyNokori == 0) {
                if (stage.checkEnd()) {
                    mode.setMode(3);
                } else {
                    mode.setMode(2);
                }

            }

            //弾丸が外に出たら弾丸死亡
            if(playerShell.getY() + playerShell.getSizeY() < 0)
                playerShell.setAlive(false);

            //死亡した弾丸を配列から削除
            if(!playerShell.getAlive()) {
                playerShellList.remove(i);
                i--;
            }
        }

        //敵が弾丸を撃つ
        for(int i = 0; i < Enemy.ENEMY_HOR_AMOUNT; i++){
            for(int j = 0; j < Enemy.ENEMY_VER_AMOUNT; j++){
                if(Enemy.ENEMY_SHOT > (int)(100 * Math.random())
                        && enemies[i][j].getAlive()){
                    enemyShellList.add(new EnemyShell(his, enemies[i][j].getX()
                            + (enemies[i][j].getSizeX() / 2), enemies[i][j].getY() + 15));
                    himShellNum++;
                }
            }
        }

        //敵の弾丸が動く
        for(int i = 0; i < himShellNum; i++){
            enemyShell = enemyShellList.get(i);
            enemyShell.addY(2);

            //弾丸が自分に当たったらゲームオーバー
            //少しくらい掠っても死なないように判定は甘めに調整
            if(enemyShell.getX() >= (player.getX() - enemyShell.getSizeX())+4
                    && enemyShell.getX() <= (player.getX() + player.getSizeX())-4
                    && enemyShell.getY() >= (player.getY() - enemyShell.getSizeY())+10
                    && enemyShell.getY() <= (player.getY() + player.getSizeY()-2)){
                player.decreaseLife();
                enemyShell.setX(10000);
                enemyShell.setY(10000);
                if (player.getLife() == 0) {
                    mode.setMode(4);
                }
            }

            //弾丸が画面外に出たら、配列から削除
            if(enemyShell.getY() > 550){
                enemyShellList.remove(i);
                himShellNum--;
                i--;
            }
        }
        repaint();
    }
}