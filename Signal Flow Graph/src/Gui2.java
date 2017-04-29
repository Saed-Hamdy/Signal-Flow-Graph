import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class Gui2 extends JFrame implements ActionListener, MouseListener {

    private double[][] gains;
    boolean flagAddNode;
    public static SFGController sfg;
    public static JButton addEdgeBtn;
    public static JButton deleteLastNodeBtn;
    public static JButton deleteEdgeBtn;
    public static JPanel graphPanel;
    public static JLabel transferFnLable;
    public static JLabel errorsLable;
    public static JButton showResultsBtn;
    public static JButton getTranferFnBtn;
    public static JButton addNodeBtn;
    public static JButton clearAllBtn;
    public static JFrame frame;

    /**
     * Create the application.
     */
    public Gui2() {
        setResizable(false);
        gains = new double[20][20];
        sfg = new SFGController();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = this;
        frame.setTitle("SFG Project");
        frame.setBounds(100, 100, 800, 600);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(null);

        {
            addNodeBtn = new JButton("Add Node");
            addNodeBtn.setBounds(20, 24, 97, 23);
            frame.getContentPane().add(addNodeBtn);

            deleteLastNodeBtn = new JButton("Delete Last Node");
            deleteLastNodeBtn.setBounds(154, 24, 141, 23);
            frame.getContentPane().add(deleteLastNodeBtn);

            addEdgeBtn = new JButton("Add Edge");

            addEdgeBtn.setBounds(341, 24, 97, 23);
            frame.getContentPane().add(addEdgeBtn);

            deleteEdgeBtn = new JButton("Delete Edge");
            deleteEdgeBtn.setBounds(482, 24, 111, 23);
            frame.getContentPane().add(deleteEdgeBtn);

            clearAllBtn = new JButton("Clear");
            clearAllBtn.setBounds(649, 24, 111, 23);
            frame.getContentPane().add(clearAllBtn);

            showResultsBtn = new JButton("Show Details");
            showResultsBtn.setBounds(580, 493, 125, 23);
            frame.getContentPane().add(showResultsBtn);

            getTranferFnBtn = new JButton("Get Transfer FN");
            getTranferFnBtn.setBounds(411, 493, 111, 23);
            frame.getContentPane().add(getTranferFnBtn);

            addNodeBtn.addActionListener(this);
            deleteLastNodeBtn.addActionListener(this);
            deleteEdgeBtn.addActionListener(this);
            addEdgeBtn.addActionListener(this);
            clearAllBtn.addActionListener(this);
            getTranferFnBtn.addActionListener(this);
            showResultsBtn.addActionListener(this);
        }
        graphPanel = new JPanel();
        graphPanel.setBounds(20, 71, 740, 400);
        frame.getContentPane().add(graphPanel);
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
        graphPanel.setBackground(Color.WHITE);
        // GraphPanel graphPanel = new GraphPanel();
        // graphPanel.setBounds(10, 60, 665, 335);
        // frame.getContentPane().add(graphPanel);
        //
        transferFnLable = new JLabel("Transfer Function :");
        transferFnLable.setBounds(10, 493, 325, 23);
        frame.getContentPane().add(transferFnLable);

        errorsLable = new JLabel("Errors : ");
        errorsLable.setBounds(10, 527, 460, 23);
        frame.getContentPane().add(errorsLable);
        this.addMouseListener(this);

    }

   

    public void paint(Graphics g) {
        super.paintComponents(g);
        List<Node> shapes = sfg.nodes;
        for (Node shape : shapes) {
            shape.draw(g);
            println("draw");
        }
        for (int i = 0; i < shapes.size(); i++) {
            for (int j = 0; j < shapes.size(); j++) {
                if (gains[i][j] != 0) {
                    if (i < j) {
                        Arc a = new Arc(shapes.get(i).position, shapes.get(j).position, gains[i][j], true);
                        a.draw(g);
                    } else if (i > j) {
                        Arc a = new Arc(shapes.get(i).position, shapes.get(j).position, gains[i][j], false);
                        a.draw(g);
                    } else {
                        SelfLoop a = new SelfLoop(gains[i][j], shapes.get(i).position);
                        a.draw(g);
                    }
                }
            }
        }
        
    }

    private void addNode(MouseEvent e) {
      
        if(e.getX()<50|e.getY()<120|e.getX()>740|e.getY()>480)
            return;
        Node s = new Node(sfg.nodes.size(), e.getPoint());
        s.radius = 30;
        sfg.nodes.add(s);
        println(sfg.nodes.size());
        
        if (sfg.nodes.size() > gains.length) {
            double[][] temp = new double[sfg.nodes.size() + 10][sfg.nodes.size() + 10];
            for (int i = 0; i < gains.length; i++) {
                for (int j = 0; j < gains.length; j++) {
                    temp[i][j] = gains[i][j];
                }
            }
            gains = temp;
        }
        // add=false;
    }

    private void addEdge() {
        JTextField to = new JTextField(5);
        JTextField from = new JTextField(5);
        JTextField weight = new JTextField(5);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("From:"));
        myPanel.add(from);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("TO:"));
        myPanel.add(to);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Weight:"));
        myPanel.add(weight);
        int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter The Required Values",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Integer destination = Integer.parseInt(to.getText());
                Integer home = Integer.parseInt(from.getText());
                Float cost = Float.parseFloat(weight.getText());
                if (home >= 0 && home < sfg.nodes.size() && destination >= 0 && destination < sfg.nodes.size()) {
                    if(destination ==0){
                        errorsLable.setText("Errors : can't put edge to node 0 ");
                        return;
                    }
                    if (gains[home][destination] != 0) {
                        gains[home][destination] += cost;
                        sfg.addCost(home, destination, cost);
                    } else {
                        sfg.addEdge(home, destination, cost);
                        gains[home][destination] = cost;
                    }
                } else
                    errorsLable.setText("Errors : out of Bound");
            } catch (Exception e) {
                errorsLable.setText("Errors : only Numbers is valid");
            }

        }
        repaint();
    }

