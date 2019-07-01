package applications;

import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.File;  
  
import javax.swing.JButton;  
import javax.swing.JFileChooser;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
  
public class FileChooser extends JFrame implements ActionListener{  
    JButton open=null; 
    String filePath;
    
    public static void main(String[] args) {
    	
    	new FileChooser();
    }
    public FileChooser(){  
    	JFileChooser jfc=new JFileChooser("D:\\整理 临时\\2019 春季\\2019 春季学期 软件构造\\LABHUB\\lab3\\Lab3-1163450201\\src\\txt");  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
        jfc.showDialog(new JLabel(), "选择");  
        File file=jfc.getSelectedFile();  
        if(file.isDirectory()){  
        	filePath = file.getAbsolutePath();
        	System.out.println(filePath);  
        }else if(file.isFile()){  
            System.out.println("重新选择");  
        }  
    }  
    
    @Override  
    public void actionPerformed(ActionEvent e) {  
          
    }  
    
    
  
}  