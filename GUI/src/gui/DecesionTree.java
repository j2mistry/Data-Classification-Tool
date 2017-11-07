package  gui;

import com.sun.org.apache.xml.internal.serializer.utils.AttList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.Timer;
import org.w3c.dom.css.Counter;





public class DecesionTree {
	
	String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	String DB_URL = "jdbc:mysql://localhost/a";
	Connection conn ;
	double total_rows;
        int tst_rows;
	int total_class,lmt;
	List<String> cls;
	String classname;
	ArrayList<String> att_lst,att_lst_o;
	DbConnection db;
        Node n1;
        double tn=0,tp=0,fp=0,fn=0,ne=0,p=0;
        boolean testCaseFlag;
        DbConnection dbtst;
        StringBuilder s;
        StringBuilder tree;
        Date t;
	public  void main1(DbConnection db1) throws SQLException, ClassNotFoundException
	{
            
    		t= new Date(System.currentTimeMillis());
                tree=new StringBuilder();
		att_lst=new ArrayList<String>();
                att_lst_o=new ArrayList<String>();
                db=db1;
                classname = db.className;
		Statement stmt=db.getStatement();
		ResultSet rs=stmt.executeQuery("select count(*) from " + db.tableName);
		rs.next();
		total_rows=rs.getInt(1);
                if(testCaseFlag)
                {
                    lmt=(int)total_rows;
                    dbtst.className=db.className;
                }
                
                else
                lmt=(int)total_rows/2;
                
                tst_rows=lmt;
		System.out.println(total_rows);
		rs=stmt.executeQuery("select * from "+ db.tableName);
		ResultSetMetaData rmd =rs.getMetaData();
		rs.close();
		int no_attr=rmd.getColumnCount()-1;
		Map M =new HashMap();
		classname=rmd.getColumnName(5);
		System.out.println("VLS " + classname + " " + no_attr + " " + rmd.getColumnCount());
		cls= new  ArrayList<String>();
		int i=0;
		Statement stt=db.getStatement();
		ResultSet rs1=stt.executeQuery("select distinct("+ classname + ") from "+ db.tableName + " order by " + classname);
		
while(rs1.next())
	{
		 cls.add(rs1.getString(1));
                 System.out.println(rs1.getString(1));
		 Statement stmt2 = db.getStatement();
		 String s=null;
                 if(db.db==1)
                 {
                  s="select count(*) from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where " + classname + "='"+rs1.getString(1) +"'";
                 }
                 
                 
                 System.out.println(s);
		 //ResultSet rs2=stmt2.executeQuery(s);
		 //rs2.next();
	     //rs2.close();
		 stmt2.close();	
	}
total_class=cls.size();

			
			i=1;
                        System.out.println("S" +rmd.getColumnCount());
             
                
			while(i<=no_attr)
			{
                            System.out.println("s" + i +" " + rmd.getColumnLabel(i));
				att_lst.add(rmd.getColumnLabel(i));
                                //att_lst_o.add(rmd.getColumnLabel(i));
				i++;
			}
		 System.out.println(att_lst.size() + " sa" + att_lst_o.size());
			 n1=create_node( new ArrayList<String>(),new ArrayList<String>());
                         
			print(n1);
                        System.out.println("TEST");
                        
                        if(testCaseFlag)
                        {
                            testCase(n1);
                        }
                         else       
                         test(n1);
                        
                         System.out.println("TEST F");
			
	}

	

        int spces=0;
        String s12="";
	private void print(Node n1) {
		// TODO Auto-generated method stub
		
                
                int i=0;
                for(int z=0;z<spces;z++)
                {
                    tree.append(" ");
                    System.out.print("  ");
                }
                
                System.out.print("-->" + n1.name+ " "+ s12 +"\n" );
                tree.append("-->" + n1.name.toUpperCase()+ " ("+ s12 +")\n" );
		//System.out.println("NOde "+n1.name);
		while(i<n1.value.size())
		{
			
		
                s12=n1.value.get(i);
		//System.out.println(n1.value.get(i));
		spces+=5;
		print(n1.map.get(n1.value.get(i)));
                spces-=5;
	//	System.out.println("LOL");
		i++;
		}
		
	}



