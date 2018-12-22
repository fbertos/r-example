package org.fbertos.r;

import org.rosuda.JRI.RList;
import org.rosuda.JRI.Rengine;

public class RExecute {

	public static void main(String[] args) {
        //String javaVector = "c(1,2,3,4,5)";

		Rengine engine = new Rengine(args, false, new TextConsole());
        
        //Rengine engine = new Rengine(new String[] { "--no-save" }, false, null);
        
        engine.waitForR();

        /*
        engine.eval("murders_final_sort <- read.csv(file=\"/home/fbertos/Escritorio/Training/r/murders_final_sort.csv\")");
        
        engine.eval("m_filtered <- murders_final_sort[murders_final_sort$state == 'Texas',]");
        
        engine.eval("barplot(m_filtered$change, names.arg=m_filtered$city, cex.names = 0.6, las=2, main='Changes')");
        
        engine.eval("png(filename=\"/home/fbertos/Escritorio/Training/r/murders_final_sort.png\")");
        
        engine.eval("barplot(m_filtered$change, names.arg=m_filtered$city, cex.names = 0.6, las=2, main='Changes', col=c(\"blue\", \"orange \"))");
        
        engine.eval("dev.off()");
        
        engine.eval("rVector <- " + javaVector);
        
        engine.eval("meanVal <- mean(rVector)");
        
        double mean = engine.eval("meanVal").asDouble();
        
        System.out.println("Mean of given vector is=" + mean);
        */
        
        engine.eval("library('prophet')");
        engine.eval("births <- scan(\"http://robjhyndman.com/tsdldata/data/nybirths.dat\")");
        engine.eval("birthstimeseries <- ts(births, frequency=12, start=c(1946,1))");
        engine.eval("df <- data.frame(seq(as.Date(\"1946-01-01\"), as.Date(\"1959-12-31\"), \"months\"), birthstimeseries)");
        engine.eval("names(df) <- c(\"ds\", \"y\")");
        engine.eval("m <- prophet(df)");
        engine.eval("f_df <- make_future_dataframe(m, periods=24, freq=\"month\")");
        engine.eval("f <- predict(m, f_df)");
        RList list = engine.eval("f").asList();
        
        for (int i=0; i < engine.eval("f").asList().keys().length; i++) {
        	String k = list.keys()[i];
        	double[] v = list.at(k).asDoubleArray();        	
        	System.out.println("\n\n" + k + " values (" + v.length + "):");
        	
        	for (int j=0; j < v.length; j++) {
        		System.out.println(v[j]);	
        	}
        }
        
        engine.end();
	}

}
