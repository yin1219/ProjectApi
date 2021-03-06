package com.example.ProjectApi.engineControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.DocFlavor.INPUT_STREAM;

import com.example.ProjectApi.engine.entity.Face;
import com.example.ProjectApi.util.CopyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetResult {
	static protected String ENGINEPATH = "C:\\eGroupAI_FaceEngine_v3.1.0";

	public static List<Face>  main(String args[]) {
		List<Face> faceList = new ArrayList<>();
		final Gson gson = new Gson();
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();

		// Get All Retrieve Data
		Date dNow = new Date();
		System.out.println("FLAG1");
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String date = (ft.format(dNow));
		System.out.println(date);
		Integer startIndex = 0;
		int count =0;
		String jsonName = "output." + date + ".egroup"; // Get All Retrieve Data
		boolean flag = true;
		while (flag) {
			long startTime = System.currentTimeMillis();
			faceList = getAllResult(ENGINEPATH, jsonName, startIndex);
			if (faceList.size() > 0) {
				startIndex = faceList.get(faceList.size() - 1).getEndIndex();
			}
			System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,startIndex="
					+ startIndex + ",faceList=" + new Gson().toJson(faceList));
//			faceList =gson.fromJson( new Gson().toJson(faceList), faceListType);
//			for (Face face : faceList) 
//			{
//				System.out.println(face.getPersonId());
//			}
			// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame =
			// 100 ms
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			flag=false;
		}
		return faceList;
		
		// Stop by yourself

//		//Get Real-time data
//		String cacheJsonName = "output.cache.egroup";	// Get Real-time data
//		while(true) {
//			long startTime = System.currentTimeMillis();
//			faceList = getCacheResult(ENGINEPATH,cacheJsonName);
//			System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,faceList="+new Gson().toJson(faceList));
//			// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame = 100 ms
//			try {
//				Thread.sleep(300);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		// Stop by yourself
	}

	/**
	 * Get Retrieve result json
	 * 
	 * @author Daniel
	 *
	 * @param jsonPath
	 * @param startIndex
	 * @return
	 */
	public static List<Face> getAllResult(String jsonPath,String jsonName,int startIndex) {
		// init func
		final Gson gson = new Gson();
		final CopyUtil copyUtil = new CopyUtil();
		
		// init variable
		final Type faceListType = new TypeToken<ArrayList<Face>>() {}.getType();
		List<Face> faceList = new ArrayList<Face>();
		List<Face> faceList2 = new ArrayList<Face>();
		
		// Get retrieve result
		final File sourceJson = new File(jsonPath.toString() + "/"+jsonName+".json");
		final StringBuilder jsonFileName = new StringBuilder(jsonPath + "/"+jsonName+"_copy.json");
		final File destJson = new File(jsonFileName.toString());
		if(sourceJson.exists()&&sourceJson.length()!=destJson.length()) {
			try {
				copyUtil.copyFile(sourceJson, destJson);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final File jsonFile = new File(jsonFileName.toString());
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;

			// If json exists
			if (jsonFile.exists()) {
				try {
					fileReader = new FileReader(jsonFileName.toString());
				} catch (FileNotFoundException e) {
				}
				bufferedReader = new BufferedReader(fileReader);
				// Read Json
				final StringBuilder jsonContent = new StringBuilder();
				String line;
				try {
					// Read line
					line = bufferedReader.readLine();
					while (line != null) {
						jsonContent.append(line + "\n");
						line = bufferedReader.readLine();
					}
					// If has data
					if (jsonContent.toString() != null) {
						faceList = gson.fromJson(jsonContent.toString(), faceListType);
					}
				} catch (IOException e) {
				} finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
					}
				}
			}
		}		
		return faceList;
	}
	
	public static List<Face> getAllDynamicResult(String jsonPath,String jsonName,int startIndex) {
		// init func
		final Gson gson = new Gson();
		final CopyUtil copyUtil = new CopyUtil();
		
		// init variable
		final Type faceListType = new TypeToken<ArrayList<Face>>() {}.getType();
		List<Face> faceList = new ArrayList<Face>();
		List<Face> faceList2 = new ArrayList<Face>();
		
		// Get retrieve result
		final File sourceJson = new File(jsonPath.toString() + "/"+jsonName+".json");
		final StringBuilder jsonFileName = new StringBuilder(jsonPath + "/"+jsonName+"_copy.json");
		final File destJson = new File(jsonFileName.toString());
		if(sourceJson.exists()&&sourceJson.length()!=destJson.length()) {
			try {
				copyUtil.copyFile(sourceJson, destJson);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final File jsonFile = new File(jsonFileName.toString());
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;

			// If json exists
			if (jsonFile.exists()) {
				try {
					fileReader = new FileReader(jsonFileName.toString());
				} catch (FileNotFoundException e) {
				}
				bufferedReader = new BufferedReader(fileReader);
				// Read Json
				final StringBuilder jsonContent = new StringBuilder();
				String line;
				try {
					// Read line
					line = bufferedReader.readLine();
					while (line != null) {
						jsonContent.append(line + "\n");
						line = bufferedReader.readLine();
					}
					// If has data
					if (jsonContent.toString() != null) {
						// Get last one object
						int endIndex = jsonContent.lastIndexOf("}\n\t,");
						String json;
						// Reorganization json
						if (endIndex != -1 && startIndex != endIndex && startIndex < endIndex) {
							if (startIndex > 0) {
								json = "[" + jsonContent.toString().substring(startIndex + 2, endIndex) + "}]";
							} else {
								json = jsonContent.toString().substring(startIndex, endIndex) + "}]";
							}
							System.out.println("json="+json);
							faceList = gson.fromJson(json, faceListType);				
							System.out.println("faceList2 size = " + faceList.size());
							faceList.get(faceList.size() - 1).setEndIndex(endIndex + 2);
							
						}
					}
				} catch (IOException e) {
				} finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
					}
				}
			}
		}		
		return faceList;
	}

	/**
	 * Get Retrieve result json
	 * 
	 * @author Daniel
	 *
	 * @param jsonPath
	 * @param startIndex
	 * @return
	 */
	public static List<Face> getCacheResult(String jsonPath, String jsonName) {
		// init func
		final Gson gson = new Gson();
		final CopyUtil copyUtil = new CopyUtil();

		// init variable
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		List<Face> faceList = new ArrayList<Face>();

		// Get retrieve result
		final File sourceJson = new File(jsonPath.toString() + "/" + jsonName + ".json");
		final StringBuilder jsonFileName = new StringBuilder(jsonPath + "/" + jsonName + "_copy.json");
		final File destJson = new File(jsonFileName.toString());
		if (sourceJson.exists() && sourceJson.length() != destJson.length()) {
			try {
				copyUtil.copyFile(sourceJson, destJson);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final File jsonFile = new File(jsonFileName.toString());
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;

			// If json exists
			if (jsonFile.exists()) {
				try {
					fileReader = new FileReader(jsonFileName.toString());
				} catch (FileNotFoundException e) {
				}
				bufferedReader = new BufferedReader(fileReader);
				// Read Json
				final StringBuilder jsonContent = new StringBuilder();
				String line;
				try {
					// Read line
					line = bufferedReader.readLine();
					while (line != null) {
						jsonContent.append(line + "\n");
						line = bufferedReader.readLine();
					}
					// If has data
					if (jsonContent.toString() != null) {
						// Get last one object
						final int endIndex = jsonContent.lastIndexOf("}\n\t,");
						final String json = jsonContent.toString().substring(0, endIndex) + "}]";
						faceList = gson.fromJson(json, faceListType);
					}
				} catch (IOException e) {
				} finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return faceList;
	}
}