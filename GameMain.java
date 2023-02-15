package impl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameMain extends JFrame {

    Chessboard qpan;

    public GameMain() {
        qpan = new Chessboard();
        // 创建菜单栏
        JMenuBar menubar = new JMenuBar();

        JMenu menu = new JMenu("游戏(G)");
        JMenuItem item1 = new JMenuItem("新游戏");
        JMenuItem item2 = new JMenuItem("退出");
        menu.add(item1);
        menu.addSeparator();
        menu.add(item2);
        menubar.add(menu);


        // 按钮
        JPanel btn = new JPanel();
        btn.setLayout(new GridLayout(10, 1));
        Button btn1 = new Button("重新开始");
        Button btn2 = new Button("悔棋");
        btn.add(btn1);
        btn.add(btn2);

        // 窗口设置
        BorderLayout bl = new BorderLayout();
        this.setTitle("五子棋");
        this.setSize(480, 490);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.add(menubar, bl.NORTH);
        this.add(qpan, bl.CENTER);
        this.add(btn, bl.EAST);

        // 按钮事件
        btn1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                qpan.againGame();
            }

        });
        //悔棋
        btn2.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                qpan.huiqi();
            }

        });

        // 新游戏
        item1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                qpan.againGame();
            }
        });

        // 退出
        item2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new GameMain();
    }

}
class Chess {
    Chessboard cp; 	//棋盘
    int row;		//横坐标
    int col;		//纵坐标
    Color color;	//棋子颜色

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public static final int BANJING = 18;

    public Chess(Chessboard cp, int col, int row, Color color) {
        this.cp = cp;
        this.col = col;
        this.row = row;
        this.color = color;
    }

    //画棋子
    public void draw(Graphics g) {
        //定义棋子圆心
        int xPos = col * 20 + 15;
        int yPos = row * 20 + 15;

        Graphics2D g2d = (Graphics2D) g;

        RadialGradientPaint paint = null;
        Color[] c = { Color.WHITE, Color.BLACK };
        float[] f = { 0f, 1f };
        int x = xPos + 3;
        int y = yPos - 3;
        if (color == Color.WHITE) {
            paint = new RadialGradientPaint(x, y, BANJING * 3, f, c);
        } else {
            paint = new RadialGradientPaint(x, y, BANJING, f, c);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(paint);
        g2d.fillOval(xPos - BANJING / 2, yPos - BANJING / 2, BANJING, BANJING);
    }
}
class Chessboard extends JPanel {
    public static final int MARGIN = 15;
    public static final int SPAN = 20;
    public static final int ROWS = 19;
    public static final int COLS = 19;
    Chess[] chessList = new Chess[19 * 19];
    int chessCount = 0;
    boolean iso = false;
    boolean isBlack = true;
    String message = "黑棋先下";

