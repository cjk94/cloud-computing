import java.awt.*;
import java.io.*;
import java.util.Collections;
import java.io.FileInputStream;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Blob;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.google.api.services.dataproc.model.SubmitJobRequest;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.dataproc.*;
import com.google.api.services.dataproc.model.HadoopJob;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.cloud.storage.Storage.*;
import com.google.api.services.storage.model.StorageObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.paging.Page;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class gui extends JFrame {

	private JPanel contentPane;
	private JTextField txtEnterSearchTerm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui frame = new gui();
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
	public gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 680);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JCheckBox chckbxShakespeare = new JCheckBox("Shakespeare");
		chckbxShakespeare.setBounds(16, 35, 140, 23);
		contentPane.add(chckbxShakespeare);
		
		JCheckBox chckbxHugo = new JCheckBox("Hugo");
		chckbxHugo.setBounds(16, 61, 97, 23);
		contentPane.add(chckbxHugo);
		
		JCheckBox chckbxTolstoy = new JCheckBox("Tolstoy");
		chckbxTolstoy.setBounds(16, 87, 97, 23);
		contentPane.add(chckbxTolstoy);
		
		JLabel lblConnorEngine = new JLabel("Connor's Inverted Indice Engine");
		lblConnorEngine.setFont(new Font("Agency FB", Font.BOLD | Font.ITALIC, 20));
		lblConnorEngine.setBounds(530, 15, 494, 102);
		contentPane.add(lblConnorEngine);
		
		JButton btnSubmit = new JButton("Build Indices");
		btnSubmit.setBounds(131, 65, 155, 45);
		contentPane.add(btnSubmit);
		
		JButton btnOutput = new JButton("Display results");
		btnOutput.setBounds(342, 65, 155, 45);
		contentPane.add(btnOutput);
		
		JTextArea jAll = new JTextArea();
		jAll.setColumns(20);
		jAll.setRows(5);
		jAll.setBounds(16, 143, 1008, 440);
		
		JScrollPane scroll = new JScrollPane (jAll, 
		   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(16, 143, 504, 440);
		contentPane.add(scroll);
		
		
		JTextArea jSearched = new JTextArea();
		jSearched.setColumns(20);
		jSearched.setRows(5);
		jSearched.setBounds(530, 231, 504, 352);
		
		JScrollPane scroll_1 = new JScrollPane(jSearched, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_1.setBounds(530, 231, 494, 352);
		contentPane.add(scroll_1);
		
		JLabel lblChooseAuthor = new JLabel("Choose author");
		lblChooseAuthor.setBounds(16, 14, 173, 14);
		contentPane.add(lblChooseAuthor);
		
		txtEnterSearchTerm = new JTextField();
		txtEnterSearchTerm.setHorizontalAlignment(SwingConstants.LEFT);
		txtEnterSearchTerm.setText("Enter Search Term");
		txtEnterSearchTerm.setBounds(530, 143, 494, 36);
		contentPane.add(txtEnterSearchTerm);
		txtEnterSearchTerm.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(935, 190, 89, 23);
		contentPane.add(btnSearch);
		
		JLabel lblWarning = new JLabel("please wait at least 45 seconds after building indices before displaying results");
		lblWarning.setBounds(131, 121, 883, 14);
		contentPane.add(lblWarning);
		
		btnSubmit.addActionListener(e -> 
		{
			ArrayList<String> authors = new ArrayList<String>(3);
            if(chckbxShakespeare.isSelected())
            	authors.add("Shakespeare");
            if(chckbxHugo.isSelected())
            	authors.add("Hugo");
            if(chckbxTolstoy.isSelected())
            	authors.add("Tolstoy");
            String input = "gs://dataproc-staging-us-west1-401467404745-tqywcyaw/Data/" + authors.get(0) + "/"; //only get one author right now
            //submit job
            try 
            {
            	GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("./credentials.json"))
                        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
                HttpRequestInitializer ri = new HttpCredentialsAdapter(credentials);
                Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(),new JacksonFactory(), ri).build();
                HadoopJob job = new HadoopJob();
                job.setMainJarFileUri("gs://dataproc-staging-us-west1-401467404745-tqywcyaw/JAR/invertedindex.jar");
                job.setArgs(ImmutableList.of("InvertedIndex",input,"gs://dataproc-staging-us-west1-401467404745-tqywcyaw/output"));
                dataproc.projects().regions().jobs().submit("cloud-computing-273412" , "us-west1", new SubmitJobRequest()
                             .setJob(new Job()
                                .setPlacement(new JobPlacement()
                                    .setClusterName("cluster-1660"))
                             .setHadoopJob(job)))
                            .execute();
            }
            catch (Exception e1) 
            {
                e1.printStackTrace();
            }
        });
		btnOutput.addActionListener(e -> 
		{
			jAll.setText("");
			try {
				
	            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("./credentials.json"))
	                        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
	            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
	            StorageObject target = new StorageObject();
	            target.setBucket("dataproc-staging-us-west1-401467404745-tqywcyaw");
	            target.setName("output.txt");
	            Storage.ComposeRequest.Builder requestBuilder = new Storage.ComposeRequest.Builder();
	            Page<Blob> outputList = storage.list("dataproc-staging-us-west1-401467404745-tqywcyaw",BlobListOption.currentDirectory(),BlobListOption.prefix("output/"));
	            Iterator<Blob> iterator = outputList.iterateAll().iterator();
	            ArrayList<String> outputs = new ArrayList<>();
	            ArrayList<BlobId> blobIDs = new ArrayList<>();
	            while(iterator.hasNext()) {
	                Blob currBlob = iterator.next();
	                blobIDs.add(currBlob.getBlobId());
	                String name = currBlob.getName();
	                if (!name.equals("output/_SUCCESS")) {//success file is ignored
	                    outputs.add(name);
	                }
	            }
	            requestBuilder.setTarget(BlobInfo.newBuilder("dataproc-staging-us-west1-401467404745-tqywcyaw", "output.txt").build());
	            requestBuilder.addSource(outputs);
	            Storage.ComposeRequest request = requestBuilder.build();
	            storage.compose(request);
	            storage.delete(blobIDs);

	            Blob blob = storage.get(BlobId.of("dataproc-staging-us-west1-401467404745-tqywcyaw", "output.txt"));
	            blob.downloadTo(Paths.get("./output.txt"));
	            
	            Scanner scanny = new Scanner(new File("output.txt"));
	            while(scanny.hasNextLine()) {
	                String curr = scanny.nextLine();
	                jAll.append(curr + "\n");
	            }
	            jAll.repaint();
			} catch(Exception e3)
			{
				e3.printStackTrace();
			}
		});
		btnSearch.addActionListener(e -> 
		{
			String searchTerm = new String(txtEnterSearchTerm.getText());
			if(jAll.getText().contains(searchTerm))
			{
				int index = jAll.getText().indexOf(searchTerm);
				String sub = jAll.getText().substring(index);
				int end = sub.indexOf("\n");
				if(end != -1)
					sub = sub.substring(0,end);
				jSearched.setText(sub);
			}
			else
			{
				jSearched.setText("term not found");
			}
		});
	}
}
