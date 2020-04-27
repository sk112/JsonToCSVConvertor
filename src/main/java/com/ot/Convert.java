package com.ot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

public class Convert 
{
    public static void main( String[] args ) throws IOException
    {

        if(args.length < 1){
            System.out.println("Need Input source to proceed!");
            System.exit(0);
        }
        System.out.println(args[0]);
        File filesList = new File(args[0]);
        String outputFolder = args[0]+"\\output";
        File files[] = filesList.listFiles();
        
        System.out.println(files.length);
        for(File file: files){

            if(file.getName().endsWith("json")){
                System.out.println(file.toPath().toString());
                Path path = FileSystems.getDefault().getPath(file.toPath().toString());

                List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
                List<String> allKeys  = new ArrayList<>();

                /**
                 * get all keys in a map
                 */
                JSONObject json = new JSONObject(lines.get(0));
                JSONObject ob = json.getJSONObject("query");
                Iterator<String> jsonObjKeys = ob.keys();

                while(jsonObjKeys.hasNext()){
                    allKeys.add(jsonObjKeys.next());
                }
                
                /** END
                 * get all keys in a map
                 */

                File out = new File(outputFolder);

                out.mkdirs();
                
                String filename = path.getFileName().toString();
                
                System.out.println(filename);

                File newFile = new File(outputFolder+filename+".csv");
                newFile.createNewFile();

                FileWriter writer = new FileWriter(newFile);
                writer.write("time,resultCount,");
                for(String key: allKeys){
                    writer.write(key + ",");
                }
                
                writer.write("\n");

                for(String line: lines){
                    // System.out.println("line->> "+ line);
                    json = new JSONObject(line);
                    writer.write(json.getInt("time")+","+json.getInt("resultCount")+",");

                    JSONObject queryObj = json.getJSONObject("query"); 
                    for(String key: allKeys){
                        writer.write(queryObj.getString(key)+",");
                    }
                    writer.write("\n");
                }
                writer.close();
            }
        }
    }
}
