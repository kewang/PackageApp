package tw.kewang.packageapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public static final boolean EXECUTE_JAR = false;

	public static void main(String[] args) {
		String projectName = "";
		String debugPackageName = "";
		String argMode = "";
		boolean externalTools = false;

		for (String arg : args) {
			switch (arg.charAt(0)) {
			case '-':
				argMode = String.valueOf(arg.charAt(1));

				if (argMode.equals("e")) {
					externalTools = true;
				}

				break;
			default:
				if (argMode.equals("p")) {
					projectName = arg;
				} else if (argMode.equals("d")) {
					debugPackageName = arg;
				}

				argMode = "";

				break;
			}
		}

		generateProjectProperties(debugPackageName);
		generateLocalProperties();
		generateAntProperties();
		generateBuildXml(projectName);

		if (externalTools) {
			generateExternalTools(projectName, false);
			generateExternalTools(projectName, true);
		}
	}

	private static void generateProjectProperties(String debugPackageName) {
		/*
		 * try { BufferedReader br = EXECUTE_JAR ? new BufferedReader( new
		 * InputStreamReader( Main.class.getClass().getResourceAsStream(
		 * "/template/project.properties"))) : new BufferedReader(new
		 * FileReader(new File( "template/project.properties"))); BufferedWriter
		 * bw = new BufferedWriter(new FileWriter(new File(
		 * "project.properties"))); String line;
		 * 
		 * while ((line = br.readLine()) != null) { if
		 * (line.contains("{DEBUG_PACKAGENAME}")) { if (debugPackageName != "")
		 * { line = line.replace("{DEBUG_PACKAGENAME}", debugPackageName); }
		 * else { line = ""; } }
		 * 
		 * bw.write(line + "\r\n"); }
		 * 
		 * br.close(); bw.close(); } catch (FileNotFoundException e) {
		 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		 */
		try {
			Process process = Runtime.getRuntime().exec(
					"android update project --path .");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateLocalProperties() {
		try {
			BufferedReader br = EXECUTE_JAR ? new BufferedReader(
					new InputStreamReader(Main.class.getClass()
							.getResourceAsStream("/template/local.properties")))
					: new BufferedReader(new FileReader(new File(
							"template/local.properties")));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					"local.properties")));
			String line;

			while ((line = br.readLine()) != null) {
				bw.write(line + "\r\n");
			}

			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateAntProperties() {
		try {
			BufferedReader br = EXECUTE_JAR ? new BufferedReader(
					new InputStreamReader(Main.class.getClass()
							.getResourceAsStream("/template/ant.properties")))
					: new BufferedReader(new FileReader(new File(
							"template/ant.properties")));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					"ant.properties")));
			String line;

			while ((line = br.readLine()) != null) {
				bw.write(line + "\r\n");
			}

			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateBuildXml(String projectName) {
		try {
			BufferedReader br = EXECUTE_JAR ? new BufferedReader(
					new InputStreamReader(Main.class.getClass()
							.getResourceAsStream("/template/build.xml")))
					: new BufferedReader(new FileReader(new File(
							"template/build.xml")));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					"build.xml")));
			String line;

			while ((line = br.readLine()) != null) {
				if (line.contains("{PROJECT_NAME}")) {
					line = line.replace("{PROJECT_NAME}", projectName);
				}

				bw.write(line + "\r\n");
			}

			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateExternalTools(String projectName, boolean debug) {
		try {
			BufferedReader br;
			BufferedWriter bw;

			if (debug) {
				br = EXECUTE_JAR ? new BufferedReader(new InputStreamReader(
						Main.class.getClass().getResourceAsStream(
								"/template/DebugPackageApp.launch")))
						: new BufferedReader(new FileReader(new File(
								"template/DebugPackageApp.launch")));
				bw = new BufferedWriter(new FileWriter(new File(
						"DebugPackageApp.launch")));
			} else {
				br = EXECUTE_JAR ? new BufferedReader(new InputStreamReader(
						Main.class.getClass().getResourceAsStream(
								"/template/ReleasePackageApp.launch")))
						: new BufferedReader(new FileReader(new File(
								"template/ReleasePackageApp.launch")));

				bw = new BufferedWriter(new FileWriter(new File(
						"ReleasePackageApp.launch")));
			}

			String line;

			while ((line = br.readLine()) != null) {
				if (line.contains("{PROJECT_NAME}")) {
					line = line.replace("{PROJECT_NAME}", projectName);
				}

				bw.write(line + "\r\n");
			}

			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