	private  Node create_node(ArrayList<String> att_name, ArrayList<String> val) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("start");
		Statement st=db.getStatement();
		String qry1="";
		Map M =new HashMap();
		if(att_name.size()>0 && val.size()> 0 )
		{
                    String qry="";
                    if(db.db==1)
			 qry="Select count(*) from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where ";
                    else
                         qry="Select count(*) from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ")  where ";
			for(int i=0;i<att_name.size();i++)
			{
				qry1=qry1 + att_name.get(i) + "='" +val.get(i) +"'";
				if(i!=att_name.size()-1)
				{
					qry1 += " and ";
				}
			}
			qry=qry+qry1;
			System.out.println("Execute qry "+ qry);
			ResultSet rs=st.executeQuery(qry);
			rs.next();
			total_rows=rs.getInt(1);
			rs.close();
                        
                        if(db.db==1)
			rs=st.executeQuery("Select count(distinct("+ classname + ")) from   (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "+ qry1);
                        else
                            rs=st.executeQuery("Select count(distinct("+ classname + ")) from   (SELECT * FROM  "+db.tableName+" where rownum < " + lmt + ") where "+ qry1);
			rs.next();
			if(rs.getInt(1)==1)
					{
						rs.close();
                                                if(db.db==1)
						rs=st.executeQuery("Select distinct("+ classname + ") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "+ qry1);
                                                else
                                                 rs=st.executeQuery("Select distinct("+ classname + ") from (SELECT * FROM  "+db.tableName+" where rownum < " + lmt + ") where "+ qry1);
						rs.next();
						Node n= new Node(rs.getString(1));
						return n;
					}
			else
					{
				
					}
			
			
			
		}
		Statement stm=db.getStatement();
		
		String qr="";
	
		if(qry1.equals(""))
		{
                    if(db.db==1)
			qr="Select count(*) from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq";
                    else
                        qr="Select count(*) from (SELECT * FROM  "+db.tableName+" where rownum < " + lmt + ")";
		}
		else
		{
                    if(db.db==1)
			qr="Select count(*) from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "  + qry1;
                    else
                        qr="Select count(*) from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where "  + qry1;
		}
		ResultSet rd = stm.executeQuery(qr);
		rd.next();
		total_rows=rd.getDouble(1);
		
		
		System.out.println(att_lst.size());
		if(qry1.equals(""))
	{
            if(db.db==1)
		qr="select " + att_lst.toString().substring(1,att_lst.toString().length()-1) + " from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq";
            else
                qr="select " + att_lst.toString().substring(1,att_lst.toString().length()-1) + " from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ")";
	}
	else
	{
            if(db.db==1)
		qr="select " + att_lst.toString().substring(1,att_lst.toString().length()-1) + " from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "  + qry1;
            else
                qr="select " + att_lst.toString().substring(1,att_lst.toString().length()-1) + " from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ")";
	}
	
		System.out.println(qr);
	     ResultSet rst =stm.executeQuery(qr);
	     ResultSetMetaData rmd=rst.getMetaData();
	     int i=1;
	     while(i<=rmd.getColumnCount())
	     {
	     double t=info_gain(qry1) - calculate_gain_for_attribute(rmd.getColumnName(i),qry1);
		System.out.println(t);
		M.put(rmd.getColumnName(i), t);//considering last column as a class neeed to change later if class selected is not last columnn
		i++;
	     }	
	     String att_n=max_gain(rmd,M);
	     
	     Node n=new Node(att_n);
	     rst.close();
	     String qrt;
             if(db.db==1)
                 qrt="select distinct("+ att_n+") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq";
             else
                 qrt="select distinct("+ att_n+") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ")";
	     rst=stm.executeQuery(qrt);
	     System.out.println(qrt);
	     while(rst.next())
	     {
	    	 n.value.add(rst.getString(1));
	    	 att_name.add(att_n);
	    	 val.add(rst.getString(1));
	    	 
	    	 
	    	 att_lst.remove(att_n);
	    	 	 
	    	 n.map.put(rst.getString(1), create_node(att_name, val));
	    	 
	    	 att_lst.add(att_n);
	    	 att_name.remove(att_n);
	    	 val.remove(rst.getString(1));
	    	 
	     }
	return n;
	}



	private  double info_gain(String qry1) throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt=db.getStatement();
		Double info_g=0.00;
		String qr="";
		if(qry1.equals(""))
		{
                    if(db.db==1)
			qr="select distinct("+ classname + ") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq";
                    else
                        qr="select distinct("+ classname + ") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ")";
			
		}
		else
		{
                    if(db.db==1)
			qr="select distinct("+ classname + ") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "+ qry1;
                    else
                        qr="select distinct("+ classname + ") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where "+ qry1;
		}
		
		ResultSet rs1=stmt.executeQuery(qr);
		
		while(rs1.next())
			{
				 Statement stmt2 =db.getStatement();
				 String s;
				 if(qry1.equals(""))
				 {
                                     if(db.db==1)
					 s="select count(*) from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where " + classname + "='"+rs1.getString(1) +"'";
                                     else
                                         s="select count(*) from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where " + classname + "='"+rs1.getString(1) +"'";
				 }
				 else
				 {
                                     if(db.db==1)
					 s="select count(*) from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where " + classname + "='"+rs1.getString(1) +"' and " + qry1;
                                     else
                                         s="select count(*) from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where " + classname + "='"+rs1.getString(1) +"' and " + qry1;
				 }
				// System.out.println(s);
				 ResultSet rs2=stmt2.executeQuery(s);

				rs2.next();
			
				double tmp=rs2.getDouble(1)/total_rows;
				rs2.close();
				stmt2.close();
				info_g=info_g - ((tmp)*(Math.log(tmp)/Math.log(2))); 
				
			}
