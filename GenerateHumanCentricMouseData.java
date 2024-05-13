package MouseIDMatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateHumanCentricMouseData {

	
	
	public static void main(String[] args) {
		
		try {
			
			HashMap map_human2mouse = new HashMap();
			HashMap db_map = new HashMap();
			HashMap db_map_mouse = new HashMap();
			HashMap db_map_human = new HashMap();
			
			String outputFile = "/Users/4472414/Documents/Current_Manuscripts/GeneSymbol_Annotation_20240430/HOM_MouseHumanSequence_TimProcessed_HumanCentric.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			
			String inputFile = "/Users/4472414/Documents/Current_Manuscripts/GeneSymbol_Annotation_20240430/HOM_MouseHumanSequence.rpt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String DB_class_id = split[0];
				if (db_map.containsKey(DB_class_id)) {
					LinkedList list = (LinkedList)db_map.get(DB_class_id);
					list.add(str);
					db_map.put(DB_class_id, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(str);
					db_map.put(DB_class_id, list);
				}				
				if (split[2].equals("10090")) {
					if (db_map_mouse.containsKey(DB_class_id)) {
						LinkedList list = (LinkedList)db_map_mouse.get(DB_class_id);
						list.add(str);	
						System.out.println(str);
						//db_map_mouse.put(DB_class_id, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(str);
						db_map_mouse.put(DB_class_id, list);
					}		
				}
				if (split[2].equals("9606")) {
					if (db_map_human.containsKey(DB_class_id)) {
						LinkedList list = (LinkedList)db_map_human.get(DB_class_id);
						list.add(str);
						db_map_human.put(DB_class_id, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(str);
						db_map_human.put(DB_class_id, list);
					}		
				}
			}
			in.close();
			
			out.write(header + "\t" + header + "\n");
			Iterator itr = db_map.keySet().iterator();
			while (itr.hasNext()) {
				String db_id = (String)itr.next();
				
				String[] split_values = split_header;
				for (int i = 0; i < split_values.length; i++) {
					split_values[i] = "";
				}
				if (db_map_mouse.containsKey(db_id)) {
					LinkedList mouse_list = (LinkedList)db_map_mouse.get(db_id);
					Iterator itr_mouse = mouse_list.iterator();
					while (itr_mouse.hasNext()) {
						String mouse_str = (String)itr_mouse.next();
						
						String[] split_mouse_str = mouse_str.split("\t");
						for (int i = 0; i < split_mouse_str.length; i++) {
							split_values[i] = split_values[i] += split_mouse_str[i] + ",";
						}
					}
				}
				if (db_map_human.containsKey(db_id)) {
					LinkedList human_list = (LinkedList)db_map_human.get(db_id);
					Iterator itr_human = human_list.iterator();
					// generate each human match
					while (itr_human.hasNext()) {
						String human_str = (String)itr_human.next();
						out.write(human_str);
						for (int i = 0; i < split_values.length; i++) {
							if (split_values[i].length() > 0) {
								if (split_values[i].substring(split_values[i].length() - 1, split_values[i].length()).equals(",")) {
									split_values[i] = split_values[i].substring(0, split_values[i].length() - 1);
								}
							}
							
							out.write("\t" + split_values[i]);
						}
						out.write("\n");
					}
				}
			}
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
