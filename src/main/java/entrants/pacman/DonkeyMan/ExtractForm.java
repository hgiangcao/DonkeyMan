/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entrants.pacman.DonkeyMan;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 *
 * @author Giang
 */
public class ExtractForm extends javax.swing.JFrame {

    /**
     * Creates new form extractForm
     */
    public int pacmanX, pacmanY;
    int ghostPosition[][];
    int pillPosition[][];
    int pillActivePosition[][];

    final int unit = 4;
    final int unitX = 8;
    int margin = 25;

    public MCTSNode MCTSTree;
    int twoNearestChild[];
    Game game;

    public ExtractForm() {
        initComponents();
        ghostPosition = new int[4][2];
        pillPosition = new int[300][2];
        pillActivePosition = new int[300][2];
        twoNearestChild = new int[4];
        MCTSTree = new MCTSNode();

    }

    public ExtractForm(Game game) {
        initComponents();
        ghostPosition = new int[4][2];
        pillPosition = new int[300][2];
        pillActivePosition = new int[300][2];
        twoNearestChild = new int[4];
        MCTSTree = new MCTSNode();
        MCTSTree.init(game);
        updateGameInformation(game);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCheckNextJunction = new javax.swing.JButton();
        btnMove = new javax.swing.JButton();
        btnRunMCTS = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Extract Infomation");
        setBackground(new java.awt.Color(51, 51, 51));

        btnCheckNextJunction.setText("CheckNextJunction");
        btnCheckNextJunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckNextJunctionActionPerformed(evt);
            }
        });

        btnMove.setText("Move");
        btnMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveActionPerformed(evt);
            }
        });

        btnRunMCTS.setText("RunMCTS");
        btnRunMCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunMCTSActionPerformed(evt);
            }
        });

        jButton1.setText("MoveByMCTS");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCheckNextJunction)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRunMCTS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(562, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCheckNextJunction)
                    .addComponent(btnMove)
                    .addComponent(btnRunMCTS)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    int currentNodeCheckJunction = -1;

    private void btnCheckNextJunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckNextJunctionActionPerformed
        // TODO add your handling code here:

        Random R = new Random();
        currentNodeCheckJunction = this.game.getJunctionIndices()[R.nextInt(this.game.getJunctionIndices().length)];
        repaint();;
        validate();

    }//GEN-LAST:event_btnCheckNextJunctionActionPerformed

    private void btnMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveActionPerformed
        // TODO add your handling code here:

        Game simulatedGame = this.game;

        int currentPacManNode = simulatedGame.getPacmanCurrentNodeIndex();

        MOVE nextMove = null;
        Random R = new Random();
        if (simulatedGame.getPossibleMoves(currentPacManNode).length != 0) {
            nextMove = simulatedGame.getPossibleMoves(currentPacManNode, this.game.getPacmanLastMoveMade())[R.nextInt(simulatedGame.getPossibleMoves(currentPacManNode, this.game.getPacmanLastMoveMade()).length)];
        }
        //  nextMove =  this.listChild[this.selectedChild].moveToReach;

        EnumMap<GHOST, MOVE> listGhostMove = new EnumMap<GHOST, MOVE>(GHOST.class);

        // get Ghost current Move
        if (simulatedGame.getPossibleMoves(currentPacManNode).length != 0) {
            for (GHOST ghost : GHOST.values()) {
                // if ghost require a move
                if (this.game.doesGhostRequireAction(ghost)) {
                    MOVE dangerMove = simulatedGame.getPossibleMoves(this.game.getGhostCurrentNodeIndex(ghost))[R.nextInt(simulatedGame.getPossibleMoves(this.game.getGhostCurrentNodeIndex(ghost)).length)];

                    listGhostMove.put(ghost, dangerMove);
                }

            }
        }

        simulatedGame.advanceGame(nextMove, listGhostMove);

        currentNodeCheckJunction = simulatedGame.getPacmanCurrentNodeIndex();
        updateGameInformation(this.game);
        repaint();
        validate();

    }//GEN-LAST:event_btnMoveActionPerformed
    ArrayList<Junction> listJunction = new ArrayList<>();
    MCTSNode root;
    MOVE moveByMCTS;
    int currentLevel;
    private void btnRunMCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunMCTSActionPerformed
        double timeDue = System.currentTimeMillis() + 40;
        //Place your game logic here to play the game as Ms Pac-Man
        int currentPacMan = game.getPacmanCurrentNodeIndex();

        // clone game
        String strGameState = game.getGameState();
        Game gameX = new Game(0);
        gameX.setGameState(strGameState);

      
                root = new MCTSNode();
                root.init(gameX);
                root.createEntireTree(root, 0);
                MCTSNode.currentTactic = 0;
    
        while (System.currentTimeMillis() < (timeDue - 10)) {
            root.init(gameX);
            MCTSNode.runMCTS(root, gameX.getNumberOfActivePills());
        }

        if ((root.maxViValue[0]) > MCTSNode.NOMAL_MIN_SURVIVAL) {
            MCTSNode.currentTactic = 1;
        } else {
            MCTSNode.currentTactic = 0;
        }

        MOVE nextMove = root.selectBestMove(gameX);
        //    System.out.println("FROM  " + gameX.getPacmanCurrentNodeIndex() + " MOVE MAKE " + nextMove +" WILL REACH " + gameX.getNeighbour(currentPacMan, nextMove));

        currentLevel = gameX.getCurrentLevel();

        SimulateGhostMove ghostsMove = new SimulateGhostMove();
        EnumMap<GHOST, MOVE> listGhostMove = new EnumMap<>(GHOST.class);
        listGhostMove = ghostsMove.getMove(this.game.copy());

        gameX.setGameState(strGameState);
        this.game.advanceGame(nextMove, listGhostMove);
        
        System.out.println("");
        MCTSNode.print(root);
        System.out.println("\nEND MCTS");
        
        updateGameInformation(this.game);
        repaint();
        validate();
    }//GEN-LAST:event_btnRunMCTSActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      

    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckNextJunction;
    private javax.swing.JButton btnMove;
    private javax.swing.JButton btnRunMCTS;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

    public void updateGameInformation(Game game) {
        this.game = game;
        // pacman
        int currentPosition = game.getPacmanCurrentNodeIndex();
        pacmanX = game.getNodeXCood(currentPosition);
        pacmanY = game.getNodeYCood(currentPosition);
        int index = 0;
        // ghost
        // Strategy 1: Adjusted for PO
        for (GHOST ghost : GHOST.values()) {

            // If can't see these will be -1 so all fine there
            int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
            if (ghostLocation != -1) {
                ghostPosition[index][0] = game.getNodeXCood(ghostLocation);
                ghostPosition[index][1] = game.getNodeYCood(ghostLocation);
            } else {
                ghostPosition[index][0] = -1;
                ghostPosition[index][1] = -1;
            }
            index++;
        }

        // PILLs
        int[] pillIndices = game.getPillIndices();

        int count = 0, countActive = 0;
        int tempActiveArray[][] = new int[300][2];
        int tempArray[][] = new int[300][2];

        for (int pillIndex : pillIndices) {
            tempArray[count][0] = game.getNodeXCood(pillIndex);
            tempArray[count][1] = game.getNodeYCood(pillIndex);
            count++;
        }

        pillIndices = game.getActivePillsIndices();

        for (int pillIndex : pillIndices) {
            if (game.isPillStillAvailable(game.getPillIndex(pillIndex))) {
                tempActiveArray[countActive][0] = game.getNodeXCood(pillIndex);
                tempActiveArray[countActive][1] = game.getNodeYCood(pillIndex);
                countActive++;
            }
        }

        this.pillActivePosition = new int[countActive][2];
        this.pillActivePosition = tempActiveArray;
        this.pillPosition = new int[count][2];
        this.pillPosition = tempArray;

    }

    public void paint(Graphics g) {

        g.setColor(Color.black);

        g.fillRect(margin, margin, 113 * 4, 130 * 4);
        Color listColor [] = new Color[] {Color.GREEN,Color.PINK, Color.ORANGE, Color.RED };
        int x =0 ;
        // ghost Position
        /*
        BLINKY(40, "Blinky"),
        PINKY(60, "Pinky"),
        INKY(80, "Inky"),
        SUE(100, "Sue");
        */
        
        for (GHOST ghost : GHOST.values()) {
            g.setColor(listColor[x++]);
            if (this.game.isGhostEdible(ghost)) {
                g.setColor(Color.blue);
            }
            g.fillOval(this.game.getNodeXCood(this.game.getGhostCurrentNodeIndex(ghost)) * unit + margin - unitX / 2, this.game.getNodeYCood(this.game.getGhostCurrentNodeIndex(ghost)) * unit + margin - unitX / 2, unitX * 2, unitX * 2);
        }

        // ACTIVE PILLS Position
        g.setColor(Color.white);
        int lenActive = this.pillActivePosition.length;
        for (int i = 0; i < lenActive; i++) {
            g.fillRect(pillActivePosition[i][0] * unit + margin + unitX / 8, pillActivePosition[i][1] * unit + margin + unitX / 8, unitX / 4, unitX / 4);
        }

        // drawJuction
        g.setColor(Color.white);
        int lenConjunction = this.game.getJunctionIndices().length;
        int listJuction[] = new int[lenConjunction + 1];
        listJuction = this.game.getJunctionIndices();
        for (int i = 0; i < lenConjunction; i++) {
            g.setColor(Color.WHITE);
            g.drawString(listJuction[i] + "", game.getNodeXCood(listJuction[i]) * unit + margin, game.getNodeYCood(listJuction[i]) * unit + margin - 10);

            g.fillRect(game.getNodeXCood(listJuction[i]) * unit + margin - 2, game.getNodeYCood(listJuction[i]) * unit + margin - 2, unitX + 4, unitX + 4);
        }

        g.setColor(Color.YELLOW);
        // pacman Position
        g.fillOval(this.game.getNodeXCood(this.game.getPacmanCurrentNodeIndex()) * unit + margin - unitX / 2, this.game.getNodeYCood(this.game.getPacmanCurrentNodeIndex()) * unit + margin - unitX / 2, unitX * 2, unitX * 2);

        if (this.currentNodeCheckJunction != -1) {
            paint(this.getGraphics(), this.game.getNodeXCood(currentNodeCheckJunction), this.game.getNodeYCood(currentNodeCheckJunction), currentNodeCheckJunction, Color.RED);

            for (Junction jucntion : listJunction) {
                paint(this.getGraphics(), this.game.getNodeXCood(jucntion.nodeIndex), this.game.getNodeYCood(jucntion.nodeIndex), jucntion.nodeIndex, Color.RED);
            }

        }

        drawTree(g, root);

    }

    void drawTree(Graphics g, MCTSNode root) {
        if (root == null) {
            return;
        }

        paint(g, root.game.getNodeXCood(root.nodeIndex), root.game.getNodeYCood(root.nodeIndex), root.nodeIndex);

        if (root.isLeaf()) {
            return;
        }

        for (MCTSNode child : root.listChild) {
            if (child == null) {
                continue;
            }
            drawTree(g, child);
            g.setColor(Color.red);
            g.drawLine(root.game.getNodeXCood(child.nodeIndex) * unit + margin + unit / 2, root.game.getNodeYCood(child.nodeIndex) * unit + margin + unit / 2,
                    root.game.getNodeXCood(root.nodeIndex) * unit + margin + unit / 2, root.game.getNodeYCood(root.nodeIndex) * unit + margin + unit / 2);

        }

    }

// draw specific point
    public void paint(Graphics g, int x, int y, int id) {
        g.setColor(Color.WHITE);
        g.drawString(id + "", x * unit + margin, y * unit + margin - 10);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x * unit + margin, y * unit + margin, unitX, unitX);

    }

    public void paint(Graphics g, int x, int y, int id, Color C) {
        g.setColor(Color.WHITE);
        g.drawString(id + "", x * unit + margin, y * unit + margin - 10);
        g.setColor(C);
        g.fillRect(x * unit + margin, y * unit + margin, unitX, unitX);

    }

   public boolean autoUpdate () {
       btnRunMCTSActionPerformed(null);
       
       return !this.game.wasPacManEaten() ;
   
   }
}
