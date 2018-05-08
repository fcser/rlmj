package cn.java.rlmj.connect;


public class EquipmentList {
	//提供设备与ip的映射
	public static EquipmentMap<Integer , String> equipments=new EquipmentMap<>();
	//提供设备与楼号的映射
	/*public static void getEquipment(){
		File file=new File("d:\\equipment.txt");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			while(true){
				String str=br.readLine();
				if(str==null){
					break;
				}
				System.out.println(str);
				String[] strs=str.split("#");
				if(strs.length==2){
					equipments.map.put(strs[0], strs[1]);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("��ȡ�ļ����� ");
		}
		
	}*/
}
