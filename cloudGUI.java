
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class cloudGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cloudGUI frame = new cloudGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public cloudGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLoadMyEngine = new JLabel("Load My Engine");
		lblLoadMyEngine.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoadMyEngine.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblLoadMyEngine.setBackground(Color.MAGENTA);
		lblLoadMyEngine.setBounds(293, 31, 138, 51);
		JFileChooser fileChooser = new JFileChooser("Choose Files");
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setBounds(23,321,212,104);
		contentPane.add(lblLoadMyEngine);
		
		DefaultListModel<File> listModel;
		listModel = new DefaultListModel<File>();
		JList list = new JList(listModel);
		list.setBackground(Color.WHITE);
		list.setBounds(144, 147, 448, 148);
		contentPane.add(list);
		
		JButton btnChooseFiles = new JButton("Choose Files");
		btnChooseFiles.setBounds(293, 93, 138, 43);
		contentPane.add(btnChooseFiles);
		
		JLabel lblConnorKalinasSearch = new JLabel("Connor Kalina's Search Engine");
		lblConnorKalinasSearch.setBounds(10, 11, 157, 35);
		contentPane.add(lblConnorKalinasSearch);
        btnChooseFiles.addActionListener(e -> {
            File[] f = selectFile();
            for(int i = 0; i < f.length; i++)
            {
            	listModel.addElement(f[i]);
            }
        });
	}

    public File[] selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setBounds(23,321,212,104);
        // optionally set chooser options ...
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] f = fileChooser.getSelectedFiles();
            return(f);
            // read  and/or display the file somehow. ....
        } else {
        	return null;
            // user changed their mind
        }
    }
}
