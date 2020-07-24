package Zelda;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Zelda();

	}

}

class ImageService {
	public static final String[] standingImageName = { "f.png" };
	public static final String[] walkingLeftImageName = { "l_1.png", "l_2.png", "l_3.png", "l_4.png", "l_5.png",
			"l_6.png", "l_7.png", "l_8.png", "l_9.png", "l_10.png" };
	public static final String[] walkingRightImageName = { "r_1.png", "r_2.png", "r_3.png", "r_4.png", "r_5.png",
			"r_6.png", "r_7.png", "r_8.png", "r_9.png", "r_10.png" };
}

interface State {
	// 세가지 메소드
	public void standing_button_pushed(Zelda zelda);

	public void left_button_pushed(Zelda zelda);

	public void right_button_pushed(Zelda zelda);

	public int playAnim(JLabel label, int imageIndex);
	// 이미지 인덱스 반환, 증가 시켜주는 부분
}

class STANDING implements State {
	private static STANDING standing;
	private static final int MOVING_DIRECTION = 0;

	private STANDING() {

	}

	public static STANDING getInstance() {
		if (standing == null) {
			standing = new STANDING();
		}
		return standing;
	}

	@Override
	public void standing_button_pushed(Zelda zelda) {
		// TODO Auto-generated method stub

	}

	@Override
	public void left_button_pushed(Zelda zelda) {
		// TODO Auto-generated method stub
		zelda.setState(LEFT.getInstance());

	}

	@Override
	public void right_button_pushed(Zelda zelda) {
		zelda.setState(RIGHT.getInstance());

	}

	@Override
	public int playAnim(JLabel lbl, int imageIndex) {
		// TODO Auto-generated method stub
		if (ImageService.standingImageName.length <= imageIndex) {
			imageIndex = 0;
		}

		lbl.setIcon(new ImageIcon("./src/img/" + ImageService.standingImageName[imageIndex]));
		lbl.setLocation(lbl.getLocation().x + MOVING_DIRECTION, lbl.getLocation().y);

		return ++imageIndex;
	}

}

class LEFT implements State {
	private static LEFT left;
	private static final int MOVING_DIRECTION = -1;
	private static int speed = 0;
	private static boolean isFast = true;

	private LEFT() {

	}

	public static LEFT getInstance() {
		if (left == null) {
			left = new LEFT();
		}
		return left;
	}

	@Override
	public void standing_button_pushed(Zelda zelda) {
		// TODO Auto-generated method stub
		zelda.setState(STANDING.getInstance());
		speed = 0;

	}

	@Override
	public void left_button_pushed(Zelda zelda) {
		// TODO Auto-generated method stub
		speed++;

		isFast = !isFast;

	}

	@Override
	public void right_button_pushed(Zelda zelda) {
		// TODO Auto-generated method stub
		zelda.setState(RIGHT.getInstance());
		speed = 0;

	}

	@Override
	public int playAnim(JLabel lbl, int imageIndex) {
		// TODO Auto-generated method stub
		if (ImageService.walkingLeftImageName.length <= imageIndex) {
			imageIndex = 0;
		}

		lbl.setIcon(new ImageIcon("./src/img/" + ImageService.walkingLeftImageName[imageIndex]));
		
		/*
		 * lbl.setLocation(lbl.getLocation().x + MOVING_DIRECTION * speed,
		 * lbl.getLocation().y); return ++imageIndex;
		 */
		
		  if (lbl.getLocation().x < -180) { lbl.setLocation(lbl.getLocation().x,
		  lbl.getLocation().y); }
		  
		  else { lbl.setLocation(lbl.getLocation().x + MOVING_DIRECTION * speed,
		  lbl.getLocation().y);
		  
		  } return ++imageIndex;
		 

	}
}

class RIGHT implements State {

	private static RIGHT right;
	private static final int MOVING_DIRECTION = 1;
	private static int speed = 0;

	private RIGHT() {

	}

	public static RIGHT getInstance() {
		if (right == null) {
			right = new RIGHT();
		}
		return right;
	}

	@Override
	public void standing_button_pushed(Zelda zelda) {
		// TODO Auto-generated method stub
		zelda.setState(STANDING.getInstance());
		speed = 0;

	}

	@Override
	public void left_button_pushed(Zelda zelda) {
		// TODO Auto-generated method stub
		zelda.setState(LEFT.getInstance());
		speed = 0;

	}

	@Override
	public void right_button_pushed(Zelda zelda) {
		speed++;

	}

	public int playAnim(JLabel lbl, int imageIndex) {
		// TODO Auto-generated method stub
		if (ImageService.walkingRightImageName.length <= imageIndex) {

			imageIndex = 0;
		}

		lbl.setIcon(new ImageIcon("./src/img/" + ImageService.walkingRightImageName[imageIndex]));
		lbl.setLocation(lbl.getLocation().x + MOVING_DIRECTION * speed, lbl.getLocation().y);
		return ++imageIndex;

	}
}

class Zelda extends JFrame {
	private static final long serialVersionUID = 1L;
	private Field field;
	private Controller controller;
	private State state; // 스테이트 값 가지게 하고,

	private Zelda zelda; // 객체 생성하고,

	public void setState(State state) {
		this.state = state;
	}

	public Zelda() {
		super("Zelda");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		zelda = this;
		// 실제로 젤다 객체가 생성될 때, 자기 자신을 젤다에 넣도록
		state = STANDING.getInstance();

		field = new Field();
		controller = new Controller();

		add(controller, BorderLayout.SOUTH);
		add(field, BorderLayout.CENTER);

		setSize(400, 400);
		setVisible(true);

		Thread th = new Thread(field);
		th.start();
	}

	private class Field extends JPanel implements Runnable {
		private static final long serialVersionUID = 1L;
		private JLabel lbl;

		public Field() {
			setLayout(new BorderLayout());
			lbl = new JLabel();
			lbl.setIcon(new ImageIcon("./src.img/" + ImageService.standingImageName));
			add(lbl, BorderLayout.CENTER);

		}

		public void run() {

			int imageIndex = 0;
			while (true) {
				imageIndex = state.playAnim(lbl, imageIndex);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private class Controller extends JPanel implements ActionListener {

		private JButton btnStanding, btnWalkingLeft, btnWalkingRight;
		private JSlider jslider;

		public Controller() {
			setLayout(new FlowLayout());

			btnStanding = new JButton("Standing");
			btnWalkingLeft = new JButton("btnWalkingLeft");
			btnWalkingRight = new JButton("btnWalkingRight");

			btnStanding.addActionListener(this);
			btnWalkingLeft.addActionListener(this);
			btnWalkingRight.addActionListener(this);

			add(btnStanding);
			add(btnWalkingLeft);
			add(btnWalkingRight);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton btn = (JButton) e.getSource();
			if (btn == btnStanding) {
				state.standing_button_pushed(zelda);

			} else if (btn == btnWalkingLeft) {
				state.left_button_pushed(zelda);
			} else if (btn == btnWalkingRight) {
				state.right_button_pushed(zelda);
			}
		}
	}
}