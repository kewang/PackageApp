package tw.kewang.packageapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private static final String ANDROID_LIBRARY_REFERENCE = "android.library.reference";
	private static final boolean EXECUTE_JAR = true;

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
					.println("Usage: java -jar PackageApp.jar [-e] -p projectName [-d debugPackageName]");
			System.exit(0);
		}

		updateProject(projectName);
		generateProjectAndLocalProperties(projectName, debugPackageName);
		generateAntProperties();
		generateBuildXml(projectName);

		if (externalTools) {
			generateExternalTools(projectName, false);
			generateExternalTools(projectName, true);
		}
	}

	private static void updateProject(String projectName) {
		try {
			String command = String.format(
					"android update project --name %s --path .", projectName);
			Runtime.getRuntime().exec(command).waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateProjectAndLocalProperties(String projectName,
			String debugPackageName) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					"project.properties"), true));

			new File("build.xml").delete();

			if (EXECUTE_JAR) {
				copyAndExecuteJarToLibrary();
			}

			if (debugPackageName != "") {
				bw.write(String
						.format("packagename.debug=%s", debugPackageName));
				bw.newLine();
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void copyAndExecuteJarToLibrary() {
		try {
			String line;
			List<File> dirs = new ArrayList<File>();
			BufferedReader br = new BufferedReader(new FileReader(new File(
					"project.properties")));

			while ((line = br.readLine()) != null) {
				line = line.trim();

				if (!line.startsWith("#")
						&& line.contains(ANDROID_LIBRARY_REFERENCE)) {
					String[] keys = line.split("=");

					dirs.add(new File(keys[1]));
				}
			}
			br.close();

			String currentPath = Main.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();

			for (File dir : dirs) {
				copyFile(currentPath, dir.getCanonicalPath()
						+ "/PackageApp.jar");
			}
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

	private static void copyFile(String src, String dest) {
		try {
			FileChannel srcChannel = new FileInputStream(src).getChannel();
			FileChannel destChannel = new FileOutputStream(dest).getChannel();

			destChannel.transferFrom(srcChannel, 0, srcChannel.size());

			srcChannel.close();
			destChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}