import java.awt.*;      //Frame

abstract class AnimationFrame extends Frame {

    private int frame_w;    //画面横幅
    private int frame_h;    //画面縦幅

    Image offs;     //オフスクリーン用バッファ
    Graphics offg;  //オフスクリーングラフィックスオブジェクト


    public AnimationFrame(String title,int frame_w,int frame_h){
        super(title);

        this.frame_w = frame_w;
        this.frame_h = frame_h;

    }

    public void addNotify(){
        super.addNotify();
        offs = createImage(frame_w,frame_h);    //オフスクリーンを生成
        offg = offs.getGraphics();              //オフスクリーン用グラフィックスを取得
    }


    public void update(Graphics g){
        paint(g);
    }

    abstract public void aniPaint(Graphics g);  //これをオーバライドして表示する

    public void paint(Graphics g){
        offg.setColor(Color.BLACK);         //白にして
        offg.fillRect(0,0,frame_w,frame_h); //四角形で塗りつぶし
        offg.setColor(Color.BLACK);         //黒に戻して
        aniPaint(offg);                     //aniPaintを描画
        g.drawImage(offs,0,0,this);         //オフスクリーンを実際の画面に描画
    }

}