return info_g;
	}



	private  String max_gain(ResultSetMetaData rmd, Map m) throws SQLException {
		// TODO Auto-generated method stub
		
		double max=0;
		String at_nm = null;
		int i=1;
		while(i<=rmd.getColumnCount())
		{
			System.out.println("Max----" +  m.size());
                        System.out.println(max);
			if(max<Double.parseDouble((m.get(rmd.getColumnName(i)).toString())))
			{
				
				max=Double.parseDouble((m.get(rmd.getColumnName(i)).toString()));
				at_nm=rmd.getColumnName(i);
			}
			i++;
		}
		
		return at_nm;
	}

	private  double calculate_gain_for_attribute(String columnName,String qry1) throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt =db.getStatement();
		String q="";
		if(qry1.isEmpty())
		{
                    if(db.db==1)
			q="select distinct("+columnName+") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq";
                    else
                        q="select distinct("+columnName+") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ")";
		}
		else
		{
                    if(db.db==1)
			q="select distinct("+columnName+") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where " + qry1;
                    else
                        q="select distinct("+columnName+") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where " + qry1;
		}
		ResultSet rs=stmt.executeQuery(q);
		int i=0;
		double Info=0;
		double x=0;
		double totalRowsForAtt;
		System.out.println(columnName);
		while(rs.next())
		{
			x=0;
			Statement stmt1 = db.getStatement();
			if(qry1.isEmpty())
			{
                            if(db.db==1)
				q="select count("+columnName+") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "+ columnName+"='"+rs.getString(1) +"'";
                            else
                                q="select count("+columnName+") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where "+ columnName+"='"+rs.getString(1) +"'";
			}
			else
			{
                            if(db.db==1)
				q="select count("+columnName+") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "+ columnName+"='"+rs.getString(1) +"' and " + qry1;
                            else
                                q="select count("+columnName+") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where "+ columnName+"='"+rs.getString(1) +"' and " + qry1;
			}
			ResultSet rs2=stmt1.executeQuery(q);
			rs2.next();
			totalRowsForAtt=rs2.getInt(1);
			int j=0;
			while(j<total_class)
			{
				Statement stmt2 =db.getStatement();
				if(qry1.isEmpty())
				{
                                    if(db.db==1)
					q="select count("+columnName+") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "+ columnName+"='"+rs.getString(1) +"' and " + classname +"='"+ cls.get(j) +"'";
                                    else
                                        q="select count("+columnName+") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where "+ columnName+"='"+rs.getString(1) +"' and " + classname +"='"+ cls.get(j) +"'";
				}
				else
				{
                                    if(db.db==1)
					q="select count("+columnName+") from (SELECT * FROM "+db.tableName+" AS s  LIMIT "+lmt+") AS aq where "+ columnName+"='"+rs.getString(1) +"' and " + classname +"='"+ cls.get(j) +"' and " + qry1;
                                    else
                                        q="select count("+columnName+") from (SELECT * FROM "+db.tableName+" where rownum < " + lmt + ") where "+ columnName+"='"+rs.getString(1) +"' and " + classname +"='"+ cls.get(j) +"' and " + qry1;
				}	
				ResultSet rs3=stmt2.executeQuery(q);
				rs3.next();
				
				double tmp1=rs3.getDouble(1)/totalRowsForAtt;
				if(tmp1>0)
					{
					//System.out.println(rs3.getDouble(1) + "/"+ totalRowsForAtt + " = " + tmp1);
					x-= (tmp1)*(Math.log(tmp1)/Math.log(2));
					//System.out.println((tmp1)*(Math.log(tmp1)/Math.log(2))+    "  x= " + x );
					}
				
					j++;
			}
			Info=Info+ (x*(totalRowsForAtt/total_rows));
			i++;
		}
		
		return Info;
	}	


        public void test(Node n1) throws SQLException
        {
            System.out.println("IN");
            db.st=db.getStatement();
            int n=tst_rows;
            Node tmp=n1;
            String qr;
            if(db.db==1)
                qr="SELECT * FROM (SELECT * FROM "+db.tableName+" AS s  LIMIT "+n+","+n+") AS aq";
            else
                qr="SELECT " + att_lst.toString().substring(1,att_lst.toString().length()-1) +"," +classname + " FROM (SELECT a.*,rownum rt FROM "+db.tableName+" a) where rt >"+ n;
                
            System.out.println(qr);
            ResultSet rs = db.st.executeQuery(qr);
            
            ResultSetMetaData rmd = rs.getMetaData();
            int i=1;
            StringBuilder ss = new StringBuilder();
                for(int k=0;k<att_lst.size();k++)
                {
                    ss.append(att_lst.get(k).toUpperCase() + "\t");
                    System.out.println(att_lst.get(k) + "\t");
                }
                ss.append(classname + "\t");
                ss.append(" Predicted\n");
            while(rs.next())
            {
                tmp=n1;
                while(!tmp.map.isEmpty())
                {
                    i=1;
                    while(true)
                    {
                    if(rmd.getColumnName(i).equals(tmp.name))
                    {
                       tmp=tmp.map.get(rs.getString(tmp.name)); 
                       
                       break;
                    }
                    i++;
                    
                    }
                }
                System.out.println("<--------------------------------->");
                
                for(int k=1;k<=att_lst.size();k++)
                {
                    ss.append(rs.getString(k) + "\t");
                    if(k==3)
                        ss.append("\t");
                    System.out.println(rs.getString(k) + "\t");
                }
                   ss.append(rs.getString(classname) + "\t");
             
                ss.append(tmp.name);
                ss.append("\n");
                //System.out.println(rs.getString(1) + "" +rs.getString(2) + " " +rs.getString(3) + " " + rs.getString(4) + " "  );
                System.out.println("predicted " + tmp.name + " actual "+ rs.getString(classname));
                
                if(cls.get(0).equals(tmp.name))
                {
                    if(tmp.name.equals(rs.getString(classname)))
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
                    
                    if(tmp.name.equals(rs.getString(classname)))
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
             s.append("\n   Method:      Decesion Tree");
             s.append("\n   Relation:   "+ db.tableName);
             s.append("\n   Instances: "+ lmt*2);
             s.append("\n   Attributes    "+ (att_lst.size()+1));
             for(int z=0;z<att_lst.size();z++)
             {
                 s.append("\n\t" + att_lst.get(z));
             }
             s.append("\n\t" + db.className);
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
            s.append("\n\n=== Evaluation Details ===\n");
            s.append(ss);
            s.append("\n=== Decesion Tree ===");
            s.append("\n"+ tree);
            
            

           // System.out.println(nj.getComponent(1).toString());
            
            
                   
        }

    private void testCase(Node n1) throws SQLException{
            System.out.println("IN");
            db=dbtst;
            db.st=db.getStatement();
            int n=tst_rows;
            Node tmp=n1;
            String qr;
            if(db.db==1)
                qr="SELECT * FROM " + db.tableName ;
            else
                qr="SELECT " + att_lst.toString().substring(1,att_lst.toString().length()-1) +"," +classname + " FROM "+db.tableName;
                
            System.out.println(qr);
            ResultSet rs = db.st.executeQuery(qr);
            
            ResultSetMetaData rmd = rs.getMetaData();
            int i=1;
            StringBuilder ss = new StringBuilder();
                for(int k=0;k<att_lst.size();k++)
                {
                    ss.append(att_lst.get(k).toUpperCase() + "\t");
                    System.out.println(att_lst.get(k) + "\t");
                }
ss.append(classname + "\t");
                ss.append(" Predicted\n");
            while(rs.next())
            {
                tmp=n1;
                while(!tmp.map.isEmpty())
                {
                    i=1;
                    while(true)
                    {
                    if(rmd.getColumnName(i).equals(tmp.name))
                    {
                       tmp=tmp.map.get(rs.getString(tmp.name)); 
                       
                       break;
                    }
                    i++;
                    
                    }
                }
                System.out.println("<--------------------------------->");
                
                for(int k=1;k<=att_lst.size();k++)
                {
                    ss.append(rs.getString(k) + "\t");
                    if(k==3)
                        ss.append("\t");
                    System.out.println(rs.getString(k) + "\t");
                }
                ss.append(rs.getString(classname) +"\t");
                ss.append(tmp.name);
                ss.append("\n");
                //System.out.println(rs.getString(1) + "" +rs.getString(2) + " " +rs.getString(3) + " " + rs.getString(4) + " "  );
                System.out.println("predicted " + tmp.name + " actual "+ rs.getString(classname));
                
                if(cls.get(0).equals(tmp.name))
                {
                    if(tmp.name.equals(rs.getString(classname)))
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
                    
                    if(tmp.name.equals(rs.getString(classname)))
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
             s.append("\n   Method:      Decesion Tree");
             s.append("\n   Relation:   "+ db.tableName);
             s.append("\n   Instances: "+ lmt*2);
             s.append("\n   Attributes    "+ (att_lst.size()+1));
             for(int z=0;z<att_lst.size();z++)
             {
                 s.append("\n\t" + att_lst.get(z));
             }
             s.append("\n\t" + db.className);
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
            s.append("\n\n=== Evaluation Details ===\n");
            s.append(ss);

            s.append("\n=== Decesion Tree ===");
            s.append("\n"+ tree);            
                   
        }
}
