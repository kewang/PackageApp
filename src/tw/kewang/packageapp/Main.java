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

		if (projectName == "") {
			System.out
					.println("Usage: java -jar [-e] -p projectName -d debugPackageName");
			System.exit(0);
		}

		generateProjectAndLocalProperties(projectName, debugPackageName);
		generateAntProperties();
		generateBuildXml(projectName);

		if (externalTools) {
			generateExternalTools(projectName, false);
			generateExternalTools(projectName, true);
		}
	}

	private static void generateProjectAndLocalProperties(String projectName,
			String debugPackageName) {
		try {
			String command = String.format(
					"android update project --name %s --path .", projectName);
			Runtime.getRuntime().exec(command).waitFor();
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					"project.properties"), true));

			new File("build.xml").delete();

			if (debugPackageName != "") {
				bw.write(String
						.format("packagename.debug=%s", debugPackageName));
				bw.newLine();
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
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
				bw.write(line);
				bw.newLine();
			}

			br.close();
			bw.flush();
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

				bw.write(line);
				bw.newLine();
			}

			br.close();
			bw.flush();
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

				bw.write(line);
				bw.newLine();
			}

			br.close();
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}