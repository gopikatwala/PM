package gov.nist.csd.pm.application.medrec.editors;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gov.nist.csd.pm.common.application.SysCaller;
import gov.nist.csd.pm.common.application.SysCallerImpl;
import gov.nist.csd.pm.common.net.Packet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gov.nist.csd.pm.common.util.Generators.generateRandomName;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/**
 * 
 * @author joshua.roberts@nist.gov
 *
 */
public class HistoryEditor extends JFrame implements ActionListener{

	private List<String> newResults = new ArrayList<String>();
	private ArrayList<String> found = new ArrayList<String>();

	private SysCaller syscaller;
	private SysCallerImpl syscallerimpl;

	private String sSessionId;
	private String sProcessId;

	private String sKstorePath;
	private String sTstorePath;
	private String sRtfPath;
	private String sWkfPath;
	private String sEmlPath;
	private String sOffPath;

	private JRadioButton butM;
	private JRadioButton butF;
	protected JEditorPane myEditorPane;
	public JTextField mrnField;
	private JTextField ssnField;
	private JTextField firstField;
	private JTextField midField;
	private JTextField lastField;
	private JTextField dobField;
	private JTextField dateField;
	private JButton btnSubmit, btnSave, btnDone;
	private JButton btnOpenSelect;
	private JTextField doctField;
	private JList list;
	private JRadioButton rdbtnHistory;
	private JRadioButton rdbtnDrafts;
	private JList resultList;
	private JTextField textFromMonth;
	private JTextField textToMonth;
	private JLabel resultsLabel;
	private JButton btnReadAll;
	private DefaultListModel resultListModel;

	static String sessid;
	static String pid;
	static int simport;
	static String recname;
	static boolean debug;


	public HistoryEditor(int nSimPort, String sSessId, String sProcId, boolean bDebug) {
		setTitle("History Editor");

		setBounds(100, 100, 450, 674);
		setSize(450, 842);

		this.sSessionId = sSessId;
		this.sProcessId = sProcId;

		syscaller = new SysCallerImpl(nSimPort, sSessId, sProcId, bDebug, "History Editor");

		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		JLabel lblDate = new JLabel("Date:");
		getContentPane().add(lblDate);

		dateField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, dateField, 130, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, dateField, -130, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblDate, 3, SpringLayout.NORTH, dateField);
		springLayout.putConstraint(SpringLayout.EAST, lblDate, -6, SpringLayout.WEST, dateField);
		getContentPane().add(dateField);
		dateField.setEditable(false);
		dateField.setColumns(22);

		JLabel lblMrn = new JLabel("HN:");
		springLayout.putConstraint(SpringLayout.NORTH, lblMrn, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblMrn, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblMrn);

