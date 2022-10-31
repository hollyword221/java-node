/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientA {
    static final int BUFFER_SIZE = 1024;
    static final int NODES = 5;
    
    
    public static void main(String[] args) throws IOException {
    	
    	Socket[] s=new Socket[NODES];
        ObjectInputStream[] in=new ObjectInputStream[NODES];
        ObjectOutputStream[] out=new ObjectOutputStream[NODES];
        
    	Scanner sc= new Scanner(System.in);
    	int i;
    		
    	s[0] = new Socket("localhost", 2222);
    	s[1] = new Socket("localhost", 3333);
    	s[2] = new Socket("localhost", 4444);
    	s[3] = new Socket("localhost", 5555);
    	s[4] = new Socket("localhost", 6666);
    	
    	
		for (i=0;i<NODES;i++)
		{
			
			in[i]=new ObjectInputStream(s[i].getInputStream());
			out[i]=new ObjectOutputStream(s[i].getOutputStream());
		}

			
		
    	    	
    	System.out.println("Which file would you distribute? ");    	        	
		String filePath=sc.nextLine();
		

		System.out.println("Would you save backup files?(yes/no) ");
		String confirm=sc.nextLine();
		
		
		boolean flag=confirm.equalsIgnoreCase("yes");
		
		
        try {
        	BufferedInputStream bis=new BufferedInputStream(new FileInputStream(new File(filePath)));
			byte[] buffer = new byte[0];
			
			byte [] block=bis.readNBytes(BUFFER_SIZE);
			byte[] echoed=new byte[0];
			
			int nodeInd=0;
			
			while(block.length>0) {
				
					try {
						out[nodeInd].writeObject(block);
						out[nodeInd].flush();
						echoed = (byte[]) in[nodeInd].readObject();	
						
					}catch(Exception e) {e.printStackTrace();}
					
					if (echoed!=null && echoed.length>0) {
	            		  if (flag) buffer=concatenateByteArrays(buffer,echoed);
	            		  block=bis.readNBytes(BUFFER_SIZE);
	            	}
				nodeInd=(nodeInd+1)%NODES;
				
			}
            	  
			FileOutputStream fos;
			
			if (flag) {
				fos=new FileOutputStream(new File(filePath+".backup"));
				fos.write(buffer); fos.flush(); fos.close();
			}
			
			
			System.out.println("Completely retrieved ");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length]; 
        System.arraycopy(a, 0, result, 0, a.length); 
        System.arraycopy(b, 0, result, a.length, b.length); 
        return result;
    } 
}

