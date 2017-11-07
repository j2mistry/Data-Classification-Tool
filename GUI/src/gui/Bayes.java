package  gui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Bayes {
	 String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	 String DB_URL = "jdbc:mysql://localhost";
	 Connection conn ;
	 double total_rows;
	 int total_class,lmt;
	 List<String> cls;
	 String classname;
	 ArrayList<String> att_lst;
	 Map<String, String> cls_value;
        double tn=0,tp=0,fp=0,fn=0,ne=0,p=0;
	DbConnection db;
        boolean testCaseFlag;
        DbConnection dbtst;
        StringBuilder s;
        Date t;

	public  void main1(DbConnection db1) throws SQLException
	{
                
                db=db1;
                
                t=new  Date(System.currentTimeMillis());
		db.st=db.getStatement();
                
		
		ResultSet rs=db.st.executeQuery("select count(*) from "+ db.tableName);
		Statement stmt_tst;
		ResultSet rs_tst;
                if(testCaseFlag)
                {
                    stmt_tst = dbtst.getStatement();
                        rs_tst=stmt_tst.executeQuery("select * from " + dbtst.tableName);
                }
                else
                {
                    stmt_tst = db.getStatement();
                    rs_tst=stmt_tst.executeQuery("select * from " + db.tableName);
                }
		ResultSetMetaData rmd=rs_tst.getMetaData();
		
		int no_attr=rmd.getColumnCount()-1;
		
		cls_value=new HashMap<String, String>();
		cls=new ArrayList<String>();
		rs.next();
		total_rows=rs.getInt(1);
		classname = db.className;
		rs.close();
		rs=db.st.executeQuery("select distinct("+classname+") from " + db.tableName);
		while(rs.next())
		{
			cls.add(rs.getString(1));
			String qr="select count(*) from "+db.tableName+" where " + classname+"='"+rs.getString(1)+"'";
			System.out.println(qr);
			db.st=db.getStatement();
                        
			ResultSet rs2 = db.st.executeQuery(qr);
			rs2.next();
			System.out.println(rs2.getInt(1));
			cls_value.put(rs.getString(1),String.valueOf(rs2.getDouble(1)/total_rows));
		}
		
                StringBuilder ss=new StringBuilder();
		int clcnt=0;
                while(rs_tst.next())
                {
		int i=0;
		int j=0;
		double x=1;
                double[] tmp=new double[cls.size()];
		db.st= db.getStatement();
                
		while(j<cls.size())
		{
			x=1;
			i=0;
		while(i<no_attr)
		{
			String qry="select count(*) from "+db.tableName+" where "+classname+"='"+cls.get(j)+"' and "+rmd.getColumnName(i+1)+" = '"+rs_tst.getString(i+1)+"'";
			ResultSet rs3 = db.st.executeQuery(qry);
                       
			rs3.next();
                        
			 
			x=x*rs3.getDouble(1)/(Double.valueOf(cls_value.get(cls.get(j)))*total_rows);
			
			i++;
		}
                
                tmp[j]=x*Double.valueOf(cls_value.get(cls.get(j)));
		System.out.println((clcnt+1) + " " +cls.get(j) + ": "+ x*Double.valueOf(cls_value.get(cls.get(j))));
		
		j++;
		}
                int z;
                for(z=1;z<rmd.getColumnCount();z++)
                {
                    ss.append(rs_tst.getString(z) +"\t");
                    
                }
                ss.append("\t");
                ss.append( String.format("%.4f", tmp[0])+"         " + String.format("%.4f", tmp[1]) + "         ");
                ss.append(rs_tst.getString(z));
                for(int k=0;k<15-rs_tst.getString(z).length();k++)
                {
                    ss.append(" ");
                }
                //ss.append("\n No="+ String.format("%.4f", tmp[0]) + " Yes="+ String.format("%.4f", tmp[1]));
                
                clcnt++;
                double max=0;
                int indx=0;
                
                for(int k=0;k<cls.size();k++)
                {
                    if(max<tmp[k])
                    {
                        max=tmp[k];
                        indx=k;
                    }

                }
                ss.append(cls.get(indx));
                for(int k=0;k<15-cls.get(indx).length();k++)
                {
                    ss.append(" ");
                }
                ss.append("\n");
                        
                        
                
                if(cls.get(0).equals(cls.get(indx)))
                {
                    if(cls.get(indx).equals(rs_tst.getString(classname)))
                    {
                        tn++;
                    }
                    else
                    {
                        fp++;
                    }
                    
                }
                else
                {
                    
                    if(cls.get(indx).equals(rs_tst.getString(classname)))
                    {
                        tp++;
                    }
                    else
                    {
                        fn++;
                    }
                }
                

                }
                
                            p=tp+fn;
            ne=tn+fp;
            
            System.out.println("tp =" + tp);
            System.out.println("tn =" + tn);
            System.out.println("fp =" + fp);
            System.out.println("fn =" + fn);
            System.out.println("p =" + p);
            System.out.println("n =" + ne);
             s=new StringBuilder();
             s.append("=== Run information ===\n");
             
             s.append("\n   Method:      Naive Bayes");
             s.append("\n   Relation:   "+ db.tableName);
             s.append("\n   Instances: "+ total_rows);
             s.append("\n   Attributes    "+ (rmd.getColumnCount()));
             for(int z=1;z<=rmd.getColumnCount();z++)
             {
                 s.append("\n\t" + rmd.getColumnName(z));
             }
//             s.append("\n\t" + db.className);
             s.append("\n\n   Time taken to build model:"+ (System.currentTimeMillis()-t.getTime())+ "ms \n");
             
            s.append("=== Summary ===\n\n");
            String s1=String.format("%.2f",((tp+tn)/((p+ne)))*100);
            String s2=String.format("%.2f",((fp+fn)/((p+ne)))*100);
            
            s.append("\nCorrectly Classified Instances : " + (tp+tn)  +"   " + s1+"%");
            s.append("\nIncorrectly Classified Instances : " + (fp+fn)   +"   " + s2 + "%");
            System.out.println(s);
            System.out.println(" TP Rate   FP Rate   Precision   Recall  F-Measure   ROC Area  Class");
            String tp_rate,fp_rate,precesion,recall;
            String tp_rate1,fp_rate1,precesion1,recall1;
            s.append("\n\n\n=== Detailed Accuracy By Class ===\n\n");
            tp_rate=String.format("%.2f",tp/(tp+fn));
            fp_rate=String.format("%.2f", fp/(tn+fp));
            precesion=String.format("%.2f",tp/(tp+fp));
            recall=String.format("%.2f", tp/p);
            s.append("\n\t  TP Rate   FP Rate   Precision   Recall  Class");
            s.append("\n\t     "+ tp_rate + "         " + fp_rate + "          "+ precesion+ "           "+ recall +"     " + cls.get(1));
            tp_rate1=String.format("%.2f",tn/(tn+fp));
            fp_rate1=String.format("%.2f",fn/(tp+fn) );
            precesion1=String.format("%.2f", tn/(tn+fn));
            recall1=String.format("%.2f", tn/ne);
            
            s.append("\n\t     "+ tp_rate1 + "         " + fp_rate1 + "          "+ precesion1+ "           "+ recall1 +"     " + cls.get(0));
            
            
            System.out.println("  "+ tn/(tn+fp)+"   " + fn/(tp+fn) + "  " + tn/(tn+fn) + "  " +  tn/ne + " " + cls.get(0) );
            tp_rate1=String.format("%.2f",(Double.parseDouble(tp_rate)+Double.parseDouble(tp_rate1))/2);
            fp_rate1=String.format("%.2f",(Double.parseDouble(fp_rate)+Double.parseDouble(fp_rate1))/2);
            precesion1=String.format("%.2f",(Double.parseDouble(precesion)+Double.parseDouble(precesion1))/2);
            recall1=String.format("%.2f",(Double.parseDouble(recall)+Double.parseDouble(recall1))/2);
            s.append("\n\nWeighted Avg.\t    "+ tp_rate1 + "         " + fp_rate1 + "          "+ precesion1+ "           "+ recall1 +"     ");
            s.append("\n\n=== Confusion Matrix ===");
            s.append("\n     "+ cls.get(1) + "   " +cls.get(0));
            s.append("\n     " +tp + "   " + fn );
            s.append("\n     " +fp + "   " + tn );
            s.append("\n\n === Evaluation Details===\n");
            s.append("    ");
            for(int i=1;i<rmd.getColumnCount();i++)
            {
                s.append( rmd.getColumnName(i).toUpperCase()+"\t");
                
                
            }
            for(int i=0;i<cls.size();i++)
            {
                s.append(cls.get(i).toUpperCase());
                for(int k=0;k<15-(cls.get(i).length());k++)
                    {
                        s.append(" ");
                    }
            }
            s.append("ACTUAL         PREDICTED      ");
            s.append("\n" +ss);

	}
}
