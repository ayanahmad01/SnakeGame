import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int B_Height=400, B_Width=400;
    int Dot_Size=10;
    int Max_Dots=1600;
    int Dots;
    int[] x=new int[Max_Dots];
    int[] y=new int[Max_Dots];
    int apple_x, apple_y;

    //Images
    Image body,head,apple;
    Timer timer;
    int DELAY=200;
    boolean leftDir=true, rightDir=false, downDir=false, upDir=false;
    boolean inGame=true;
    Board(){
        TAdapter tAdapter=new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_Width,B_Height));
        setBackground(Color.black);
        InitializeGame();
        loadImages();
    }
    //Initialize game
    public void InitializeGame(){
        Dots=3;
        //Initialize Snake's position
        x[0]=150;
        y[0]=150;
        for(int i =1;i<Dots;i++){
            x[i]=x[0]+Dot_Size*i;
            y[i]=y[0];
        }
        locateApple();
        timer= new Timer(DELAY, this);
        timer.start();
    }
    //Load images from resources folder to Image object
    public void loadImages(){
        ImageIcon bodyIcon= new ImageIcon("src/resources/dot.png");
        body=bodyIcon.getImage();
        ImageIcon headIcon= new ImageIcon("src/resources/head.png");
        head=headIcon.getImage();
        ImageIcon appleIcon= new ImageIcon("src/resources/apple.png");
        apple=appleIcon.getImage();
    }
    //draw images at snake's and apple's position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
    //draw image
    public void doDrawing(Graphics g){
        if(inGame){
            g.drawImage(apple,apple_x,apple_y,this);
            for(int i=0;i<Dots;i++){
                if(i==0) g.drawImage(head,x[0],y[0],this);
                else g.drawImage(body,x[i],y[i],this);
            }
        }
        else {
            gameOver(g);
            timer.stop();
        }
    }
    //Randomize apple's position
    public void locateApple(){
        apple_x=((int)(Math.random()*39))*Dot_Size;
        apple_y=((int)(Math.random()*39))*Dot_Size;
    }
    //Check Collision with Border and Body
    public void checkColl(){
        //Collision with body
        for(int i=1;i<Dots;i++){
            if(i>4&&x[0]==x[i]&&y[0]==y[i]){
                inGame=false;
                return;
            }
        }
        //Collision with border
        if(x[0]<0) inGame=false;
        if(x[0]>=B_Width) inGame=false;
        if(y[0]<0) inGame=false;
        if(y[0]>=B_Height) inGame=false;
    }
    //GameOver and Score
    public void gameOver(Graphics g){
        String msg="Game Over";
        int score=(Dots-3)*100;
        String scoreMsg="Score: "+score;
        Font small=new Font("Helvetica",Font.BOLD,14);
        FontMetrics fontMetrics=getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg,(B_Width-fontMetrics.stringWidth(msg))/2,B_Height/4);
        g.drawString(scoreMsg,(B_Width-fontMetrics.stringWidth(scoreMsg))/2,(3*B_Height)/4);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame) {
            checkApple();
            checkColl();
            move();
        }
        repaint();
    }
    //Make Snake Move
    public void move(){
        for(int i=Dots;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        if(leftDir) x[0]-=Dot_Size;
        if(rightDir) x[0]+=Dot_Size;
        if(upDir) y[0]-=Dot_Size;
        if(downDir) y[0]+=Dot_Size;
    }
    //Make Snake eat food
    public void checkApple(){
        if(apple_x==x[0]&&apple_y==y[0]){
            Dots++;
            locateApple();
        }
    }
    //Implement Controls
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key=keyEvent.getKeyCode();
            if(key==KeyEvent.VK_LEFT&&!rightDir){
                leftDir=true;
                upDir=false;
                downDir=false;
            }
            if(key==KeyEvent.VK_RIGHT&&!leftDir){
                rightDir=true;
                upDir=false;
                downDir=false;
            }
            if(key==KeyEvent.VK_UP&&!downDir){
                leftDir=false;
                upDir=true;
                rightDir=false;
            }
            if(key==KeyEvent.VK_DOWN&&!upDir){
                leftDir=false;
                rightDir=false;
                downDir=true;
            }
        }
    }
}