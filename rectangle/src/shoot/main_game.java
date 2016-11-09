package shoot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class main_game extends JFrame {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new main_game();
			}
		});
	}
	private JPanel subPanelNorth, subPanelEast, subPanelSouth, subPanelWest;

	static JLabel lifeLabel,timerLabel;

	main_game() {
		setTitle("Rectangle");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(540, 580);
		setLocationRelativeTo(null);
		setPanel();
		setVisible(true);
		setResizable(false);
	}
	void setPanel() {
			setLayout(new BorderLayout());
			subPanelNorth = new JPanel();
			subPanelNorth.setBackground(Color.GRAY);
			subPanelNorth.setPreferredSize(new Dimension(0, 40));
			subPanelNorth.setLayout(new GridLayout(1, 2));

			lifeLabel=new JLabel("");
			lifeLabel.setFont(new Font(null, Font.BOLD,15));
			lifeLabel.setHorizontalAlignment(JLabel.LEFT);
			subPanelNorth.add(lifeLabel);
			timerLabel=new JLabel("");
			timerLabel.setFont(new Font(null, Font.BOLD,15));
			timerLabel.setHorizontalAlignment(JLabel.RIGHT);
			subPanelNorth.add(timerLabel);

			subPanelEast = new JPanel();
			subPanelEast.setBackground(Color.GRAY);
			subPanelEast.setPreferredSize(new Dimension(20, 0));
			subPanelSouth = new JPanel();
			subPanelSouth.setBackground(Color.GRAY);
			subPanelSouth.setPreferredSize(new Dimension(0, 20));
			subPanelWest = new JPanel();
			subPanelWest.setBackground(Color.GRAY);
			subPanelWest.setPreferredSize(new Dimension(20, 0));
			add(subPanelNorth, BorderLayout.NORTH);
			add(subPanelEast, BorderLayout.EAST);
			add(subPanelSouth, BorderLayout.SOUTH);
			add(subPanelWest, BorderLayout.WEST);
			add(new Main_Panel(), BorderLayout.CENTER);
		}
	}