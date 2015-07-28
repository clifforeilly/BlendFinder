import java.io.*;
import java.util.List;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by cliff on 31/05/2014.
 */

//args
// 0=input folder
// 1=output folder
// 2=ostype - 1=mac, 2=pc
// 3=alg type


public class BlendFinder
{
    static String InputFileFolder = "";
    static String OutputFileFolder = "";
    static int osType = 0;
    static int aType = 0;
    static String slash = "";

    public static void main(String[] args)
    {
        try
        {
            if(args.length>0)
            {
                System.out.println(args[0]);
                InputFileFolder=args[0];
                System.out.println(args[1]);
                OutputFileFolder=args[1];
                System.out.println(args[2]);
                osType=Integer.parseInt(args[2]);
                System.out.println(args[3]);
                aType=Integer.parseInt(args[3]);

                if(osType==1) //mac
                {
                    slash="/";
                }

                if(osType==2) //pc
                {
                    slash="\\";
                }


                if (aType == 1)
                {
                    File f = new File(InputFileFolder);
                    File[] matchingFiles = f.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".csv");
                        }
                    });
                    System.out.println("Scanning ... " + InputFileFolder);
                    //int filecount=0;
                    String cmd="";

                    List itsimport;


                    for(File tf : matchingFiles)
                    {
                        //filecount++;
                        //load file
                        CSVReader itsdata = new CSVReader(new FileReader(tf));
                        itsimport = itsdata.readAll();
                        itsdata.close();
                        String[] rowits;
                        CSVWriter csvout = new CSVWriter(new FileWriter(OutputFileFolder + File.separator + "bo-" + tf.getName()));

                        int smspace = 0;
                        String sfn = "bananaheadlong";

                        boolean firsttime = true;

                        List <Double[]> tranche = new ArrayList<Double[]>();

                        for(Object ob : itsimport)
                        {
                            rowits=(String[]) ob;

                            System.out.println(rowits[0] + ";" + rowits[1]);

                            int mspace = Integer.parseInt(rowits[0]);
                            String fn = rowits[1];
                            //int s = Integer.parseInt(rowits[2]);

                            if(!fn.equals(sfn))
                            {
                                if(!firsttime)
                                {
                                    //do calculations
                                    String[] t2Tranche;
                                    t2Tranche = new String[30];

                                    Double DocumentBlendMeasure = 0.0;
                                    int numSentences = 0;
                                    for(int n1 = 0; n1<tranche.size(); n1++)
                                    {
                                        for(int n2 = 0; n2<tranche.size(); n2++)
                                        {
                                            if(n2!=n1)
                                            {
                                                if(n2>n1)
                                                {
                                                    Double sumz = 0.0;
                                                    for(int q = 0; q<30; q++)
                                                    {
                                                        sumz = sumz + Math.abs(tranche.get(n1)[q] - tranche.get(n2)[q]);
                                                        t2Tranche[q] = String.valueOf(Math.abs(tranche.get(n1)[q] - tranche.get(n2)[q]));
                                                    }
                                                    /*
                                                    String SentenceBlendMeasure = String.valueOf(1.0/sumz);
                                                    String[] outs = new String[5];
                                                    outs[0]= Integer.toString(smspace);
                                                    outs[1]=sfn;
                                                    outs[2]=String.valueOf(n1+1);
                                                    outs[3]=String.valueOf(n2+1);
                                                    outs[4]=SentenceBlendMeasure;
                                                    csvout.writeNext(outs);
                                                    */
                                                    DocumentBlendMeasure = DocumentBlendMeasure + 1.0/sumz;
                                                }
                                            }
                                        }
                                        numSentences = n1;
                                    }

                                    String[] outs = new String[3];
                                    outs[0]=Integer.toString(smspace);
                                    outs[1]=sfn;
                                    outs[2]=String.valueOf(DocumentBlendMeasure/numSentences);
                                    csvout.writeNext(outs);

                                }

                                //new tranche
                                tranche = new ArrayList<Double[]>();
                                smspace=mspace;
                                sfn=fn;
                                firsttime=false;
                            }

                            Double[] tTranche;
                            tTranche = new Double[30];

                            for(int q = 0; q<30; q++)
                            {
                                tTranche[q]=Double.valueOf(rowits[q+2]);
                            }

                            tranche.add(tTranche);
                            System.out.println("y");




                        }

                        csvout.close();
                    }


                } //if (type == 1)


            }
            else
            {
                System.out.println("Error - no parameters supplied");
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
