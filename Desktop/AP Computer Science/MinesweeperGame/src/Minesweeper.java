import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//NEXT: add what happens when player wins the game (bombs marked with *)

public class Minesweeper implements ActionListener{

	JFrame frame = new JFrame("Minesweeper");
	JButton reset = new JButton("Reset");
	JButton[][] buttons = new JButton[9][9];
	int[][] grid = new int[9][9];
	Container container = new Container();
	final int MINE = -1;
	MouseListener mouse;
	Image bomb;
	
	
	public Minesweeper() {
		try{
			bomb = ImageIO.read(new File("src/Bomb.gif"));
		} catch (IOException e) {
				e.printStackTrace();
			}
		bomb = bomb.getScaledInstance(60, 54, Image.SCALE_DEFAULT);
		
		mouse = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getModifiers()==MouseEvent.BUTTON3_MASK) {
					if(e.getButton() == 3) {
						for(int r = 0; r<buttons.length; r++) {
							for(int c = 0; c<buttons[r].length; c++) {
								if(e.getSource().equals(buttons[r][c])){
									if(buttons[r][c].isEnabled()) {
										buttons[r][c].setText("*");
										buttons[r][c].setEnabled(false);
										checkWinGame();
									} else if (!buttons[r][c].isEnabled() && buttons[r][c].getText().equals("*")){
										buttons[r][c].setEnabled(true);
										buttons[r][c].setText("");
									}
								}
							}
						}
					}
				}
			}

			private void checkWinGame() {
				boolean win = true;
				for(int r = 0; r<buttons.length; r++) {
					for(int c = 0; c<buttons[r].length; c++) {
						if(grid[r][c] == MINE && !buttons[r][c].getText().equals("*")) {
							win = false;
						}
					}
				}
				if(win == true) {
					JFrame window1 = new JFrame();
					JPanel panel1 = new JPanel();
					JLabel label1 = new JLabel("You won! Play again by clicking reset.");
					window1.add(panel1);
					panel1.add(label1);
					window1.pack();
					window1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					window1.setVisible(true);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		};
		frame.setSize(600, 600);
		frame.setLayout(new BorderLayout());
		frame.add(reset, BorderLayout.NORTH);
		reset.addActionListener(this);
		
		//Button grid stuff
		container.setLayout(new GridLayout(9, 9));
		for(int r = 0; r < buttons.length; r++) {
			for(int c = 0; c < buttons[r].length; c++) {
				buttons[r][c] = new JButton();
				buttons[r][c].addActionListener(this);
				container.add(buttons[r][c]);
				buttons[r][c].addMouseListener(mouse);
				
			}
		}
		frame.add(container, BorderLayout.CENTER);
		createMines();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Minesweeper();

	}
	
	public void createMines() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int r = 0; r < grid.length; r++) {
			for(int c = 0; c<grid[r].length; c++) {
				list.add(r*100+c);
			}
		}
		
		for(int x = 0; x<10; x++) {
			int pick = (int) (Math.random() * list.size());
			grid[list.get(pick)/100][list.get(pick)%100] = MINE;
			System.out.println("Mine created");
			list.remove(pick);
		}
		//count neighbors
		for(int x = 0; x<grid.length; x++) {
			for(int y = 0; y<grid[x].length; y++) {
				if(grid[x][y] != MINE) {
					int neighborCount = 0;
					if(x>0 && y>0 && grid[x-1][y-1]==MINE) {//up, left
						neighborCount++;
					}
					if(y>0 && grid[x][y-1] == MINE) {//up
						neighborCount++;
					}
					if(x<grid.length-1 && y>0 && grid[x+1][y-1] == MINE) {//up, right
						neighborCount++;
					}
					if(x>0 && grid[x-1][y] == MINE) {//left
						neighborCount++;
					}
					if(x<grid.length-1 && grid[x+1][y] == MINE) {//right
						neighborCount++;
					}
					if(x>0 && y<grid[0].length-1 && grid[x-1][y+1] == MINE) {//down, left
						neighborCount++;
					}
					if(y<grid[0].length-1 && grid[x][y+1]== MINE) {//down
						neighborCount++;
					}
					if(x<grid.length-1 && y<grid[0].length-1 && grid[x+1][y+1] == MINE) {//down, right
						neighborCount++;
					}
					grid[x][y] = neighborCount;
				}
			}
		}
	}

	public void loseGame() {
		for(int r = 0; r<buttons.length; r++) {
			for(int c = 0; c<buttons[r].length; c++) {
				if (buttons[r][c].isEnabled()) {
					if(grid[r][c] != MINE) {
						buttons[r][c].setText(grid[r][c] +"");
						buttons[r][c].setEnabled(false);
					}
					else {
						if(grid[r][c] == MINE) {
							buttons[r][c].setText("");
							buttons[r][c].setIcon(new ImageIcon(bomb));
							buttons[r][c].setEnabled(false);
						}
					}
				}
			}
		}
		JFrame window = new JFrame();
		JPanel panel = new JPanel();
		JLabel label = new JLabel("You lost. Try again by clicking reset!");
		window.add(panel);
		panel.add(label);
		window.pack();
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
	}
	
	public void removeZeros(int r, int c) {
		if(r>=0 && r<buttons.length && c>=0 && c<buttons[r].length
				&& buttons[r][c].isEnabled()==true
				&& grid[r][c]!= MINE) { //FIX THIS
			buttons[r][c].setText(grid[r][c] +"");
			buttons[r][c].setEnabled(false);
			if(grid[r][c] == 0) {
				removeZeros(r-1, c);
				removeZeros(r, c);
				removeZeros(r+1, c);
				removeZeros(r, c-1);
				removeZeros(r, c+1);
				removeZeros(r+1, c+1);
				removeZeros(r-1, c-1);
				removeZeros(r+1, c-1);
			}
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(reset)) {
			//reset game
			for(int r = 0; r<buttons.length; r++) {
				for(int c = 0; c<buttons[r].length; c++) {
					buttons[r][c].setEnabled(true);
					buttons[r][c].setText("");
					grid[r][c] = 0;
				}
			}
			createMines();
		}
		else{
			for(int r = 0; r<buttons.length; r++) {
				for(int c = 0; c<buttons[r].length; c++) {
					if(event.getSource().equals(buttons[r][c])) {
						if(grid[r][c] == MINE) {
							buttons[r][c].setText("");
							buttons[r][c].setIcon(new ImageIcon(bomb));
							loseGame();
						}
						else if (grid[r][c] == 0) {
							removeZeros(r, c);
						}
						else {
							buttons[r][c].setText(grid[r][c]+"");
							buttons[r][c].setEnabled(false);
						}
					}
				}
			}
		}
	}
}