//    private void ShowError(String s) {
//        errorsLable.setText("Errors : " + sfg.errorMessage + "  " + s);
//        repaint();
//    }

    @SuppressWarnings("unused")
    private void deleteEdge() {
        JTextField to = new JTextField(5);
        JTextField from = new JTextField(5);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("From:"));
        myPanel.add(from);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("TO:"));
        myPanel.add(to);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter The Required Values",
                JOptionPane.OK_CANCEL_OPTION);
        try {
            if (result == JOptionPane.OK_OPTION) {
                Integer destination = Integer.parseInt(to.getText());
                Integer home = Integer.parseInt(from.getText());

                if (home >= 0 && home < SFGController.nodes.size() && destination >= 0
                        && destination < SFGController.nodes.size()) {
                    if (gains[home][destination] != 0) {
                        sfg.deleteEdge(home, destination);
                        gains[home][destination] = 0;
                    }
                } else
                    errorsLable.setText("Errors : out of Bound");
            }
        } catch (Exception e) {
            errorsLable.setText("Errors : only Integers is valid");
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        flagAddNode = false;
        if (e.getSource() == deleteLastNodeBtn) {
            if (sfg.nodes.size() > 0) {
                gains[sfg.nodes.size() - 1] = new double[gains.length];
                for (int i = 0; i < gains.length; i++) {
                    if (gains[i][sfg.nodes.size() - 1] != 0) {
                        sfg.deleteEdge(i, sfg.nodes.size() - 1);
                        gains[i][sfg.nodes.size() - 1] = 0;
                    }
                }
                sfg.deleteNode(sfg.nodes.size() - 1);
                repaint();
            }
        } else if (e.getSource() == addNodeBtn) {
            flagAddNode = true;
            println("add");
        } else if (e.getSource() == addEdgeBtn) {
            addEdge();
            repaint();
        } else if (e.getSource() == deleteEdgeBtn) {
            deleteEdge();
            repaint();
        } else if (e.getSource() == clearAllBtn) {
            graphPanel.setForeground(null);
            sfg.nodes.clear();
            gains = new double[20][20];
            repaint();
        } else if (e.getSource() == getTranferFnBtn) {
            getTranferFn();

        } else if (e.getSource() == showResultsBtn) {
            ShowDetails();
        }

        errorsLable.setText("Errors :" + sfg.errorMessage);
    }

    private void getTranferFn() {
        JTextField to = new JTextField(5);
        JTextField from = new JTextField(5);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("From:"));
        myPanel.add(from);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("TO:"));
        myPanel.add(to);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter The Required Values",
                JOptionPane.OK_CANCEL_OPTION);
        try {
            if (result == JOptionPane.OK_OPTION) {
                Integer destination = Integer.parseInt(to.getText());
                Integer home = Integer.parseInt(from.getText());

                if (home >= 0 && home < sfg.nodes.size() && destination >= 0 && destination < sfg.nodes.size()) {
                    boolean flag = true;
                    for (int i = 0; i < gains.length && flag; i++) {
                        if (gains[i][home] != 0)
                            flag = false;
                    }
                    if (flag) {
                        transferFnLable.setText("Transfer Function :" + sfg.getTransferFn(home, destination));

                    } else {
                        Integer x = Integer.parseInt(sfg.getTransferFn(0, home));
                        x = Integer.parseInt(sfg.getTransferFn(0, destination)) / x;
                        transferFnLable.setText("Transfer Function :" + x);
                       errorsLable.setText("Errors : Tranfer function done in two Phases ");
                    }
                }
            }
        } catch (Exception e) {
            errorsLable.setText("Errors : only Integers is valid");
        }
        repaint();

    }

    private void println(Object o) {
        // System.out.println(o);

    }

    private void ShowDetails() {
        JTextArea results = new JTextArea();
        JPanel p = new JPanel();

        results.setText(sfg.resuts);
        results.setEditable(false);
        p.add(results);
        int result = JOptionPane.showConfirmDialog(null, p, "Results", JOptionPane.CLOSED_OPTION);

        repaint();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (flagAddNode)
            addNode(e);

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        Gui2 g = new Gui2();
        g.frame.setVisible(true);

    }

}