		mrnField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, mrnField, 7, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, mrnField, 6, SpringLayout.EAST, lblMrn);
		springLayout.putConstraint(SpringLayout.NORTH, dateField, 6, SpringLayout.SOUTH, mrnField);
		getContentPane().add(mrnField);
		mrnField.setEditable(false);
		mrnField.setColumns(10);

		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 3, SpringLayout.SOUTH, dateField);
		springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, 432, SpringLayout.WEST, getContentPane());
		getContentPane().add(panel);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),
				BorderFactory.createTitledBorder("Patient Identification:")));
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		JLabel lblFirstName = new JLabel("First Name:");
		panel.add(lblFirstName);

		firstField = new JTextField();
		firstField.setEditable(false);
		sl_panel.putConstraint(SpringLayout.NORTH, firstField, 20, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblFirstName, 0, SpringLayout.WEST, firstField);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblFirstName, -6, SpringLayout.NORTH, firstField);
		panel.add(firstField);
		firstField.setColumns(10);

		JLabel lblMi = new JLabel("MI:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblMi, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblMi, 190, SpringLayout.WEST, panel);
		panel.add(lblMi);

		midField = new JTextField();
		midField.setEditable(false);
		sl_panel.putConstraint(SpringLayout.NORTH, midField, 6, SpringLayout.SOUTH, lblMi);
		sl_panel.putConstraint(SpringLayout.WEST, midField, 34, SpringLayout.EAST, firstField);
		panel.add(midField);
		midField.setColumns(10);

		JLabel lblLastName = new JLabel("Last Name:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblLastName, 0, SpringLayout.NORTH, panel);
		panel.add(lblLastName);

		lastField = new JTextField();
		lastField.setEditable(false);
		sl_panel.putConstraint(SpringLayout.NORTH, lastField, 6, SpringLayout.SOUTH, lblLastName);
		sl_panel.putConstraint(SpringLayout.WEST, lastField, 256, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, midField, -30, SpringLayout.WEST, lastField);
		sl_panel.putConstraint(SpringLayout.WEST, lblLastName, 0, SpringLayout.WEST, lastField);
		panel.add(lastField);
		lastField.setColumns(10);

		JLabel lblSsn = new JLabel("SSN:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblSsn, 6, SpringLayout.SOUTH, firstField);
		sl_panel.putConstraint(SpringLayout.WEST, firstField, 0, SpringLayout.WEST, lblSsn);
		panel.add(lblSsn);

		ssnField = new JTextField();
		ssnField.setEditable(false);
		sl_panel.putConstraint(SpringLayout.WEST, lblSsn, 0, SpringLayout.WEST, ssnField);
		panel.add(ssnField);
		ssnField.setColumns(10);

		JLabel lblSex = new JLabel("Sex:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblSex, 0, SpringLayout.NORTH, lblSsn);
		panel.add(lblSex);

		butM = new JRadioButton("M");
		butM.setEnabled(false);
		sl_panel.putConstraint(SpringLayout.NORTH, butM, 1, SpringLayout.SOUTH, lblSex);
		sl_panel.putConstraint(SpringLayout.WEST, lblSex, 0, SpringLayout.WEST, butM);
		sl_panel.putConstraint(SpringLayout.NORTH, ssnField, 1, SpringLayout.NORTH, butM);
		sl_panel.putConstraint(SpringLayout.EAST, ssnField, -17, SpringLayout.WEST, butM);
		sl_panel.putConstraint(SpringLayout.WEST, butM, 173, SpringLayout.WEST, panel);
		butM.setActionCommand("M");
		butM.addActionListener(this);
		panel.add(butM);


		butF = new JRadioButton("F");
		butF.setEnabled(false);
		sl_panel.putConstraint(SpringLayout.NORTH, butF, 21, SpringLayout.SOUTH, midField);
		sl_panel.putConstraint(SpringLayout.WEST, butF, 6, SpringLayout.EAST, butM);
		butF.setActionCommand("F");
		butF.addActionListener(this);
		panel.add(butF);

		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(butM);
		bg1.add(butF);

		JLabel lblDob = new JLabel("DOB:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblDob, 6, SpringLayout.SOUTH, lastField);
		sl_panel.putConstraint(SpringLayout.WEST, lblDob, 0, SpringLayout.WEST, lblLastName);
		panel.add(lblDob);

		dobField = new JTextField();
		dobField.setEditable(false);
		sl_panel.putConstraint(SpringLayout.NORTH, dobField, 1, SpringLayout.SOUTH, lblDob);
		sl_panel.putConstraint(SpringLayout.WEST, dobField, 0, SpringLayout.WEST, lblLastName);
		panel.add(dobField);
		dobField.setColumns(10);

		JPanel panel_1 = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, panel, -6, SpringLayout.NORTH, panel_1);
		springLayout.putConstraint(SpringLayout.SOUTH, panel_1, -411, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel_1, 0, SpringLayout.EAST, panel);
		springLayout.putConstraint(SpringLayout.NORTH, panel_1, 198, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(panel_1);
		panel_1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),
				BorderFactory.createTitledBorder("History:")));
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);

		/*
		btnCancel = new JButton("Cancel");
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnCancel, 5, SpringLayout.SOUTH, treatArea);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnCancel, 0, SpringLayout.WEST, treatArea);
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(this);
		panel_1.add(btnCancel);
		 */

		btnSave = new JButton(getSaveDraftAlt());
		//sl_panel_1.putConstraint(SpringLayout.WEST, btnSave, 82, SpringLayout.EAST, btnCancel);
		btnSave.setActionCommand("Save");
		btnSave.addActionListener(this);
		panel_1.add(btnSave);

		btnSubmit = new JButton(getSubmitToHistory());
		sl_panel_1.putConstraint(SpringLayout.EAST, btnSave, -6, SpringLayout.WEST, btnSubmit);
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnSubmit, 0, SpringLayout.NORTH, btnSave);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnSubmit, 232, SpringLayout.WEST, panel_1);
		btnSubmit.setEnabled(false);
		btnSubmit.setActionCommand("Submit");
		btnSubmit.addActionListener(this);
		btnSubmit.setEnabled(true);
		panel_1.add(btnSubmit);

		JPanel panel_2 = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel_2, 6, SpringLayout.SOUTH, panel_1);
		springLayout.putConstraint(SpringLayout.WEST, panel_2, 0, SpringLayout.WEST, lblMrn);
		springLayout.putConstraint(SpringLayout.EAST, panel_2, 0, SpringLayout.EAST, panel);

		myEditorPane = new JEditorPane();
		sl_panel_1.putConstraint(SpringLayout.EAST, btnSubmit, 0, SpringLayout.EAST, myEditorPane);
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnSave, 6, SpringLayout.SOUTH, myEditorPane);
		sl_panel_1.putConstraint(SpringLayout.NORTH, myEditorPane, 0, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, myEditorPane, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, myEditorPane, 135, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, myEditorPane, 400, SpringLayout.WEST, panel_1);
		panel_1.add(myEditorPane);

		JButton btnNewButton = new JButton("New");
		sl_panel_1.putConstraint(SpringLayout.WEST, btnSave, 7, SpringLayout.EAST, btnNewButton);
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnNewButton, 6, SpringLayout.SOUTH, myEditorPane);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnNewButton, 0, SpringLayout.WEST, myEditorPane);
		panel_1.add(btnNewButton);
		getContentPane().add(panel_2);
		panel_2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),
				BorderFactory.createTitledBorder("Patient Treatment History:")));
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_2.setLayout(sl_panel_2);
		btnNewButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dateField.setText("");
				mrnField.setText("");
				myEditorPane.setText("");

				btnSubmit.setEnabled(true);
				btnSave.setEnabled(true);
			}

		});

		resultListModel = new DefaultListModel();
		resultList = new JList(resultListModel);

		JScrollPane scrollPane = new JScrollPane(resultList);
		sl_panel_2.putConstraint(SpringLayout.NORTH, scrollPane, 124, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, scrollPane, 400, SpringLayout.WEST, panel_2);
		panel_2.add(scrollPane);

		btnDone = new JButton("Done");
		springLayout.putConstraint(SpringLayout.SOUTH, panel_2, -6, SpringLayout.NORTH, btnDone);
		springLayout.putConstraint(SpringLayout.SOUTH, btnDone, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnDone, 0, SpringLayout.EAST, panel);
		btnDone.setActionCommand("Done");
		btnDone.addActionListener(this);
		getContentPane().add(btnDone);

		rdbtnHistory = new JRadioButton("History");
		sl_panel_2.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, rdbtnHistory);
		sl_panel_2.putConstraint(SpringLayout.WEST, rdbtnHistory, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.NORTH, rdbtnHistory, 0, SpringLayout.NORTH, panel_2);
		panel_2.add(rdbtnHistory);

		rdbtnDrafts = new JRadioButton("Drafts");
		sl_panel_2.putConstraint(SpringLayout.WEST, rdbtnDrafts, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.NORTH, rdbtnDrafts, 6, SpringLayout.SOUTH, rdbtnHistory);
		panel_2.add(rdbtnDrafts);

		btnOpenSelect = new JButton(getOpenAction());
		sl_panel_2.putConstraint(SpringLayout.SOUTH, scrollPane, -6, SpringLayout.NORTH, btnOpenSelect);
		btnOpenSelect.setActionCommand("Open");
		btnOpenSelect.addActionListener(this);
		panel_2.add(btnOpenSelect);

		JLabel lblFrom = new JLabel("From:");
		sl_panel_2.putConstraint(SpringLayout.NORTH, lblFrom, 4, SpringLayout.NORTH, rdbtnHistory);
		panel_2.add(lblFrom);

		textFromMonth = new JTextField();
		sl_panel_2.putConstraint(SpringLayout.WEST, btnOpenSelect, 0, SpringLayout.WEST, textFromMonth);
		sl_panel_2.putConstraint(SpringLayout.EAST, lblFrom, -6, SpringLayout.WEST, textFromMonth);
		sl_panel_2.putConstraint(SpringLayout.NORTH, textFromMonth, 1, SpringLayout.NORTH, rdbtnHistory);
		textFromMonth.setText("MM");
		panel_2.add(textFromMonth);
		textFromMonth.setColumns(2);
		textFromMonth.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent m){
				textFromMonth.setText("");
			}
		});

		JLabel lblTo = new JLabel("To:");
		sl_panel_2.putConstraint(SpringLayout.NORTH, lblTo, 4, SpringLayout.NORTH, rdbtnHistory);
		panel_2.add(lblTo);

		textToMonth = new JTextField();
		sl_panel_2.putConstraint(SpringLayout.EAST, lblTo, -6, SpringLayout.WEST, textToMonth);
		sl_panel_2.putConstraint(SpringLayout.NORTH, textToMonth, 1, SpringLayout.NORTH, rdbtnHistory);
		textToMonth.setText("MM");
		panel_2.add(textToMonth);
		textToMonth.setColumns(2);
		textToMonth.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent m){
				textToMonth.setText("");
			}
		});


		textFromDay = new JTextField();
		sl_panel_2.putConstraint(SpringLayout.EAST, textFromMonth, -6, SpringLayout.WEST, textFromDay);
		sl_panel_2.putConstraint(SpringLayout.NORTH, textFromDay, 1, SpringLayout.NORTH, rdbtnHistory);
		textFromDay.setText("DD");
		panel_2.add(textFromDay);
		textFromDay.setColumns(2);
		textFromDay.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent m){
				textFromDay.setText("");
			}
		});

		textToDay = new JTextField();
		sl_panel_2.putConstraint(SpringLayout.EAST, textToMonth, -6, SpringLayout.WEST, textToDay);
		sl_panel_2.putConstraint(SpringLayout.NORTH, textToDay, 1, SpringLayout.NORTH, rdbtnHistory);
		textToDay.setText("DD");
		panel_2.add(textToDay);
		textToDay.setColumns(2);
		textToDay.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent m){
				textToDay.setText("");
			}
		});

		textToYear = new JTextField();
		sl_panel_2.putConstraint(SpringLayout.EAST, textToYear, -10, SpringLayout.EAST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, textToDay, -6, SpringLayout.WEST, textToYear);
		sl_panel_2.putConstraint(SpringLayout.NORTH, textToYear, 1, SpringLayout.NORTH, rdbtnHistory);
		textToYear.setText("YYYY");
		panel_2.add(textToYear);
		textToYear.setColumns(3);
		textToYear.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent m){
				textToYear.setText("");
			}
		});

		textFromYear = new JTextField();
		sl_panel_2.putConstraint(SpringLayout.EAST, textFromDay, -6, SpringLayout.WEST, textFromYear);
		sl_panel_2.putConstraint(SpringLayout.NORTH, textFromYear, 1, SpringLayout.NORTH, rdbtnHistory);
		sl_panel_2.putConstraint(SpringLayout.EAST, textFromYear, -6, SpringLayout.WEST, lblTo);
		textFromYear.setText("YYYY");
		panel_2.add(textFromYear);
		textFromYear.setColumns(3);
		textFromYear.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent m){
				textFromYear.setText("");
			}
		});

		btnReadAll = new JButton("Read All Results ");
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnReadAll, 0, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnReadAll, -10, SpringLayout.EAST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnOpenSelect, 0, SpringLayout.NORTH, btnReadAll);
		panel_2.add(btnReadAll);
		btnReadAll.setEnabled(false);
		btnReadAll.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openAll();

			}

		});

		JButton btnClearSearch = new JButton("Clear Search");
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnClearSearch, 6, SpringLayout.SOUTH, scrollPane);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnClearSearch, 0, SpringLayout.WEST, scrollPane);
		panel_2.add(btnClearSearch);
		btnClearSearch.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resultListModel.clear();

				textFromMonth.setText("MM");
				textFromDay.setText("DD");
				textFromYear.setText("YYYY");

				textToMonth.setText("MM");
				textToDay.setText("DD");
				textToYear.setText("YYYY");

				resultsLabel.setText("");

				rdbtnHistory.setSelected(false);
				rdbtnDrafts.setSelected(false);
				
				btnOpenSelect.setEnabled(false);
				btnReadAll.setEnabled(false);
			}
		});

		resultsLabel = new JLabel("");
		sl_panel_2.putConstraint(SpringLayout.WEST, resultsLabel, 0, SpringLayout.WEST, scrollPane);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, resultsLabel, -6, SpringLayout.NORTH, scrollPane);
		panel_2.add(resultsLabel);

		btnSearch = new JButton("   Search   ");
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnSearch, 0, SpringLayout.NORTH, rdbtnDrafts);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnSearch, -10, SpringLayout.EAST, panel_2);
		btnSearch.setAction(getSearchAction());
		panel_2.add(btnSearch);
		
		JLabel lblor = new JLabel("-or-");
		sl_panel_2.putConstraint(SpringLayout.NORTH, lblor, 6, SpringLayout.SOUTH, btnSearch);
		sl_panel_2.putConstraint(SpringLayout.EAST, lblor, -35, SpringLayout.EAST, panel_2);
		panel_2.add(lblor);
		
		JButton btnViewAllTreatments = new JButton("View All History");
		btnViewAllTreatments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String id1 = syscaller.getIdOfEntityWithNameAndType("History", "b");
				String[] results;
				List<String> newResults = new ArrayList<String>();
				String sType = "b";
				String sGraphType = "ac";
				List<String[]> members = syscaller.getMembersOf("History", id1, sType, sGraphType);
				System.out.println("before getting history");
				for(int i = 0; i < members.size(); i++){
					results = (String[])members.get(i);
					System.out.println("member " + i + ":" + results[0] + ":" + results[1] + ":" + results[2]);
					if(results[0].equals("o") && results[2].length() == 8){
						newResults.add(results[2]);

					}
				}
				ReadAllDocument read = new ReadAllDocument();
				read.setTitle("History");
				for(int i = 0; i < newResults.size(); i++){
					String sObjName = newResults.get(i);
					String sHandle = syscaller.openObject3(sObjName, "File read");
					byte[] buf = syscaller.readObject3(sHandle);
					ByteArrayInputStream bais = new ByteArrayInputStream(buf);
					Properties props = new Properties();

					try {
						System.out.println("about to load " + sObjName);
						props.load(bais);
						System.out.println("loaded properties for " + sObjName);
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("properties did not load for " + sObjName);
						return;
					}
					String tn = props.getProperty("hn");
					String date = props.getProperty("date");
					String doctor = props.getProperty("doctor");
					String patient = props.getProperty("name");
					String treat = props.getProperty("history");

					Scanner sc = new Scanner(treat);
					String fTreat = "";
					while(sc.hasNextLine()){
						String line = sc.nextLine();
						fTreat += line + "\n\t";
					}

					System.out.println("HISTORY    " + treat);

					String treatment = "Date:\t" + date
							+ "\nPatient:\t" + patient
							+ "\nHN:\t" + tn
							+ "\nDoctor:\t" + doctor
							+  "\nHistory:\t" + fTreat
							+ "\n=============================================================\n";
					read.write(treatment);
				}
				read.setVisible(true);
			}
		});
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnViewAllTreatments, 6, SpringLayout.SOUTH, lblor);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnViewAllTreatments, -10, SpringLayout.EAST, panel_2);
		panel_2.add(btnViewAllTreatments);


		JLabel lblDoctor = new JLabel("Doctor:");
		springLayout.putConstraint(SpringLayout.NORTH, lblDoctor, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblDoctor, 238, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, mrnField, -6, SpringLayout.WEST, lblDoctor);
		getContentPane().add(lblDoctor);

		doctField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, doctField, 6, SpringLayout.EAST, lblDoctor);
		springLayout.putConstraint(SpringLayout.EAST, doctField, 0, SpringLayout.EAST, panel);
		doctField.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, doctField, 7, SpringLayout.NORTH, getContentPane());
		getContentPane().add(doctField);
		doctField.setColumns(10);

		Packet res = (Packet) syscaller.getKStorePaths();
		if (res.hasError()) {
			JOptionPane.showMessageDialog(this, res.getErrorMessage());
			return;
		}
		sKstorePath = res.getStringValue(0);
		sTstorePath = res.getStringValue(1);
		System.out.println("Kstore path = " + sKstorePath);
		System.out.println("Tstore path = " + sTstorePath);

		sRtfPath = syscaller.getAppPath("Rich Text Editor")[0];
		sWkfPath = syscaller.getAppPath("Workflow Editor")[0];
		sEmlPath = syscaller.getAppPath("e-grant")[0];
		sOffPath = syscaller.getAppPath("Open Office")[0];

		System.out.println("RTF path = " + sRtfPath);
		System.out.println("Wkf path = " + sWkfPath);
		System.out.println("Eml path = " + sEmlPath);
		System.out.println("Off path = " + sOffPath);
	}

	public String getDateAndTime() {
		String d = "";

		Date date = new Date();

		d = date.toString();
		dateField.setText(d);

		return d;
	}

	public void setProps(String first, String mi, String last,
			String ssn, String cmd, String dob, String doc){
		//mrnField.setText(mrn);
		firstField.setText(first);
		midField.setText(mi);
		lastField.setText(last);
		ssnField.setText(ssn);
		if(cmd.equals("M")){
			butM.setSelected(true);
		}else{
			butF.setSelected(true);
		}
		dobField.setText(dob);
		doctField.setText(doc);
	}

	public void openAll() {
		System.out.println("openAll called()()()()");
		for(int i = 0; i < found.size(); i++){
			System.out.println("found: " + i + " : " + found.get(i));
		}
		ReadAllDocument reader = new ReadAllDocument();
		if(rdbtnHistory.isSelected()){
			reader.setTitle("History");
			for(int j = 0; j < found.size(); j++){
				System.out.println("found: " + found.get(j));
				String sObjName = found.get(j);
				String sHandle = syscaller.openObject3(sObjName, "File read");
				byte[] buf = syscaller.readObject3(sHandle);
				ByteArrayInputStream bais = new ByteArrayInputStream(buf);
				Properties props = new Properties();

				try {
					System.out.println("about to load " + sObjName);
					props.load(bais);
					System.out.println("loaded properties for " + sObjName);
				}catch(Exception ex){
					ex.printStackTrace();
					System.out.println("properties did not load for " + sObjName);
					return;
				}
				String hn = props.getProperty("hn");
				String date = props.getProperty("date");
				String doctor = props.getProperty("doctor");
				String patient = props.getProperty("name");
				String hist = props.getProperty("history");
				
				Scanner sc = new Scanner(hist);
				String fHist = "";
				while(sc.hasNextLine()){
					String line = sc.nextLine();
					fHist += line + "\n\t";
				}
				
				System.out.println("History    " + fHist);

				String history = "Date:\t" + date
						+ "\nPatient:\t" + patient
						+ "\nHN:\t" + hn
						+ "\nDoctor:\t" + doctor
						+  "\nHistory:\t" + fHist
						+ "\n=============================================================\n";			
				reader.write(history);
			}
			reader.setVisible(true);
		}else if(rdbtnDrafts.isSelected()){
			reader.setTitle("History Drafts");
			for(int j = 0; j < found.size(); j++){
				System.out.println("found draft: " + found.get(j));
				String sObjName = found.get(j);
				String sHandle = syscaller.openObject3(sObjName, "File read");
				byte[] buf = syscaller.readObject3(sHandle);
				ByteArrayInputStream bais = new ByteArrayInputStream(buf);
				Properties props = new Properties();
				if(sObjName.substring(8).equals("Draft")){
					try {
						System.out.println("about to load " + sObjName);
						props.load(bais);
						System.out.println("loaded properties for " + sObjName);
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("properties did not load for " + sObjName);
						return;
					}
					String hn = props.getProperty("hn");
					String date = props.getProperty("date");
					String doctor = props.getProperty("doctor");
					String patient = props.getProperty("name");
					String hist = props.getProperty("history");
					
					Scanner sc = new Scanner(hist);
					String fHist = "";
					while(sc.hasNextLine()){
						String line = sc.nextLine();
						fHist += line + "\n\t";
					}

					String history = "Date:\t" + date
							+ "\nPatient:\t" + patient
							+ "\nHN:\t" + hn
							+ "\nDoctor:\t" + doctor
							+  "\nHistory:\t" + fHist
							+ "\n=============================================================\n";			
					reader.write(history);
				}
			}
			reader.setVisible(true);
		}
	}

	public void actionPerformed(ActionEvent evt){
		String cmd = evt.getActionCommand();

		if(cmd.equalsIgnoreCase("done")) {
			syscaller.exitProcess(sProcessId);
			setVisible(false);
			/*
		}else if(cmd.equalsIgnoreCase("reset")){
			setProps(null, null, null, null, null, null, null);
			 */
		}
	}

	/**
	 * Launch the application.
	 */
	public static void createGUI(){
		HistoryEditor draft = new HistoryEditor(simport, sessid, pid, debug);
		draft.pack();
		draft.setVisible(true);
	}

	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-session")) {
				sessid = args[++i];
			} else if (args[i].equals("-process")) {
				pid = args[++i];
			} else if (args[i].equals("-simport")) {
				simport = Integer.valueOf(args[++i]).intValue();
			} else if (args[i].equals("-debug")) {
				debug = true;
			} else {
				recname = args[i];
			}
		}
		if (sessid == null) {
			System.out.println("This application must run within a Policy Machine session!");
			System.exit(-1);
		}
		if (pid == null) {
			System.out.println("This application must run in a Policy Machine process!");
			System.exit(-1);
		}

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createGUI();
			}
		});
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	public Action getSaveDraftAlt(){
		return saveDraftAlt;
	}

	private Action saveDraftAlt = new SaveDraftAlt();
	class SaveDraftAlt extends AbstractAction {
		public SaveDraftAlt(){
			super("Save As Draft");
		}
		public void actionPerformed(ActionEvent e){
			dateField.setText(getDateAndTime());
			//System.out.println("saveDraftAlt() called");
			String doctor = doctField.getText();
			String sssn = ssnField.getText();
			String slast = firstField.getText() + " " + midField.getText() + " " + lastField.getText();
			String sdate = dateField.getText();
			String sdob = dobField.getText();
			String sex = "";
			if(butM.isSelected()){
				sex = "M";
			}else{
				sex = "F";
			}

			String sContainers = "b|PatHistoryDrafts,b|DraftHistory";
			String sObjClass = "File";
			String sObjType = "rtf";
			String sPerms = "File write";


			if (mrnField.getText().length() == 0) {
				
				String shist = myEditorPane.getText();
				if(shist.length() == 0){
					JOptionPane.showMessageDialog(HistoryEditor.this, "Write something to add to history");
					return;
				}
				
				mrnField.setText(generateRandomName(4) + "Draft");
				String smrn = mrnField.getText();
				String sObjName = smrn.substring(0, 8) + "Draft";

				//System.out.println("sObjName: " + sObjName);
				//System.out.println("Containers: " + sContainers);
				//System.out.println("Object name: " + sObjName);

				Scanner sc = new Scanner(shist);
				String fHist = "";
				String line = sc.nextLine();
				fHist += line + "\\par";
				while(sc.hasNextLine()){
					line = sc.nextLine();
					fHist += "\t\t" + line + "\\par";
				}
				
				//create object
				String sHandle = syscaller.createObject3(sObjName, sObjClass,
						sObjType, sContainers, sPerms, null, null, null, null);
				if (sHandle == null) {
					JOptionPane.showMessageDialog(HistoryEditor.this,
							syscaller.getLastError());
					System.out.println("sHandle was null");
					return;
				}
				System.out.println("sHandle is not null");

				//Set up properties
				Properties props = new Properties();

				props.put("date", sdate);
				props.put("hn", sObjName);
				props.put("ssn", sssn);
				props.put("name", slast);
				props.put("history", shist);
				props.put("doctor", doctor);
				props.put("sex", sex);
				props.put("dob", sdob);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintWriter pw = new PrintWriter(baos, true);
				pw.print("{\\rtf1\\ansi{\\fonttbl\\f0\\fnil Monospaced;}" //\\li0\\ri0\\fi0\\i0\\b0\\ul0\\cf0" 
						+ "\nDRAFT HISTORY\\par");
				pw.print("\\par date:\t" + sdate);
				pw.print("\\par hn:\t" + sObjName);
				pw.print("\\par name:\t" + slast);
				pw.print("\\par ssn:\t" + sssn);
				pw.print("\\par dob:\t" + sdob);
				pw.print("\\par sex:\t" + sex);
				pw.print("\\par doctor:\t" + doctor);
				pw.print("\\par History:\t" + fHist);
				pw.print("\\par ==============================properties==============================");
				pw.close();

				try {
					props.store(baos, "");

					byte[] buf = baos.toByteArray();
					int len = syscaller.writeObject3(sHandle, buf);
					if (len < 0) {
						JOptionPane.showMessageDialog(HistoryEditor.this,
								syscaller.getLastError());
						return;
					}
					syscaller.closeObject(sHandle);
					JOptionPane.showMessageDialog(HistoryEditor.this,
							"Save successful!");

				} catch (Exception ex) {
					System.out.println("writing the contents failed");
					ex.printStackTrace();

				}
				btnSave.setEnabled(false);
			}else{
				String sObjName = mrnField.getText().substring(0, 8) + "Draft";
				System.out.println("sObjName: " + sObjName);
				System.out.println("Containers: " + sContainers);
				System.out.println("Object name: " + sObjName);
				String shist = myEditorPane.getText();
				if(shist.length() == 0){
					JOptionPane.showMessageDialog(HistoryEditor.this, "Fill History field");
					return;
				}
				
				Scanner sc = new Scanner(shist);
				String fHist = "";
				String line = sc.nextLine();
				fHist += line + "\\par";
				while(sc.hasNextLine()){
					line = sc.nextLine();
					fHist += "\t\t" + line + "\\par";
				}

				System.out.println(sObjName + sObjClass + sObjType
						+ sContainers + sPerms + null + null + null + null);

				String sHandle = syscaller.openObject3(sObjName, "File read");
				if (sHandle == null) {
					JOptionPane.showMessageDialog(HistoryEditor.this,
							syscaller.getLastError());
					System.out.println("sHandle was null");
					return;
				}

				Properties props = new Properties();

				props.put(" date", sdate);
				props.put(" hn", sObjName);
				props.put(" ssn", sssn);
				props.put(" name", slast);
				props.put(" history", shist);
				props.put(" doctor", doctor);
				props.put(" sex", sex);
				props.put(" dob", sdob);


				for (Enumeration propEnum = props.propertyNames(); propEnum.hasMoreElements();) {
					String sName = (String) propEnum.nextElement();
					System.out.println(sName + "=" + (String) props.get(sName));
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintWriter pw = new PrintWriter(baos, true);
				pw.print("{\\rtf1\\ansi{\\fonttbl\\f0\\fnil Monospaced;}" //\\li0\\ri0\\fi0\\i0\\b0\\ul0\\cf0" 
						+ "\nDRAFT HISTORY\\par");
				pw.print("\\par date:\t" + sdate);
				pw.print("\\par hn:\t" + sObjName);
				pw.print("\\par name:\t" + slast);
				pw.print("\\par ssn:\t" + sssn);
				pw.print("\\par dob:\t" + sdob);
				pw.print("\\par sex:\t" + sex);
				pw.print("\\par doctor:\t" + doctor);
				pw.print("\\par history:\t" + fHist);
				pw.print("\\par ==============================properties==============================");
				pw.close();


				try {
					props.store(baos, "");

					byte[] buf = baos.toByteArray();
					int len = syscaller.writeObject3(sHandle, buf);
					if (len < 0) {
						JOptionPane.showMessageDialog(HistoryEditor.this,
								syscaller.getLastError());
						return;
					}
					syscaller.closeObject(sHandle);

					JOptionPane.showMessageDialog(HistoryEditor.this,
							"Save successful!");
				} catch (Exception ex) {
					ex.printStackTrace();

				}
			}
		}
	}


	//////////////////////////////////////SUBMIT///////////////////////////////////////////////////
	public Action getSubmitToHistory(){
		return submitHist;
	}

	private Action submitHist = new SubmitToHistory();

	class SubmitToHistory extends AbstractAction{
		public SubmitToHistory(){
			super("Submit to History");

		}
		public void actionPerformed(ActionEvent e){
			dateField.setText(getDateAndTime());
			String sObjName = mrnField.getText();
			String sssn = ssnField.getText();
			String slast = firstField.getText() + " " + midField.getText() + " " + lastField.getText();
			String sdate = dateField.getText();
			String doctor = doctField.getText();
			String sdob = dobField.getText();

			String sex = "";

			if(butM.isSelected()){
				sex = "M";
			}else{
				sex = "F";
			}

			Properties props = new Properties();

			//If this history has not been saved to a draft before
			if(sObjName.length() == 0){
		
				String sHist = myEditorPane.getText();
				if(sHist.length() == 0){
					JOptionPane.showMessageDialog(HistoryEditor.this, "Fill History field");
					return;
				}
				
				sObjName = generateRandomName(4);
				mrnField.setText(sObjName);
				sObjName = mrnField.getText();
				
				Scanner sc = new Scanner(sHist);
				String fHist = "";
				String line = sc.nextLine();
				fHist += line + "\\par";
				while(sc.hasNextLine()){
					line = sc.nextLine();
					fHist += "\t\t" + line + "\\par";
				}


				String sContainers = "b|PatHistory,b|History";

				System.out.println("sObjName: " + sObjName);

				System.out.println("Containers: " + sContainers);
				System.out.println("Object name: " + sObjName);

				String sObjClass = "File";
				String sObjType = "rtf";
				String sPerms = "File read";

				String sHandle = syscaller.createObject3(sObjName, sObjClass, sObjType, 
						sContainers, sPerms, null, null, null, null);

				if(sHandle == null){
					JOptionPane.showMessageDialog(HistoryEditor.this, syscaller.getLastError());
					System.out.println("sHandle was null");
					return;
				}

				props.put("date", sdate);
				props.put("hn", sObjName);
				props.put("ssn", sssn);
				props.put("name", slast);
				props.put("history", sHist);
				props.put("doctor", doctor);
				props.put("sex", sex);
				props.put("dob", sdob);


				for (Enumeration propEnum = props.propertyNames(); propEnum.hasMoreElements();) {
					String sName = (String) propEnum.nextElement();
					System.out.println(sName + "=" + (String) props.get(sName));
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintWriter pw = new PrintWriter(baos, true);
				pw.print("{\\rtf1\\ansi{\\fonttbl\\f0\\fnil Monospaced;}" //\\li0\\ri0\\fi0\\i0\\b0\\ul0\\cf0" 
						+ "\nSUBMITTED HISTORY\\par");
				pw.print("\\par date:\t" + sdate);
				pw.print("\\par hn:\t" + sObjName);
				pw.print("\\par name:\t" + slast);
				pw.print("\\par ssn:\t" + sssn);
				pw.print("\\par dob:\t" + sdob);
				pw.print("\\par sex:\t" + sex);
				pw.print("\\par doctor:\t" + doctor);
				pw.print("\\par history:\t" + fHist);
				pw.print("\\par ==============================properties==============================");
				pw.close();


				try {
					props.store(baos, "");
					byte[] buf = baos.toByteArray();
					int len = syscaller.writeObject3(sHandle, buf);
					if(len < 0){
						JOptionPane.showMessageDialog(HistoryEditor.this, syscaller.getLastError());
						return;
					}
					syscaller.closeObject(sHandle);

				}catch(Exception ex){
					ex.printStackTrace();
					return;
				}
				//if the history has been saved as a draft and already has a history number	
			}else if(sObjName.length() > 0){
				System.out.println("actionPerformed sObjName = " + sObjName);

				sObjName = sObjName.substring(0, 8) + "Draft";
				sdate = getDateAndTime();
				doctor = doctField.getText();
				String sProp1 = "PatHistoryDrafts";
				String sContainer1 = "PatHistory";
				System.out.println(sContainer1 + " " + sProp1);
				String sContainer2 = "History";
				String sProp2 = "DraftHistory";
				String newHist = myEditorPane.getText();
				if(newHist.length() == 0){
					JOptionPane.showMessageDialog(HistoryEditor.this, "Fill History field");
					return;
				}
				
				Scanner sc = new Scanner(newHist);
				String fHist = "";
				String line = sc.nextLine();
				fHist += line + "\\par";
				while(sc.hasNextLine()){
					line = sc.nextLine();
					fHist += "\t\t" + line + "\\par";
				}


				/*boolean cmd = syscaller.deleteAssignment(sObjName, "Drafts", "o");
				System.out.println("deleting " + sObjName + " from " + sProp1);
				if(!cmd){
					System.out.println("DELETEASSIGNEMNT dihn't work 1");
					return;
				}*/


				boolean cmd = syscaller.deassignObjFromOattr3(sObjName, sProp1);
				System.out.println("deassigning " + sObjName + " from " + sProp1);
				if(!cmd){
					System.out.println("deassignObjFromOattr dihn't work 1");
					return;
				}
				cmd = syscaller.deassignObjFromOattr3(sObjName, sProp2);
				System.out.println("deassigning " + sObjName + " from " + sProp2);
				if(!cmd){
					System.out.println("deassignObjFromOattr dihn't work 2");
					return;
				}


				sObjName = sObjName.substring(0, 8);
				String sHandle = syscaller.createObject3(sObjName, "File", "rtf",
						"b|PatHistory,b|History", "File read", null, null, null, null);
				System.out.println(sHandle);
				if(sHandle == null){
					JOptionPane.showMessageDialog(HistoryEditor.this, syscaller.getLastError());
					return;
				}

				props.put("date", sdate);
				props.put("hn", sObjName);
				props.put("ssn", sssn);
				props.put("name", slast);
				props.put("history", newHist);
				props.put("doctor", doctor);
				props.put("sex", sex);
				props.put("dob", sdob);


				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintWriter pw = new PrintWriter(baos, true);
				pw.print("{\\rtf1\\ansi{\\fonttbl\\f0\\fnil Monospaced;}" //\\li0\\ri0\\fi0\\i0\\b0\\ul0\\cf0" 
						+ "\nSUBMITTED HISTORY\\par");
				pw.print("\\par date:\t" + sdate);
				pw.print("\\par hn:\t" + sObjName);
				pw.print("\\par name:\t" + slast);
				pw.print("\\par ssn:\t" + sssn);
				pw.print("\\par dob:\t" + sdob);
				pw.print("\\par sex:\t" + sex);
				pw.print("\\par doctor:\t" + doctor);
				pw.print("\\par history:\t" + fHist);
				pw.print("\\par ==============================properties==============================");
				pw.close();

				try {
					props.store(baos, "");					
					byte[] buf = baos.toByteArray();
					int ret = syscaller.writeObject3(sHandle, buf);
					if (ret < 0) {
						JOptionPane.showMessageDialog(HistoryEditor.this,
								syscaller.getLastError());
						return;
					}
					syscaller.closeObject(sHandle);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(HistoryEditor.this,
							"Exception while saving object: " + ex.getMessage());
					return;
				}


			}
			btnSubmit.setEnabled(false);
			JOptionPane.showMessageDialog(HistoryEditor.this, "Submit Sucessful!");
			btnSave.setEnabled(false);
			if(rdbtnDrafts.isSelected()){
				((SearchAction) searchAction).search();				
			}

		}
	}
	//////////////////////////////////////////////////////SEARCH/////////////////////////////////////////////
	public Action getSearchAction(){
		return searchAction;
	}

	private Action searchAction = new SearchAction();

	class SearchAction extends AbstractAction{

		public SearchAction(){
			super("Search");
		}
		public void search(){
			found.clear();
			System.out.println("search() called");
			resultListModel.clear();
			newResults.clear();
			String sLast = firstField.getText() + " " + midField.getText() + " " + lastField.getText();
			String sel = "";
			String[] results = null;
			if(rdbtnHistory.isSelected()){
				sel = "history";
			}else{
				sel = "drafts";
			}
			System.out.println(sel);

			if(rdbtnHistory.isSelected() && rdbtnDrafts.isSelected()){
				JOptionPane.showMessageDialog(HistoryEditor.this, "Only select one");
				return;
			}else if(!rdbtnHistory.isSelected() && !rdbtnDrafts.isSelected()){
				JOptionPane.showMessageDialog(HistoryEditor.this, "Please select Drafts or History");
				return;
			}

			if(sel.length() == 0){
				JOptionPane.showMessageDialog(HistoryEditor.this, "Please select one");
				return;

			}
			if(sel.equals("history")){
				String id1 = syscaller.getIdOfEntityWithNameAndType("History", "b");

				String sType = "b";
				String sGraphType = "ac";
				List<String[]> members = syscaller.getMembersOf("History", id1, sType, sGraphType);
				System.out.println("before getting History");
				for(int i = 0; i < members.size(); i++){
					results = (String[])members.get(i);
					System.out.println("member " + i + ":" + results[0] + ":" + results[1] + ":" + results[2]);
					if(results[0].equals("o") && results[2].length() == 8){
						newResults.add(results[2]);

					}
				}
				System.out.println("getting History");
			}else{
				System.out.println("getting drafts");
				String id1 = syscaller.getIdOfEntityWithNameAndType("DraftHistory", "b");

				String sType = "b";
				String sGraphType = "ac";
				List<String[]> members = syscaller.getMembersOf("DraftHistory", id1, sType, sGraphType);

				for(int i = 0; i < members.size(); i++){
					results = (String[])members.get(i);
					System.out.println("member " + i + ":" + results[0] + ":" + results[1] + ":" + results[2]);
					if(results[0].equals("o") && results[2].length() == 13){
						newResults.add(results[2]);
					}
				}
				System.out.println("got drafts");
			}
			System.out.println("results have been set");
			System.out.println(newResults.size());
			try{
				int counter = 0;
				for(int i = 0; i < newResults.size(); i++){
					String res = newResults.get(i);
					System.out.println(res);
					String sHandle = syscaller.openObject3(res, "File read");
					System.out.println(sHandle);
					byte[] buf = syscaller.readObject3(sHandle);
					ByteArrayInputStream bais = new ByteArrayInputStream(buf);
					Properties props = new Properties();

					try {
						System.out.println("about to load " + res);
						props.load(bais);
						System.out.println("loaded properties for " + res);
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("properties did not load for " + res);
						return;
					}
					System.out.println("about to get patient");
					String patient = props.getProperty("name");
					System.out.println("got patient " + patient);
					System.out.println(sLast);


					if(patient.equals(sLast)){//firstField.getText() + " " + midField.getText() + " " + lastField.getText())){
						String date = props.getProperty("date");
						int year = Integer.valueOf(date.substring(24));
						int day = Integer.valueOf(date.substring(8, 10));
						String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
						String mon = date.substring(4, 7);
						int month = Integer.valueOf(Arrays.asList(months).indexOf(mon))+1;
						String histDate =  month + "/" + day + "/" + year;
						System.out.println("History date: " + histDate);

						int fromMonth = Integer.valueOf(textFromMonth.getText()); 
						int fromDay = Integer.valueOf(textFromDay.getText());
						int fromYear = Integer.valueOf(textFromYear.getText());
						String fromDate =   fromMonth + "/" + fromDay + "/" + fromYear;
						System.out.println("From: " + fromDate);

						int toMonth = Integer.valueOf(textToMonth.getText()); 
						int toDay = Integer.valueOf(textToDay.getText());
						int toYear = Integer.valueOf(textToYear.getText());
						String toDate =  toMonth + "/" + toDay + "/" + toYear;
						System.out.println("To: " + toDate);

						DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
						Date dDate = format.parse(histDate);
						Date dFrom = format.parse(fromDate);
						Date dTo = format.parse(toDate);

						System.out.println(dFrom + " " + dDate + " " + dTo);

						if((dDate.compareTo(dFrom) >= 0) && (dDate.compareTo(dTo) <= 0)){
							counter++;
							found.add(res);
							resultListModel.addElement(date);
							System.out.println("result list " + i + " = " + date);
						}
					}					
				}
				resultsLabel.setText("Search found " + counter + " results:");
			}catch(Exception ex){
				System.out.println("something went wrong in searchAction");
				ex.printStackTrace();
			}
			if(found.size() == 0){
				btnReadAll.setEnabled(false);
				return;
			}else{
				btnReadAll.setEnabled(true);
			}
		}
		public void actionPerformed(ActionEvent e){
			Object cmd = e.getSource();
			System.out.println(cmd);
			if(cmd.equals(btnSearch)){
				search();
			}
		}
	}
	public Action getOpenAction(){
		return openAction;
	}

	private Action openAction = new OpenAction();
	private JTextField textFromDay;
	private JTextField textToDay;
	private JTextField textToYear;
	private JTextField textFromYear;
	private JButton btnSearch;

	class OpenAction extends AbstractAction{	

		public OpenAction(){
			super("Open");
		}

		public void select(){
			String sObjName = ""; 

			int nSelIx = resultList.getSelectedIndex();
			if(nSelIx < 0){
				JOptionPane.showMessageDialog(HistoryEditor.this, "Select a result");
				return;
			}

			String selectObj = (String) resultListModel.get(nSelIx);
			System.out.println("SELECTED " + selectObj);
			String sel = "";
			if(rdbtnHistory.isSelected()){
				sel = "history";
			}else{
				sel = "drafts";
			}
			System.out.println(sel);

			Properties props = new Properties();
			for(int i = 0; i < newResults.size(); i++){
				System.out.println(newResults.get(i));
			}

			for(int i = 0; i < newResults.size(); i++){
				String res = newResults.get(i);
				System.out.println(res);
				String sHandle = syscaller.openObject3(res, "File read");
				System.out.println(sHandle);
				byte[] buf = syscaller.readObject3(sHandle);
				ByteArrayInputStream bais = new ByteArrayInputStream(buf);

				try {
					System.out.println("about to load " + res);
					props.load(bais);
					System.out.println("loaded properties for " + res);
				}catch(Exception ex){
					ex.printStackTrace();
					System.out.println("properties did not load for " + res);
					return;
				}
				String date = props.getProperty("date");
				System.out.println(date);
				if(date.equals(selectObj)){
					sObjName = props.getProperty("hn");
					break;
				}
			}
			System.out.println("OBJNAME: " + sObjName);

			for (Enumeration propEnum = props.propertyNames(); propEnum.hasMoreElements();) {
				String sName = (String) propEnum.nextElement();
				System.out.println(sName + "=" + (String) props.get(sName));
			}

			String[] name = props.getProperty("name").split(" ");

			mrnField.setText(props.getProperty("hn"));
			dateField.setText(props.getProperty("date"));
			dobField.setText(props.getProperty("dob"));
			firstField.setText(name[0]);
			midField.setText(name[1]);
			lastField.setText(name[2]);
			ssnField.setText(props.getProperty("ssn"));
			if(props.getProperty("sex").equalsIgnoreCase("M")){
				butM.setSelected(true);
			}else{
				butF.setSelected(true);
			}
			dobField.setText(props.getProperty("dob"));
			doctField.setText(props.getProperty("doctor"));
			myEditorPane.setText(props.getProperty("history"));
			//resultListModel.removeAllElements();
			//newResults.clear();
			/*
			if(rdbtnDrafts.isSelected()){
				btnSubmit.setEnabled(true);
				btnSave.setEnabled(false);
			}else{
				btnSubmit.setEnabled(false);
			}
			*/
			btnOpenSelect.setEnabled(true);
			
			if(sel.equals("history")){
				btnSubmit.setEnabled(false);
				btnSave.setEnabled(false);
			}else{
				System.out.println(sel);
				btnSubmit.setEnabled(true);
				btnSave.setEnabled(false);
			}
		}

		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			if(cmd.equalsIgnoreCase("open")){
				select();
			}
		}
	}

	class LauncherThread extends Thread {

		String sPrefix;
		String cmd;
		Process proc;

		LauncherThread(String cmd, String sPrefix) {
			this.sPrefix = sPrefix;
			this.cmd = cmd;
		}

		public Process getProcess() {
			return proc;
		}

		/*
		 * public void destroy() { proc.destroy(); }
		 */
		@Override
		@SuppressWarnings("CallToThreadDumpStack")
		public void run() {
			Runtime rt = Runtime.getRuntime();
			try {
				proc = rt.exec(cmd);
				StreamGobbler errGobbler = new StreamGobbler(
						proc.getErrorStream(), sPrefix);
				StreamGobbler outGobbler = new StreamGobbler(
						proc.getInputStream(), sPrefix);
				errGobbler.start();
				outGobbler.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class StreamGobbler extends Thread {

		InputStream is;
		String sPrefix;

		StreamGobbler(InputStream is, String sPrefix) {
			this.sPrefix = sPrefix;
			this.is = is;
		}

		@Override
		@SuppressWarnings("CallToThreadDumpStack")
		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.println(sPrefix + line);
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.out.println(sPrefix + ioe.getMessage());
			}
		}
	}
}