    public Chessboard() {
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                if (iso) {
                    return;
                }
                int col, row;
                col = (e.getX() - 15 + 10) / 20;
                row = (e.getY() - 15 + 10) / 20;

                if (col > 19 || col < 0 || row > 19 || row < 0) {
                    return;
                } else {
                    if (haschess(col, row)) {
                        return;
                    } else {
                        Color c = Color.BLACK;
                        if (isBlack) {
                            c = Color.BLACK;
                            message = "轮到白棋";
                        } else {
                            c = Color.WHITE;
                            message = "轮到黑棋";
                        }
                        Chess cc = new Chess(Chessboard.this, col, row, c);
                        chessList[chessCount++] = cc;
                        repaint();

                        if (isWin(col, row)) {
                            if (c == Color.BLACK) {
                                JOptionPane.showMessageDialog(Chessboard.this, "黑棋获胜！");
                            } else if (c == Color.WHITE) {
                                JOptionPane.showMessageDialog(Chessboard.this, "白旗获胜！");
                            }
                            iso = true;
                            return;
                        }
                        isBlack = !isBlack;
                    }
                }
            }
        });
    }

    @Override
    public void paint(Graphics e) {
        e.setColor(Color.ORANGE);
        e.fillRect(0, 0, 410, 460);
        e.setColor(Color.black);
        for (int i = 0; i < 20; i++) {
            e.drawLine(MARGIN, MARGIN + SPAN * i, MARGIN + 19 * 20, MARGIN + 20 * i);
        }
        for (int i = 0; i < 20; i++) {
            e.drawLine(15 + SPAN * i, 15, 15 + SPAN * i, 15 + 19 * 20);
        }

        e.fillRect(15 + 3 * 20 - 2, 15 + 3 * 20 - 2, 5, 5);
        e.fillRect(15 + 9 * 20 - 2, 15 + 3 * 20 - 2, 5, 5);
        e.fillRect(15 + 15 * 20 - 2, 15 + 3 * 20 - 2, 5, 5);
        e.fillRect(15 + 3 * 20 - 2, 15 + 9 * 20 - 2, 5, 5);
        e.fillRect(15 + 9 * 20 - 2, 15 + 9 * 20 - 2, 5, 5);
        e.fillRect(15 + 15 * 20 - 2, 15 + 9 * 20 - 2, 5, 5);
        e.fillRect(15 + 3 * 20 - 2, 15 + 15 * 20 - 2, 5, 5);
        e.fillRect(15 + 9 * 20 - 2, 15 + 15 * 20 - 2, 5, 5);
        e.fillRect(15 + 15 * 20 - 2, 15 + 15 * 20 - 2, 5, 5);

        Graphics2D e2 = (Graphics2D) e;
        e2.setStroke(new BasicStroke(3f));
        e2.drawLine(10, 10, 10, 400);
        e2.drawLine(10, 10, 400, 10);
        e2.drawLine(400, 10, 400, 400);
        e2.drawLine(10, 400, 400, 400);

        for (int i = 0; i < chessCount; i++) {
            chessList[i].draw(e);
        }
        e.setFont(new Font("黑体", Font.BOLD, 15));
        e.drawString("游戏提示:" + message, 20, 420);
    }

    private boolean haschess(int col, int row) {
        boolean result = false;
        for (int i = 0; i < chessCount; i++) {
            Chess cc = chessList[i];
            if (cc != null && cc.getCol() == col && cc.getRow() == row) {
                return true;
            }
        }
        return result;
    }

    private boolean haschess(int col, int row, Color c) {
        Boolean result = false;
        for (int i = 0; i < chessCount; i++) {
            Chess ch = chessList[i];
            if (ch != null && ch.getCol() == col && ch.getRow() == row && ch.getColor() == c) {
                result = true;
            }
        }
        return result;
    }

    private boolean isWin(int col, int row) {
        boolean result = false;
        int CountCh = 1;
        Color c = null;
        if (isBlack) {
            c = Color.BLACK;
        } else {
            c = Color.WHITE;
        }

        // 水平向左
        for (int x = col - 1; x >= 0; x--) {
            if (haschess(x, row, c)) {
                CountCh++;
            } else {
                break;
            }
        }
        // 水平向右
        for (int x = col + 1; x <= 19; x++) {
            if (haschess(x, row, c)) {
                CountCh++;
            } else {
                break;
            }
        }
        // 水平取胜
        if (CountCh >= 5) {
            result = true;
            message = "游戏结束";
        } else {
            result = false;
            CountCh = 1;
        }
        // 竖直向上
        for (int y = row - 1; y >= 0; y--) {
            if (haschess(col, y, c)) {
                CountCh++;
            } else {
                break;
            }
        }
        // 竖直向下
        for (int y = row + 1; y <= 19; y++) {
            if (haschess(col, y, c)) {
                CountCh++;
            } else {
                break;
            }
        }
        // 竖直取胜
        if (CountCh >= 5) {
            result = true;
            message = "游戏结束";
        } else {
            result = false;
            CountCh = 1;
        }
        // 斜向右上
        for (int x = col + 1, y = row - 1; x <= 19 && y >= 0; x++, y--) {
            if (haschess(x, y, c)) {
                CountCh++;
            } else {
                break;
            }
        }
        // 斜向左下
        for (int x = col - 1, y = row + 1; x >= 0 && y <= 19; x--, y++) {
            if (haschess(x, y, c)) {
                CountCh++;
            } else {
                break;
            }
        }
        // 斜向取胜
        if (CountCh >= 5) {
            result = true;
            message = "游戏结束";
        } else {
            result = false;
            CountCh = 1;
        }
        // 斜向左上
        for (int x = col - 1, y = row - 1; x >= 0 && y >= 0; x--, y--) {
            if (haschess(x, y, c)) {
                CountCh++;
            } else {
                break;
            }
        }
        // 斜向右下
        for (int x = col + 1, y = row + 1; x <= 19 && y <= 19; x--, y--) {
            if (haschess(x, y, c)) {
                CountCh++;
            } else {
                break;
            }
        }
        // 斜向取胜
        if (CountCh >= 5) {
            result = true;
            message = "游戏结束";
        } else {
            result = false;
            CountCh = 1;
        }

        return result;
    }

    public void againGame() {
        for (int i = 0; i < chessList.length; i++) {
            chessList[i] = null;
        }
        chessCount = 0;
        iso = false;
        message = "开局黑棋先手";
        repaint();
    }

    public void huiqi() {
        if (iso) {
            return;
        }
        chessList[chessCount - 1] = null;
        chessCount--;
        if (isBlack) {
            message = "白棋悔棋";
        } else {
            message = "黑棋悔棋";
        }
        isBlack = !isBlack;
        repaint();
    }
}