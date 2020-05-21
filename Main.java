package main;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;


import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class Main {
private final static String bucketName = "huangzhenghong";
private final static String filePath   = "D:/论文&闲话/大数据实训/lab1";
private final static String accessKey = "440E02A3AA89BA9D1355";
private final static String secretKey = "WzJBNjQzQjFGMEQzRjJGMjE0MjJBMUZGREU2Mzc5QUJFRUE3OEZFMEZd";
private final static String serviceEndpoint = 
"http://scuts3.depts.bingosoft.net:29999";

    private final static String signingRegion = "";

    public static void main(String[] args) {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        final ClientConfiguration ccfg = new ClientConfiguration().
                withUseExpectContinue(false);

        final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .build();

        List<String> pcList = new ArrayList<String>();
        List<String> s3List = new ArrayList<String>();
        
        try {
        	ObjectListing o1 = s3.listObjects(bucketName);
        	List<S3ObjectSummary> objects = o1.getObjectSummaries();
        	System.out.println("files in S3 service:");
        	for(S3ObjectSummary os : objects) {
        		System.out.println("*" + os.getKey());
        		s3List.add(os.getETag());//获取所有s3文件的eTag
        	 }
        }catch(AmazonClientException e) {
        	System.err.println(e.toString());
        	System.exit(1);
        }

        //System.out.println(s3List);
        
        File pcfile = new File(filePath);
        if(pcfile.isDirectory()) {
        	File[] files = pcfile.listFiles();
        	for(int i =0; i<files.length; i++) {
        		//如果还是文件夹，递归获取里面的文件
        		if(files[i].isDirectory()) {
        			
        		}
        	}
        	
        }
       

        System.out.println("Done!");
    }
}